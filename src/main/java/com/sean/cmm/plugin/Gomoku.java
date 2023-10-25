package com.sean.cmm.plugin;

import java.util.*;

public class Gomoku {
    public static final int BOARD_SIZE = 15;

    public enum State {
        EMPTY, BLACK, WHITE
    }

    public static class Board {
        private final State[][] grid;

        public Board() {
            grid = new State[BOARD_SIZE][BOARD_SIZE];
            for (State[] row : grid) {
                Arrays.fill(row, State.EMPTY);
            }
        }

        public void placeStone(int x, int y, State player) {
            grid[x][y] = player;
        }

        public boolean checkWin(int x, int y, State player) {
            int[][] directions = {{1, 0}, {0, 1}, {1, 1}, {1, -1}};
            for (int[] direction : directions) {
                int count = 1;
                for (int i = 1; i < 5; i++) {
                    int newX = x + i * direction[0];
                    int newY = y + i * direction[1];
                    if (newX >= 0 && newX < BOARD_SIZE && newY >= 0 && newY < BOARD_SIZE && grid[newX][newY] == player) {
                        count++;
                    } else {
                        break;
                    }
                }
                for (int i = 1; i < 5; i++) {
                    int newX = x - i * direction[0];
                    int newY = y - i * direction[1];
                    if (newX >= 0 && newX < BOARD_SIZE && newY >= 0 && newY < BOARD_SIZE && grid[newX][newY] == player) {
                        count++;
                    } else {
                        break;
                    }
                }
                if (count >= 5) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            for (State[] row : grid) {
                for (State cell : row) {
                    switch (cell) {
                        case EMPTY:
                            stringBuilder.append(".");
                            break;
                        case BLACK:
                            stringBuilder.append("B");
                            break;
                        case WHITE:
                            stringBuilder.append("W");
                            break;
                    }
                    stringBuilder.append(" ");
                }
                stringBuilder.append("\n");
            }
            return stringBuilder.toString();
        }
    }

    public static void main(String[] args) {
        Board board = new Board();
        State currentPlayer = State.BLACK;
        Scanner scanner = new Scanner(System.in);

        boolean gameOver = false;
        while (!gameOver) {
            System.out.println(board);
            System.out.println(currentPlayer + " to move. Please enter coordinates (e.g., 7 7): ");
            int x = scanner.nextInt();
            int y = scanner.nextInt();
            if (x >= 0 && x < BOARD_SIZE && y >= 0 && y < BOARD_SIZE && board.grid[x][y] == State.EMPTY) {
                board.placeStone(x, y, currentPlayer);
                if (board.checkWin(x, y, currentPlayer)) {
                    System.out.println(board);
                    System.out.println(currentPlayer + " wins!");
                    gameOver = true;
                } else {
                    currentPlayer = (currentPlayer == State.BLACK) ? State.WHITE : State.BLACK;
                }
            } else {
                System.out.println("Invalid move. Please try again.");
            }
        }
    }
}
