package de.greenrobot.daoexample.database;

import java.util.List;
import de.greenrobot.dao.sync.GreenSync;
import com.google.gson.reflect.TypeToken;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import de.greenrobot.daoexample.database.Notes;
import de.greenrobot.daoexample.database.NoteType;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table NOTES.
*/
public class NotesDao extends AbstractDao<Notes, Long> {

    public static final String TABLENAME = "NOTES";

    /**
     * Properties of entity Notes.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property SyncBaseId = new Property(0, Long.class, "syncBaseId", false, "SYNC_BASE_ID");
        public final static Property Name = new Property(1, String.class, "name", false, "NAME");
        public final static Property Comment = new Property(2, String.class, "comment", false, "COMMENT");
        public final static Property Id = new Property(3, Long.class, "id", true, "_id");
        public final static Property Type = new Property(4, NoteType.class, "type", false, "TYPE");
    };

    private DaoSession daoSession;

    public NotesDao(DaoConfig config) {
        super(config);
    }
    
    public NotesDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'NOTES' (" + //
                "'SYNC_BASE_ID' INTEGER REFERENCES 'SYNC_BASE'('SYNC_BASE_ID') ," + // 0: syncBaseId
                "'NAME' TEXT NOT NULL ," + // 1: name
                "'COMMENT' TEXT," + // 2: comment
                "'_id' INTEGER PRIMARY KEY ," + // 3: id
                "'TYPE' INTEGER);"); // 4: type
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'NOTES'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, Notes entity) {
        stmt.clearBindings();
 
        Long syncBaseId = entity.getSyncBaseId();
        if (syncBaseId != null) {
            stmt.bindLong(1, syncBaseId);
        }
        stmt.bindString(2, entity.getName());
 
        String comment = entity.getComment();
        if (comment != null) {
            stmt.bindString(3, comment);
        }
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(4, id);
        }
 
        NoteType type = entity.getType();
        if (type != null) {
            stmt.bindLong(5, type.getValue());
        }
    }

    @Override
    protected void attachEntity(Notes entity) {
        super.attachEntity(entity);
        entity.__setDaoSession(daoSession);
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 3) ? null : cursor.getLong(offset + 3);
    }    

    /** @inheritdoc */
    @Override
    public Notes readEntity(Cursor cursor, int offset) {
        Notes entity = new Notes( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // syncBaseId
            cursor.getString(offset + 1), // name
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // comment
            cursor.isNull(offset + 3) ? null : cursor.getLong(offset + 3), // id
            cursor.isNull(offset + 4) ? null : NoteType.fromInt(cursor.getLong(offset + 4)) // type
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, Notes entity, int offset) {
        entity.setSyncBaseId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setName(cursor.getString(offset + 1));
        entity.setComment(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setId(cursor.isNull(offset + 3) ? null : cursor.getLong(offset + 3));
        entity.setType(cursor.isNull(offset + 4) ? null : NoteType.fromInt(cursor.getLong(offset + 4)));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(Notes entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(Notes entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
    @Override
    protected void onPreInsertEntity(Notes entity) {
        entity.insertBase(daoSession.getSyncBaseDao());
        entity.setSyncBaseId(entity.getSyncBaseId());
    }

    @Override
    protected void onPreLoadEntity(Notes entity) {
        entity.loadBase(daoSession.getSyncBaseDao(), entity.getSyncBaseId());
    }

    @Override
    protected void onPreRefreshEntity(Notes entity) {
        entity.loadBase(daoSession.getSyncBaseDao(), entity.getSyncBaseId());
    }

    @Override
    protected void onPreUpdateEntity(Notes entity) {
        entity.updateBase(daoSession.getSyncBaseDao());
    }

    @Override
    protected void onPreDeleteEntity(Notes entity) {
        entity.deleteBase(daoSession.getSyncBaseDao());
    }

    static {
        GreenSync.registerListTypeToken("Notes", new TypeToken<List<Notes>>(){}.getType());
        GreenSync.registerTypeToken("Notes", Notes.class);
    }

}
