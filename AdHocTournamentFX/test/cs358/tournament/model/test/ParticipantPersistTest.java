package cs358.tournament.model.test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import cs358.tournament.model.Participant;

class ParticipantPersistTest {

  @Test
  void existingParticipantDB() {
    Participant joe = new Participant(1);
    Assertions.assertEquals("Joe", joe.getFirstName());
    Assertions.assertEquals("Smith", joe.getLastName());
    Assertions.assertEquals("IMDB", joe.getOrganization());

  }

  @Test
  void addParticipantDB() {
    Participant cutie = new Participant("Cutie", "Richardt", "Pet Crew");
    cutie.persist();
    Assertions.assertEquals("Cutie", cutie.getFirstName());
    Assertions.assertEquals("Richardt", cutie.getLastName());
    Assertions.assertEquals("Pet Crew", cutie.getOrganization());

    cutie.setOrganization("Hamster Crew");
    cutie.persist();
    Assertions.assertEquals("Cutie", cutie.getFirstName());
    Assertions.assertEquals("Richardt", cutie.getLastName());
    Assertions.assertEquals("Hamster Crew", cutie.getOrganization());

    cutie.deletePersistedObject();

    Assertions.assertFalse(cutie.fetchPersistedObject());

  }

  @Test
  void updateParticipantDB() {
    Participant steve = new Participant(3, "Stevey", "Smiths", "GLHFU");
    Assertions.assertEquals("Stevey", steve.getFirstName());
    Assertions.assertEquals("Smiths", steve.getLastName());
    Assertions.assertEquals("GLHFU", steve.getOrganization());

    // discard change to Hamster Crew
    steve.fetchPersistedObject();
    Assertions.assertEquals("Steve", steve.getFirstName());
    Assertions.assertEquals("Smith", steve.getLastName());
    Assertions.assertEquals("GLHF", steve.getOrganization());

    // rechange to Hamster Crew
    steve.setOrganization("HLU");
    steve.persist();
    Assertions.assertEquals("Steve", steve.getFirstName());
    Assertions.assertEquals("Smith", steve.getLastName());
    Assertions.assertEquals("HLU", steve.getOrganization());

    // reset to back to Pet Crew
    steve.setOrganization("GLHF");
    steve.persist();
    Assertions.assertEquals("Steve", steve.getFirstName());
    Assertions.assertEquals("Smith", steve.getLastName());
    Assertions.assertEquals("GLHF", steve.getOrganization());

  }

}
