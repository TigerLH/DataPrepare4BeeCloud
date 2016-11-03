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

import com.beecloud.main.JWindow;

public class DbCopyDialog extends JDialog{
	private JTextField textField_ip;
	private JTextField textField_dbname;
	private JTextField textField_port;
	private JTextField textField_username;
	private JTextField textField_password;
	private JTextField textField_limit;
	private JTextField textField_table;
	private JTextField textField_search;
	
	
	public DbCopyDialog(Frame owner, boolean model) {
		super(owner, model);//对话模式
		this.setSize(400,400);	  //设置大小
		this.setLocationRelativeTo(null);	//设置居中显示
		this.setAlwaysOnTop(true);
		JLabel label1 = new JLabel("\u7528\u6237\u540D:");
	    textField_username = new JTextField(20);
	    textField_password = new JTextField(20);
	    textField_ip = new JTextField(20);
	    textField_dbname = new JTextField(20);
	    textField_port = new JTextField(20);
	    textField_table = new JTextField();
	    textField_table.setColumns(10);
	    textField_limit = new JTextField();
	    textField_limit.setColumns(10);
	    textField_username.setText("root");
	    textField_password.setText("BeeCloud2016@");
	    textField_ip.setText("10.28.4.25");
	    textField_port.setText("3306");
	    if(JWindow.dbcopymap.get("dbname")!=null){	//根据选中的JTree节点自动填充数据库名称
	    	textField_dbname.setText(JWindow.dbcopymap.get("dbname").trim());
	    }
	    
	    if(JWindow.dbcopymap.get("table")!=null){	//根据选中的JTree节点自动填充数据表名称
	    	if(JWindow.dbcopymap.get("table").contains("√")){
	    		textField_table.setText(JWindow.dbcopymap.get("table").replace("√", "").trim());
	    	}else{
	    		textField_table.setText(JWindow.dbcopymap.get("table"));
	    	}
	    }
	    
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
				String limit = textField_limit.getText();
				String tableName = textField_table.getText();
				String condition = textField_search.getText();
				JWindow.dbcopymap.put("table", tableName);
				JWindow.dbcopymap.put("username", userName);
				JWindow.dbcopymap.put("password", passWord);
				JWindow.dbcopymap.put("host", ip);
				JWindow.dbcopymap.put("port", port);
				JWindow.dbcopymap.put("dbname", dbName);
				JWindow.dbcopymap.put("condition", condition);
				JWindow.dbcopymap.put("limit", limit);
				System.out.println(JWindow.dbcopymap);
				dispose();
			}
	    	
	    });
	    JLabel lblUrl = new JLabel("\u4E3B\u673A\u540D\u6216IP\u5730\u5740:");
	    
	    JLabel label = new JLabel("\u6570\u636E\u5E93\u540D\u79F0:");
	    
	    
	    JLabel label_1 = new JLabel("\u7AEF\u53E3:");
	    
	    JLabel label_2 = new JLabel("\u590D\u5236\u6761\u6570:");
	    
	    JLabel label_3 = new JLabel("\u8868\u540D\u79F0:");
	    
	    JLabel lblNewLabel = new JLabel("\u67E5\u8BE2\u6761\u4EF6:");
	    
	    textField_search = new JTextField();
	    textField_search.setColumns(10);
	    

	    GroupLayout gl_panel = new GroupLayout(panel);
	    gl_panel.setHorizontalGroup(
	    	gl_panel.createParallelGroup(Alignment.LEADING)
	    		.addGroup(gl_panel.createSequentialGroup()
	    			.addGap(170)
	    			.addComponent(ok)
	    			.addContainerGap(223, Short.MAX_VALUE))
	    		.addGroup(gl_panel.createSequentialGroup()
	    			.addGap(27)
	    			.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
	    				.addComponent(label2)
	    				.addComponent(label1)
	    				.addComponent(lblUrl)
	    				.addComponent(label)
	    				.addComponent(label_1)
	    				.addComponent(label_3)
	    				.addComponent(label_2)
	    				.addComponent(lblNewLabel))
	    			.addPreferredGap(ComponentPlacement.RELATED)
	    			.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
	    				.addComponent(textField_search, GroupLayout.DEFAULT_SIZE, 156, Short.MAX_VALUE)
	    				.addComponent(textField_limit, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 156, Short.MAX_VALUE)
	    				.addComponent(textField_table, GroupLayout.DEFAULT_SIZE, 156, Short.MAX_VALUE)
	    				.addComponent(textField_dbname, 156, 156, Short.MAX_VALUE)
	    				.addComponent(textField_ip, GroupLayout.DEFAULT_SIZE, 156, Short.MAX_VALUE)
	    				.addComponent(textField_password, 156, 156, Short.MAX_VALUE)
	    				.addComponent(textField_username, GroupLayout.DEFAULT_SIZE, 156, Short.MAX_VALUE)
	    				.addComponent(textField_port, GroupLayout.DEFAULT_SIZE, 156, Short.MAX_VALUE))
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
	    			.addGap(17)
	    			.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
	    				.addComponent(label_3)
	    				.addComponent(textField_table, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
	    			.addPreferredGap(ComponentPlacement.UNRELATED)
	    			.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
	    				.addComponent(textField_search, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
	    				.addComponent(lblNewLabel))
	    			.addPreferredGap(ComponentPlacement.RELATED)
	    			.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
	    				.addComponent(textField_limit, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
	    				.addComponent(label_2))
	    			.addPreferredGap(ComponentPlacement.RELATED)
	    			.addComponent(ok))
	    );
	    panel.setLayout(gl_panel);
	}
}
