package cs358.tournament.controller;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.ResourceBundle;
import cs358.tournament.application.Main;
import cs358.tournament.model.Tournament;
import cs358.tournament.model.TournamentManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;

public class EditTournamentController implements Initializable {

  @FXML
  private ChoiceBox<String> bracketType;
  @FXML
  private ChoiceBox<String> diversityType;
  @FXML
  private DatePicker theStartDate;
  @FXML
  private DatePicker theEndDate;
  private ToggleGroup teamgenGroup;
  @FXML
  private RadioButton teamgenRandom;
  @FXML
  private RadioButton teamgenPlayerChoice;
  @FXML
  private Button buttonCancel;
  @FXML
  private Button buttonSave;
  @FXML
  private Button bDelete;
  @FXML
  private TextField textName;
  @FXML
  private TextField textMinSize;
  @FXML
  private TextField textMaxSize;
  @FXML
  private TextField textIdealSize;
  @FXML
  private Label accessCodeLabel;

  final PseudoClass errorClass = PseudoClass.getPseudoClass("error");

  @Override
  public void initialize(URL arg0, ResourceBundle arg1) {
    // ObservableList<String> bracketOptions =
    // FXCollections.observableArrayList("Single Elimination", "Double Elimination",
    // "Ad-Hoc"); No way to check for adhoc currently
    ObservableList<String> bracketOptions = FXCollections.observableArrayList("Single Elimination",
        "Double Elimination");
    ObservableList<String> diversityOptions = FXCollections.observableArrayList("Closed", "Homogenous",
        "Maximized Diversity", "Enforced Diversity", "Open");

    bracketType.getItems().setAll(bracketOptions);
    diversityType.getItems().setAll(diversityOptions);

    teamgenGroup = new ToggleGroup();
    teamgenRandom.setToggleGroup(teamgenGroup);
    teamgenPlayerChoice.setToggleGroup(teamgenGroup);

    if (Main.selectedTournament != null && Main.selectedTournament.getTeamManager() != null) {
      load();
    } else {
      bDelete.setVisible(false);
    }
  }

  private boolean isValidInteger(String s) {
    try {
      if (Integer.parseInt(s) > 0) {
        return true;
      }
    } catch (Exception e) {
      return false;
    }
    return false;
  }

  private boolean validate() {
    // If any field fails, change this to false
    boolean valid = true;

    // To set error state on input field
    // object.pseudoClassStateChanged(errorClass, true);
    // Requires relevant css in application.css
    // Currently included: text field, choice box, radio button

    if (textName.getText() == null || textName.getText().isBlank()) {
      textName.pseudoClassStateChanged(errorClass, true);
      valid = false;
      System.out.println("Invalid name");
    }
    if (textMinSize.getText() == null || textMinSize.getText().isBlank() || !isValidInteger(textMinSize.getText())) {
      textMinSize.pseudoClassStateChanged(errorClass, true);
      valid = false;
      System.out.println("Invalid minimum team size");
    }
    if (textMaxSize.getText() == null || textMaxSize.getText().isBlank() || !isValidInteger(textMaxSize.getText())) {
      textMaxSize.pseudoClassStateChanged(errorClass, true);
      valid = false;
      System.out.println("Invalid maximum team size");
    }
    if (textIdealSize.getText() == null || textIdealSize.getText().isBlank()
        || !isValidInteger(textIdealSize.getText())) {
      textIdealSize.pseudoClassStateChanged(errorClass, true);
      valid = false;
      System.out.println("Invalid ideal team size");
    }
    if (bracketType.getValue() == null) {
      bracketType.pseudoClassStateChanged(errorClass, true);
      valid = false;
      System.out.println("Invalid bracket type");
    }
    if (diversityType.getValue() == null) {
      diversityType.pseudoClassStateChanged(errorClass, true);
      valid = false;
      System.out.println("Invalid diversity type");
    }
    if (teamgenGroup.getSelectedToggle() == null) {
      teamgenRandom.pseudoClassStateChanged(errorClass, true);
      teamgenPlayerChoice.pseudoClassStateChanged(errorClass, true);
      valid = false;
      System.out.println("Invalid team generation type");
    }
    if (theStartDate.getValue() == null) {
      theStartDate.pseudoClassStateChanged(errorClass, true);
      valid = false;
      System.out.println("Invalid start date");
    }
    if (theEndDate.getValue() == null || !theEndDate.getValue().isAfter(theStartDate.getValue())) {
      theEndDate.pseudoClassStateChanged(errorClass, true);
      valid = false;
      System.out.println("Invalid end date");
    }

    return valid;
  }

  private void backToMain() {
    Main.changeScene(Main.MAIN_MENU);
  }

  private void saveCode() throws Exception {
    if (!validate()) {
      return;
    }

    String tournamentName = textName.getText();
    // if selected tournament exists, set it to that. if it doesnt, make a new
    // tournament.
    Tournament theTournament = Main.selectedTournament != null ? Main.selectedTournament
        : new Tournament(tournamentName);
    theTournament.setName(tournamentName);

    LocalDateTime startDate = theStartDate.getValue().atTime(12, 30, 30, 30);
    LocalDateTime endDate = theEndDate.getValue().atTime(12, 30, 30, 30);
    if (startDate == null) {
      System.out.println("No Start Date");
    }
    if (endDate == null) {
      System.out.println("No End Date");
    }
    theTournament.setDates(startDate, endDate);
    theTournament.setTypeIndicator((bracketType.getSelectionModel().getSelectedIndex() == 1) ? true : false);

    int minTeamSize = Integer.parseInt(textMinSize.getText());
    int maxTeamSize = Integer.parseInt(textMaxSize.getText());
    int idealTeamSize = Integer.parseInt(textIdealSize.getText());
    theTournament.getTeamManager().setSizes(idealTeamSize, minTeamSize, maxTeamSize);

    theTournament.setSignUpMode(diversityType.getSelectionModel().getSelectedIndex());

    if (teamgenRandom.isSelected()) {
      theTournament.getTeamManager().setTeamGeneration(true);
    } else
      theTournament.getTeamManager().setTeamGeneration(false);

    if (Main.selectedTournament != null) {
      accessCodeLabel.setText("Access Code: " + theTournament.getAccessCode());
    } else {
      accessCodeLabel.setText("Access Code: " + theTournament.generateAccessCode());
    }

    if (Main.selectedTournament != null) {
      if (theTournament.updatePersistedObject()) {
        System.out.println("Updated");
        return;
      } else
        throw new Exception("Couldn't update tournament to database");
    } else {
      if (theTournament.addPersistedObject())
        return;
      else
        throw new Exception("Couldn't add tournament to database");
    }
  }

  private void load() {
    Tournament theTournament = Main.selectedTournament;
    textName.setText(theTournament.getName());
    theStartDate.setValue(theTournament.getStartDate().toLocalDate());
    theEndDate.setValue(theTournament.getEndDate().toLocalDate());
    bracketType.getSelectionModel().select((theTournament.getTypeIndicator()) ? 1 : 0);
    textMinSize.setText(String.valueOf(theTournament.getTeamManager().getMinTeamSize()));
    textMaxSize.setText(String.valueOf(theTournament.getTeamManager().getMaxTeamSize()));
    textIdealSize.setText(String.valueOf(theTournament.getTeamManager().getIdealTeamSize()));
    if (theTournament.getTeamManager().getTeamGeneration()) {
      teamgenGroup.selectToggle(teamgenRandom);
    } else {
      teamgenGroup.selectToggle(teamgenPlayerChoice);
    }
    diversityType.getSelectionModel().select(theTournament.getSignUpMode());
  }

//BELOW ARE THE PUBLIC FUNCTIONS, WHICH ARE FIRED ON EVENTS IN THE SCENE BUILDER

  // On Action of the Delete button
  public void delete() {

    if (!validate()) {
      return;
    }
    String tournamentName = textName.getText();
    Tournament theTournament = Main.selectedTournament != null
        ? new Tournament(Integer.parseInt(Main.selectedTournament.getAccessCode()))
        : new Tournament(tournamentName);
    theTournament.deletePersistedObject();
    backToMain();

  }

  // On Action of the Back button
  public void cancel() {
    backToMain();
  }

  // On Action of the Save button
  public void save() throws Exception {
    saveCode();
  }

  /**
   * Clear error state for name field
   */
  public void clearErrorTextName() {
    textName.pseudoClassStateChanged(errorClass, false);
  }

  /**
   * Clear error state for min size field
   */
  public void clearErrorTextMinSize() {
    textMinSize.pseudoClassStateChanged(errorClass, false);
  }

  /**
   * Clear error state for max size field
   */
  public void clearErrorTextMaxSize() {
    textMaxSize.pseudoClassStateChanged(errorClass, false);
  }

  /**
   * Clear error state for ideal size field
   */
  public void clearErrorTextIdealSize() {
    textIdealSize.pseudoClassStateChanged(errorClass, false);
  }

  /**
   * Clear error state for bracket combo
   */
  public void clearErrorComboBracketType() {
    bracketType.pseudoClassStateChanged(errorClass, false);
  }

  /**
   * Clear error state for diversity combo
   */
  public void clearErrorComboDiversityType() {
    diversityType.pseudoClassStateChanged(errorClass, false);
  }

  /**
   * Clear error state for teamgen radio
   */
  public void clearErrorRadioTeamgen() {
    teamgenRandom.pseudoClassStateChanged(errorClass, false);
    teamgenPlayerChoice.pseudoClassStateChanged(errorClass, false);
  }

  /**
   * Clear error state for start date field
   */
  public void clearErrorDateStart() {
    theStartDate.pseudoClassStateChanged(errorClass, false);
  }

  /**
   * Clear error state for end date field
   */
  public void clearErrorDateEnd() {
    theEndDate.pseudoClassStateChanged(errorClass, false);
  }

}
