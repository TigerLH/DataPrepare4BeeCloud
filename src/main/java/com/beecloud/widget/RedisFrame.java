package com.beecloud.widget;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang3.StringUtils;

import com.beecloud.Util.JsonUtil;
import com.beecloud.Util.RedisUtil;
import com.beecloud.Util.XmlHelper;
import com.beecloud.bean.config.RedisInfo;


/** 
 * @author  linhong: 
 * @date 2016年5月4日 下午2:50:31 
 * @Description: TODO
 * @version 1.0  
 */
public class RedisFrame extends JFrame implements TreeSelectionListener,KeyListener,MouseListener,ActionListener{
	private JTextArea textArea_redis_content;
	private JTree tree;
	private JMenuItem addRedis;//新增Redis配置
	
	private JMenuItem clearItem;//清空key
	private JMenuItem delItem;//删除Key
	private JMenuItem delDbItem;//Redis名称删除
	private JMenuItem loadItem;//导入Key
	private JMenuItem saveIconItem; //保存按钮
	private Point mousePoint; //鼠标所在位置
	
	private static String selectedRedisName ="";

	/**
	 * 存储连接redis所需要的ip/port/auth
	 */
	public static Map<String,String> redisConnectMap = new HashMap<String,String>();
	
	/**
	 * redis中的Map<redis名称,Map<数据类型,<key,value>>>
	 */
	//public static Map<String,Map<String,Map<String,String>>> redisMap = new HashMap<String,Map<String,Map<String,String>>>();
	
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
	private JScrollPane jsp;
	private JScrollPane scroll;
	public void initWindow(){
		Insets screenInsets = Toolkit.getDefaultToolkit().getScreenInsets(this.getGraphicsConfiguration());
		 // 获取底部任务栏高度
		int pc_bottom_height = screenInsets.bottom;
		Dimension scrSize=Toolkit.getDefaultToolkit().getScreenSize();
		width = scrSize.width;
		height = scrSize.height-pc_bottom_height;
		textArea_redis_content = new JTextArea();
		textArea_redis_content.setLineWrap(true);
		//textArea_redis_content.setColumns(10);
		textArea_redis_content.addKeyListener(this);
		scroll = new JScrollPane(textArea_redis_content);
		scroll.setBounds(250, -2, width-250,height-90);
//		scroll.add(textArea_redis_content);
//		scroll.setHorizontalScrollBarPolicy( 
//		JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED); 
//		scroll.setVerticalScrollBarPolicy( 
//		JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED); 
		getContentPane().add(scroll);
		
		JMenuBar menuBar = new JMenuBar();
		saveIconItem = new JMenuItem();
		saveIconItem.setToolTipText("点击保存");
		ImageIcon saveIcon = new ImageIcon(getClass().getClassLoader().getResource("images/save.png"));
		saveIconItem.setIcon(saveIcon);
		saveIconItem.addActionListener(this);
		JPanel jpanel = new JPanel();
		jpanel.add(saveIconItem);
		jpanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		menuBar.add(jpanel);
		setJMenuBar(menuBar);
		
		tree = new JTree();
		tree.setInvokesStopCellEditing(true);
		tree.setModel(new DefaultTreeModel(new DefaultMutableTreeNode("数据库管理"){}));
		jsp = new JScrollPane(tree);
		jsp.setBounds(0, -2, 250,height-90);
		getContentPane().add(jsp);
		tree.addMouseListener(this);
		tree.addKeyListener(this);
		tree.addTreeSelectionListener(this);
	}

    
	public RedisFrame() {
		initWindow();
	    setTitle("Redis操作界面");//设置标题
	    //setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//点击窗口右上角的关闭按钮关闭窗口,退出程序
	    setVisible(true);// 显示窗口
	    this.setSize(width*4/5,height-90);	 

	    setLocationRelativeTo(null); //设置程序居中
	    setAlwaysOnTop(true);
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addComponent(jsp, GroupLayout.PREFERRED_SIZE, 250, GroupLayout.PREFERRED_SIZE)
					.addGap(6)
					.addComponent(scroll, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(4)
					.addComponent(jsp, GroupLayout.PREFERRED_SIZE, 540, GroupLayout.PREFERRED_SIZE))
				.addComponent(scroll, GroupLayout.PREFERRED_SIZE, 544, GroupLayout.PREFERRED_SIZE)
		);
		getContentPane().setLayout(groupLayout);
	}
	
	
	/**
	 * JTree动态添加子节点
	 * @param itemName
	 */
	public void addTreeNode(DefaultMutableTreeNode parent,String itemName){
		DefaultTreeModel treeModel = (DefaultTreeModel)tree.getModel();
		DefaultMutableTreeNode child = new DefaultMutableTreeNode(itemName);
		treeModel.insertNodeInto(child, parent, 0);
		TreeNode root = (TreeNode)tree.getModel().getRoot();//根节点
		for(int i=0;i<root.getChildCount();i++){		//自动展开保存的表结构父节点
				TreeNode treeNode = root.getChildAt(i);
				TreePath currentPath = new TreePath(((DefaultTreeModel) tree.getModel()).getPathToRoot(treeNode)); 
				tree.expandPath(currentPath);
		}
	}
	
	
	
	

	@Override
	public void valueChanged(TreeSelectionEvent paramTreeSelectionEvent) {
		 DefaultMutableTreeNode selectedNode=(DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
		 Map<String,String> map = null;
		 if(selectedNode.getLevel()==3){
			 DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode)selectedNode.getParent();
			 map = redisDataMap.get(parentNode.toString());
			 System.out.println(map);
			 if(map!=null){
				 textArea_redis_content.setText(map.get(selectedNode.toString()));
			 }
		 } 
	}




	@Override
	public void keyPressed(KeyEvent e) {
		 if (e.isControlDown() && e.VK_S == e.getKeyCode()){
			 DefaultMutableTreeNode selectedNode=(DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
			 DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode)selectedNode.getParent();
			 redisDataMap.get(parentNode.toString()).put(selectedNode.toString(), textArea_redis_content.getText());
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
        	 System.out.println("chufa");
        	 tree.stopEditing();
        	 tree.setEditable(false);
         }	
	}



	@Override
	public void mouseEntered(MouseEvent paramMouseEvent) {
		// TODO 自动生成的方法存根
		
	}



	@Override
	public void mouseExited(MouseEvent paramMouseEvent) {
		// TODO 自动生成的方法存根
		
	}



	@Override
	public void mousePressed(MouseEvent e) {
		 DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();//获取选中的节点
		 if(e.getSource()==tree&&selectedNode.getLevel()==0){ 	//根节点
			 JPopupMenu popupMenu = new JPopupMenu();
			 addRedis = new JMenuItem("添加");
			 addRedis.addActionListener(this);
			 popupMenu.add(addRedis);
			 TreePath path = tree.getPathForLocation(e.getX(), e.getY());
			 if(path==null){
				 return;
			 }
			 tree.setSelectionPath(path);
			 if(e.getButton()==3){	//右键点击触发
				 popupMenu.show(tree, e.getX(), e.getY());
			 }
		 }
		 
		 
		 
		 
		 
		 /**
		  * 一级节点
		  */
		 if(e.getSource()==tree&&selectedNode.getLevel()==1){
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
		 }
		 
		 
		 
		 /**
		  * 二级节点
		  */
		 if(e.getSource()==tree&&selectedNode.getLevel()==2){
			 mousePoint = new Point();
			 mousePoint.x = e.getX();
			 mousePoint.y = e.getY();
			 JPopupMenu popupMenu = new JPopupMenu();
			 loadItem = new JMenuItem("导入");
			 loadItem.addActionListener(this);
			 popupMenu.add(loadItem);
			 TreePath path = tree.getPathForLocation(e.getX(), e.getY());
			 if(path==null){
				 return;
			 }
			 tree.setSelectionPath(path);
			 if(e.getButton()==3){	//右键点击触发
				 popupMenu.show(tree, e.getX(), e.getY());
			 }
		 }
		 
		 
		 
		 
		 //三级节点
		 if(e.getSource()==tree&&selectedNode.getLevel()==3){ 	
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
		 }
		 
		 
		 
		
	}



	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO 自动生成的方法存根
		
	}



	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==addRedis){
			RedisConfigureDialog addDialog = new RedisConfigureDialog(RedisFrame.this, true);
			addDialog.show();
			String host = redisConnectMap.get("host");
			int port = Integer.parseInt(redisConnectMap.get("port"));
			String auth = redisConnectMap.get("auth");
			int index = Integer.parseInt(redisConnectMap.get("index"));
			String name = "redis_"+StringUtils.substring(UUID.randomUUID().toString(), 24);
			DefaultMutableTreeNode parent = (DefaultMutableTreeNode)tree.getModel().getRoot();//根节点
			addTreeNode(parent,name);
			RedisInfo redisInfo = new RedisInfo();
			redisInfo.setHost(host);
			redisInfo.setPort(port);
			redisInfo.setAuth(auth);
			redisInfo.setIndex(index);
			redisInfoMap.put(name, redisInfo);
			DefaultMutableTreeNode child = (DefaultMutableTreeNode) parent.getChildAt(0);
			addTreeNode(child,"depend");
			addTreeNode(child,"target");
			addTreeNode(child,"check");
		}
		
		/**
		 * 导入Redis数据
		 */
		if(e.getSource()==loadItem){
			RedisLoadKeyByRegexDialog loadDialog = new RedisLoadKeyByRegexDialog(RedisFrame.this,true);
			loadDialog.setLocation(mousePoint);
			loadDialog.show();
			DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();//获取选中的节点
			DefaultMutableTreeNode parent = (DefaultMutableTreeNode) selectedNode.getParent();
			selectedRedisName = parent.toString();
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
				if(!map.containsKey(key)){
					addTreeNode(selectedNode,key);
				}
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
		 * db name一级节点删除
		 */
		if(e.getSource()==delDbItem){
			DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
			DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();//获取选中的节点
			DefaultMutableTreeNode parent = (DefaultMutableTreeNode) selectedNode.getParent();
			if(selectedNode!=null&&parent!=null){
				model.removeNodeFromParent(selectedNode);
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
		
		
		/**
		 * 清空子节点
		 */
		if(e.getSource()==clearItem){
			DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();//获取选中的节点
			selectedNode.removeAllChildren();
			tree.updateUI();
		}
		
		
		/**
		 * 保存按钮
		 */
		if(e.getSource()==saveIconItem){
			if(redisDataMap.get("check")==null){
				System.out.println("空数据");
				return;
			}
			try {
				XmlHelper.saveRedisData("data",selectedRedisName,redisConnectMap.get("index"), redisDataMap);
			} catch (ParserConfigurationException | IOException e1) {
				// TODO 自动生成的 catch 块
				e1.printStackTrace();
			}
			this.dispose();
		}
		
		
	}
}
