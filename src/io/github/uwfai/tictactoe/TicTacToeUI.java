package io.github.uwfai.tictactoe;

public interface TicTacToeUI
{
   void displayBoard(Board board);
   int getUserMove(Player player);
   boolean resetGame();
   void setResetGame(boolean reset);
}
