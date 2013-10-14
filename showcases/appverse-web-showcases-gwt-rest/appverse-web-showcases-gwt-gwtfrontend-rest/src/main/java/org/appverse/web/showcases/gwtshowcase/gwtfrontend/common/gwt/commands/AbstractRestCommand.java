package org.appverse.web.showcases.gwtshowcase.gwtfrontend.common.gwt.commands;

import org.appverse.web.framework.frontend.gwt.commands.AbstractCommand;

import com.mvp4g.client.event.EventBusWithLookup;

/**
 * this class should have the necessary code to create the Rest client and communicate with the rest server.
 * @author rrbl
 *
 */
public abstract class AbstractRestCommand <E extends EventBusWithLookup> extends 
	AbstractCommand<E>{
	

	protected <Service> Service getService(Service service) {
		//place to do some xsrf stuff
		return service;
	}
}
