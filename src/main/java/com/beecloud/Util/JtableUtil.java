package com.beecloud.Util;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

public class JtableUtil {
	private static  Map<String,Object> map;
	public static void FitTableColumns(JTable myTable){
		  JTableHeader header = myTable.getTableHeader();
		     int rowCount = myTable.getRowCount();
		     Enumeration columns = myTable.getColumnModel().getColumns();
		     while(columns.hasMoreElements()){
		         TableColumn column = (TableColumn)columns.nextElement();
		         int col = header.getColumnModel().getColumnIndex(column.getIdentifier());
		         int width = (int)myTable.getTableHeader().getDefaultRenderer()
		                 .getTableCellRendererComponent(myTable, column.getIdentifier()
		                         , false, false, -1, col).getPreferredSize().getWidth();
		         for(int row = 0; row<rowCount; row++){
		             int preferedWidth = (int)myTable.getCellRenderer(row, col).getTableCellRendererComponent(myTable,
		               myTable.getValueAt(row, col), false, false, row, col).getPreferredSize().getWidth();
		             width = Math.max(width, preferedWidth);
		         }
		         header.setResizingColumn(column);
		         column.setWidth(width+myTable.getIntercellSpacing().width);
		     }
	}
	
	/**
	 * 保存当前JTablePane中的Table数据
	 * @param tabbedPane
	 * @param tabMap
	 * @return
	 * @throws ParserConfigurationException
	 * @throws IOException
	 * @throws SAXException
	 */
	public static boolean saveData(JTabbedPane tabbedPane,Map<String,String> tabMap) throws ParserConfigurationException, IOException, SAXException{
		int current_tab = tabbedPane.getSelectedIndex();
		String tabName = tabbedPane.getTitleAt(current_tab);
		String dbName = tabMap.get(tabName);
		JScrollPane js = (JScrollPane)tabbedPane.getComponentAt(current_tab);
		JTable table =(JTable)js.getViewport().getView();
		
		List<Map<String,String>> values = new ArrayList<Map<String,String>>();
		TableModel tableModel = table.getModel();
		int column_Count = tableModel.getColumnCount();	//列数
		int row_Count = tableModel.getRowCount();	//行数
		for(int j=0;j<row_Count;j++){
			String type = (String) table.getModel().getValueAt(j, column_Count-1);
			if(type==null){
				break;
			}else{
				Map<String,String> map = new HashMap<String,String>();
				for(int c=0;c<column_Count;c++){
					Object value = table.getModel().getValueAt(j,c);
					if(value==null){
						value = new String("");
					}
					map.put(tableModel.getColumnName(c), value.toString());
				}
				values.add(map);
			}
		}
		return XmlHelper.saveDataXml("data", dbName, tabName, values);
	}
	
	
	
	
	/**
	 * 复制粘贴功能
	 * 复制:ctrl+c
	 * 粘贴:ctrl+v
	 */
	public static void copyCellsEnable(final JTable table){
		table.addKeyListener(new KeyListener(){
			@Override
			public void keyPressed(KeyEvent e) {
				/**
				 * 由于存在空数据,复制时保存列名，确保对应关系不错乱
				 * 监听到Ctrl+C时,触发
				 */
				 if (e.isControlDown() && e.VK_C == e.getKeyCode()){
					 map = new HashMap<String,Object>();
					 TableModel tableModel = table.getModel();
					 int column_Count = tableModel.getColumnCount();	//table列数
					 int row = table.getSelectedRow();	//选中的行
					 for(int i = 0;i<column_Count;i++){
						 String name = table.getColumnName(i);
						 Object value = table.getValueAt(row, i);
						 map.put(name, value);
					 }
					 System.out.println("选中行:"+row);
					 System.out.println(map);
				 }
				
				
				
				
				 /**
				  * 粘贴功能
				  */
				 if (e.isControlDown() && e.VK_V == e.getKeyCode())
				 	{
					 	List<String> list = new ArrayList<String>();
					 	if(map!=null){
					 		 TableModel tableModel = table.getModel();
							 int column_Count = tableModel.getColumnCount();	//table列数
							 int row = table.getSelectedRow();	//选中的行
							 for(int i = 0;i<column_Count;i++){
								 String name = table.getColumnName(i);
								 list.add(name);
							 }
							 
							 for(int c = 0;c<list.size();c++){
								 table.setValueAt(map.get(list.get(c)), row,c);
							 }
					 	}
	              	}
				 
				 
				 	/**
					 * 删除数据
					 * 监听到Ctrl+D时,触发
					 */
					 if (e.isControlDown() && e.VK_D == e.getKeyCode()){
						 Map<String,Object> lastrow_map = new HashMap<String,Object>();
						 List<String> list = new ArrayList<String>();
						 TableModel tableModel = table.getModel();
						 int column_Count = tableModel.getColumnCount();	//table列数
						 int row = table.getSelectedRow();	//选中的行
						 int rowcount = tableModel.getRowCount();
						 for(int i = 0;i<column_Count;i++){
								String name = table.getColumnName(i);
								list.add(name);
							}
						 
						int lastline = 0;
						for(int i=0;i<rowcount;i++){
							if((table.getValueAt(i, 0)==null)||("").equals(table.getValueAt(i, 0))){
								lastline = i - 1;
								for(int c = 0;c<column_Count;c++){//最后一行的数据存到map中,用于删除某行数据后填充
									lastrow_map.put(list.get(c), table.getValueAt(lastline, c));
								}
								break;
							}
						 }

						if(row==lastline){								//如果选中的是最后一行则不做填充
							for(int c = 0;c<list.size();c++){
								table.setValueAt(null,lastline,c);
							}
						}else{											//选中的不是最后一行，则填充
							for(int c = 0;c<list.size();c++){
								table.setValueAt(null,lastline,c);
								table.setValueAt(lastrow_map.get(list.get(c)), row,c);
							}
						}
						table.updateUI();
					}                  
				}

			@Override
			public void keyReleased(KeyEvent arg0) {
				// TODO 自动生成的方法存根
				
			}

			@Override
			public void keyTyped(KeyEvent arg0) {
				// TODO 自动生成的方法存根
				
			}
			
		});
	}
	
	
	/**
	 * 获取tabbedPane中已添加的tab
	 * @return
	 */
	public static List<String> getTabList(JTabbedPane tabbedPane){
		List<String> list = new ArrayList<String>();
		for(int i=0;i<tabbedPane.getTabCount();i++){
			String tabName = tabbedPane.getTitleAt(i);
			list.add(tabName);
		}
		return list;
	}
	
}
