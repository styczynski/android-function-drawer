package com.example.extremetic_tac_toe;
 
import java.util.Vector;

import com.example.extremetic_tac_toe.FunctionPanelActivity.ListAdapter;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.app.TabActivity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;
//import android.support.v7.widget.PopupMenu;
 
@SuppressLint("NewApi") public class MainActivity extends TabActivity {
    /** Called when the activity is first created. */
	
	protected static MainActivity runningMain = null;
	protected static FunctionPanelActivity panelInstance = null;
	
	private static final int MENU_NEW_ENTRY_FUNCTION   = 1;
	private static final int MENU_NEW_ENTRY_VARIABLE   = 2;
	private static final int MENU_NEW_ENTRY_CURVE      = 3;
	private static final int MENU_NEW_ENTRY_LUA_SCRIPT = 4;
	
	private static final int MENU_OP_SAVE_FUNCTION = 100;
	private static final int MENU_OP_SAVE_FUNCTION_AS = 101;
	private static final int MENU_OP_LOAD_FUNCTION = 102;
	private static final int MENU_OP_SETTINGS = 103;
	
	static ImageButton menuButton;
	static Drawable menuButtonBgOrigin = null;
	static ImageButton saveButton;
	
	
	static {
		FunctionVariable funct0 = (FunctionVariable)FunctionTypeSelector.select(new Function("a=5"));
		funct0.eqaution = "a=5";
		funct0.pbMinValue=5;
		funct0.pbMaxValue=25;
		funct0.pbStepValue=0.5;
		funct0.pbStepAuto=false;
		Core.functions.add(funct0);
		Function funct1 = new Function("a");
		funct1.eqaution = "a";
		Core.functions.add(funct1);
	}
	private AnimatedTabHostListener tabAnimator;
	
	
	private void newEntryLuaScript() {
		Function funct = new Function("");
    	funct.object_type = Function.ENTRY_TYPE_LUA_DRAWER;
    	/*funct.eqaution = "fd.setColor(\"black\");\r\n" + 
    			" for i=-1000,1000 do\r\n" + 
    			" 	fd.drawPoint(i, jmath.sin(i)*500);\r\n" + 
    			" end";*/
    	
    	
    	funct.eqaution = "\r\n" + 
    			"function onFunctionLooking(x, y)\r\n" + 
    			"	return(\"@ = \" .. x .. \", \" .. y );\r\n" + 
    			"end\r\n" + 
    			"\r\n" + 
    			"fd.setColor(\"black\");\r\n" + 
    			"\r\n" + 
    			"--step = fd.getSettings().graphingStep;\r\n" + 
    			"step = (fd.getCoord().right-fd.getCoord().left)/2;\r\n" + 
    			"print(\"WTFX = \" .. fd.getCoord().left .. \", \" .. fd.getCoord().right);\r\n" + 
    			"for x=fd.getCoord().left, fd.getCoord().right, step do\r\n" + 
    			"	fd.drawLine( x-step, x-step, x, x );\r\n" + 
    			"end";
    	
    	funct.lua_name = "some lua script";
    	Core.functions.add(funct);
	}
	
	private void newEntryFunction() {
		Function funct = new Function("");
    	funct.object_type = Function.ENTRY_TYPE_FUNCTION;
    	funct.eqaution = funct.prp_equation = "3*x+5";
    	Core.functions.add(funct);
	}
	
	private void newEntryCurve() {
		Function funct = new Function("");
    	funct.object_type = Function.ENTRY_TYPE_CURVE_OBJECT;
    	funct.eqaution = funct.prp_equation = "x^3=y^2+1";
    	Core.functions.add(funct);
	}
	
	private void newEntryVariable() {
		String varexp = new String(new char[]{ (char)((Math.random()*('z'-'a')+'a')) });
    	varexp += "=";
    	varexp += ((Object)((int)(Math.random()*500))).toString();
    	Function funct = FunctionTypeSelector.select(new Function(varexp));
    	funct.eqaution = varexp;
    	Core.functions.add(funct);
   
	}
	
	protected void runAnimation(Animator anim) {
		anim.start();
	}
	
	protected void whenSwitchedToDrawingPanel() {
		menuButton.setImageResource(android.R.drawable.ic_menu_search);
		saveButton.setImageResource(android.R.drawable.ic_menu_upload_you_tube);
		
		
		//saveButton.setVisibility(View.GONE);
		
		saveButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Console.whenActivateButtonPressed();
			}
		});
		
		menuButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(FunctionDrawer.functionLookingMode) {
					v.setBackground(menuButtonBgOrigin);
					FunctionDrawer.functionLookingMode = false;
				} else {
					v.setBackgroundColor(android.graphics.Color.MAGENTA);
					FunctionDrawer.functionLookingMode = true;
				}
			}
		});
		
		if(!FunctionDrawer.functionLookingMode) {
			menuButton.setBackground(menuButtonBgOrigin);
		} else {
			menuButton.setBackgroundColor(android.graphics.Color.MAGENTA);
		}
	}
	
	protected void whenSwitchedToFunctionPanel() {
		FunctionDrawer.functionLookingMode = false;
		
		menuButton.setBackground(menuButtonBgOrigin);
		
		menuButton.setImageResource(android.R.drawable.ic_menu_sort_by_size);
        menuButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				PopupMenu popup = new PopupMenu(MainActivity.this, menuButton);
                popup.getMenu().add(Menu.NONE, MENU_NEW_ENTRY_FUNCTION, 1, "Add new function");
                popup.getMenu().add(Menu.NONE, MENU_NEW_ENTRY_VARIABLE, 2, "Add new variable");
                popup.getMenu().add(Menu.NONE, MENU_NEW_ENTRY_CURVE, 3, "Add new curve");
                popup.getMenu().add(Menu.NONE, MENU_NEW_ENTRY_LUA_SCRIPT, 4, "Add new Lua script");
                
                popup.setOnMenuItemClickListener(new OnMenuItemClickListener() {
					@Override
					public boolean onMenuItemClick(MenuItem item) {
						switch(item.getItemId()) {
							case MENU_NEW_ENTRY_FUNCTION:
								newEntryFunction();
								break;
							case MENU_NEW_ENTRY_VARIABLE:
								newEntryVariable();
								break;
							case MENU_NEW_ENTRY_CURVE:
								newEntryCurve();
								break;
							case MENU_NEW_ENTRY_LUA_SCRIPT:
								newEntryLuaScript();
								break;
						}
						if(panelInstance!=null) {
							panelInstance.reload();
						}
						return false;
					}
                });
                
                popup.show();
			}
        });

        
        saveButton.setImageResource(android.R.drawable.ic_menu_save);
        //saveButton.setVisibility(View.VISIBLE);
        saveButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				PopupMenu popup = new PopupMenu(MainActivity.this, menuButton);
                popup.getMenu().add(Menu.NONE, MENU_OP_SAVE_FUNCTION, 1, "Save functions");
                popup.getMenu().add(Menu.NONE, MENU_OP_SAVE_FUNCTION_AS, 1, "Save functions as...");
                popup.getMenu().add(Menu.NONE, MENU_OP_LOAD_FUNCTION, 2, "Load functions");
                popup.getMenu().add(Menu.NONE, MENU_OP_SETTINGS, 3, "Application settings");
                
                popup.setOnMenuItemClickListener(new OnMenuItemClickListener() {
					@Override
					public boolean onMenuItemClick(MenuItem item) {
						switch(item.getItemId()) {
							case MENU_OP_SETTINGS:
								Intent intent = new Intent(MainActivity.this, SettingsPanelActivity.class);
						    	startActivity(intent);
								break;
							case MENU_OP_LOAD_FUNCTION:
								FunctionPanelActivity.onLoadFunction(MainActivity.this);
								break;
							case MENU_OP_SAVE_FUNCTION:
								FunctionPanelActivity.onSaveFunction(MainActivity.this);
								break;
							case MENU_OP_SAVE_FUNCTION_AS:
								FunctionPanelActivity.onSaveFunctionAs(MainActivity.this);
								break;
						}
						return false;
					}
                });
                
                popup.show();
			}
        });
		
	}
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        if(Core.clearAlwaysAtStartup) {
        	Core.functions.clear();
        }
        if(getIntent().getDataString()!=null) {
	        Vector<Function> fv = Function.importFile(this.getApplicationContext(), getIntent().getData());
			final int siz = fv.size();
			for(int it=0;it<siz;++it) {
				if(!Core.integrateFunction(fv.get(it))) {}
			}
        }
        
        setContentView(R.layout.activity_main);
        
        menuButton = (ImageButton)findViewById(R.id.main_activity_menu_button);
        saveButton = (ImageButton)findViewById(R.id.main_activity_save_button);
        TabHost tabHost = getTabHost();
        
        menuButtonBgOrigin = menuButton.getBackground();
        
        whenSwitchedToFunctionPanel();
        
        // Tab for Photos
        TabSpec photospec = tabHost.newTabSpec("EquationPanel");
        // setting Title and Icon for the Tab
        photospec.setIndicator("Equation Panel", getResources().getDrawable(R.drawable.ic_launcher));
        Intent photosIntent = new Intent(this, FunctionPanelActivity.class);
        photospec.setContent(photosIntent);
         
        // Tab for Songs
        TabSpec songspec = tabHost.newTabSpec("GraphingPanel");       
        songspec.setIndicator("Graphing Panel", getResources().getDrawable(R.drawable.ic_launcher));
        Intent songsIntent = new Intent(this, DrawingPanelActivity.class);
        songspec.setContent(songsIntent);
         
        // Adding all TabSpec to TabHost
        tabHost.addTab(photospec); // Adding photos tab
        tabHost.addTab(songspec); // Adding songs tab
        
        tabAnimator = new AnimatedTabHostListener(tabHost);
        tabHost.setOnTabChangedListener(tabAnimator);
        
        runningMain = this;
    }
    
    
    public class AnimatedTabHostListener implements OnTabChangeListener {

        private static final int ANIMATION_TIME = 240;
        private TabHost tabHost;
        private View previousView;
        private View currentView;
        private int currentTab;

        public AnimatedTabHostListener(TabHost tabHost)
        {
            this.tabHost = tabHost;
            this.previousView = tabHost.getCurrentView();
        }

        @Override
        public void onTabChanged(String tabId) {
        	
			System.out.println("TAB CHANGED = {"+tabId+"}");
			if(tabId.equals("EquationPanel")) {
				whenSwitchedToFunctionPanel();
			} else if(tabId.equals("GraphingPanel")) {
				whenSwitchedToDrawingPanel();
			} else {
				
			}
        	
            currentView = tabHost.getCurrentView();
            if (tabHost.getCurrentTab() > currentTab) {
                previousView.setAnimation(outToLeftAnimation());
                currentView.setAnimation(inFromRightAnimation());
            } else {
                previousView.setAnimation(outToRightAnimation());
                currentView.setAnimation(inFromLeftAnimation());
            }
            previousView = currentView;
            currentTab = tabHost.getCurrentTab();

        }

        private Animation inFromRightAnimation() {
            Animation inFromRight = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 1.0f, Animation.RELATIVE_TO_PARENT, 0.0f,
                    Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f);
            return setProperties(inFromRight);
        }

        private Animation outToRightAnimation() {
            Animation outToRight = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 1.0f,
                    Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f);
            return setProperties(outToRight);
        }

        private Animation inFromLeftAnimation() {
            Animation inFromLeft = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, -1.0f, Animation.RELATIVE_TO_PARENT, 0.0f,
                    Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f);
            return setProperties(inFromLeft);
        }

        private Animation outToLeftAnimation() {
            Animation outtoLeft = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, -1.0f,
                    Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f);
            return setProperties(outtoLeft);
        }

        private Animation setProperties(Animation animation) {
            animation.setDuration(ANIMATION_TIME);
            animation.setInterpolator(new AccelerateInterpolator());
            return animation;
        }
    }
    
}