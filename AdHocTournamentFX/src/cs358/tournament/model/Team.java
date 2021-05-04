/**
 * 
 */
package cs358.tournament.model;

import java.util.ArrayList;

/**
 * Team - named list of participants to play in a tournament
 * 
 * @author jocelyn
 *
 */
public class Team {

  private final static String DEFAULT_TEAM_NAME = "Team";

  private ArrayList<Participant> mMembers;
  private String mTeamName;
  private boolean mHasLost; // indicates what bracket the team is currently in true if they are in the
                            // loser's side of a double elim bracket, false otherwise

  /**
   * Constructor
   * 
   * @param name - name of the team
   */
  public Team(String name) {
    mMembers = new ArrayList<Participant>();
    if (name == null || name.isBlank()) {
      setmTeamName(DEFAULT_TEAM_NAME);
    } else {
      setmTeamName(name.trim());
    }
    mHasLost = false;
  }

  /**
   * addMember - adds a participant to a team
   * 
   * @param participant to add to the team
   * @throws Exception
   */
  public void addMember(Participant participant) throws Exception {
    if (!mMembers.contains(participant)) {
      mMembers.add(participant);
    } else {
      throw new Exception("Duplicate participant");
    }
  }

  /**
   * removeMember - removes a participant from a team
   * 
   * @param participant to remove from the team
   */
  public void removeMember(Participant participant) {
    mMembers.remove(participant);
  }

  /**
   * getMembers - returns list of members from the team
   * 
   * @return
   * 
   */
  public ArrayList<Participant> getMembers() {
    return mMembers;
  }

  public boolean getHasLost() {
    return mHasLost;
  }

  public void setHasLost(boolean mHasLost) {
    this.mHasLost = mHasLost;
  }

  public String getmTeamName() {
    return mTeamName;
  }

  public void setmTeamName(String mTeamName) {
    this.mTeamName = mTeamName;
  }
}
