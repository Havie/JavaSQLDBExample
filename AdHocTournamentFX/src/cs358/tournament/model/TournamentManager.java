package cs358.tournament.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import cs358.tournament.model.AdHocTournamentDB;

/**
 * TournamentManager - Singleton to manage the tournaments based on the access
 * code
 *
 * @author richardtj
 *
 */
public class TournamentManager {

  // Map of tournaments based on access key string
  private HashMap<String, Tournament> mTournaments;

  // singleton instance
  private static TournamentManager instance = null;

  // private constructor
  private TournamentManager() {
    mTournaments = new HashMap<String, Tournament>();
  }

  /**
   * getInstance returns the TournamentManager singleton
   * 
   * @return TournamentManager
   */
  public static TournamentManager getInstance() {

    // if instance hasn't been created yet
    if (instance == null) {
      instance = new TournamentManager();
    }

    return instance;
  }

  /**
   * findTournament - finds the tournament based on the access code
   * 
   * @return Tournament
   *
   */
  public Tournament findTournament(String accessCode) {
    return mTournaments.get(accessCode);
  }

  /**
   * addTournament - add tournament to the manager
   * 
   * @param Tournament
   *
   */
  public void addTournament(Tournament tourney) {
    mTournaments.put(tourney.getAccessCode(), tourney);
  }

  /**
   * getAllTournaments - return all tournaments
   *
   */
  public ArrayList<Tournament> getAllTournaments() {
    return new ArrayList<Tournament>(mTournaments.values());
  }

  /**
   * loadTournaments - loads tournaments from db into local object
   */
  public void loadTournaments() {
    try {
      HashMap<String, Tournament> temp = new HashMap<String, Tournament>();
      Connection connection = AdHocTournamentDB.getConnection();
      String fetch = "SELECT accessCode from Tournament";
      PreparedStatement preparedStatement = connection.prepareStatement(fetch);
      ResultSet result = preparedStatement.executeQuery();

      while (result.next()) {
        Tournament tourney = new Tournament(Integer.parseInt(result.getString("accessCode")));
        temp.put(tourney.getAccessCode(), tourney);
      }
      this.mTournaments = temp;

    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
