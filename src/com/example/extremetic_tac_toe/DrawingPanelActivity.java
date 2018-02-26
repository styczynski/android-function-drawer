package com.example.extremetic_tac_toe;

import java.util.Calendar;
import java.util.Vector;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.input.touch.controller.MultiTouch;
import org.andengine.input.touch.detector.PinchZoomDetector;
import org.andengine.opengl.font.FontFactory;
import org.andengine.ui.activity.SimpleLayoutGameActivity;

import com.example.extremetic_tac_toe.tinyandui.TCanvas;
import com.example.extremetic_tac_toe.tinyandui.TContextEventListener;
import com.example.extremetic_tac_toe.tinyandui.TDelayer;
import com.example.extremetic_tac_toe.tinyandui.TSynchronizer;

import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

public class DrawingPanelActivity extends SimpleLayoutGameActivity 
{
    @Override
    protected int getLayoutID() {
        return R.layout.drawing_panel_layout;
    }
     
    @Override
    protected int getRenderSurfaceViewID() {
        return R.id.gameSurfaceView;
    }

	private Camera camera;
	
	public static DrawingPanelActivity runningActivity = null;
	
	public static final float CAMERA_ZOOM_FACTOR = 0.5f;
	public static final int CAMERA_WIDTH = (int)(480.0f*CAMERA_ZOOM_FACTOR);
	public static final int CAMERA_HEIGHT = (int)(800.0f*CAMERA_ZOOM_FACTOR);
	protected FunctionDrawer fd;
	private TCanvas canvas;
	private Scene mainScene;
	
	public void updateFunctions() {
		fd.setFunctions(Core.functions);
	}
	
	protected void flush() {
		try {
			updateFunctions();
			System.out.println("#DRAWING Painting canvas...");
			Paint paint = new Paint();
			paint.setColor(android.graphics.Color.WHITE);
			canvas.getCanvas().drawPaint(paint);
			paint.setAntiAlias(true);
			paint.setTypeface(Typeface.defaultFromStyle(16974266));
			paint.setTextSize(10);
			System.out.println("#DRAWING (Painting by FunctionDrawer...)");
			
			TSynchronizer sync = new TSynchronizer();
			sync.newMeasurement();
			
			fd.paintComponent(canvas.getCanvas(), paint, canvas.getCanvas().getWidth(), canvas.getCanvas().getHeight());
			sync.newMeasurement();
			System.out.println("#DRAWING Canvas painted. Time = "+((Object)sync.getFieldChange(Calendar.MILLISECOND)).toString());
			System.out.println("#DRAWING Flushing image...");
			canvas.flushImage();
			sync.newMeasurement();
			System.out.println("#DRAWING Image flushed. Time = "+((Object)sync.getFieldChange(Calendar.MILLISECOND)).toString());
			canvas.flush(DrawingPanelActivity.this, mainScene);
			sync.newMeasurement();
			System.out.println("#DRAWING Container flushed! Time = "+((Object)sync.getFieldChange(Calendar.MILLISECOND)).toString());
		} catch(Throwable t) {
			System.out.println("Error while flushing drawing panel! :(");
		}
	}
	
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		updateFunctions();
		System.out.println("#DRAWING Painting canvas...");
		Paint paint = new Paint();
		paint.setColor(android.graphics.Color.WHITE);
		canvas.getCanvas().drawPaint(paint);
		paint.setAntiAlias(true);
		paint.setTypeface(Typeface.defaultFromStyle(16974266));
		paint.setTextSize(10);
		System.out.println("#DRAWING (Painting by FunctionDrawer...)");
		
		TSynchronizer sync = new TSynchronizer();
		sync.newMeasurement();
		
		fd.paintComponent(canvas.getCanvas(), paint, canvas.getCanvas().getWidth(), canvas.getCanvas().getHeight());
		sync.newMeasurement();
		System.out.println("#DRAWING Canvas painted. Time = "+((Object)sync.getFieldChange(Calendar.MILLISECOND)).toString());
		System.out.println("#DRAWING Flushing image...");
		canvas.flushImage();
		sync.newMeasurement();
		System.out.println("#DRAWING Image flushed. Time = "+((Object)sync.getFieldChange(Calendar.MILLISECOND)).toString());
		canvas.flush(DrawingPanelActivity.this, mainScene);
		sync.newMeasurement();
		System.out.println("#DRAWING Container flushed! Time = "+((Object)sync.getFieldChange(Calendar.MILLISECOND)).toString());
	
	}
	
	@Override 
	public void onCreate(Bundle pSavedInstanceState) {
		super.onCreate(pSavedInstanceState);
		runningActivity = this;
		System.out.println("UPDATED STATE! HUUUURAY! :D");
		
	}
	
	@Override
	public void onResume() {
		super.onCreateEngine(this.onCreateEngineOptions());
		super.onResume();
		runningActivity = this;
		System.out.println("UPDATED STATE! HUUUURAY! :D");
	}
	
	@Override
	public EngineOptions onCreateEngineOptions() {
		
		
		camera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		EngineOptions engineOptions = new EngineOptions(true, ScreenOrientation.PORTRAIT_SENSOR, new FillResolutionPolicy(), camera);

		engineOptions.getTouchOptions().setNeedsMultiTouch(true);

        if(MultiTouch.isSupported(this)) {
            if(MultiTouch.isSupportedDistinct(this)) {
                Toast.makeText(this, "MultiTouch detected --> Both controls will work properly!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "MultiTouch detected, but your device has problems distinguishing between fingers.\n\nControls are placed at different vertical locations.", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "Sorry your device does NOT support MultiTouch!\n\n(Falling back to SingleTouch.)\n\nControls are placed at different vertical locations.", Toast.LENGTH_LONG).show();
        }
        
		return engineOptions;
	}

	@Override
	protected void onCreateResources() {
	
	}

	@Override
	protected Scene onCreateScene() {
		Scene mainscene = new Scene();
		
		
		fd = new FunctionDrawer();
		fd.setAdditionalWorkAreaHeight(200);
		canvas = new TCanvas("", 0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		canvas.addEventListener(new TContextEventListener() {

			boolean TouchState = false;
			TDelayer delayer = new TDelayer();
			private static final float mouseSensivity = 0.2f;
			
			@Override
			public void whenTouched(TouchEvent pSceneTouchEvent) {
				updateFunctions();
				if(!delayer.isRealized()) {
					return;
				}
				//System.out.println("Pos.X = "+((Object)pSceneTouchEvent.getX()).toString());
				//System.out.println("Pos.Y = "+((Object)pSceneTouchEvent.getY()).toString());
				//fd.mouseDragged(pSceneTouchEvent);
				
				
				
				if(pSceneTouchEvent.isActionDown()||TouchState == false) {
					TouchState = true;
					fd.setLastPos((int)pSceneTouchEvent.getX(), (int)pSceneTouchEvent.getY());
				}
				
				if(pSceneTouchEvent.isActionUp()||pSceneTouchEvent.isActionCancel()) {
					TouchState = false;
					fd.mouseDragged(pSceneTouchEvent, mouseSensivity);
					System.out.println("#DRAWING Painting canvas...");
					Paint paint = new Paint();
					paint.setColor(android.graphics.Color.WHITE);
					canvas.getCanvas().drawPaint(paint);
					paint.setAntiAlias(true);
					paint.setTypeface(Typeface.defaultFromStyle(16974266));
					paint.setTextSize(10);
					System.out.println("#DRAWING (Painting by FunctionDrawer...)");
					
					TSynchronizer sync = new TSynchronizer();
					sync.newMeasurement();
					
					fd.paintComponent(canvas.getCanvas(), paint, canvas.getCanvas().getWidth(), canvas.getCanvas().getHeight());
					sync.newMeasurement();
					System.out.println("#DRAWING Canvas painted. Time = "+((Object)sync.getFieldChange(Calendar.MILLISECOND)).toString());
					System.out.println("#DRAWING Flushing image...");
					canvas.flushImage();
					sync.newMeasurement();
					System.out.println("#DRAWING Image flushed. Time = "+((Object)sync.getFieldChange(Calendar.MILLISECOND)).toString());
					canvas.flush(DrawingPanelActivity.this, mainScene);
					sync.newMeasurement();
					System.out.println("#DRAWING Container flushed! Time = "+((Object)sync.getFieldChange(Calendar.MILLISECOND)).toString());
				
				}
				
			}
			
			@Override
			public void whenCreated() {
				
			}

			@Override
			public void whenDestroyed() {
				
			}

			@Override
			public void whenPinchZoom(PinchZoomDetector pPinchZoomDetector, TouchEvent pTouchEvent, float pZoomFactor) {
				
			}

			@Override
			public void whenPinchZoomStarted(PinchZoomDetector pPinchZoomDetector, TouchEvent pTouchEvent) {
				
			}

			@Override
			public void whenPinchZoomFinished(PinchZoomDetector pPinchZoomDetector, TouchEvent pTouchEvent, float pZoomFactor) {
				updateFunctions();
				//if(true) return;
				//pZoomFactor = 1.0f/pZoomFactor;
				System.out.println("Prev zoom factor was = "+((Object)pZoomFactor).toString());
				
				
				
				fd.zoomed(pZoomFactor, mouseSensivity);
				System.out.println("Zoom factor was = "+((Object)pZoomFactor).toString());
				
				/*System.out.println("#DRAWING Painting canvas...");
				Paint paint = new Paint();
				paint.setColor(android.graphics.Color.WHITE);
				canvas.getCanvas().drawPaint(paint);
				paint.setAntiAlias(true);
				paint.setTypeface(Typeface.defaultFromStyle(16974266));
				paint.setTextSize(10);
				System.out.println("#DRAWING (Painting by FunctionDrawer...)");
				fd.paintComponent(canvas.getCanvas(), paint, 500, 500);
				System.out.println("#DRAWING Canvas painted.");
				System.out.println("#DRAWING Flushing image...");
				canvas.flushImage();
				System.out.println("#DRAWING Image flushed.");
				container.flush();
				System.out.println("#DRAWING Container flushed!");*/
				
				
				System.out.println("#DRAWING Painting canvas...");
				Paint paint = new Paint();
				paint.setColor(android.graphics.Color.WHITE);
				canvas.getCanvas().drawPaint(paint);
				paint.setAntiAlias(true);
				paint.setTypeface(Typeface.defaultFromStyle(16974266));
				paint.setTextSize(10);
				System.out.println("#DRAWING (Painting by FunctionDrawer...)");
				
				TSynchronizer sync = new TSynchronizer();
				sync.newMeasurement();
				
				fd.paintComponent(canvas.getCanvas(), paint, canvas.getCanvas().getWidth(), canvas.getCanvas().getHeight());
				sync.newMeasurement();
				System.out.println("#DRAWING Canvas painted. Time = "+((Object)sync.getFieldChange(Calendar.MILLISECOND)).toString());
				System.out.println("#DRAWING Flushing image...");
				canvas.flushImage();
				sync.newMeasurement();
				System.out.println("#DRAWING Image flushed. Time = "+((Object)sync.getFieldChange(Calendar.MILLISECOND)).toString());
				canvas.flush(DrawingPanelActivity.this, mainScene);
				sync.newMeasurement();
				System.out.println("#DRAWING Container flushed! Time = "+((Object)sync.getFieldChange(Calendar.MILLISECOND)).toString());
			
				
				delayer = new TDelayer(500);
			}
			
			
		});
		canvas.onInit(this, mainscene);
		updateFunctions();
		
		System.out.println("#DRAWING Painting canvas...");
		Paint paint = new Paint();
		paint.setColor(android.graphics.Color.WHITE);
		canvas.getCanvas().drawPaint(paint);
		paint.setAntiAlias(true);
		paint.setTypeface(Typeface.defaultFromStyle(16974266));
		paint.setTextSize(10);
		System.out.println("#DRAWING (Painting by FunctionDrawer...)");
		
		TSynchronizer sync = new TSynchronizer();
		sync.newMeasurement();
		
		fd.paintComponent(canvas.getCanvas(), paint, canvas.getCanvas().getWidth(), canvas.getCanvas().getHeight());
		sync.newMeasurement();
		System.out.println("#DRAWING Canvas painted. Time = "+((Object)sync.getFieldChange(Calendar.MILLISECOND)).toString());
		System.out.println("#DRAWING Flushing image...");
		canvas.flushImage();
		sync.newMeasurement();
		System.out.println("#DRAWING Image flushed. Time = "+((Object)sync.getFieldChange(Calendar.MILLISECOND)).toString());
		canvas.flush(DrawingPanelActivity.this, mainScene);
		sync.newMeasurement();
		System.out.println("#DRAWING Container flushed! Time = "+((Object)sync.getFieldChange(Calendar.MILLISECOND)).toString());
	
		
		return mainscene;
	}
}