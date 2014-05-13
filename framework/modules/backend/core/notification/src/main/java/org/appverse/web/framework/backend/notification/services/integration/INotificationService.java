/*
 Copyright (c) 2012 GFT Appverse, S.L., Sociedad Unipersonal.

 This Source Code Form is subject to the terms of the Appverse  License
 Version 2.0 (“APL v2.0”). If a copy of the APL was not distributed with this
 file, You can obtain one at http://www.appverse.mobi/licenses/apl_v2.0.pdf. [^]

 Redistribution and use in source and binary forms, with or without modification,
 are permitted provided that the conditions of the AppVerse  License v2.0
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
package org.appverse.web.framework.backend.notification.services.integration;


import org.appverse.web.framework.backend.notification.model.integration.NPlatformDTO;
import org.appverse.web.framework.backend.notification.model.integration.NUserDTO;

import java.util.List;
import java.util.Map;

public interface INotificationService {

     void registerUser(NUserDTO nUserDTO) throws Exception;

     void updateUser(NUserDTO nUserDTO) throws Exception;

     NUserDTO retrieveUser(String userId) throws Exception;

     void addPlatformToUser(String userId, NPlatformDTO nPlatformDTO) throws Exception;

     boolean sendNotification(String userId, List<String> platformId, String body) throws Exception;

     boolean sendNotification(String platform, String token, String body, Map<String,String> params)throws Exception;

     boolean sendNotificationByPlatform(String userId, List<NPlatformDTO> nPlatformDTOs, String body) throws Exception;

     boolean sendNotification(String userId, List<String> platformIds, String body, Map<String,String> params) throws Exception;

     void updatePlatform(String userId, String platformId, String token) throws Exception;

     boolean sendNotification(String platform, String token, String body) throws Exception;

     void outputData() throws Exception;


}
