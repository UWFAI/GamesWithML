package io.github.uwfai.tictactoe;

public class ValueSetter
{
   private int col;
   private int row;

   public int getCol()
   {
      return col;
   }
   public int getRow()
   {
      return row;
   }

   public boolean checkIfGood(String move)
   {
      if(checkValue(move)==-1)
      {
         return true;
      }

      if((int)(move.charAt(0))> 57|| (int)(move.charAt(0))<48)
      {

         return true;
      }
         return false;
   }

   public int checkValue(String value)
   {

      if(value.length()>1)
         return -1;
      else
         return 0;
   }

   public boolean settingActualValue(String value)
   {
      if(checkIfGood(value))
      {
         return true;
      }
      else
      {
         setValue(Integer.parseInt(value.charAt(0)+""));
         return false;
      }
   }


   public void setValue(int value)
   {

      switch (value)
      {
      case 1:
      {
         col = 0;
         row = 0;
         break;
      }
      case 2:
      {
         col = 1;
         row = 0;
         break;
      }
      case 3:
      {
         col = 2;
         row = 0;
         break;
      }
      case 4:
      {
         col = 0;
         row = 1;
         break;
      }
      case 5:
      {
         col = 1;
         row = 1;
         break;
      }
      case 6:
      {
         col = 2;
         row = 1;
         break;
      }
      case 7:
      {
         col = 0;
         row = 2;
         break;
      }
      case 8:
      {
         col = 1;
         row = 2;
         break;
      }
      case 9:
      {
         row=2;
         col=2;
         break;
      }

      }


   }
}
