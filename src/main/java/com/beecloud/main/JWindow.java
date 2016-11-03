package com.beecloud.main;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.beecloud.Util.DbUtil;
import com.beecloud.Util.FileUtil;
import com.beecloud.Util.JTreeUtil;
import com.beecloud.Util.JtableUtil;
import com.beecloud.Util.XmlHelper;
import com.beecloud.bean.config.DbInfo;
import com.beecloud.bean.config.RedisInfo;
import com.beecloud.listenser.JTreeListener;
import com.beecloud.widget.MergeDataDialog;
import com.beecloud.widget.RedisPanel;

/**
 * 
 * @description //TODO
 * @author hong.lin@beecloud.com
 * @date 2016年11月2日 上午10:25:10
 * @version v1.0
 */
public class JWindow extends JFrame implements ActionListener,MouseListener{
	private static final long serialVersionUID = 7618961966546866592L;
	private static JTree tree;
	public static JWindow context;
	private JMenuItem exportItem;//导出按钮
	
	private JMenuItem saveIconItem;//保存按钮
	
//	private JMenuItem redisIconItem;
	
	private JTabbedPane tabbedPane;
	private RedisPanel redisPanel;
	public static Map<String,String> dbcopymap = new HashMap<String,String>();
	/**
	 * key:数据库名称
	 * value:文件名称
	 */
	public static Map<String,String> fileMap = new HashMap<String,String>();
	
	/**
	 * key:数据库名称
	 * value:数据库配置对象DbInfo
	 */
	public static Map<String,DbInfo> dbMap = new HashMap<String,DbInfo>();
	
	/**
	 * key:tab分页名称
	 * value:数据库名称
	 */
	public static Map<String,String> tabMap = new HashMap<String,String>();
	/**
	 * 工具显示宽度
	 */
	public static int width ;
	
	/**
	 * 工具显示高度
	 */
	public static int height;
	
	public static int panelWidth;
	
	/**
	 * 数据展示区域大小
	 */
	public static int panelHeight;
	public static void main(String...args) throws ParserConfigurationException, SAXException, IOException{
		 context = new JWindow();
	}
	
	
	
	public JWindow() throws ParserConfigurationException, SAXException, IOException{
		initWindow();
		BuildMysqlWindow();
//		BuildRedisWindow();
	    setTitle("云蜂接口测试数据生成工具");//设置标题
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//点击窗口右上角的关闭按钮关闭窗口,退出程序
	    this.setSize(width, height);
	    setVisible(true);// 显示窗口
	    setLocationRelativeTo(null); //设置程序居中
	  }
	
	
	
	/**
	 * 获取TabbedPane中当前的JTable
	 */
	public JTable getCurrentTable(){
		int current_tab = tabbedPane.getSelectedIndex();
		JScrollPane js = (JScrollPane)tabbedPane.getComponentAt(current_tab);
		JTable table =(JTable)js.getViewport().getView();
		return table;
	}
	


	/**
	 * 更新Jtree数据
	 * @throws Exception 
	 */
	public static void updateWindow() throws Exception{
					tree.setModel(new DefaultTreeModel(
							new DefaultMutableTreeNode("数据库管理") {
								{
									DefaultMutableTreeNode node_1 = null;
									File file = new File("dbconfig");
									if(file.isDirectory()){
										File[] files = file.listFiles();
										for(int i =0;i<files.length;i++){
											if(files[i].getName().contains("redis")){
												RedisInfo redisInfo = XmlHelper.getRedisInfo(files[i].getAbsolutePath());
												node_1 = new DefaultMutableTreeNode(redisInfo.getName());
												JTreeUtil.addTreeNode(tree,node_1,"depend");
												JTreeUtil.addTreeNode(tree,node_1,"target");
												JTreeUtil.addTreeNode(tree,node_1,"check");
												JTreeListener.redisInfoMap.put(redisInfo.getName(), redisInfo);
											}else{
												DbInfo dbInfo = XmlHelper.getDbInfo(files[i].getAbsolutePath());
												node_1 = new DefaultMutableTreeNode(dbInfo.getDbName());
												dbMap.put(dbInfo.getDbName(),dbInfo);
												fileMap.put(dbInfo.getDbName(), files[i].getAbsolutePath());
												List<String> table_list = DbUtil.getTableList(dbInfo.getUserName(), dbInfo.getPassword(), dbInfo.getUrl());
												for(int j=0;j<table_list.size();j++){
													List<String>  hasSaved = FileUtil.getSavelist("data");
													if(hasSaved.contains(dbInfo.getDbName()+"_"+table_list.get(j)+".xml")){
														node_1.add(new DefaultMutableTreeNode(table_list.get(j)+" √"));
													}else{					
														node_1.add(new DefaultMutableTreeNode(table_list.get(j)));
													}
												}
											}
											add(node_1);
										}
									}
								}
							}
						));
		}
	
	
	
	/**
	 * 初始化窗体基本信息
	 * @throws IOException 
	 * @throws SAXException 
	 * @throws ParserConfigurationException 
	 */
	public void initWindow() throws ParserConfigurationException, SAXException, IOException{
		/**
		 * 设置工具显示高度和宽度
		 */
		File file = new File("data");
		if(!file.exists()){
			file.mkdir();
		}
		Insets screenInsets = Toolkit.getDefaultToolkit().getScreenInsets(this.getGraphicsConfiguration());
		//  获取底部任务栏高度
		int pc_bottom_height = screenInsets.bottom;
		Dimension scrSize=Toolkit.getDefaultToolkit().getScreenSize();
		width = scrSize.width;
		height = scrSize.height-pc_bottom_height;
		panelWidth = width-250;
		panelHeight = height;
		JMenuBar menuBar = new JMenuBar();
		saveIconItem = new JMenuItem();
		saveIconItem.setToolTipText("点击保存");
		ImageIcon saveIcon = new ImageIcon(getClass().getClassLoader().getResource("images/save.png"));
		saveIconItem.setIcon(saveIcon);
		saveIconItem.addActionListener(this);
		
		
//		redisIconItem = new JMenuItem();
//		redisIconItem.setToolTipText("Redis操作");
//		ImageIcon redisIcon = new ImageIcon(getClass().getClassLoader().getResource("images/redis.png"));
//		redisIconItem.setIcon(redisIcon);
//		redisIconItem.addActionListener(this);
		
		
		exportItem = new JMenuItem();
		ImageIcon exportIcon = new ImageIcon(getClass().getClassLoader().getResource("images/export.png"));
		exportItem.setToolTipText("点击导出");
		exportItem.setIcon(exportIcon);
		exportItem.addActionListener(this);
		JPanel jpanel = new JPanel();
		jpanel.add(exportItem);
		jpanel.add(saveIconItem);
//		jpanel.add(redisIconItem);
		jpanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		menuBar.add(jpanel);
		setJMenuBar(menuBar);
	}
	
	/**
	 * MySql数据库使用窗体
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public void BuildMysqlWindow() throws ParserConfigurationException, SAXException, IOException
	  {
		/**
		 * 初始化JTabbedPane
		 */
		getContentPane().setLayout(null);
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(250, -2, 4*width/5, height-90);
		//tabbedPane.addMouseListener(this);
		redisPanel = new RedisPanel();
		redisPanel.setBounds(255, -2, JWindow.width-260, JWindow.height-90);
		redisPanel.setVisible(false);
		getContentPane().add(redisPanel);
		getContentPane().add(tabbedPane);
		/**
		 * 初始化数据库管理树形结构
		 * 编辑文件夹下的配置文件获取数据表
		 */
		tree = new JTree();
		new JTreeListener(tabbedPane,redisPanel,tree);
		JScrollPane jsp = new JScrollPane(tree);
		try {
			updateWindow();
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		} //加载数据
		//tree.addTreeSelectionListener(this);
		jsp.setBounds(0, -2, 250,height-90);
		getContentPane().add(jsp);
	}
	
	
	
//	/**
//	 * 初始化Redis数据窗口
//	 */
//	public  void BuildRedisWindow(){
////		JScrollPane jsp = new JScrollPane(textArea_redis_content);
////		jsp.setBounds(255, -2, 4*JWindow.width/5, JWindow.height-90);
////		getContentPane().add(jsp);
//		RedisFrame redisFrame = new RedisFrame(this);
//		redisFrame.setBounds(250, -2, panelWidth, panelHeight);
//		getContentPane().add(redisFrame);
//	}
	
	@Override
	public void actionPerformed(ActionEvent e)  {
		// TODO 自动生成的方法存根
		/**
		 * 合并数据
		 */
		if(e.getSource()==exportItem){
			MergeDataDialog dialog;
			try {
				dialog = new MergeDataDialog();
				dialog.show();
			} catch (Exception e1) {
				// TODO 自动生成的 catch 块
				e1.printStackTrace();
			}
			
			return;    
		}
		
		
	
		
		
		
		
		/**
		 * MenuBar保存按钮响应事件
		 */
		if(e.getSource()==saveIconItem){
			String selectdbName = JTreeListener.selectedNodeName;
			DefaultMutableTreeNode rootNode= (DefaultMutableTreeNode)tree.getModel().getRoot();//根节点
			if(selectdbName.contains("redis")){
				try {
					XmlHelper.saveRedisData("data",selectdbName,JTreeListener.redisConnectMap.get("index"), JTreeListener.redisDataMap);
					DefaultMutableTreeNode selected = JTreeUtil.getChildTreeNodeByName(tree, rootNode, selectdbName);
					if(selected!=null){
						DefaultMutableTreeNode check = JTreeUtil.getChildTreeNodeByName(tree, selected, "check");
						DefaultMutableTreeNode depend = JTreeUtil.getChildTreeNodeByName(tree, selected, "depend");
						DefaultMutableTreeNode target = JTreeUtil.getChildTreeNodeByName(tree, selected, "target");
						check.removeAllChildren();
						depend.removeAllChildren();
						target.removeAllChildren();
						tree.updateUI();
						RedisPanel.textField.setVisible(false);
					}
				} catch (ParserConfigurationException | IOException e1) {
					// TODO 自动生成的 catch 块
					e1.printStackTrace();
				}
			}else{
				boolean isSaveSucess = false;
				try {
					isSaveSucess = JtableUtil.saveData(tabbedPane,tabMap);
				} catch (ParserConfigurationException | IOException
						| SAXException e2) {
					// TODO 自动生成的 catch 块
					e2.printStackTrace();
				}
				if(isSaveSucess){			//保存成功才更新UI
					int current_tab = tabbedPane.getSelectedIndex();
					String tabName = tabbedPane.getTitleAt(current_tab);
					String dbName = tabMap.get(tabName);
					tabbedPane.remove(current_tab);
					try {
						this.updateWindow();
					} catch (Exception e1) {
						// TODO 自动生成的 catch 块
						e1.printStackTrace();
					}
					TreeNode root = (TreeNode)tree.getModel().getRoot();//根节点
					for(int i=0;i<root.getChildCount();i++){		//自动展开保存的表结构父节点
						if((dbName).equals(root.getChildAt(i).toString())){
							TreeNode treeNode = root.getChildAt(i);
							TreePath currentPath = new TreePath(((DefaultTreeModel) tree.getModel()).getPathToRoot(treeNode)); 
							tree.expandPath(currentPath);
						}
					}
				}
			}
			return;
		}
		
	}



	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO 自动生成的方法存根
		
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