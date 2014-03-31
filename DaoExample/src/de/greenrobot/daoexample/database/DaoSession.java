package de.greenrobot.daoexample.database;

import android.database.sqlite.SQLiteDatabase;

import java.util.Map;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.AbstractDaoSession;
import de.greenrobot.dao.identityscope.IdentityScopeType;
import de.greenrobot.dao.internal.DaoConfig;

import de.greenrobot.daoexample.database.SyncBase;
import de.greenrobot.daoexample.database.BaseState;
import de.greenrobot.daoexample.database.Notes;
import de.greenrobot.daoexample.database.NoteType;
import de.greenrobot.daoexample.database.Customers;
import de.greenrobot.daoexample.database.Order;

import de.greenrobot.daoexample.database.SyncBaseDao;
import de.greenrobot.daoexample.database.NotesDao;
import de.greenrobot.daoexample.database.CustomersDao;
import de.greenrobot.daoexample.database.OrderDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see de.greenrobot.dao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig syncBaseDaoConfig;
    private final DaoConfig notesDaoConfig;
    private final DaoConfig customersDaoConfig;
    private final DaoConfig orderDaoConfig;

    private final SyncBaseDao syncBaseDao;
    private final NotesDao notesDao;
    private final CustomersDao customersDao;
    private final OrderDao orderDao;

    public DaoSession(SQLiteDatabase db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        syncBaseDaoConfig = daoConfigMap.get(SyncBaseDao.class).clone();
        syncBaseDaoConfig.initIdentityScope(type);

        notesDaoConfig = daoConfigMap.get(NotesDao.class).clone();
        notesDaoConfig.initIdentityScope(type);

        customersDaoConfig = daoConfigMap.get(CustomersDao.class).clone();
        customersDaoConfig.initIdentityScope(type);

        orderDaoConfig = daoConfigMap.get(OrderDao.class).clone();
        orderDaoConfig.initIdentityScope(type);

        syncBaseDao = new SyncBaseDao(syncBaseDaoConfig, this);
        notesDao = new NotesDao(notesDaoConfig, this);
        customersDao = new CustomersDao(customersDaoConfig, this);
        orderDao = new OrderDao(orderDaoConfig, this);

        registerDao(SyncBase.class, syncBaseDao);
        registerDao(Notes.class, notesDao);
        registerDao(Customers.class, customersDao);
        registerDao(Order.class, orderDao);
    }
    
    public void clear() {
        syncBaseDaoConfig.getIdentityScope().clear();
        notesDaoConfig.getIdentityScope().clear();
        customersDaoConfig.getIdentityScope().clear();
        orderDaoConfig.getIdentityScope().clear();
    }

    public SyncBaseDao getSyncBaseDao() {
        return syncBaseDao;
    }

    public NotesDao getNotesDao() {
        return notesDao;
    }

    public CustomersDao getCustomersDao() {
        return customersDao;
    }

    public OrderDao getOrderDao() {
        return orderDao;
    }

}
