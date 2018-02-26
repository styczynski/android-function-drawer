package com.example.extremetic_tac_toe.tinyandui;

import java.util.HashMap;
import java.util.Iterator;

import org.andengine.entity.scene.Scene;
import org.andengine.ui.activity.BaseGameActivity;

public final class TNullFence extends TFence {

	public TNullFence() {
		super();
	}
	
	@Override
	public void onInit(BaseGameActivity activity, Scene scene) {
		
	}

	@Override
	public void onRemove(BaseGameActivity activity, Scene scene) {
		
	}	
	
	@Override
	public void flush(BaseGameActivity activity, Scene scene) {

	}
	
	@Override
	public void pushNewSetElement(TComponent component) {

	}
	
	@Override
	public TComponent recursiveSearch(String id) {
		return null;
	}
	
}
