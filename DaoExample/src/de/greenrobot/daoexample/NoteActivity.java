/*
 * Copyright (C) 2011 Markus Junginger, greenrobot (http://greenrobot.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.greenrobot.daoexample;

import android.app.ListActivity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import de.greenrobot.dao.sync.GreenSync;
import de.greenrobot.daoexample.database.*;
import de.greenrobot.daoexample.database.DaoMaster.DevOpenHelper;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NoteActivity extends ListActivity {

    public final String TAG = getClass().getSimpleName();

    private SQLiteDatabase db;

    private EditText editText;

    private DaoMaster daoMaster;
    private DaoSession daoSession;
    private NotesDao noteDao;
    private CustomersDao customerDao;
    private List<Notes> notes;
    private List<Customers> customers;
    private MyAdapter adapter;
    private GreenSync greenSync;
    private List<Notes> demoNotes;

    public static class MyAdapter extends ArrayAdapter<Notes> {

        int mResource;
        LayoutInflater mInflater;

        public MyAdapter(Context context, int resource, List<Notes> objects) {
            super(context, resource, objects);

            mResource = resource;
            mInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View view = convertView;
            ViewHolder viewHolder;

            if (view == null) {
                view = mInflater.inflate(mResource, parent, false);
                TextView title = (TextView) view.findViewById(android.R.id.text1);
                TextView subTitle = (TextView) view.findViewById(android.R.id.text2);
                viewHolder = new ViewHolder(title, subTitle);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }

            Notes note = getItem(position);

            viewHolder.title.setText(note.getName());
            viewHolder.subTitle.setText(note.getComment());

            return view;
        }

        static class ViewHolder {

            public ViewHolder(TextView title, TextView subTitle) {
                this.title = title;
                this.subTitle = subTitle;
            }

            TextView title;
            TextView subTitle;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);

        DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "notes-db", null);
        db = helper.getWritableDatabase();

        DaoMaster.dropAllTables(db, true);
        DaoMaster.createAllTables(db, true);

        daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
        noteDao = daoSession.getNotesDao();
        customerDao = daoSession.getCustomersDao();

        greenSync = new GreenSync(daoSession, MySyncServce.get(this));

        createDemoData();

        notes = noteDao.loadAll();
        customers = customerDao.loadAll();
        toJson();

        adapter = new MyAdapter(this, android.R.layout.simple_list_item_2, notes);
        setListAdapter(adapter);

        editText = (EditText) findViewById(R.id.editTextNote);
        addUiListeners();
    }

    private void createDemoData() {

        int max = 5;

        List<Notes> demoNotes = new ArrayList<Notes>();
        List<Customers> demoCustomers = new ArrayList<Customers>();

        for (int i = 0; i < max; i++) {

            Notes note = new Notes("My Note " + i, "Comment " + i, null, NoteType.fromInt(i % 3 + 1));
            note.setCreatedOn(new Date());
            note.setUpdatedOn(new Date());
            demoNotes.add(note);

            Customers customer = new Customers("Joe Bob" + i, null);
            customer.setCreatedOn(new Date());
            customer.setUpdatedOn(new Date());
            demoCustomers.add(customer);
        }

        noteDao.insertInTx(demoNotes);
        customerDao.insertInTx(demoCustomers);

        noteDao.updateInTx(demoNotes.subList(0, max / 2));
    }

    protected void addUiListeners() {
        editText.setOnEditorActionListener(new OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    addNote();
                    return true;
                }
                return false;
            }
        });

        final View button = findViewById(R.id.buttonAdd);
        button.setEnabled(false);
        editText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                boolean enable = s.length() != 0;
                button.setEnabled(enable);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    public void onMyButtonClick(View view) {
        addNote();
    }

    private void addNote() {
        String noteText = editText.getText().toString();
        editText.setText("");

        final DateFormat df = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM);
        String comment = "Added on " + df.format(new Date());
        Notes note = new Notes(comment, noteText, null, NoteType.FRISBEE);
        note.setCreatedOn(new Date());
        note.setUpdatedOn(new Date());
        noteDao.insert(note);

        Customers customer = new Customers("Joe Bob", null);
        customerDao.insert(customer);

        notes.add(note);
        customers.add(customer);
        adapter.notifyDataSetChanged();

        toJson();
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {

        noteDao.delete(notes.get(position));

        notes.remove(position);
        adapter.notifyDataSetChanged();
    }

    private void toJson() {
        greenSync.sync();
//        Long start = System.currentTimeMillis();
//        String json = greenSync.syncBatch();
//        Log.i(TAG, "Write: " + json);
//        start = System.currentTimeMillis();
//        greenSync.processResponse(json);
//        Log.i(TAG, "Read: " + (System.currentTimeMillis() - start));
    }


}