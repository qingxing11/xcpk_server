package com.wt.main;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import com.wt.client.ClientHelper;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

public class MjTestFrame
{
	private JFrame frame;
	private JTextField textField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args)
	{
		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				try
				{
					MjTestFrame window = new MjTestFrame();
					window.frame.setVisible(true);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MjTestFrame()
	{
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize()
	{
		frame = new JFrame();
		frame.setResizable(false);
		frame.setBounds(100, 100, 565, 438);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JButton btnNewButton = new JButton("连接服务器");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				button_connect();
			}
		});
		btnNewButton.setFont(new Font("微软雅黑", Font.PLAIN, 13));
		btnNewButton.setBounds(246, 9, 104, 23);
		frame.getContentPane().add(btnNewButton);
		
		textField = new JTextField();
		textField.setFont(new Font("微软雅黑", Font.PLAIN, 13));
		textField.setBounds(57, 11, 104, 21);
		frame.getContentPane().add(textField);
		textField.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("线程数");
		lblNewLabel.setFont(new Font("微软雅黑", Font.PLAIN, 13));
		lblNewLabel.setBounds(10, 14, 54, 15);
		frame.getContentPane().add(lblNewLabel);
		
		JButton btnNewButton_1 = new JButton("测试功能1");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				button_test1();
			}
		});
		btnNewButton_1.setFont(new Font("微软雅黑", Font.PLAIN, 13));
		btnNewButton_1.setBounds(10, 332, 93, 23);
		frame.getContentPane().add(btnNewButton_1);
		
		comboBox_ip_port = new JComboBox();
		comboBox_ip_port.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				comboBox_selectIp();
			}
		});
		comboBox_ip_port.setFont(new Font("微软雅黑", Font.PLAIN, 13));
		comboBox_ip_port.setModel(new DefaultComboBoxModel(new String[] {"127.0.0.1:1985","172.16.6.111:1985","172.16.6.255:1985","121.43.189.186:1985"}));
		comboBox_ip_port.setBounds(379, 10, 160, 21);
		frame.getContentPane().add(comboBox_ip_port);
		
		JButton btnNewButton_2 = new JButton("创建");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				button_createThread();
			}
		});
		btnNewButton_2.setFont(new Font("微软雅黑", Font.PLAIN, 13));
		btnNewButton_2.setBounds(163, 9, 80, 23);
		frame.getContentPane().add(btnNewButton_2);
		
		JButton btnNewButton_3 = new JButton("注册");
		btnNewButton_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				button_register();
			}
		});
		btnNewButton_3.setFont(new Font("微软雅黑", Font.PLAIN, 13));
		btnNewButton_3.setBounds(112, 368, 93, 23);
		frame.getContentPane().add(btnNewButton_3);
		
		textField_registerName = new JTextField();
		textField_registerName.setFont(new Font("微软雅黑", Font.PLAIN, 13));
		textField_registerName.setBounds(10, 368, 93, 21);
		frame.getContentPane().add(textField_registerName);
		textField_registerName.setColumns(10);
		
		testField_chat = new JTextField();
		testField_chat.setBounds(14, 91, 147, 24);
		frame.getContentPane().add(testField_chat);
		testField_chat.setColumns(10);
		
		JButton btnNewButton_4 = new JButton("聊天");
		btnNewButton_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				button_testchat();
			}
		});
		btnNewButton_4.setFont(new Font("微软雅黑", Font.PLAIN, 15));
		btnNewButton_4.setBounds(175, 90, 113, 27);
		frame.getContentPane().add(btnNewButton_4);
		
		textField_loginName = new JTextField();
		textField_loginName.setBounds(14, 45, 147, 24);
		frame.getContentPane().add(textField_loginName);
		textField_loginName.setColumns(10);
		
		JButton btnNewButton_5 = new JButton(" 登录");
		btnNewButton_5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				button_login();
			}
		});
		btnNewButton_5.setFont(new Font("微软雅黑", Font.PLAIN, 15));
		btnNewButton_5.setBounds(173, 45, 113, 27);
		frame.getContentPane().add(btnNewButton_5);
		
		textField_wxCode = new JTextField();
		textField_wxCode.setFont(new Font("微软雅黑", Font.PLAIN, 13));
		textField_wxCode.setBounds(14, 135, 147, 21);
		frame.getContentPane().add(textField_wxCode);
		textField_wxCode.setColumns(10);
		
		JButton btnNewButton_6 = new JButton("微信登录");
		btnNewButton_6.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				button_wxLogin();
			}
		});
		btnNewButton_6.setFont(new Font("微软雅黑", Font.PLAIN, 13));
		btnNewButton_6.setBounds(175, 135, 93, 23);
		frame.getContentPane().add(btnNewButton_6);
		
		JButton btnNewButton_7 = new JButton("重启");
		btnNewButton_7.setFont(new Font("微软雅黑", Font.PLAIN, 13));
		btnNewButton_7.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MjFrame.reStart();
			}
		});
		btnNewButton_7.setBounds(113, 332, 93, 23);
		frame.getContentPane().add(btnNewButton_7);
		
		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		
		JMenu mnNewMenu = new JMenu("文件");
		mnNewMenu.setFont(new Font("微软雅黑", Font.PLAIN, 13));
		menuBar.add(mnNewMenu);
		
		JMenuItem mntmNewMenuItem = new JMenuItem("退出");
		mntmNewMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				button_exit();
			}
		});
		mntmNewMenuItem.setFont(new Font("微软雅黑", Font.PLAIN, 13));
		mnNewMenu.add(mntmNewMenuItem);
	}

	protected void button_exit()
	{
		for (MjFrame mjFrame : nbbFrames)
		{
			mjFrame.close();
		}
	}

	protected void button_wxLogin()
	{
		String str = textField_wxCode.getText();
		for (int i = 0 ; i < nbbFrames.length ; i++)
		{
			nbbFrames[i].testWxLogin(str);
		}
	}

	protected void button_register() {
		String str = textField_registerName.getText();
		for (int i = 0 ; i < nbbFrames.length ; i++)
		{
			nbbFrames[i].testRegister(str);
		}	
	}

	protected void button_login() {
		String str = textField_loginName.getText();
		
		for (int i = 0 ; i < nbbFrames.length ; i++)
		{
			nbbFrames[i].testLogin(str);
		}
	}

	protected void button_testchat() {
		String str = testField_chat.getText();
		for (int i = 0 ; i < nbbFrames.length ; i++)
		{
			nbbFrames[i].testChat(str);
		}
	}

	protected void button_test1()
	{
		for (int i = 0 ; i < nbbFrames.length ; i++)
		{
			nbbFrames[i].test1();
		}
	}
	private JComboBox comboBox_ip_port;

	protected void comboBox_selectIp()
	{
		String[] ip_port = ((String) comboBox_ip_port.getSelectedItem()).split(":");
		ClientHelper.SERVER_HOST = ip_port[0];
		ClientHelper.SERVER_PORT = Integer.parseInt(ip_port[1]);
	
		System.out.println("ip:"+ClientHelper.SERVER_HOST+",port:"+ClientHelper.SERVER_PORT);
	}

	private MjFrame[] nbbFrames;
	private JTextField textField_registerName;
	private JTextField testField_chat;
	private JTextField textField_loginName;
	private JTextField textField_wxCode;
	protected void button_connect()
	{
		for (int i = 0 ; i < nbbFrames.length ; i++)
		{
			nbbFrames[i].connect();
		}
	}
	
	protected void button_createThread()
	{
		int threadNum = Integer.parseInt(textField.getText());
		nbbFrames = new MjFrame[threadNum];
		for (int i = 0 ; i < threadNum ; i++)
		{
			System.out.println("创建线程:"+i);
			nbbFrames[i] = new MjFrame();
			nbbFrames[i].start();
		}
	}
	
	protected void createThread()
	{
		int threadNum = 1;
		nbbFrames = new MjFrame[threadNum];
		for (int i = 0 ; i < threadNum ; i++)
		{
			System.out.println("创建线程:"+i);
			nbbFrames[i] = new MjFrame();
			nbbFrames[i].start();
		}
	}
}
