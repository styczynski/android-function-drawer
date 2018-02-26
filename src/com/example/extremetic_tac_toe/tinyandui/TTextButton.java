package com.example.extremetic_tac_toe.tinyandui;


import java.util.Vector;

import org.andengine.entity.scene.Scene;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.ui.activity.SimpleLayoutGameActivity;

import android.view.KeyEvent;

import com.example.extremetic_tac_toe.locale.logging.Logger;

public class TTextButton extends TExComponent<TButtonEventListener> implements TGenericEventHandler {

	private TInteractiveSprite button_sprite = null;
	private TInteractiveSprite button_onclick_sprite = null;
	
	protected float posX = 0;
	protected float posY = 0;
	private boolean state = false;
	private TStaticText textDisplay;
	
	public TTextButton(String ID, float X, float Y, TText text) {
		super(TCore.ComponentClassID.T_TEXT_BUTTON);
		posX = X;
		posY = Y;
		assignID(ID);
		textDisplay = new TStaticText(id+":text", posX+10, posY+10, text);
	}
	
	public TTextButton(String ID, float X, float Y, TText text, TSkin skin) {
		super(TCore.ComponentClassID.T_TEXT_BUTTON);
		posX = X;
		posY = Y;
		assignID(ID);
		textDisplay = new TStaticText(id+":text", posX+10, posY+10, text);
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
		
		textDisplay.onInit(activity, scene);
		textDisplay.setPos(15, 0);
		textDisplay.indexZ = indexZ+parentIndexZ+1;

		flush(activity, scene);
	}

	public TText getTextOptions() {
		return textDisplay.getTextOptions();
	}
	
	public void setTextOptions(TText options) {
		textDisplay.setTextOptions(options);
	}
	
	@Override
	public void onRemove(BaseGameActivity activity, Scene scene) {
		scene.detachChild(button_sprite);
		scene.detachChild(button_onclick_sprite);
		
		textDisplay.onRemove(activity, scene);
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
	public void whenZIndexChanged() {
		textDisplay.indexZ = parentIndexZ+indexZ+1;
	}
	
	public void allign() {
		this.setW(textDisplay.getTextWidth()*1.25f);
	}
	
	@Override
	public void onKey(KeyEvent event,BaseGameActivity activity, Scene scene) {
		
	}
	
	@Override
	public void flush(BaseGameActivity activity, Scene scene) {
		if(w==-1) {
			allign();
		}
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
		//textDisplay.opacity = this.textDisplay.getTextOptions().colorA;
		button_sprite.setWidth(w);
		button_onclick_sprite.setWidth(w);
		button_sprite.setHeight(h);
		button_onclick_sprite.setHeight(h);
		
		textDisplay.parentPosX = posX+parentPosX+((w-textDisplay.getTextWidth())/4);
		textDisplay.parentPosY = posY+parentPosY+((h-textDisplay.getTextHeight())/4);
		textDisplay.indexZ = indexZ+parentIndexZ+1;
		textDisplay.parentOpacity = Math.min(opacity, parentOpacity);
		textDisplay.parentVisible = visible && parentVisible;
		textDisplay.flush(activity, scene);
		//button_onclick_sprite.setVisible(visible && parentVisible);
		
		updateElement(activity, scene);
	}
	
}

