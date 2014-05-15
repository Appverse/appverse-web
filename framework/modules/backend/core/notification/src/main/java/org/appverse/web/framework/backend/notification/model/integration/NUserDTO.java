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
package org.appverse.web.framework.backend.notification.model.integration;

import org.appverse.web.framework.backend.persistence.model.integration.AbstractIntegrationAuditedJPABean;

import javax.persistence.*;
import java.util.List;

/**
 *
 *
 *
 {
    "user":{
    "id":"1689555270",
    "platforms":{
        "android":{
            "appVersion":"1.0",
            "token":"APA91bE_8UgjpeK7gFOUTcw_UgMIrjGSdBo5fy1eNQizM02u0SZ7_qwTLiTaaqbfE555_6ce2qxklw7tzyzjYvqNC_3LBAx4tpNORDOUKd8jcKUWX5ElgrTvRg6Dhpbsoh6T1Zz3bV",
            "alias":"Teléfono del trabajo",
            "id":"67ae574a37f1a3d1"
            }
        }
    }
 }
 */
@Entity
@Table(name = "NUSER")
public class NUserDTO extends AbstractIntegrationAuditedJPABean {

    private String userId;

    private List<NPlatformDTO> notPlatforms;

    private boolean banned;

    @Id
    @TableGenerator(name = "NUSER_GEN", table = "SEQUENCE", pkColumnName = "SEQ_NAME", pkColumnValue = "NUSER_SEQ", valueColumnName = "SEQ_COUNT", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "NUSER_GEN")
    public long getId() {
        return id;
    }

    @Column(nullable = false)
    public boolean isBanned() {
        return banned;
    }

    @Column(nullable = false, length = 40)
    public String getUserId() {
        return userId;
    }

    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "NUSER_NPLATFORM", joinColumns = @JoinColumn(name = "NUSER_ID"), inverseJoinColumns = @JoinColumn(name = "NPLATFORM_ID"))
    public List<NPlatformDTO> getNotPlatforms() {
        return notPlatforms;
    }

    public void setNotPlatforms(List<NPlatformDTO> notPlatforms) {
        this.notPlatforms = notPlatforms;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setBanned(boolean banned) {
        this.banned = banned;
    }
}
