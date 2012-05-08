/**
 * Author            : Elan
 * Created On        : 2012-5-7 下午03:31:45
 * 
 * Copyright 2012.  All rights reserved. 
 *
 * Revision History
 * 
 *    Date       Modifier       Comments
 * ----------    -------------  --------------------------------------------
 * 
 */
package org.pmp.sms.client;

import org.apache.log4j.Logger;
import org.pmp.sms.dao.SMSSendDAO;
import org.pmp.sms.jms.JmsSubscriber;
import org.pmp.sms.util.SpringUtil;

/**
 * @author Elan
 * @version 1.0
 * @update TODO
 */
public class SmsClient {

    //~ Static Fields ==================================================================================================
    private static Logger logger = Logger.getLogger(SmsClient.class.getName());

    //~ Methods ========================================================================================================
    public static void main(String args[]){
	logger.info("sms client begin to work");
	JmsSubscriber consumer = (JmsSubscriber)SpringUtil.getBean("msgReceiver");
        consumer.receive();
        logger.info("sms client end work");
    }
}
