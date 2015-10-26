package game;

import java.awt.Point;

public class GameModel {
	
	public GameModel(){
		newGame();
	}

	private boolean game = false;
	
	public boolean isGame(){
		return game;
	}
	
	private int turn = 1;
	
	public boolean isMakeTurn(int row, int column) {
		return isGame() && (gameField[row][column] == -1);
	}
	
	public int makeTurn(int row, int column)
	{
		if (turn == 1) {
			setFieldX(row, column);
			turn = 0;
			return 1;
		}
		else {
			setFieldO(row, column);
			turn = 1;
			return 0;
		}
	}
	
	private int[][] gameField = new int[3][3];
	
	public int[][] getGameField() {
		return gameField;
	}
	
	public int getFieldValue(int row, int column) {
		return gameField[row][column];
	}
	
	private void setFieldX(int row, int column){
		gameField[row][column] = 1;
	}
	
	private void setFieldO(int row, int column){
		gameField[row][column] = 0;
	}
	
	private Point[] combo = null;
	
	public int checkForWinner() {
		int c = 0;
		for (int i = 0; i < 3; i++) {
			boolean f1 = gameField[i][0] != -1, 
					f2 = gameField[0][i] != -1;
			for (int j = 0; j < 3; j++) {
				f1 = f1 && (gameField[i][0] == gameField[i][j]);
				f2 = f2 && (gameField[0][i] == gameField[j][i]);
				c += (gameField[i][j] != -1)? 1 : 0;
			}
			if (f1) {
				game = false;
				combo = new Point[3];
				for (int j = 0; j < 3; j++) combo[j] = new Point(i, j);
				return gameField[i][0]; 
			}
			if (f2) {
				game = false;
				combo = new Point[3];
				for (int j = 0; j < 3; j++) combo[j] = new Point(j, i);
				return gameField[0][i];
			}
		}
		if (gameField[0][0] == gameField[1][1] && gameField[1][1] == gameField[2][2] && gameField[0][0] != -1) { 
			game = false;
			combo = new Point[3];
			for (int i = 0; i < 3; i++) combo[i] = new Point(i, i);
			return gameField[0][0];
		}
		if (gameField[0][2] == gameField[1][1] && gameField[1][1] == gameField[2][0] && gameField[0][2] != -1) { 
			game = false;
			combo = new Point[3];
			for (int i = 0; i < 3; i++) combo[i] = new Point(i, 2 - i);
			return gameField[0][2];
		}
		if (c == 9) return 2;
		return -1;
	}
	
	public Point[] getCombo(){
		return combo;
	}
	
	public void newGame() {
		for (int i = 0; i < 3; i++)
			for (int j = 0; j < 3; j++)
				gameField[i][j] = -1;
		game = true;
		combo = null;
		turn = 1;
	}
}
