package io.github.uwfai.othello;

public class RefGame
{
	public boolean[] error= new boolean[2];

	
	 // Declare the constants
	  /** A constant indicating the size of the game board. */
	  private static final int BOARD_SIZE = 8;
	  
	  // Declare the instance variables
	  /** This array keeps track of the logical state of the game. */
	  private Player[][] grid = new Player[BOARD_SIZE][BOARD_SIZE]
	    ;
	  /** This board contains the physical state of the game. */
	  private Board gameBoard =null;
	  
	  
	  /** 
	   * Othello constructor.
	   * 
	   * PRE: none
	   * POST: Logically and physically intializes the Othello game with pegs.
	   */ 
	  public RefGame(Board board)
	  {
		  gameBoard= board;
	    // ADD CODE HERE
	    for (int i=0; i<grid.length; i++)
	    {
	      for (int j=0; j<grid[i].length; j++)
	      {
	        grid[i][j]=Player.Empty;
	      }
	    }
	    grid[3][3]=Player.White;
	    grid[3][4]=Player.Black;
	    grid[4][3]=Player.Black;
	    grid[4][4]=Player.White;
	    
	    // This method must be called to refresh the board after the logic of the game has changed
	    this.updateView(gameBoard);
	  }

		public boolean getErrorIndex(int i)
		{
			return error[i];
		}
	  

	    
	  /** 
	   * This method will handle the logic of a single turn in the Othello game.
	   * It should "flip" the opponents pegs wherever they are surrounded in a line
	   * by the latest peg played and the first peg of the player's same colour in
	   * that line.
	   * The method this.updateView() should be called at the end of this method.
	   * 
	   * PRE: 0 <= row < this.gameBoard.getRows() && 0 <= col < this.gameBoard.getColumns()
	   *      colour == BLACK_UP || WHITE_UP
	   *      The row and col values are a valid location for a move in the game.
	   * POST: The pegs of the opposite colour are flipped according to the rules of Othello.
	   */
	  public void takeTurn(Player turn, int row, int col)
	  {
	    // ADD CODE HERE
	    //Check Above
	    grid[row][col]=turn;
	    //check above & below
	    direction(row, col, turn, 0, -1);
	    direction(row, col, turn, 0, 1);
	    //check right & right 
	    direction(row, col, turn, 1,0);
	    direction(row, col, turn, -1, 0);
	    //check corners
	    direction(row, col, turn, 1,1);
	    direction(row, col, turn, 1,-1);
	    direction(row, col, turn, -1,1);
	    direction(row, col, turn, -1,-1);
	    
	    // This method must be called to refresh the board after the logic of the game has changed
	    this.updateView(gameBoard);
	  }
	  /*This method will check the colours of pegs in an indicated direction
	   *  PRE: 0 <= row < this.gameBoard.getRows() && 0 <= col < this.gameBoard.getColumns()
	   *      colour == BLACK || WHITE
	   *      The row and col values are a valid location for a move in the game.
	   * POST: The pegs of the opposite colour are flipped according to the rules of Othello
	   *       when the method is called and the parameters are entered correctly.
	   */
	  private void direction(int row, int column, Player colour, int colDir, int rowDir)
	  {
	    int currentRow= row + rowDir;
	    int currentCol = column + colDir;
	    if (currentRow==8 || currentRow<0 || currentCol==8 || currentCol<0)
	    {
	      return;
	    }
	    while (grid[currentRow][currentCol]==Player.Black || grid[currentRow][currentCol]==Player.White)
	    {
	      if (grid[currentRow][currentCol]==colour)
	      {
	        while(!(row==currentRow && column==currentCol))
	        {
	          grid[currentRow][currentCol]=colour;
	          currentRow=currentRow-rowDir;
	          currentCol=currentCol-colDir;
	        }
	        break;
	      }else
	      {
	      currentRow=currentRow + rowDir;
	      currentCol=currentCol + colDir;
	      }
	      if (currentRow<0 || currentCol<0 || currentRow==8 || currentCol==8)
	      { 
	        break;
	      }
	    }
	  }
	  /** 
	   * This method will determine if the player has selected a valid move.
	   * A valid move is when the player has selected an empty square, adjacent
	   * to a peg of the opposite colour on the board.
	   * 
	   * PRE: none
	   * POST: Returns true when the move was valid, false otherwise.
	   */
	  public boolean validMove(Player turn, int row, int col)
	  {
	    // ADD CODE HERE
	    boolean result=false;
	    Player oppCol=Player.Black;
	    if (turn==Player.Black)
	    {
	      oppCol=Player.White;
	    }
	    error[0]=false;
	    error[1]=false;
	  //current
	  if (grid[row][col]==Player.Empty)
	  {
	    if (row+1<8 && col+1<8 && grid[row+1][col+1]==oppCol)
	    { 
	      result=true;
	    }else if(row+1<8 && grid[row+1][col]==oppCol)
	    {
	      result=true;
	    }else if(col+1<8 && grid[row][col+1]==oppCol)
	    {
	      result=true;
	    }else if (col-1>-1 && grid[row][col-1]==oppCol)
	    {
	      result=true;
	    }else if (row-1>-1 && col-1>-1 && grid[row-1][col-1]==oppCol)
	    {
	      result=true;
	    }else if (row-1>-1 && grid[row-1][col]==oppCol)
	    { 
	      result=true;
	    }else if(row-1>-1 && col+1<8 && grid[row-1][col+1]==oppCol)
	    {
	      result=true;
	    }else if (row+1<8 && col-1>-1 && grid[row+1][col-1]==oppCol)
	    {
	      result = true;
	    }
	    else
	    {
	    	error[1]=true;
	    }
	  }
	  else
	  {
		  error[0]=true;
	  }
	  return result;
	}

	  
	  /** 
	   * This method will determine when the game is over.
	   * The game is over when the board is filled with pegs.
	   * 
	   * PRE: none
	   * POST: Returns true when there are no valid moves left, false otherwise.
	   */
	  public boolean gameOver()
	  {
	    // ADD CODE HERE
	    boolean full=false;
	    int countTot=0;
	    for (int i=0; i<8; i++)
	    {
	      for (int j=0; j<8; j++)
	      {
	        if (grid[i][j]==Player.Black || grid[i][j]==Player.White)
	        {
	          countTot++;
	        }
	      }
	    }
	        if (countTot==64)
	        {
	          full=true;
	        }
	    return full;
	  }
   
	  /** 
	   * This method will reflect the current state of the pegs on the board.
	   * It should be called at the end of the constructor and the end of the
	   * takeTurn method and any other time the game has logically changed.
	   * 
	   * NOTE: This is the only method that requires calls to putPeg and removePeg.
	   *       All other methods should manipulate the logical state of the game in the 
	   *       grid array and then call this method to refresh the gameBoard.
	   * 
	   * PRE: none
	   * POST: Where the array holds a value of BLACK, a black peg is put in that spot.
	   *       Where the array holds a value of WHITE, a white peg is put in that spot.
	   *       Where the array holds a value of Empty, a peg should be removed from that spot.
	   */
	  public void updateView(Board board)
	  {
	    for (int i=0; i<grid.length; i++)
	    {
	      for (int j=0; j<grid[i].length; j++)
	      {
	        if (grid[i][j]==Player.White)
	        {
	          board.setValue(i, j, Player.White);
	        }else if(grid[i][j]==Player.Black)
	        {
	          board.setValue(i, j, Player.Black);
	        }
	      }
	    }

	  }
	
}
