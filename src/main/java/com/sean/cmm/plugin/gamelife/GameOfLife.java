package com.sean.cmm.plugin.gamelife;

public class GameOfLife {
    private final int gridSize;
    private final CellLifePredictor cellLifePredictor;

    public GameOfLife(int gridSize) {
        this.gridSize = gridSize;
        this.cellLifePredictor = new CellLifePredictorImpl();
    }

    public void displayGrid(boolean[][] grid) {
        for (boolean[] row : grid) {
            for (boolean cell : row) {
                System.out.print(cell ? "1 " : "0 ");
            }
            System.out.println();
        }
    }

    public boolean[][] nextGeneration(boolean[][] grid) {
        boolean[][] nextGen = new boolean[gridSize][gridSize];

        for (int row = 0; row < gridSize; row++) {
            for (int col = 0; col < gridSize; col++) {
                int liveNeighbors = cellLifePredictor.countLiveNeighbors(grid, row, col);

                if(grid[row][col]){
                    if(liveNeighbors < 2){
                        nextGen[row][col] = cellLifePredictor.whenFewerThanTwoLiveNeighbors();
                    } else if(liveNeighbors == 2 || liveNeighbors == 3){
                        nextGen[row][col] = cellLifePredictor.whenEqualTwoOrThreeLiveNeighbors();
                    } else {
                        nextGen[row][col] = cellLifePredictor.whenMoreThanThreeLiveNeighbors();
                    }
                }else {
                    if(liveNeighbors == 3){
                        nextGen[row][col] = cellLifePredictor.whenEqualThree();
                    }
                }
            }
        }
        return nextGen;
    }

    public int countLiveNeighbors(boolean[][] grid, int row, int col) {
        return cellLifePredictor.countLiveNeighbors(grid, row, col);
    }
}