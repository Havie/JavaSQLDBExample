package cs358.tournament.model.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import cs358.tournament.model.Participant;
import cs358.tournament.model.RandomTeamBuilder;
import cs358.tournament.model.Team;
import cs358.tournament.model.Tournament;

class TournamentTest {

  @Test
  void defaultConstructorTest() {
    Tournament pingPong = new Tournament("PingPong");

    // check default start date is today
    LocalDateTime now = LocalDate.now().atTime(0, 0);
    Assertions.assertTrue(now.equals(pingPong.getStartDate()));
    // System.out.println("Start date: " + pingPong.getStartDate());
    // System.out.println("Now: " + now);

    // check default end date is tomorrow
    Assertions.assertTrue(now.plusDays(1).equals(pingPong.getEndDate()));
    // System.out.println("End date: " + pingPong.getEndDate());

    /// (the way we generate accessCodes is not this way)
    // check code minus last two unique chars
    // String code = pingPong.getAccessCode();
    // Assertions.assertEquals("PIN11", code.substring(0, 5));
    // System.out.println("Access Code: " + pingPong.getAccessCode());

    Assertions.assertEquals("PingPong", pingPong.getName());

  }

  @Test
  void nullConstructorTest() {
    Tournament nullTourney = new Tournament(null);

    // check default start date is today
    LocalDateTime now = LocalDate.now().atTime(0, 0);
    Assertions.assertTrue(now.equals(nullTourney.getStartDate()));
    // System.out.println("Start date: " + nullTourney.getStartDate());
    // System.out.println("Now: " + now);

    // check default end date is tomorrow
    Assertions.assertTrue(now.plusDays(1).equals(nullTourney.getEndDate()));
    // System.out.println("End date: " + nullTourney.getEndDate());

    // (the way we generate accessCodes is not this way)
    // check code minus last two unique chars
    // String code = nullTourney.getAccessCode();
    // Assertions.assertEquals("TOU11", code.substring(0, 5));
    // System.out.println("Access Code: " + nullTourney.getAccessCode());

    Assertions.assertEquals("Tournament", nullTourney.getName());

  }

  @Test
  void setNameTests() {
    Tournament pingPong = new Tournament("Ping Pong");

    pingPong.setName("Family Reunion Ping Pong Tournament");
    Assertions.assertEquals("Family Reunion Ping Pong Tournament", pingPong.getName());

    // use default name on bad input
    pingPong.setName("     ");
    Assertions.assertEquals("Tournament", pingPong.getName());

    // use default name on null
    pingPong.setName(null);
    Assertions.assertEquals("Tournament", pingPong.getName());

  }

  @Test
  void setValidDatesTests() {
    Tournament pingPong = new Tournament("Ping Pong");

    LocalDateTime now = LocalDate.now().atTime(0, 0);

    // valid dates
    try {
      pingPong.setDates(now, now.plusDays(1));
    } catch (Exception e) {
      Assertions.fail("valid dates threw an exception");
    }

  }

  @Test
  void setStartAfterEndDatesTests() {
    Tournament pingPong = new Tournament("Ping Pong");

    LocalDateTime now = LocalDate.now().atTime(0, 0);

    // start after end
    boolean exceptionWasThrown = false;
    try {
      pingPong.setDates(now.plusDays(1), now);
    } catch (Exception e) {
      exceptionWasThrown = true;
    }
    Assertions.assertTrue(exceptionWasThrown);

  }

  /*
   * Ignored for streamlined development
   * 
   * @Test void setStartAfterTodayTests() { Tournament pingPong = new
   * Tournament("Ping Pong");
   * 
   * LocalDateTime now = LocalDate.now().atTime(0, 0);
   * 
   * // start before today Boolean exceptionWasThrown = false; try {
   * pingPong.setDates(now.minusDays(1), now); } catch (Exception e) {
   * exceptionWasThrown = true; } Assertions.assertTrue(exceptionWasThrown); }
   */
  @Test
  void addParticipantsTest() {

    Tournament pingPong = new Tournament("Ping Pong");

    for (int i = 0; i < 30; i++) {
      try {
        pingPong.addParticipant(new Participant("Joe" + i, "Schmoe", "Fake place"));
      } catch (Exception e) {
        Assertions.fail("exception adding participants");
      }
      Assertions.assertEquals(i + 1, pingPong.getUnattachedParticipants().size());
    }

    // add a duplicate
    Boolean exceptionWasThrown = false;
    try {
      pingPong.addParticipant(new Participant("Joe0", "Schmoe", "Fake place"));
    } catch (Exception e) {
      exceptionWasThrown = true;
    }
    Assertions.assertTrue(exceptionWasThrown);
    Assertions.assertEquals(30, pingPong.getUnattachedParticipants().size());
  }

  @Test
  void resolveMatchDoubleElimTest() throws Exception {

    Tournament pingPong = new Tournament("Ping Pong");

    for (int i = 0; i < 30; i++) {
      try {
        pingPong.addParticipant(new Participant("Joe" + i, "Schmoe", "Fake place"));
      } catch (Exception e) {
        Assertions.fail("exception adding participants");
      }
      Assertions.assertEquals(i + 1, pingPong.getUnattachedParticipants().size());
    }

    pingPong.getTeamManager().setSizes(5, 5, 6);

    RandomTeamBuilder teamBuilder = new RandomTeamBuilder();

    teamBuilder.fillTeams(pingPong);

    pingPong.setTypeIndicator(false);

    Team toLose = pingPong.getTeamManager().getTeams().get(1);

    Assertions.assertNotNull(toLose);

    pingPong.resolveMatch(pingPong.getTeamManager().getTeams().get(0), pingPong.getTeamManager().getTeams().get(1));

    Assertions.assertTrue(pingPong.getTeamManager().getTeams().contains(toLose));

    pingPong.resolveMatch(pingPong.getTeamManager().getTeams().get(0), pingPong.getTeamManager().getTeams().get(1));

    Assertions.assertFalse(pingPong.getTeamManager().getTeams().contains(toLose));

  }

  @Test
  void resolveMatchSingleElimTest() throws Exception {

    Tournament pingPong = new Tournament("Ping Pong");

    for (int i = 0; i < 30; i++) {
      try {
        pingPong.addParticipant(new Participant("Joe" + i, "Schmoe", "Fake place"));
      } catch (Exception e) {
        Assertions.fail("exception adding participants");
      }
      Assertions.assertEquals(i + 1, pingPong.getUnattachedParticipants().size());
    }

    pingPong.getTeamManager().setSizes(5, 5, 6);

    RandomTeamBuilder teamBuilder = new RandomTeamBuilder();

    teamBuilder.fillTeams(pingPong);

    pingPong.setTypeIndicator(true);

    Team toLose = pingPong.getTeamManager().getTeams().get(1);

    Assertions.assertNotNull(toLose);

    pingPong.resolveMatch(pingPong.getTeamManager().getTeams().get(0), pingPong.getTeamManager().getTeams().get(1));

    Assertions.assertFalse(pingPong.getTeamManager().getTeams().contains(toLose));

  }

  @Test
  void dbAddNewTournamentToDataBase() {
    Tournament t = new Tournament("Test_");
    assert (t.addPersistedObject());
  }

  @Test
  void dbGetExistingTournamentFromDatabase() {
    // 0001
    Integer accessCodeDBKey = 0001;
    Tournament t = new Tournament(accessCodeDBKey);
    assert (accessCodeDBKey.toString().equals(t.getAccessCode()));

  }

  @Test
  void dbDeleteTournamentFromDatabase() {
    Tournament t = new Tournament("Test_Delete");
    assert (t.addPersistedObject());
    assert (t.deletePersistedObject());

  }

  @Test
  void dbUpdateTournamentFromDatabase() {
    Integer accessCodeDBKey = 1002;
    Tournament t = new Tournament(accessCodeDBKey);
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
    LocalDateTime now = LocalDateTime.now();
    t.setName("Updated Tourney - " + dtf.format(now));
    assert (t.updatePersistedObject());
  }

}
