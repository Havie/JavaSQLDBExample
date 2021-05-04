package cs358.tournament.model.test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import cs358.tournament.model.Participant;
import cs358.tournament.model.DiversifiedTeamBuilder;
import cs358.tournament.model.Team;
import cs358.tournament.model.Tournament;

class DiversifiedTeamBuilderTest {

  @Test
  void testIdealSize() {
    Tournament pingPong = new Tournament("Ping Pong");
    try {
      pingPong.getTeamManager().setSizes(2, 2, 2);
    } catch (Exception e2) {
      // TODO Auto-generated catch block
      e2.printStackTrace();
    }

    for (int i = 0; i < 30; i++) {
      try {
        pingPong.addParticipant(new Participant("Joe" + i, "Schmoe", "Org" + (i % 5)));
      } catch (Exception e) {
        Assertions.fail("exception adding participants");
      }
      Assertions.assertEquals(i + 1, pingPong.getUnattachedParticipants().size());
    }

    DiversifiedTeamBuilder teamBuilder = new DiversifiedTeamBuilder();
    try {
      teamBuilder.fillTeams(pingPong);
    } catch (Exception e1) {
      // TODO Auto-generated catch block
      e1.printStackTrace();
    }

    ArrayList<Team> teamList = pingPong.getTeamManager().getTeams();
    for (Team team : teamList) {
      Assertions.assertTrue(team.getMembers().size() >= pingPong.getTeamManager().getMinTeamSize());
      Assertions.assertTrue(team.getMembers().size() <= pingPong.getTeamManager().getMaxTeamSize());
    }

    Assertions.assertEquals(15, teamList.size());

  }

  @Test
  void testFitIntoMaxSize() {
    Tournament pingPong = new Tournament("Ping Pong");
    try {
      pingPong.getTeamManager().setSizes(5, 5, 6);
    } catch (Exception e2) {
      Assertions.fail("Sizes not set as expected");
    }

    for (int i = 0; i < 24; i++) {
      try {
        pingPong.addParticipant(new Participant("Joe" + i, "Schmoe", "Org" + (i % 5)));
      } catch (Exception e) {
        Assertions.fail("exception adding participants");
      }
      Assertions.assertEquals(i + 1, pingPong.getUnattachedParticipants().size());
    }

    DiversifiedTeamBuilder teamBuilder = new DiversifiedTeamBuilder();
    try {
      teamBuilder.fillTeams(pingPong);
    } catch (Exception e1) {
      // TODO Auto-generated catch block
      e1.printStackTrace();
    }

    ArrayList<Team> teamList = pingPong.getTeamManager().getTeams();
    for (Team team : teamList) {
      Assertions.assertTrue(team.getMembers().size() >= pingPong.getTeamManager().getMinTeamSize());
      Assertions.assertTrue(team.getMembers().size() <= pingPong.getTeamManager().getMaxTeamSize());
    }

    Assertions.assertEquals(4, teamList.size());

    // check for removal of players that have been put on teams
    Assertions.assertEquals(0, pingPong.getUnattachedParticipants().size());

  }

  @Test
  void testFitIntoMinSize() {
    Tournament pingPong = new Tournament("Ping Pong");
    try {
      pingPong.getTeamManager().setSizes(5, 4, 5);
    } catch (Exception e2) {
      Assertions.fail("Sizes not set as expected");
    }

    for (int i = 0; i < 24; i++) {
      try {
        pingPong.addParticipant(new Participant("Joe" + i, "Schmoe", "Org" + (i % 5)));
      } catch (Exception e) {
        Assertions.fail("exception adding participants");
      }
      Assertions.assertEquals(i + 1, pingPong.getUnattachedParticipants().size());
    }

    DiversifiedTeamBuilder teamBuilder = new DiversifiedTeamBuilder();
    try {
      teamBuilder.fillTeams(pingPong);
    } catch (Exception e1) {
      // TODO Auto-generated catch block
      e1.printStackTrace();
    }

    ArrayList<Team> teamList = pingPong.getTeamManager().getTeams();
    for (Team team : teamList) {
      Assertions.assertTrue(team.getMembers().size() >= pingPong.getTeamManager().getMinTeamSize());
      Assertions.assertTrue(team.getMembers().size() <= pingPong.getTeamManager().getMaxTeamSize());
    }

    Assertions.assertEquals(5, teamList.size());

    // check for removal of players that have been put on teams
    Assertions.assertEquals(0, pingPong.getUnattachedParticipants().size());

  }

  @Test
  void testDropExtraParticipantsSize() {
    Tournament pingPong = new Tournament("Ping Pong");
    try {
      pingPong.getTeamManager().setSizes(10, 9, 12);
    } catch (Exception e2) {
      Assertions.fail("Sizes not set as expected");
    }

    for (int i = 0; i < 26; i++) {
      try {
        pingPong.addParticipant(new Participant("Joe" + i, "Schmoe", "Org" + (i % 5)));
      } catch (Exception e) {
        Assertions.fail("exception adding participants");
      }
      Assertions.assertEquals(i + 1, pingPong.getUnattachedParticipants().size());
    }

    DiversifiedTeamBuilder teamBuilder = new DiversifiedTeamBuilder();
    try {
      teamBuilder.fillTeams(pingPong);
    } catch (Exception e1) {
      // TODO Auto-generated catch block
      e1.printStackTrace();
    }

    ArrayList<Team> teamList = pingPong.getTeamManager().getTeams();
    for (Team team : teamList) {
      Assertions.assertTrue(team.getMembers().size() >= pingPong.getTeamManager().getMinTeamSize());
      Assertions.assertTrue(team.getMembers().size() <= pingPong.getTeamManager().getMaxTeamSize());
    }

    Assertions.assertEquals(2, teamList.size());

    // check for removal of players that have been put on teams
    Assertions.assertEquals(2, pingPong.getUnattachedParticipants().size());
    for (Participant person : pingPong.getUnattachedParticipants()) {
      System.out.println(person.getFirstName());
    }
    Assertions.assertTrue(pingPong.getUnattachedParticipants().contains(new Participant("Joe25", "Schmoe", "Org0")));
    Assertions.assertTrue(pingPong.getUnattachedParticipants().contains(new Participant("Joe24", "Schmoe", "Org4")));

  }

}
