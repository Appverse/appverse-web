package org.appverse.web.tools.archeytegenerator.plugin;

import java.io.IOException;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.Mojo;

@Mojo(name="generate-archetypes")
public class ArchetypeGeneratorMojo extends AbstractMojo {

	public void execute() throws MojoExecutionException, MojoFailureException {
		Log logger = getLog();
		logger.info("Installing Appverse archetypes...");
		final int numberOfArchetypes=2;
		int currentArchetype=1;
		try {
			Process process = Runtime.getRuntime().exec("cmd.exe /c mvn -f src/main/resources/gwt/pom.xml install");
			process.waitFor();
			logger.info("["+currentArchetype+"/"+numberOfArchetypes+"] GWT archetype installed");
			currentArchetype++;
			process = Runtime.getRuntime().exec("cmd.exe /c mvn -f src/main/resources/jsf2/pom.xml install");
			process.waitFor();
			logger.info("["+currentArchetype+"/"+numberOfArchetypes+"] JSF2 archetype installed");
		} catch (Exception e) {
			logger.error("Error generating archetypes");
			e.printStackTrace();
		}
		logger.info("All archetypes have been installed successfully");
		
	}



}
