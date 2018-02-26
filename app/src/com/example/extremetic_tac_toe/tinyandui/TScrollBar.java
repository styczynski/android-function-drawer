package com.example.extremetic_tac_toe.tinyandui;


import java.util.Vector;

import org.andengine.entity.scene.Scene;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.ui.activity.SimpleLayoutGameActivity;

import android.view.KeyEvent;

import com.example.extremetic_tac_toe.locale.logging.Logger;

public class TScrollBar extends TExComponent<TScrollBarEventListener> implements TGenericEventHandler {

	private TInteractiveSprite bar_sprite = null;
	private TInteractiveSprite bar_marker_sprite = null;
	
	protected float posX = 0;
	protected float posY = 0;
	private int scrollValue = 0;
	private float value = 0;
	
	public TScrollBar(String ID, float X, float Y) {
		super(TCore.ComponentClassID.T_TEXT_BUTTON);
		posX = X;
		posY = Y;
		assignID(ID);
	}
	
	public TScrollBar(String ID, float X, float Y, TSkin skin) {
		super(TCore.ComponentClassID.T_TEXT_BUTTON);
		posX = X;
		posY = Y;
		assignID(ID);
	}
	
	
	@Override
	public void onInit(BaseGameActivity activity, Scene scene) {
		
		this.skin.putDefaults(new TSkin().val("img", "taui/uistyle.png").val("marker_img", "taui/uistyle.png"));
		bar_sprite = TCore.getSprite(skin.getValue("img"), activity);
		bar_marker_sprite = TCore.getSprite(skin.getValue("marker_img"), activity);
		
		bar_sprite.init(this, 0);
		bar_sprite.setWidth(w);
		bar_sprite.setHeight(h);
		
		bar_marker_sprite.init(this, 1);
		bar_marker_sprite.setWidth(w);
		bar_marker_sprite.setHeight(h);
		
		scene.registerTouchArea(bar_sprite);
		scene.registerTouchArea(bar_marker_sprite);
		
		scene.attachChild(bar_sprite);
		scene.attachChild(bar_marker_sprite);
		
		bar_sprite.setVisible(true);
		bar_marker_sprite.setVisible(false);

		flush(activity, scene);
	}
	
	@Override
	public void onRemove(BaseGameActivity activity, Scene scene) {
		scene.detachChild(bar_sprite);
		scene.detachChild(bar_marker_sprite);
	}
	
	public float getValue() {
		return value;
	}
	
	public void setValue(float newValue) {
		final float oldValue = value;
		value = newValue;
		if(newValue<0) {
			value=0;
		} else if(newValue>100.0f) {
			value=100.0f;
		}
		scrollValue = (int)(value*h/100.0f);

		final int size = super.getEventListenersNumber();
		for(int it=0;it<size;++it) {
			super.getEventListener(it).whenValueChanged(oldValue, value);
		}
		flush();
	}
	
	@Override
	public void onTouchEvent(TouchEvent pSceneTouchEvent, float X, float Y, int componentID) {
		if(blocked||parentBlocked) {
			return;
		}
		if(!visible || !parentVisible) {
			return;
		}	
		
		final float oldValue = value;
		scrollValue = (int)( ((float)(pSceneTouchEvent.getY()-posY-parentPosY)) );
		
		if(scrollValue<0) {
			scrollValue=0;
		} else if(scrollValue>h) {
			scrollValue=(int)h;
		}
		
		value = (scrollValue*100/h);
		
		bar_sprite.setPosition(posX+parentPosX+(w/2), posY+parentPosY);
		bar_sprite.setZIndex(indexZ+parentIndexZ);
		bar_marker_sprite.setPosition(posX+parentPosX, posY+parentPosY+scrollValue);
		bar_marker_sprite.setZIndex(indexZ+parentIndexZ);
		
		bar_sprite.setVisible(visible && parentVisible);
		bar_marker_sprite.setVisible(visible && parentVisible);
		bar_sprite.setAlpha(Math.min(opacity, parentOpacity));
		bar_marker_sprite.setAlpha(Math.min(opacity, parentOpacity));
		
		bar_sprite.setWidth(w/5.0f);
		bar_marker_sprite.setWidth(w);
		bar_sprite.setHeight(h);
		bar_marker_sprite.setHeight(h/8.0f);
		
		final int size = super.getEventListenersNumber();
		for(int it=0;it<size;++it) {
			super.getEventListener(it).whenValueChanged(oldValue, value);
		}
	}
	
	@Override
	public void whenZIndexChanged() {
		
	}
	
	@Override
	public void onKey(KeyEvent event,BaseGameActivity activity, Scene scene) {
		
	}
	
	@Override
	public void flush(BaseGameActivity activity, Scene scene) {
		if(isVisibleChanged()) {
			if(!visible || !parentVisible) {
				scene.unregisterTouchArea(bar_sprite);
				scene.unregisterTouchArea(bar_marker_sprite);
			} else {
				scene.registerTouchArea(bar_sprite);
				scene.registerTouchArea(bar_marker_sprite);
			}
		}

		bar_sprite.setPosition(posX+parentPosX+(w/2), posY+parentPosY);
		bar_sprite.setZIndex(indexZ+parentIndexZ);
		bar_marker_sprite.setPosition(posX+parentPosX, posY+parentPosY+scrollValue);
		bar_marker_sprite.setZIndex(indexZ+parentIndexZ);
		
		bar_sprite.setVisible(visible && parentVisible);
		bar_marker_sprite.setVisible(visible && parentVisible);
		bar_sprite.setAlpha(Math.min(opacity, parentOpacity));
		bar_marker_sprite.setAlpha(Math.min(opacity, parentOpacity));
		
		bar_sprite.setWidth(w/3.0f);
		bar_marker_sprite.setWidth(w);
		bar_sprite.setHeight(h);
		bar_marker_sprite.setHeight(h/8.0f);
		
		updateElement(activity, scene);
	}
	
}

