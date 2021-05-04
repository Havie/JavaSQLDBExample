package cs358.tournament.model;

import java.util.ArrayList;

/**
 * TeamManager - adds and removes the teams for a tournament
 * 
 * @author jocelyn
 *
 */
public class TeamManager {

  private final static int DEFAULT_TEAM_SIZE = 2;

  private int mIdealTeamSize;
  private int mMinTeamSize;
  private int mMaxTeamSize;
  private boolean mteamGeneration; // Random(True) or Player choice(False) team generation

  private ArrayList<Team> mTeamList;

  /*
   * TeamManager Default Constructor
   */
  public TeamManager() {
    mIdealTeamSize = DEFAULT_TEAM_SIZE;
    mMinTeamSize = DEFAULT_TEAM_SIZE;
    mMaxTeamSize = DEFAULT_TEAM_SIZE;

    mTeamList = new ArrayList<Team>();
  }

  /*
   * addTeam - adds a team to the tournament
   * 
   * @param team to add
   */
  public void addTeam(Team team) {
    mTeamList.add(team);
  }

  /*
   * removeTeam - removes a team from the tournament
   * 
   * @param team to add
   */
  public void removeTeam(Team team) {
    mTeamList.remove(team);
  }

  /*
   * setSizes - sets the team size for the tournament
   * 
   * @param team to add
   */
  public void setSizes(int ideal, int min, int max) throws Exception {
    if (ideal <= max && ideal >= min) {
      this.mIdealTeamSize = ideal;
      this.mMaxTeamSize = max;
      this.mMinTeamSize = min;
    }
  }

  public ArrayList<Team> validateTeams() {
    ArrayList<Team> invalid = new ArrayList<Team>();
    for (Team team : mTeamList) {
      int size = team.getMembers().size();
      if (size < this.mMinTeamSize || size > this.mMaxTeamSize) {
        invalid.add(team);
      }
    }
    return invalid;
  }

  public ArrayList<Team> getTeams() {
    return mTeamList;
  }

  /**
   * @return the mIdealTeamSize
   */
  public int getIdealTeamSize() {
    return mIdealTeamSize;
  }

  /**
   * @return the mMaxTeamSize
   */
  public int getMaxTeamSize() {
    return mMaxTeamSize;
  }

  /**
   * @return the mMinTeamSize
   */
  public int getMinTeamSize() {
    return mMinTeamSize;
  }

  public boolean getTeamGeneration() {
    return mteamGeneration;
  }

  public void setTeamGeneration(boolean teamGeneration) {
    this.mteamGeneration = teamGeneration;
  }

}
