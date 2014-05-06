package de.greenrobot.daoexample.database;

import de.greenrobot.daoexample.database.Customers.OrderType;
import de.greenrobot.dao.DaoException;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table ORDERS.
 */
public class Order extends SyncBase  {

    private transient Long syncBaseId;
    private java.util.Date date;
    private transient Long id;
    private OrderType type;
    private transient long customerId;

    /** Used to resolve relations */
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    private transient OrderDao myDao;
    private Customers customers;
    private Long customers__resolvedKey;

    public Order() {
    }

    public Order(Long id) {
        this.id = id;
        setDerivedEntityType(getClass().getCanonicalName());
    }

    Order(Long syncBaseId, java.util.Date date, Long id, OrderType type, long customerId) {
        this.syncBaseId = syncBaseId;
        this.date = date;
        this.id = id;
        this.type = type;
        this.customerId = customerId;
    }

    public Order(java.util.Date date, Long id, OrderType type, long customerId) {
        this.date = date;
        this.id = id;
        this.type = type;
        this.customerId = customerId;
        setDerivedEntityType(getClass().getCanonicalName());
    }

    /** called by internal mechanisms, do not call yourself. */
    @Override
    public void __setDaoSession(DaoSession daoSession) {
        super.__setDaoSession(daoSession);
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getOrderDao() : null;
    }

    public Long getSyncBaseId() {
        return syncBaseId;
    }

    public void setSyncBaseId(Long syncBaseId) {
        this.syncBaseId = syncBaseId;
    }

    public java.util.Date getDate() {
        return date;
    }

    public void setDate(java.util.Date date) {
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OrderType getType() {
        return type;
    }

    public void setType(OrderType type) {
        this.type = type;
    }

    public long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }

    /** To-one relationship, resolved on first access. */
    public Customers getCustomers() {
        long __key = this.customerId;
        if (customers__resolvedKey == null || !customers__resolvedKey.equals(__key)) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            CustomersDao targetDao = daoSession.getCustomersDao();
            Customers customersNew = targetDao.load(__key);
            synchronized (this) {
                customers = customersNew;
            	customers__resolvedKey = __key;
            }
        }
        return customers;
    }

    public void setCustomers(Customers customers) {
        if (customers == null) {
            throw new DaoException("To-one property 'customerId' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.customers = customers;
            customerId = customers.getId();
            customers__resolvedKey = customerId;
        }
    }

    /** Convenient call for {@link AbstractDao#delete(Object)}. Entity must attached to an entity context. */
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.delete(this);
    }

    /** Convenient call for {@link AbstractDao#update(Object)}. Entity must attached to an entity context. */
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.update(this);
    }

    /** Convenient call for {@link AbstractDao#refresh(Object)}. Entity must attached to an entity context. */
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.refresh(this);
    }

}