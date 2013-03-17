package com.derpg.dnet;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class MainMenu extends JFrame implements ActionListener {
	
	private JTextField username;
	private JPasswordField password;
	private JButton login;

	public MainMenu() {
		this.setTitle("DERPG Network Login");
		
		JPanel lpanel = new JPanel();
		
		this.setSize(new Dimension(250, 185));
		
		username = new JTextField(15);
		password = new JPasswordField(15);
		login = new JButton("Log In");
		login.addActionListener(this);
		
		lpanel.add(new JLabel("--------DERPG Network Login--------"));
		lpanel.add(new JLabel("Username: "));
		lpanel.add(username);
		lpanel.add(new JLabel("Password: "));
		lpanel.add(password);
		lpanel.add(login);
		
		this.add(lpanel);
		this.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		JFrame frame = new JFrame();
		frame.setTitle("DERPG Network");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(new Dimension(600, 400));
		
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		
		Game game = new Game(username.getText());
		frame.getContentPane().add(game);
		frame.pack();
		
		frame.setVisible(true);
		
		this.setVisible(false);
	}
	
}
