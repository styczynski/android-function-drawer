package com.example.extremetic_tac_toe.tinyandui;

import java.util.Calendar;

import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.input.touch.detector.PinchZoomDetector;
import org.andengine.input.touch.detector.PinchZoomDetector.IPinchZoomDetectorListener;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.source.EmptyBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.bitmap.source.decorator.BaseBitmapTextureAtlasSourceDecorator;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.ui.activity.BaseGameActivity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.KeyEvent;

public class TCanvas extends TExComponent<TContextEventListener> implements TGenericEventHandler, IPinchZoomDetectorListener {

	
	BitmapTextureAtlas mBitmapTextureAtlas = null;
	TInteractiveSprite sprite = null;
	Canvas canvas = new Canvas();
	android.graphics.Bitmap bmp = null;
	float tw = 0.0f, th = 0.0f, tmaxw = 0.0f, tmaxh = 0.0f;
	private PinchZoomDetector mPinchZoomDetector = null;
	 
	
	public void flushImage() {
		TSynchronizer sync = new TSynchronizer();
		sync.newMeasurement();
		
		sync.newMeasurement();
		System.out.println("@A flushImage() begin. [TEST GROUP] Time = "+((Object)sync.getFieldChange(Calendar.MILLISECOND)).toString());
		sync.newMeasurement();
		mBitmapTextureAtlas.unload();
		System.out.println("@B flushImage() unloaded. Time = "+((Object)sync.getFieldChange(Calendar.MILLISECOND)).toString());
		sync.newMeasurement();
		mBitmapTextureAtlas.clearTextureAtlasSources();
		System.out.println("@C flushImage() cleared. Time = "+((Object)sync.getFieldChange(Calendar.MILLISECOND)).toString());
		sync.newMeasurement();
		final IBitmapTextureAtlasSource baseTextureSource = new EmptyBitmapTextureAtlasSource((int)tmaxw, (int)tmaxh);
        final IBitmapTextureAtlasSource decoratedTextureAtlasSource = new BaseBitmapTextureAtlasSourceDecorator(baseTextureSource) {
                @Override
                protected void onDecorateBitmap(Canvas pCanvas) throws Exception {
                	drawOn(pCanvas);
                	//canvas.scale(tmaxw/canvas.getWidth(), tmaxh/canvas.getHeight());
                	//canvas.scale(tmaxw, tmaxh);
                
                	System.out.println("canvas.width="+((Object)canvas.getWidth()).toString());
                	System.out.println("canvas.height="+((Object)canvas.getHeight()).toString());
                	System.out.println("canvas.tmaxw="+((Object)tmaxw).toString());
                	System.out.println("canvas.tmaxh="+((Object)tmaxh).toString());
            		pCanvas.save();
                }
				@Override
				public BaseBitmapTextureAtlasSourceDecorator deepCopy() {
					return null;
				}
        };
        TextureRegion mDecoratedBalloonTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromSource(mBitmapTextureAtlas, decoratedTextureAtlasSource, 0, 0);
        System.out.println("@D flushImage() inited. Time = "+((Object)sync.getFieldChange(Calendar.MILLISECOND)).toString());
        sync.newMeasurement();
        mBitmapTextureAtlas.load();
        //sprite.init();
       
        System.out.println("@E flushImage() loaded. Time = "+((Object)sync.getFieldChange(Calendar.MILLISECOND)).toString());
	}
	
	public TCanvas(String ID, float X, float Y, float W, float H) {
		super(TCore.ComponentClassID.T_DEFAULT);
		assignID(ID);
		posX = X;
		posY = Y;
		w = W;
		h = H;
		tw = W;
		th = H;
		tmaxw = W;
		tmaxh = H;
		
		bmp = android.graphics.Bitmap.createBitmap((int)tmaxw, (int)tmaxh, Config.ARGB_8888);
		canvas = new Canvas(bmp);
	}
	
	public TCanvas(String ID, float X, float Y, float W, float H, float TmaxW, float TmaxH) {
		super(TCore.ComponentClassID.T_DEFAULT);
		assignID(ID);
		posX = X;
		posY = Y;
		w = W;
		h = H;
		tw = W;
		th = H;
		tmaxw = TmaxW;
		tmaxh = TmaxH;
		
		bmp = android.graphics.Bitmap.createBitmap((int)tmaxw, (int)tmaxh, Config.ARGB_8888);
		canvas = new Canvas(bmp);
	}
	
	public void setCanvasDimension(float W, float H) {
		if(tw<tmaxw&&th<tmaxh) {
			tw = W;
			th = H;
			bmp = android.graphics.Bitmap.createBitmap((int)tmaxw, (int)tmaxh, Config.ARGB_8888);
			canvas = new Canvas(bmp);
		}
	}
	
	private void drawOn(Canvas pCanvas) {
		canvas.save();
		Paint paint = new Paint();
		paint.setColor(android.graphics.Color.WHITE);
		pCanvas.drawBitmap(bmp, 0, 0, paint);
	}
	
	public void setCanvas(Canvas pCanvas) {
		canvas = pCanvas;
	}

	public Canvas getCanvas() {
		return canvas;
	}
	
	@Override
	public void onKey(KeyEvent event,BaseGameActivity activity, Scene scene) {
			
	}
	
	@Override
	public void onTouchEvent(TouchEvent pSceneTouchEvent, float X, float Y, int componentID) {
		System.out.println("HER!");
		if(blocked||parentBlocked) {
			return;
		}
		if(!visible || !parentVisible) {
			return;
		}

		if(this.mPinchZoomDetector != null) {
	             this.mPinchZoomDetector.onTouchEvent(pSceneTouchEvent);
	
	             if(this.mPinchZoomDetector.isZooming()) {
	                     //this.mScrollDetector.setEnabled(false);
	             } else {
		         		final int size = super.getEventListenersNumber();
		        		for(int it=0;it<size;++it) {
		        			super.getEventListener(it).whenTouched(pSceneTouchEvent);
		        		}
	             }
	     } else {
	             //this.mScrollDetector.onTouchEvent(pSceneTouchEvent);
	     }


	}
	
	@Override
	public void onInit(BaseGameActivity activity, Scene scene) {
		mPinchZoomDetector = new PinchZoomDetector(this);

		mBitmapTextureAtlas = new BitmapTextureAtlas(activity.getTextureManager(), (int)tmaxw, (int)tmaxh, TextureOptions.BILINEAR);
		mBitmapTextureAtlas.clearTextureAtlasSources();
		final IBitmapTextureAtlasSource baseTextureSource = new EmptyBitmapTextureAtlasSource((int)tmaxw, (int)tmaxh);
        final IBitmapTextureAtlasSource decoratedTextureAtlasSource = new BaseBitmapTextureAtlasSourceDecorator(baseTextureSource) {
                @Override
                protected void onDecorateBitmap(Canvas pCanvas) throws Exception {
                	drawOn(pCanvas);
            		pCanvas.save();
                }
				@Override
				public BaseBitmapTextureAtlasSourceDecorator deepCopy() {
					return null;
				}
        };
        TextureRegion mDecoratedBalloonTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromSource(mBitmapTextureAtlas, decoratedTextureAtlasSource, 0, 0);
        sprite = new TInteractiveSprite(w, h, mDecoratedBalloonTextureRegion, activity.getVertexBufferObjectManager());
        sprite.init(this, 0);
        
        mBitmapTextureAtlas.load();
        
        scene.attachChild(sprite);
        scene.registerTouchArea(sprite);
		
	}

	@Override
	public void onRemove(BaseGameActivity activity, Scene scene) {
		scene.detachChild(sprite);
		scene.unregisterTouchArea(sprite);
	}

	public void clear() {
		Paint paint = new Paint();
		paint.setColor(android.graphics.Color.TRANSPARENT);
		paint.setStyle(Paint.Style.FILL);
		canvas.drawPaint(paint);
		bmp.eraseColor(android.graphics.Color.TRANSPARENT);
	}
	
	public void resize(BaseGameActivity activity) {
		mBitmapTextureAtlas = new BitmapTextureAtlas(activity.getTextureManager(), (int)w, (int)h, TextureOptions.BILINEAR);
		bmp = android.graphics.Bitmap.createBitmap((int)w, (int)h, Config.ARGB_8888);
		canvas = new Canvas(bmp);
	}
	
	@Override
	public void flush(BaseGameActivity activity, Scene scene) {
		if(isVisibleChanged()) {
			if(!visible || !parentVisible) {
				scene.unregisterTouchArea(sprite);
			} else {
				scene.registerTouchArea(sprite);
			}
		}
	
		sprite.setPosition(posX+parentPosX, posY+parentPosY);
		sprite.setZIndex(indexZ+parentIndexZ);
		sprite.setVisible(visible && parentVisible);
		sprite.setAlpha(Math.min(opacity, parentOpacity));
		sprite.setWidth(w);
		sprite.setHeight(h);
		
		updateElement(activity, scene);
		//flushImage();
		
	}

	@Override
	public void onPinchZoomStarted(PinchZoomDetector pPinchZoomDetector, TouchEvent pSceneTouchEvent) {
		final int size = super.getEventListenersNumber();
		for(int it=0;it<size;++it) {
			super.getEventListener(it).whenPinchZoomStarted(pPinchZoomDetector, pSceneTouchEvent);
		}
	}

	@Override
	public void onPinchZoom(PinchZoomDetector pPinchZoomDetector, TouchEvent pTouchEvent, float pZoomFactor) {
		final int size = super.getEventListenersNumber();
		for(int it=0;it<size;++it) {
			super.getEventListener(it).whenPinchZoom(pPinchZoomDetector, pTouchEvent, pZoomFactor);
		}
	}

	@Override
	public void onPinchZoomFinished(PinchZoomDetector pPinchZoomDetector, TouchEvent pTouchEvent, float pZoomFactor) {
		final int size = super.getEventListenersNumber();
		for(int it=0;it<size;++it) {
			super.getEventListener(it).whenPinchZoomFinished(pPinchZoomDetector, pTouchEvent, pZoomFactor);
		}
	}

}
