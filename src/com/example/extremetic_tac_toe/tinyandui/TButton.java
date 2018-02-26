package com.example.extremetic_tac_toe.tinyandui;

import java.util.Vector;

import org.andengine.entity.scene.Scene;
import org.andengine.input.touch.TouchEvent;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.ui.activity.SimpleLayoutGameActivity;

import android.view.KeyEvent;

import com.example.extremetic_tac_toe.locale.logging.Logger;

public class TButton extends TExComponent<TButtonEventListener> implements TGenericEventHandler {

	
	private TInteractiveSprite button_sprite = null;
	private TInteractiveSprite button_onclick_sprite = null;
	
	protected float posX = 0;
	protected float posY = 0;
	private boolean state = false;
	
	
	public TButton(String ID, float X, float Y) {
		super(TCore.ComponentClassID.T_BUTTON);
		posX = X;
		posY = Y;
		assignID(ID);
	}
	
	@Override
	public void onInit(BaseGameActivity activity, Scene scene) {
	
		this.skin.putDefaults(new TSkin().val("img", "taui/uistyle.png").val("hovered_img", "taui/uistyle.png"));
		button_sprite = TCore.getSprite(skin.getValue("img"), activity);
		button_onclick_sprite = TCore.getSprite(skin.getValue("hovered_img"), activity);
		
		button_sprite.init(this, 0);
		button_sprite.setWidth(w);
		button_sprite.setHeight(h);
		
		button_onclick_sprite.init(this, 1);
		button_onclick_sprite.setColor(1.0f, 0.0f, 0.0f);
		button_onclick_sprite.setWidth(w);
		button_onclick_sprite.setHeight(h);
		
		scene.registerTouchArea(button_sprite);
		scene.registerTouchArea(button_onclick_sprite);
		
		scene.attachChild(button_sprite);
		scene.attachChild(button_onclick_sprite);
		
		button_sprite.setVisible(true);
		button_onclick_sprite.setVisible(false);
		
		flush(activity, scene);
	}

	@Override
	public void onRemove(BaseGameActivity activity, Scene scene) {
		scene.detachChild(button_sprite);
		scene.detachChild(button_onclick_sprite);
	}
	
	@Override
	public void onTouchEvent(TouchEvent pSceneTouchEvent, float X, float Y, int componentID) {
		if(blocked||parentBlocked) {
			return;
		}
		if(!visible || !parentVisible) {
			return;
		}
		final int size = super.getEventListenersNumber();
		switch(componentID) {
			case 0:
				if(pSceneTouchEvent.isActionDown()) {
					if(!state) {
						state = true;
						button_sprite.setVisible(false);
						button_onclick_sprite.setVisible(true);
						for(int it=0;it<size;++it) {
							super.getEventListener(it).whenPressed();
						}
					}
				} else {
					if(state) {
						state = false;
						button_sprite.setVisible(true);
						button_onclick_sprite.setVisible(false);
						for(int it=0;it<size;++it) {
							super.getEventListener(it).whenReleased();
							super.getEventListener(it).whenClicked();
						}
					}
				}
				break;
			case 1:
				if(pSceneTouchEvent.isActionUp()||pSceneTouchEvent.isActionCancel()||pSceneTouchEvent.isActionOutside()||pSceneTouchEvent.isActionMove()) {
					button_sprite.setVisible(true);
					button_onclick_sprite.setVisible(false);
					state = false;
				}
				break;
		}
	}
	
	@Override
	public void onKey(KeyEvent event, BaseGameActivity activity, Scene scene) {
		
	}
	
	@Override
	public void flush(BaseGameActivity activity, Scene scene) {
		
		if(isVisibleChanged()) {
			if(!visible || !parentVisible) {
				scene.unregisterTouchArea(button_sprite);
				scene.unregisterTouchArea(button_onclick_sprite);
			} else {
				scene.registerTouchArea(button_sprite);
				scene.registerTouchArea(button_onclick_sprite);
			}
		}
		
		button_sprite.setPosition(posX+parentPosX, posY+parentPosY);
		button_sprite.setZIndex(indexZ+parentIndexZ);
		button_onclick_sprite.setPosition(posX+parentPosX, posY+parentPosY);
		button_onclick_sprite.setZIndex(indexZ+parentIndexZ);
		button_sprite.setVisible(visible && parentVisible);
		button_sprite.setAlpha(Math.min(opacity, parentOpacity));
		button_onclick_sprite.setAlpha(Math.min(opacity, parentOpacity));
		
		updateElement(activity, scene);
		//button_onclick_sprite.setVisible(visible && parentVisible);
	}
	
}
