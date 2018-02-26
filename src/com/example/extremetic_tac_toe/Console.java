package com.example.extremetic_tac_toe;

import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class Console {
	
	public static boolean isEnabled = false;

	protected static void whenActivateButtonPressed() {
		EditText consoleInput = (EditText)DrawingPanelActivity.runningActivity.findViewById(R.id.graphing_panel_console_input);
		if(consoleInput==null) return;	
		
		if(Console.isEnabled) {
			consoleInput.setEnabled(false);
			consoleInput.setVisibility(View.GONE);
			Console.hideOutput();
		} else {
			consoleInput.setEnabled(true);
			consoleInput.setVisibility(View.VISIBLE);
			Console.hideOutput();

			consoleInput.setOnEditorActionListener(new OnEditorActionListener() {

				@Override
				public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
					if(actionId == EditorInfo.IME_ACTION_DONE) {
						Console.parse(v.getText().toString());
						v.setText("");
					}
					return false;
				}
				
			});
		}
		Console.isEnabled = !Console.isEnabled;
	}
	
	private static void showOutput(String text) {
		EditText outputView = (EditText) DrawingPanelActivity.runningActivity.findViewById(R.id.graphing_panel_console_output);
		outputView.setVisibility(View.VISIBLE);
		outputView.setText(text);
	}
	
	private static void hideOutput() {
		EditText outputView = (EditText) DrawingPanelActivity.runningActivity.findViewById(R.id.graphing_panel_console_output);
		outputView.setVisibility(View.GONE);
	}
	
	protected static boolean onCommandToken(String token, String[] args) {
		boolean wasParsed = false;
		hideOutput();
		
		if(token.equals("plot")) {
			Function funct = new Function();
			funct.eqaution = args[0];
			funct = FunctionTypeSelector.select(funct);
			funct.isTemporary = true;
			Core.functions.add(funct);
			wasParsed = true;
		} else if(token.equals("variable")||token.equals("var")) {
			FunctionVariable funct = new FunctionVariable();
			funct.name = args[0];
			funct.value = Double.parseDouble(args[1]);
			funct.updateState();
			funct.isTemporary = true;
			Core.functions.add(funct);
			wasParsed = true;
		} else if(token.equals("clear")) {
			Core.functions.clear();
		} else if(token.equals("get")) {
			if(args[0].equals("functions.number")) {
				showOutput(((Object)Core.functions.size()).toString());
			} else {
				int index = Integer.parseInt(args[0].replaceAll("functions.", ""));
				showOutput(Core.functions.get(index).eqaution);
			}
		} else if(token.equals("calc")||token.equals("cal")||token.equals("calculate")) {
			Function funct = new Function(args[0]);
			double result = funct.eval(0);
			showOutput(((Object)result).toString());
		} else if(token.equals("end")) {
			Core.cleanupTemporaryFunctions();
			wasParsed = true;
		}
		
		if(wasParsed) {
			DrawingPanelActivity.runningActivity.flush();
		}
		
		return wasParsed;
	}
	
	public static void parse(String command) {
		LuaComParser.parse(command);
	}

}
