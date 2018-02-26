package com.example.extremetic_tac_toe;

//FunctionPanelEditColorActivity

import com.example.extremetic_tac_toe.R;
import com.larswerkman.holocolorpicker.ColorPicker;

import android.app.Activity;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.Button;

public class FunctionPanelEditColorActivity extends Activity {

	Function function;
	ColorPicker colorpick;
	Button button;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.function_panel_edit_color_layout);
        function = Core.functions.get(Core.toEditIndex);
        
        colorpick = (ColorPicker) findViewById(R.id.function_panel_edit_function_color_picker);
        colorpick.setColor(function.color);
        
        button = (Button) findViewById(R.id.function_panel_edit_function_color_back_button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	SpannableStringBuilder fl;
            	function.color = colorpick.getColor();
                finish();
            }
        });

        
	}
	
}
