package com.example.extremetic_tac_toe.tinyandui;

import java.util.Calendar;

import org.andengine.entity.scene.Scene;
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

import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.KeyEvent;

public class TCanvas extends TExComponent<TContextEventListener>
	implements TGenericEventHandler, IPinchZoomDetectorListener {

    private PinchZoomDetector mPinchZoomDetector = null;
    android.graphics.Bitmap bmp = null;
    Canvas canvas = new Canvas();
    BitmapTextureAtlas mBitmapTextureAtlas = null;
    TInteractiveSprite sprite = null;
    float tw = 0.0f, th = 0.0f, tmaxw = 0.0f, tmaxh = 0.0f;

    public TCanvas(final String ID, final float X, final float Y, final float W, final float H) {
	super(TCore.ComponentClassID.T_DEFAULT);
	this.assignID(ID);
	this.posX = X;
	this.posY = Y;
	this.w = W;
	this.h = H;
	this.tw = W;
	this.th = H;
	this.tmaxw = W;
	this.tmaxh = H;

	this.bmp = android.graphics.Bitmap.createBitmap((int) this.tmaxw, (int) this.tmaxh, Config.ARGB_8888);
	this.canvas = new Canvas(this.bmp);
    }

    public TCanvas(final String ID, final float X, final float Y, final float W, final float H, final float TmaxW,
	    final float TmaxH) {
	super(TCore.ComponentClassID.T_DEFAULT);
	this.assignID(ID);
	this.posX = X;
	this.posY = Y;
	this.w = W;
	this.h = H;
	this.tw = W;
	this.th = H;
	this.tmaxw = TmaxW;
	this.tmaxh = TmaxH;

	this.bmp = android.graphics.Bitmap.createBitmap((int) this.tmaxw, (int) this.tmaxh, Config.ARGB_8888);
	this.canvas = new Canvas(this.bmp);
    }

    public void clear() {
	final Paint paint = new Paint();
	paint.setColor(android.graphics.Color.TRANSPARENT);
	paint.setStyle(Paint.Style.FILL);
	this.canvas.drawPaint(paint);
	this.bmp.eraseColor(android.graphics.Color.TRANSPARENT);
    }

    @Override
    public void flush(final BaseGameActivity activity, final Scene scene) {
	if (this.isVisibleChanged()) {
	    if (!this.visible || !this.parentVisible) {
		scene.unregisterTouchArea(this.sprite);
	    } else {
		scene.registerTouchArea(this.sprite);
	    }
	}

	this.sprite.setPosition(this.posX + this.parentPosX, this.posY + this.parentPosY);
	this.sprite.setZIndex(this.indexZ + this.parentIndexZ);
	this.sprite.setVisible(this.visible && this.parentVisible);
	this.sprite.setAlpha(Math.min(this.opacity, this.parentOpacity));
	this.sprite.setWidth(this.w);
	this.sprite.setHeight(this.h);

	this.updateElement(activity, scene);
	// flushImage();

    }

    public void flushImage() {
	final TSynchronizer sync = new TSynchronizer();
	sync.newMeasurement();

	sync.newMeasurement();
	System.out.println("@A flushImage() begin. [TEST GROUP] Time = "
		+ ((Object) sync.getFieldChange(Calendar.MILLISECOND)).toString());
	sync.newMeasurement();
	this.mBitmapTextureAtlas.unload();
	System.out.println(
		"@B flushImage() unloaded. Time = " + ((Object) sync.getFieldChange(Calendar.MILLISECOND)).toString());
	sync.newMeasurement();
	this.mBitmapTextureAtlas.clearTextureAtlasSources();
	System.out.println(
		"@C flushImage() cleared. Time = " + ((Object) sync.getFieldChange(Calendar.MILLISECOND)).toString());
	sync.newMeasurement();
	final IBitmapTextureAtlasSource baseTextureSource = new EmptyBitmapTextureAtlasSource((int) this.tmaxw,
		(int) this.tmaxh);
	final IBitmapTextureAtlasSource decoratedTextureAtlasSource = new BaseBitmapTextureAtlasSourceDecorator(
		baseTextureSource) {
	    @Override
	    public BaseBitmapTextureAtlasSourceDecorator deepCopy() {
		return null;
	    }

	    @Override
	    protected void onDecorateBitmap(final Canvas pCanvas) throws Exception {
		TCanvas.this.drawOn(pCanvas);
		// canvas.scale(tmaxw/canvas.getWidth(),
		// tmaxh/canvas.getHeight());
		// canvas.scale(tmaxw, tmaxh);

		System.out.println("canvas.width=" + ((Object) TCanvas.this.canvas.getWidth()).toString());
		System.out.println("canvas.height=" + ((Object) TCanvas.this.canvas.getHeight()).toString());
		System.out.println("canvas.tmaxw=" + ((Object) TCanvas.this.tmaxw).toString());
		System.out.println("canvas.tmaxh=" + ((Object) TCanvas.this.tmaxh).toString());
		pCanvas.save();
	    }
	};
	final TextureRegion mDecoratedBalloonTextureRegion = BitmapTextureAtlasTextureRegionFactory
		.createFromSource(this.mBitmapTextureAtlas, decoratedTextureAtlasSource, 0, 0);
	System.out.println(
		"@D flushImage() inited. Time = " + ((Object) sync.getFieldChange(Calendar.MILLISECOND)).toString());
	sync.newMeasurement();
	this.mBitmapTextureAtlas.load();
	// sprite.init();

	System.out.println(
		"@E flushImage() loaded. Time = " + ((Object) sync.getFieldChange(Calendar.MILLISECOND)).toString());
    }

    public Canvas getCanvas() {
	return this.canvas;
    }

    @Override
    public void onInit(final BaseGameActivity activity, final Scene scene) {
	this.mPinchZoomDetector = new PinchZoomDetector(this);

	this.mBitmapTextureAtlas = new BitmapTextureAtlas(activity.getTextureManager(), (int) this.tmaxw,
		(int) this.tmaxh, TextureOptions.BILINEAR);
	this.mBitmapTextureAtlas.clearTextureAtlasSources();
	final IBitmapTextureAtlasSource baseTextureSource = new EmptyBitmapTextureAtlasSource((int) this.tmaxw,
		(int) this.tmaxh);
	final IBitmapTextureAtlasSource decoratedTextureAtlasSource = new BaseBitmapTextureAtlasSourceDecorator(
		baseTextureSource) {
	    @Override
	    public BaseBitmapTextureAtlasSourceDecorator deepCopy() {
		return null;
	    }

	    @Override
	    protected void onDecorateBitmap(final Canvas pCanvas) throws Exception {
		TCanvas.this.drawOn(pCanvas);
		pCanvas.save();
	    }
	};
	final TextureRegion mDecoratedBalloonTextureRegion = BitmapTextureAtlasTextureRegionFactory
		.createFromSource(this.mBitmapTextureAtlas, decoratedTextureAtlasSource, 0, 0);
	this.sprite = new TInteractiveSprite(this.w, this.h, mDecoratedBalloonTextureRegion,
		activity.getVertexBufferObjectManager());
	this.sprite.init(this, 0);

	this.mBitmapTextureAtlas.load();

	scene.attachChild(this.sprite);
	scene.registerTouchArea(this.sprite);

    }

    @Override
    public void onKey(final KeyEvent event, final BaseGameActivity activity, final Scene scene) {

    }

    @Override
    public void onPinchZoom(final PinchZoomDetector pPinchZoomDetector, final TouchEvent pTouchEvent,
	    final float pZoomFactor) {
	final int size = super.getEventListenersNumber();
	for (int it = 0; it < size; ++it) {
	    super.getEventListener(it).whenPinchZoom(pPinchZoomDetector, pTouchEvent, pZoomFactor);
	}
    }

    @Override
    public void onPinchZoomFinished(final PinchZoomDetector pPinchZoomDetector, final TouchEvent pTouchEvent,
	    final float pZoomFactor) {
	final int size = super.getEventListenersNumber();
	for (int it = 0; it < size; ++it) {
	    super.getEventListener(it).whenPinchZoomFinished(pPinchZoomDetector, pTouchEvent, pZoomFactor);
	}
    }

    @Override
    public void onPinchZoomStarted(final PinchZoomDetector pPinchZoomDetector, final TouchEvent pSceneTouchEvent) {
	final int size = super.getEventListenersNumber();
	for (int it = 0; it < size; ++it) {
	    super.getEventListener(it).whenPinchZoomStarted(pPinchZoomDetector, pSceneTouchEvent);
	}
    }

    @Override
    public void onRemove(final BaseGameActivity activity, final Scene scene) {
	scene.detachChild(this.sprite);
	scene.unregisterTouchArea(this.sprite);
    }

    @Override
    public void onTouchEvent(final TouchEvent pSceneTouchEvent, final float X, final float Y, final int componentID) {
	System.out.println("HER!");
	if (this.blocked || this.parentBlocked) {
	    return;
	}
	if (!this.visible || !this.parentVisible) {
	    return;
	}

	if (this.mPinchZoomDetector != null) {
	    this.mPinchZoomDetector.onTouchEvent(pSceneTouchEvent);

	    if (this.mPinchZoomDetector.isZooming()) {
		// this.mScrollDetector.setEnabled(false);
	    } else {
		final int size = super.getEventListenersNumber();
		for (int it = 0; it < size; ++it) {
		    super.getEventListener(it).whenTouched(pSceneTouchEvent);
		}
	    }
	} else {
	    // this.mScrollDetector.onTouchEvent(pSceneTouchEvent);
	}

    }

    public void resize(final BaseGameActivity activity) {
	this.mBitmapTextureAtlas = new BitmapTextureAtlas(activity.getTextureManager(), (int) this.w, (int) this.h,
		TextureOptions.BILINEAR);
	this.bmp = android.graphics.Bitmap.createBitmap((int) this.w, (int) this.h, Config.ARGB_8888);
	this.canvas = new Canvas(this.bmp);
    }

    public void setCanvas(final Canvas pCanvas) {
	this.canvas = pCanvas;
    }

    public void setCanvasDimension(final float W, final float H) {
	if (this.tw < this.tmaxw && this.th < this.tmaxh) {
	    this.tw = W;
	    this.th = H;
	    this.bmp = android.graphics.Bitmap.createBitmap((int) this.tmaxw, (int) this.tmaxh, Config.ARGB_8888);
	    this.canvas = new Canvas(this.bmp);
	}
    }

    private void drawOn(final Canvas pCanvas) {
	this.canvas.save();
	final Paint paint = new Paint();
	paint.setColor(android.graphics.Color.WHITE);
	pCanvas.drawBitmap(this.bmp, 0, 0, paint);
    }

}
