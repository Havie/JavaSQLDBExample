package cs358.tournament.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import org.junit.jupiter.api.Assertions;

import cs358.tournament.model.Participant;
import cs358.tournament.model.Team;
import cs358.tournament.model.TeamManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * Controller for team resolution view
 * 
 * @author Alec Selle
 *
 */
public class ResolveTeamController implements Initializable {
  @FXML
  private TableView invalidTeamsTable;

  @FXML
  private TableColumn<TeamDisplay, String> teamColumn;

  @FXML
  private TableColumn<TeamDisplay, String> sizeColumn;

  @FXML
  private TableColumn<TeamDisplay, String> problemColumn;

  @Override
  public void initialize(URL arg0, ResourceBundle arg1) {
    teamColumn.setCellValueFactory(new PropertyValueFactory<TeamDisplay, String>("teamName"));
    sizeColumn.setCellValueFactory(new PropertyValueFactory<TeamDisplay, String>("teamSize"));
    problemColumn.setCellValueFactory(new PropertyValueFactory<TeamDisplay, String>("problem"));
  }

  private void populateTable(TeamManager teamManager) {
    ObservableList<TeamDisplay> data = generateData(teamManager);

    invalidTeamsTable.getItems().clear();
    invalidTeamsTable.getItems().addAll(data);
  }

  private ObservableList<TeamDisplay> generateData(TeamManager manager) {
    ObservableList<TeamDisplay> data = FXCollections.observableArrayList();
    ArrayList<Team> invalid = manager.validateTeams();

    for (Team team : invalid) {
      data.add(new TeamDisplay(manager, team));
    }

    return data;
  }

  public class TeamDisplay {
    private String teamName;
    private int teamSize;
    private String problem;

    public TeamDisplay(TeamManager manager, Team team) {
      this.teamName = team.getmTeamName();
      this.teamSize = team.getMembers().size();
      if (this.teamSize < manager.getMinTeamSize()) {
        this.problem = "Too small";
      } else if (this.teamSize > manager.getMaxTeamSize()) {
        this.problem = "Too big";
      }
    }

    public String getTeamName() {
      return teamName;
    }

    public String getTeamSize() {
      return teamSize + "";
    }

    public String getProblem() {
      return problem;
    }

  }

}
