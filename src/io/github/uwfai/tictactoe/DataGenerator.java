package io.github.uwfai.tictactoe;

import java.util.Random;

/**
 * Created by carso on 4/13/2017.
 */
public class DataGenerator
{
   static ComputerMove cm = new ComputerMove();

   public static void next(Board bm, Player player)
   {
	   int level=0;
      Player other = (player == Player.X ? Player.O : Player.X);
      for (int y = 0; y < 3; ++y)
      {
         for (int x = 0; x < 3; ++x)
         {
            Board tm = new Board(bm);
            if (tm.getValueAtSquare(y, x) == Player.EMPTY) {
               tm.setBoard(player, y, x);
               if (!tm.checkIfWinner(player))
               {
                  DataGenerator.cm.smartComputerMove(other, tm,level);
                  if (!tm.checkIfWinner(other))
                  {
                     DataGenerator.next(tm, player);
                  }
               }
            }
         }
      }
   }

   public static void main(String[] arg)
   {
      for (int y = 0; y < 3; ++y)
      {
         for (int x = 0; x < 3; ++x)
         {
            Board bm = new Board();
            bm.setBoard(Player.X, y, x);
            next(bm, Player.O);
         }
      }
   }
}
