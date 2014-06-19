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
package org.appverse.web.framework.backend.bpm.services.integration;


import org.bonitasoft.engine.bpm.data.DataDefinition;
import org.bonitasoft.engine.bpm.flownode.ActivityInstance;
import org.bonitasoft.engine.bpm.flownode.HumanTaskInstance;
import org.bonitasoft.engine.bpm.process.ProcessDeploymentInfo;
import org.bonitasoft.engine.bpm.process.ProcessInstance;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface IBpmService {

    void login(String userName, String password) throws Exception;
    void logout() throws Exception;

    /**
     * Lists the process instances of a specified process definition for current user
     * @param processDefinitionName
     * @param processVersion
     * @param start
     * @param maxRecords
     * @return
     * @throws Exception
     */

    List<HumanTaskInstance> obtainCurrentFlows(long processDefinitionId, int start, int maxRecords) throws Exception;
    List<ProcessInstance> getProcessInstancesByProcessDefinition(String processDefinitionName, String processVersion, int start, int maxRecords) throws Exception;

    List<ProcessInstance> getProcessInstancesByProcessDefinition(long processDefinitionId, int start, int maxRecords) throws Exception;

    List<ProcessDeploymentInfo> getDeployedProcesses() throws Exception;

    long getProcessDefinitionIdByNameVersion(String processDefinitionName, String processVersion) throws Exception;

    List<DataDefinition> getDataDefinitionsByActivity(String processDefinitionName,
                                                             String processVersion,
                                                             String activityName,
                                                             int startIndex, int maxResults) throws Exception;

    List<DataDefinition> getDataDefinitionsByProcessDefinition(String processDefinitionName,
                                                                      String processVersion,
                                                                      int startIndex, int maxResults) throws Exception;

    long createCase(long processDefinitionId, Map<String, Serializable> variables) throws Exception;

    long createCase(String processDefinitionName, String processVersion, Map<String, Serializable> variables) throws Exception;

    /**
     * Retrieve a list of Activity Instances by process instance id.
     * Those activities are not necessarily assigned to a specific user.
     * @param processInstanceId
     * @return
     * @throws Exception
     */
    List<ActivityInstance> getActivityInstancesByProcesInstance(long processInstanceId) throws Exception;

    /**
     * Retrieves a list of available Human tasks.
     * A Task is HumanTaskInstance while it is not assigned to a user. At that point it becomes a UserTaskInstance.
     * @return
     * @throws Exception
     */
    List<HumanTaskInstance> getHumanTaskInstances() throws Exception;


    void assignTaskToUser(long taskId, long userId) throws Exception;

    void assignTaskToCurrentUser(long taskId) throws Exception;

    void executeTaskFlowNode(long taskId) throws Exception;
    void executeTaskFlowNode(long flowNodeId, Map<String,Serializable> variables) throws Exception;
    void executeTaskFlowNode(long flowNodeId, Map<String,Serializable> variables, Map<String,Object> documents) throws Exception;


    void addCommentToProcessInstance(long processInstanceId, String comment) throws Exception;

    //public ProcessAPI getProcessAPI(APISession session, String userName, String password) throws Exception;

    //public List<ProcessDeploymentInfo> get

    void test(String processDefinitionName, String processVersion, String activityName, int startIndex, int maxResults) throws Exception;
    void test(long processInstanceId) throws Exception;
}
