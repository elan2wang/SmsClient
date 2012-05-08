/**
 * Author            : Elan
 * Created On        : 2012-5-7 下午03:17:10
 * 
 * Copyright 2012.  All rights reserved. 
 *
 * Revision History
 * 
 *    Date       Modifier       Comments
 * ----------    -------------  --------------------------------------------
 * 
 */
package org.pmp.sms.util;

import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author Elan
 * @version 1.0
 * @update TODO
 */
public class SpringUtil {

    //~ Static Fields ==================================================================================================
    private static Logger logger = Logger.getLogger(SpringUtil.class.getName());
    //~ Instance Fields ================================================================================================
    private static ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
    
    //~ Methods ========================================================================================================
    public static Object getBean(String name) throws BeansException{
	return applicationContext.getBean(name);
    }
    
    public static boolean containsBean(String name) {
	return applicationContext.containsBean(name);
    }
}
