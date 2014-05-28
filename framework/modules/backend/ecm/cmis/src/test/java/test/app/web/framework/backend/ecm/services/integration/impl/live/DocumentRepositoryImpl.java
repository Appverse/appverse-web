
package test.app.web.framework.backend.ecm.services.integration.impl.live;

import org.appverse.web.framework.backend.ecm.cmis.services.integration.impl.live.CMISSimpleNonVersionedDocumentService;
import org.appverse.web.framework.backend.ecm.core.model.integration.DocumentDTO;
import org.springframework.stereotype.Repository;
import test.app.web.framework.backend.ecm.services.integration.DocumentRepository;


@Repository("documentRepository")
public class DocumentRepositoryImpl extends CMISSimpleNonVersionedDocumentService<DocumentDTO>
    implements DocumentRepository {

}