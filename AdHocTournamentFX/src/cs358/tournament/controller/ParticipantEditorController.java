package cs358.tournament.controller;

import java.io.IOException;

import cs358.tournament.model.Participant;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;

public class ParticipantEditorController {
  @FXML
  private TextField firstname;
  @FXML
  private TextField lastname;
  @FXML
  private TextField organization;
  @FXML
  private TextField participantId;

  Participant mExistingParticipant;

  /*
   * Matthew Friemoth 04/18/2021 Checks if a string is an integer
   */
  public static boolean isInteger(String s) {
    try {
      Integer.parseInt(s);
    } catch (NumberFormatException e) {
      return false;
    } catch (NullPointerException e) {
      return false;
    }
    // only got here if we didn't return false
    return true;
  }

  @FXML
  private void saveParticipantChanges(ActionEvent event) {

    Participant addParticipant;

    String firstNameValue = firstname.getText();
    String lastNameValue = lastname.getText();
    String organizationValue = organization.getText();

    String participantIdValue = participantId.getText();
    if (mExistingParticipant == null || participantIdValue == "") {
      addParticipant = new Participant(firstNameValue, lastNameValue, organizationValue);
      addParticipant.persist();
      mExistingParticipant = addParticipant;
      participantId.setText(mExistingParticipant.getDatabaseId().toString());
    } else if (isInteger(participantIdValue)) {
      // don't allow a Participant id to be updated
      if (Integer.valueOf(participantIdValue) != mExistingParticipant.getDatabaseId()) {
        System.out.println("Not allowed to update the ID");
        mExistingParticipant = null;
        // only allow the fields to be updated if the id wasn't changed.
      } else {
        mExistingParticipant.setOrganization(organizationValue);
        mExistingParticipant.persist();
      }
    } else {
      System.out.println("Invalid Input 1");
    }
    event.consume();
    System.out.println("Save Changes");
  }

  @FXML
  private void cancelParticipantChanges(ActionEvent event) {
    event.consume();
    participantId.setText("");
    firstname.setText("");
    lastname.setText("");
    organization.setText("");
    mExistingParticipant = null;
    System.out.println("Cancel Changes");
  }

  @FXML
  private void loadParticipantFromDatabase(ActionEvent event) {
    event.consume();

    if (isInteger(participantId.getText())) {
      Integer participantIdValue = Integer.valueOf(participantId.getText());
      mExistingParticipant = new Participant(participantIdValue);
      if (mExistingParticipant.getDatabaseId() != null) {
        firstname.setText(mExistingParticipant.getFirstName());
        lastname.setText(mExistingParticipant.getLastName());
        organization.setText(mExistingParticipant.getOrganization());
      } else {
        System.out.println("Participant does not exist.");
      }
    } else {
      System.out.println("Invalid Input 2");
    }
  }
}
