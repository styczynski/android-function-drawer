package com.example.extremetic_tac_toe.tinyandui;

import org.andengine.input.touch.TouchEvent;

public interface TGenericEventHandler {

	public abstract void onTouchEvent(TouchEvent pSceneTouchEvent, float X, float Y, int componentID);
	
}
