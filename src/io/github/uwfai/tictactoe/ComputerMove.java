package io.github.uwfai.tictactoe;
import io.github.uwfai.neural.Matrix;
import io.github.uwfai.neural.NeuralNetwork;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ComputerMove
{

   double eta = 1.0;
   double lambda = 0.001;
   NeuralNetwork.ActivationType at = NeuralNetwork.ActivationType.SIGMOID;
   NeuralNetwork.CostType ct = NeuralNetwork.CostType.CROSSENTROPY;
   NeuralNetwork.InitializeType it = NeuralNetwork.InitializeType.SMART;
   private boolean computerTurn=true;
   private NeuralNetwork NN0 = new NeuralNetwork()
         .Input(9)
         .Feedforward(3)
         .Output(1)
         .Build(eta, lambda, ct, at, it);
   private NeuralNetwork NN1 = new NeuralNetwork()
         .Input(9)
         .Feedforward(3)
         .Output(1)
         .Build(eta, lambda, ct, at, it);
   private NeuralNetwork NN2 = new NeuralNetwork()
         .Input(9)
         .Feedforward(3)
         .Output(1)
         .Build(eta, lambda, ct, at, it);
   private NeuralNetwork NN3 = new NeuralNetwork()
         .Input(9)
         .Feedforward(3)
         .Output(1)
         .Build(eta, lambda, ct, at, it);
   private NeuralNetwork NN4 = new NeuralNetwork()
         .Input(9)
         .Feedforward(3)
         .Output(1)
         .Build(eta, lambda, ct, at, it);
   private NeuralNetwork NN5 = new NeuralNetwork()
         .Input(9)
         .Feedforward(3)
         .Output(1)
         .Build(eta, lambda, ct, at, it);
   private NeuralNetwork NN6 = new NeuralNetwork()
         .Input(9)
         .Feedforward(3)
         .Output(1)
         .Build(eta, lambda, ct, at, it);
   private NeuralNetwork NN7 = new NeuralNetwork()
         .Input(9)
         .Feedforward(3)
         .Output(1)
         .Build(eta, lambda, ct, at, it);
   private NeuralNetwork NN8 = new NeuralNetwork()
         .Input(9)
         .Feedforward(3)
         .Output(1)
         .Build(eta, lambda, ct, at, it);

   public ComputerMove()
   {
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

  public void smartComputerMove(Player player,Board board)
   {
      int[] temp =move(player,board);
      int xNew =temp[0];
      int yNew =temp[1];
      board.setBoard(player,xNew,yNew);

      Matrix bm = new Matrix();
      for (int x = 0; x < 2; ++x) {
         for (int y = 0; y < 2; ++y) {
            Player p = board.getValueAtSquare(x, y);
            bm.append(p == Player.X ? 1 : (p == Player.O ? -1 : 0));
         }
      }
      for (int n = 0; n < 8; ++n) {
         File f = new File("data.txt");
         try
         {
            PrintWriter pf = new PrintWriter(f);
            pf.append(n+"["+bm.print()+","+((xNew*3)+yNew == n ? new Matrix(1) : new Matrix(0)).print()+"]\n");
         }
         catch (Exception x)
         {

         }
      }


      /*
      Matrix answer = NN0.feedforward(board);
      Matrix answer = NN0.feedword(board);
      Matrix answer = NN0.feedword(board);
      Matrix answer = NN0.feedword(board);
      Matrix answer = NN0.feedword(board);
      Matrix answer = NN0.feedword(board);
      Matrix answer = NN0.feedword(board);
      Matrix answer = NN0.feedword(board);
      Matrix answer = NN0.feedword(board);
      max(0, ..., 8);
       */

   }
   public boolean makeMove(Player player, Board board)
   {
      if(computerTurn)
      {
         smartComputerMove(player,board);
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
