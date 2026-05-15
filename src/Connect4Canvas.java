import javax.microedition.lcdui.*;

public class Connect4Canvas extends Canvas implements Runnable {
	private Connect4 game;
	private Image[] images;
	
	private static final int
		WIDTH = 240,
		HEIGHT = 320;
	
	private static final int
		FIELD = 0,
		RED = 1,
		BLUE = 2,
		STAR = 3,
		RED_GHOST = 4,
		BLUE_GHOST = 5,
		IMAGE_COUNT = 6;

	private static final int
		offsetX = 0,
		offsetY = 20,
		cellSize = 40;
	
	private int selectedCol;
	private static final int ANIM_DELAY = 20;
	private boolean running = true;
	
	Connect4Canvas() {
		game = new Connect4();
		images = new Image[IMAGE_COUNT];
		selectedCol = -1;
		for (int i = 0; i < IMAGE_COUNT; i++) {
			try {
				images[i] = Image.createImage("/" + String.valueOf(i) + ".png");
			} catch (Exception e) {}
		}
		
		setFullScreenMode(true);
		new Thread(this).start();
	}
	
	private int getCellX(int row) {
		return (Connect4.HEIGHT - row - 1) * cellSize;
	}
	
	private int getCellY(int col) {
		return col * cellSize;
	}
	
	protected void paint(Graphics g) {
		g.setColor(0xffffff);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		
		for (int row = 0; row < Connect4.HEIGHT; row++) {
			for (int col = 0; col < Connect4.WIDTH; col++) {
				int x = getCellX(row);
				int y = getCellY(col);
				
				if (game.field[row][col] == Connect4.RED) {
					g.drawImage(images[RED], offsetX + x, offsetY + y, Graphics.TOP | Graphics.LEFT);
				} else if (game.field[row][col] == Connect4.BLUE) {
					g.drawImage(images[BLUE], offsetX + x, offsetY + y, Graphics.TOP | Graphics.LEFT);
				}
			}
		}
		
		if (game.state == Connect4.GAME_ANIM) {
			int player = game.currentPlayer == Connect4.RED ? RED : BLUE;
			int x = WIDTH - game.animProgress * cellSize / Connect4.ANIM_STEP;
			int y = getCellY(game.animCol);
			g.drawImage(images[player], offsetX + x, offsetY + y, Graphics.TOP | Graphics.RIGHT);
		} else {
			int player = game.currentPlayer == Connect4.RED ? RED_GHOST : BLUE_GHOST;
			int row = game.getEmptyRow(selectedCol);
			int x = getCellX(row);
			int y = getCellY(selectedCol);
			g.drawImage(images[player], offsetX + x, offsetY + y, Graphics.TOP | Graphics.LEFT);
		}
		
		g.drawImage(images[FIELD], 0, 0, Graphics.TOP | Graphics.LEFT);
		
		if (game.winRow != null) {
			for (int i = 0; i < Connect4.GOAL; i++) {
				int row = game.winRow[i][0];
				int col = game.winRow[i][1];
				
				int x = getCellX(row);
				int y = getCellY(col);
				
				g.drawImage(images[STAR], offsetX + x, offsetY + y, Graphics.TOP | Graphics.LEFT);
			}
		}
	}
	
	protected void pointerReleased(int x, int y) {
		if (game.state == Connect4.GAME_WIN_RED || 
				game.state == Connect4.GAME_WIN_BLUE || 
				game.state == Connect4.GAME_DRAW) {
			game.newGame();
			return;
		}
		
		int col = (y - offsetY) / cellSize;
		game.makeMove(col);
	}
	
	protected void keyPressed(int keyCode) {
		if (game.state == Connect4.GAME_WIN_RED || 
				game.state == Connect4.GAME_WIN_BLUE || 
				game.state == Connect4.GAME_DRAW) {
			game.newGame();
			return;
		}
		
		int gameAction = getGameAction(keyCode);
		if (gameAction == UP || keyCode == KEY_NUM2) {
			if (--selectedCol < -1) {
				selectedCol = Connect4.WIDTH - 1;
			}
		} else if (gameAction == DOWN || keyCode == KEY_NUM8) {
			if (++selectedCol >= Connect4.WIDTH) {
				selectedCol = -1;
			}
		} else if (gameAction == FIRE || keyCode == KEY_NUM5) {
			game.makeMove(selectedCol);
		}
	}

	public void run() {
		while (running) {
			game.updateAnim();
			repaint();
            try {
            	Thread.sleep(ANIM_DELAY);
            }
            catch (InterruptedException e) {}
		}
	}
}
