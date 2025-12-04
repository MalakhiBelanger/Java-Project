package com.example.esmanager.Pages.DataStuff;

public class Match {
    private boolean won;
    private int scoreA;
    private int scoreB;
    private String game;
    public Match(boolean won, int scoreA, int scoreB, String game) {
        this.won = won;
        this.scoreA = scoreA;
        this.scoreB = scoreB;
        this.game = game;
    }

    public boolean isWon() {
        return won;
    }

    public void setWon(boolean won) {
        this.won = won;
    }

    public int getScoreA() {
        return scoreA;
    }

    public void setScoreA(int scoreA) {
        this.scoreA = scoreA;
    }

    public int getScoreB() {
        return scoreB;
    }

    public void setScoreB(int scoreB) {
        this.scoreB = scoreB;
    }

    public String getGame() {
        return game;
    }

    public void setGame(String game) {
        this.game = game;
    }
}
