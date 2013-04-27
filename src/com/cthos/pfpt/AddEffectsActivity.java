package com.cthos.pfpt;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONObject;

import com.cthos.pfpt.equipment.ObjectBonus;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentValues;
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

public class AddEffectsActivity extends Activity {
    protected static final int ADD_BONUS_DIALOG = 1;

    protected static long characterId;

    protected String bonusTypeValue;
    protected String toWhatValue;
    protected String bonusAmount;

    protected ContentResolver cr;
    protected ContentValues cv;

    protected ArrayList<ObjectBonus> _bonuses = new ArrayList<ObjectBonus>();
    protected ArrayList<String> _bonusTexts = new ArrayList<String>();

    protected ArrayAdapter<String> _listAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_effects);

        long charId = getIntent().getLongExtra("characterId", 0);
        characterId = charId;

        _listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, _bonusTexts);

        ListView lv = (ListView) findViewById(R.id.add_effects_bonuses_list);
        lv.setAdapter(_listAdapter);

        _initButtons();

    }

    protected void _initButtons() {
        Button addEffectBonusButton = (Button) findViewById(R.id.add_effects_add_bonus);
        addEffectBonusButton.setOnClickListener(handleAddCliked);

        Button saveEffectButton = (Button) findViewById(R.id.add_effects_save);
        saveEffectButton.setOnClickListener(handleSaveClicked);

    }

    /**
     * Called after a bonus is added. Puts the bonus into a locally stored array
     * since it only needs to be called once the save button is clicked.
     *
     * @return void
     */
    protected void handleBonusAdded() {
        Number mucho;
        try {
            mucho = NumberFormat.getInstance(Locale.US).parse(this.bonusAmount);
            ObjectBonus ob = new ObjectBonus(bonusTypeValue, toWhatValue, mucho);
            _bonuses.add(ob);

            String bonusText = bonusTypeValue + " (" + toWhatValue + ") (" + mucho.toString() + ")";
            _bonusTexts.add(bonusText);

            _listAdapter.notifyDataSetInvalidated();
        } catch (ParseException e) {
            Log.d("Numeric Parse Error", e.getMessage());
        }
    }

    protected void handleSave() {
        cr = getContentResolver();

        EditText name = (EditText) findViewById(R.id.add_effects_name);
        EditText duration = (EditText) findViewById(R.id.add_effects_duration);
        EditText description = (EditText) findViewById(R.id.add_effects_description);

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

        cv = new ContentValues();
        cv.put("name", name.getText().toString());
        cv.put("duration", duration.getText().toString());
        cv.put("description", description.getText().toString());
        cv.put("character_id", characterId);
        cv.put("bonuses", bonusesJson.toString());

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... args) {
                cr.insert(Uri.parse("content://com.cthos.pfpt.core.activeeffectprovider/effect"), cv);
                return null;
            }
        }.execute();

        Intent i = new Intent();
        i.setClass(this, com.cthos.pfpt.ActiveEffectsActivity.class);
        i.putExtra("characterId", characterId);
        startActivity(i);
    }

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
     * Handles initing the add bonus dialog, which requires setting
     * up the spinners.
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

        final EditText bonusAmountText = (EditText) custom_view.findViewById(R.id.add_bonus_bonus_amount);

        AlertDialog dialog = new AlertDialog.Builder(this).setTitle("Add Effect Bonus")
                .setView(custom_view)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        bonusAmount = bonusAmountText.getText().toString();

                        handleBonusAdded();
                        dialog.cancel();
                    }
                })
                .setNegativeButton("Cancel", null).create();

        return dialog;
    }

    protected OnClickListener handleAddCliked = new OnClickListener() {

        public void onClick(View v) {
            Activity a = (Activity) v.getContext();
            a.showDialog(ADD_BONUS_DIALOG);
        }
    };

    protected OnClickListener handleSaveClicked = new OnClickListener() {
        public void onClick(View v) {
            handleSave();
        }
    };
}
