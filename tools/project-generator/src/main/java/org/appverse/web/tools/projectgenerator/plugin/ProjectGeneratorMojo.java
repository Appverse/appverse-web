package org.appverse.web.tools.projectgenerator.plugin;

import java.io.IOException;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.Mojo;

@Mojo(name="generate-project")
public class ProjectGeneratorMojo extends AbstractMojo {

	public void execute() throws MojoExecutionException, MojoFailureException {
		Log logger = getLog();
		try {
			Process process = Runtime.getRuntime().exec("cmd.exe /c mvn clean");
			process.waitFor();
			//log.error(process.getOutputStream().);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.error("Hello generate project");
		
	}



}
