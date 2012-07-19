/**
 * Author            : Elan
 * Created On        : 2012-5-19 下午11:24:58
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
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.apache.log4j.Logger;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

/**
 * @author Elan
 * @version 1.0
 * @update TODO
 */
public class JmsPublisher {

    //~ Static Fields ==================================================================================================
    private static Logger logger = Logger.getLogger(JmsPublisher.class.getName());
    //~ Instance Fields ================================================================================================
    private JmsTemplate jmsTemplate;
    private Destination destination;
    
    //~ Constructor ====================================================================================================

    //~ Methods ========================================================================================================
    public void sendMessgae(final String command){
	logger.debug("begin to send a command to SmsClient");
	jmsTemplate.send(destination, new MessageCreator() {  
            public Message createMessage(Session session) throws JMSException {
                return session.createTextMessage(command);  
            }  
        }); 
	logger.debug("successfully send a command to SmsClient");
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
    
}
