package io.github.uwfai.tictactoe;

public class TicTacToeStarter
{
   public TicTacToeStarter()
   {
	   start();
   }
	public static void restart(GameGUI gameGUI, Game game)
   {
      game.getBoard().refreshBoard();
      gameGUI.displayBoard(game.getBoard());
      gameGUI.setResetGame(false);
   }
   public static void  start()
   {
	   GameGUI gameGUI = new GameGUI();
	      Game game = new Game(gameGUI);

	     boolean go= true;
	     int playAgain=0;
	     do
	     {

	        while(go)
	        {
	           if(gameGUI.resetGame())
	           {
	              restart(gameGUI,game);
	           }

	           gameGUI.displayBoard(game.getBoard());
	           go = game.humanPlayGame(Player.X);
	           if(go)
	           {
	              if(gameGUI.resetGame())
	              {
	                 restart(gameGUI,game);
	              }


	              if(gameGUI.computerPlayer)
	              {
	                 go = game.computerPlayGame(Player.O);
	              }
	              else
	              {
	                 gameGUI.displayBoard(game.getBoard());
	                 go = game.humanPlayGame(Player.O);


	              }

	           }

	        }

	        if(game.gameIsTie())
	        {
	           gameGUI.displayBoard(game.getBoard());
	           if(gameGUI.computerPlayer)
	           {
	              gameGUI.account.setTiedWithComputer(gameGUI.account.getTiedWithComputer() + 1);
	           }
	           else
	           {
	              gameGUI.account.setTiedWithHuman(gameGUI.account.getTiedWithHuman() + 1);
	           }
	           playAgain = gameGUI.doYouWantToPlayAgain("The game is a tie");
	           if(playAgain==0)
	           {
	              game.getBoard().refreshBoard();
	              game.setTie(false);
	              go=true;
	           }
	           else
	              System.exit(0);
	        }
	        if(game.getWinner())
	        {
	           gameGUI.displayBoard(game.getBoard());

	           if(game.getGameWinner()==Player.O)
	           {
	              if(gameGUI.computerPlayer)
	              gameGUI.account.setComputerWin(gameGUI.account.getComputerWin() + 1);
	              else
	              gameGUI.account.setguestWin(gameGUI.account.getGuestWin() + 1);
	           }
	           else
	           {
	              if(gameGUI.computerPlayer)
	              {
	                 gameGUI.account.setNumberOfWinC(gameGUI.account.getNumberOfWinC() + 1);
	              }
	              else
	              {
	                 gameGUI.account.setNumberOfWin(gameGUI.account.getNumberOfWin() + 1);
	              }
	           }

	           playAgain = gameGUI.doYouWantToPlayAgain("the winner is: "+ game.getGameWinner());
	           if(playAgain==0)
	           {
	              game.getBoard().refreshBoard();
	              game.setWinner(false,Player.EMPTY);
	              go=true;
	           }
	           else
	              System.exit(0);
	        }
	        gameGUI.account.setNumberOfPlay(gameGUI.account.getNumberOfPlay()+1);

	     }while(playAgain==0); 
   }
   public static void main(String[] args)
   {
	 // TicTacToeStarter start = new TicTacToeStarter();

   }
}
