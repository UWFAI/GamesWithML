package io.github.uwfai.tictactoe;

public enum  Player
{
    EMPTY, X, O;

    public Player getOtherColor()
    {
        switch(this) {
            case X:
                return O;
            case O:
                return X;
            case EMPTY:
            default:
                return EMPTY;


        }
    }
    public char getShortName()
    {
        switch (this) {
            case X:
                return 'X';
            case O:
                return 'O';
            case EMPTY:
            default:
                return 'E';


        }
    }
    public Player getColor()
    {
        return this;
    }


}
