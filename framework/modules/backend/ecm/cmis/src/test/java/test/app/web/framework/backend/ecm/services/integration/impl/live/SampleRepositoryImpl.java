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
package test.app.web.framework.backend.ecm.services.integration.impl.live;

import org.apache.chemistry.opencmis.client.api.*;
import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.appverse.web.framework.backend.ecm.cmis.services.integration.impl.live.CMISService;
import org.springframework.stereotype.Repository;
import test.app.web.framework.backend.ecm.model.integration.NodeDTO;
import test.app.web.framework.backend.ecm.services.integration.SampleRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is a sample repository (integration layer service) that have two methods:
 * One retrieves all the "nodes" (documents, folders) from the root folder.
 * The other one retrieves all the nodes in folders with a particular name.
 * Is just an example showing how you could implement your own repository methods
 * using Open CMIS API
 */
@Repository("sampleRepository")
public class SampleRepositoryImpl extends CMISService<NodeDTO>
        implements SampleRepository {

    @Override
    public List<NodeDTO> getRootFolderNodes() throws Exception {
        List<NodeDTO> nodeDTOList = new ArrayList<NodeDTO>();
        Folder root = getCmisSession().getRootFolder();
        ItemIterable<CmisObject> children = root.getChildren();
        NodeDTO node;
        for (CmisObject o : children) {
            node = new NodeDTO();
            node.setName(o.getName());
            nodeDTOList.add(node);
        }
        return nodeDTOList;
    }

    @Override
    public List<NodeDTO> getNodesfromFolderUsingQuery(String folderName) throws Exception {
        return getNodesfromFolderUsingQuery(folderName, getCmisSession());
    }

    @Override
    public List<NodeDTO> getNodesfromFolderUsingQuery(String folderName, Session session) throws Exception {
        List<NodeDTO> nodeDTOList = new ArrayList<NodeDTO>();
        ItemIterable<QueryResult> results = session.query("SELECT * FROM cmis:folder WHERE cmis:name='" + folderName + "'", false);
        NodeDTO node;
        for (QueryResult result : results) {
            node = new NodeDTO();
            String id = result.getPropertyValueById(PropertyIds.OBJECT_ID);
            node.setName((String)result.getPropertyValueById(PropertyIds.NAME));
            nodeDTOList.add(node);
        }
        return nodeDTOList;
    }

}