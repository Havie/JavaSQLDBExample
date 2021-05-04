package cs358.tournament.model;

import java.util.HashMap;

public class Round {

  public HashMap<String, Game> mGames;

  public Game findGame(String gameId) {

    return mGames.get(gameId);

  }

  public String toString() {

    String theRound = "";

    for (String e : mGames.keySet()) {
      theRound += mGames.get(e).toString() + "/n";
    }

    return theRound;
  }

}
