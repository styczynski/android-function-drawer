package com.example.extremetic_tac_toe;

import java.util.Calendar;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.input.touch.TouchEvent;
import org.andengine.input.touch.controller.MultiTouch;
import org.andengine.input.touch.detector.PinchZoomDetector;
import org.andengine.ui.activity.SimpleLayoutGameActivity;

import com.example.extremetic_tac_toe.tinyandui.TCanvas;
import com.example.extremetic_tac_toe.tinyandui.TContextEventListener;
import com.example.extremetic_tac_toe.tinyandui.TDelayer;
import com.example.extremetic_tac_toe.tinyandui.TSynchronizer;

import android.graphics.Paint;
import android.os.Bundle;
import android.widget.Toast;

public class DrawingPanelActivity extends SimpleLayoutGameActivity {
    public static final int CAMERA_HEIGHT = (int) (800.0f * DrawingPanelActivity.CAMERA_ZOOM_FACTOR);

    public static final int CAMERA_WIDTH = (int) (480.0f * DrawingPanelActivity.CAMERA_ZOOM_FACTOR);

    public static final float CAMERA_ZOOM_FACTOR = 0.5f;

    public static DrawingPanelActivity runningActivity = null;

    private Camera camera;
    private TCanvas canvas;
    private Scene mainScene;
    protected FunctionDrawer fd;

    @Override
    public void onCreate(final Bundle pSavedInstanceState) {
	super.onCreate(pSavedInstanceState);
	DrawingPanelActivity.runningActivity = this;
	System.out.println("UPDATED STATE! HUUUURAY! :D");

    }

    @Override
    public EngineOptions onCreateEngineOptions() {

	this.camera = new Camera(0, 0, DrawingPanelActivity.CAMERA_WIDTH, DrawingPanelActivity.CAMERA_HEIGHT);
	final EngineOptions engineOptions = new EngineOptions(true, ScreenOrientation.PORTRAIT_SENSOR,
		new FillResolutionPolicy(), this.camera);

	engineOptions.getTouchOptions().setNeedsMultiTouch(true);

	if (MultiTouch.isSupported(this)) {
	    if (MultiTouch.isSupportedDistinct(this)) {
		Toast.makeText(this, "MultiTouch detected --> Both controls will work properly!", Toast.LENGTH_SHORT)
			.show();
	    } else {
		Toast.makeText(this,
			"MultiTouch detected, but your device has problems distinguishing between fingers.\n\nControls are placed at different vertical locations.",
			Toast.LENGTH_LONG).show();
	    }
	} else {
	    Toast.makeText(this,
		    "Sorry your device does NOT support MultiTouch!\n\n(Falling back to SingleTouch.)\n\nControls are placed at different vertical locations.",
		    Toast.LENGTH_LONG).show();
	}

	return engineOptions;
    }

    @Override
    public void onResume() {
	super.onCreateEngine(this.onCreateEngineOptions());
	super.onResume();
	DrawingPanelActivity.runningActivity = this;
	System.out.println("UPDATED STATE! HUUUURAY! :D");
    }

    @Override
    public void onWindowFocusChanged(final boolean hasFocus) {
	this.updateFunctions();
	System.out.println("#DRAWING Painting canvas...");
	final Paint paint = new Paint();
	paint.setColor(android.graphics.Color.WHITE);
	this.canvas.getCanvas().drawPaint(paint);
	paint.setAntiAlias(true);
	// paint.setTypeface(Typeface.defaultFromStyle(16974266));
	paint.setTextSize(10);
	System.out.println("#DRAWING (Painting by FunctionDrawer...)");

	final TSynchronizer sync = new TSynchronizer();
	sync.newMeasurement();

	this.fd.paintComponent(this.canvas.getCanvas(), paint, this.canvas.getCanvas().getWidth(),
		this.canvas.getCanvas().getHeight());
	sync.newMeasurement();
	System.out.println(
		"#DRAWING Canvas painted. Time = " + ((Object) sync.getFieldChange(Calendar.MILLISECOND)).toString());
	System.out.println("#DRAWING Flushing image...");
	this.canvas.flushImage();
	sync.newMeasurement();
	System.out.println(
		"#DRAWING Image flushed. Time = " + ((Object) sync.getFieldChange(Calendar.MILLISECOND)).toString());
	this.canvas.flush(DrawingPanelActivity.this, this.mainScene);
	sync.newMeasurement();
	System.out.println("#DRAWING Container flushed! Time = "
		+ ((Object) sync.getFieldChange(Calendar.MILLISECOND)).toString());

    }

    public void updateFunctions() {
	this.fd.setFunctions(Core.functions);
    }

    protected void flush() {
	try {
	    this.updateFunctions();
	    System.out.println("#DRAWING Painting canvas...");
	    final Paint paint = new Paint();
	    paint.setColor(android.graphics.Color.WHITE);
	    this.canvas.getCanvas().drawPaint(paint);
	    paint.setAntiAlias(true);
	    // paint.setTypeface(Typeface.defaultFromStyle(16974266));
	    paint.setTextSize(10);
	    System.out.println("#DRAWING (Painting by FunctionDrawer...)");

	    final TSynchronizer sync = new TSynchronizer();
	    sync.newMeasurement();

	    this.fd.paintComponent(this.canvas.getCanvas(), paint, this.canvas.getCanvas().getWidth(),
		    this.canvas.getCanvas().getHeight());
	    sync.newMeasurement();
	    System.out.println("#DRAWING Canvas painted. Time = "
		    + ((Object) sync.getFieldChange(Calendar.MILLISECOND)).toString());
	    System.out.println("#DRAWING Flushing image...");
	    this.canvas.flushImage();
	    sync.newMeasurement();
	    System.out.println("#DRAWING Image flushed. Time = "
		    + ((Object) sync.getFieldChange(Calendar.MILLISECOND)).toString());
	    this.canvas.flush(DrawingPanelActivity.this, this.mainScene);
	    sync.newMeasurement();
	    System.out.println("#DRAWING Container flushed! Time = "
		    + ((Object) sync.getFieldChange(Calendar.MILLISECOND)).toString());
	} catch (final Throwable t) {
	    System.out.println("Error while flushing drawing panel! :(");
	}
    }

    @Override
    protected int getLayoutID() {
	return R.layout.drawing_panel_layout;
    }

    @Override
    protected int getRenderSurfaceViewID() {
	return R.id.gameSurfaceView;
    }

    @Override
    protected void onCreateResources() {

    }

    @Override
    protected Scene onCreateScene() {
	final Scene mainscene = new Scene();

	this.fd = new FunctionDrawer();
	this.fd.setAdditionalWorkAreaHeight(200);
	this.canvas = new TCanvas("", 0, 0, DrawingPanelActivity.CAMERA_WIDTH, DrawingPanelActivity.CAMERA_HEIGHT);
	this.canvas.addEventListener(new TContextEventListener() {

	    private static final float mouseSensivity = 0.2f;
	    TDelayer delayer = new TDelayer();
	    boolean TouchState = false;

	    @Override
	    public void whenCreated() {

	    }

	    @Override
	    public void whenDestroyed() {

	    }

	    @Override
	    public void whenPinchZoom(final PinchZoomDetector pPinchZoomDetector, final TouchEvent pTouchEvent,
		    final float pZoomFactor) {

	    }

	    @Override
	    public void whenPinchZoomFinished(final PinchZoomDetector pPinchZoomDetector, final TouchEvent pTouchEvent,
		    final float pZoomFactor) {
		DrawingPanelActivity.this.updateFunctions();
		// if(true) return;
		// pZoomFactor = 1.0f/pZoomFactor;
		System.out.println("Prev zoom factor was = " + ((Object) pZoomFactor).toString());

		DrawingPanelActivity.this.fd.zoomed(pZoomFactor, mouseSensivity);
		System.out.println("Zoom factor was = " + ((Object) pZoomFactor).toString());

		/*
		 * System.out.println("#DRAWING Painting canvas..."); Paint
		 * paint = new Paint();
		 * paint.setColor(android.graphics.Color.WHITE);
		 * canvas.getCanvas().drawPaint(paint);
		 * paint.setAntiAlias(true);
		 * paint.setTypeface(Typeface.defaultFromStyle(16974266));
		 * paint.setTextSize(10); System.out.println(
		 * "#DRAWING (Painting by FunctionDrawer...)");
		 * fd.paintComponent(canvas.getCanvas(), paint, 500, 500);
		 * System.out.println("#DRAWING Canvas painted.");
		 * System.out.println("#DRAWING Flushing image...");
		 * canvas.flushImage(); System.out.println(
		 * "#DRAWING Image flushed."); container.flush();
		 * System.out.println("#DRAWING Container flushed!");
		 */

		System.out.println("#DRAWING Painting canvas...");
		final Paint paint = new Paint();
		paint.setColor(android.graphics.Color.WHITE);
		DrawingPanelActivity.this.canvas.getCanvas().drawPaint(paint);
		paint.setAntiAlias(true);
		// paint.setTypeface(Typeface.defaultFromStyle(16974266));
		paint.setTextSize(10);
		System.out.println("#DRAWING (Painting by FunctionDrawer...)");

		final TSynchronizer sync = new TSynchronizer();
		sync.newMeasurement();

		DrawingPanelActivity.this.fd.paintComponent(DrawingPanelActivity.this.canvas.getCanvas(), paint,
			DrawingPanelActivity.this.canvas.getCanvas().getWidth(),
			DrawingPanelActivity.this.canvas.getCanvas().getHeight());
		sync.newMeasurement();
		System.out.println("#DRAWING Canvas painted. Time = "
			+ ((Object) sync.getFieldChange(Calendar.MILLISECOND)).toString());
		System.out.println("#DRAWING Flushing image...");
		DrawingPanelActivity.this.canvas.flushImage();
		sync.newMeasurement();
		System.out.println("#DRAWING Image flushed. Time = "
			+ ((Object) sync.getFieldChange(Calendar.MILLISECOND)).toString());
		DrawingPanelActivity.this.canvas.flush(DrawingPanelActivity.this, DrawingPanelActivity.this.mainScene);
		sync.newMeasurement();
		System.out.println("#DRAWING Container flushed! Time = "
			+ ((Object) sync.getFieldChange(Calendar.MILLISECOND)).toString());

		this.delayer = new TDelayer(500);
	    }

	    @Override
	    public void whenPinchZoomStarted(final PinchZoomDetector pPinchZoomDetector, final TouchEvent pTouchEvent) {

	    }

	    @Override
	    public void whenTouched(final TouchEvent pSceneTouchEvent) {
		DrawingPanelActivity.this.updateFunctions();
		if (!this.delayer.isRealized()) {
		    return;
		}
		// System.out.println("Pos.X =
		// "+((Object)pSceneTouchEvent.getX()).toString());
		// System.out.println("Pos.Y =
		// "+((Object)pSceneTouchEvent.getY()).toString());
		// fd.mouseDragged(pSceneTouchEvent);

		if (pSceneTouchEvent.isActionDown() || this.TouchState == false) {
		    this.TouchState = true;
		    DrawingPanelActivity.this.fd.setLastPos((int) pSceneTouchEvent.getX(),
			    (int) pSceneTouchEvent.getY());
		}

		if (pSceneTouchEvent.isActionUp() || pSceneTouchEvent.isActionCancel()) {
		    this.TouchState = false;
		    DrawingPanelActivity.this.fd.mouseDragged(pSceneTouchEvent, mouseSensivity);
		    System.out.println("#DRAWING Painting canvas...");
		    final Paint paint = new Paint();
		    paint.setColor(android.graphics.Color.WHITE);
		    DrawingPanelActivity.this.canvas.getCanvas().drawPaint(paint);
		    paint.setAntiAlias(true);
		    // paint.setTypeface(Typeface.defaultFromStyle(16974266));
		    paint.setTextSize(10);
		    System.out.println("#DRAWING (Painting by FunctionDrawer...)");

		    final TSynchronizer sync = new TSynchronizer();
		    sync.newMeasurement();

		    DrawingPanelActivity.this.fd.paintComponent(DrawingPanelActivity.this.canvas.getCanvas(), paint,
			    DrawingPanelActivity.this.canvas.getCanvas().getWidth(),
			    DrawingPanelActivity.this.canvas.getCanvas().getHeight());
		    sync.newMeasurement();
		    System.out.println("#DRAWING Canvas painted. Time = "
			    + ((Object) sync.getFieldChange(Calendar.MILLISECOND)).toString());
		    System.out.println("#DRAWING Flushing image...");
		    DrawingPanelActivity.this.canvas.flushImage();
		    sync.newMeasurement();
		    System.out.println("#DRAWING Image flushed. Time = "
			    + ((Object) sync.getFieldChange(Calendar.MILLISECOND)).toString());
		    DrawingPanelActivity.this.canvas.flush(DrawingPanelActivity.this,
			    DrawingPanelActivity.this.mainScene);
		    sync.newMeasurement();
		    System.out.println("#DRAWING Container flushed! Time = "
			    + ((Object) sync.getFieldChange(Calendar.MILLISECOND)).toString());

		}

	    }

	});
	this.canvas.onInit(this, mainscene);
	this.updateFunctions();

	System.out.println("#DRAWING Painting canvas...");
	final Paint paint = new Paint();
	paint.setColor(android.graphics.Color.WHITE);
	this.canvas.getCanvas().drawPaint(paint);
	paint.setAntiAlias(true);
	// paint.setTypeface(Typeface.defaultFromStyle(16974266));
	paint.setTextSize(10);
	System.out.println("#DRAWING (Painting by FunctionDrawer...)");

	final TSynchronizer sync = new TSynchronizer();
	sync.newMeasurement();

	this.fd.paintComponent(this.canvas.getCanvas(), paint, this.canvas.getCanvas().getWidth(),
		this.canvas.getCanvas().getHeight());
	sync.newMeasurement();
	System.out.println(
		"#DRAWING Canvas painted. Time = " + ((Object) sync.getFieldChange(Calendar.MILLISECOND)).toString());
	System.out.println("#DRAWING Flushing image...");
	this.canvas.flushImage();
	sync.newMeasurement();
	System.out.println(
		"#DRAWING Image flushed. Time = " + ((Object) sync.getFieldChange(Calendar.MILLISECOND)).toString());
	this.canvas.flush(DrawingPanelActivity.this, this.mainScene);
	sync.newMeasurement();
	System.out.println("#DRAWING Container flushed! Time = "
		+ ((Object) sync.getFieldChange(Calendar.MILLISECOND)).toString());

	return mainscene;
    }
}