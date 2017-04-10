package GameController;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class Controller {
	private volatile boolean isClick=false;
	private volatile boolean errorMessage =false;


	private JButton othello = new JButton();
	private JButton tictactoe = new JButton();
	
	private JLabel topMessage = new JLabel("Welcome", SwingConstants.CENTER);	// Center align to reduce the need for spaces
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
	   
	      panel.add(option, BorderLayout.CENTER);

	      return panel;
	   }
	   public void setButtonsSize()
	   {
		  // Dimension d = new Dimension();
		   othello.setSize(new Dimension(50,40));
	   }
	   private class ButtonsClick implements ActionListener
	   {
	      @Override public void actionPerformed(ActionEvent e)
	      {

	         String newValue= ((JButton)e.getSource()).getText();
	         
	         isClick = true;
	         
	      }
	   }

}
