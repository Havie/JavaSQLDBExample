package cs358.tournament.model;

public abstract class PersistableObject implements Persistable {

  protected Integer mId;
  private boolean mExistsInDB;

  public PersistableObject() {
    // set as unsaved in the database as the default
    mId = null;
    mExistsInDB = false;
  }

  /**
   * persist - updates or adds participant details to the database as needed
   */
  @Override
  final public boolean persist() {

    // add if it has never been saved before
    // then set the mExistsInDB based on the success of the persist calls
    if (this.isNotPersisted()) {
      mExistsInDB = addPersistedObject();
    } else {
      mExistsInDB = updatePersistedObject();
    }

    if (!mExistsInDB) {
      mId = null;
    }

    return mExistsInDB;
  }

  @Override
  final public boolean isNotPersisted() {
    return !this.mExistsInDB;
  }

  @Override
  final public void removePersistence() {
    mId = null;
    mExistsInDB = false;
  }

  /**
   * setDatabaseId - set the database primary key id on the object
   * 
   * @param primary key id
   */
  @Override
  final public void setDatabaseId(Integer id) {
    // assume the user has a valid id
    if (id != null) {
      mExistsInDB = true;
    }
    mId = id;
  };

  /**
   * setDatabaseId - set the database primary key id on the object
   * 
   * @param primary key id
   * @return
   */
  @Override
  final public Integer getDatabaseId() {
    return mId;
  };

  @Override
  abstract public boolean updatePersistedObject();

  @Override
  abstract public boolean addPersistedObject();

  @Override
  abstract public boolean fetchPersistedObject();

  @Override
  abstract public boolean deletePersistedObject();

}
