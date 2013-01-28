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

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;
import org.appverse.web.framework.tools.jpagenerator.plugin.impl.DDLClassLoader;
import org.appverse.web.framework.tools.jpagenerator.plugin.impl.DDLGeneratorTool;
import org.eclipse.persistence.config.PersistenceUnitProperties;
import org.eclipse.persistence.logging.SessionLog;

public class JpaDdlMojo extends AbstractMojo {

	private static final String ECLIPSELINK_PLATFORM_PACKAGE = "org.eclipse.persistence.platform.database";

	private static boolean isEmptyParameter(String s) {
		return s == null || s.trim().length() == 0;
	}

	private final String persistenceUnitName = "";
	private final String persistenceUnitFile = "META-INF/ddl-persistence.xml";
	private final String ddlOutputDir = "";
	private String ddlCreateFileName;
	private String ddlDropFileName;
	private final String ddlGenerationMode = PersistenceUnitProperties.DROP_AND_CREATE;
	private final String ddlGenerationLoggingLevel = SessionLog.FINEST_LABEL;
	private final String targetDbPlatform = "";

	private MavenProject project;

	@Override
	public void execute() throws MojoExecutionException {
		Log logger = getLog();

		try {
			DDLParameters params = makeParams();
			DDLClassLoader classLoader = DDLClassLoader.create(this.getClass().getClassLoader(), project, params);

			DDLGeneratorTool tooling = new DDLGeneratorTool(params, classLoader, logger);
			tooling.generateSchema();
		} catch (Throwable e) {
			throw new MojoExecutionException("Error while generating schema", e);
		}
	}

	private DDLParameters makeParams() {
		DDLParameters parameters = new DDLParameters();

		parameters.setPersistenceUnitName(persistenceUnitName);
		parameters.setPersistenceUnitFile(persistenceUnitFile);
		parameters.setDdlOutputDir(ddlOutputDir);

		if (isEmptyParameter(ddlCreateFileName)) {
			ddlCreateFileName = String.format("%s-ddl-create.sql", persistenceUnitName);
		}
		parameters.setDdlCreateFileName(ddlCreateFileName);

		if (isEmptyParameter(ddlDropFileName)) {
			ddlDropFileName = String.format("%s-ddl-drop.sql", persistenceUnitName);
		}
		parameters.setDdlDropFileName(ddlDropFileName);

		parameters.setDdlGenerationMode(ddlGenerationMode);
		parameters.setDdlGenerationLoggingLevel(ddlGenerationLoggingLevel);
		parameters.setTargetDbPlatform(ECLIPSELINK_PLATFORM_PACKAGE + "." + targetDbPlatform);

		return parameters;
	}
}
