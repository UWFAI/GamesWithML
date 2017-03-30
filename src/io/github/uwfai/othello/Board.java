package io.github.uwfai.othello;
// TODO: Auto-generated Javadoc
/**
 * The Class Board.
 */
public class Board 
{
	
	/** The board. */
	private Player[][] board;
	
	/** The size. */
	public int size =8;
	
	
	/**
	 * Instantiates a new board.
	 */
	public Board()
	{
		board = new Player[size][size];
		reset();
	}

	public int[] getOccupiedSpaces() {
		int blackOccupied = 0;
		int whiteOccupied = 0;
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if (board[i][j] == Player.Black)
					blackOccupied++;
				else if (board[i][j] == Player.White)
					whiteOccupied++;
			}
		}

		return new int[] {blackOccupied, whiteOccupied};
	}

	/**
	 * Reset.
	 */
	public void reset() 
	{
		for(int i=0; i<size; i++)
		{
			for(int j=0; j<size; j++)
			{
				board[i][j]= Player.Empty;
			}
		}
		
		board[3][3]=Player.Black;
		board[4][4]=Player.Black;
		board[3][4]=Player.White;
		board[4][3]=Player.White;
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * Gets the player with index.
	 *
	 * @param x the x
	 * @param y the y
	 * @return the player with index
	 */
	public Player getPlayerWithIndex(int x, int y)
	{
		return board[x][y];
	}
	
	/**
	 * Sets the value.
	 *
	 * @param x the x
	 * @param y the y
	 * @param value the value
	 */
	public void setValue(int x, int y, Player value)
	{

			board[x][y] = value;
	}

	

}
