package org.appverse.web.framework.backend.ecm.cmis.services.integration;

import org.apache.chemistry.opencmis.client.api.Session;
import org.appverse.web.framework.backend.api.model.integration.AbstractIntegrationBean;
import org.appverse.web.framework.backend.ecm.core.services.integration.IDocumentService;

/**
 * Base implementation service that provides simple operations for non versioned documents.
 * Basically provides the basic operations that a file system would provide.
 */
public interface ICMISSimpleNonVersionedDocumentService<T extends AbstractIntegrationBean> extends IDocumentService<T>, ICMISService<T>{

    void insert(String path, T document, Session session) throws Exception;

    T retrieve(final String path, final String documentName, Session session) throws Exception;

    void move(String pathOrigin, String documentName, String pathDestination, Session session) throws Exception;

    void move(String pathOrigin, String documentNameOrigin, String pathDestination, String documentNameDestination, Session session) throws Exception;

    void delete(final String path, final String documentName, Session session) throws Exception;

    void deleteFolder(String path, Session session) throws Exception;

}
