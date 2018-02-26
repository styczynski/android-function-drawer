package com.example.extremetic_tac_toe.tinyandui;


import java.util.Vector;

import org.andengine.entity.scene.Scene;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.ui.activity.SimpleLayoutGameActivity;

import android.content.Context;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.example.extremetic_tac_toe.MainActivity;
import com.example.extremetic_tac_toe.locale.logging.Logger;

public class TTextInput extends TExComponent<TButtonEventListener> implements TGenericEventHandler {

	private TInteractiveSprite button_sprite = null;
	private TInteractiveSprite button_onclick_sprite = null;
	
	protected float posX = 0;
	protected float posY = 0;
	private boolean state = false;
	private TStaticText textDisplay;
	protected String content = "";
	
	public TTextInput(String ID, float X, float Y, TText text) {
		super(TCore.ComponentClassID.T_TEXT_BUTTON);
		posX = X;
		posY = Y;
		assignID(ID);
		textDisplay = new TStaticText(id+":text", posX+10, posY+10, text);
	}
	
	public TTextInput(String ID, float X, float Y, TText text, TSkin skin) {
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
	
	boolean enableKeyboardInput = false;
	@Override
	public void onKey(KeyEvent event, BaseGameActivity activity, Scene scene) {
		if(event.getAction()!=KeyEvent.ACTION_UP) {
			return;
		}
		if(!enableKeyboardInput) {
			return;
		}
		System.out.println("INPUT KEY (UNICODE) = "+((Object)(event.getUnicodeChar())).toString());
		System.out.println("INPUT KEY SCAN CODE = "+((Object)(event.getScanCode())).toString());
		System.out.println("INPUT KEY CODE = "+((Object)(event.getKeyCode())).toString());
		System.out.println("INPUT KEY LABEL = "+((Object)(event.getDisplayLabel())).toString());
		
		if(event.getKeyCode()==KeyEvent.KEYCODE_ENTER) {	
			enableKeyboardInput = false;
			InputMethodManager inputMgr = (InputMethodManager)TCore.getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
			button_sprite.setVisible(true);
			button_onclick_sprite.setVisible(false);
			state = false;
			inputMgr.toggleSoftInput(0, 0);
		} else if(event.getKeyCode()==KeyEvent.KEYCODE_DEL) {
			if(content.length()>1){
				content = content.substring(0, content.length()-1);
			} else {
				content = "";
			}
		} else {
			content = content.concat(new String(new char[]{(char)event.getUnicodeChar()}));
		}
		
		String toDisplay = content;
		if(content.length()>10) {
			toDisplay = content.substring(content.length()-10, content.length());
		}
		
		textDisplay.setContent(toDisplay);
		allign();
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
		
		/*EditText editText = null;
		EditText editText = new EditText
		InputMethodManager imm = (InputMethodManager)TCore.getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);*/
		
		InputMethodManager inputMgr = (InputMethodManager)TCore.getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
		
		final int size = super.getEventListenersNumber();
		switch(componentID) {
			case 0:
				if(pSceneTouchEvent.isActionDown()) {
					if(!state) {
						state = true;
						enableKeyboardInput = !enableKeyboardInput;
						button_sprite.setVisible(false);
						button_onclick_sprite.setVisible(true);
						for(int it=0;it<size;++it) {
							super.getEventListener(it).whenPressed();
							super.getEventListener(it).whenReleased();
							super.getEventListener(it).whenClicked();
						}
						inputMgr.toggleSoftInput(0, 0);
					}
				} else {
					/*if(state) {
						state = false;
						button_sprite.setVisible(true);
						button_onclick_sprite.setVisible(false);
						for(int it=0;it<size;++it) {
							super.getEventListener(it).whenReleased();
							super.getEventListener(it).whenClicked();
						}
						inputMgr.toggleSoftInput(0, 0);
					}*/
				}
				break;
			case 1:
				if(pSceneTouchEvent.isActionUp()||pSceneTouchEvent.isActionCancel()||pSceneTouchEvent.isActionOutside()||pSceneTouchEvent.isActionMove()) {
					button_sprite.setVisible(true);
					button_onclick_sprite.setVisible(false);
					state = false;
					inputMgr.toggleSoftInput(0, 0);
				}
				break;
		}
	}
	
	@Override
	public void whenZIndexChanged() {
		textDisplay.indexZ = parentIndexZ+indexZ+1;
	}
	
	public void allign() {
		this.setW(Math.min(textDisplay.getTextWidth()*1.95f+50, 250));
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
		button_sprite.setVisible((!state) && visible && parentVisible);
		button_onclick_sprite.setVisible(state && visible && parentVisible);
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

