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
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.appverse.web.framework.tools.jpagenerator.plugin.impl.DDLClassLoader;
import org.appverse.web.framework.tools.jpagenerator.plugin.impl.DDLGeneratorTool;

@Mojo(name="generate-schema")
public class JpaDdlMojo extends AbstractMojo {

	@Parameter(property="persistenceUnitName",defaultValue="persistence-unit")
	private String persistenceUnitName;
	@Parameter(property="persistenceUnitFile",defaultValue="META-INF/persistence-ddl.xml")
	private String persistenceUnitFile;
	@Parameter(property="ddlOutputDir",defaultValue="src/main/resources/sql")
	private String ddlOutputDir;
	@Parameter(property="ddlCreateFileName",defaultValue="schema-create.sql")
	private String ddlCreateFileName;
	@Parameter(property="ddlDropFileName",defaultValue="schema-drop.sql")
	private String ddlDropFileName;
	@Parameter(property="targetDbPlatform",defaultValue="HSQLPlatform")
	private String targetDbPlatform;
	@Parameter(readonly=true,property="project")
	private MavenProject project; 
	
	@Override
	public void execute() throws MojoExecutionException {
		Log logger = getLog();
		try {
			ddlCreateFileName=targetDbPlatform+"-"+ddlCreateFileName;
			ddlDropFileName=targetDbPlatform+"-"+ddlDropFileName;
			DDLParameters parameters = new DDLParameters(persistenceUnitName,persistenceUnitFile,ddlOutputDir,ddlCreateFileName,ddlDropFileName,"org.eclipse.persistence.platform.database." + targetDbPlatform);
			DDLClassLoader classLoader = DDLClassLoader.create(this.getClass().getClassLoader(), project);
			DDLGeneratorTool tooling = new DDLGeneratorTool(parameters, classLoader, logger);
			tooling.generateSchema();
		} catch (Throwable e) {
			throw new MojoExecutionException("Error while generating schema", e);
		}
	}
}