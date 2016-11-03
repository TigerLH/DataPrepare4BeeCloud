package com.beecloud.listenser;

import java.awt.Color;
import java.awt.Component;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.DocumentException;
import org.xml.sax.SAXException;

import com.beecloud.Util.DbUtil;
import com.beecloud.Util.FileUtil;
import com.beecloud.Util.JTreeUtil;
import com.beecloud.Util.JsonUtil;
import com.beecloud.Util.JtableUtil;
import com.beecloud.Util.RedisUtil;
import com.beecloud.Util.XmlHelper;
import com.beecloud.bean.ColumnInfo;
import com.beecloud.bean.config.DbInfo;
import com.beecloud.bean.config.RedisInfo;
import com.beecloud.main.JWindow;
import com.beecloud.widget.DbConfigDialog;
import com.beecloud.widget.DbCopyDialog;
import com.beecloud.widget.MyTabPanel;
import com.beecloud.widget.RedisConfigureDialog;
import com.beecloud.widget.RedisLoadKeyByRegexDialog;
import com.beecloud.widget.RedisPanel;

/**
 * 
 * @description //TODO
 * @author hong.lin@beecloud.com
 * @date 2016年10月31日 下午2:29:33
 * @version v1.0
 */
public class JTreeListener implements ActionListener,MouseListener,TreeSelectionListener,KeyListener{
	public JTabbedPane tabbedPane;
	public JTree tree;
	private JPopupMenu popupMenu;
	private JMenuItem newMysqlItem;
	private JMenuItem loadMysqlItem;
	private JMenuItem copyMysqlItem;
	private JMenuItem delMysqlItem;//删除数据库配置悬浮按钮
	private JMenuItem addMysqlItem;//添加数据库配置悬浮按钮
	private JMenuItem addRedisItem;
	
	private JMenuItem delItem;//删除Key
	private JMenuItem delDbItem;//Redis名称删除
	private JMenuItem loadByTypeItem;//从本地导入数据
	private JMenuItem loadItem;//导入Key
	private Point mousePoint; //鼠标所在位置
	
	private final int defaultTableSize = 30;
	public static String selectedNodeName ="";  //二级节点名称，区分是Mysql还是Redis
	
	/**
	 * 存储连接redis所需要的ip/port/auth
	 */
	public static Map<String,String> redisConnectMap = new HashMap<String,String>();
	
	
	/**
	 * redis数据类型Map,key:数据类型
	 */
	public static Map<String,Map<String,String>> redisDataMap = new HashMap<String,Map<String,String>>();
	/**
	 * key:Redis名称    value:对应的RedisInfo对象
	 */
	public static Map<String,RedisInfo> redisInfoMap = new HashMap<String,RedisInfo>();
	/**
	 * 工具显示宽度
	 */
	private int width ;
	
	/**
	 * 工具显示高度
	 */
	private int height;
	/**
	 * redis导入过滤规则
	 */
	public static String regex;
	private RedisPanel redisPanel;
	
	
	public JTreeListener(JTabbedPane tabbedPane,RedisPanel redisPanel,JTree tree){
		 this.tabbedPane = tabbedPane;
		 this.tree = tree;
		 this.redisPanel = redisPanel;
		 tree.addMouseListener(this);
		 tree.addTreeSelectionListener(this);
		 tree.addKeyListener(this);
		 redisPanel.textField.addKeyListener(this);
		 popupMenu = new JPopupMenu();
	}
	
	
	/**
	 * 设置不允许为空的标示
	 */
	public void setTag4NotNullAble(DbInfo dbinfo,String tableName,JTable table){
		List<ColumnInfo> list = DbUtil.getColumnInfo(dbinfo.getUserName(), dbinfo.getPassword(), dbinfo.getUrl(),tableName);
		DefaultTableCellRenderer HeaderRenderer = new DefaultTableCellRenderer(){
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)  
            {
                JComponent comp = (JComponent) super.getTableCellRendererComponent(table, value, isSelected, hasFocus,row, column);
                comp.setBackground(Color.RED);
                comp.setBorder(BorderFactory.createRaisedBevelBorder());               
                return comp;
            }
		};
		for(int i=0;i<list.size();i++){
			if(!list.get(i).isNullable()){
				TableColumn table_cloumn = table.getTableHeader().getColumnModel().getColumn(i);
				table_cloumn.setHeaderRenderer(HeaderRenderer);
			}
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		/**
		 * 添加悬浮按钮
		 */
		if(e.getSource()==addMysqlItem){
			DbConfigDialog dialog = new DbConfigDialog();
			dialog.show();
			return;
		}
		
		if(e.getSource()==addRedisItem){
			RedisConfigureDialog addDialog = new RedisConfigureDialog(JWindow.context, true);
			addDialog.show();
			String host = redisConnectMap.get("host");
			int port = Integer.parseInt(redisConnectMap.get("port"));
			String auth = redisConnectMap.get("auth");
			int index = Integer.parseInt(redisConnectMap.get("index"));
			String name = "redis_"+StringUtils.substring(UUID.randomUUID().toString(), 24);
			try {
				XmlHelper.createRedisXml("dbconfig", name, host, port, index, auth);	//保存Redis配置文件
			} catch (ParserConfigurationException e1) {
				// TODO 自动生成的 catch 块
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO 自动生成的 catch 块
				e1.printStackTrace();
			}
			DefaultMutableTreeNode parent = (DefaultMutableTreeNode)tree.getModel().getRoot();//根节点
			JTreeUtil.addTreeNode(tree,parent,name);
			RedisInfo redisInfo = new RedisInfo();
			redisInfo.setHost(host);
			redisInfo.setPort(port);
			redisInfo.setAuth(auth);
			redisInfo.setIndex(index);
			redisInfoMap.put(name, redisInfo);
			DefaultMutableTreeNode child = (DefaultMutableTreeNode) parent.getChildAt(0);
			JTreeUtil.addTreeNode(tree,child,"depend");
			JTreeUtil.addTreeNode(tree,child,"target");
			JTreeUtil.addTreeNode(tree,child,"check");
			redisPanel.setVisible(true);
			return;
		}
		
		
		/**
		 * 删除悬浮按钮
		 */
		if(e.getSource()==delMysqlItem){
			DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();//获取选中的节点
			String filename = JWindow.fileMap.get(selectedNode.toString());
			System.out.println("删除文件:"+filename);
			FileUtil.delXml(filename);//删除数据配置文件
			File dir = new File("data");
			File[] files = dir.listFiles();
			for(int i=0;i<files.length;i++){
				if(files[i].getName().contains(selectedNode.toString())){
					FileUtil.delXml(files[i].getName());
				}
			}
			try {
				JWindow.updateWindow();
			} catch (Exception e1) {
				// TODO 自动生成的 catch 块
				e1.printStackTrace();
			}
			return;
		}
		
		/**
		 * 新建数据
		 */
		if(e.getSource()==newMysqlItem){
			DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();//获取选中的节点
			String table_select_name = selectedNode.toString().replace("√", "").trim();
			if(JtableUtil.getTabList(tabbedPane).contains(table_select_name)){
				 JOptionPane.showMessageDialog(null, "数据添加至Tab中", "提示", JOptionPane.CLOSED_OPTION);
				return;
			}
			final JTable table = new JTable();
			table.setFillsViewportHeight(true);
			table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			JtableUtil.copyCellsEnable(table);
			//JtableUtil.FitTableColumns(table);
			JScrollPane scrollPane = new JScrollPane(table);
			tabbedPane.addTab(table_select_name, scrollPane);
			tabbedPane.setTabComponentAt(tabbedPane.indexOfComponent(scrollPane), new MyTabPanel(table_select_name,tabbedPane));
			if(tabbedPane.getTabCount()>0){
				tabbedPane.setSelectedIndex(tabbedPane.getTabCount()-1);//自动选中新建的tab页
			}
			DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode)selectedNode.getParent();
			DbInfo dbInfo = JWindow.dbMap.get(parentNode.toString());
			JWindow.tabMap.put(table_select_name, parentNode.toString());
			
			List<String> table_list = DbUtil.getTableList(dbInfo.getUserName(),dbInfo.getPassword(),dbInfo.getUrl());
			if(table_list.size()>0){
				List<String> field_list = DbUtil.getFieldList(dbInfo.getUserName(),dbInfo.getPassword(),dbInfo.getUrl(), table_select_name);
				DefaultTableModel model = new DefaultTableModel(field_list.toArray(), defaultTableSize);
				table.setModel(model);
				setTag4NotNullAble(dbInfo, table_select_name, table);
				TableColumn tableColumn = table.getColumn("数据类型");	//type通过下拉选择
				final JComboBox<String> comboBox = new JComboBox<String>();
				comboBox.setEditable(true);
		        comboBox.addItem("target");
		        comboBox.addItem("depend");
		        comboBox.addItem("check");  
				tableColumn.setCellEditor((TableCellEditor) new DefaultCellEditor(comboBox)); 
				comboBox.addItemListener(new ItemListener(){
					@Override
					public void itemStateChanged(ItemEvent event) {
						if(event.getStateChange() == ItemEvent.SELECTED){
							if(("check").equals(comboBox.getSelectedItem())){
								new JTableListener(table);
							}
						}
					}
				});
			}
			return;
		}
		
		
		/**
		 * 从数据库导入
		 */
		if(e.getSource()==copyMysqlItem){
			DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();//获取选中的节点
			String table_select_name = selectedNode.toString().replace("√", "").trim();
			if(JtableUtil.getTabList(tabbedPane).contains(table_select_name)){
				 JOptionPane.showMessageDialog(null, "数据已添加至Tab中", "提示", JOptionPane.CLOSED_OPTION);
				return;
			}
			DefaultMutableTreeNode selectNode = (DefaultMutableTreeNode)selectedNode;
			String table_Name = selectNode.toString();
			String db_Name = selectNode.getParent().toString();
			JWindow.dbcopymap.put("dbname", db_Name);
			JWindow.dbcopymap.put("table", table_Name);
			DbCopyDialog dialog = new DbCopyDialog(JWindow.context,true);
			dialog.show();
			final JTable table = new JTable();
			table.setFillsViewportHeight(true);
			table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			JtableUtil.copyCellsEnable(table);
			JScrollPane scrollPane = new JScrollPane(table);
			tabbedPane.addTab(table_select_name, null, scrollPane, null);
			tabbedPane.setTabComponentAt(tabbedPane.indexOfComponent(scrollPane), new MyTabPanel(table_select_name,tabbedPane));
			if(tabbedPane.getTabCount()>0){
				tabbedPane.setSelectedIndex(tabbedPane.getTabCount()-1);//自动选中新建的tab页
			}
			DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode)selectedNode.getParent();
			JWindow.tabMap.put(table_select_name, parentNode.toString());
			DbInfo dbInfo = JWindow.dbMap.get(parentNode.toString());
			List<String> field_list = DbUtil.getFieldList(dbInfo.getUserName(),dbInfo.getPassword(),dbInfo.getUrl(), table_select_name);
			DefaultTableModel model = new DefaultTableModel(field_list.toArray(), defaultTableSize);
			table.setModel(model);
			setTag4NotNullAble(dbInfo, table_select_name, table);
			TableColumn tableColumn = table.getColumn("数据类型");	//type通过下拉选择
			final JComboBox<String> comboBox = new JComboBox<String>();
			comboBox.setEditable(true);
		    comboBox.addItem("target");  
		    comboBox.addItem("depend");
		    comboBox.addItem("check");
			comboBox.addItemListener(new ItemListener(){
				@Override
				public void itemStateChanged(ItemEvent event) {
					if(event.getStateChange() == ItemEvent.SELECTED){
						if(("check").equals(comboBox.getSelectedItem())){
							new JTableListener(table);
						}
					}
				}
			});
		    tableColumn.setCellEditor((TableCellEditor) new DefaultCellEditor(comboBox));
		    
		    String host = JWindow.dbcopymap.get("host");
		    String port = JWindow.dbcopymap.get("port");
		    String dbname = JWindow.dbcopymap.get("dbname");
		    String tablename = JWindow.dbcopymap.get("table");
		    String username = JWindow.dbcopymap.get("username");
		    String password = JWindow.dbcopymap.get("password");
		    String limit = JWindow.dbcopymap.get("limit");
		    String condition = JWindow.dbcopymap.get("condition");
		    String jdbc_url = "jdbc:mysql://"+host+":"+port+"/"+dbname+"?"+"useUnicode=true&characterEncoding=utf8&tinyInt1isBit=false";
		    List<Map<String, Object>> dbData = DbUtil.getDataFromDb(username, password, jdbc_url,tablename,condition,limit);
			Set<String> keySet = dbData.get(0).keySet();
			
			if(keySet.size()!=field_list.size()){	//字段长度不一致时,不导入
				JOptionPane.showMessageDialog(null, "导入的数据库字段不匹配", "提示", JOptionPane.CLOSED_OPTION);
				return;
			}
			
			int size = dbData.size();
			if(size>=defaultTableSize){
				model.setRowCount(size+10);
				table.updateUI();
			}
			
		    for(int i=0;i<dbData.size();i++){
				 for(int j=0;j<table.getColumnCount();j++){
					 String ColumnName = table.getColumnName(j);
					 table.setValueAt(dbData.get(i).get(ColumnName), i, j);
				 }
			 }
	}
		
		
		/**
		 * 导入数据
		 */
		if(e.getSource()==loadMysqlItem){
			DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();//获取选中的节点
			String table_select_name = selectedNode.toString().replace("√", "").trim();
			if(JtableUtil.getTabList(tabbedPane).contains(table_select_name)){
				 JOptionPane.showMessageDialog(null, "数据已添加至Tab中", "提示", JOptionPane.CLOSED_OPTION);
				return;
			}
			final JTable table = new JTable();
			table.setFillsViewportHeight(true);
			table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			JtableUtil.copyCellsEnable(table);
			JScrollPane scrollPane = new JScrollPane(table);
			tabbedPane.addTab(table_select_name, null, scrollPane, null);
			tabbedPane.setTabComponentAt(tabbedPane.indexOfComponent(scrollPane), new MyTabPanel(table_select_name,tabbedPane));
			if(tabbedPane.getTabCount()>0){
				tabbedPane.setSelectedIndex(tabbedPane.getTabCount()-1);//自动选中新建的tab页
			}
			DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode)selectedNode.getParent();
			JWindow.tabMap.put(table_select_name, parentNode.toString());
			DbInfo dbInfo = JWindow.dbMap.get(parentNode.toString());
			List<String> field_list = DbUtil.getFieldList(dbInfo.getUserName(),dbInfo.getPassword(),dbInfo.getUrl(), table_select_name);
			DefaultTableModel model = new DefaultTableModel(field_list.toArray(), defaultTableSize);
			table.setModel(model);
			setTag4NotNullAble(dbInfo, table_select_name, table);
			TableColumn tableColumn = table.getColumn("数据类型");	//type通过下拉选择
			final JComboBox<String> comboBox = new JComboBox<String>();
			comboBox.setEditable(true);
		    comboBox.addItem("target");
		    comboBox.addItem("depend");
		    comboBox.addItem("check");
			comboBox.addItemListener(new ItemListener(){
				@Override
				public void itemStateChanged(ItemEvent event) {
					if(event.getStateChange() == ItemEvent.SELECTED){
						if(("check").equals(comboBox.getSelectedItem())){
							new JTableListener(table);
						}
					}
				}
			});
		    tableColumn.setCellEditor((TableCellEditor) new DefaultCellEditor(comboBox));
		    try {
				List<Map<String, Object>> historyData = XmlHelper.loadXmlData("data"+File.separator+selectedNode.getParent().toString()+"_"+table_select_name+".xml");
				int size = historyData.size();
				if(size>=defaultTableSize){
					model.setRowCount(size+10);
					table.updateUI();
				}
				
				for(int i=0;i<historyData.size();i++){
					 for(int j=0;j<table.getColumnCount();j++){
						 String ColumnName = table.getColumnName(j);
						 Object Value = historyData.get(i).get(ColumnName);
						 table.setValueAt(Value.toString(), i, j);
					 }
				 }
			} catch (SAXException | IOException | ParserConfigurationException | DocumentException e1) {
				// TODO 自动生成的 catch 块
				e1.printStackTrace(); 
			}
		    return; 
		}
		
		
		
		/**
		 * 导入Redis数据
		 */
		if(e.getSource()==loadItem){
			RedisPanel.textField.setVisible(true);
			RedisPanel.textField.setText("");
			RedisLoadKeyByRegexDialog loadDialog = new RedisLoadKeyByRegexDialog(JWindow.context,true);
			loadDialog.setLocation(mousePoint);
			loadDialog.show();
			DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();//获取选中的节点
			DefaultMutableTreeNode parent = (DefaultMutableTreeNode) selectedNode.getParent();
			RedisInfo redisInfo  = redisInfoMap.get(parent.toString());
			RedisUtil redisUtil =  new RedisUtil(redisInfo);
			Map<String,String> map = null;
			try{
				map = redisDataMap.get(selectedNode.toString());
			}catch(Exception e1){
				System.err.println(e1.getMessage());
			}
			if(map==null){
				map = new HashMap<String,String>();
			}
			List<String> list = redisUtil.getKeys(regex);
			if(list==null){
				return;
			}
			for(String key:list){
				String value = redisUtil.getValueBykey(key);
				String jsonFormat = JsonUtil.formatJson(value);
				JTreeUtil.addTreeNode(tree,selectedNode,key);
				map.put(key, jsonFormat);
			 }
			if(("check").equals(selectedNode.toString())){
				redisDataMap.put("check", map);
			}else if(("depend").equals(selectedNode.toString())){
				redisDataMap.put("depend", map);
			}else if(("target").equals(selectedNode.toString())){
				redisDataMap.put("target", map);
			}
		}
		
		
		
		/**
		 * 从本地导入redis数据
		 */
		if(e.getSource()==loadByTypeItem){
			RedisPanel.textField.setVisible(true);
			RedisPanel.textField.setText("");
			RedisInfo redisInfo = null;
			DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();//获取选中的节点
			DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) selectedNode.getParent();
			Map<String, Map<String, String>> map = null;
			try {
				map = XmlHelper.loadRedisData("data/"+parentNode.toString()+".xml");
			} catch (ParserConfigurationException | SAXException | IOException | DocumentException e1) {
				// TODO 自动生成的 catch 块
				e1.printStackTrace();
			}
			if(selectedNode.toString().equals("check")){
				Map<String,String> checkMap = map.get("check");
				for(String item:checkMap.keySet()){
					JTreeUtil.addTreeNode(tree,selectedNode,item);
				}
			}else if(selectedNode.toString().equals("depend")){
				Map<String,String> checkMap = map.get("depend");
				for(String item:checkMap.keySet()){
					JTreeUtil.addTreeNode(tree,selectedNode,item);
				}
			}else if(selectedNode.toString().equals("target")){
				Map<String,String> checkMap = map.get("target");
				for(String item:checkMap.keySet()){
					JTreeUtil.addTreeNode(tree,selectedNode,item);
				}
			}
			try {
				 redisInfo = XmlHelper.getRedisInfo("dbconfig/"+parentNode.toString()+".xml");
			} catch (ParserConfigurationException | SAXException | IOException e1) {
				// TODO 自动生成的 catch 块
				e1.printStackTrace();
			} catch (DocumentException e1) {
				// TODO 自动生成的 catch 块
				e1.printStackTrace();
			}
			redisDataMap = map;
		}
		
		
		
		/**
		 * Redis名称删除
		 */
		if(e.getSource()==delDbItem){
			DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();//获取选中的节点
			FileUtil.delXml("dbconfig"+File.separator+selectedNode.toString()+".xml");//删除数据配置文件
			FileUtil.delXml("data"+File.separator+selectedNodeName.toString()+".xml");
			RedisPanel.textField.setVisible(false);
			RedisPanel.textField.setText("");
			try {
				JWindow.updateWindow();
			} catch (Exception e1) {
				// TODO 自动生成的 catch 块
				e1.printStackTrace();
			}
		}
		
		
		
		/**
		 * 删除子节点
		 */
		if(e.getSource()==delItem){
			DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
			DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();//获取选中的节点
			DefaultMutableTreeNode parent = (DefaultMutableTreeNode) selectedNode.getParent();
			if(selectedNode!=null&&parent!=null){
				redisDataMap.get(parent.toString()).remove(selectedNode.toString());
				model.removeNodeFromParent(selectedNode);
			}
		}
		
		
	}
	@Override
	public void mouseClicked(MouseEvent evt) {
		 TreePath path=tree.getPathForLocation(evt.getX(),evt.getY());
         tree.stopEditing();
         if(path==null){
        	 return;
         }
         DefaultMutableTreeNode node=(DefaultMutableTreeNode)path.getLastPathComponent();
         DefaultMutableTreeNode parent = (DefaultMutableTreeNode) node.getParent();
         if(evt.getClickCount()==2&&node.getLevel()==3)	//三级节点双击可编辑
         {
        		 tree.setEditable(true);
        		 tree.startEditingAtPath(path);
        		 
        	 }
         if(evt.getClickCount()==1)
         {
        	 tree.stopEditing();
        	 tree.setEditable(false);
         }	
		
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
	public void mousePressed(MouseEvent e) {
		 String parentName ="";
		 DefaultMutableTreeNode parentNode = null;
		 DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();//获取选中的节点
		 if(selectedNode==null){
			 return;
		 }
		 parentNode = (DefaultMutableTreeNode)selectedNode.getParent();//获取选中的节点
		 if(parentNode!=null){
			 parentName = parentNode.toString();
		 }

		 if(e.getSource()==tree&&selectedNode.getLevel()==0){	//屏蔽JTree外的右键点击事件
				 popupMenu.removeAll();
				 addMysqlItem = new JMenuItem("添加Mysql");
				 addRedisItem = new JMenuItem("添加Redis");
				 addMysqlItem.addActionListener(this);
				 addRedisItem.addActionListener(this);
				 popupMenu.add(addMysqlItem);
				 popupMenu.add(addRedisItem);
				 TreePath path = tree.getPathForLocation(e.getX(), e.getY());
				 if(path==null){
					 return;
				 }
				 tree.setSelectionPath(path);
				 if(e.getButton()==3){	//右键点击触发
					 popupMenu.show(tree, e.getX(), e.getY());
				 }
			 return;
		 }
		 
		 
		 if(e.getSource()==tree&&selectedNode.getLevel()==1){
			 selectedNodeName = selectedNode.toString();
			 if(selectedNodeName.contains("redis")){
				 redisPanel.setVisible(true);
				 mousePoint = new Point();
				 mousePoint.x = e.getX();
				 mousePoint.y = e.getY();
				 JPopupMenu popupMenu = new JPopupMenu();
				 delDbItem= new JMenuItem("删除");
				 delDbItem.addActionListener(this);
				 popupMenu.add(delDbItem);
				 
				 TreePath path = tree.getPathForLocation(e.getX(), e.getY());
				 if(path==null){
					 return;
				 }
				 tree.setSelectionPath(path);
				 if(e.getButton()==3){	//右键点击触发
					 popupMenu.show(tree, e.getX(), e.getY());
				 }
			 }else{
				 redisPanel.setVisible(false);
				 popupMenu.removeAll();
				 delMysqlItem = new JMenuItem("删除");
				 delMysqlItem.addActionListener(this);
				 popupMenu.add(delMysqlItem);
				 TreePath path = tree.getPathForLocation(e.getX(), e.getY());
				 if(path==null){
					 return;
				 }
				 tree.setSelectionPath(path);
				 if(e.getButton()==3){	//右键点击触发
					 popupMenu.show(tree, e.getX(), e.getY());
				 }
			 }
			 return;
		 }
		 
		 
		 if(e.getSource()==tree&&selectedNode.getLevel()==2){
			 selectedNodeName = parentNode.toString();
			 if(parentName.contains("redis")){
				 redisPanel.setVisible(true);
				 mousePoint = new Point();
				 mousePoint.x = e.getX();
				 mousePoint.y = e.getY();
				 JPopupMenu popupMenu = new JPopupMenu();
				 loadItem = new JMenuItem("导入");
				 loadByTypeItem = new JMenuItem("本地导入");
				 loadByTypeItem.addActionListener(this);
				 loadItem.addActionListener(this);
				 popupMenu.add(loadItem);
				 popupMenu.add(loadByTypeItem);
				 TreePath path = tree.getPathForLocation(e.getX(), e.getY());
				 if(path==null){
					 return;
				 }
				 tree.setSelectionPath(path);
				 if(e.getButton()==3){	//右键点击触发
					 popupMenu.show(tree, e.getX(), e.getY());
				 }
			 }else{
				 redisPanel.setVisible(false);
				 popupMenu.removeAll();
				 newMysqlItem = new JMenuItem("新建");
				 newMysqlItem.addActionListener(this);
				 copyMysqlItem = new JMenuItem("从数据库导入");
				 copyMysqlItem.addActionListener(this);
				 popupMenu.add(newMysqlItem);
				 if(selectedNode.toString().contains("√")){
					 loadMysqlItem = new JMenuItem("本地导入");
					 loadMysqlItem.addActionListener(this);
					 popupMenu.add(loadMysqlItem);
				 }
				 popupMenu.add(copyMysqlItem);
				 TreePath path = tree.getPathForLocation(e.getX(), e.getY());
				 if(path==null){
					 return;
				 }
				 tree.setSelectionPath(path);
				 if(e.getButton()==3){	//右键点击触发
					 popupMenu.show(tree, e.getX(), e.getY());
				 }
			 }
			 return;
		 }
		 
		 
		 
		 
		 
		 //三级节点,只有redis操作才有三级节点
		 if(e.getSource()==tree&&selectedNode.getLevel()==3){
			 redisPanel.setVisible(true);
			 JPopupMenu popupMenu = new JPopupMenu();
			 delItem = new JMenuItem("删除");
			 delItem.addActionListener(this);
			 popupMenu.add(delItem);
			 TreePath path = tree.getPathForLocation(e.getX(), e.getY());
			 if(path==null){
				 return;
			 }
			 tree.setSelectionPath(path);
			 if(e.getButton()==3){	//右键点击触发
				 popupMenu.show(tree, e.getX(), e.getY());
			 }
			 return;
		 }
	}
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		
	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO 自动生成的方法存根
		
	}
	@Override
	public void keyPressed(KeyEvent e) {
		if (e.isControlDown() && e.VK_S == e.getKeyCode()){
			 DefaultMutableTreeNode selectedNode=(DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
			 DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode)selectedNode.getParent();
			 redisDataMap.get(parentNode.toString()).put(selectedNode.toString(), RedisPanel.textField.getText());
			 return;
		 }
		
	}
	@Override
	public void keyReleased(KeyEvent paramKeyEvent) {
		// TODO 自动生成的方法存根
		
	}
	@Override
	public void keyTyped(KeyEvent paramKeyEvent) {
		// TODO 自动生成的方法存根
		
	}
	@Override
	public void valueChanged(TreeSelectionEvent paramTreeSelectionEvent) {
		 DefaultMutableTreeNode selectedNode=(DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
		 Map<String,String> map = null;
		 if(selectedNode!=null&&selectedNode.getLevel()==3){
			 DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode)selectedNode.getParent();
			 map = redisDataMap.get(parentNode.toString());
			 System.out.println(map);
			 if(map!=null){
				 RedisPanel.textField.setText(map.get(selectedNode.toString()));
			 }
		 } 
		
	}
}
