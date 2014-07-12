package de.greenrobot.daotest.sync;

import de.greenrobot.dao.sync.SyncUri;
import de.greenrobot.dao.test.DbTest;

/**
 * Created by saulhoward on 7/11/14.
 */
public class SyncUriTest extends DbTest {

    public void testSyncUri() {
        SyncUri uri = new SyncUri.Builder()
                .pluralizePathNames()
                .appendClass(JournalEntry.class)
                .appendId("12345")
                .build();
        assertEquals("JournalEntries/12345", uri.toString());

        uri = new SyncUri.Builder()
                .pluralizePathNames()
                .appendObject(JournalEntry.class, "12345")
                .appendPagination(2, 10)
                .build();
        assertEquals("JournalEntries/12345?pageNumber=2&itemsPerPage=10", uri.toString());

        uri = new SyncUri.Builder()
                .pluralizePathNames()
                .appendObject(JournalEntry.class, "12345")
                .appendPagination(2, 10)
                .appendInclude("image")
                .appendInclude("tag")
                .build();
        assertEquals("JournalEntries/12345?pageNumber=2&itemsPerPage=10&include=image,tag", uri.toString());
    }

    public static class JournalEntry {

    }
}
