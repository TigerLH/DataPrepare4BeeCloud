package com.beecloud.widget;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;

import com.beecloud.Util.FileUtil;
import com.beecloud.Util.XmlHelper;

public class MergeDataDialog extends JDialog implements MouseListener{
	private JTextField  jTextField;	//路径展示框
	private JButton jButton;	//路径选择按钮
	public MergeDataDialog() throws Exception {
		List<String> list = FileUtil.getSavelist("data");
		this.setVisible(true);
		this.setSize(400,400);	  //设置大小
		this.setLocationRelativeTo(null);
		this.setTitle("合并文件");
		JPanel panel = new JPanel();
		jTextField= new JTextField("");
		jTextField.setEditable(false);
		JButton ok = new JButton("确定");
		getContentPane().add(panel,BorderLayout.SOUTH);
		
		jButton = new JButton("\u9009\u62E9\u5B58\u50A8\u4F4D\u7F6E:");
		jButton.addMouseListener(this);
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel.createSequentialGroup()
					.addComponent(jButton)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(jTextField, GroupLayout.DEFAULT_SIZE, 199, Short.MAX_VALUE)
					.addContainerGap())
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap(150, Short.MAX_VALUE)
					.addComponent(ok, GroupLayout.PREFERRED_SIZE, 98, GroupLayout.PREFERRED_SIZE)
					.addGap(136))
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(jButton)
						.addComponent(jTextField, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addComponent(ok))
		);
		panel.setLayout(gl_panel);
		
		
		final JPanel panel_1 = new JPanel();
		BoxLayout layout=new BoxLayout(panel_1, BoxLayout.Y_AXIS); 
		panel_1.setLayout(layout);
		JScrollPane SP = new JScrollPane(panel_1);
		for(int i=0;i<list.size();i++){
			System.out.println("file:"+list.get(i).toString());
			JCheckBox checkBox = new JCheckBox(list.get(i).toString());
			checkBox.setSelected(true);
			panel_1.add(checkBox);
		}
		getContentPane().add(SP,FlowLayout.LEFT);
		ok.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO 自动生成的方法存根
				List<String> selected = new ArrayList<String>();
				int count = panel_1.getComponentCount();
				for(int i=0;i<count;i++){
					Object obj = panel_1.getComponent(i);
					if(obj instanceof JCheckBox){
						JCheckBox jCheckBox = (JCheckBox)obj;
						if(jCheckBox.isSelected()){
							selected.add(jCheckBox.getText());//如果被选中则添加到需要merge的列表中
						}
					}
				} 
				String fileName = jTextField.getText();
				if("".equals(fileName)){
					JOptionPane.showMessageDialog(null, "请先选择保存到的文件", "提示", JOptionPane.CLOSED_OPTION);
					return;
				}
				try {
					XmlHelper.merge(fileName,"data",selected);
				} catch (Exception e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
				dispose();
			}
			
		});
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO 自动生成的方法存根
		if(arg0.getSource()==jButton){
			JFileChooser chooser=new JFileChooser();  
		    int retval = chooser.showSaveDialog(this);
		    if(retval == JFileChooser.APPROVE_OPTION) {
		    	File file = chooser.getSelectedFile();
	           try {
	        	   jTextField.setText(file.getAbsolutePath());
			} catch (Exception e1) {
				// TODO 自动生成的 catch 块
				e1.printStackTrace();
			}
	        }
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
	public void mousePressed(MouseEvent arg0) {
		// TODO 自动生成的方法存根
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO 自动生成的方法存根
		
	}
}
