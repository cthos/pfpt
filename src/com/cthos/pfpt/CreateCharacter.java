package com.cthos.pfpt;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

/**
 * Handles the CreateCharacter activity, which shows the
 * form to create a new character. This activity will
 * also handle the editing of characters' base attributes.
 *
 * @author Alex Ward <daginus@gmail.com>
 */
public class CreateCharacter extends Activity {
    public final int MENU_ITEM_CANCEL = 2010;

    //This is legacy, should probably kill references to it.
    public static String lvValue = "0";

    /**
     * The following hold the standard Pathfinder attributes
     */
    public static String strValue;
    public static String dexValue;
    public static String conValue;
    public static String wisValue;
    public static String intValue;
    public static String chaValue;

    protected ContentResolver cr;

    protected ContentValues val;

    private OnClickListener saveCharListener = new OnClickListener() {
        public void onClick(View v) {
            cr = getContentResolver();

            Activity ac = (Activity) v.getContext();
            EditText et = (EditText) ac.findViewById(R.id.characterName);

            val = new ContentValues();
            val.put("name", et.getText().toString());
            val.put("level", CreateCharacter.lvValue);
            val.put("strength", CreateCharacter.strValue);
            val.put("dexterity", CreateCharacter.dexValue);
            val.put("constitution", CreateCharacter.conValue);
            val.put("wisdom", CreateCharacter.wisValue);
            val.put("intelligence", CreateCharacter.intValue);
            val.put("charisma", CreateCharacter.chaValue);

            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... args) {
                    cr.insert(Uri.parse("content://com.cthos.pfpt.core/character"), val);
                    return null;
                }
            }.execute();

            Intent i = new Intent();
            i.setClassName("com.cthos.pfpt", "com.cthos.pfpt.PlayerToolkitMain");
            startActivity(i);
        }
    };

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_character);

        this.setUpSpinners();

        Button saveChar = (Button) findViewById(R.id.createCharButton);
        saveChar.setOnClickListener(this.saveCharListener);
    }

    private void setUpSpinners() {
        ArrayAdapter<CharSequence> ablAdapter = ArrayAdapter.createFromResource(
                this, R.array.ability_scores_array, android.R.layout.simple_spinner_item);
        ablAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner strSpin = (Spinner) findViewById(R.id.strengthSpinner);
        strSpin.setAdapter(ablAdapter);
        strSpin.setSelection(9);
        strSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                CreateCharacter.strValue = parent.getItemAtPosition(pos).toString();
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        Spinner dexSpinner = (Spinner) findViewById(R.id.dexteritySpinner);
        dexSpinner.setAdapter(ablAdapter);
        dexSpinner.setSelection(9);
        dexSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                CreateCharacter.dexValue = parent.getItemAtPosition(pos).toString();
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        Spinner conSpinner = (Spinner) findViewById(R.id.constitutionSpinner);
        conSpinner.setAdapter(ablAdapter);
        conSpinner.setSelection(9);
        conSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                CreateCharacter.conValue = parent.getItemAtPosition(pos).toString();
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        Spinner intSpinner = (Spinner) findViewById(R.id.intellegenceSpinner);
        intSpinner.setAdapter(ablAdapter);
        intSpinner.setSelection(9);
        intSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                CreateCharacter.intValue = parent.getItemAtPosition(pos).toString();
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        Spinner wisSpinner = (Spinner) findViewById(R.id.wisdomSpinner);
        wisSpinner.setAdapter(ablAdapter);
        wisSpinner.setSelection(9);
        wisSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                CreateCharacter.wisValue = parent.getItemAtPosition(pos).toString();
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        Spinner chaSpinner = (Spinner) findViewById(R.id.charismaSpinner);
        chaSpinner.setAdapter(ablAdapter);
        chaSpinner.setSelection(9);
        chaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                CreateCharacter.chaValue = parent.getItemAtPosition(pos).toString();
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        // This is our one standard application action -- inserting a
        // new note into the list.
        menu.add(0, MENU_ITEM_CANCEL, 0, "Cancel Character")
                .setShortcut('3', 'a')
                .setIcon(android.R.drawable.ic_menu_delete);

        Intent intent = new Intent(null, getIntent().getData());
        intent.addCategory(Intent.CATEGORY_ALTERNATIVE);
        menu.addIntentOptions(Menu.CATEGORY_ALTERNATIVE, 0, 0,
                new ComponentName(this, CreateCharacter.class), null, intent, 0, null);

        return true;
    }
}
