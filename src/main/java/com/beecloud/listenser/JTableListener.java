package com.beecloud.listenser;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTable;

/**
 * 
 * @description 在数据类型为Check的行显示悬浮按钮    row为当前选中的数据类型为check的行
 * @author hong.lin@beecloud.com
 * @date 2016年11月2日 下午2:30:00
 * @version v1.0
 */
public class JTableListener implements ActionListener,MouseListener{
	public JTable table;
	private JPopupMenu TablepopupMenu;
	private JMenuItem notEqual;
	private JMenuItem  equal;
	public JTableListener(JTable table){
		this.table = table;
		table.addMouseListener(this);
		ImageIcon equalIcon = new ImageIcon(getClass().getClassLoader().getResource("images/nequal.png"));
		ImageIcon nequalIcon = new ImageIcon(getClass().getClassLoader().getResource("images/equal.png"));
		TablepopupMenu = new JPopupMenu();
		notEqual = new JMenuItem();
		notEqual.setIcon(nequalIcon);
		notEqual.addActionListener(this);
		equal = new JMenuItem();
		equal.setIcon(equalIcon);
		equal.addActionListener(this);
		TablepopupMenu.add(notEqual);
		TablepopupMenu.add(equal);
	}
	
	
	
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO 自动生成的方法存根
		
	}
	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO 自动生成的方法存根
		
	}
	@Override
	public void mouseExited(MouseEvent e) {
		// TODO 自动生成的方法存根
		
	}
	@Override
	public void mousePressed(MouseEvent e) {
		 //右键选中单元格获取焦点
		if(e.getButton()==3){
			Point p = e.getPoint();
	        int row = table.rowAtPoint(p);
	        int column = table.columnAtPoint(p);
	        table.changeSelection(row, column, e.isControlDown(), e.isShiftDown());
		}
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
		//鼠标抬起时所在的行,不能在mousePressed中
		if(e.getButton()==3){//右键点击触发
			String typeValue = (String)table.getModel().getValueAt(table.getSelectedRow(), table.getColumnCount()-1);
			if(("check").equals(typeValue)){
				TablepopupMenu.show(table, e.getX(), e.getY());
			}
		}
	}
		
	@Override
	public void actionPerformed(ActionEvent e) {
		/**
		 * Table悬浮按钮:不相等
		 */
		if(e.getSource()==notEqual){
			int row = table.getSelectedRow();
			int column = table.getSelectedColumn();
			String value =(String)table.getModel().getValueAt(row, column);
			table.setValueAt("[NE]"+value, table.getSelectedRow(), table.getSelectedColumn());
			table.updateUI();
		}
		
		/**
		 * Table悬浮按钮:相等
		 */
		if(e.getSource()==equal){
			int row = table.getSelectedRow();
			int column = table.getSelectedColumn();
			String value = (String)table.getModel().getValueAt(row, column);
			table.setValueAt("[EQ]"+value, table.getSelectedRow(), table.getSelectedColumn());
			table.updateUI();
		}
	}
}
