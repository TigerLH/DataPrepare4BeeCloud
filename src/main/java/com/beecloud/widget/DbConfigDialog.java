package com.beecloud.widget;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import javax.xml.parsers.ParserConfigurationException;

import com.beecloud.Util.XmlHelper;
import com.beecloud.main.JWindow;

/**
 * 
 * @description //TODO
 * @author hong.lin@beecloud.com
 * @date 2016年11月1日 下午6:11:52
 * @version v1.0
 */
public class DbConfigDialog extends JDialog{
	private JTextField textField_ip;
	private JTextField textField_dbname;
	private JTextField textField_port;
	private JTextField textField_username;
	private JTextField textField_password;
	public DbConfigDialog() {
		this.setVisible(true);
		this.setSize(400,300);	  //设置大小
		this.setLocationRelativeTo(null);	//设置居中显示
		this.setAlwaysOnTop(true);
		
		JLabel label1 = new JLabel("\u7528\u6237\u540D:");
	    textField_username = new JTextField(20);
	    textField_password = new JTextField(20);
	    textField_ip = new JTextField(20);
	    textField_port = new JTextField(20);
	    textField_dbname = new JTextField(20);
	    textField_username.setText("root");
	    textField_password.setText("BeeCloud2016@");
	    textField_ip.setText("10.28.4.25");
	    textField_port.setText("3306");
	    JPanel panel = new JPanel();
	    JLabel label2 = new JLabel("\u5BC6\u7801:");
	    getContentPane().add(panel,FlowLayout.LEFT);
	    JButton ok = new JButton("确定");
	    ok.setHorizontalAlignment(SwingConstants.LEFT);
	    ok.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO 自动生成的方法存根
				String ip = textField_ip.getText();
				String port = textField_port.getText();
				String userName = textField_username.getText();
				String passWord = textField_password.getText();
				String dbName = textField_dbname.getText();
				try {
					try {
						XmlHelper.createXml("dbconfig",ip, port, userName, passWord, dbName);
					} catch (IOException e1) {
						// TODO 自动生成的 catch 块
						e1.printStackTrace();
					}
				} catch (ParserConfigurationException e1) {
					// TODO 自动生成的 catch 块
					e1.printStackTrace();
				}
				try {
					JWindow.updateWindow();
				} catch (Exception e1) {
					// TODO 自动生成的 catch 块
					e1.printStackTrace();
				}
				dispose();
			}
	    	
	    });
	    JLabel lblUrl = new JLabel("\u4E3B\u673A\u540D\u6216IP\u5730\u5740:");
	    

	    JLabel label = new JLabel("\u6570\u636E\u5E93\u540D\u79F0:");
	    
	 
	    
	    JLabel label_1 = new JLabel("\u7AEF\u53E3:");
	    
	   

	    GroupLayout gl_panel = new GroupLayout(panel);
	    gl_panel.setHorizontalGroup(
	    	gl_panel.createParallelGroup(Alignment.LEADING)
	    		.addGroup(gl_panel.createSequentialGroup()
	    			.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
	    				.addGroup(gl_panel.createSequentialGroup()
	    					.addGap(27)
	    					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
	    						.addComponent(label2)
	    						.addComponent(label1)
	    						.addComponent(lblUrl)
	    						.addComponent(label)
	    						.addComponent(label_1))
	    					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
	    						.addGroup(gl_panel.createSequentialGroup()
	    							.addPreferredGap(ComponentPlacement.RELATED)
	    							.addComponent(textField_dbname, 156, 156, 156))
	    						.addGroup(gl_panel.createSequentialGroup()
	    							.addPreferredGap(ComponentPlacement.RELATED)
	    							.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
	    								.addComponent(textField_ip, GroupLayout.PREFERRED_SIZE, 156, GroupLayout.PREFERRED_SIZE)
	    								.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
	    									.addComponent(textField_password, 156, 156, Short.MAX_VALUE)
	    									.addComponent(textField_username, GroupLayout.DEFAULT_SIZE, 156, Short.MAX_VALUE)
	    									.addComponent(textField_port, GroupLayout.DEFAULT_SIZE, 156, Short.MAX_VALUE))))))
	    				.addGroup(gl_panel.createSequentialGroup()
	    					.addGap(164)
	    					.addComponent(ok)))
	    			.addGap(173))
	    );
	    gl_panel.setVerticalGroup(
	    	gl_panel.createParallelGroup(Alignment.LEADING)
	    		.addGroup(gl_panel.createSequentialGroup()
	    			.addGap(28)
	    			.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
	    				.addComponent(lblUrl)
	    				.addComponent(textField_ip, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
	    			.addGap(12)
	    			.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
	    				.addComponent(label_1)
	    				.addComponent(textField_port, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
	    			.addGap(12)
	    			.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING)
	    				.addComponent(label1)
	    				.addComponent(textField_username, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
	    			.addPreferredGap(ComponentPlacement.UNRELATED)
	    			.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
	    				.addComponent(textField_password, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
	    				.addComponent(label2))
	    			.addPreferredGap(ComponentPlacement.UNRELATED)
	    			.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING)
	    				.addComponent(label)
	    				.addComponent(textField_dbname, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
	    			.addGap(15)
	    			.addComponent(ok)
	    			.addGap(99))
	    );
	    panel.setLayout(gl_panel);
	}
}
