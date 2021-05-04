package cs358.tournament.model.test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import cs358.tournament.model.Participant;
import cs358.tournament.model.Team;
import cs358.tournament.model.Tournament;

class TeamTest {

  @Test
  void addMemberTest() {

    Team aTeam = new Team("A-Team");

    for (int i = 0; i < 30; i++) {
      try {
        aTeam.addMember(new Participant("Joe" + i, "Schmoe", "Fake place"));
      } catch (Exception e) {
        Assertions.fail("exception adding participants");
      }
      Assertions.assertEquals(i + 1, aTeam.getMembers().size());
    }

    // add a duplicate
    Boolean exceptionWasThrown = false;
    try {
      aTeam.addMember(new Participant("Joe0", "Schmoe", "Fake place"));
    } catch (Exception e) {
      exceptionWasThrown = true;
    }
    Assertions.assertTrue(exceptionWasThrown);
    Assertions.assertEquals(30, aTeam.getMembers().size());
  }

  @Test
  void removeMemberTest() {

    Team aTeam = new Team("A-Team");

    for (int i = 0; i < 30; i++) {
      try {
        aTeam.addMember(new Participant("Joe" + i, "Schmoe", "Fake place"));
      } catch (Exception e) {
        Assertions.fail("exception adding participants");
      }
      Assertions.assertEquals(i + 1, aTeam.getMembers().size());
    }

    // remove a member
    Boolean exceptionWasThrown = false;
    try {
      aTeam.removeMember(new Participant("Joe0", "Schmoe", "Fake place"));
    } catch (Exception e) {
      Assertions.fail("exception removing participant");
    }
    Assertions.assertEquals(29, aTeam.getMembers().size());
  }

}
