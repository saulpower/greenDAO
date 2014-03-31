package de.greenrobot.dao.sync;

/**
 * Created by saulhoward on 3/31/14.
 */
public abstract class GreenSyncBase {

    public abstract String getExternalId();
    public abstract void setExternalId(String externalId);
    public abstract void clean();


}
