package com.cthos.pfpt;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.cthos.pfpt.equipment.ObjectBonus;

public class AddSlottedItemActivity extends Activity {
    public static final int ADD_BONUS_DIALOG = 1;

    public static String slotLocation = "Armor";

    public long characterId;

    public String theBonusAmount = "0";

    public String toWhatValue;

    public String bonusTypeValue;

    protected ArrayList<ObjectBonus> _bonuses = new ArrayList<ObjectBonus>();

    protected ArrayList<String> _bonusTexts = new ArrayList<String>();

    protected ArrayAdapter<String> _slotListAdapter;

    protected ContentResolver cr;

    protected ContentValues val;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_slotted_item);

        long charId = getIntent().getLongExtra("characterId", 0);
        characterId = charId;

        _initSpinner();
        _initButton();

        _slotListAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, _bonusTexts);

        ListView slotList = (ListView) findViewById(R.id.add_slotted_item_bonus_list);
        slotList.setAdapter(_slotListAdapter);
    }

    private OnClickListener handleAddBonusClick = new OnClickListener() {
        public void onClick(View v) {
            Activity act = (Activity) v.getContext();
            act.showDialog(ADD_BONUS_DIALOG);
        }
    };

    private OnClickListener handleCreateButtonClick = new OnClickListener() {
        public void onClick(View v) {
            cr = getContentResolver();

            Activity ac = (Activity) v.getContext();
            EditText et = (EditText) ac.findViewById(R.id.slotted_item_name);

            JSONArray bonusesJson = new JSONArray();
            JSONObject jsonBonus;
            ObjectBonus ob;

            Iterator<ObjectBonus> it = _bonuses.iterator();
            while (it.hasNext()) {
                ob = it.next();

                jsonBonus = new JSONObject();

                try {
                    jsonBonus.put("name", ob.name);
                    jsonBonus.put("toWhat", ob.toWhat);
                    jsonBonus.put("howMuch", ob.howMuch);
                } catch (Exception e) {

                }


                bonusesJson.put(jsonBonus);
            }

            val = new ContentValues();
            val.put("name", et.getText().toString());
            val.put("location", slotLocation);
            val.put("character_id", characterId);
            val.put("bonuses", bonusesJson.toString());

            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... args) {
                    cr.insert(Uri.parse("content://com.cthos.pfpt.core.slotteditemprovider/slotted_item"), val);
                    return null;
                }
            }.execute();

            Intent i = new Intent();
            i.setClassName("com.cthos.pfpt", "com.cthos.pfpt.InventoryActivity");
            i.putExtra("characterId", characterId);
            startActivity(i);
        }
    };

    protected void _initButton() {
        Button addBonus = (Button) findViewById(R.id.slotted_item_add_bonus_button);
        addBonus.setOnClickListener(handleAddBonusClick);

        Button createItem = (Button) findViewById(R.id.slotted_item_create_button);
        createItem.setOnClickListener(handleCreateButtonClick);
    }

    protected void _initSpinner() {
        ArrayAdapter<CharSequence> slotsAdapter = ArrayAdapter.createFromResource(
                this, R.array.slotted_items_slots_array, android.R.layout.simple_spinner_item);
        slotsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner slots = (Spinner) findViewById(R.id.slotted_item_slot_spinner);
        slots.setAdapter(slotsAdapter);
        slots.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                AddSlottedItemActivity.slotLocation = parent.getItemAtPosition(pos).toString();
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    /**
     * Handled when the dialog is set to be summoned, and tells
     * the application to do setup required for the
     * Add Bonus dialog.
     *
     * @param int did
     */
    protected Dialog onCreateDialog(int did) {
        AlertDialog dialog;

        switch (did) {
            case ADD_BONUS_DIALOG:
            default:
                dialog = _initAddBonusDialog();
                break;
        }

        return dialog;
    }

    /**
     * Handles building out the add bonus type modal.
     *
     * @return AlertDialog
     */
    protected AlertDialog _initAddBonusDialog() {
        LayoutInflater inflater = LayoutInflater.from(this);
        final View custom_view = inflater.inflate(R.layout.add_bonus_dialog, null);

        ArrayAdapter<CharSequence> bonusesAdapter = ArrayAdapter.createFromResource(
                this, R.array.bonus_types_array, android.R.layout.simple_spinner_item);
        bonusesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        final Spinner bonusSpinner = (Spinner) custom_view.findViewById(R.id.add_bonus_bonus_type_spinner);
        bonusSpinner.setAdapter(bonusesAdapter);

        bonusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                bonusTypeValue = parent.getItemAtPosition(pos).toString();
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        ArrayAdapter<CharSequence> toWhatAdapter = ArrayAdapter.createFromResource(
                this, R.array.apply_bonus_to_array, android.R.layout.simple_spinner_item);
        toWhatAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        final Spinner toWhatSpinner = (Spinner) custom_view.findViewById(R.id.add_bonus_to_what_spinner);
        toWhatSpinner.setAdapter(toWhatAdapter);

        toWhatSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                toWhatValue = parent.getItemAtPosition(pos).toString();
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        final EditText bonusAmount = (EditText) custom_view.findViewById(R.id.add_bonus_bonus_amount);

        AlertDialog dialog = new AlertDialog.Builder(this).setTitle("Add Bonus")
                .setView(custom_view)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        theBonusAmount = bonusAmount.getText().toString();
                        handleBonusAdded();

                        dialog.cancel();
                    }
                })
                .setNegativeButton("Cancel", null).create();

        return dialog;
    }

    /**
     * Adds the bonus to the Bonus holding area.
     */
    public void handleBonusAdded() {
        Number mucho;
        try {
            mucho = NumberFormat.getInstance(Locale.US).parse(theBonusAmount);
            ObjectBonus ob = new ObjectBonus(bonusTypeValue, toWhatValue, mucho);
            _bonuses.add(ob);

            String bonusText = bonusTypeValue + " (" + toWhatValue + ") (" + mucho.toString() + ")";
            _bonusTexts.add(bonusText);

            _slotListAdapter.notifyDataSetInvalidated();
        } catch (ParseException e) {
            Log.d("Numeric Parse Error", e.getMessage());
        }
    }
}
