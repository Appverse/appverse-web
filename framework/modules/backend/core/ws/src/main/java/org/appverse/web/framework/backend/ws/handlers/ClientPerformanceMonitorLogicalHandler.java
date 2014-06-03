package org.appverse.web.framework.backend.ws.handlers;

import java.io.PrintStream;
import java.util.Date;

import javax.xml.ws.handler.LogicalHandler;
import javax.xml.ws.handler.LogicalMessageContext;
import javax.xml.ws.handler.MessageContext;

public class ClientPerformanceMonitorLogicalHandler extends
		BaseHandler<LogicalMessageContext> implements
		LogicalHandler<LogicalMessageContext> {

	private static final String HANDLER_NAME = "ClientPerformanceMonitorLogicalHandler";

	private static PrintStream out = System.out;

	Date startTime, endTime;

	public ClientPerformanceMonitorLogicalHandler() {
		super();
		super.setHandlerName(HANDLER_NAME);
	}

	public boolean handleMessage(LogicalMessageContext smc) {
		Boolean outboundProperty = (Boolean) smc
				.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY); 
		if (outboundProperty.booleanValue()) { 
			startTime = new Date();
			smc.put(HandlerName + "StartTime", startTime);
		} else { 
			try {
				startTime = (Date) smc.get(HandlerName + "StartTime");
				endTime = new Date();
				long elapsedTime = endTime.getTime() - startTime.getTime();
				
				smc.put("ELAPSED_TIME", elapsedTime);
				smc.setScope("ELAPSED_TIME", MessageContext.Scope.APPLICATION);
				
				out.println("");
				out.println("Elapsed time in handler " + HandlerName + " is "
						+ elapsedTime);
				out.println("");
			} catch (Exception x) {
				x.printStackTrace();
			}
		} 
		return true;
	}
}