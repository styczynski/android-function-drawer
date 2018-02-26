package com.example.extremetic_tac_toe.tinyandui;

import org.andengine.entity.scene.Scene;
import org.andengine.ui.activity.BaseGameActivity;

public class TBackground extends TImage {

	public TBackground(String ID, float X, float Y) {
		super(ID, X, Y);
	}
	
	public TBackground(String ID, float X, float Y, TSkin skin) {
		super(ID, X, Y, skin);
	}
	
	@Override
	public void flush(BaseGameActivity activity, Scene scene) {
		image_sprite.setPosition(posX, posY);
		image_sprite.setZIndex(indexZ+parentIndexZ);
		image_sprite.setVisible(visible && parentVisible);
		image_sprite.setAlpha(Math.min(opacity, parentOpacity));
		image_sprite.setWidth(w);
		image_sprite.setHeight(h);
		updateElement(activity, scene);
	}

}
