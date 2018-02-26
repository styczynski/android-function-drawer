package com.example.extremetic_tac_toe.tinyandui;

import org.andengine.entity.scene.Scene;
import org.andengine.input.touch.TouchEvent;
import org.andengine.ui.activity.BaseGameActivity;

import android.view.KeyEvent;

public class TButton extends TExComponent<TButtonEventListener> implements TGenericEventHandler {

    private TInteractiveSprite button_onclick_sprite = null;
    private TInteractiveSprite button_sprite = null;

    private boolean state = false;
    protected float posX = 0;
    protected float posY = 0;

    public TButton(final String ID, final float X, final float Y) {
	super(TCore.ComponentClassID.T_BUTTON);
	this.posX = X;
	this.posY = Y;
	this.assignID(ID);
    }

    @Override
    public void flush(final BaseGameActivity activity, final Scene scene) {

	if (this.isVisibleChanged()) {
	    if (!this.visible || !this.parentVisible) {
		scene.unregisterTouchArea(this.button_sprite);
		scene.unregisterTouchArea(this.button_onclick_sprite);
	    } else {
		scene.registerTouchArea(this.button_sprite);
		scene.registerTouchArea(this.button_onclick_sprite);
	    }
	}

	this.button_sprite.setPosition(this.posX + this.parentPosX, this.posY + this.parentPosY);
	this.button_sprite.setZIndex(this.indexZ + this.parentIndexZ);
	this.button_onclick_sprite.setPosition(this.posX + this.parentPosX, this.posY + this.parentPosY);
	this.button_onclick_sprite.setZIndex(this.indexZ + this.parentIndexZ);
	this.button_sprite.setVisible(this.visible && this.parentVisible);
	this.button_sprite.setAlpha(Math.min(this.opacity, this.parentOpacity));
	this.button_onclick_sprite.setAlpha(Math.min(this.opacity, this.parentOpacity));

	this.updateElement(activity, scene);
	// button_onclick_sprite.setVisible(visible && parentVisible);
    }

    @Override
    public void onInit(final BaseGameActivity activity, final Scene scene) {

	this.skin.putDefaults(new TSkin().val("img", "taui/uistyle.png").val("hovered_img", "taui/uistyle.png"));
	this.button_sprite = TCore.getSprite(this.skin.getValue("img"), activity);
	this.button_onclick_sprite = TCore.getSprite(this.skin.getValue("hovered_img"), activity);

	this.button_sprite.init(this, 0);
	this.button_sprite.setWidth(this.w);
	this.button_sprite.setHeight(this.h);

	this.button_onclick_sprite.init(this, 1);
	this.button_onclick_sprite.setColor(1.0f, 0.0f, 0.0f);
	this.button_onclick_sprite.setWidth(this.w);
	this.button_onclick_sprite.setHeight(this.h);

	scene.registerTouchArea(this.button_sprite);
	scene.registerTouchArea(this.button_onclick_sprite);

	scene.attachChild(this.button_sprite);
	scene.attachChild(this.button_onclick_sprite);

	this.button_sprite.setVisible(true);
	this.button_onclick_sprite.setVisible(false);

	this.flush(activity, scene);
    }

    @Override
    public void onKey(final KeyEvent event, final BaseGameActivity activity, final Scene scene) {

    }

    @Override
    public void onRemove(final BaseGameActivity activity, final Scene scene) {
	scene.detachChild(this.button_sprite);
	scene.detachChild(this.button_onclick_sprite);
    }

    @Override
    public void onTouchEvent(final TouchEvent pSceneTouchEvent, final float X, final float Y, final int componentID) {
	if (this.blocked || this.parentBlocked) {
	    return;
	}
	if (!this.visible || !this.parentVisible) {
	    return;
	}
	final int size = super.getEventListenersNumber();
	switch (componentID) {
	case 0:
	    if (pSceneTouchEvent.isActionDown()) {
		if (!this.state) {
		    this.state = true;
		    this.button_sprite.setVisible(false);
		    this.button_onclick_sprite.setVisible(true);
		    for (int it = 0; it < size; ++it) {
			super.getEventListener(it).whenPressed();
		    }
		}
	    } else {
		if (this.state) {
		    this.state = false;
		    this.button_sprite.setVisible(true);
		    this.button_onclick_sprite.setVisible(false);
		    for (int it = 0; it < size; ++it) {
			super.getEventListener(it).whenReleased();
			super.getEventListener(it).whenClicked();
		    }
		}
	    }
	    break;
	case 1:
	    if (pSceneTouchEvent.isActionUp() || pSceneTouchEvent.isActionCancel() || pSceneTouchEvent.isActionOutside()
		    || pSceneTouchEvent.isActionMove()) {
		this.button_sprite.setVisible(true);
		this.button_onclick_sprite.setVisible(false);
		this.state = false;
	    }
	    break;
	}
    }

}
