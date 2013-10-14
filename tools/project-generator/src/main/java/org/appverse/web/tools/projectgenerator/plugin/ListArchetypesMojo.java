package org.appverse.web.tools.projectgenerator.plugin;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.Mojo;

@Mojo(name="list-archetypes", requiresProject=false)
public class ListArchetypesMojo extends AbstractMojo {

	public void execute() throws MojoExecutionException, MojoFailureException {
		Log logger = getLog();
		logger.info("List of project archetypes");
		logger.info("[1/2] Appverse Web GWT Frontend");
		logger.info("[2/2] Appverse Web JSF2 Frontend");
	}


}
