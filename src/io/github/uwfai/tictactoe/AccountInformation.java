package io.github.uwfai.tictactoe;

public class AccountInformation
{

   private int numberOfWin=0;

   private int numberOfWinC=0;
   private  int numberOfPlay=1;
   private int tiedWithComputer=0;
   private int tiedWithHuman =0;

   private int computerWin=0;

   private int guestWin=0;

   public AccountInformation()
   {
   }
   public void setTiedWithComputer(int tie)
   {
      tiedWithComputer =tie;
   }
   public void setTiedWithHuman(int tie)
   {
      tiedWithHuman =tie;
   }
   public void setNumberOfWin(int win)
   {
      numberOfWin=win;
   }
   public void setNumberOfWinC(int win)
   {
      numberOfWinC=win;
   }
   public void setComputerWin(int win)
   {
      computerWin=win;
   }
   public void setguestWin(int win)
   {
      guestWin=win;
   }

   public int getNumberOfWin()
   {
      return numberOfWin;
   }
   public int getNumberOfWinC()
   {
      return numberOfWinC;
   }
   public int getComputerWin()
   {
      return computerWin;
   }
   public int getTiedWithComputer()
   {
      return tiedWithComputer;
   }
   public int getTiedWithHuman()
   {
      return tiedWithHuman;
   }
   public int getGuestWin()
   {
      return guestWin;
   }
   public int getNumberOfPlay()
   {
      return numberOfPlay;
   }
   public void setNumberOfPlay(int play)
   {
      numberOfPlay=play;
   }
   public String toString(int whatToPrint)
   {
      String info="";
      ///computer
      if(whatToPrint==1)
      {
         info= "Play #: "+getNumberOfPlay()+ " |Won: "+ getNumberOfWinC()+" |"+ "AI won: "+getComputerWin()+" |Tied: "+getTiedWithComputer();
      }
      //quest
      else
      {
         info= "Play #: "+getNumberOfPlay()+ " |Won: "+ getNumberOfWin()+" |"+ "Guest won: "+getGuestWin()+" |Tied: "+getTiedWithHuman();
      }

      return info;
   }

}
