package org.appverse.web.framework.backend.ws.handlers;

import java.util.Date;

import javax.xml.ws.handler.LogicalHandler;
import javax.xml.ws.handler.LogicalMessageContext;
import javax.xml.ws.handler.MessageContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * Client Performance Monitor Handler
 * @author MOCR
 *
 */
public class ClientPerformanceMonitorLogicalHandler extends
		BaseHandler<LogicalMessageContext> implements
		LogicalHandler<LogicalMessageContext> {
    /**
     * Logger
     */
	private static Logger logger = LoggerFactory.getLogger(ClientPerformanceMonitorLogicalHandler.class);
	/**
	 * Handler name
	 */
	private static final String HANDLER_NAME = "ClientPerformanceMonitorLogicalHandler"; 

	/**
	 * Handle times
	 */
	private Date startTime, endTime;
	/**
	 * Constructor
	 */
	public ClientPerformanceMonitorLogicalHandler() {
		super();
		super.setHandlerName(HANDLER_NAME);
	}
	/**
    * Handle message 
    * @param smc SOAPMessageContext
    * @return boolean 
    */
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
				logger.info("Elapsed time in handler " + HandlerName + " is "
						+ elapsedTime); 
			} catch (Exception x) {
				x.printStackTrace();
			}
		} 
		return true;
	}
}