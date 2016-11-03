package com.beecloud.widget;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/** 
 * @author  linhong: 
 * @date 2016年5月10日 下午6:46:41 
 * @Description: TODO
 * @version 1.0  
 */
public class RedisPanel extends JPanel{
	public static JTextArea textField;
	public RedisPanel() {
		 // 获取底部任务栏高度
		Dimension scrSize=Toolkit.getDefaultToolkit().getScreenSize();
		int width = scrSize.width;
		int height = scrSize.height-90;
		this.setSize(width-250,height-90);
		textField = new JTextArea();
		//textField.setColumns(10);
		textField.setEditable(true);
		textField.setLineWrap(true);
		JScrollPane jsp = new JScrollPane(textField);
		jsp.setBounds(250, -2, width-250,height-90);
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addComponent(jsp, GroupLayout.DEFAULT_SIZE, 1082, Short.MAX_VALUE)
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addComponent(jsp, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 588, Short.MAX_VALUE)
		);
		setLayout(groupLayout);
	}

}
