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
package org.appverse.web.framework.tools.jpagenerator.plugin;

public class DDLParameters {

	private String persistenceUnitName;
	private String persistenceUnitFile;
	private String ddlOutputDir;
	private String ddlCreateFileName;
	private String ddlDropFileName;
	private String targetDbPlatform;

	public DDLParameters(String persistenceUnitName,
			String persistenceUnitFile, String ddlOutputDir,
			String ddlCreateFileName, String ddlDropFileName,
			String targetDbPlatform) {
		super();
		this.persistenceUnitName = persistenceUnitName;
		this.persistenceUnitFile = persistenceUnitFile;
		this.ddlOutputDir = ddlOutputDir;
		this.ddlCreateFileName = ddlCreateFileName;
		this.ddlDropFileName = ddlDropFileName;
		this.targetDbPlatform = targetDbPlatform;
	}
	
	public String getDdlCreateFileName() {
		return ddlCreateFileName;
	}

	public String getDdlDropFileName() {
		return ddlDropFileName;
	}

	public String getDdlOutputDir() {
		return ddlOutputDir;
	}

	public String getPersistenceUnitFile() {
		return persistenceUnitFile;
	}

	public String getPersistenceUnitName() {
		return persistenceUnitName;
	}

	public String getTargetDbPlatform() {
		return targetDbPlatform;
	}

	public void setDdlCreateFileName(String ddlCreateFileName) {
		this.ddlCreateFileName = ddlCreateFileName;
	}

	public void setDdlDropFileName(String ddlDropFileName) {
		this.ddlDropFileName = ddlDropFileName;
	}

	public void setDdlOutputDir(String ddlOutputDir) {
		this.ddlOutputDir = ddlOutputDir;
	}

	public void setPersistenceUnitFile(String persistenceUnitFile) {
		this.persistenceUnitFile = persistenceUnitFile;
	}

	public void setPersistenceUnitName(String persistenceUnitName) {
		this.persistenceUnitName = persistenceUnitName;
	}

	public void setTargetDbPlatform(String targetDbPlatform) {
		this.targetDbPlatform = targetDbPlatform;
	}
}
