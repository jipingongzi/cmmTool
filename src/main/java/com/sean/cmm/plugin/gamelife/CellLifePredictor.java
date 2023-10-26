package com.sean.cmm.plugin.gamelife;

public interface CellLifePredictor {

    Boolean whenFewerThanTwoLiveNeighbors();
    Boolean whenEqualTwoOrThreeLiveNeighbors();
    Boolean whenMoreThanThreeLiveNeighbors();
    Boolean whenEqualThree();

    default int countLiveNeighbors(boolean[][] grid, int row, int col){
        int liveNeighbors = 0;
        for (int xAxis = -1; xAxis <= 1; xAxis++) {
            for (int yAxis = -1; yAxis <= 1; yAxis++) {
                int newRow = row + xAxis;
                int newCol = col + yAxis;

                if (isNeighborAlive(grid, xAxis, yAxis, newRow, newCol)) {
                    liveNeighbors++;
                }
            }
        }
        return liveNeighbors;
    }

    default boolean isNeighborAlive(boolean[][] grid, int xAxis, int yAxis, int newRow, int newCol) {
        return newRow >= 0 && newRow < grid.length && newCol >= 0 && newCol < grid[0].length
                && !(xAxis == 0 && yAxis == 0) && grid[newRow][newCol];
    }

}
