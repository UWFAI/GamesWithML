package io.github.uwfai.othello;

public class TheGame
{
	Board board = new Board();
	OthelloUI display = new GameDisplay();
	RefGame ref = new RefGame(board);
	
	public void playGame()
	{
		while(!ref.gameOver())
		{
			
			makeMoves(Player.Black);
			makeMoves(Player.White);
			
			
		}
	}

	/**
	 * Make moves.
	 *
	 * @param player the player
	 */
	private void makeMoves(Player player) 
	{
		boolean keepGoing= false;
		do{

			display.setErrorMessage(false);
            display.setTopMessageTextWithPlayer(player);
			
//<<<<<<< HEAD
//=======
			int[] movesOnBoard = board.getOccupiedSpaces();
			display.print(board,movesOnBoard);
			
			//int[] move = display.makeMove(player);
//>>>>>>> branch 'master' of https://github.com/UWF-AIRG/othello.git

			while(!display.isClick())
			{
				
			}
			// My possible bug fix. This move was being called before the display was clicked.
			// The first time it was called, the move would be 0,0, which is not a legal move
			// when the game first starts.
			int[] move = display.makeMove(player);
			System.out.println(move[0]+"-------------"+ move[1]);
			display.setIsClick(false);
			keepGoing = ref.validMove(player, move[0], move[1]);
			System.out.println(keepGoing);
			if(keepGoing==true)
			{
				board.setValue(move[0], move[1], player);
				ref.takeTurn(player, move[0], move[1]);
			}
			if(!keepGoing)
			{
				display.setErrorMessage(true);
				if(ref.getErrorIndex(0))
				{
					display.errorSpaceMessage();
				}
				else if(ref.getErrorIndex(1))
				{
					display.errorOutMessage();
				}
			}
		}while(!keepGoing);
		
	}
	

}
