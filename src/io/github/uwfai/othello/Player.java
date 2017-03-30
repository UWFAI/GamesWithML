package io.github.uwfai.othello;

public enum Player
{
	Black, White,Empty;
	
	public char getPlayer()
	{
		char letter=' ';
		switch(this)
		{
			case White:letter='W';
			break;
			case Black:letter='B';
			break;
			case Empty:letter='E';
			break;
			default:
		}
		
		return letter;
	}
	public char getOtherPlayer()
	{
		char letter=' ';
		switch(this)
		{
		case White:letter='B';
		break;
		case Black:letter='W';
		break;
		default:
		}
		
		return letter;
	}
}
