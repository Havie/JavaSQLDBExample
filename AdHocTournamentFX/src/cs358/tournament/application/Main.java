package cs358.tournament.application;

import cs358.tournament.model.Tournament;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class Main extends Application {
  private static Stage primaryStage;
  public static final String MAIN_MENU = "mainMenu.fxml";
  public static final String CREATE_MENU = "EditTournamentMenu.fxml";
  public static final String EDIT_MENU = "EditTournamentMenu.fxml";

  public static Tournament selectedTournament = null;

  @Override
  public void start(Stage primaryStage) {
    Main.primaryStage = primaryStage;
    try {
      Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("cs358/tournament/view/" + MAIN_MENU));

      Scene scene = new Scene(root);
      scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

      primaryStage.setTitle("Group 4 Tournament Editor");
      primaryStage.setScene(scene);
      primaryStage.show();

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * changeScene - Changes active scene of application
   * 
   * @param fxml Name of fxml file, must be in view folder
   */
  public static void changeScene(String fxml) {
    // System.out.println("Trying to load path cs358/tournament/view/ " +fxml);
    try {
      Parent pane = FXMLLoader.load(Main.class.getClassLoader().getResource("cs358/tournament/view/" + fxml));
      Main.primaryStage.getScene().setRoot(pane);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args) {
    launch(args);
  }
}