package cs358.tournament.model;

import java.util.ArrayList;
import java.util.Collections;

public class RandomTeamBuilder {

  ArrayList<Participant> mPlayingList;

  public void fillTeams(Tournament tourney) throws Exception {

    int idealSize = tourney.getTeamManager().getIdealTeamSize();
    int minSize = tourney.getTeamManager().getMinTeamSize();
    int maxSize = tourney.getTeamManager().getMaxTeamSize();

    ArrayList<Participant> allList = tourney.getUnattachedParticipants();
    int nbrParticipants = allList.size();

    // check for at least 2 teams
    if (nbrParticipants < (2 * minSize)) {
      throw new Exception("Not enough participants for two teams");
    }

    // start with ideal size teams
    int nbrTeams = nbrParticipants / idealSize;
    int extraParticipants = nbrParticipants % idealSize;

    // see if extra participants require a new team to be added
    // because they would exceed the max team size
    if (extraParticipants > (nbrTeams * (maxSize - idealSize))) {
      nbrTeams++;
      // see if the extra team breaks the min team size
      double calculatedMinTeamSize = nbrParticipants / nbrTeams;
      if (calculatedMinTeamSize < minSize) {
        // can not add a team to accommodate all players
        nbrTeams--;
      }
    }

    if (nbrTeams > Tournament.MAX_TEAMS) {
      nbrTeams = Tournament.MAX_TEAMS;
    }

    // remove extra players
    if (nbrParticipants > (nbrTeams * maxSize)) {
      mPlayingList = new ArrayList<Participant>(allList.subList(0, (nbrTeams * maxSize - 1)));
    } else {
      mPlayingList = (ArrayList<Participant>) allList.clone();
    }

    mix();

    ArrayList<Team> teamList = new ArrayList<Team>();
    for (int i = 1; i <= nbrTeams; i++) {
      teamList.add(new Team("Team" + i));
    }

    for (int i = 0; i < mPlayingList.size(); i++) {
      teamList.get(i % nbrTeams).addMember(mPlayingList.get(i));
    }

    for (int i = 0; i < nbrTeams; i++) {
      tourney.getTeamManager().addTeam(teamList.get(i));
    }

    // remove unattached player that are not on teams
    if (nbrParticipants > (nbrTeams * maxSize)) {
      allList = new ArrayList<Participant>(allList.subList(nbrTeams * maxSize, nbrParticipants));
      tourney.removeAllUnattachedParticipants();
      for (Participant player : allList) {
        tourney.addParticipant(player);
      }
    } else {
      tourney.removeAllUnattachedParticipants();
    }

  }

  protected void mix() {
    Collections.shuffle(mPlayingList);
  }

}
