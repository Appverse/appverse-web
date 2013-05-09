package org.appverse.web.tools.projectgenerator.plugin;

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
	@Parameter(property="groupId",defaultValue="org.organization")
	private String groupId;
	@Parameter(property="artifactId",defaultValue="project")
	private String artifactId;
	@Parameter(property="version",defaultValue="1.0-SNAPSHOT")
	private String version;
	@Parameter(property="name",defaultValue="project")
	private String name;	
	@Parameter(property="dbPlatform",defaultValue="MySQLPlatform")
	private String dbPlatform;
	@Parameter(property="archetypeVersion",defaultValue="1.1.0-RELEASE")
	private String archetypeVersion;	
	public void execute() throws MojoExecutionException, MojoFailureException {
		Log logger = getLog();
		logger.info("Creating project using archetypeVersion "+archetypeVersion+"...");
		try{
		if(PresentationLayerType.GWT.equals(presentationLayerType)){
			logger.info("Project presentation layer flavour selected: "+PresentationLayerType.GWT.getValue());
			Process process;
			process = Runtime.getRuntime().exec("cmd.exe /c mv pom.xml pom.xml_");
			process = Runtime.getRuntime().exec("cmd.exe /c mvn archetype:generate -DarchetypeGroupId=org.appverse.web.framework.archetypes.gwt -DarchetypeArtifactId=appverse-web-archetypes-gwt -DarchetypeVersion="+archetypeVersion+" -DgroupId="+groupId+"."+artifactId+" -DartifactId="+artifactId+" -Dversion="+version+" -DnewProjectName="+name+" -DtargetDbPlatform="+dbPlatform+" -DinteractiveMode=false");
			process.waitFor();
			process = Runtime.getRuntime().exec("cmd.exe /c mv pom.xml_ pom.xml");
			logger.info("Project generated successfully");
		}else if(PresentationLayerType.JSF2.equals(presentationLayerType)){
			logger.info("Project presentation layer flavour selected: "+PresentationLayerType.JSF2.getValue());
			Process process;
			process = Runtime.getRuntime().exec("cmd.exe /c mv pom.xml pom.xml_");
			process = Runtime.getRuntime().exec("cmd.exe /c mvn archetype:generate -DarchetypeGroupId=org.appverse.web.framework.archetypes.jsf2 -DarchetypeArtifactId=appverse-web-archetypes-jsf2 -DarchetypeVersion="+archetypeVersion+" -DgroupId="+groupId+"."+artifactId+" -DartifactId="+artifactId+" -Dversion="+version+" -DnewProjectName="+name+" -DinteractiveMode=false");
			process.waitFor();
			process = Runtime.getRuntime().exec("cmd.exe /c mv pom.xml_ pom.xml");
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
