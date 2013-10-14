package org.appverse.web.tools.projectgenerator.plugin;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

@Mojo(name = "generate-project", requiresProject = false)
public class GenerateProjectMojo extends AbstractMojo {

	@Parameter(property = "osName", defaultValue = "${os.name}")
	private String osName;
	@Parameter(property = "archetypeVersion", defaultValue = "1.1.1-SNAPSHOT")
	private String archetypeVersion;
	@Parameter(property = "groupId", defaultValue = "org.organization")
	private String groupId;
	@Parameter(property = "artifactId", defaultValue = "project")
	private String artifactId;
	@Parameter(property = "version", defaultValue = "1.0-SNAPSHOT")
	private String version;
	@Parameter(property = "projectName", defaultValue = "project")
	private String projectName;
	@Parameter(property = "presentationLayerType", defaultValue = "GWT")
	private String presentationLayerType;
	@Parameter(property = "dbPlatform", defaultValue = "MySQLPlatform")
	private String dbPlatform;
	@Parameter(property = "enterpriseApplication", defaultValue = "false")
	private boolean enterpriseApplication;

	public void execute() throws MojoExecutionException, MojoFailureException {
		Log logger = getLog();
		ProcessBuilder processBuilder;
		Process process;
		BufferedReader standardReader;
		BufferedReader errorReader;
		String line;
		int processReturnValue;
		boolean windowsFamilyOS = false;
		String mavenCommand = "mvn";
		if (osName.toLowerCase().indexOf("windows") > -1) {
			windowsFamilyOS = true;
			mavenCommand = "mvn.bat";
		}

		logger.debug("WindowsFamilyOS:" + windowsFamilyOS);
		logger.info("Creating project using archetypeVersion "
				+ archetypeVersion + "...");

		String earSuffix = "";
		if (enterpriseApplication) {
			earSuffix = "-ear";
		}

		try {
			if (PresentationLayerType.GWT.equals(presentationLayerType)) {
				logger.info("Project presentation layer flavour selected: "
						+ PresentationLayerType.GWT.getValue());
				processBuilder = new ProcessBuilder(
						mavenCommand,
						"archetype:generate",
						"-DarchetypeGroupId=org.appverse.web.framework.archetypes.gwt",
						"-DarchetypeArtifactId=appverse-web-archetypes-gwt"
								+ earSuffix, "-DarchetypeVersion="
								+ archetypeVersion, "-DgroupId=" + groupId
								+ "." + artifactId, "-DartifactId="
								+ artifactId, "-Dversion=" + version,
						"-DprojectName=" + projectName, "-DtargetDbPlatform="
								+ dbPlatform, "-DinteractiveMode=false");
				processBuilder.redirectErrorStream(true);
				process = processBuilder.start();
				standardReader = new BufferedReader(new InputStreamReader(
						process.getInputStream()));
				while ((line = standardReader.readLine()) != null) {
					logger.info(line);
				}
				errorReader = new BufferedReader(new InputStreamReader(
						process.getErrorStream()));
				while ((line = errorReader.readLine()) != null) {
					logger.error(line);
				}
				processReturnValue = process.waitFor();
				if (processReturnValue == 0) {
					logger.info("Project generated successfully");
				} else {
					throw new MojoExecutionException("Project creation failed");
				}
			} else if (PresentationLayerType.JSF2.equals(presentationLayerType)) {
				logger.info("Project presentation layer flavour selected: "
						+ PresentationLayerType.JSF2.getValue());
				processBuilder = new ProcessBuilder(
						mavenCommand,
						"archetype:generate",
						"-DarchetypeGroupId=org.appverse.web.framework.archetypes.jsf2",
						"-DarchetypeArtifactId=appverse-web-archetypes-jsf2"
								+ earSuffix, "-DarchetypeVersion="
								+ archetypeVersion, "-DgroupId=" + groupId
								+ "." + artifactId, "-DartifactId="
								+ artifactId, "-Dversion=" + version,
						"-DprojectName=" + projectName,
						"-DinteractiveMode=false");
				processBuilder.redirectErrorStream(true);
				process = processBuilder.start();
				standardReader = new BufferedReader(new InputStreamReader(
						process.getInputStream()));
				while ((line = standardReader.readLine()) != null) {
					logger.info(line);
				}
				errorReader = new BufferedReader(new InputStreamReader(
						process.getErrorStream()));
				while ((line = errorReader.readLine()) != null) {
					logger.error(line);
				}
				processReturnValue = process.waitFor();
				if (processReturnValue == 0) {
					logger.info("Project generated successfully");
				} else {
					throw new MojoExecutionException("Project creation failed");
				}
			} else {
				logger.info("No valid presentation flavour selected: "
						+ presentationLayerType + " the valids ones are "
						+ PresentationLayerType.GWT.getValue() + " and "
						+ PresentationLayerType.JSF2.getValue());
			}

		} catch (MojoExecutionException e) {
			throw e;
		} catch (Exception e) {
			throw new MojoFailureException("Project creation failed", e);
		}
	}
}
