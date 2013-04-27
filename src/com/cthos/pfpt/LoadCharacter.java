package com.cthos.pfpt;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.ContentProvider;
import android.content.ContentProviderClient;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class LoadCharacter extends ListActivity {
    private static final String[] PROJECTION = new String[]{
            "_id",
            "name",
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        if (intent.getData() == null) {
            intent.setData(Uri.parse("content://com.cthos.pfpt.core/character"));
        }

        Cursor cursor = managedQuery(
                intent.getData(),
                PROJECTION,
                null,
                null,
                "_id ASC"
        );

        startManagingCursor(cursor);

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.load_character_item, cursor,
                new String[]{"name"}, new int[]{android.R.id.text1});
        setListAdapter(adapter);

        registerForContextMenu(getListView());
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Intent i = new Intent();
        i.setClassName("com.cthos.pfpt", "com.cthos.pfpt.ViewCharacter");
        i.putExtra("characterId", id);
        startActivity(i);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.load_character_context, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info;
        info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        long id = getListAdapter().getItemId(info.position);
        Log.d("", "id = " + id);

        Uri editUri = ContentUris.withAppendedId(Uri.parse("content://com.cthos.pfpt.core/character"), id);

        ContentProviderClient client = getContentResolver().acquireContentProviderClient(editUri);
        ContentProvider provider = client.getLocalContentProvider();

        switch (item.getItemId()) {
            case R.id.edit_character:

                break;
            case R.id.delete:
                provider.delete(editUri, null, null);
                break;
        }

        return true;
    }
}
