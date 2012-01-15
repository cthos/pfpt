package com.cthos.util;

import java.util.HashMap;

import android.util.Log;

/**
 * Simple static registry pattern implementation, so
 * that I can keep data persistent in memory, but
 * not across application loads.
 * 
 * @author cthos <daginus@gmail.com>
 */
public class Registry
{
	protected HashMap<String, Object> values;
	
	protected static Registry reg = null;
	
	public Registry()
	{
		values = new HashMap<String, Object>();
	}
	
	public static Registry getInstance()
	{
		if (Registry.reg == null) {
			Registry.reg = new Registry();
		}
		
		return Registry.reg;
	}
	
	public Object get(String key)
	{
		return values.get(key);
	}
	
	public void set(String key, Object value)
	{
		Log.d("Registry Set", value.toString());
		values.put(key, value);
	}
}
