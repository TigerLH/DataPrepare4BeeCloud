package com.beecloud.widget;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.SystemColor;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
public class MyTabPanel extends JPanel implements MouseListener{
	private JTabbedPane tabbedPane;
	private JLabel title;
	public MyTabPanel(String tableName,JTabbedPane tabbedPane){
		this.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
		title = new JLabel(tableName);
		title.setOpaque(true);	//设置组件JLabel不透明，只有设置为不透明，设置背景色才有效  
		title.setBackground(tabbedPane.getBackground());
		this.add(title);
		JButton jButton = new JButton();
		jButton.setForeground(SystemColor.activeCaption);
		jButton.setBackground(UIManager.getColor("TabbedPane.background"));
		jButton.setPreferredSize(new Dimension(17,17));
		ImageIcon deleteIcon = new ImageIcon(getClass().getClassLoader().getResource("images/delete.png"));
		jButton.setIcon(deleteIcon);
		this.add(jButton);
		this.tabbedPane = tabbedPane;
		jButton.addMouseListener(this);
	}
	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO 自动生成的方法存根
		tabbedPane.remove(tabbedPane.indexOfTabComponent(this));
	}
	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO 自动生成的方法存根
		
	}
	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO 自动生成的方法存根
		
	}
	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO 自动生成的方法存根
		
	}
	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO 自动生成的方法存根
		
	}
	
	
}



