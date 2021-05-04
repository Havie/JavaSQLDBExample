package cs358.tournament.model;

public class Game {

  static int mGameIdCounter = 1111;
  private Team mHomeTeam;
  private Team mAwayTeam;
  private int mHomeScore;
  private int mAwayScore;
  private String mGameId;
  private GameOrigin mHomeOrigin;
  private GameOrigin mAwayOrigin;

  public Game(Team awayTeam, Team homeTeam) {
    this.mHomeTeam = homeTeam;
    this.mAwayTeam = awayTeam;

    this.mGameId = String.valueOf(++mGameIdCounter);

  }

  public Team getWinner() {

    if (mHomeScore > mAwayScore) {
      mHomeOrigin.storeWinner(mGameId, mHomeTeam);
      return mHomeTeam;
    } else if (mAwayScore > mHomeScore) {
      mAwayOrigin.storeWinner(mGameId, mAwayTeam);
      return mAwayTeam;
    } else
      return null;

  }

  public String toString() {

    String theMatch = "Home Team: " + mHomeTeam.getmTeamName() + " Away Team: " + mAwayTeam.getmTeamName();
    return theMatch;

  }

}
