/**
 * 
 */
package cs358.tournament.model;

/**
 * @author jocelyn
 *
 */
public interface Persistable {

  /**
   * persist - saves or updates the database depending upon the state of the
   * object
   * 
   * @return status of operation success
   */
  public boolean persist();

  /**
   * isNotPersisted - boolean check to see if the objects exists in the database
   * yet
   * 
   * @return status of operation success
   */
  public boolean isNotPersisted();

  /**
   * removePersistence - the object is no longer saved in the database and
   * therefore must remove persistence
   * 
   * @return status of operation success
   */
  public void removePersistence();

  /**
   * setDatabaseId - set the database primary key id on the object
   * 
   * @param primary key id
   */
  public void setDatabaseId(Integer id);

  /**
   * getDatabaseId - return the database primary key id on the object
   * 
   * @return primary key id
   */
  public Integer getDatabaseId();

  /**
   * update - update the database with the changes to the object
   * 
   * @return status of operation success
   */
  public boolean updatePersistedObject();

  /**
   * add - add the object to the database as a new row
   * 
   * @return status of operation success
   */
  public boolean addPersistedObject();

  /**
   * fetch - populate the object data from the database based on the id - will
   * overwrite any local changes
   * 
   * @return status of operation success
   */
  public boolean fetchPersistedObject();

  /**
   * delete - removes the object from the database
   * 
   * @return status of operation success
   */
  public boolean deletePersistedObject();
}
