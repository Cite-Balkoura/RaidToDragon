package fr.grimtown.RaidToDragon.entities;

public class GameTeam {

    private final GamePlayer[] players;
    private long lastKnowPosition;

    public GameTeam(GamePlayer[] players) {
        this.players = players;
        this.lastKnowPosition = -1L;
    }

    public GamePlayer[] getPlayers() {
        return this.players;
    }

    public long getLastKnowPosition() {
        return this.lastKnowPosition;
    }

    public void setLastKnowPosition(long lastKnowPosition) {
        this.lastKnowPosition = lastKnowPosition;
    }
}
