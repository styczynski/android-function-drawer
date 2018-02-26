package com.example.extremetic_tac_toe.tinyandui;

import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.vbo.ISpriteVertexBufferObject;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.shader.ShaderProgram;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.DrawType;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public class TInteractiveSprite extends Sprite {

	private TGenericEventHandler mTouchEventHandler = null;
	private boolean mTouchEventHandlerInited = false;
	private boolean mTouchEventHandlerPaused = false;
	private int mComponentID = -1;
	
	public TInteractiveSprite(float pX, float pY, float pWidth, float pHeight,
			ITextureRegion pTextureRegion,
			ISpriteVertexBufferObject pSpriteVertexBufferObject,
			ShaderProgram pShaderProgram) {
		super(pX, pY, pWidth, pHeight, pTextureRegion, pSpriteVertexBufferObject,
				pShaderProgram);
	}

	public TInteractiveSprite(float pX, float pY, float pWidth, float pHeight,
			ITextureRegion pTextureRegion,
			ISpriteVertexBufferObject pSpriteVertexBufferObject) {
		super(pX, pY, pWidth, pHeight, pTextureRegion, pSpriteVertexBufferObject);
	}

	public TInteractiveSprite(float pX, float pY, float pWidth, float pHeight,
			ITextureRegion pTextureRegion,
			VertexBufferObjectManager pVertexBufferObjectManager,
			DrawType pDrawType, ShaderProgram pShaderProgram) {
		super(pX, pY, pWidth, pHeight, pTextureRegion, pVertexBufferObjectManager,
				pDrawType, pShaderProgram);
	}

	public TInteractiveSprite(float pX, float pY, float pWidth, float pHeight,
			ITextureRegion pTextureRegion,
			VertexBufferObjectManager pVertexBufferObjectManager,
			DrawType pDrawType) {
		super(pX, pY, pWidth, pHeight, pTextureRegion, pVertexBufferObjectManager,
				pDrawType);
	}

	public TInteractiveSprite(float pX, float pY, float pWidth, float pHeight,
			ITextureRegion pTextureRegion,
			VertexBufferObjectManager pVertexBufferObjectManager,
			ShaderProgram pShaderProgram) {
		super(pX, pY, pWidth, pHeight, pTextureRegion, pVertexBufferObjectManager,
				pShaderProgram);
	}

	public TInteractiveSprite(float pX, float pY, float pWidth, float pHeight,
			ITextureRegion pTextureRegion,
			VertexBufferObjectManager pVertexBufferObjectManager) {
		super(pX, pY, pWidth, pHeight, pTextureRegion, pVertexBufferObjectManager);
	}

	public TInteractiveSprite(float pX, float pY,
			ITextureRegion pTextureRegion,
			ISpriteVertexBufferObject pVertexBufferObject,
			ShaderProgram pShaderProgram) {
		super(pX, pY, pTextureRegion, pVertexBufferObject, pShaderProgram);
	}

	public TInteractiveSprite(float pX, float pY,
			ITextureRegion pTextureRegion,
			ISpriteVertexBufferObject pVertexBufferObject) {
		super(pX, pY, pTextureRegion, pVertexBufferObject);
	}

	public TInteractiveSprite(float pX, float pY,
			ITextureRegion pTextureRegion,
			VertexBufferObjectManager pVertexBufferObjectManager,
			DrawType pDrawType, ShaderProgram pShaderProgram) {
		super(pX, pY, pTextureRegion, pVertexBufferObjectManager, pDrawType,
				pShaderProgram);
	}

	public TInteractiveSprite(float pX, float pY,
			ITextureRegion pTextureRegion,
			VertexBufferObjectManager pVertexBufferObjectManager,
			DrawType pDrawType) {
		super(pX, pY, pTextureRegion, pVertexBufferObjectManager, pDrawType);
	}

	public TInteractiveSprite(float pX, float pY,
			ITextureRegion pTextureRegion,
			VertexBufferObjectManager pVertexBufferObjectManager,
			ShaderProgram pShaderProgram) {
		super(pX, pY, pTextureRegion, pVertexBufferObjectManager, pShaderProgram);
	}

	public TInteractiveSprite(float pX, float pY,
			ITextureRegion pTextureRegion,
			VertexBufferObjectManager pVertexBufferObjectManager) {
		super(pX, pY, pTextureRegion, pVertexBufferObjectManager);
	}
	
	@Override
    public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float X, float Y) {
		if(this.mTouchEventHandlerInited&&(!this.mTouchEventHandlerPaused)) {
			this.mTouchEventHandler.onTouchEvent(pSceneTouchEvent, X, Y, this.mComponentID);
		}
		return true;
    };
    
    public TInteractiveSprite init() {
    	 mTouchEventHandlerInited = false;
    	 return this;
    }
    
    public TInteractiveSprite init(TGenericEventHandler handler, int id) {
    	this.mTouchEventHandlerInited = true;
    	this.mTouchEventHandler = handler;
    	this.mComponentID = id;
    	return this;
    }
    
    public TInteractiveSprite init(TGenericEventHandler handler) {
    	this.mTouchEventHandlerInited = true;
    	this.mTouchEventHandler = handler;
    	this.mComponentID = -1;
    	return this;
    }
    
    public void pauseTouchEventHandler() {
    	this.mTouchEventHandlerPaused = true;
    }
    
    public void continueTouchEventHandler() {
    	this.mTouchEventHandlerPaused = false;
    }

}
