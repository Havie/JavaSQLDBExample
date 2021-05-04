/**
 * 
 */
package cs358.tournament.model.test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import cs358.tournament.model.Participant;

/**
 * @author richardtj
 *
 */
class ParticipantTest {

  @Test
  void memberDataTest() {

    Participant joe = new Participant("Joe", "Schmoe", "Fake place");
    Assertions.assertEquals("Joe", joe.getFirstName());
    Assertions.assertEquals("Schmoe", joe.getLastName());
    Assertions.assertEquals("Fake place", joe.getOrganization());

  }

  @Test
  void changeOrganizationTest() {

    Participant joe = new Participant("Joe", "Schmoe", "Fake place");
    Assertions.assertEquals("Fake place", joe.getOrganization());

    // change the org
    joe.setOrganization("UW-Stout");
    Assertions.assertEquals("Joe", joe.getFirstName());
    Assertions.assertEquals("Schmoe", joe.getLastName());
    Assertions.assertEquals("UW-Stout", joe.getOrganization());

  }

  @Test
  void compareParticipants() {

    Participant joe1 = new Participant("Joe", "Schmoe", "Fake place");
    Participant joe2 = new Participant("Joe", "Schmoe", "Fake place");
    Assertions.assertEquals(joe1, joe2);

    Participant mary = new Participant("Mary", "Schmoe", "Fake place");
    Assertions.assertNotEquals(mary, joe2);

    Participant joeFoe = new Participant("Joe", "Foe", "Fake place");
    Assertions.assertNotEquals(joeFoe, joe2);

    Participant someOtherJoeSchmoe = new Participant("Joe", "Schmoe", "A different Fake place");
    Assertions.assertNotEquals(someOtherJoeSchmoe, joe2);

    Participant totallyDifferentPerson = new Participant("Betty", "Sue", "The 1950s");
    Assertions.assertNotEquals(totallyDifferentPerson, joe2);

  }

}
