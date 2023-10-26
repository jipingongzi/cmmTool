package com.sean.cmm.plugin.gamelife;

public class CellLifePredictorImpl implements CellLifePredictor {
    @Override
    public Boolean whenFewerThanTwoLiveNeighbors() {
        return false;
    }

    @Override
    public Boolean whenEqualTwoOrThreeLiveNeighbors() {
        return true;
    }

    @Override
    public Boolean whenMoreThanThreeLiveNeighbors() {
        return false;
    }

    @Override
    public Boolean whenEqualThree() {
        return true;
    }
}
