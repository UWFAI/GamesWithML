package io.github.uwfai.othello;

import javax.swing.JPanel;

public interface OthelloUI
{
	public void print(Board board, int[] numberOfMoves);
	public int[] makeMove(Player player);
	
	public void setErrorMessage(boolean b);
	public boolean isClick();
	public void setTopMessageTextWithPlayer(Player player);
	
	/**
	 * Sets the checks if is click.
	 *
	 * @param click the new checks if is click
	 */
	public void setIsClick(boolean click);
	public void errorSpaceMessage();
	public void errorOutMessage();
	public JPanel allPanel();
}
