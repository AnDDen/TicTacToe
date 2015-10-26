package application;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import java.awt.Point;
import java.io.File;
import game.GameModel;
import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.scene.layout.GridPane;


public class Controller {	
	
	@FXML Label lbl;
	
	@FXML Pane gameField;
	
	GameModel game = new GameModel();
	
	StackPane[][] tiles = new StackPane[3][3];

	@FXML GridPane gridPane;
	
	final double tileSize = 100.0;
	final double lineWidth = 2.5;

	public void createField(){
		lbl.setText("Ход крестиков");

		gridPane.getChildren().clear();
		
		for (int i = 0; i < 3; i++)
			for (int j = 0; j < 3; j++) {
				StackPane tile = new StackPane();
				tile.prefHeight(tileSize);
				tile.prefWidth(tileSize);
				Canvas canvas = new Canvas();
				canvas.setHeight(tileSize);
				canvas.setWidth(tileSize);
				GraphicsContext gc = canvas.getGraphicsContext2D();
				gc.setStroke(Color.BLACK);
				gc.setLineWidth(lineWidth);
				if (i != 2) {
					gc.moveTo(tileSize - lineWidth, 0);
					gc.lineTo(tileSize - lineWidth, tileSize);
				}
				if (j != 2) {
					gc.moveTo(0, tileSize - lineWidth);
					gc.lineTo(tileSize, tileSize - lineWidth);
				}
				gc.stroke();
				tile.getChildren().add(canvas);
				tile.setOnMouseClicked(this::tileClick);
				
				tiles[i][j] = tile;
				gridPane.add(tiles[i][j], i, j);
			}
	}
	
	
	@FXML public void tileClick(MouseEvent event) {
		StackPane tile = (StackPane)event.getSource();
		
		int row = -1, column = -1;
		for (int i = 0; i < 3; i++)
			for (int j = 0; j < 3; j++) 
				if (tiles[i][j] == tile) {
					row = i;
					column = j;
				}
		
		
		if (game.isMakeTurn(row, column)) {	//если можно сделать ход
			int turn = game.makeTurn(row, column); //сделать ход
			
			Canvas c = (Canvas)tile.getChildren().get(0);
			
			if (turn == 1) { //если ход сделали крестики
				drawX(c);
				playSoundX();
				lbl.setText("Ход ноликов");
			}
			else if (turn == 0) { //если ход сделали нулики
				drawO(c);
				playSoundO();
				lbl.setText("Ход крестиков");
			}
			int f = game.checkForWinner(); //проверка на наличие победителя
			if (f != -1) {
				if (f == 2) 
					lbl.setText("Игра окончена. Ничья"); 
				else {
					lbl.setText("Игра окончена. Победили " + (f == 1 ? "крестики" : "нолики"));
					Point[] combo = game.getCombo();
					
					int x0 = combo[0].x, y0 = combo[0].y;
					boolean t1 = (combo[1].x == x0),
							t2 = (combo[1].y == y0),
							t3 = (combo[1].x - x0 == combo[1].y - y0) ;
					if (t1) { //все в одном столбце
						for (int i = 0; i < 3; i++) {
							Canvas canvas = new Canvas();
							canvas.setHeight(tileSize);
							canvas.setWidth(tileSize);
							
							GraphicsContext gc = canvas.getGraphicsContext2D();
							gc.setLineWidth(lineWidth * 2);
							gc.setStroke(Color.RED);
							gc.moveTo(tileSize / 2, 0);
							gc.lineTo(tileSize / 2, tileSize);
							gc.stroke();
							
							tiles[combo[i].x][combo[i].y].getChildren().add(canvas);
						}
					} else
					if (t2) { //все в одном ряду
						for (int i = 0; i < 3; i++) {
							Canvas canvas = new Canvas();
							canvas.setHeight(tileSize);
							canvas.setWidth(tileSize);
							
							GraphicsContext gc = canvas.getGraphicsContext2D();
							gc.setLineWidth(lineWidth * 2);
							gc.setStroke(Color.RED);
							gc.moveTo(0, tileSize / 2);
							gc.lineTo(tileSize, tileSize / 2);
							gc.stroke();
							
							tiles[combo[i].x][combo[i].y].getChildren().add(canvas);
						}
					} else
					if (t3) {
						for (int i = 0; i < 3; i++) {
							Canvas canvas = new Canvas();
							canvas.setHeight(tileSize);
							canvas.setWidth(tileSize);
							
							GraphicsContext gc = canvas.getGraphicsContext2D();
							gc.setLineWidth(lineWidth * 2);
							gc.setStroke(Color.RED);
							gc.moveTo(0, 0);
							gc.lineTo(tileSize, tileSize);
							gc.stroke();
							
							tiles[combo[i].x][combo[i].y].getChildren().add(canvas);
						}
					} else {
						for (int i = 0; i < 3; i++) {
							Canvas canvas = new Canvas();
							canvas.setHeight(tileSize);
							canvas.setWidth(tileSize);
							
							GraphicsContext gc = canvas.getGraphicsContext2D();
							gc.setLineWidth(lineWidth * 2);
							gc.setStroke(Color.RED);
							gc.moveTo(tileSize, 0);
							gc.lineTo(0, tileSize);
							gc.stroke();
							
							tiles[combo[i].x][combo[i].y].getChildren().add(canvas);
						}
					}
				}
			}			
		}
	}
	
	private double x, y, stepX, stepY;
	
	private void drawX(Canvas c){
		
		GraphicsContext gc = c.getGraphicsContext2D();
        double h = c.getHeight();
        double w = c.getWidth();
        gc.setStroke(Color.BLACK);

        x = w / 5; y = h / 5;
 	    stepX = (3 * w / 5) / 10;
	    stepY = (3 * h / 5) / 10;
        
	    AnimationTimer timer2 = new AnimationTimer() {
            @Override
            public void handle(long now) {
            	gc.moveTo(x, y);
            	x -= stepX; y += stepY;
            	gc.lineTo(x, y);
            	gc.stroke();
            	if (x <= w / 5 && y >= 4 * h / 5){           		
            		stop();
            		
            	}            		
            }            
        };
	    
        AnimationTimer timer1 = new AnimationTimer() {
            @Override
            public void handle(long now) {
            	gc.moveTo(x, y);
            	x += stepX; y += stepY;
            	gc.lineTo(x, y);
            	gc.stroke();
            	if (x >= 4 * w / 5 && y >= 4 * h / 5){
            		stop();

            		x = 4 * w / 5; y = h / 5;
                    
                    timer2.start();
            	}            		
            }            
        };
        
        timer1.start();
        
	}
	
	double angle, delta;
	
	private void drawO(Canvas c){
		
		GraphicsContext gc = c.getGraphicsContext2D();
        double h = c.getHeight();
        double w = c.getWidth();
        gc.setStroke(Color.BLACK);
        
        angle = 0.0; delta = 18.0;
        
        AnimationTimer timer1 = new AnimationTimer() {
            @Override
            public void handle(long now) {
            	gc.strokeArc(w / 5, h / 5, 3 * w / 5, 3 * h / 5, angle, delta, ArcType.OPEN);
            	angle += delta;
            	gc.stroke();
            	if (angle >= 360.0){
            		stop();
            		
            	}            		
            }    
        };
        
        timer1.start();
	}
	
	private void playSound(String fileName) {
        Media sound = new Media(new File(fileName).toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.setVolume(0.5);
        mediaPlayer.play();
    }
	
	private void playSoundX() {
		playSound("sounds/Pencil1.wav");
	}
	
	private void playSoundO() {
		playSound("sounds/Pencil2.wav");
    }

	@FXML public void newGameBtnClick(ActionEvent event) {
		game.newGame();
		createField();
	}
	
}
