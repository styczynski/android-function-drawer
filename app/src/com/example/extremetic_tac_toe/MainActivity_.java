package com.example.extremetic_tac_toe;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.util.FPSLogger;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.view.RenderSurfaceView;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.ui.activity.SimpleLayoutGameActivity;

import com.example.extremetic_tac_toe.locale.logging.Logger;
import com.example.extremetic_tac_toe.tinyandui.TButton;
import com.example.extremetic_tac_toe.tinyandui.TButtonEventListener;
import com.example.extremetic_tac_toe.tinyandui.TContainer;
import com.example.extremetic_tac_toe.tinyandui.TCore;
import com.example.extremetic_tac_toe.tinyandui.TDriveOutPassAnimation;
import com.example.extremetic_tac_toe.tinyandui.TFadePassAnimation;
import com.example.extremetic_tac_toe.tinyandui.TFence;
import com.example.extremetic_tac_toe.tinyandui.TProgressBar;

import android.app.Activity;
import android.app.Fragment;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

public abstract class MainActivity_ extends SimpleLayoutGameActivity {
/*
	public static final int CAMERA_WIDTH = 480;
	public static final int CAMERA_HEIGHT = 800;

	public static TButton button;
	public static TButton button2;
	public static TButton button3;
	public static TButton button4;
	public static TContainer container;
	public static TFence fence1;
	public static TFence fence2;

	
	
	private Camera mCamera;
	private Scene mMainScene;
	
	private BitmapTextureAtlas mBitmapTextureAtlas;
	private TiledTextureRegion mPlayerTiledTextureRegion;
	
	@Override
	public EngineOptions onCreateEngineOptions() {
		this.mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		return new EngineOptions(true, ScreenOrientation.PORTRAIT_FIXED, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), this.mCamera);
	}
	
	@Override
	protected void onCreateResources() {
		Logger.post(Logger.DEBUG, "onCreateResources() by MainActivity was invoked!", "!!!");
		this.mBitmapTextureAtlas = new BitmapTextureAtlas(this.getTextureManager(), 32, 32);
		this.mPlayerTiledTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlas, this, "face_box.png", 0, 0, 1, 1);
		this.mBitmapTextureAtlas.load();
		TCore.init(this);
		for(int i=0;i<100;++i) {
			//SplashScreenActivity.updateProgressBar(i);
			for(int it=0;it<999999;++it) {}
			for(int it=0;it<999999;++it) {}
			Logger.post(Logger.DEBUG, "next step (you know what) by MainActivity was invoked! step = "+((Object)i).toString(), "!!!");
		}
	}

	
	@Override
	protected Scene onCreateScene() {
		this.mEngine.registerUpdateHandler(new FPSLogger());
		this.mMainScene = new Scene();
		this.mMainScene.setBackground(new Background(1, 1, 1));
		
		container = new TContainer(this);

		fence1 = new TFence(container, "1");
		fence2 = new TFence(container, "2");
		
		button = new TButton(0, 100);
		button.addEventListener(new TButtonEventListener() {
			@Override
			public void whenDestroyed() {
				
			}
			
			@Override
			public void whenCreated() {
				
			}
			
			@Override
			public void whenReleased() {
				
			}
			
			@Override
			public void whenPressed() {
				
			}
			
			@Override
			public void whenClicked() {
				fence1.runPassAnimation(new TDriveOutPassAnimation(TDriveOutPassAnimation.ANIMATION_TO_RIGHT, 200), fence2);
			}
		});
		
		button2 = new TButton(50, 100);
		button3 = new TButton(50, 300);
		button4 = new TButton(50, 600);
		
		button2.addEventListener(new TButtonEventListener() {
			@Override
			public void whenDestroyed() {
				
			}
			
			@Override
			public void whenCreated() {
				
			}
			
			@Override
			public void whenReleased() {
				
			}
			
			@Override
			public void whenPressed() {
				
			}
			
			@Override
			public void whenClicked() {
				fence2.runPassAnimation(new TFadePassAnimation(200), fence1);
			}
		});
		
		button3.addEventListener(new TButtonEventListener() {
			@Override
			public void whenDestroyed() {
				
			}
			
			@Override
			public void whenCreated() {
				
			}
			
			@Override
			public void whenReleased() {
				
			}
			
			@Override
			public void whenPressed() {
				
			}
			
			@Override
			public void whenClicked() {
				fence2.runPassAnimation(new TDriveOutPassAnimation(TDriveOutPassAnimation.ANIMATION_TO_TOP, 200), fence1);
			}
		});
		
		button4.addEventListener(new TButtonEventListener() {
			@Override
			public void whenDestroyed() {
				
			}
			
			@Override
			public void whenCreated() {
				
			}
			
			@Override
			public void whenReleased() {
				
			}
			
			@Override
			public void whenPressed() {
				
			}
			
			@Override
			public void whenClicked() {
				fence2.runPassAnimation(new TDriveOutPassAnimation(TDriveOutPassAnimation.ANIMATION_TO_BOTTOM, 200), fence1);				
			}
		});
		
		fence1.add(button);
		fence2.add(button2);
		fence2.add(button3);
		fence2.add(button4);
		
		fence2.setVisible(false);
		fence2.flush();
		fence1.setVisible(true);
		fence1.flush();
		
		return container.draw();
	}
	
	@Override
    protected int getLayoutID()
    {
        return R.layout.activity_main;
    }
     
    @Override
    protected int getRenderSurfaceViewID()
    {
        return R.id.gameSurfaceView;
    }
    */
}


