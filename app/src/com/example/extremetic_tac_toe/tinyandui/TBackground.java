package com.example.extremetic_tac_toe.tinyandui;

import org.andengine.ui.activity.BaseGameActivity;

import org.andengine.entity.scene.Scene;

public class TBackground extends TImage {

    public TBackground(final String ID, final float X, final float Y) {
	super(ID, X, Y);
    }

    public TBackground(final String ID, final float X, final float Y, final TSkin skin) {
	super(ID, X, Y, skin);
    }

    @Override
    public void flush(final BaseGameActivity activity, final Scene scene) {
	this.image_sprite.setPosition(this.posX, this.posY);
	this.image_sprite.setZIndex(this.indexZ + this.parentIndexZ);
	this.image_sprite.setVisible(this.visible && this.parentVisible);
	this.image_sprite.setAlpha(Math.min(this.opacity, this.parentOpacity));
	this.image_sprite.setWidth(this.w);
	this.image_sprite.setHeight(this.h);
	this.updateElement(activity, scene);
    }

}
