package cs358.tournament.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Participant - a tournament participant who will play on a team in a bracket
 * 
 * @author richardtj
 *
 */
public class Participant extends PersistableObject {

  private String mFirstName;
  private String mLastName;
  private String mOrganization;

  /**
   * Constructor
   * 
   * @param first        first name of the participant
   * @param last         last name of the participant
   * @param organization organization in which the participant belongs
   */
  public Participant(String first, String last, String organization) {
    super();
    mFirstName = first;
    mLastName = last;
    mOrganization = organization;
  }

  /**
   * Constructor
   * 
   * @param id           id of the participant from the database table
   * @param first        first name of the participant
   * @param last         last name of the participant
   * @param organization organization in which the participant belongs
   */
  public Participant(Integer id, String first, String last, String organization) {
    setDatabaseId(id);
    mFirstName = first;
    mLastName = last;
    mOrganization = organization;
  }

  /**
   * Constructor
   * 
   * @param id id of the participant from the database table
   */
  public Participant(Integer id) {
    setDatabaseId(id);
    fetchPersistedObject();
  }

  /**
   * setOrganization - allows participant to change organizations
   * 
   * @param organization organization in which the participant belongs
   */
  public void setOrganization(String organization) {
    mOrganization = organization;
  }

  /**
   * getFirstName
   * 
   * @return first name
   */
  public String getFirstName() {
    return mFirstName;
  }

  /**
   * getLastName
   * 
   * @return last name
   */
  public String getLastName() {
    return mLastName;
  }

  /**
   * getOrganization
   * 
   * @return organization
   */
  public String getOrganization() {
    return mOrganization;
  }

  @Override
  public boolean equals(Object o) {
    // self check
    if (this == o)
      return true;
    // null check
    if (o == null)
      return false;
    // type check and cast
    if (getClass() != o.getClass())
      return false;
    Participant player = (Participant) o;
    // field comparison
    return mFirstName.equals(player.mFirstName) && mLastName.equals(player.mLastName)
        && mOrganization.equals(player.mOrganization);
  }

  @Override
  public boolean updatePersistedObject() {

    Connection connection;
    try {
      connection = AdHocTournamentDB.getConnection();

      String updateParticipant = "update Participant set lastName = ?, firstName = ?, organization = ? where participantId = ?;";
      PreparedStatement preparedStatement = connection.prepareStatement(updateParticipant);

      preparedStatement.setString(1, this.mLastName);
      preparedStatement.setString(2, this.mFirstName);
      preparedStatement.setString(3, this.mOrganization);
      preparedStatement.setLong(4, getDatabaseId());

      int rowsAffected = preparedStatement.executeUpdate();
      if (rowsAffected != 1) {
        // id was not valid
        removePersistence();
        return false;
      }
    } catch (ClassNotFoundException e) {
      // update failed
      return false;
    } catch (SQLException e) {
      // update failed
      return false;
    }

    return true;
  }

  @Override
  public boolean addPersistedObject() {

    Connection connection;
    try {
      connection = AdHocTournamentDB.getConnection();

      String addParticipant = "insert into Participant (firstName, lastName, organization) values (?,?,?);";
      PreparedStatement preparedStatement1 = connection.prepareStatement(addParticipant);

      preparedStatement1.setString(1, this.mFirstName);
      preparedStatement1.setString(2, this.mLastName);
      preparedStatement1.setString(3, this.mOrganization);

      int rowsAffected = preparedStatement1.executeUpdate();
      if (rowsAffected == 1) {

        String sql = "select participantId from Participant where firstName=? and lastName=? and organization=?";
        PreparedStatement preparedStatement2 = connection.prepareStatement(sql);

        preparedStatement2.setString(1, this.mFirstName);
        preparedStatement2.setString(2, this.mLastName);
        preparedStatement2.setString(3, this.mOrganization);

        ResultSet result = preparedStatement2.executeQuery();
        if (result.next()) {
          Integer id = result.getInt(1);
          setDatabaseId(id);
        } else {
          // not found in the database
          removePersistence();
          return false;
        }
      } else {
        return false;
      }
    } catch (ClassNotFoundException e) {
      // insert failed
      return false;
    } catch (SQLException e) {
      // insert failed
      return false;
    }

    return true;
  }

  /**
   * fetch - queries the database for participant details based on id
   */
  @Override
  public boolean fetchPersistedObject() {

    // only try to fetch if it has been persisted
    if (!this.isNotPersisted()) {
      Connection connection;
      try {
        connection = AdHocTournamentDB.getConnection();
        String sql = "select firstName, lastName, organization from Participant where participantId = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);

        preparedStatement.setLong(1, getDatabaseId());

        ResultSet result = preparedStatement.executeQuery();
        if (result.next()) {
          this.mFirstName = result.getString(1);
          this.mLastName = result.getString(2);
          this.mOrganization = result.getString(3);
        } else {
          // not found in the database so remove persistence
          removePersistence();
          return false;
        }
      } catch (ClassNotFoundException e) {
        // fetch failed; we don't know if the id is good or not
        return false;
      } catch (SQLException e) {
        // fetch failed; we don't know if the id is good or not
        return false;
      }
    } else {
      // it wasn't persisted yet
      return false;
    }
    return true;
  }

  /**
   * delete - removes the object from the database
   * 
   * @return status of operation success
   */
  @Override
  public boolean deletePersistedObject() {
    if (getDatabaseId() != null) {

      Connection connection;
      try {
        connection = AdHocTournamentDB.getConnection();

        String deleteParticipant = "delete from Participant where id = ?";
        PreparedStatement preparedStatement1 = connection.prepareStatement(deleteParticipant);

        preparedStatement1.setLong(1, getDatabaseId());

        int rowsAffected = preparedStatement1.executeUpdate();
        if (rowsAffected == 1) {
          // deleted from the database so remove persistence
          removePersistence();
        }

      } catch (ClassNotFoundException e) {
        // delete failed
        return false;
      } catch (SQLException e) {
        // delete failed
        return false;
      }
    }
    return true;

  }

}
