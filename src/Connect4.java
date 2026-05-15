public class Connect4 {
	public static final int
		EMPTY = 0,
		RED = 1,
		BLUE = 2;
	public static final int
		GAME_ACTIVE = 0,
		GAME_ANIM = 1,
		GAME_WIN_RED = 2,
		GAME_WIN_BLUE = 3,
		GAME_DRAW = 4;
	public static final int
		WIDTH = 7,
		HEIGHT = 6;
	public static final int GOAL = 4;
	public static final int
		ANIM_SPEED = 20,
		ANIM_STEP = 100;
	
	public int state;
	public int[][] winRow;
	public int currentPlayer;
	public int[][] field;
	
	public int animProgress;
	public int animRow;
	public int animCol;
	public int animStop;
	
	Connect4() {
		field = new int[HEIGHT][WIDTH];
		newGame();
	}
	
	public void newGame() {
		state = GAME_ACTIVE;
		currentPlayer = RED;
		winRow = null;
		
		for (int row = 0; row < HEIGHT; row++) {
			for (int col = 0; col < WIDTH; col++) {
				field[row][col] = EMPTY;
			}
		}
	}
	
	public boolean isMoveCorrect(int col) {
		return col >= 0 && col < WIDTH && field[0][col] == EMPTY;
	}
	
	public void makeMove(int col) {
		if (state != GAME_ACTIVE || !isMoveCorrect(col)) return;
		
		int row = getEmptyRow(col);
		startAnim(row, col);
	}
	
	public int getEmptyRow(int col) {
		if (!isMoveCorrect(col)) return -1;
		
		for (int row = HEIGHT - 1; row >= 0; row--) {
			if (field[row][col] == EMPTY) {
				return row;
			}
		}
		
		return -1;
	}
	
	private void startAnim(int row, int col) {
		state = GAME_ANIM;
		animRow = row;
		animCol = col;
		animProgress = -ANIM_STEP;
		animStop = row * ANIM_STEP;
	}
	
	public void updateAnim() {
		if (state != GAME_ANIM) return;
		
		animProgress += ANIM_SPEED;
		if (animProgress >= animStop) {
			endAnim();
		}
	}
	
	private void endAnim() {
		field[animRow][animCol] = currentPlayer;
		toNextPlayer();
		checkGameState();
	}
	
	private void checkGameState() {
		for (int row = 0; row < HEIGHT; row++) {
			for (int col = 0; col < WIDTH - GOAL + 1; col++) {
				int winner = field[row][col];
				boolean line = winner != EMPTY;
				for (int d = 0; d < GOAL; d++) {
					if (field[row][col + d] != winner) {
						line = false;
						break;
					}
				}
				if (line) {
					state = (winner == RED) ? GAME_WIN_RED : GAME_WIN_BLUE;
					winRow = new int[GOAL][2];
					for (int d = 0; d < GOAL; d++) {
						winRow[d] = new int[] {row, col + d};
					}
					return;
				}
			}
		}
		
		for (int row = 0; row < HEIGHT - GOAL + 1; row++) {
			for (int col = 0; col < WIDTH; col++) {
				int winner = field[row][col];
				boolean line = winner != EMPTY;
				for (int d = 0; d < GOAL; d++) {
					if (field[row + d][col] != winner) {
						line = false;
						break;
					}
				}
				if (line) {
					state = (winner == RED) ? GAME_WIN_RED : GAME_WIN_BLUE;
					winRow = new int[GOAL][2];
					for (int d = 0; d < GOAL; d++) {
						winRow[d] = new int[] {row + d, col};
					}
					return;
				}
			}
		}
		
		for (int row = 0; row < HEIGHT - GOAL + 1; row++) {
			for (int col = 0; col < WIDTH - GOAL + 1; col++) {
				int winner = field[row][col];
				boolean line = winner != EMPTY;
				for (int d = 0; d < GOAL; d++) {
					if (field[row + d][col + d] != winner) {
						line = false;
						break;
					}
				}
				if (line) {
					state = (winner == RED) ? GAME_WIN_RED : GAME_WIN_BLUE;
					winRow = new int[GOAL][2];
					for (int d = 0; d < GOAL; d++) {
						winRow[d] = new int[] {row + d, col + d};
					}
					return;
				}
				winner = field[row + GOAL - 1][col];
				line = winner != EMPTY;
				for (int d = 0; d < GOAL; d++) {
					if (field[row + GOAL - 1 - d][col + d] != winner) {
						line = false;
						break;
					}
				}
				if (line) {
					state = (winner == RED) ? GAME_WIN_RED : GAME_WIN_BLUE;
					winRow = new int[GOAL][2];
					for (int d = 0; d < GOAL; d++) {
						winRow[d] = new int[] {row + GOAL - 1 - d, col + d};
					}
					return;
				}
			}
		}
		
		for (int row = 0; row < HEIGHT; row++) {
			for (int col = 0; col < WIDTH - GOAL + 1; col++) {
				if (field[row][col] == EMPTY) {
					state = GAME_ACTIVE;
					return;
				}
			}
		}
		
		state = GAME_DRAW;
	}
	
	private void toNextPlayer() {
		currentPlayer = (currentPlayer == BLUE) ? RED : BLUE;
	}
}