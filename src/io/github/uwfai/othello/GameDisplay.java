package io.github.uwfai.othello;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;


public class GameDisplay implements OthelloUI
{
	private volatile boolean isClick=false;
	private volatile boolean errorMessage =false;
	private int MainROW=8;
	private int MainCOL=8;
	private volatile int xCordinate;
	private volatile int yCordinate;
	private JButton[][] buttons = new JButton[MainCOL][MainROW];
	private JLabel topMessage = new JLabel("Welcome", SwingConstants.CENTER);	// Center align to reduce the need for spaces
	private JLabel bottomMessage = new JLabel("Welcome");
	
	public GameDisplay()
	{
		buttons = new JButton[MainCOL][MainROW];
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
	      panel.add(topMessage, BorderLayout.NORTH);
	      panel.add(bottomMessage, BorderLayout.SOUTH);
	      panel.add(addButtonToPanel(), BorderLayout.CENTER);

	      return panel;
	   }
	   private class ButtonsClick implements ActionListener
	   {
	      @Override public void actionPerformed(ActionEvent e)
	      {

	         String newValue= ((JButton)e.getSource()).getText();
	         
	         Point p = getPressedButton(e);
	         xCordinate = p.x;
	         yCordinate = p.y;
	         isClick = true;
	         
	      }
	   }
	   
	   private Point getPressedButton(ActionEvent evt){
		    Object source = evt.getSource();
		    for(int i = 0; i < MainCOL; i++){
		        for(int j = 0; j < MainROW; j++){
		            if(buttons[i][j] == source)
		                return new Point(i,j);
		        }
		    }
		    return null;
		  }
	   
   	/**
   	 * Adds the button to panel.
   	 *
   	 * @return the panel
   	 */
   	private Panel addButtonToPanel()
	   {
	      Panel panel = new Panel();
	      panel.setLayout(new GridLayout(MainCOL, MainROW,2,2));
	      Font font = new Font("Courier", Font.BOLD, 20);
	      panel.setBackground(Color.LIGHT_GRAY);
	      for (int i = 0; i <MainCOL ; i++)
	      {
	         for (int j = 0; j < MainROW; j++)
	         {

	            buttons[i][j] = new JButton();

	            panel.add(buttons[i][j]);
	            buttons[i][j].addActionListener(new ButtonsClick());

	            buttons[i][j].setPreferredSize(new Dimension(5, 5));
	            buttons[i][j].setForeground(Color.BLACK);
	            buttons[i][j].setBackground(Color.GRAY);

	            buttons[i][j].setFont(font);



	         }
	      }
	      return panel;
	   }
	public void print(Board board,int[] movesOnBoard)
	{
		bottomMessage.setText(String.format("Black has %d pieces%s | White has %d pieces", movesOnBoard[0], '\n', movesOnBoard[1]));
		for ( int i=0; i<board.size; i++)
	      {
	         for( int j=0; j<board.size; j++)
	         {  
	                     

	                      if(board.getPlayerWithIndex(i, j)==Player.Empty)
	                     {
	                    	 buttons[i][j].setBackground(Color.GREEN);
	                     }
	                     else if(board.getPlayerWithIndex(i, j)==Player.Black)
	                     {
	                    	 buttons[i][j].setBackground(Color.black); 
	                     }
	                     else if(board.getPlayerWithIndex(i, j)==Player.White)
	                     {
	                    	 buttons[i][j].setBackground(Color.WHITE); 
	                     }
	         }
	      }
	}
	public void setErrorMessage(boolean b)
	{
		errorMessage = b;
	}
	public boolean isClick()
	{
		return isClick;
	}
	public void setIsClick(boolean click)
	{
		isClick = click;
	}
	public void errorSpaceMessage()
	{
		if(errorMessage)
		{
			bottomMessage.setText("Space is Ocupy");
			Font font = new Font("Courier", Font.BOLD, 30);
			bottomMessage.setFont(font);
			
		}
	}
	
	public void errorOutMessage()
	{
		if(errorMessage)
		{

			// If the thread is already running, don't create a new thread
			if (getThreadByName("MessageReversionThread") != null)
				return;
			// Display move status in a thread
			new MessageReversion("MessageReversionThread").start();

		}
	}
	
	public int[] makeMove(Player player)
	{
		setTopMessageTextWithPlayer(player);
		Font font = new Font("Courier", Font.BOLD, 50);
		topMessage.setFont(font);
		int[] move =  new int[]{xCordinate,yCordinate};
		
		return move;
	}

	public void setTopMessageTextWithPlayer(Player player) {
		// Implemented this method because the the players turn got out of sync with reality
		// after implementing the double click fix. Basically removed this from makeMove()
		// and put it here so it didn't depend on makeMove() to be called.
		topMessage.setText(player+" player turn");
	}

	public Thread getThreadByName(String threadName) {
		for (Thread t : Thread.getAllStackTraces().keySet()) {
			if (t.getName().equals(threadName)) return t;
		}
		return null;
	}

	/**
	 * The class displays a message on the bottom, then reverts the message back.
	 * Runs on a separate thread
	 */
	class MessageReversion extends Thread {
		public MessageReversion(String message) {
			super(message);
		}

		public void run() {
			try
			{
				// Set name of thread
				Thread.currentThread().setName("MessageReversionThread");
				Font font = new Font("Courier", Font.BOLD, 30);
				bottomMessage.setFont(font);
				bottomMessage.setText("Illegal Move");
				// Sleep for 1.5 seconds. Adjust as needed
				sleep(1500);
				bottomMessage.setText("Welcome");
			} catch (InterruptedException i) {

			}
		}
	}
	
}
