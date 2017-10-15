package io.github.uwfai.tictactoe;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import io.github.uwfai.neural.Matrix;
import io.github.uwfai.neural.NeuralNetwork;

public class ComputerMove
{

   private NeuralNetwork NN0;
   private NeuralNetwork NN1;
   private NeuralNetwork NN2;
   private NeuralNetwork NN3;
   private NeuralNetwork NN4;
   private NeuralNetwork NN5;
   private NeuralNetwork NN6;
   private NeuralNetwork NN7;
   private NeuralNetwork NN8;

   private boolean computerTurn = true;

   public ComputerMove()
   {
      boolean cont = true;
      File f;
      FileReader fr;
      BufferedReader br;
      for (int n = 0; n < 9; ++n) {
         f = new File("NN"+n+".json");
         cont &= f.exists();
      }
      if (cont) {
         try
         {
            ArrayList<NeuralNetwork> NNs = new ArrayList<NeuralNetwork>();
            NNs.add(NN0);
            NNs.add(NN1);
            NNs.add(NN2);
            NNs.add(NN3);
            NNs.add(NN4);
            NNs.add(NN5);
            NNs.add(NN6);
            NNs.add(NN7);
            NNs.add(NN8);

            String json = "";

            for (int n = 0; n < 9; ++n) {
               f = new File("NN"+n+".json");
               fr = new FileReader(f);
               br = new BufferedReader(fr);
               String line;
               while ((line = br.readLine()) != null) {
                  json += line;
               }
               br.close();
               fr.close();
               NNs.set(n, NeuralNetwork.Load(json));
            }
         }
         catch (Exception e)
         {

         }
      }
   }

   private int evaluateLine(Player player,Board board,int row1, int col1, int row2, int col2, int row3, int col3)
   {
      int score = 0;

      // First cell
      if (board.getValueAtSquare(row1,col1) == player)
      {
         score = 1;
      }
      else if (board.getValueAtSquare(row1,col1) == player.getOtherColor())
      {
         score = -1;
      }

      // Second cell
      if (board.getValueAtSquare(row2,col2) == player)
      {
         if (score == 1) {   // cell1 is mySeed
            score = 10;
         } else if (score == -1) {
            return 0;
         } else {
            score = 1;
         }
      } else if (board.getValueAtSquare(row2,col2) == player.getOtherColor()) {
         if (score == -1) {
            score = -10;
         } else if (score == 1) {
            return 0;
         } else {
            score = -1;
         }
      }

      // Third cell
      if (board.getValueAtSquare(row3,col3) == player)
      {
         if (score > 0)
         {
            score *= 10;
         }
         else if (score < 0)
         {
            return 0;
         }
         else
         {
            score = 1;
         }
      }
      else if
            (board.getValueAtSquare(row3,col3) == player.getOtherColor())
      {
         if (score < 0)
         {
            score *= 10;
         } else if (score > 1)
         {
            return 0;
         } else {
            score = -1;
         }
      }
      return score;
   }

   //   public int score(Board game, int depth, Player player)
   //   {
   //       if(game.checkIfWinner(player))
   //         return 10 - depth;
   //       else if (game.checkIfWinner(player.getOtherColor()))
   //         return depth - 10;
   //         else
   //         return 0;
   //
   //   }
   //   private int[] minimax(int depth, Player player,Board board)
   //   {
   //      // Generate possible next moves in a List of int[2] of {row, col}.
   //      List<int[]> nextMoves = generateMoves(board,player);
   //
   //      // mySeed is maximizing; while oppSeed is minimizing
   //      int bestScore = (player == player.getColor()) ? Integer.MIN_VALUE : Integer.MAX_VALUE;
   //      int currentScore;
   //      int bestRow = -1;
   //      int bestCol = -1;
   //
   //      if (nextMoves.isEmpty() || depth == 0)
   //      {
   //         // Gameover or depth reached, evaluate score
   //         bestScore = evaluate(board,player);
   //         return score(board,depth,player);
   //      }
   //      else
   //      {
   //         for (int[] move : nextMoves)
   //         {
   //            // Try this move for the current "player"
   //            // cells[move[0]][move[1]].content = player;
   //            board.setBoard(player,move[0],move[1] );
   //            if (player == player.getColor())
   //            {  // mySeed (computer) is maximizing player
   //               currentScore = minimax(depth-1, player.getOtherColor(), board)[0];
   //               if (currentScore > bestScore)
   //               {
   //                  bestScore = currentScore;
   //                  bestRow = move[0];
   //                  bestCol = move[1];
   //               }
   //            }
   //            else
   //            {  // oppSeed is minimizing player
   //               currentScore = minimax(depth-1, player.getColor(),board)[0];
   //               if (currentScore < bestScore)
   //               {
   //                  bestScore = currentScore;
   //                  bestRow = move[0];
   //                  bestCol = move[1];
   //               }
   //            }
   //            // Undo move
   //            //cells[move[0]][move[1]].content = player.EMPTY;
   //            board.setBoard(player.EMPTY,move[0],move[1] );
   //         }
   //      }
   //      return new int[] {bestScore, bestRow, bestCol};
   //   }

   private int evaluate(Board board, Player player) {
      int score = 0;
      // Evaluate score for each of the 8 lines (3 rows, 3 columns, 2 diagonals)
      score += evaluateLine(player,board,0, 0, 0, 1, 0, 2);  // row 0
      score += evaluateLine(player,board,1, 0, 1, 1, 1, 2);  // row 1
      score += evaluateLine(player,board,2, 0, 2, 1, 2, 2);  // row 2
      score += evaluateLine(player,board,0, 0, 1, 0, 2, 0);  // col 0
      score += evaluateLine(player,board,0, 1, 1, 1, 2, 1);  // col 1
      score += evaluateLine(player,board,0, 2, 1, 2, 2, 2);  // col 2
      score += evaluateLine(player,board,0, 0, 1, 1, 2, 2);  // diagonal
      score += evaluateLine(player,board,0, 2, 1, 1, 2, 0);  // alternate diagonal

      return score;
   }
   private List<int[]> generateMoves(Board board, Player player)
   {
      List<int[]> nextMoves = new ArrayList<>(); // allocate List


      if (board.checkIfWinner(player))
      {
         return nextMoves;

      }

      for (int row = 0; row < 3; ++row)
      {
         for (int col = 0; col < 3; ++col)
         {
            if ( board.getValueAtSquare(row,col)== Player.EMPTY)
            {
               nextMoves.add(new int[] {row, col});
            }
         }
      }
      return nextMoves;
   }
   private int[] minimax(int depth, Player player,Board board)
   {
      // Generate possible next moves in a List of int[2] of {row, col}.
      List<int[]> nextMoves = generateMoves(board,player);

      // mySeed is maximizing; while oppSeed is minimizing
      int bestScore = (player == player.getColor()) ? Integer.MIN_VALUE : Integer.MAX_VALUE;
      int currentScore;
      int bestRow = -1;
      int bestCol = -1;

      if (nextMoves.isEmpty() || depth == 0)
      {
         // Gameover or depth reached, evaluate score
         bestScore = evaluate(board,player);
      }
      else
      {
         for (int[] move : nextMoves)
         {
            // Try this move for the current "player"
            // cells[move[0]][move[1]].content = player;
            board.setBoard(player,move[0],move[1] );
            if (player == player.getColor())
            {  // mySeed (computer) is maximizing player
               currentScore = minimax(depth-1, player.getOtherColor(), board)[0];
               if (currentScore > bestScore)
               {
                  bestScore = currentScore;
                  bestRow = move[0];
                  bestCol = move[1];
               }
            }
            else
            {  // oppSeed is minimizing player
               currentScore = minimax(depth-1, player.getColor(),board)[0];
               if (currentScore < bestScore)
               {
                  bestScore = currentScore;
                  bestRow = move[0];
                  bestCol = move[1];
               }
            }
            // Undo move
            //cells[move[0]][move[1]].content = player.EMPTY;
            board.setBoard(player.EMPTY,move[0],move[1] );
         }
      }
      return new int[] {bestScore, bestRow, bestCol};
   }


   int[] move(Player player,Board board)
   {
      int[] result = minimax(2,player,board);
      return new int[] {result[1], result[2]};

   }

   public void smartComputerMove(Player player,Board board,int level)
   {
      Matrix bm = new Matrix();
      for (int x = 0; x < 3; ++x) {
         for (int y = 0; y < 3; ++y) {
            Player p = board.getValueAtSquare(x, y);
            bm.append(p == Player.X ? 1 : (p == Player.O ? -1 : 0));
         }
      }

      double[] all = {
        NN0.feedforward(bm).getd(0),
        NN1.feedforward(bm).getd(0),
        NN2.feedforward(bm).getd(0),
        NN3.feedforward(bm).getd(0),
        NN4.feedforward(bm).getd(0),
        NN5.feedforward(bm).getd(0),
        NN6.feedforward(bm).getd(0),
        NN7.feedforward(bm).getd(0),
        NN8.feedforward(bm).getd(0)
      };

      int h = 0;
      for (int i = 1; i < 9; ++i) {
         if (all[i] > all[h]) {
            h = i;
         }
      }

      if (board.getValueAtSquare(h%3, h/3) == Player.EMPTY) {
         board.setBoard(player, h%3, h/3);
      } else {
         for (int i = 0; i < 9; ++i) {
            if (board.getValueAtSquare(i%3, i/3) == Player.EMPTY) {
               board.setBoard(player, i%3, i/3);
               System.out.println("AI failed. [used next available move]");
               break;
            }
         }
      }

      /*int[] temp =move(player,board);
      int xNew =temp[0];
      int yNew =temp[1];

      board.setBoard(player,xNew,yNew);*/
      System.out.println(level+"----------------------");
      /*Matrix bm = new Matrix();
      for (int x = 0; x < 3; ++x) {
         for (int y = 0; y < 3; ++y) {
            Player p = board.getValueAtSquare(x,  y);
            bm.append(p == Player.X ? 1 : (p == Player.O ? -1 : 0));
         }
      }
      try {
         File f = new File("data.txt");
         if (!f.exists()) {
            f.createNewFile();
         }
         FileWriter fw = new FileWriter(f, true);
         BufferedWriter bw = new BufferedWriter(fw);
         bw.write("new "+bm.print()+",\n");
         bw.close();
         fw.close();
      } catch (Exception e) {

      }
      for (int n = 0; n < 9; ++n) {
         if ((xNew*3)+yNew == n)
         {
            try
            {

               File f = new File("data" + n + ".txt");
               if (!f.exists())
               {
                  f.createNewFile();
               }
               FileWriter fw = new FileWriter(f, true);
               BufferedWriter bw = new BufferedWriter(fw);
               bw.write((i++)+", ");
               bw.close();
               fw.close();
            }
            catch (Exception e)
            {

            }
         }
      }*/

   }
   public boolean makeMove(Player player, Board board, int level)
   {
      if(computerTurn)
      {
         smartComputerMove(player,board,level);
         if(board.checkIfWinner(player))
         {
            return true;

         }

      }

      return false;
   }

   public void setComputerTurn(boolean setTurn)
   {
      computerTurn= setTurn;
   }


}