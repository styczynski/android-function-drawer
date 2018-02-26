package com.example.extremetic_tac_toe.tinyandui;

import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.ui.activity.BaseGameActivity;

import android.view.KeyEvent;


public class TImage extends TComponent {

	protected Sprite image_sprite = null;
	
	public TImage(String ID, float X, float Y) {
		super(TCore.ComponentClassID.T_TEXT_BUTTON);
		posX = X;
		posY = Y;
		assignID(ID);
	}
	
	public TImage(String ID, float X, float Y, TSkin skin) {
		super(TCore.ComponentClassID.T_TEXT_BUTTON);
		posX = X;
		posY = Y;
		assignID(ID);
	}
	
	@Override
	public void onInit(BaseGameActivity activity, Scene scene) {
		this.skin.putDefaults(new TSkin().val("img", "taui/ui_style.png").val("hovered_img", "taui/ui_style.png"));
		image_sprite = TCore.getSprite(skin.getValue("img"), activity);
		image_sprite.setWidth(w);
		image_sprite.setHeight(h);
		scene.attachChild(image_sprite);
		image_sprite.setVisible(true);

		flush(activity, scene);
	}
	
	@Override
	public void onKey(KeyEvent event, BaseGameActivity activity, Scene scene) {
		
	}
	
	@Override
	public void onRemove(BaseGameActivity activity, Scene scene) {
		scene.detachChild(image_sprite);
	}
	
	@Override
	public void flush(BaseGameActivity activity, Scene scene) {
		image_sprite.setPosition(posX+parentPosX, posY+parentPosY);
		image_sprite.setZIndex(indexZ+parentIndexZ);
		image_sprite.setVisible(visible && parentVisible);
		image_sprite.setAlpha(Math.min(opacity, parentOpacity));
		image_sprite.setWidth(w);
		image_sprite.setHeight(h);
		updateElement(activity, scene);
	}
	
}

