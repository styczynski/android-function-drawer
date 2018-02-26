package com.example.extremetic_tac_toe.tinyandui;

import org.andengine.util.color.Color;

public class TText {

	protected float colorR = 1.0f;
	protected float colorG = 1.0f;
	protected float colorB = 1.0f;
	protected float colorA = 1.0f;
	protected String textContent;

	protected TText() {
		textContent = "";
	}
	
	public void setColor(float r, float g, float b, float a) {
		colorR = r;
		colorG = g;
		colorB = b;
		colorA = a;
	}
	
	public void setColor(float r, float g, float b) {
		setColor(r, g, b, 1.0f);
	}
	
	public TText useSkin(TSkin skin) {
		String temp = "";
		
		temp = skin.getValue("font_color");
		if(temp!=null) {
			String[] tab = temp.split(";");	
			if(tab.length>3) {
				colorR = ((float)Integer.parseInt(tab[0]))/255.0f;
				colorG = ((float)Integer.parseInt(tab[1]))/255.0f;
				colorB = ((float)Integer.parseInt(tab[2]))/255.0f;
				colorA = ((float)Integer.parseInt(tab[3]))/255.0f;
			} else if(tab.length==3) {
				colorR = ((float)Integer.parseInt(tab[0]))/255.0f;
				colorG = ((float)Integer.parseInt(tab[1]))/255.0f;
				colorB = ((float)Integer.parseInt(tab[2]))/255.0f;
			} else {
				colorR = 1.0f;
				colorG = 1.0f;
				colorB = 1.0f;
				colorA = 1.0f;
			}
		}
		return this;
	}
	
	public TText(String content) {
		textContent = content;
	}
	
	public TText(String content, float R, float G, float B) {
		textContent = content;
		colorR = R;
		colorG = G;
		colorB = B;
	}
	
	public TText(String content, float R, float G, float B, float A) {
		textContent = content;
		colorR = R;
		colorG = G;
		colorB = B;
		colorA = A;
	}
	
	public TText(String content, Color color) {
		textContent = content;
		colorR = color.getRed();
		colorG = color.getGreen();
		colorB = color.getBlue();
		colorA = color.getAlpha();
	}
	
}
