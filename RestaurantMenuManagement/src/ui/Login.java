package ui;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.JButton;
import java.awt.Color;

public class Login extends BaseJFrame {

	private JPanel contentPane;
	private int width = 350;
	private int height = 260;
	private JTextField txt_userName;
	private JPasswordField txt_pwd;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper.launchBeautyEyeLNF();
					UIManager.put("RootPane.setupButtonVisible", false);
					
					Login frame = new Login();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Login() {
		InitFram();
	}
	
	private void InitFram(){
		setTitle("RestaurantMenu Management");
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int screenWidth = screenSize.width;
		int screenHeight = screenSize.height;
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds((screenWidth - width) / 2, (screenHeight - height) / 2, width, height);
		contentPane = new JPanel();
		contentPane.setForeground(Color.BLACK);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("User Name:");
		lblNewLabel.setBounds(29, 38, 82, 16);
		contentPane.add(lblNewLabel);
		
		JLabel lblPassword = new JLabel("Password:");
		lblPassword.setBounds(29, 76, 82, 16);
		contentPane.add(lblPassword);
		
		txt_userName = new JTextField();
		txt_userName.setBounds(107, 33, 130, 26);
		contentPane.add(txt_userName);
		txt_userName.setColumns(10);
		
		txt_pwd = new JPasswordField();
		txt_pwd.setColumns(10);
		txt_pwd.setBounds(107, 71, 130, 26);
		contentPane.add(txt_pwd);
		
		JButton btn_login = new JButton("Login");
		btn_login.setBounds(46, 122, 89, 29);
		contentPane.add(btn_login);
		
		JButton btn_cancel = new JButton("Cancel");
		btn_cancel.setBounds(147, 122, 82, 29);
		contentPane.add(btn_cancel);
		
		btn_login.addMouseListener(new MouseListener(){

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				MainFrame main = new MainFrame();
				dispose();
				
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		});
	}
}
