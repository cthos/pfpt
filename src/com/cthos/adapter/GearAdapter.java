package com.cthos.adapter;

import com.cthos.pfpt.R;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class GearAdapter extends SimpleCursorAdapter
{
	protected Context _context;
	protected Cursor _cursor;
	
	protected final int POSITION = 0;
	
	public GearAdapter(Context context, int layout, Cursor c, String[] from, int[] to) 
	{
		super(context, layout, c, from, to);
		_context = context;
		_cursor = c;
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent)
	{
		View row = super.newView(context, cursor, parent);
		return row;
	}
	
	/**
	 * Called in order to overload the view and thus the click listeners.
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		View row = super.getView(position, convertView, parent);
		
		Button minusButton = (Button) row.findViewById(R.id.gear_list_minus);
		minusButton.setTag(String.valueOf(position));
		minusButton.setOnClickListener(handleSubtractCount);
		
		Button plusButton = (Button) row.findViewById(R.id.gear_list_plus);
		plusButton.setOnClickListener(handleAddCount);
		plusButton.setTag(String.valueOf(position));
		
		row.setOnCreateContextMenuListener(null);
		
		return row;
	}
	
	/**
	 * Updates the individual item with the new value, in the
	 * Sqlite DB, for data persistance.
	 * 
	 * @param newValue
	 */
	protected void _handleItemChanged(int newValue)
	{
		int id = _cursor.getInt(0);
		
		ContentResolver cr = _context.getContentResolver();
		ContentValues cv = new ContentValues();
		cv.put("quantity", String.valueOf(newValue));
		
		cr.update(
			Uri.parse("content://com.cthos.pfpt.core.gearprovider/gear"),
			cv,
			"_id = ?",
			new String[]{String.valueOf(id)}
		);
	}
	
	private OnClickListener handleSubtractCount = new OnClickListener() {
    	public void onClick(View v)
    	{
    		LinearLayout parent = (LinearLayout) v.getParent();
    		LinearLayout grandParent = (LinearLayout) parent.getParent();
    		
    		_cursor.moveToPosition(Integer.valueOf(v.getTag().toString()));
    		
    		TextView currentValue = (TextView) grandParent.findViewById(R.id.gear_item_quantity);
    		int count = Integer.valueOf(currentValue.getText().toString());
    		
    		if (count <= 0) {
    			return;
    		}
    		
    		count--;
    		currentValue.setText(String.valueOf(count));
    		grandParent.invalidate();
    		
    		_handleItemChanged(count);
    	}
    };
    
    private OnClickListener handleAddCount = new OnClickListener() {
    	public void onClick(View v)
    	{
    		LinearLayout parent = (LinearLayout) v.getParent();
    		LinearLayout grandParent = (LinearLayout) parent.getParent();
    		
    		_cursor.moveToPosition(Integer.valueOf(v.getTag().toString()));
    		
    		TextView currentValue = (TextView) grandParent.findViewById(R.id.gear_item_quantity);
    		int count = Integer.valueOf(currentValue.getText().toString());
    		
    		count++;
    		currentValue.setText(String.valueOf(count));
    		grandParent.invalidate();
    		
    		_handleItemChanged(count);
    	}
    };
}
