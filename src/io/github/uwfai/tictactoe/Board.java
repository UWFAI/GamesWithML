package io.github.uwfai.tictactoe;

public class Board
{
    public static final int ROWS =3, COLUMNS=3;
    private Player[][] player=null;

    public Board()
    {
        player=new Player[ROWS][COLUMNS];
        refreshBoard();
    }

    public void refreshBoard()
    {
        for(int i = 0; i< ROWS; i++)
        {
            for(int j = 0; j< COLUMNS; j++)
            {

                    player[i][j] = Player.EMPTY;

            }
        }
    }
    public void setBoard(Player players, int i, int j)
    {

        player[i][j] = players;
    }

    public int getRow()
    {
        return ROWS;
    }

    public int getCol()
    {
        return COLUMNS;
    }

    public Player getValueAtSquare(int row, int column)
    {
        return player[row][column];
    }

    public boolean checkIfWinner(Player player)
    {
        if(getValueAtSquare(0,0)== getValueAtSquare(1,0) && getValueAtSquare(1,0)== getValueAtSquare(2,0) &&getValueAtSquare(0,0)== player)
            return true;
        else if(getValueAtSquare(0,1)== getValueAtSquare(1,1) && getValueAtSquare(1,1)== getValueAtSquare(2,1) &&getValueAtSquare(0,1)== player)
            return true;
        else if(getValueAtSquare(0,2)== getValueAtSquare(1,2) && getValueAtSquare(1,2)== getValueAtSquare(2,2) &&getValueAtSquare(0,2)== player)
            return true;
        else if(getValueAtSquare(0,0)== getValueAtSquare(0,1) && getValueAtSquare(0,1)== getValueAtSquare(0,2) &&getValueAtSquare(0,0)== player)
            return true;
        else if(getValueAtSquare(1,0)== getValueAtSquare(1,1) && getValueAtSquare(1,1)== getValueAtSquare(1,2) &&getValueAtSquare(1,0)== player)
            return true;
        else if(getValueAtSquare(2,0)== getValueAtSquare(2,1) && getValueAtSquare(2,1)== getValueAtSquare(2,2) &&getValueAtSquare(2,0)== player)
            return true;
        else if(getValueAtSquare(0,0)== getValueAtSquare(1,1) && getValueAtSquare(1,1)== getValueAtSquare(2,2) &&getValueAtSquare(0,0)== player)
            return true;
        else if(getValueAtSquare(2,0)== getValueAtSquare(1,1) && getValueAtSquare(1,1)== getValueAtSquare(0,2) &&getValueAtSquare(2,0)== player)
            return true;
        else
            return false;
    }


    public String toString()
    {

        String value="------------------------------------- \n";
        String count="";
        for(int i = 0; i< ROWS; i++)
        {
            for(int j = 0; j< COLUMNS; j++)
            {
                char value2=player[i][j].getShortName();
                value=value+value2;
            }
            value=value+"\n";
        }

        return value+"  "+count;
    }


    public boolean boardIsFull()
    {
        for(int i = 0; i< ROWS; i++)
        {
            for(int j = 0; j< COLUMNS; j++)
            {
                if (player[i][j] == Player.EMPTY)
                    return false;
            }
        }
            return true;
    }

}
