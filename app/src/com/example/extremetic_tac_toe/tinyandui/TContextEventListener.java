package com.example.extremetic_tac_toe.tinyandui;

import org.andengine.input.touch.TouchEvent;
import org.andengine.input.touch.detector.PinchZoomDetector;

public interface TContextEventListener extends TGenericEventListener {

	public void whenTouched(TouchEvent pSceneTouchEvent);
	public void whenPinchZoom(PinchZoomDetector pPinchZoomDetector, TouchEvent pTouchEvent, float pZoomFactor);
	public void whenPinchZoomStarted(PinchZoomDetector pPinchZoomDetector, TouchEvent pTouchEvent);
	public void whenPinchZoomFinished(PinchZoomDetector pPinchZoomDetector, TouchEvent pTouchEvent, float pZoomFactor);
	
	
}
