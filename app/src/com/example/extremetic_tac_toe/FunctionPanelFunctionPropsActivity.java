
package com.example.extremetic_tac_toe;

import org.andengine.engine.options.EngineOptions;
import org.andengine.entity.scene.Scene;
import org.andengine.ui.activity.SimpleLayoutGameActivity;

import com.example.extremetic_tac_toe.R;
import com.larswerkman.holocolorpicker.ColorPicker;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class FunctionPanelFunctionPropsActivity extends Activity  {

	TextView functionEquation;
	Function function;
	Button button;
	ColorPicker colorpick;
	TextView propMonotonic;
	TextView propIsInjection;
	TextView propParity;
	TextView propRoots;
	TextView propFixedPoints;
	ProgressDialog progressDialog;
	
	
	private class LoadViewTask extends AsyncTask<Void, Integer, Void> {  
        @Override  
        protected void onPreExecute() {  
            /*progressDialog = new ProgressDialog(FunctionPanelFunctionProps.this);  
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);  
            progressDialog.setTitle("Loading...");  
            progressDialog.setMessage("Loading function data...");    
            progressDialog.setCancelable(false);  
            progressDialog.setIndeterminate(false);  
            progressDialog.setMax(100);  
            progressDialog.setProgress(0);  
            progressDialog.show();*/
        	progressDialog = ProgressDialog.show(FunctionPanelFunctionPropsActivity.this,"Loading...", "Loading function data, please wait...", false, false);
        	progressDialog.show();
        }
                  
        @Override  
        protected Void doInBackground(Void... params)  
        {  
        	try{
        		FunctionPanelFunctionPropsActivity.this.toExecute();
        	} catch(Throwable t) {
        		t.printStackTrace();
        	}
        	return null;  
        }  
          
        @Override  
        protected void onProgressUpdate(Integer... values) {    
            progressDialog.setProgress(values[0]);  
        }  
  
        @Override  
        protected void onPostExecute(Void result){
            progressDialog.dismiss();  
        }  
    }  
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		  super.onCreate(savedInstanceState);
	      setContentView(R.layout.function_panel_function_props);
	      function = Core.functions.get(Core.toEditIndex);
	      propMonotonic = (TextView) findViewById(R.id.function_panel_function_props_monotonic_value);
	      propIsInjection = (TextView) findViewById(R.id.function_panel_function_props_injectionf_value);
	      propParity = (TextView) findViewById(R.id.function_panel_function_props_parity_value);
	      propRoots = (TextView) findViewById(R.id.function_panel_function_props_roots_value);
	      propFixedPoints = (TextView) findViewById(R.id.function_panel_function_props_fixed_points_value);
	      functionEquation = (TextView) findViewById(R.id.function_panel_function_prop_equation);
	      button = (Button) findViewById(R.id.function_panel_function_prop_back_button);
	      button.setOnClickListener(new View.OnClickListener() {
	            public void onClick(View v) {
	                finish();
	            }
	      });
	      functionEquation.setText("...");
	      propIsInjection.setText("...");
	      propParity.setText("...");
	      propMonotonic.setText("...");
	        
	      if(!function.isGood()) {
	    		new AlertDialog.Builder(FunctionPanelFunctionPropsActivity.this)
        	    .setTitle("Invalid expression")
        	    .setMessage("Cannot check properties of the function, because it's uncompletted or invalid.")
        	    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
        	        public void onClick(DialogInterface dialog, int which) { 
        	        	FunctionPanelFunctionPropsActivity.this.finish();
        	        }
        	     })
        	    .setIcon(android.R.drawable.ic_dialog_alert)
        	    .show();
	    		return;
	      }
	      
	      new LoadViewTask().execute();    
	}
	
	
    public void toExecute() {
        String textBuffer = "";
        
        Function funct = function;
        FunctionProperties fprops = funct.getProperties();
        
        final int resultMonotonicity = fprops.monotonicity;
		switch(resultMonotonicity) {
			case Function.FUNCTION_TYPE_CONSTANT: textBuffer = "Constant"; break;
			case Function.FUNCTION_TYPE_INCREASING: textBuffer = "Increasing"; break;
			case Function.FUNCTION_TYPE_DECREASING: textBuffer = "Decreasing"; break;
			case Function.FUNCTION_TYPE_SOFTLY_INCREASING: textBuffer = "Softly increasing"; break;
			case Function.FUNCTION_TYPE_SOFTLY_DECREASING: textBuffer = "Softly decreasing"; break;
			default: textBuffer = "Not monotonic"; break;
		}
		final String text_propMonotonic = new String(textBuffer);
		
		if(resultMonotonicity==Function.FUNCTION_TYPE_INCREASING||resultMonotonicity==Function.FUNCTION_TYPE_DECREASING) {
			textBuffer = "Yes";
		} else {
			textBuffer = "No";
		}
		final String text_propIsInjection = new String(textBuffer);
		
		final int resultParity = fprops.parity;
		switch(resultParity) {
			case Function.FUNCTION_TYPE_NOT_PARITY_NOT_NOTPARITY: textBuffer = "Not parity\nnor not parity"; break;
			case Function.FUNCTION_TYPE_PARITY: textBuffer = "Parity"; break;
			case Function.FUNCTION_TYPE_NOTPARITY: textBuffer = "Not parity"; break;
			case Function.FUNCTION_TYPE_PARITY_AND_NOTPARITY: textBuffer = "Parity and\nnot parity"; break;
			default: textBuffer="Not parity\nnor not parity"; break;
		}
		final String text_propParity = new String(textBuffer);	
		
		textBuffer = "{ ";
		double temp[] = (fprops.roots);
		int len = temp.length;
		for(int it=0;it<len;++it) {
			textBuffer = textBuffer.concat(((Object)DoubleComparator.roundDouble(temp[it],3)).toString());
			if(it!=len-1) {
				textBuffer = textBuffer.concat(",\n");
			}
		}
		textBuffer = textBuffer.concat(" }");
		final String text_propRoots = new String(textBuffer);
		
		
		textBuffer = "{ ";
		/*temp = (fprops.fixedPoints);
		len = temp.length;
		for(int it=0;it<len;++it) {
			textBuffer = textBuffer.concat(((Object)DoubleComparator.roundDouble(temp[it],3)).toString());
			if(it!=len-1) {
				textBuffer = textBuffer.concat(",\n");
			}
		}*/
		textBuffer = textBuffer.concat(" }");
		final String text_propFixedPoints = new String(textBuffer);
		
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				propMonotonic.setText(text_propMonotonic);
				propIsInjection.setText(text_propIsInjection);
				propParity.setText(text_propParity);
				propRoots.setText(text_propRoots);
				propFixedPoints.setText(text_propFixedPoints);
				functionEquation.setText(function.eqaution);
			}
		});

	}
	
}

