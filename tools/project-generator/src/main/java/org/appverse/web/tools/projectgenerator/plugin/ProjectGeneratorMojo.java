package org.appverse.web.tools.projectgenerator.plugin;

import java.io.IOException;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

@Mojo(name="generate-project")
public class ProjectGeneratorMojo extends AbstractMojo {

	@Parameter(property="presentationLayerType",defaultValue="GWT")
	private String presentationLayerType;
	public void execute() throws MojoExecutionException, MojoFailureException {
		Log logger = getLog();
		logger.info("Creating project...");
		try{
		if(PresentationLayerType.GWT.equals(presentationLayerType)){
			logger.info("Project presentation layer flavour selected: "+PresentationLayerType.GWT.getValue());
			Process process;
			process = Runtime.getRuntime().exec("cmd.exe /c rm pom.xml");
			process = Runtime.getRuntime().exec("cmd.exe /c mvn archetype:generate -DarchetypeGroupId=org.appverse.web.framework.archetypes.gwt -DarchetypeArtifactId=appverse-web-archetypes-gwt -DarchetypeVersion=1.1.0-RELEASE -DgroupId=org.organization.project -DartifactId=project -Dversion=1.0-SNAPSHOT -DnewProjectName=MyProject -DtargetDbPlatform=MysqlPlatform -DinteractiveMode=false");
			process.waitFor();
			logger.info("Project generated successfully");
		}else if(PresentationLayerType.JSF2.equals(presentationLayerType)){
			logger.info("Project presentation layer flavour selected: "+PresentationLayerType.JSF2.getValue());
			Process process;
			process = Runtime.getRuntime().exec("cmd.exe /c rm pom.xml");
			process = Runtime.getRuntime().exec("cmd.exe /c mvn archetype:generate -DarchetypeGroupId=org.appverse.web.framework.archetypes.jsf2 -DarchetypeArtifactId=appverse-web-archetypes-jsf2 -DarchetypeVersion=1.1.0-RELEASE -DgroupId=org.organization.project -DartifactId=project -Dversion=1.0-SNAPSHOT -DnewProjectName=MyProject -DinteractiveMode=false");
			process.waitFor();
			logger.info("Project generated successfully");
		}else{
			logger.info("No valid presentation flavour selected: "+presentationLayerType+" the valids ones are "+PresentationLayerType.GWT.getValue()+" and "+PresentationLayerType.JSF2.getValue());
		}

		} catch (Exception e) {
			logger.error("Error creating Project");
			e.printStackTrace();
		}
	}



}
