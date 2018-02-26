package com.example.extremetic_tac_toe.tinyandui;

import java.util.Vector;

import org.andengine.entity.scene.Scene;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontManager;
import org.andengine.opengl.font.IFont;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.ui.activity.SimpleLayoutGameActivity;
import org.andengine.util.color.Color;

import android.view.KeyEvent;

import com.example.extremetic_tac_toe.locale.logging.Logger;

public class TStaticText extends TComponent implements TGenericEventHandler {

	private Text text;
	private TText textOptions;
	private int maxContentSize = 64;
	
	private Font initFont = null;
	
	public void setTextOptions(TText options) {
		textOptions = options;
	}
	
	public TText getTextOptions() {
		textOptions.colorA = opacity;
		return textOptions;
	}
	
	public TStaticText(String ID, float X, float Y, TText text) {
		super();
		textOptions = text;
		opacity = text.colorA;
		initFont = TCore.getDefaultComponentFont();
	}
	
	public TStaticText(String ID, float X, float Y, Font font, String content) {
		super();
		posX = X;
		posY = Y;
		initFont = font;
		textOptions = new TText();
		textOptions.textContent = content;
		assignID(ID);
	}
	
	public TStaticText(String ID, float X, float Y, String content) {
		super();
		posX = X;
		posY = Y;
		initFont = TCore.getDefaultComponentFont();
		textOptions = new TText();
		textOptions.textContent = content;
		maxContentSize = content.length();
		assignID(ID);
	}
	
	@Override
	public void onTouchEvent(TouchEvent pSceneTouchEvent, float X, float Y, int componentID) {
		
	}

	@Override
	public void onKey(KeyEvent event,BaseGameActivity activity, Scene scene) {
		
	}
	
	@Override
	public void onInit(BaseGameActivity activity, Scene scene) {
		text = new Text(posX, posY, initFont, textOptions.textContent, maxContentSize, activity.getVertexBufferObjectManager());
		text.setVisible(true);
		scene.attachChild(text);
		flush(activity, scene);
	}

	@Override
	public void onRemove(BaseGameActivity activity, Scene scene) {
		scene.detachChild(text);
	}

	public void setColor(Color pColor) {
		textOptions.colorR = pColor.getRed();
		textOptions.colorG = pColor.getGreen();
		textOptions.colorB = pColor.getBlue();
		opacity = pColor.getAlpha();
	}
	
	public void setColor(float r, float g, float b) {
		textOptions.colorR = r;
		textOptions.colorG = g;
		textOptions.colorB = b;
	}
	
	public void setColor(float r, float g, float b, float a) {
		textOptions.colorR = r;
		textOptions.colorG = g;
		textOptions.colorB = b;
		opacity = a;
	}
	
	public String getContent() {
		return textOptions.textContent;
	}
	
	public void setContent(CharSequence cseq) {
		final int size = cseq.length();
		final int del = maxContentSize-size;
		if(del<0) {
			cseq = cseq.subSequence(0, size-del);
		}
		textOptions.textContent = (String) cseq;
	}
	
	public void setMaximumContentLength(int newMaximum) {
		maxContentSize = newMaximum;
	}
	
	public IFont getFont() {
		return text.getFont();
	}
	
	public float getTextWidth() {
		return text.getWidthScaled();
	}
	
	public float getTextHeight() {
		return text.getHeightScaled();
	}
	
	@Override
	public void flush(BaseGameActivity activity, Scene scene) {
		
		text.setPosition(posX+parentPosX, posY+parentPosY);
		text.setVisible(visible && parentVisible);
		text.setAlpha(Math.min(opacity, parentOpacity));
		text.setColor(textOptions.colorR, textOptions.colorG, textOptions.colorB);
		text.setText(textOptions.textContent);
		text.setZIndex(indexZ);
		
		updateElement(activity, scene);
	}

	
}