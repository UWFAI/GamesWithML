package io.github.uwfai.tictactoe;

public class MakeMove
{
    private Board board=null;
    ValueSetter valueSet;



    public MakeMove()
    {
        board=new Board();
        valueSet = new ValueSetter();
    }

    public Board getBoard()
    {
        return board;
    }

    public boolean makeMove(int move, Player player)
    {
        valueSet.settingActualValue(move+"");
        int yvalue=valueSet.getCol();
        int xvalue=valueSet.getRow();
        if(board.getValueAtSquare(xvalue,yvalue)==player.EMPTY)
        {
            board.setBoard(player, xvalue, yvalue);
            if(board.checkIfWinner(player))
            {
                return true;
            }
        }

        return false;
    }

}
