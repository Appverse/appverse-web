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
package org.appverse.web.framework.backend.bpm.services.integration.impl.live;

import org.appverse.web.framework.backend.api.helpers.log.AutowiredLogger;
import org.appverse.web.framework.backend.api.services.business.AbstractBusinessService;
import org.appverse.web.framework.backend.bpm.services.integration.IBpmService;
import org.bonitasoft.engine.api.LoginAPI;
import org.bonitasoft.engine.api.ProcessAPI;
import org.bonitasoft.engine.api.TenantAPIAccessor;
import org.bonitasoft.engine.bpm.data.DataDefinition;
import org.bonitasoft.engine.bpm.flownode.*;
import org.bonitasoft.engine.bpm.process.ProcessDefinition;
import org.bonitasoft.engine.bpm.process.ProcessDeploymentInfo;
import org.bonitasoft.engine.bpm.process.ProcessInstance;
import org.bonitasoft.engine.bpm.process.ProcessInstanceSearchDescriptor;
import org.bonitasoft.engine.identity.User;
import org.bonitasoft.engine.search.SearchOptionsBuilder;
import org.bonitasoft.engine.search.SearchResult;
import org.bonitasoft.engine.session.APISession;
import org.slf4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import org.springframework.web.context.WebApplicationContext;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository("bpmService")
@Scope(WebApplicationContext.SCOPE_SESSION)
public class BpmService extends AbstractBusinessService implements IBpmService {

    @AutowiredLogger
    private static Logger logger;

    private APISession userSession = null;

    private String userName;

    @Override
    public void login(String userName, String password) throws Exception {
        this.userName = userName;
        final LoginAPI loginAPI = TenantAPIAccessor.getLoginAPI();
        userSession = loginAPI.login(userName, password);
        userName = userSession.getUserName();
        logger.info("Logged in [{}] to BPM Platform.", userName);
    }

    @Override
    public void logout() throws Exception {
        final LoginAPI loginAPI = TenantAPIAccessor.getLoginAPI();
        if (userSession != null) {
            loginAPI.logout(userSession);
        }else{
            logger.warn("Already logged out");
        }
        userSession = null;
        logger.info("Logged out [{}] from BPM Platform.", userName);
    }

    @Override
    public List<HumanTaskInstance> obtainCurrentFlows(long processDefinitionId, int start, int maxRecords) throws Exception {
        if( userSession == null ) {
            throw new IllegalStateException("UserSession not established. Please login first.");
        }
        ProcessAPI processAPI = TenantAPIAccessor.getProcessAPI(userSession);

        SearchOptionsBuilder criteria = new SearchOptionsBuilder(start,maxRecords);
        criteria.filter(HumanTaskInstanceSearchDescriptor.PROCESS_DEFINITION_ID, processDefinitionId);

        SearchResult<HumanTaskInstance> list = processAPI.searchPendingTasksForUser(userSession.getUserId(), criteria.done());
        return list.getResult();
    }

    @Override
    public List<ProcessDeploymentInfo> getDeployedProcesses() throws Exception {
        if( userSession == null ) {
            throw new IllegalStateException("UserSession not established. Please login first.");
        }
        ProcessAPI processAPI = TenantAPIAccessor.getProcessAPI(userSession);
        long userId = userSession.getUserId();
        SearchOptionsBuilder builder = new SearchOptionsBuilder(0,20);
        final SearchResult<ProcessDeploymentInfo> deploymentInfoResults = processAPI.searchProcessDeploymentInfos(userId, builder.done());
        List<ProcessDeploymentInfo> lDeployments = deploymentInfoResults.getResult();
        for( ProcessDeploymentInfo processDeploymentInfo:lDeployments) {
            logger.debug("   Process deployed id[{}] name [{}] ", processDeploymentInfo.getId(), processDeploymentInfo.getName()) ;
            logger.debug("description [{}] version [{}]", processDeploymentInfo.getDescription(), processDeploymentInfo.getVersion());
            //it is already enabled.... don't know what will happen with a production environment.
            //System.out.println("   enabling process..."+processDeploymentInfo.getProcessId());
            //processAPI.enableProcess(processDeploymentInfo.getProcessId());

            //ProcessDefinition pd = processAPI.getProcessDefinition(processDeploymentInfo.getProcessId());
            //System.out.println("Process Definition Id ["+pd.getId()+"]");

            //long totalTasks = processAPI.getNumberOfAssignedHumanTaskInstances(userId);
            //System.out.println("User ["+userId+"] has "+totalTasks+" tasks.");

            //processAPI.getOneAssignedUserTaskInstanceOfProcessInstance();

            //The following are based on running instances!
            //System.out.println("   starting process..."+processDeploymentInfo.getProcessId());
            //ProcessInstance pInstance = processAPI.startProcess(processDeploymentInfo.getProcessId());
            //System.out.println("A new process instance was started with id: " + pInstance.getId());

            List<ActivityInstance> activityInstances = processAPI.getActivities(processDeploymentInfo.getProcessId(), 0, 100);
            for( ActivityInstance activityInstance: activityInstances) {
                logger.debug("Activity {}", activityInstance.getDisplayName());

            }
            //processDeploymentInfo.getConfigurationState().

        }
        return lDeployments;
    }

    @Override
    public long getProcessDefinitionIdByNameVersion(String processDefinitionName, String processVersion) throws Exception {
        if( userSession == null ) {
            throw new IllegalStateException("UserSession not stablished. Please call loadSession first.");
        }
        ProcessAPI processAPI = TenantAPIAccessor.getProcessAPI(userSession);
        return processAPI.getProcessDefinitionId(processDefinitionName, processVersion);
    }

    @Override
    public long createCase(String processDefinitionName, String processVersion, Map<String, Serializable> variables) throws Exception {
        ProcessAPI processAPI = TenantAPIAccessor.getProcessAPI(userSession);
        long processDefinitionId = processAPI.getProcessDefinitionId(processDefinitionName, processVersion);
        return createCase(processDefinitionId, variables);
    }

    @Override
    public long createCase(long processDefinitionId, Map<String, Serializable> variables) throws Exception {
        if( userSession == null ) {
            throw new Exception("UserSession not stablished. Please call loadSession first.");
        }
        ProcessAPI processAPI = TenantAPIAccessor.getProcessAPI(userSession);

        ProcessDefinition def = processAPI.getProcessDefinition(processDefinitionId);

        if (variables==null){
            variables = new HashMap<String, Serializable>();
        }

        ProcessInstance pi = processAPI.startProcess(processDefinitionId, variables);

        logger.info("create case username:'{}' processId:{} process:{}",new Object[] {userSession.getUserName(),pi.getId(), def!=null?def.getName():null});
        return pi.getId();
    }


    @Override
    public List<ProcessInstance> getProcessInstancesByProcessDefinition(String processDefinitionName, String processVersion, int start, int maxRecords) throws Exception {
        if( userSession == null ) {
            throw new IllegalStateException("UserSession not stablished. Please call loadSession first.");
        }
        ProcessAPI processAPI = TenantAPIAccessor.getProcessAPI(userSession);
        long processDefinitionId = processAPI.getProcessDefinitionId(processDefinitionName, processVersion);
        return getProcessInstancesByProcessDefinition(processDefinitionId, start, maxRecords);
    }

    @Override
    public List<ProcessInstance> getProcessInstancesByProcessDefinition(long processDefinitionId, int start, int maxRecords) throws Exception {
        long userId = userSession.getUserId();
        ProcessAPI processAPI = TenantAPIAccessor.getProcessAPI(userSession);
        final SearchOptionsBuilder builder = new SearchOptionsBuilder(start, maxRecords);
        builder.filter(ProcessInstanceSearchDescriptor.PROCESS_DEFINITION_ID, processDefinitionId);
        final SearchResult<ProcessInstance> processInstanceResults = processAPI.searchOpenProcessInstances(builder.done());
        List<ProcessInstance> processInstances = processInstanceResults.getResult();
        for( ProcessInstance processInstance: processInstances) {
            logger.debug(" Process Instance [{}][{}]", processInstance.getId(), processInstance.getName());
        }
        return processInstances;
    }

    @Override
    public List<ActivityInstance> getActivityInstancesByProcesInstance(long processInstanceId) throws Exception {
        if( userSession == null ) {
            throw new IllegalStateException("UserSession not stablished. Please call loadSession first.");
        }
        ProcessAPI processAPI = TenantAPIAccessor.getProcessAPI(userSession);
        List<ActivityInstance> activityInstances = processAPI.getActivities(processInstanceId, 0, 50);
        logger.debug("List of activities for Process INSTANCE Id={}", processInstanceId);
        for(ActivityInstance activityInstance: activityInstances) {
            logger.debug("Activity [" + activityInstance.getId() + "]" +
                    "Name [" + activityInstance.getName() + "]" + activityInstance.getClass() + "[" + activityInstance + "]");
        }
        return activityInstances;
    }

    @Override
    public List<DataDefinition> getDataDefinitionsByActivity(String processDefinitionName,
                                                             String processVersion,
                                                             String activityName,
                                                             int startIndex, int maxResults) throws Exception {
        if( userSession == null ) {
            throw new IllegalStateException("UserSession not stablished. Please call loadSession first.");
        }
        ProcessAPI processAPI = TenantAPIAccessor.getProcessAPI(userSession);
        long processDefinitionId = processAPI.getProcessDefinitionId(processDefinitionName, processVersion);
        List<DataDefinition> dataDefinitions = processAPI.getActivityDataDefinitions(processDefinitionId,
                activityName, startIndex, maxResults);
        for( DataDefinition dataDefinition: dataDefinitions) {
            logger.debug("DataDefinition [" + dataDefinition + "]", dataDefinition);
            /** TODO -> Check this
             * Some doubts here:
             *    for onBoarding, the activity "resgister new employee":
             *       - using bpm portal, I can see typical employee details {first name, last name, email, mobile, date of birth}
             *       - using API, I can see:
             *          DataDefinition [DataDefinitionImpl [name=dateOfBirth, description=null, type=null, transientData=false, className=java.util.Date, defaultValueExpression=ExpressionImpl [name=null, content=null, expressionType=null, returnType=null, interpreter=null, dependencies=[]]]]
             *          DataDefinition [DataDefinitionImpl [name=salary, description=null, type=null, transientData=false, className=java.lang.Integer, defaultValueExpression=ExpressionImpl [name=null, content=null, expressionType=null, returnType=null, interpreter=null, dependencies=[]]]]
             *     seems like the API is not getting all necessary info.
             *
             *  Looking at BPM Bonita Studio, this is normal, as for the activity register new employee, only those data are defined.
             *  Other used data in this activity, are defined at process level -> see getDataDefinitionsByProcessDefinition.
             */
        }
        return dataDefinitions;
    }

    @Override
    public List<DataDefinition> getDataDefinitionsByProcessDefinition(String processDefinitionName,
                                                                      String processVersion,
                                                                      int startIndex, int maxResults) throws Exception {
        if( userSession == null ) {
            throw new IllegalStateException("UserSession not stablished. Please call loadSession first.");
        }
        ProcessAPI processAPI = TenantAPIAccessor.getProcessAPI(userSession);
        long processDefinitionId = processAPI.getProcessDefinitionId(processDefinitionName, processVersion);

        List<DataDefinition> dataDefinitions = processAPI.getProcessDataDefinitions(processDefinitionId, startIndex, maxResults);
        for( DataDefinition dataDefinition: dataDefinitions) {
            logger.info("DataDefinition [{}]", dataDefinition);
        }
        return dataDefinitions;
    }

    public void assignTaskToUser(long taskId, long userId) throws Exception {
        if( userSession == null ) {
            throw new IllegalStateException("UserSession not stablished. Please call loadSession first.");
        }
        User user = TenantAPIAccessor.getIdentityAPI(userSession).getUser(userId);
        String username = user!=null?user.getUserName():String.valueOf(userId);
        TenantAPIAccessor.getProcessAPI(userSession).assignUserTask(taskId, userId);
        logger.info("assigned task to user username:{} taskId:{}'",username , taskId);
    }

    public void assignTaskToCurrentUser(long taskId) throws Exception {
        assignTaskToUser(taskId, userSession.getUserId());
    }

    public void executeTaskFlowNode(long flowNodeId) throws Exception{
        executeTaskFlowNode(flowNodeId, null);
    }

    public void executeTaskFlowNode(long flowNodeId, Map<String,Serializable> variables) throws Exception {
        executeTaskFlowNode(flowNodeId, variables, null);
    }

    public void executeTaskFlowNode(long flowNodeId, Map<String,Serializable> variables, Map<String,Object> documents) throws Exception {
        if( userSession == null ) {
            throw new IllegalStateException("UserSession not stablished. Please call loadSession first.");
        }
        ProcessAPI processApi = TenantAPIAccessor.getProcessAPI(userSession);
        //update variables
        if (variables!=null){
            for (String variableName : variables.keySet()) {
                processApi.updateActivityDataInstance(variableName, flowNodeId, variables.get(variableName));
            }
        }
        //attach documents
        if (documents!=null){
            for (String documentName : documents.keySet()) {
                Object data = documents.get(documentName);
                if (data instanceof ByteArrayOutputStream){
                    processApi.attachDocument(flowNodeId, documentName, documentName, "application/pdf", ((ByteArrayOutputStream) data).toByteArray());
                }else{
                    processApi.attachDocument(flowNodeId, documentName, documentName, "application/pdf", (String)data);
                }

            }
        }

        FlowNodeInstance flow = processApi.getFlowNodeInstance(flowNodeId);
        if (!processApi.canExecuteTask(flowNodeId,userSession.getUserId())){
            logger.error("task can't be executed by username:{} taskId:{}'",userName , flowNodeId);
            throw new Exception("flowNodeId:"+flowNodeId+" can't be executed by user:"+userSession.getUserName());
        }
        processApi.executeFlowNode(userSession.getUserId(),flowNodeId);
        logger.info("executed username:{} taskId:{} process:{}'",new Object[]{userName , flowNodeId, flow.getName()});
    }

    public void addCommentToProcessInstance(long processInstanceId, String comment) throws Exception {
        logger.info("add comment username:{} processInstanceId:{}'",userName , processInstanceId);
        TenantAPIAccessor.getProcessAPI(userSession).addProcessComment(processInstanceId, comment);
    }

    public List<HumanTaskInstance> getHumanTaskInstances() throws Exception {
        if( userSession == null ) {
            throw new IllegalStateException("UserSession not stablished. Please call loadSession first.");
        }
        ProcessAPI processAPI = TenantAPIAccessor.getProcessAPI(userSession);
        List<HumanTaskInstance> humanTaskInstances = processAPI.getPendingHumanTaskInstances(userSession.getUserId(), 0,100, ActivityInstanceCriterion.PRIORITY_DESC);
        for( HumanTaskInstance humanTaskInstance: humanTaskInstances) {
            logger.info("Human Task Instance [{},{}]",humanTaskInstance.getId(),humanTaskInstance.getName());
        }
        return humanTaskInstances;
    }


    public void test(String processDefinitionName, String processVersion, String activityName, int startIndex, int maxResults) throws Exception {
        if( userSession == null ) {
            throw new IllegalStateException("UserSession not stablished. Please call loadSession first.");
        }
        ProcessAPI processAPI = TenantAPIAccessor.getProcessAPI(userSession);
        List<HumanTaskInstance> humanTaskInstances = processAPI.getPendingHumanTaskInstances(userSession.getUserId(), 0,100, ActivityInstanceCriterion.PRIORITY_DESC);
        for( HumanTaskInstance humanTaskInstance: humanTaskInstances) {
            logger.info("Human Task Instance [{},{}]",humanTaskInstance.getId(),humanTaskInstance.getName());
            assignTaskToCurrentUser(humanTaskInstance.getId());
            executeTaskFlowNode(humanTaskInstance.getId());
        }

        /*long processDefinitionId = processAPI.getProcessDefinitionId(processDefinitionName, processVersion);

        List<DataDefinition> dataDefinitions = processAPI.getProcessDataDefinitions(processDefinitionId, startIndex, maxResults);
        for( DataDefinition dataDefinition: dataDefinitions) {
            System.out.println("DataDefinition ["+dataDefinition+"]");
        }*/
    }

    public void test(long processInstanceId) throws Exception {
        if( userSession == null ) {
            throw new IllegalStateException("UserSession not stablished. Please call loadSession first.");
        }
        long userId = userSession.getUserId();
        ProcessAPI processAPI = TenantAPIAccessor.getProcessAPI(userSession);
        List<ActivityInstance> activityInstances = processAPI.getActivities(processInstanceId, 0, 50);
        logger.info("List of activities for Process INSTANCE Id="+processInstanceId);
        for(ActivityInstance activityInstance: activityInstances) {
            logger.info("Activity [{}] Name [{}]",activityInstance.getId(), activityInstance.getName());
        }
        //long taskInstanceId = processAPI.getActivityOneAssignedUserTaskInstanceOfProcessInstance(processInstanceId, userId);
        //System.out.println("getOneAssignedUserTaskInstanceOfProcessInstance:"+taskInstanceId);
        //HumanTaskInstance hti = processAPI.getTaskInstance(taskInstanceId);
        //System.out.println("Task Instance is :"+hti.getName());
    }

    public ProcessAPI getProcessAPI (String userName, String password) throws Exception {
        if( userSession == null ) {
            throw new IllegalStateException("UserSession not stablished. Please call loadSession first.");
        }
        long userId = userSession.getUserId();
        ProcessAPI processAPI = TenantAPIAccessor.getProcessAPI(userSession);
        SearchOptionsBuilder builder = new SearchOptionsBuilder(0,20);
        final SearchResult<ProcessDeploymentInfo> deploymentInfoResults = processAPI.searchProcessDeploymentInfos(userId, builder.done());
        List<ProcessDeploymentInfo> lDeployments = deploymentInfoResults.getResult();
        for( ProcessDeploymentInfo processDeploymentInfo:lDeployments) {
            System.out.println("   Process deployed" + " id["+processDeploymentInfo.getId()+"]"+
                    " name ["+processDeploymentInfo.getName()+ "]" +
                    " description ["+processDeploymentInfo.getDescription()+"]") ;
            //it is already enabled.... don't know what will happen with a production environment.
            //System.out.println("   enabling process..."+processDeploymentInfo.getProcessId());
            //processAPI.enableProcess(processDeploymentInfo.getProcessId());
            logger.info("   starting process...{}",processDeploymentInfo.getProcessId());
            ProcessInstance pInstance = processAPI.startProcess(processDeploymentInfo.getProcessId());
            logger.info("A new process instance was started with id: {}", pInstance.getId());
        }
        /*
        SearchResult<HumanTaskInstance> searchResults = processAPI.searchMyAvailableHumanTasks(userId, builder.done());
        System.out.println("There are [" + searchResults.getCount() + "] available tasks for user [" + userId + "]");
        List<HumanTaskInstance> lHumanTask = searchResults.getResult();
        for( HumanTaskInstance humanTask: lHumanTask) {
            processAPI.executeFlowNode(humanTask.getId());
            //this fails saying the activity is not yet assigned.... need a bit more of investigation
        } */
//        loginAPI.logout(session);
        return processAPI;
    }


}
