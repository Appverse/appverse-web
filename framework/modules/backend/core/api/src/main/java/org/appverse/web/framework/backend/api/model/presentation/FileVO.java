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
package org.appverse.web.framework.backend.api.model.presentation;

/**
 * The business class for the attachment
 */
public class FileVO extends AbstractPresentationBean {

	private static final long serialVersionUID = -5519885416784453309L;

	private long id;
	private String filename;
	private byte[] bytes;

	public FileVO() {
	}

	public byte[] getBytes() {
		return bytes;
	}

	public String getFilename() {
		return filename;
	}

	public long getId() {
		return id;
	}

	public String getMimeType() {
		if (filename.endsWith(".doc") || filename.endsWith(".docx")
				|| filename.endsWith(".rtf") || filename.endsWith(".docm")
				|| filename.endsWith(".dotx") || filename.endsWith(".dotm")
				|| filename.endsWith(".odt")) {
			return "application/msword";
		} else if (filename.endsWith(".xls") || filename.endsWith(".xlsx")
				|| filename.endsWith(".xlsm") || filename.endsWith(".xltx")
				|| filename.endsWith(".xltm") || filename.endsWith(".xlsb")
				|| filename.endsWith(".xlam")) {
			return "application/vnd.ms-excel";
		} else if (filename.endsWith(".pdf")) {
			return "application/pdf";
		} else if (filename.endsWith(".png")) {
			return "image/png";
		} else if (filename.endsWith(".jpg") || filename.endsWith(".jpeg")
				|| filename.endsWith(".jpe")) {
			return "image/jpeg";
		} else if (filename.endsWith(".ico")) {
			return "image/x-icon";
		} else if (filename.endsWith(".mp4")) {
			return "video/mp4";
		} else if (filename.endsWith(".ppt")) {
			return "application/powerpoint";
		} else if (filename.endsWith(".apk")) {
			return "application/vnd.android.package-archive";
		} else if (filename.endsWith(".ipa")) {
			return "application/octet-stream";
		} else if (filename.endsWith(".plist")) {
			return "text/xml";
		} else if (filename.endsWith(".crx")) {
			return "application/x-chrome-extension";
		} else if (filename.endsWith(".xpi")) {
			return "application/x-xpinstall";
		} else if (filename.endsWith(".safariextz")) {
			return "application/x-safari-safariextz";
		} else {
			return "";
		}
	}

	public void setBytes(final byte[] bytes) {
		this.bytes = bytes;
	}

	public void setFilename(final String filename) {
		this.filename = filename;
	}

	public void setId(final long id) {
		this.id = id;
	}
}