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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.jms.Destination;
import javax.jms.TextMessage;
import javax.swing.JTextArea;

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
    public void receive(JTextArea ta){
	/* 设置服务启动提示信息 */
	logger.info("begin to listen the message queue");
	DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	ta.append("["+format.format(new Date())+"]短信服务已开启,正在监听消息队列\n");
	ta.setCaretPosition(ta.getText().length());
	/* 进入监听状态 */
	while(true){
	    try {
		/* 接收消息 */
		TextMessage txtmsg = (TextMessage) jmsTemplate.receive(destination);
	        if (null != txtmsg){
	            /* 如果消息不为null,解析消息 */
	            String content = txtmsg.getText().trim();
	            ta.append("["+format.format(new Date())+"]"+content+"\n");
	            ta.setCaretPosition(ta.getText().length());
	            if(ta.getLineCount()>300){
	        	int i = ta.getText().indexOf("\n");
	        	ta.getDocument().remove(0, i);
	            }
	            /* 获取消息命名部分 */
	            String command = content.split(":")[0].trim();
	            if (command.equals("begin to send")){
	        	/* 如果接收到begin to send命令,解析并获取参数 */
	        	logger.info("receive command to send SMS");
	        	String params = content.split(":")[1].trim();
	        	/* 调用smsUtil对象发送短信 */
	        	smsUtil.SmsSend(params);
	            } else if(command.equals("stop service")) {
	        	/* 如果接收到stop service命令,退出监听,结束服务 */
	        	logger.info("receive command to stop service");
	        	break;
	            } else {
	        	logger.info("receive non-related message");
	            }
	        }
	    } catch (Exception e){
		e.printStackTrace();
	    }
	}
	logger.info("exit to listen the message queue");
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
