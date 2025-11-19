package net.minheur.potoflux.tictactoe;

public class SmallBoard {
    private Cell[][] grid = new Cell[3][3];
    private boolean finished = false;
    private Cell winner = Cell.EMPTY;

    public boolean play(int x, int y, Cell player) {
        if (grid[x][y] != Cell.EMPTY || finished) return false;
        grid[x][y] = player;
        checkWinner();
        return true;
    }

    private void checkWinner() {

    }

    public boolean isFull() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (grid[i][j] == Cell.EMPTY) return false;
            }
        }
        return true;
    }
}
