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
package org.appverse.web.framework.backend.ecm.core.model.integration;

import org.appverse.web.framework.backend.api.model.integration.AbstractIntegrationBean;

import java.util.Date;

/**
 * Base abstract class for document management
 *
 */
public abstract class AbstractDocumentIntegrationBean extends AbstractIntegrationBean {

    protected long id;

    protected byte[] contentStream;
    protected String contentStreamFilename;
    protected long contentStreamLenght;
    protected String contentStreamMimeType;

    protected Date created;
    protected String createdBy;
    protected Date updated;
    protected String updatedBy;

    public void setContentStreamMimeType(String contentStreamMimeType) {
        this.contentStreamMimeType = contentStreamMimeType;
    }

    public void setContentStreamLenght(long contentStreamLenght) {
        this.contentStreamLenght = contentStreamLenght;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public void setContentStream(final byte[] contentStream) {
        this.contentStream = contentStream;
    }

    public void setContentStreamFilename(final String contentStreamFilename) {
        this.contentStreamFilename = contentStreamFilename;
    }

    public void setId(final long id) {
        this.id = id;
    }

    public long getContentStreamLenght() {
        return contentStreamLenght;
    }

    public String getContentStreamMimeType() {
        return contentStreamMimeType;
    }

    public byte[] getContentStream() {
        return contentStream;
    }

    public String getContentStreamFileNameExtension() {
        return contentStreamFilename.substring(contentStreamFilename.lastIndexOf(".") + 1);
    }

    public String getContentStreamFilename() {
        return contentStreamFilename;
    }

    public long getId() {
        return id;
    }

    public Date getCreated() {
        return created;
    }

    public String getMimeTypeFromContentStreamFileName() {
        if (contentStreamFilename.endsWith(".doc") || contentStreamFilename.endsWith(".docx")
                || contentStreamFilename.endsWith(".rtf") || contentStreamFilename.endsWith(".docm")
                || contentStreamFilename.endsWith(".dotx") || contentStreamFilename.endsWith(".dotm")
                || contentStreamFilename.endsWith(".odt")) {
            return "application/msword";
        } else if (contentStreamFilename.endsWith(".xls") || contentStreamFilename.endsWith(".xlsx")
                || contentStreamFilename.endsWith(".xlsm") || contentStreamFilename.endsWith(".xltx")
                || contentStreamFilename.endsWith(".xltm") || contentStreamFilename.endsWith(".xlsb")
                || contentStreamFilename.endsWith(".xlam")) {
            return "application/vnd.ms-excel";
        } else if (contentStreamFilename.endsWith(".pdf")) {
            return "application/pdf";
        } else if (contentStreamFilename.endsWith(".png")) {
            return "image/png";
        } else if (contentStreamFilename.endsWith(".jpg") || contentStreamFilename.endsWith(".jpeg")
                || contentStreamFilename.endsWith(".jpe")) {
            return "image/jpeg";
        } else if (contentStreamFilename.endsWith(".ico")) {
            return "image/x-icon";
        } else if (contentStreamFilename.endsWith(".mp4")) {
            return "video/mp4";
        } else if (contentStreamFilename.endsWith(".ppt")) {
            return "application/powerpoint";
        } else if (contentStreamFilename.endsWith(".apk")) {
            return "application/vnd.android.package-archive";
        } else if (contentStreamFilename.endsWith(".ipa")) {
            return "application/octet-stream";
        } else if (contentStreamFilename.endsWith(".plist")) {
            return "text/xml";
        } else if (contentStreamFilename.endsWith(".crx")) {
            return "application/x-chrome-extension";
        } else if (contentStreamFilename.endsWith(".xpi")) {
            return "application/x-xpinstall";
        } else if (contentStreamFilename.endsWith(".safariextz")) {
            return "application/x-safari-safariextz";
        } else if (contentStreamFilename.endsWith(".txt")) {
            return "text/plain";
        } else {
            return "";
        }
    }
}