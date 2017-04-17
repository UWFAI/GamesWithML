package io.github.uwfai.tictactoe;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import java.awt.Color;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JButton;

public class
GameGUI extends JPanel implements TicTacToeUI
{
   private volatile boolean buttonPressed = false;
   private volatile int indexOfButtonPressed;
   boolean computerPlayer = true;
   private volatile boolean resetGame = false;
   private volatile int level =1;
   private JLabel topMessage = new JLabel("Hello");
   private JLabel difficulty = new JLabel("Difficulty:");
   private JLabel bottomMessage;
   private JButton[][] buttons = new JButton[3][3];
   public AccountInformation account = new AccountInformation();

   private JMenuBar theMenuBar = new JMenuBar();
   private JMenuItem vsComp = new JMenuItem("Human vs Human");
   private JMenuItem exit = new JMenuItem("Exit");
   private JMenu optionsMenu = new JMenu("Options");
   private JMenuItem vsHuman = new JMenuItem("Human vs Computer");
   JComboBox<String> comboLanguage = new JComboBox<String>();
   
   public GameGUI()
   {
	   
	   	  setTypePlayer();

	      Panel topMessagePanel = new Panel();
	      Panel topPart = new Panel();
	      Font font = new Font("", Font.BOLD, 20);

	      topMessage.setFont(font);
	      
	      topMessage.setForeground(Color.WHITE);

	      topMessagePanel.add(topMessage);
	      Panel bottomMessagePanel = new Panel();
	      
	      bottomMessage.setFont(font);
	      bottomMessage.setForeground(Color.WHITE);
	      bottomMessagePanel.add(bottomMessage);
	      bottomMessage.setText(account.toString(1));

	      JPanel panel = new JPanel(new BorderLayout());
	      panel.setBackground(Color.blue);


	      difficulty.setForeground(Color.WHITE);
	      difficulty.setFont(font);
	      
	      topPart.add(difficulty);
	      topPart.add(comboLanguage, BorderLayout.NORTH);
	      topPart.add(topMessagePanel, BorderLayout.CENTER);
	      comboBox();
	      panel.add(topPart, BorderLayout.NORTH);
	      panel.add(addButtonToPanel(), BorderLayout.CENTER);
	      panel.add(bottomMessagePanel, BorderLayout.SOUTH);

	      panel.setSize(900,500);
	       menuSetter();
	      JFrame frame = new JFrame();
	      frame.setJMenuBar(this.theMenuBar);
	      frame.add(panel);
	      frame.setTitle("AIRG Tictactoe");

	      frame.setSize(900, 800);
	      frame.setVisible(true);

     

   }
   public int getLevel()
   {
	   return level;
   }
   public void comboBox()
   {
		// add items to the combo box
		comboLanguage.addItem("Easy");
		comboLanguage.addItem("Medium");
		comboLanguage.addItem("Hard");
		
	   comboLanguage.addActionListener(new ActionListener() {
	    	  
   	    @Override
   	    public void actionPerformed(ActionEvent event) {
   	        JComboBox<String> combo = (JComboBox<String>) event.getSource();
   	        String selectedBook = (String) combo.getSelectedItem();
   	 
   	        if (selectedBook.equals("Easy")) 
   	        {
   	        	level =1;
   	            System.out.println("Good choice!");
   	        } 
   	        if (selectedBook.equals("Medium")) 
   	        {
   	        	level = 2;
   	            System.out.println("Nice pick, too!");
   	        }
   	        if (selectedBook.equals("Hard")) 
   	        {
   	        	level = 3;
   	            System.out.println("hard"+level);
   	        }
   	    }
   	});
   }
   public void menuSetter()
   {
      theMenuBar.add(optionsMenu);
      optionsMenu.add(exit);
      optionsMenu.add(vsComp);
      optionsMenu.add(vsHuman);

      exit.addActionListener(new Exit());
      vsHuman.addActionListener(new VsHumanMenuListener());
      vsComp.addActionListener(new VsCompMenuListener());
   }
   private void setTypePlayer()
   {
      if(computerPlayer)
      bottomMessage = new JLabel(account.toString(1));
      else
         bottomMessage = new JLabel(account.toString(0));
   }

   private Panel addButtonToPanel()

   {

      Panel panel = new Panel();
      panel.setLayout(new GridLayout(3, 1, 2, 3));
      Font font = new Font("Courier", Font.BOLD, 90);
      panel.setBackground(Color.magenta);
      int count = 0;

      for (int i = 0; i < 3; i++)

      {

         for (int j = 0; j < 3; j++)

         {

            count++;

            buttons[i][j] = new JButton("" + count);

            panel.add(buttons[i][j]);

            buttons[i][j].addActionListener(new ButtonsClick());

            buttons[i][j].setPreferredSize(new Dimension(145, 140));

            buttons[i][j].setForeground(Color.BLACK);

            buttons[i][j].setFont(font);



         }

      }

      return panel;

   }
   public  int doYouWantToPlayAgain(String message)
   {
      topMessage.setText(message);

      int response = JOptionPane.showConfirmDialog(null, message + "\n Do you want to play again? ", "Play Again", JOptionPane.YES_NO_OPTION);
      return response;
   }

   @Override public void displayBoard(Board board)
   {
      int count =0;
      for (int i=0; i<3; i++)
      {
         for(int j=0; j<3; j++)
         {
            count++;
            if(board.getValueAtSquare(i,j) ==Player.X || board.getValueAtSquare(i,j)  ==Player.O)
            {
               buttons[i][j].setText(board.getValueAtSquare(i,j) .getShortName()+"" );
            }
            else
               buttons[i][j].setText(count+"" );

         }
      }
   }

   @Override public int getUserMove(Player player)
   {

	   String say ="Who's Playing: ";
      if(computerPlayer)
      {
         bottomMessage.setText(account.toString(1));
         if(player==Player.O)
            topMessage.setText(say+"AI-"+player.getColor() + " turn.");
         else
            topMessage.setText(say+"You"+"-"+player.getColor() + " turn.");

      }
      else
      {
         bottomMessage.setText(account.toString(0));
         if(player==Player.O)
            topMessage.setText(say+"Guest-"+player.getColor() + " turn.");
         else
            topMessage.setText(say+"You"+"-"+player.getColor() + " turn.");

      }
      while (!buttonPressed && !resetGame)
      {
         //Do nothing

      }

      buttonPressed = false;
      return indexOfButtonPressed;

   }

   @Override public boolean resetGame()
   {
      return resetGame;
   }

   private class VsHumanMenuListener implements ActionListener
   {

      public void actionPerformed(ActionEvent e)

      {
         computerPlayer=true;
         resetGame = true;

      }

   }

   private class VsCompMenuListener implements ActionListener
   {

      public void actionPerformed(ActionEvent e)

      {
         computerPlayer=false;
         resetGame = true;
      }

   }
   private class Exit implements ActionListener
   {

      public void actionPerformed(ActionEvent e)

      {
         int response = JOptionPane.showConfirmDialog(null, "Are you sure you want to exit this program? ", "Exit for Real?", JOptionPane.YES_NO_OPTION);
         if(response==0)
         System.exit(0);
      }

   }
   public void setResetGame(boolean reset)
   {
      resetGame = reset;
   }
   private class ButtonsClick implements ActionListener
   {
      @Override public void actionPerformed(ActionEvent e)
      {
         String newValue= ((JButton)e.getSource()).getText();
         if((int)newValue.charAt(0)>=49 && (int)newValue.charAt(0)<=57)
         {
            indexOfButtonPressed = Integer.valueOf(newValue);
            buttonPressed = true;

         }

      }
   }

}
