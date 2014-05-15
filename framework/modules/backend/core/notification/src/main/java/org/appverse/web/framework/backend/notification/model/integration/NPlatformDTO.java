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


@Entity
@Table(name = "NPLATFORM")
public class NPlatformDTO extends AbstractIntegrationAuditedJPABean {


    private NPlatformTypeDTO nPlatformType;
    private String appVersion;
    private String alias;
    private String platformId;
    /**
     * For Android, token will host the Registration Id
     * For IOS, token will host the device token
     * Both will be obtained by the client app and passed to the application when addPlatformToUser.
     */
    private String token;
    private boolean enabled;

    @Id
    @TableGenerator(name = "NPLATFORM_GEN", table = "SEQUENCE", pkColumnName = "SEQ_NAME", pkColumnValue = "NPLATFORM_SEQ", valueColumnName = "SEQ_COUNT", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "NPLATFORM_GEN")
    public long getId() {
        return id;
    }

    @Column(nullable = false)
    public boolean isEnabled() {
        return enabled;
    }

    @ManyToOne( optional = false, cascade = { CascadeType.REFRESH })
    @JoinColumn(name = "NPLATFORMTYPE_ID")
    public NPlatformTypeDTO getNotPlatformType() {
        return nPlatformType;
    }

    @Column(nullable = false, length = 40)
    public String getAppVersion() {
        return appVersion;
    }

    @Column(nullable = false, length = 40)
    public String getAlias() {
        return alias;
    }

    @Column(nullable = false, length = 40)
    public String getPlatformId() {
        return platformId;
    }

    @Column(nullable = false, length = 200)
    public String getToken() {
        return token;
    }

    public void setPlatformId(String platformId) {
        this.platformId = platformId;
    }
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public void setNotPlatformType(NPlatformTypeDTO nPlatformType) {
        this.nPlatformType = nPlatformType;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public void setToken(String token) {
        this.token = token;
    }

}

