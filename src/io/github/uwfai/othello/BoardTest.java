package io.github.uwfai.othello;

import static org.junit.Assert.*;

import org.junit.Test;

public class BoardTest {
	Board board = new Board();
	@Test
	public void test() {
		
		
		
		//fail("Not yet implemented");
	}
	public void testReset()
	{
		board.reset();
	}
	@Test
	public void testGetPlayerWithIndex()
	{
		//board.reset();
		System.out.println(board.getPlayerWithIndex(3, 4));
	}

}
