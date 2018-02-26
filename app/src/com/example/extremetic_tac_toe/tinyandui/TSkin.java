package com.example.extremetic_tac_toe.tinyandui;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

public class TSkin {

	private Hashtable<String, String> values;
	
	public String getValue(String name) {
		return values.get(name);
	}
	
	public TSkin() {
		values = new Hashtable<String, String>();
	}
	
	public TSkin val(String key, String val) {
		values.put(key, val);
		return this;
	}
	
	public TSkin putDefaults(TSkin defaults) {
		Iterator<Hashtable.Entry<String, String>> it = defaults.values.entrySet().iterator();
		while (it.hasNext()) {
			Hashtable.Entry<String, String> entry = it.next();
			if(!this.values.containsKey(entry.getKey())) {
				this.values.put(entry.getKey(), entry.getValue());
			}
		}
		return this;
	}
	
	public void clear() {
		values.clear();
	}
	
	public void loadFrom(String data) {
		System.out.println("PArsing skin from JSON!");
		clear();
		JSONObject jobj = null;
		try {
			jobj = new JSONObject(data);
			Iterator it = jobj.keys();
			while(it.hasNext()) {
				String key = (String)it.next();
				System.out.println("JSON Entry pushed: "+key+"; "+jobj.getString(key));
				this.val(key, jobj.getString(key));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		
	}
	
	@Override
	public String toString() {
		String ret = "@TSkin{ ";
		Iterator<Hashtable.Entry<String, String>> it = values.entrySet().iterator();
		while (it.hasNext()) {
			Hashtable.Entry<String, String> entry = it.next();
			ret+=entry.getKey()+"="+entry.getValue()+", ";
		}
		return ret+"} ";
	}

}
