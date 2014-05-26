package org.appverse.web.framework.backend.ecm.cmis.services.integration.impl.live;

import org.apache.chemistry.opencmis.client.api.*;
import org.apache.chemistry.opencmis.client.util.FileUtils;
import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.apache.chemistry.opencmis.commons.data.ContentStream;
import org.apache.chemistry.opencmis.commons.enums.UnfileObject;
import org.apache.chemistry.opencmis.commons.enums.VersioningState;
import org.apache.chemistry.opencmis.commons.exceptions.CmisObjectNotFoundException;
import org.apache.chemistry.opencmis.commons.impl.dataobjects.ContentStreamImpl;
import org.apache.commons.io.IOUtils;
import org.appverse.web.framework.backend.api.helpers.log.AutowiredLogger;
import org.appverse.web.framework.backend.ecm.core.model.integration.AbstractDocumentIntegrationBean;
import org.appverse.web.framework.backend.ecm.core.services.integration.IDocumentService;
import org.slf4j.Logger;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

public class CMISSimpleNonVersionedDocumentService<T extends AbstractDocumentIntegrationBean> extends CMISService<T>
        implements IDocumentService<T>{

    @AutowiredLogger
    private static Logger logger;


    @Override
    public void addDocument(String path, T document) throws Exception{
        Document test = createDocument(path, document);
        FileUtils.printProperties(test);
    }

    @Override
    public T retrieveDocument(final String path,
                                      final String documentName) throws Exception {
        Document object = (Document)FileUtils.getObject(path  + documentName, cmisSession);
        ContentStream contentStream = object.getContentStream();

        Class<T> classP = getClassP();
        T document = classP.newInstance();

        document.setContentStream(IOUtils.toByteArray(contentStream.getStream()));
        document.setContentStreamFilename(documentName);
        document.setContentStreamLenght(contentStream.getLength());
        document.setContentStreamMimeType(contentStream.getMimeType());
        return document;
    }

    @Override
    public void moveDocument(String pathOrigin, String documentName, String pathDestination) throws Exception {
        moveDocument(pathOrigin, documentName, pathDestination, documentName);
    }

    // TODO: destination name is not being used
    @Override
    public void moveDocument(String pathOrigin, String documentNameOrigin, String pathDestination, String documentNameDestination) throws Exception {
        // Disable cache
        cmisSession.getDefaultContext().setCacheEnabled(false);

        // Fetch the object
        FileableCmisObject fileableCmisObject = (FileableCmisObject) FileUtils.getObject(pathOrigin + documentNameOrigin, cmisSession);

        // ECM folder paths does not end with "/"
        if (pathOrigin.endsWith("/")) pathOrigin = pathOrigin.substring(0, pathOrigin.length()-1);
        if (pathDestination.endsWith("/")) pathDestination = pathDestination.substring(0, pathDestination.length()-1);

        // Fetch source folder
        CmisObject sourceObject = FileUtils.getObject(pathOrigin, cmisSession);

        // Fetch the destination folder
        // We need to make sure the target folder exists
        createFolder(pathDestination);
        CmisObject targetObject = FileUtils.getObject(pathDestination, cmisSession);

        // Move the object
        fileableCmisObject.move(sourceObject, targetObject);

        // Enable cache
        cmisSession.getDefaultContext().setCacheEnabled(true);
    }

    @Override
    public void removeDocument(String path, String documentName) throws Exception {
        // Disable cache
        cmisSession.getDefaultContext().setCacheEnabled(false);

        Document document = (Document)FileUtils.getObject(path + "/" + documentName, cmisSession);
        document.delete();

        // Enable cache
        cmisSession.getDefaultContext().setCacheEnabled(true);
    }

    @Override
    public void removeFolder(String path) throws Exception {
        // Disable cache
        cmisSession.getDefaultContext().setCacheEnabled(false);

        if (path.endsWith("/")) path = path.substring(0, path.length()-1);
        Folder folder = (Folder)FileUtils.getObject(path, cmisSession);
        folder.deleteTree(true, UnfileObject.DELETE, true);

        // Enable cache
        cmisSession.getDefaultContext().setCacheEnabled(true);

    }

    private Folder createFolder(String path){
        String[] subFolderNames = path.split("/");
        Folder folder = cmisSession.getRootFolder();
        for (String subfolder : subFolderNames){
            String folderPath = folder.getPath();
            if (folder.getPath().equals("/")){
                folderPath = "";
            }
            try{
                folder = FileUtils.getFolder(folderPath + "/" + subfolder, cmisSession);
                logger.debug("Parent folder already exists, new folder value is: " + folder.getPath());
            }
            catch (CmisObjectNotFoundException e){
                // The folder does not existe, we create it here
                folder = FileUtils.createFolder(folder, subfolder, null);
                logger.debug("New folder created, new folder value is: " + folder.getPath());
            }
        }
        return folder;
    }

    private Document createDocument(String path, T document){
        // Disable cache
        cmisSession.getDefaultContext().setCacheEnabled(false);

        Folder parent = createFolder(path);

        // CMIS properties
        // (minimal set: name and object type id)
        Map<String, Object> properties = new HashMap<String, Object>();
        properties.put(PropertyIds.OBJECT_TYPE_ID, "cmis:document");
        properties.put(PropertyIds.NAME, document.getContentStreamFilename());

        // content
        String mimeType = document.getContentStreamMimeType();
        if (mimeType == null){
            mimeType = document.getMimeTypeFromContentStreamFileName();
        }
        InputStream stream = new ByteArrayInputStream(document.getContentStream());
        ContentStream contentStream = new ContentStreamImpl(document.getContentStreamFilename(), BigInteger.valueOf(document.getContentStream().length), mimeType, stream);

        // If the document existed already we override it. In order to do so, we have to remove it and we create it again
        Document documentToRemove = null;
        try{
            documentToRemove = (Document)FileUtils.getObject(path + document.getContentStreamFilename(), cmisSession);
            documentToRemove.delete();
        }
        catch (CmisObjectNotFoundException e){
            // The document does not exist
        }

        // Determine the VersioningState
        VersioningState versioningState = VersioningState.NONE;
        String typeId = (String)properties.get(PropertyIds.OBJECT_TYPE_ID);
        DocumentType docType =
                (DocumentType) cmisSession.getTypeDefinition(typeId);
        if (Boolean.TRUE.equals(docType.isVersionable())) {
            versioningState = VersioningState.MAJOR;
        }

        // Depending on the underlaying repository a "cmis:document" can be versionable or not. If it is versionable, it is mandatory to create a version.
        // For instance, in OpenCMIS in-memory repository a "cmis:document" is not versionable (accepts documents without versioning).
        // Alfresco repository "cmis:document" is versionable (requires a version)
        Document doc = parent.createDocument(properties, contentStream, versioningState);

        // Enable cache again
        cmisSession.getDefaultContext().setCacheEnabled(true);
        return doc;
    }

    private Class<T> getClassP() {
        Class<T> classP = null;
        final Type type = this.getClass().getGenericSuperclass();
        if (type instanceof ParameterizedType) {
            final ParameterizedType pType = (ParameterizedType) type;
            if (pType.getActualTypeArguments()[0] instanceof Class) {
                classP = (Class<T>) pType.getActualTypeArguments()[0];
            }
        }
        return classP;
    }

}