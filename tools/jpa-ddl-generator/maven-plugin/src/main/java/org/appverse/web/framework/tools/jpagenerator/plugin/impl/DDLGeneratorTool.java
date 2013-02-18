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
package org.appverse.web.framework.tools.jpagenerator.plugin.impl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.apache.maven.plugin.logging.Log;
import org.appverse.web.framework.tools.jpagenerator.plugin.DDLParameters;
import org.eclipse.persistence.config.PersistenceUnitProperties;

public class DDLGeneratorTool {

	private final DDLParameters parameters;
	private final DDLClassLoader ddlClassLoader;
	private final Log log;

	public DDLGeneratorTool(DDLParameters parameters,
			DDLClassLoader classLoader, Log logger) {
		this.log = logger;
		this.parameters = parameters;
		this.ddlClassLoader = classLoader;
	}

	private void ensureOutputDirectoryExists() {
		File outputDir = new File(parameters.getDdlOutputDir());
		if (!outputDir.exists()) {
			outputDir.mkdirs();
		}
	}

	private void fixSqlLineTerminators(String ddlOutputDir, String ddlFileName) {
		File inputFile = new File(ddlOutputDir + File.separator + ddlFileName);
		try {
			BufferedReader br = new BufferedReader(new FileReader(inputFile));
			File outFile = File.createTempFile("fix_", ".tmp");
			BufferedWriter bw = new BufferedWriter(new FileWriter(outFile));

			String line = null;
			while ((line = br.readLine()) != null) {
				bw.write(line);
				if (!line.endsWith(";")) {
					bw.append(";");
				}
				bw.append("\n");
			}
			br.close();
			bw.flush();
			bw.close();
			inputFile.delete();
			outFile.renameTo(inputFile);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	public void generateSchema() {
		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put(PersistenceUnitProperties.JDBC_DRIVER,
				CreateSchemaMockDriver.class.getName());
		properties.put(PersistenceUnitProperties.JDBC_URL, "emulate:dummy");
		properties.put(PersistenceUnitProperties.DDL_GENERATION,
				PersistenceUnitProperties.DROP_AND_CREATE);
		properties.put(PersistenceUnitProperties.DDL_GENERATION_MODE,
				PersistenceUnitProperties.DDL_SQL_SCRIPT_GENERATION);
		properties.put(PersistenceUnitProperties.CREATE_JDBC_DDL_FILE,
				parameters.getDdlCreateFileName());
		properties.put(PersistenceUnitProperties.DROP_JDBC_DDL_FILE,
				parameters.getDdlDropFileName());
		properties.put(PersistenceUnitProperties.APP_LOCATION,
				parameters.getDdlOutputDir());
		properties.put(PersistenceUnitProperties.ECLIPSELINK_PERSISTENCE_XML,
				parameters.getPersistenceUnitFile());
		properties.put(PersistenceUnitProperties.TARGET_DATABASE,
				parameters.getTargetDbPlatform());
		properties.put(PersistenceUnitProperties.CLASSLOADER, ddlClassLoader);
		ensureOutputDirectoryExists();

		ClassLoader tccl = Thread.currentThread().getContextClassLoader();
		try {
			Thread.currentThread().setContextClassLoader(ddlClassLoader);
			EntityManagerFactory emf = Persistence.createEntityManagerFactory(
					parameters.getPersistenceUnitName(), properties);
			EntityManager em = emf.createEntityManager();
			em.close();
			emf.close();
			fixSqlLineTerminators(parameters.getDdlOutputDir(),
					parameters.getDdlCreateFileName());
			fixSqlLineTerminators(parameters.getDdlOutputDir(),
					parameters.getDdlDropFileName());
		} finally {
			Thread.currentThread().setContextClassLoader(tccl);
		}
	}
}
