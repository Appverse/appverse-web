/*
 Copyright (c) 2012 GFT Appverse, S.L., Sociedad Unipersonal.

 This Source Code Form is subject to the terms of the Appverse Public License
 Version 2.0 (“APL v2.0”). If a copy of the APL was not distributed with this
 file, You can obtain one at http://www.appverse.mobi/licenses/apl_v2.0.pdf. [^]

 Redistribution and use in source and binary forms, with or without modification,
 are permitted provided that the conditions of the AppVerse Public License v2.0
 are met.

 THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 DISCLAIMED. EXCEPT IN CASE OF WILLFUL MISCONDUCT OR GROSS NEGLIGENCE, IN NO EVENT
 SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT(INCLUDING NEGLIGENCE OR OTHERWISE)
 ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 POSSIBILITY OF SUCH DAMAGE.
 */
package org.appverse.web.framework.backend.ecm.filesystem.services.integration.impl.live;

import org.appverse.web.framework.backend.api.helpers.log.AutowiredLogger;
import org.appverse.web.framework.backend.ecm.core.model.integration.AbstractDocumentIntegrationBean;
import org.appverse.web.framework.backend.ecm.core.services.integration.IDocumentService;
import org.slf4j.Logger;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.FileSystemUtils;

import java.io.File;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.file.StandardCopyOption;

/**
 * This class provides a file system based implementation for IDocumentService
 *
 */
public class FileSystemDocumentService<T extends AbstractDocumentIntegrationBean> implements IDocumentService<T> {

    @AutowiredLogger
    private static Logger logger;

    @Override
    public void insert(String path, T document) throws Exception{
        File dir = new File(path);
        dir.mkdirs();
        File out = new File(dir, document.getContentStreamFilename());
        FileCopyUtils.copy(document.getContentStream(), out);
    }

    @Override
    public T retrieve(final String path, final String contentStreamFileName) throws Exception {
        File file = new File(path + "/" + contentStreamFileName);

        Class<T> classP = getClassP();
        T document = classP.newInstance();

        document.setContentStream(FileCopyUtils.copyToByteArray(file));
        document.setContentStreamFilename(contentStreamFileName);
        document.setContentStreamLenght(document.getContentStream().length);
        document.setContentStreamMimeType(document.getMimeTypeFromContentStreamFileName());
        return document;
    }

    @Override
    public void delete(String path, String documentName) throws Exception {
        String absolutePath = path + "/" + documentName;
        File file = new File(absolutePath);
        boolean success = file.delete();
        if (!success) {
            throw new Exception("Error deleting file");
        }
    }

    @Override
    public void move(String pathOrigin, String documentName, String pathDestination) throws Exception {
        move(pathOrigin, documentName, pathDestination, documentName);
    }

    @Override
    public void move(String pathOrigin, String documentNameOrigin, String pathDestination, String documentNameDestination) throws Exception{
        File originFile = new File(pathOrigin + "/" + documentNameOrigin);

        // Create destination structure if necessary
        File destinationFolder = new File(pathDestination);
        if (!destinationFolder.exists()) {
            if (!destinationFolder.mkdirs()) {
                throw new Exception(
                        "Error creating folder structure to path: "
                                + destinationFolder.toString());
            }
        }

        // Move the file
        File targetFile = new File(destinationFolder, documentNameDestination);
        try {
            java.nio.file.Files.move(originFile.toPath(),
                    targetFile.toPath(),
                    StandardCopyOption.ATOMIC_MOVE);
        } catch (Exception e) {
            throw new Exception("Error moving temporary file: "
                    + originFile.toString() + "to: "
                    + targetFile.toString());
        }
    }

    @Override
    public void deleteFolder(String path) throws Exception {
        File fileToTempDir = new File(path);
        FileSystemUtils.deleteRecursively(fileToTempDir);
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
