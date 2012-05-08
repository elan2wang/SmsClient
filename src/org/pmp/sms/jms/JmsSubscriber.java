/**
 * Author            : Elan
 * Created On        : 2012-5-7 下午03:14:11
 * 
 * Copyright 2012.  All rights reserved. 
 *
 * Revision History
 * 
 *    Date       Modifier       Comments
 * ----------    -------------  --------------------------------------------
 * 
 */
package org.pmp.sms.jms;

import javax.jms.Destination;
import javax.jms.TextMessage;

import org.apache.log4j.Logger;
import org.pmp.sms.sms.SmsUtil;
import org.springframework.jms.core.JmsTemplate;

/**
 * @author Elan
 * @version 1.0
 * @update TODO
 */
public class JmsSubscriber {

    //~ Static Fields ==================================================================================================
    private static Logger logger = Logger.getLogger(JmsSubscriber.class.getName());
    //~ Instance Fields ================================================================================================
    private JmsTemplate jmsTemplate;
    private Destination destination;
    private SmsUtil smsUtil;
    
    //~ Methods ========================================================================================================
    public void receive(){
	while(true){
	    try {
		TextMessage txtmsg = (TextMessage) jmsTemplate.receive(destination);
	        if (null != txtmsg){
	            String content = txtmsg.getText().trim();
	            String command = content.split(":")[0].trim();
	            String params = content.split(":")[1].trim();
	            if (command.equals("begin to work")){
	        	logger.info(content);
	        	smsUtil.SmsSend(params);
	            } else {
	        	logger.info("error message");
	            }
	        }
	    } catch (Exception e){
		e.printStackTrace();
	    }
	}
    }
    
    //~ Getters and Setters ============================================================================================
    public JmsTemplate getJmsTemplate() {
        return jmsTemplate;
    }

    public void setJmsTemplate(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    public Destination getDestination() {
        return destination;
    }

    public void setDestination(Destination destination) {
        this.destination = destination;
    }

    public SmsUtil getSmsUtil() {
        return smsUtil;
    }

    public void setSmsUtil(SmsUtil smsUtil) {
        this.smsUtil = smsUtil;
    }

}
