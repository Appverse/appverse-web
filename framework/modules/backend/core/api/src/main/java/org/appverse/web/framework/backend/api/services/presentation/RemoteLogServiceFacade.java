package org.appverse.web.framework.backend.api.services.presentation;

import org.appverse.web.framework.backend.api.model.presentation.RemoteLogRequestVO;
import org.appverse.web.framework.backend.api.model.presentation.RemoteLogResponseVO;

public interface RemoteLogServiceFacade extends IPresentationService {

    public RemoteLogResponseVO writeLog(RemoteLogRequestVO remoteLogRequestVO) throws Exception;

}
