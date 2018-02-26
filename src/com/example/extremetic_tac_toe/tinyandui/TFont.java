package com.example.extremetic_tac_toe.tinyandui;

import org.andengine.opengl.font.Font;
import org.andengine.ui.activity.BaseGameActivity;

public class TFont {

	private String path = null;
	private int size = 0;
	private int color = 0;
	protected float colorR = 1.0f;
	protected float colorG = 1.0f;
	protected float colorB = 1.0f;
	protected float colorA = 1.0f;
	
	public TFont() {
		path = null;
	}
	
	public TFont(String name) {
		path = name;
	}
	
	public TFont(TSkin skin) {
		String temp = "";
		
		temp = skin.getValue("font");
		if(temp!=null) {
			path = temp;
		}
		
		temp = skin.getValue("font_size");
		if(temp!=null) {
			size = Integer.parseInt(temp);
		}
		
		temp = skin.getValue("font_color");
		if(temp!=null) {
			color = Integer.parseInt(temp);
		}
	}
	
	public TFont setSize(int arg) {
		size = arg;
		return this;
	}
	
	public TFont setColor(int arg) {
		color = arg;
		return this;
	}
	
	public <T extends BaseGameActivity> Font load(T activity) {
		if(path==null) {
			return TCore.getDefaultComponentFont();
		}
		return TCore.getFont(path, activity, size, color);
	}

}
