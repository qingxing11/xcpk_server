package client.wt.main;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTextField;

import client.wt.client.ClientHelper;

public class TestMain
{
	private JFrame frame;
	private JTextField textField;
	public static JLabel label_connect;
	public static JLabel label_connectAll;
	public static int connectNum;
	public static JButton button_loopLoginPlay;

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
					TestMain window = new TestMain();
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
	public TestMain()
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
		btnNewButton.setBounds(246, 48, 104, 23);
		frame.getContentPane().add(btnNewButton);
		
		textField = new JTextField();
		textField.setFont(new Font("微软雅黑", Font.PLAIN, 13));
		textField.setBounds(57, 50, 104, 21);
		frame.getContentPane().add(textField);
		textField.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("线程数");
		lblNewLabel.setFont(new Font("微软雅黑", Font.PLAIN, 13));
		lblNewLabel.setBounds(10, 53, 54, 15);
		frame.getContentPane().add(lblNewLabel);
		
		JButton btnNewButton_1 = new JButton("测试功能1");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				button_test1();
			}
		});
		btnNewButton_1.setFont(new Font("微软雅黑", Font.PLAIN, 13));
		btnNewButton_1.setBounds(10, 319, 134, 23);
		frame.getContentPane().add(btnNewButton_1);
		
		comboBox_ip_port = new JComboBox();
		comboBox_ip_port.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				comboBox_selectIp();
			}
		});
		comboBox_ip_port.setFont(new Font("微软雅黑", Font.PLAIN, 13));
		comboBox_ip_port.setModel(new DefaultComboBoxModel(new String[] {"172.16.0.255:1985"}));
		comboBox_ip_port.setBounds(379, 49, 160, 21);
		frame.getContentPane().add(comboBox_ip_port);
		
		JButton btnNewButton_2 = new JButton("创建");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				button_createThread();
			}
		});
		btnNewButton_2.setFont(new Font("微软雅黑", Font.PLAIN, 13));
		btnNewButton_2.setBounds(163, 48, 80, 23);
		frame.getContentPane().add(btnNewButton_2);
		
		JButton btnNewButton_3 = new JButton("注册");
		btnNewButton_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				button_register();
			}
		});
		btnNewButton_3.setFont(new Font("微软雅黑", Font.PLAIN, 13));
		btnNewButton_3.setBounds(112, 355, 93, 23);
		frame.getContentPane().add(btnNewButton_3);
		
		textField_registerName = new JTextField();
		textField_registerName.setFont(new Font("微软雅黑", Font.PLAIN, 13));
		textField_registerName.setBounds(10, 355, 93, 21);
		frame.getContentPane().add(textField_registerName);
		textField_registerName.setColumns(10);
		
		testField_chat = new JTextField();
		testField_chat.setBounds(10, 176, 147, 24);
		frame.getContentPane().add(testField_chat);
		testField_chat.setColumns(10);
		
		JButton btnNewButton_4 = new JButton("聊天");
		btnNewButton_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				button_testchat();
			}
		});
		btnNewButton_4.setFont(new Font("微软雅黑", Font.PLAIN, 15));
		btnNewButton_4.setBounds(171, 175, 113, 27);
		frame.getContentPane().add(btnNewButton_4);
		
		JButton btnNewButton_5 = new JButton(" 登录");
		btnNewButton_5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				button_login();
			}
		});
		btnNewButton_5.setFont(new Font("微软雅黑", Font.PLAIN, 15));
		btnNewButton_5.setBounds(8, 127, 113, 27);
		frame.getContentPane().add(btnNewButton_5);
		
		textField_wxCode = new JTextField();
		textField_wxCode.setFont(new Font("微软雅黑", Font.PLAIN, 13));
		textField_wxCode.setBounds(10, 220, 147, 21);
		frame.getContentPane().add(textField_wxCode);
		textField_wxCode.setColumns(10);
		
		JButton btnNewButton_6 = new JButton("微信登录");
		btnNewButton_6.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				button_wxLogin();
			}
		});
		btnNewButton_6.setFont(new Font("微软雅黑", Font.PLAIN, 13));
		btnNewButton_6.setBounds(171, 220, 93, 23);
		frame.getContentPane().add(btnNewButton_6);
		
		JButton btnNewButton_7 = new JButton("重启");
		btnNewButton_7.setFont(new Font("微软雅黑", Font.PLAIN, 13));
		btnNewButton_7.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TestFrame.reStart();
			}
		});
		btnNewButton_7.setBounds(191, 319, 93, 23);
		frame.getContentPane().add(btnNewButton_7);
		
		label_connect = new JLabel("0");
		label_connect.setFont(new Font("宋体", Font.PLAIN, 13));
		label_connect.setBounds(226, 5, 47, 15);
		frame.getContentPane().add(label_connect);
		
		label_connectAll = new JLabel("0");
		label_connectAll.setFont(new Font("宋体", Font.PLAIN, 13));
		label_connectAll.setBounds(325, 5, 54, 15);
		frame.getContentPane().add(label_connectAll);
		
		JLabel label = new JLabel("/");
		label.setBounds(281, 5, 14, 15);
		frame.getContentPane().add(label);
		
		JButton btnNewButton_8 = new JButton("持续登陆");
		btnNewButton_8.setFont(new Font("微软雅黑", Font.PLAIN, 13));
		btnNewButton_8.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				button_loopLogin();
			}
		});
		btnNewButton_8.setBounds(10, 93, 121, 24);
		frame.getContentPane().add(btnNewButton_8);
		
		button_loopLoginPlay = new JButton("暂停");
		button_loopLoginPlay.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				button_loopLoginPause();
			}
		});
		button_loopLoginPlay.setFont(new Font("微软雅黑", Font.PLAIN, 13));
		button_loopLoginPlay.setBounds(141, 94, 93, 23);
		frame.getContentPane().add(button_loopLoginPlay);
		
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

	protected void button_loopLoginPause()
	{
		if(isLoopLogin)
		{
			button_loopLoginPlay.setText("继续");
		}
		else
		{
			button_loopLoginPlay.setText("暂停");
		}
		
		isLoopLogin = !isLoopLogin;
	}

	private boolean isLoopLogin = true;
	protected void button_loopLogin()
	{
		setAllNum(0);
		
		new Thread(new Runnable()
		{
			public void run()
			{
				while (true)
				{
					if(isLoopLogin)
					{
						System.out.println("button_loopLogin....");
						TestFrame testFrame = new TestFrame();
						testFrame.loopLogin();
						testFrame.start();
						try
						{
							Thread.sleep(20);
						}
						catch (InterruptedException e)
						{
							e.printStackTrace();
						}
					}
				}
			}
		}).start();
	}

	protected void button_exit()
	{
		for (TestFrame mjFrame : nbbFrames)
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
		for (int i = 0 ; i < nbbFrames.length ; i++)
		{
			nbbFrames[i].testLogin();
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

	private TestFrame[] nbbFrames;
	private JTextField textField_registerName;
	private JTextField testField_chat;
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
		nbbFrames = new TestFrame[threadNum];
		for (int i = 0 ; i < threadNum ; i++)
		{
			System.out.println("创建线程:"+i);
			nbbFrames[i] = new TestFrame();
			nbbFrames[i].start();
		}
		
		setAllNum(nbbFrames.length);
	}
	
	protected void createThread()
	{
		int threadNum = 1;
		nbbFrames = new TestFrame[threadNum];
		for (int i = 0 ; i < threadNum ; i++)
		{
			System.out.println("创建线程:"+i);
			nbbFrames[i] = new TestFrame();
			nbbFrames[i].start();
		}
	}

	public static void addConnectNum()
	{
		synchronized (label_connect)
		{
			TestMain.connectNum++;
			TestMain.label_connect.setText(connectNum+"");
	 		TestMain.label_connect.repaint();
		}
	}
	
	public void setAllNum(int num)
	{
		label_connectAll.setText(num+"");
	}
	
	public void clearNum()
	{
		TestMain.connectNum = 0;
		setNum(0);
	}
	
	public void setNum(int num)
	{
		TestMain.label_connect.setText(num+"");
 		TestMain.label_connect.repaint();
	}
}
