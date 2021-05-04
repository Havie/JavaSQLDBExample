package cs358.tournament.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import cs358.tournament.application.Main;
import cs358.tournament.controller.ResolveTeamController.TeamDisplay;
import cs358.tournament.model.Team;
import cs358.tournament.model.TeamManager;
import cs358.tournament.model.Tournament;
import cs358.tournament.model.TournamentManager;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class MainMenuController implements Initializable {

  @FXML
  TableView<TournamentDisplay> listTournament;
  @FXML
  TableColumn<TournamentDisplay, String> columnName;
  @FXML
  TableColumn<TournamentDisplay, String> columnId;
  @FXML
  Button buttonRefresh;
  @FXML
  Button buttonExit;
  @FXML
  Button buttonCreate;
  @FXML
  Button buttonEdit;

  private TournamentManager instance;

  @Override
  public void initialize(URL arg0, ResourceBundle arg1) {
    columnName.setCellValueFactory(new PropertyValueFactory<TournamentDisplay, String>("name"));
    columnId.setCellValueFactory(new PropertyValueFactory<TournamentDisplay, String>("id"));
    instance = TournamentManager.getInstance();
    populateList();
  }

  /**
   * Switch to create view
   */
  public void create() {
    Main.selectedTournament = null;
    Main.changeScene(Main.EDIT_MENU);
  }

  /**
   * Read selected tournament and switch to edit view
   */
  public void edit() {
    if (listTournament != null) {
      var selectedItem = listTournament.getSelectionModel().getSelectedItem();
      if (selectedItem != null) {
        Tournament tourney = instance.findTournament(selectedItem.getId());
        Main.selectedTournament = tourney;
        Main.changeScene(Main.EDIT_MENU);
      }
    }
  }

  /**
   * Refresh list of tournaments
   */
  public void refresh() {
    populateList();
  }

  /**
   * Exit program
   */
  public void exit() {
    Platform.exit();
  }

  private void populateList() {
    ObservableList<TournamentDisplay> data = generateData();

    listTournament.getItems().clear();
    listTournament.getItems().addAll(data);

  }

  private ObservableList<TournamentDisplay> generateData() {
    instance.loadTournaments();
    ObservableList<TournamentDisplay> data = FXCollections.observableArrayList();
    ArrayList<Tournament> tourneys = instance.getAllTournaments();

    for (Tournament tourney : tourneys) {
      data.add(new TournamentDisplay(tourney));
    }

    return data;
  }

  /**
   * TournamentDisplay - Container for tournaments allowing quick display in
   * tables
   * 
   * @author Alec Selle
   */
  public class TournamentDisplay {
    private String name;
    private String id;

    public TournamentDisplay(Tournament tourney) {
      this.name = tourney.getName();
      this.id = tourney.getAccessCode();
    }

    public String getName() {
      return name;
    }

    public String getId() {
      return id;
    }

  }

}
