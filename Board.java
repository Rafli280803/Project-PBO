import java.util.Random;

public class Board {
    private static final int BOARD_SIZE = 4;
    private final Tile[][] board;
    private int score;
    private final Random random;

    public Board() {
        this.random = new Random();
        this.board = new Tile[BOARD_SIZE][BOARD_SIZE];
        this.score = 0;
        initializeBoard();
        generateRandomTile();
    }

    public Tile[][] getBoard() {
        return board;
    }

    public int getScore() {
        return score;
    }

    public void initializeBoard() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                board[i][j] = new Tile(0);
            }
        }
    }

    public void generateRandomTile() {
        int value = random.nextInt(10) == 0 ? 4 : 2; // 10% chance of 4
        int x, y;
        do {
            x = random.nextInt(BOARD_SIZE);
            y = random.nextInt(BOARD_SIZE);
        } while (!board[x][y].isEmpty());
        board[x][y].setValue(value);
    }

    public boolean moveUp() {
        boolean moved = false;
        for (int i = 0; i < BOARD_SIZE; i++) {
            Tile[] line = new Tile[BOARD_SIZE];
            for (int j = 0; j < BOARD_SIZE; j++) {
                line[j] = board[i][j]; // Copy the row
            }
            Tile[] newLine = shiftAndMerge(line);
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (!board[i][j].equals(newLine[j])) {
                    moved = true; // Check if the board changed
                }
                board[i][j] = newLine[j]; // Update the board
            }
        }
        if (moved) generateRandomTile();
        return moved;
    }
    
    public boolean moveDown() {
        boolean moved = false;
        for (int i = 0; i < BOARD_SIZE; i++) {
            Tile[] line = new Tile[BOARD_SIZE];
            for (int j = 0; j < BOARD_SIZE; j++) {
                line[j] = board[i][BOARD_SIZE - 1 - j]; // Reverse row
            }
            Tile[] newLine = shiftAndMerge(line);
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (!board[i][BOARD_SIZE - 1 - j].equals(newLine[j])) {
                    moved = true;
                }
                board[i][BOARD_SIZE - 1 - j] = newLine[j]; // Reverse back
            }
        }
        if (moved) generateRandomTile();
        return moved;
    }

    public boolean moveLeft() {
        boolean moved = false;
        for (int j = 0; j < BOARD_SIZE; j++) {
            Tile[] line = new Tile[BOARD_SIZE];
            for (int i = 0; i < BOARD_SIZE; i++) {
                line[i] = board[i][j]; // Extract column
            }
            Tile[] newLine = shiftAndMerge(line);
            for (int i = 0; i < BOARD_SIZE; i++) {
                if (!board[i][j].equals(newLine[i])) {
                    moved = true;
                }
                board[i][j] = newLine[i]; // Update column
            }
        }
        if (moved) generateRandomTile();
        return moved;
    }
    
    public boolean moveRight() {
        boolean moved = false;
        for (int j = 0; j < BOARD_SIZE; j++) {
            Tile[] line = new Tile[BOARD_SIZE];
            for (int i = 0; i < BOARD_SIZE; i++) {
                line[i] = board[BOARD_SIZE - 1 - i][j]; // Reverse column
            }
            Tile[] newLine = shiftAndMerge(line);
            for (int i = 0; i < BOARD_SIZE; i++) {
                if (!board[BOARD_SIZE - 1 - i][j].equals(newLine[i])) {
                    moved = true;
                }
                board[BOARD_SIZE - 1 - i][j] = newLine[i]; // Reverse back
            }
        }
        if (moved) generateRandomTile();
        return moved;
    }
    

    private Tile[] shiftAndMerge(Tile[] line) {
        Tile[] result = new Tile[BOARD_SIZE];
        int insertPos = 0;
        for (int i = 0; i < BOARD_SIZE; i++) {
            if (!line[i].isEmpty()) {
                if (insertPos > 0 && result[insertPos - 1].equals(line[i])) {
                    result[insertPos - 1].merge(line[i]);
                    score += result[insertPos - 1].getValue();
                } else {
                    result[insertPos] = line[i];
                    insertPos++;
                }
            }
        }
        for (int i = insertPos; i < BOARD_SIZE; i++) {
            result[i] = new Tile(0); // Fill the remaining empty slots
        }
        return result;
    }

    public boolean isGameOver() {
        if (!has2048Tile()) {
            return isBoardFull() && !canMove();
        }
        return false;
    }

    public boolean hasWon() {
        return has2048Tile();
    }

    private boolean has2048Tile() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (board[i][j].getValue() == 2048) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isBoardFull() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (board[i][j].isEmpty()) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean canMove() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (i < BOARD_SIZE - 1 && board[i][j].equals(board[i + 1][j])) {
                    return true;
                }
                if (j < BOARD_SIZE - 1 && board[i][j].equals(board[i][j + 1])) {
                    return true;
                }
            }
        }
        return false;
    }
}
