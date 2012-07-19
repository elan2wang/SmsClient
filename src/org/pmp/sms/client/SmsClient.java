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

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.apache.log4j.Logger;
import org.pmp.sms.jms.JmsPublisher;
import org.pmp.sms.jms.JmsSubscriber;
import org.pmp.sms.util.SpringUtil;

/**
 * @author Elan
 * @version 1.0
 * @update TODO
 */
public class SmsClient extends JFrame {

    //~ Static Fields ==================================================================================================
    private static Logger logger = Logger.getLogger(SmsClient.class.getName());

    //~ Fields  ========================================================================================================
    private static JmsSubscriber consumer = (JmsSubscriber) SpringUtil.getBean("msgReceiver");
    private JmsPublisher publisher = (JmsPublisher) SpringUtil.getBean("commandPublisher");
    
    private JButton endBtn;
    private static JTextArea displayPad;
    
    public SmsClient(){
	super("舟山物业管理系统短信发送服务");
	Container container = getContentPane();
	displayPad = new JTextArea();
	endBtn = new JButton("结束短信服务");
	
	container.add(endBtn,BorderLayout.NORTH);
	container.add(endBtn,BorderLayout.SOUTH);
	container.add(new JScrollPane(displayPad),BorderLayout.CENTER);
	
	endBtn.addActionListener(new ActionListener(){
	    public void actionPerformed(ActionEvent e){
		publisher.sendMessgae("stop:0");
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		displayPad.append("["+format.format(new Date())+"]短信服务已关闭，无法接收短信发送命令。\n");
		displayPad.setCaretPosition(displayPad.getText().length());
	    }
	});
    }
    
    //~ Methods ========================================================================================================
    public static void main(String args[]){
	SmsClient client=new SmsClient();
	client.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	client.setSize(350,300);
	client.setVisible(true);
	consumer.receive(displayPad);
	logger.info("短信服务结束监听");
    }

}
