package com.example.extremetic_tac_toe;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.util.Scanner;

import org.andengine.engine.options.EngineOptions;
import org.andengine.entity.scene.Scene;
import org.andengine.ui.activity.SimpleLayoutGameActivity;

import com.example.extremetic_tac_toe.R;
import com.larswerkman.holocolorpicker.ColorPicker;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnGenericMotionListener;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebSettings.RenderPriority;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

//function_panel_edit_function_equation_simple_view

public class FunctionPanelEditFunctionActivity extends Activity  {

	//TextView functionEquationInput;
	Function function;
	
	CheckBox functionEquationState;
	Button save_button;
	Button abort_button;
	WebView webview;
	String functionLatexContent = "";
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.function_panel_menu, menu);
	    return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		boolean coomonFunction = false;
		String functionBody = "";
		switch (item.getItemId()) {
	        case R.id.function_panel_edit_function_menu_common_functions_sin: 
	        	coomonFunction = true; functionBody="sin(x)"; break;
	        case R.id.function_panel_edit_function_menu_common_functions_cos:
	        	coomonFunction = true; functionBody="cos(x)"; break;
	        case R.id.function_panel_edit_function_menu_common_functions_tan:
	        	coomonFunction = true; functionBody="tan(x)"; break;
	        case R.id.function_panel_edit_function_menu_common_functions_asin: 
	        	coomonFunction = true; functionBody="asin(x)"; break;
	        case R.id.function_panel_edit_function_menu_common_functions_acos:
	        	coomonFunction = true; functionBody="acos(x)"; break;
	        case R.id.function_panel_edit_function_menu_common_functions_atan:
	        	coomonFunction = true; functionBody="atan(x)"; break;
	        case R.id.function_panel_edit_function_menu_common_functions_exp:
	        	coomonFunction = true; functionBody="exp(x)"; break;
	        case R.id.function_panel_edit_function_menu_common_functions_log2:
	        	coomonFunction = true; functionBody="log(2,x)"; break;
	        case R.id.function_panel_edit_function_menu_common_functions_log:
	        	coomonFunction = true; functionBody="log(10,x)"; break;
	        case R.id.function_panel_edit_function_menu_common_functions_ln:
	        	coomonFunction = true; functionBody="ln(x)"; break;
	        case R.id.function_panel_edit_function_menu_common_functions_abs:
	        	coomonFunction = true; functionBody="abs(x)"; break;
	        case R.id.function_panel_edit_function_menu_common_functions_pow:
	        	coomonFunction = true; functionBody="x^2"; break;
	        case R.id.function_panel_edit_function_menu_common_functions_xtower2:
	        	coomonFunction = true; functionBody="x^x"; break;
	        case R.id.function_panel_edit_function_menu_common_functions_xtower3:
	        	coomonFunction = true; functionBody="x^(x^x)"; break;
	        case R.id.function_panel_edit_function_menu_common_functions_xtower4:
	        	coomonFunction = true; functionBody="x^(x^(x^x))"; break;
	        case R.id.function_panel_edit_function_menu_common_functions_xtower5:
	        	coomonFunction = true; functionBody="x^(x^(x^(x^x)))"; break;
	        case R.id.function_panel_edit_function_menu_common_functions_x:
	        	coomonFunction = true; functionBody="x"; break;
	        case R.id.function_panel_edit_function_menu_common_functions_linear:
	        	coomonFunction = true; functionBody="x*1+1"; break;
	        case R.id.function_panel_edit_function_menu_common_functions_quadratic:
	        	coomonFunction = true; functionBody="(x^2)*1+x*1+1"; break;
	        case R.id.function_panel_edit_function_menu_common_functions_sqrt:
	        	coomonFunction = true; functionBody="sqrt(x)"; break;
	        case R.id.function_panel_edit_function_menu_common_functions_min:
	        	coomonFunction = true; functionBody="min(0,x)"; break;
	        case R.id.function_panel_edit_function_menu_common_functions_max:
	        	coomonFunction = true; functionBody="max(0,x)"; break;
	        case R.id.function_panel_edit_function_menu_common_functions_ceil:
	        	coomonFunction = true; functionBody="ceil(x)"; break;
	        case R.id.function_panel_edit_function_menu_common_functions_floor:
	        	coomonFunction = true; functionBody="floor(x)"; break;
	        case R.id.function_panel_edit_function_menu_common_functions_round:
	        	coomonFunction = true; functionBody="round(x)"; break;
	    }
		if(coomonFunction) {
			//final String data = ((SpannableStringBuilder)functionEquationInput.getText()).toString();
			//final int curpos = functionEquationInput.getSelectionStart();
			//final String toSetText = data.substring(0, curpos).concat(functionBody).concat(data.substring(curpos, data.length()));
			//functionEquationInput.setText(((SpannableStringBuilder)functionEquationInput.getText()).toString().concat(functionBody));
			//functionEquationInput.setText(toSetText);
			
			webview.loadUrl("javascript:$('#editable-math').mathquill('write', '"+functionBody+"');"); 
			
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	class MyJavaScriptInterface { 
		
        public MyJavaScriptInterface() {
            
        }

		@JavascriptInterface
        public void processContent(String aContent){
        	FunctionPanelEditFunctionActivity.this.functionLatexContent = aContent;
			final Function funct = new Function(FunctionPanelEditFunctionActivity.this.functionLatexContent);
			functionEquationState.setChecked(funct.isGood());
			
            System.out.println("Content posted: {"+aContent+"}");
        } 
    } 
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent keyEvent) {
		InputMethodManager inputManager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        inputManager.restartInput(webview);
        webview.requestFocus();
        webview.requestFocus(View.FOCUS_DOWN);
        webview.requestFocus();
        webview.requestFocusFromTouch();
        inputManager.restartInput(webview);
        
		return true;
	}
	
	/*private void addMainJavaBridgeInterface() {
		 String handleGingerbreadStupidity=
				    "javascript:function openQuestion(id) { window.location='http://jshandler:openQuestion:'+id; }; "
				  + "javascript: function handler() { this.openQuestion=openQuestion; }; "
				  + "javascript: var jshandler = new handler();";
				 view.loadUrl(handleGingerbreadStupidity);
	}*/
	
	@SuppressLint("SetJavaScriptEnabled") @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.function_panel_edit_function_layout);
        function = Core.functions.get(Core.toEditIndex);
        
        //functview = (ImageView) findViewById(R.id.function_panel_edit_function_view);
        
        //functionEquationInput = (TextView) findViewById(R.id.function_panel_edit_function_equation);
        functionEquationState = (CheckBox) findViewById(R.id.function_panel_edit_function_good_state);
        webview = (WebView) findViewById(R.id.function_panel_edit_webview);
        webview.getSettings().setRenderPriority(RenderPriority.HIGH);
        webview.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        
        
        webview.requestFocus(View.FOCUS_DOWN);
        webview.requestFocus();
        webview.requestFocusFromTouch();
        webview.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
				if (arg1 == KeyEvent.KEYCODE_MENU) {
					FunctionPanelEditFunctionActivity.this.openContextMenu(FunctionPanelEditFunctionActivity.this.webview);
			        return true;
			    }
				return false;
			}
        });
        /*webview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_UP:
                        if (!v.hasFocus()) {
                            v.requestFocus();
                        }
                        break;
                }
                return false;
            }
        });*/
        
        webview.getSettings().setJavaScriptEnabled(true); 
        webview.requestFocus(View.FOCUS_DOWN);
        
        webview.addJavascriptInterface(new MyJavaScriptInterface(), "INTERFACE"); 
        
        webview.requestFocus();
        webview.requestFocusFromTouch();
        webview.setWebViewClient(new WebViewClient() { 
            @Override 
            public void onPageFinished(WebView view, String url) { 
                view.loadUrl("javascript:window.INTERFACE.processContent($('#editable-math').mathquill('latex'));"); 
                view.loadUrl("javascript:$('#editable-math').mathquill('latex', '"+function.eqaution+"');"); 
            }
        });
        
        /*webview.setOnGenericMotionListener(new OnGenericMotionListener() {
			@Override
			public boolean onGenericMotion(View view, MotionEvent event) {
				((WebView)view).loadUrl("javascript:window.INTERFACE.processContent($('#editable-math').mathquill('latex'));"); 
				return false;
			}
        });*/
        webview.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View view, MotionEvent event) {
				((WebView)view).loadUrl("javascript:window.INTERFACE.processContent($('#editable-math').mathquill('latex'));"); 
				return false;
			}
        });
        webview.requestFocus();
        
        webview.loadUrl("file:///android_asset/page/test/index.html");
        
        
        functionEquationState.setClickable(false);
        functionEquationState.setChecked(function.isGood());
        
        //functionEquationInput.setText(function.eqaution);
        /*functionEquationInput.addTextChangedListener(new TextWatcher(){
			@Override
			public void afterTextChanged(Editable arg0) {
				final Function funct = new Function(((SpannableStringBuilder)functionEquationInput.getText()).toString());
				functionEquationState.setChecked(funct.isGood());
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				
			}
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				
			}
        });*/
        
        //colorpick = (ColorPicker) findViewById(R.id.function_panel_edit_function_color_picker);
        //colorpick.setColor(function.color);
        
        abort_button = (Button) findViewById(R.id.function_panel_edit_function_abort_button);
        abort_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	FunctionPanelActivity.entryCanBeClicked = true;
            	finish();
            }
        });
        
        save_button = (Button) findViewById(R.id.function_panel_edit_function_save_button);
        save_button.setOnClickListener(new View.OnClickListener() {
            public synchronized void onClick(View v) {
            	FunctionPanelActivity.entryCanBeClicked = true;
            	(webview).loadUrl("javascript:window.INTERFACE.processContent($('#editable-math').mathquill('latex'));"); 
				//try {
					//wait(100);
				//} catch (InterruptedException e) {
				//	e.printStackTrace();
				//}
            	
            	final Function funct = FunctionTypeSelector.select(new Function(FunctionPanelEditFunctionActivity.this.functionLatexContent));
            	
            	if(funct.isGood()) {
	            	function.eqaution = FunctionPanelEditFunctionActivity.this.functionLatexContent;
	            	try {
						function.prp_equation = LatexParser.preparseLatexString(function.eqaution);
					} catch (Throwable e) {
						e.printStackTrace();
					}
	                function.clearProperties();
	                
	                function = FunctionTypeSelector.select(function);
	                
	                Core.functions.setElementAt(function, Core.toEditIndex);
	                
	            	finish();
            	} else {
            		new AlertDialog.Builder(FunctionPanelEditFunctionActivity.this)
            	    .setTitle("Invalid expression")
            	    .setMessage("Are you sure you want to save this expression. It's uncompletted or invalid.")
            	    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            	        public void onClick(DialogInterface dialog, int which) { 
        	            	function.eqaution = FunctionPanelEditFunctionActivity.this.functionLatexContent;
        	            	try {
        						function.prp_equation = LatexParser.preparseLatexString(function.eqaution);
        					} catch (Throwable e) {
        						e.printStackTrace();
        					}
        	            	function.clearProperties();
        	                
        	                function = FunctionTypeSelector.select(function);
        	                
        	                Core.functions.setElementAt(function, Core.toEditIndex);
        	            	finish();
            	        }
            	     })
            	    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
            	        public void onClick(DialogInterface dialog, int which) { 
            	           
            	        }
            	     })
            	    .setIcon(android.R.drawable.ic_dialog_alert)
            	    .show();
            	}
            }
        });

	}
	
}
