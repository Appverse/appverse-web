package org.appverse.web.tools.archeytegenerator.plugin;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

@Mojo(name = "generate-archetypes", requiresProject = false)
public class ArchetypeGeneratorMojo extends AbstractMojo {
	@Parameter(property = "osName", defaultValue = "${os.name}")
	private String osName;
	@Parameter(property = "presentationLayerType", defaultValue = "GWT")
	private String presentationLayerType;

	public void execute() throws MojoExecutionException, MojoFailureException {
		Log logger = getLog();
		boolean windowsFamilyOS = false;
		String mavenCommand = "mvn";
		String antCommand = "ant";
		if (osName.toLowerCase().indexOf("windows") > -1) {
			windowsFamilyOS = true;
			mavenCommand = "mvn.bat";
			antCommand = "ant.bat";
		}
		logger.debug("WindowsFamilyOS:" + windowsFamilyOS);
		logger.info("Installing Appverse archetypes...");
		final int numberOfArchetypes = 3;
		int currentArchetype = 1;
		try {
			runProcess("src/main/resources/gwt/", antCommand);
			runProcess("src/main/resources/", antCommand);
			runProcess("target/build", mavenCommand, "install", "deploy");
			logger.info("[" + currentArchetype + "/" + numberOfArchetypes
					+ "] GWT archetype installed");
			currentArchetype++;

			runProcess("src/main/resources/jsf2/", antCommand);
			runProcess("src/main/resources/", antCommand);
			runProcess("target/build", mavenCommand, "install", "deploy");
			logger.info("[" + currentArchetype + "/" + numberOfArchetypes
					+ "] GWT archetype installed");
			currentArchetype++;

			runProcess("src/main/resources/gwt/", antCommand);
			runProcess("src/main/resources/ear/", antCommand);
			runProcess("src/main/resources/", antCommand);
			runProcess("target/build", mavenCommand, "install", "deploy");
			logger.info("[" + currentArchetype + "/" + numberOfArchetypes
					+ "] GWT EAR archetype installed");
			currentArchetype++;

			// runProcess("src/main/resources/jsf2/", antCommand);
			// runProcess("src/main/resources/ear/", antCommand);
			// runProcess("target/build", mavenCommand, "install");
			// logger.info("[" + currentArchetype + "/" + numberOfArchetypes
			// + "] JSF2 EAR archetype installed");
			// currentArchetype++;

			logger.info("All archetypes have been installed successfully");
		} catch (MojoExecutionException e) {
			throw e;
		} catch (Exception e) {
			throw new MojoFailureException("Project creation failed", e);
		}
	}

	private void runProcess(String processWorkdir, String... processArgs)
			throws Exception {
		Log logger = getLog();
		String line;
		ProcessBuilder processBuilder = new ProcessBuilder(processArgs);
		processBuilder.directory(new File(processWorkdir));
		Process process = processBuilder.start();
		BufferedReader standardReader = new BufferedReader(
				new InputStreamReader(process.getInputStream()));
		while ((line = standardReader.readLine()) != null) {
			if (line.startsWith("[DEBUG]")) {
				logger.debug(line.substring(line.indexOf("[DEBUG]")
						+ "[DEBUG]".length() + 1));
			} else if (line.startsWith("[INFO]")) {
				logger.info(line.substring(line.indexOf("[INFO]")
						+ "[INFO]".length() + 1));
			} else if (line.startsWith("[WARNING]")) {
				logger.warn(line.substring(line.indexOf("[WARNING]")
						+ "[WARNING]".length() + 1));
			} else if (line.startsWith("[ERROR]")) {
				logger.error(line.substring(line.indexOf("[ERROR]")
						+ "[ERROR]".length() + 1));
			} else {
				logger.info(line);
			}
		}
		BufferedReader errorReader = new BufferedReader(new InputStreamReader(
				process.getErrorStream()));
		while ((line = errorReader.readLine()) != null) {
			if (line.startsWith("[DEBUG]")) {
				logger.info(line.substring(line.indexOf("[DEBUG]"
						+ "[DEBUG]".length() + 1)));
			} else if (line.startsWith("[INFO]")) {
				logger.info(line.substring(line.indexOf("[INFO]"
						+ "[INFO]".length() + 1)));
			} else if (line.startsWith("[WARNING]")) {
				logger.info(line.substring(line.indexOf("[WARNING]"
						+ "[WARNING]".length() + 1)));
			} else if (line.startsWith("[ERROR]")) {
				logger.info(line.substring(line.indexOf("[ERROR]")
						+ "[ERROR]".length() + 1));
			} else {
				logger.info(line);
			}
		}
		int processReturnValue = process.waitFor();
		if (processReturnValue != 0) {
			throw new MojoExecutionException("Project creation failed");
		}
	}
}
