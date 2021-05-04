package cs358.tournament.model;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;

/**
 * Tournament - a tournament with configuration settings
 * 
 * @author richardtj, Steve Datz
 *
 */

public class Tournament extends PersistableObject {

  private static Integer CURR_CODE = 1001; // Have to start above 1000 or 0001 turns into 1 in DB conversion to
                                           // mAccessCode
  public final static int MAX_TEAMS = 256;
  private final static String DEFAULT_NAME = "Tournament";

  private String mName; // name of the tournament
  private String mAccessCode; // access code of the tournament
  private int mSignupMode; // 0 = closed , 1-homogeneous, 2-maximized diversity, 3- enforced diversity,
                           // 4-Open
  private LocalDateTime mStartDate; // start date of the tournament
  private LocalDateTime mEndDate; // end date of the tournament
  private boolean mTypeIndicator; // indicates whether the tournament is a single or double elimination
                                  // tournament, [true single : false double]
  private Bracket mBracket;

  // Keys for DB queries IN ORDER matching db
  private final String mAccessCodeDB = "accessCode"; // int (id as PK)
  private final String mSignupDB = "signupMode"; // int
  private final String mBracketDB = "bracketMode"; // bool
  private final String mNameDB = "tName"; // 60 varchar
  private final String mStartDateDB = "startDate"; // sql DATE
  private final String mEndDateDB = "endDate"; // sql DATE
  private final String mMinTeamSizeDB = "minTeamSize"; // int
  private final String mMaxTeamSizeDB = "maxTeamSize"; // int
  private final String mIdealTeamSizeDB = "idealTeamSize"; // int

  private ArrayList<String> listOfLosers;

  private TeamManager mTeamManager; // team's manager

  // participants not yet assigned to a team
  private ArrayList<Participant> mUnattachedParticipants;

  /**
   * Tournament constructor
   */
  public Tournament(String name) {
    mUnattachedParticipants = new ArrayList<Participant>();

    // use a default name if a name is not provided
    if (name == null || name.isBlank()) {
      mName = DEFAULT_NAME;
    } else {
      mName = name.trim();
    }

    // generate a unique access code
    generateAccessCode();
    // default to start today and end it tomorrow
    mStartDate = LocalDate.now().atTime(0, 0);
    mEndDate = mStartDate.plusDays(1);

    mTeamManager = new TeamManager();
  }

  public Tournament(int accessCodeToFetch) {
    setDatabaseId(accessCodeToFetch);
    fetchPersistedObject();
  }

  public void setDates(LocalDateTime start, LocalDateTime end) throws Exception {

    LocalDateTime now = LocalDate.now().atTime(0, 0);
    // if not given, start it now
    if (start == null) {
      mStartDate = now;
      mEndDate = end;
    } else if (start.isAfter(now) || start.isEqual(now)) {
      mStartDate = start;
      mEndDate = end;
    } else {
      // if past start given, throw exception
      // ADDED SO WE CAN EDIT PAST TOURNAMENTS**
      mStartDate = start;
      mEndDate = end;
      System.out.println("WARNING:: START is BEFORE NOW");
      // throw new Exception("Invalid Date: START is BEFORE NOW");
    }

    // if end date is not given, end it the next day
    if (end == null) {
      mEndDate = now.plusDays(1);

      // if end is before start, throw exception
    } else if (end.isBefore(mStartDate)) {
      throw new Exception("Invalid Date: END is before START");// *************
      // mEndDate = end;
    } else {
      // mEndDate = end;
    }

  }

  /**
   * addParticipant - add a participant without a team
   * 
   * @param participant - a person who wants to be in the tournament
   */
  public void addParticipant(Participant participant) throws Exception {
    if (!mUnattachedParticipants.contains(participant)) {
      mUnattachedParticipants.add(participant);
    } else {
      throw new Exception("duplicate participant");
    }

  }

  /**
   * getUnattachedParticipants - return all participants that are not on teams yet
   * 
   * @return ArrayList<Participant>
   */
  public ArrayList<Participant> getUnattachedParticipants() {

    return mUnattachedParticipants;

  }

  /**
   * removeAllUnattachedParticipants - remove all participant without a team
   * 
   */
  public void removeAllUnattachedParticipants() {

    mUnattachedParticipants.clear();

  }

  /**
   * @return the Name
   */
  public String getName() {
    return mName;
  }

  /**
   * @return the TeamManager
   */
  public TeamManager getTeamManager() {
    return mTeamManager;
  }

  /**
   * set the Name
   */
  public void setName(String name) {
    // use a default name if a name is not provided
    if (name == null || name.isBlank()) {
      mName = DEFAULT_NAME;
    } else {
      mName = name;
    }

  }

  /**
   * @return the mAccessCode
   */
  public String getAccessCode() {
    return mAccessCode;
  }

  /**
   * @return the StartDate
   */
  public LocalDateTime getStartDate() {
    return mStartDate;
  }

  /**
   * @return the mEndDate
   */
  public LocalDateTime getEndDate() {
    return mEndDate;
  }

  /**
   *  generate a unique access code for the tournament based on next key in Database
   * @return
   */
  public String generateAccessCode() {
    try {
      Connection connection = AdHocTournamentDB.getConnection();
      // SELECT max(accessCode) FROM Tournament
      String getMaxCode = String.format("SELECT max(%s) FROM Tournament;", mAccessCodeDB);
      PreparedStatement preparedStatement = connection.prepareStatement(getMaxCode);
      ResultSet result = preparedStatement.executeQuery();
      if (result.next()) {
        this.CURR_CODE = result.getInt(1);
        // System.out.println("Got current code from Db: " + result.getInt(1));
      }
    } catch (Exception e) {
      System.out.println("Exception: " + e);
    }

    ++CURR_CODE;
    mAccessCode = CURR_CODE.toString();

    return mAccessCode;
  }

  /**
   * Converts the java localDate to the SQL date format for the database
   * @param localDate
   * @return
   */
  private Date ConvertLocalDateToDateForDB(LocalDateTime localDate) {
    ZonedDateTime zdt = ZonedDateTime.of(localDate, ZoneId.systemDefault());
    long dateLong = zdt.toInstant().toEpochMilli();
    return new Date(dateLong);
  }

  /**
   * Converts the SQL Date to the java local date
   * @param dateFromDB
   * @return
   */
  private LocalDateTime ConvertDateToLocalDateFromDB(Date dateFromDB) {
    var localDate = dateFromDB.toLocalDate();
    var year = localDate.getYear();
    var month = localDate.getMonth();
    var day = localDate.getDayOfMonth();
    LocalDateTime ld = LocalDateTime.of(year, month, day, 0, 0);
    return ld;
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
    Tournament tourney = (Tournament) o;
    // field comparison
    return this.mAccessCode == tourney.mAccessCode;
  }

  /**
   * Updates this tournament for DB
   */
  @Override
  public boolean updatePersistedObject() {
    try {
      Connection connection = AdHocTournamentDB.getConnection();

      // "update Tournament set name = ?, startDate = ? endDate = ? where accessCode =
      // ?;";
      String updateTournament = String.format(
          "update Tournament set %s = ? , %s = ?, %s = ? , %s = ? , %s = ? , %s = ? , %s = ? , %s = ?  where %s = ? ;",
          mSignupDB, mBracketDB, mNameDB, mStartDateDB, mEndDateDB, mMinTeamSizeDB, mMaxTeamSizeDB, mIdealTeamSizeDB,
          mAccessCodeDB);
      PreparedStatement preparedStatement1 = connection.prepareStatement(updateTournament);
      // Assign our vars to the prepared statement : "?"
      preparedStatement1.setInt(1, this.mSignupMode);
      preparedStatement1.setBoolean(2, this.mTypeIndicator);
      preparedStatement1.setString(3, this.mName);
      preparedStatement1.setDate(4, ConvertLocalDateToDateForDB(this.mStartDate));
      preparedStatement1.setDate(5, ConvertLocalDateToDateForDB(this.mEndDate));
      preparedStatement1.setInt(6, mTeamManager.getMinTeamSize());
      preparedStatement1.setInt(7, mTeamManager.getMaxTeamSize());
      preparedStatement1.setInt(8, mTeamManager.getIdealTeamSize());
      preparedStatement1.setString(9, this.mAccessCode);

      int rowsAffected = preparedStatement1.executeUpdate();
      if (rowsAffected == 0) {// id was not valid , [teacher had != 1 ??]
        removePersistence();
        System.out.print("No Rows Affected: Access Code:");
        System.out.println(this.mAccessCode);
        return false;
      }
    } catch (ClassNotFoundException e) {
      System.out.println(e);
      return false; // update failed
    } catch (Exception e) {
      System.out.println(e);
      return false; // update failed
    }

    return true;
  }

  /**
   * Adds a new entry for this tournament to DB
   */
  @Override
  public boolean addPersistedObject() {
    // PrintTournamentDetails();
    try {
      Connection connection = AdHocTournamentDB.getConnection();

      // Assign our stored DB table names to the : "%s"
      String addTournamentQuery = String.format(
          "insert into Tournament (%s, %s, %s, %s, %s, %s, %s, %s, %s) values (?,?,?,?,?,?,?,?,?);", mSignupDB,
          mBracketDB, mNameDB, mStartDateDB, mEndDateDB, mMinTeamSizeDB, mMaxTeamSizeDB, mIdealTeamSizeDB,
          mAccessCodeDB);
      PreparedStatement preparedStatement1 = connection.prepareStatement(addTournamentQuery);
      // Assign our vars to the prepared statement : "?"
      preparedStatement1.setInt(1, this.mSignupMode);
      preparedStatement1.setBoolean(2, this.mTypeIndicator);
      preparedStatement1.setString(3, this.mName);
      preparedStatement1.setDate(4, ConvertLocalDateToDateForDB(this.mStartDate));
      preparedStatement1.setDate(5, ConvertLocalDateToDateForDB(this.mEndDate));
      preparedStatement1.setInt(6, mTeamManager.getMinTeamSize());
      preparedStatement1.setInt(7, mTeamManager.getMaxTeamSize());
      preparedStatement1.setInt(8, mTeamManager.getIdealTeamSize());
      preparedStatement1.setString(9, this.mAccessCode);

      int rowsAffected = preparedStatement1.executeUpdate();
      if (rowsAffected == 1) {
        Integer code = Integer.parseInt(this.mAccessCode);
        setDatabaseId(code);
      } else {// not found in the database
        removePersistence();
        return false;
      }
    } catch (ClassNotFoundException e) {
      return false; // insert failed
    } catch (Exception e) {
      System.out.println("Exception : " + e);
      return false; // insert failed
    }

    return true;
  }

  /**
   * Sets this tournament to match the stored specifications in the DB for this accessCode
   */
  @Override
  public boolean fetchPersistedObject() {

    try {
      Connection connection = AdHocTournamentDB.getConnection();
      // "select mName, mStartDate, mendDate , from Tournament where accessCode =
      // 0001"
      String sqlQuery = String.format("select %s, %s, %s, %s, %s, %s, %s, %s from Tournament where %s = ? ;", mSignupDB,
          mBracketDB, mNameDB, mStartDateDB, mEndDateDB, mMinTeamSizeDB, mMaxTeamSizeDB, mIdealTeamSizeDB,
          mAccessCodeDB);
      PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);

      preparedStatement.setLong(1, getDatabaseId());

      ResultSet result = preparedStatement.executeQuery();
      if (result.next()) {
        this.mAccessCode = mId.toString();
        this.mSignupMode = result.getInt(1);
        this.mTypeIndicator = result.getBoolean(2);
        this.mName = result.getString(3);
        this.mStartDate = ConvertDateToLocalDateFromDB(result.getDate(4));
        this.mEndDate = ConvertDateToLocalDateFromDB(result.getDate(5));
        var minTeamSize = result.getInt(6);
        var maxTeamSize = result.getInt(7);
        var idealTeamSize = result.getInt(8);
        mTeamManager = new TeamManager();
        mTeamManager.setSizes(idealTeamSize, minTeamSize, maxTeamSize);

//          System.out.println("signupMode = " +mSignupMode );
//          System.out.println("bracketMode = " + mTypeIndicator );
//          System.out.println("mName = " + mName );
//          System.out.println("minTeamSize = " +minTeamSize );
//          System.out.println("maxTeamSize = " +maxTeamSize );
//          System.out.println("idealTeamSize = " +idealTeamSize );
//          System.out.println("StartDate= " + result.getDate(4)  + " ... vs: ... " + this.mStartDate + " .... vs2: ..." + ConvertLocalDateToDateForDB(this.mStartDate)) ;
//          System.out.println("mEndDate= " + result.getDate(5)  + " ... vs: ... " + this.mEndDate + " .... vs2: ..." + ConvertLocalDateToDateForDB(this.mEndDate)) ;

      } else {// not found in the database so remove persistence
        removePersistence();
        return false;
      }
    } catch (ClassNotFoundException e) {// fetch failed; we don't know if the id is good or not
      return false;
    } catch (Exception e) {// fetch failed; we don't know if the id is good or not
      System.out.println(e);
      return false;
    }

    return true;
  }

  /**
   * Deletes this tournament from the Database based on this accessCode
   */
  @Override
  public boolean deletePersistedObject() {
    if (getDatabaseId() != null) {

      Connection connection;
      try {
        connection = AdHocTournamentDB.getConnection();

        String sqlQuery = String.format("delete from Tournament where %s= ?", mAccessCodeDB);
        PreparedStatement preparedStatement1 = connection.prepareStatement(sqlQuery);

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

  /**
   * returns the type indicator for the tournament
   * @return
   */
  public boolean getTypeIndicator() {
    return mTypeIndicator;
  }

  /**
   * Sets the type indicator for this tournament
   * @param mTypeIndicator
   */
  public void setTypeIndicator(boolean mTypeIndicator) {
    this.mTypeIndicator = mTypeIndicator;
  }

  /**
   * UNFINISHED - resolves the match result based on tournament type
   * @param winner
   * @param loser
   * @throws Exception
   */
  public void resolveMatch(Team winner, Team loser) throws Exception {

    if (listOfLosers == null) {
      listOfLosers = new ArrayList<String>();
    }

    if (mTeamManager.getTeams().contains(winner) && mTeamManager.getTeams().contains(loser)) {

    } else {
      throw new Exception("One of these teams does not exist in this tournament");
    }

    if (mTypeIndicator) {

      listOfLosers.add(loser.getmTeamName());
      mTeamManager.removeTeam(loser); // remove losing team

      // advance winner in bracket, however we decide to do that, will be decided when
      // we figure out how to draw the brackets

    } else if (!mTypeIndicator) {

      if (loser.getHasLost()) {
        listOfLosers.add(loser.getmTeamName());
        mTeamManager.removeTeam(loser);
      } else {
        loser.setHasLost(true);
      }

      // advance winner somehow

    }
  }

  public Bracket getBracket() {
    return mBracket;
  }

  public void setBracket(Bracket mBracket) {
    this.mBracket = mBracket;
  }

  public void setSignUpMode(int signUpMode) {
    this.mSignupMode = signUpMode;
  }

  public int getSignUpMode() {
    return this.mSignupMode;
  }

  /**
   * A Debug method for developer debugging
   */
  private void PrintTournamentDetails() {
    System.out.println("---Trying to fux with this tournament:");
    System.out.println("Name: " + this.mName);
    System.out.println("mAccessCode: " + this.mAccessCode);
    System.out.println("mStartDate: " + this.mStartDate);
    System.out.println("mEndDate: " + this.mEndDate);
  }

}
