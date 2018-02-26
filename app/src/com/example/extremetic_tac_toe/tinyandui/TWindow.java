package com.example.extremetic_tac_toe.tinyandui;

import java.util.HashMap;
import java.util.Iterator;

import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.ui.activity.BaseGameActivity;


public class TWindow extends TFence {
	
	private Sprite window_sprite;

	public TWindow(TContainer parent, String pName) {
		super(parent, pName, false);
		id = pName;
	}
	
	public TWindow(TFence parent, String pName) {
		super(parent, pName, false);
		id = pName;
	}
	
	@Override
	public void onInit(BaseGameActivity activity, Scene scene) {
		this.skin.putDefaults(new TSkin().val("img", "taui/uistyle.png"));
		window_sprite = (Sprite) TCore.getSprite(skin.getValue("img"), activity);
		window_sprite.setPosition(posX+parentPosX, posY+parentPosY);
		window_sprite.setWidth(w);
		window_sprite.setHeight(h);
		window_sprite.setZIndex(indexZ+parentIndexZ);
		
		scene.attachChild(window_sprite);
		
		flush(activity, scene);
	}
	
	@Override
	public void flush(BaseGameActivity activity, Scene scene) {
		window_sprite.setPosition(posX+parentPosX, posY+parentPosY);
		window_sprite.setZIndex(indexZ+parentIndexZ);
		window_sprite.setVisible(visible && parentVisible);
		window_sprite.setAlpha(Math.min(opacity, parentOpacity));
		window_sprite.setWidth(w);
		window_sprite.setHeight(h);
		
		Iterator it = mStack.entrySet().iterator();
	    while (it.hasNext()) {
	        HashMap.Entry pairs = (HashMap.Entry)it.next();
	        ((TComponent)pairs.getValue()).parentIndexZ = this.parentIndexZ+1;
	        ((TComponent)pairs.getValue()).parentPosX = posX+this.parentPosX;
	        ((TComponent)pairs.getValue()).parentPosY = posY+this.parentPosY;
	        ((TComponent)pairs.getValue()).parentVisible = visible&&this.parentVisible;
	        ((TComponent)pairs.getValue()).parentOpacity = opacity*this.parentOpacity;
	        ((TComponent)pairs.getValue()).parentBlocked = blocked&&this.parentBlocked;
	        ((TComponent)pairs.getValue()).flush(activity, mScene);
	    }
	    this.mScene.sortChildren();
	}
	
}