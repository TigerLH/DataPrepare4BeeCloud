package com.beecloud.Util;

import java.util.Enumeration;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

public class JTreeUtil {
	public	static void expandAll(JTree tree, TreePath parent, boolean expand) {
        TreeNode node = (TreeNode) parent.getLastPathComponent();
        if (node.getChildCount() > 0) {
            for (Enumeration e = node.children(); e.hasMoreElements();) {
                TreeNode n = (TreeNode) e.nextElement();
                TreePath path = parent.pathByAddingChild(n);
                expandAll(tree, path, expand);
            }
        }
        if (expand) {
            tree.expandPath(parent);
        } else {
            tree.collapsePath(parent);
        }
    }
	
	
	/**
	 * JTree动态添加子节点
	 * @param itemName
	 */
	public static void addTreeNode(JTree tree,DefaultMutableTreeNode parent,String itemName){
		DefaultTreeModel treeModel = (DefaultTreeModel)tree.getModel();
		DefaultMutableTreeNode add = new DefaultMutableTreeNode(itemName);
		treeModel.insertNodeInto(add, parent, 0);
	}
	
	
	/**
	 * 根据子节点名称获取子节点
	 * @param tree
	 * @param parent
	 * @param itemName
	 * @return
	 */
	public static DefaultMutableTreeNode getChildTreeNodeByName(JTree tree,DefaultMutableTreeNode parent,String itemName){
		DefaultMutableTreeNode treeNode = null;
		for(int i=0;i<parent.getChildCount();i++){
			DefaultMutableTreeNode child = (DefaultMutableTreeNode) parent.getChildAt(i);
			if(child.toString().equals(itemName)){
				treeNode = child;
			}
		}
		return treeNode;
	}
}


