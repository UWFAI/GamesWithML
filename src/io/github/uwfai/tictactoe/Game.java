package io.github.uwfai.tictactoe;

import io.github.uwfai.tictactoe.Game;
public class Game
{
   private TicTacToeUI gui;
   private MakeMove move=null;
   private ComputerMove computer=null;
   private boolean tie=false;
   private boolean winner=false;
   private io.github.uwfai.othello.Player gameWinner;

   public Game(TicTacToeUI ticTacToeUI)
   {
      gui= ticTacToeUI;
      move= new MakeMove();
      computer =new ComputerMove();
   }
   public Board getBoard()
   {
      return move.getBoard();
   }
   public boolean gameIsTie()
   {
      return tie;
   }
   public void setTie(boolean tie)
   {
      this.tie=tie;
   }
   public void setWinner(boolean winner, Player player)
   {
      this.winner=winner;
      gameWinner = player;
   }
   public Player getGameWinner()
   {
      return gameWinner;
   }
   public boolean getWinner()
   {
      return winner;
   }
   private boolean gameStatus(MakeMove move, Player player, boolean win)
   {
      if(move.getBoard().boardIsFull() ||win)
      {
         if(win)
         {
            setTie(false);
            setWinner(win, player);
         }
         else
            setTie(true);
         return false;
      }
      return true;
   }

   public boolean computerPlayGame(Player player)
   {
      boolean win=computer.makeMove(player,move.getBoard());
      return gameStatus(move,player,win);
   }

   public boolean humanPlayGame(Player player)
   {
      boolean win;

         int value = gui.getUserMove(player);
         win =  move.makeMove(value, player);

      return gameStatus(move,player,win);
   }

}

