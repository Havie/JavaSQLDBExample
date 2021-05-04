package cs358.tournament.model;

public class GameOrigin {

  public String mGameId;
  public Team mWinningTeam;

  public void storeWinner(String gameId, Team winningTeam) {

    this.mGameId = gameId;
    this.mWinningTeam = winningTeam;
  }

}
