package com.beecloud.widget;

import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;

import com.beecloud.listenser.JTreeListener;

public class RedisConfigureDialog extends JDialog{
	private JTextField textField_ip;
	private JTextField textField_port;
	private JTextField textField_auth;
	private JTextField textField_index;
	
	
	public RedisConfigureDialog(Frame owner, boolean model) {
		super(owner, model);//对话模式
		this.setSize(400,400);	  //设置大小
		this.setLocationRelativeTo(null);	//设置居中显示
		this.setAlwaysOnTop(true);
	    textField_ip = new JTextField(20);
	    textField_port = new JTextField(20);
	    textField_ip.setText("192.168.1.8");
	    textField_port.setText("6379");
	   
	    
	    JPanel panel = new JPanel();
	    getContentPane().add(panel,FlowLayout.LEFT);
	    JButton ok = new JButton("确定");
	    ok.setHorizontalAlignment(SwingConstants.LEFT);
	    ok.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO 自动生成的方法存根
				String host = textField_ip.getText();
				String port = textField_port.getText();
				String auth = textField_auth.getText();
				String index = textField_index.getText();
				JTreeListener.redisConnectMap.put("host", host);
				JTreeListener.redisConnectMap.put("port", port);
				JTreeListener.redisConnectMap.put("auth", auth);
				JTreeListener.redisConnectMap.put("index", index);
				System.out.println(JTreeListener.redisConnectMap);
				dispose();
			}
	    	
	    });
	    JLabel lblUrl = new JLabel("IP:");
	    
	    
	    JLabel lblPort = new JLabel("Port:");
	    
	    JLabel lblAuth = new JLabel("Auth:");
	    
	    textField_auth = new JTextField();
	    textField_auth.setColumns(10);
	    textField_auth.setText("auth");
	    
	    JLabel lblNewLabel = new JLabel("DbIndex:");
	    
	    textField_index = new JTextField();
	    textField_index.setColumns(10);
	    textField_index.setText("0");

	    GroupLayout gl_panel = new GroupLayout(panel);
	    gl_panel.setHorizontalGroup(
	    	gl_panel.createParallelGroup(Alignment.LEADING)
	    		.addGroup(gl_panel.createSequentialGroup()
	    			.addGap(27)
	    			.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
	    				.addGroup(gl_panel.createSequentialGroup()
	    					.addPreferredGap(ComponentPlacement.RELATED)
	    					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
	    						.addComponent(lblNewLabel)
	    						.addComponent(lblAuth)))
	    				.addComponent(lblUrl)
	    				.addComponent(lblPort))
	    			.addPreferredGap(ComponentPlacement.RELATED)
	    			.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
	    				.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
	    					.addGroup(gl_panel.createSequentialGroup()
	    						.addComponent(textField_ip, GroupLayout.PREFERRED_SIZE, 294, GroupLayout.PREFERRED_SIZE)
	    						.addPreferredGap(ComponentPlacement.RELATED, 6, Short.MAX_VALUE))
	    					.addComponent(textField_port, GroupLayout.DEFAULT_SIZE, 294, Short.MAX_VALUE))
	    				.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
	    					.addComponent(textField_auth, GroupLayout.DEFAULT_SIZE, 286, Short.MAX_VALUE)
	    					.addComponent(textField_index, GroupLayout.DEFAULT_SIZE, 282, Short.MAX_VALUE)))
	    			.addGap(20))
	    		.addGroup(gl_panel.createSequentialGroup()
	    			.addGap(165)
	    			.addComponent(ok, GroupLayout.PREFERRED_SIZE, 73, GroupLayout.PREFERRED_SIZE)
	    			.addContainerGap(165, Short.MAX_VALUE))
	    );
	    gl_panel.setVerticalGroup(
	    	gl_panel.createParallelGroup(Alignment.LEADING)
	    		.addGroup(gl_panel.createSequentialGroup()
	    			.addGap(28)
	    			.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
	    				.addComponent(lblUrl)
	    				.addComponent(textField_ip, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
	    			.addGap(49)
	    			.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
	    				.addComponent(lblPort)
	    				.addComponent(textField_port, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
	    			.addGap(40)
	    			.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
	    				.addComponent(lblAuth)
	    				.addComponent(textField_auth, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
	    			.addGap(36)
	    			.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
	    				.addComponent(lblNewLabel)
	    				.addComponent(textField_index, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
	    			.addGap(28)
	    			.addComponent(ok)
	    			.addGap(18))
	    );
	    panel.setLayout(gl_panel);
	}
}
