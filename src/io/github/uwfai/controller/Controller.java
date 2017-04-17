package io.github.uwfai.controller;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import io.github.uwfai.othello.GameDisplay;
import io.github.uwfai.othello.TheGame;
import io.github.uwfai.tictactoe.TicTacToeStarter;

public class Controller {
	private volatile boolean isClick=false;
	private volatile boolean errorMessage =false;


	private JButton[] gameOption = new JButton[2];
	private JButton othello = new JButton("Othello");
	private JButton tictactoe = new JButton("Tictactoe");
	
	private JLabel topMessage = new JLabel("Select A Game", SwingConstants.CENTER);	// Center align to reduce the need for spaces
	private JLabel bottomMessage = new JLabel("Welcome");
	
	public Controller()
	{
	    setFrame();
	}
	
	   public void setFrame()
	   {
	      JFrame frame = new JFrame();

	         JPanel panel = new JPanel(new BorderLayout());

	       
	         panel.setBackground(Color.BLUE);
	         frame.add(allPanel());

	         frame.setTitle("AIRG Othello Game");
	         frame.setVisible(true);;
	         frame.setSize(900,900);
	         frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);   

	   }
	   public JPanel allPanel()
	   {
	      JPanel panel = new JPanel( new BorderLayout());

	     

	      
	      Font font = new Font("Courier", Font.BOLD, 30);
		   // Set font for top here, so it does not need to be set later
		   Font topFont = new Font("Courier", Font.BOLD, 50);
		   topMessage.setFont(topFont);
		  bottomMessage.setFont(font);
		  JPanel option= new JPanel();
		  option.add(othello);
		  option.add(tictactoe);
	      panel.add(topMessage, BorderLayout.NORTH);
	   
	      panel.add(setButtons(), BorderLayout.CENTER);

	      return panel;
	   }
	   public Panel setButtons()
	   {
		   gameOption[0] = new JButton("Othello");
		   gameOption[1] = new JButton("Tictactoe");
		   othello.setSize(new Dimension(50,40));
		   Panel panel = new Panel();
		      panel.setLayout(new GridLayout(2,2,2,2));
		      Font font = new Font("Courier", Font.BOLD, 80);
		      panel.setBackground(Color.LIGHT_GRAY);
		      for (int i = 0; i <2 ; i++)
		      {

		            panel.add(gameOption[i]);
		            gameOption[i].addActionListener(new ButtonsClick());

		            gameOption[i].setPreferredSize(new Dimension(5, 5));
		            gameOption[i].setForeground(Color.BLACK);
		            gameOption[i].setBackground(Color.ORANGE);

		            gameOption[i].setFont(font);
		      }
		     return panel; 
	   }
	   private class ButtonsClick implements ActionListener
	   {

		   @Override public void actionPerformed(ActionEvent e)
	      {

	         String newValue= ((JButton)e.getSource()).getText();
	         if(newValue.equals("Othello"))
	         {
	        	 TheGame game = new TheGame();
	     		  game.playGame();
	         }
	         else
	         {
	       // 	  new SecondFrame().setVisible(true);
	       // 	    FirstFrame.this.dispose();
	        	    
	        	 TicTacToeStarter start = new TicTacToeStarter();
	         }
	         System.out.println(newValue+"------");
	         isClick = true;
	         
	      }
	   }

}
