package com.example.extremetic_tac_toe;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class FunctionPanelEditLuaFunctionActivity extends Activity {

	Button save_button;
	Button abort_button;
	EditText name;
	EditText code;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.function_panel_edit_lua_function_layout);
	        
		name = (EditText) findViewById(R.id.function_panel_edit_lua_function_name);
		code = (EditText) findViewById(R.id.function_panel_edit_lua_function_code);
		
		name.setText(Core.functions.get(Core.toEditIndex).lua_name);
		code.setText(Core.functions.get(Core.toEditIndex).eqaution);
		
		abort_button = (Button) findViewById(R.id.function_panel_edit_lua_function_abort_button);
	    abort_button.setOnClickListener(new View.OnClickListener() {
	        public void onClick(View v) {
	        	FunctionPanelActivity.entryCanBeClicked = true;
	        	finish();
	        }
	    });
	    
	    save_button = (Button) findViewById(R.id.function_panel_edit_lua_function_save_button);
	    save_button.setOnClickListener(new View.OnClickListener() {
	        public synchronized void onClick(View v) {
	        	Core.functions.get(Core.toEditIndex).eqaution = code.getText().toString();
	        	Core.functions.get(Core.toEditIndex).lua_name = name.getText().toString();
	        	FunctionPanelActivity.entryCanBeClicked = true;
	        	finish();
	        }
	    });
	    
	    
	}

}
