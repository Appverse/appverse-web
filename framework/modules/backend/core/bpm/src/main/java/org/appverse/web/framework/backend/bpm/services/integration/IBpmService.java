package org.appverse.web.framework.backend.bpm.services.integration;

import org.bonitasoft.engine.api.ProcessAPI;
import org.bonitasoft.engine.bpm.data.DataDefinition;
import org.bonitasoft.engine.bpm.flownode.ActivityInstance;
import org.bonitasoft.engine.bpm.flownode.HumanTaskInstance;
import org.bonitasoft.engine.bpm.process.ProcessDeploymentInfo;
import org.bonitasoft.engine.bpm.process.ProcessInstance;
import org.bonitasoft.engine.session.APISession;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: RRBL
 * Date: 13/02/14
 * Time: 9:55
 * To change this template use File | Settings | File Templates.
 */
public interface IBpmService {

    public void login(String userName, String password) throws Exception;

    public void logout() throws Exception;

    /**
     * Lists the process instances of a specified process definition for current user
     * @param processDefinitionName
     * @param processVersion
     * @param start
     * @param maxRecords
     * @return
     * @throws Exception
     */
    public List<ProcessInstance> getProcessInstancesByProcessDefinition(String processDefinitionName, String processVersion, int start, int maxRecords) throws Exception;

    public List<ProcessInstance> getProcessInstancesByProcessDefinition(long processDefinitionId, int start, int maxRecords) throws Exception;

    public List<ProcessDeploymentInfo> getDeployedProcesses() throws Exception;

    public long getProcessDefinitionIdByNameVersion(String processDefinitionName, String processVersion) throws Exception;

    public List<DataDefinition> getDataDefinitionsByActivity(String processDefinitionName,
                                                             String processVersion,
                                                             String activityName,
                                                             int startIndex, int maxResults) throws Exception;

    public List<DataDefinition> getDataDefinitionsByProcessDefinition(String processDefinitionName,
                                                                      String processVersion,
                                                                      int startIndex, int maxResults) throws Exception;

    public long createCase(long processDefinitionId, Map<String, Object> variables) throws Exception;

    public long createCase(String processDefinitionName, String processVersion, Map<String, Object> variables) throws Exception;

    /**
     * Retrieve a list of Activity Instances by process instance id.
     * Those activities are not necessarily assigned to a specific user.
     * @param processInstanceId
     * @return
     * @throws Exception
     */
    public List<ActivityInstance> getActivityInstancesByProcesInstance(long processInstanceId) throws Exception;

    /**
     * Retrieves a list of available Human tasks.
     * A Task is HumanTaskInstance while it is not assigned to a user. At that point it becomes a UserTaskInstance.
     * @return
     * @throws Exception
     */
    public List<HumanTaskInstance> getHumanTaskInstances() throws Exception;


    public void assignTaskToUser(long taskId, long userId) throws Exception;

    public void assignTaskToCurrentUser(long taskId) throws Exception;

    public void executeTaskFlowNode(long taskId) throws Exception;

    public void addCommentToProcessInstance(long processInstanceId, String comment) throws Exception;

    //public ProcessAPI getProcessAPI(APISession session, String userName, String password) throws Exception;

    //public List<ProcessDeploymentInfo> get

    public void test(String processDefinitionName, String processVersion, String activityName, int startIndex, int maxResults) throws Exception;
    public void test(long processInstanceId) throws Exception;
}
