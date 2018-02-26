package com.example.extremetic_tac_toe;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.text.InputType;

public class SettingsPanelActivity extends PreferenceActivity
implements OnSharedPreferenceChangeListener {
	//pref_saving_clear_cache_at_startup
	private void loadLocalSettings() {
		SharedPreferences sp = getPreferenceScreen().getSharedPreferences();
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("pref_use_auto_step", FunctionDrawer.useAutoStep);
        editor.putFloat("pref_manual_step_value", (float)FunctionDrawer.stepsNumber);
        editor.putFloat("pref_ox_label_step", (float)FunctionDrawer.XAxisLabels);
        editor.putFloat("pref_oy_label_step", (float)FunctionDrawer.YAxisLabels);
        editor.putString("pref_plotting_mode", FunctionDrawer.curDisplay.getName());
        editor.putBoolean("pref_saving_open_file_when_done", Core.openFileAlwaysAfterSaving);
        editor.putBoolean("pref_saving_clear_cache_at_startup", Core.clearAlwaysAtStartup);
        editor.commit();
        
        onSharedPreferenceChanged(sp, "pref_use_auto_step");
        
        PreferenceCategory t = (PreferenceCategory)findPreference("pref_plotting_mode_advanced_settings");
		t.removeAll();
        onSharedPreferenceChanged(sp, "pref_plotting_mode");
        
        
	}
	
	private void saveLocalSettings() {
		SharedPreferences sp = getPreferenceScreen().getSharedPreferences();
		FunctionDrawer.stepsNumber = (double)sp.getFloat("pref_manual_step_value", (float)FunctionDrawer.stepsNumber);
		FunctionDrawer.useAutoStep = sp.getBoolean("pref_use_auto_step", true);
		FunctionDrawer.XAxisLabels = (int)sp.getFloat("pref_ox_label_step", 15);
		FunctionDrawer.YAxisLabels = (int)sp.getFloat("pref_oy_label_step", 15);
		Core.openFileAlwaysAfterSaving = sp.getBoolean("pref_saving_open_file_when_done", true);
		Core.clearAlwaysAtStartup = sp.getBoolean("pref_saving_clear_cache_at_startup", true);
		
		
		System.out.println(sp.getString("pref_plotting_mode", "Normal"));
		FunctionDrawer.curDisplay = FunctionDrawer.makeXYDisplay(sp.getString("pref_plotting_mode", "default"));
		System.out.println("[CurDisplay] is = "+FunctionDrawer.curDisplay.getName());
		
    	if(sp.getString("pref_plotting_mode", "default").equals("normalnprop")) {
    		FunctionDrawer.XYNormalNotProportional disp = ((FunctionDrawer.XYNormalNotProportional)FunctionDrawer.curDisplay);
        	disp.scaleX = sp.getFloat("pref_plotting_mode_ex_scalex", (float)disp.scaleX);
        	disp.scaleY = sp.getFloat("pref_plotting_mode_ex_scaley", (float)disp.scaleY);
        	disp.swapXY = sp.getBoolean("pref_plotting_mode_ex_swapxy", disp.swapXY);
        	FunctionDrawer.curDisplay = disp;
    	}
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        getPreferenceScreen().getSharedPreferences()
        	.registerOnSharedPreferenceChangeListener(this);
        loadLocalSettings();

    }
    
    @Override
    protected void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
        loadLocalSettings();
    }

    @Override
    protected void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }
    
    private String getGraphingModeDescription(String name) {
    	if(name.equals("default")) return "Normal mode";
    	if(name.equals("swap")) return "XY are swapped";
    	if(name.equals("radial")) return "Radial coordinates";
    	if(name.equals("normalnprop")) return "Advanced normal";
    	
    	return "Unknown mode";
    }
    
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals("pref_use_auto_step")) {
            this.findPreference("pref_manual_step_value").setEnabled(!sharedPreferences.getBoolean(key, false));
        }
        if(key.equals("pref_plotting_mode")) {
        	findPreference(key).setSummary(getGraphingModeDescription(sharedPreferences.getString(key, "default")));
        	if(sharedPreferences.getString(key, "default").equals("normalnprop")) {
        		PreferenceCategory t = (PreferenceCategory)findPreference("pref_plotting_mode_advanced_settings");
        		t.setEnabled(true);
        		t.setShouldDisableView(false);
        		
        		EditFloatPreference prefXAxisScale = new EditFloatPreference(this); 
        		if(FunctionDrawer.curDisplay instanceof FunctionDrawer.XYNormalNotProportional) {
        			prefXAxisScale.setDefaultValue(((Object)(((FunctionDrawer.XYNormalNotProportional)FunctionDrawer.curDisplay).scaleX)).toString());
        		} else {
        			prefXAxisScale.setDefaultValue("1.0");
        		}
        		prefXAxisScale.setKey("pref_plotting_mode_ex_scalex");
        		prefXAxisScale.setTitle("Value of scale factor on X axis.");
        		prefXAxisScale.setSummary("Set X axis linear scale.");
        		prefXAxisScale.setEnabled(true);
        		prefXAxisScale.getEditText().setInputType(InputType.TYPE_CLASS_NUMBER);
        		t.addPreference(prefXAxisScale);
        		
        		EditFloatPreference prefYAxisScale = new EditFloatPreference(this); 
        		if(FunctionDrawer.curDisplay instanceof FunctionDrawer.XYNormalNotProportional) {
        			prefYAxisScale.setDefaultValue(((Object)(((FunctionDrawer.XYNormalNotProportional)FunctionDrawer.curDisplay).scaleY)).toString());
	        	} else {
	    			prefYAxisScale.setDefaultValue("1.0");
	    		}
        		prefYAxisScale.setKey("pref_plotting_mode_ex_scaley");
        		prefYAxisScale.setTitle("Value of scale factor on Y axis.");
        		prefYAxisScale.setSummary("Set Y axis linear scale.");
        		prefYAxisScale.setEnabled(true);
        		prefYAxisScale.getEditText().setInputType(InputType.TYPE_CLASS_NUMBER);
        		t.addPreference(prefYAxisScale);
        		
        		
        		CheckBoxPreference prefXYSwap = new CheckBoxPreference(this); 
        		if(FunctionDrawer.curDisplay instanceof FunctionDrawer.XYNormalNotProportional) {
        			prefXYSwap.setDefaultValue((((FunctionDrawer.XYNormalNotProportional)FunctionDrawer.curDisplay).swapXY));
	        	} else {
	        		prefXYSwap.setDefaultValue(false);
	    		}
        		prefXYSwap.setKey("pref_plotting_mode_ex_swapxy");
        		prefXYSwap.setTitle("Rotate graph by 90 degrees.");
        		prefXYSwap.setSummary("Swap X and Y axis.");
        		prefXYSwap.setEnabled(true);
        		t.addPreference(prefXYSwap);
        		
        		//t.setEnabled(true);
        	} else {
        		PreferenceCategory t = (PreferenceCategory)findPreference("pref_plotting_mode_advanced_settings");
        		t.removeAll();
        		t.setEnabled(false);
        		t.setShouldDisableView(true);
        	}
        }
        saveLocalSettings();
    }
    
}
