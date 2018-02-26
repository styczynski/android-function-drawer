package com.example.extremetic_tac_toe;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import org.andengine.util.FileUtils;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.TwoArgFunction;

import com.example.extremetic_tac_toe.SimpleFileDialog.SimpleFileDialogTypeModifier;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.ClipData.Item;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Bitmap.Config;
import android.graphics.RectF;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.InputType;
import android.text.SpannableStringBuilder;
import android.util.SparseBooleanArray;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.inputmethod.EditorInfo;
import android.webkit.WebSettings;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.PopupMenu;
import android.widget.RadioGroup;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

@TargetApi(Build.VERSION_CODES.HONEYCOMB) @SuppressLint("NewApi") public class FunctionPanelActivity extends Activity {
	
	ListView listview;
	Button addNewFunction;
	Button addNewVar;
	protected static boolean entryCanBeClicked = true;
	
	
	class StableArrayAdapter extends ArrayAdapter<String> {
		
	    HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();
	
	    public StableArrayAdapter(Context context, int textViewResourceId,
	        List<String> objects) {
	      super(context, textViewResourceId, objects);
	      for (int i = 0; i < objects.size(); ++i) {
	        mIdMap.put(objects.get(i), i);
	      }
	    }
	
	    @Override
	    public long getItemId(int position) {
	      String item = getItem(position);
	      return mIdMap.get(item);
	    }
	
	    @Override
	    public boolean hasStableIds() {
	      return true;
	    }
	
	  }
	
	public void reloadWebView(int index) {
		int firstPosition = listview.getFirstVisiblePosition() - listview.getHeaderViewsCount(); // This is the same as child #0
		int wantedChild = index - firstPosition;
		// Say, first visible position is 8, you want position 10, wantedChild will now be 2
		// So that means your view is child #2 in the ViewGroup:
		if (wantedChild < 0 || wantedChild >= listview.getChildCount()) {
			return;
		}
		// Could also check if wantedPosition is between listView.getFirstVisiblePosition() and listView.getLastVisiblePosition() instead.
		View wantedView = listview.getChildAt(wantedChild);
		
		WebView wv = (WebView) wantedView.findViewById(R.id.id);
		final FunctionVariable fvar = (FunctionVariable)Core.functions.get(index);
		
		wv.loadUrl("javascript:$('#editable-math').mathquill('latex', '"+fvar.name+"="+((Object)fvar.value).toString()+"')");
		
		
		
	}
	
	public void reload() {
		final ArrayList<Function> list = new ArrayList<Function>();
	    final int values = Core.functions.size();
	    for (int i = 0; i < values; ++i) {
	    	list.add(Core.functions.get(i));
	    }
	    //final StableArrayAdapter adapter = new StableArrayAdapter(this, android.R.layout.simple_list_item_checked, list);
	    final ListAdapter customAdapter = new ListAdapter(this, R.layout.function_panel_tablerow, list);
	    
	    listview.setAdapter(customAdapter);
	    
	}
	
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		listview.clearChoices();
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.function_panel_menu, menu);
	    
	    return true;
	}
	
	
	static void onLoadFunction(final Activity act) {
		SimpleFileDialog FileLoadDialog =  new SimpleFileDialog(act, "FileSave",
				new SimpleFileDialog.SimpleFileDialogListener() {
					@Override
					public void onChosenDir(String chosenDir) {
						//Function funct = new Function();
						Vector<Function> fv = Function.importFile(act, chosenDir);
						final int siz = fv.size();
						for(int it=0;it<siz;++it) {
							if(!Core.integrateFunction(fv.get(it))) {
								final Function toadd = fv.get(it);
								// Use the Builder class for convenient dialog construction
						        AlertDialog.Builder builder = new AlertDialog.Builder(act);
						        builder.setMessage("One function ("+toadd.eqaution+") seems to be exact copy of one, which is already on list. Do you want to import it anyway?")
						               .setPositiveButton("ok", new DialogInterface.OnClickListener() {
						                   public void onClick(DialogInterface dialog, int id) {
						                	   Core.functions.add(toadd);
						                	   MainActivity.panelInstance.reload();
						                   }
						               })
						               .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
						                   public void onClick(DialogInterface dialog, int id) {
						                	  
						                   }
						               });
						        builder.create();
								builder.show();
							}
						}
						MainActivity.panelInstance.reload();
						
					}
    			}
        	);
        	FileLoadDialog.Default_File_Name = "my_default.txt";
			FileLoadDialog.chooseFile_or_Dir("/storage/extSdCard");	
	}
	

	static void onSaveFunctionAs(final Activity act) {
		
    	SimpleFileDialog FileSaveDialog =  new SimpleFileDialog(act, "FileSave",
		new SimpleFileDialog.SimpleFileDialogListener() {
			
			@Override
			public void onChosenDir(String chosenDir) {
				final String m_chosen = chosenDir;
				Toast.makeText(act, "Chosen FileOpenDialog File: " + 
				m_chosen, Toast.LENGTH_LONG).show();
				
				AlertDialog.Builder builderSingle = new AlertDialog.Builder(act);
	            builderSingle.setIcon(R.drawable.ic_launcher);
	            builderSingle.setTitle("Select One Name:-");
	            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(act, android.R.layout.select_dialog_singlechoice);
	            arrayAdapter.add("PNG");
	            arrayAdapter.add("JPEG");
	            arrayAdapter.add("Plain text");
	            builderSingle.setNegativeButton("cancel",
	                    new DialogInterface.OnClickListener() {
	                        @Override
	                        public void onClick(DialogInterface dialog, int which) {
	                            dialog.dismiss();
	                        }
	                    });

	            builderSingle.setAdapter(arrayAdapter,
	                    new DialogInterface.OnClickListener() {

	                        @Override
	                        public void onClick(DialogInterface dialog, int which) {
	                            final String opName = arrayAdapter.getItem(which);

	        				 	final int itemchoice_len = Core.functions.size();
	        				 	CharSequence[] items = new CharSequence[itemchoice_len];
	        				 	for(int it=0;it<itemchoice_len;++it) {
	        				 		if(Core.functions.get(it).object_type==Function.ENTRY_TYPE_LUA_DRAWER) {
	        				 			items[it] = Core.functions.get(it).lua_name;
	        				 		} else if(Core.functions.get(it).prp_equation.trim().length()>0) {
	        				 			items[it] = Core.functions.get(it).prp_equation;
	        				 		} else if(Core.functions.get(it).eqaution.trim().length()>0) {
	        				 			items[it] = Core.functions.get(it).eqaution;
	        				 		} else {
	        				 			items[it] = "Function @"+((Object)(it+1)).toString();
	        				 		}
	        				 	}
	        				 
	        				    final boolean[] states = new boolean[items.length];
	        				    for(int it=0;it<items.length;++it) {states[it]=false;}
	        				    AlertDialog.Builder builder = new AlertDialog.Builder(act);
	        				    builder.setTitle("What would you like to do?");
	        				    builder.setMultiChoiceItems(items, states, new DialogInterface.OnMultiChoiceClickListener(){
	        				        public void onClick(DialogInterface dialogInterface, int item, boolean state) {
	        				        }
	        				    });
	        				    builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
	        				        public void onClick(DialogInterface dialog, int id) {
	        				        	
	        				        	SparseBooleanArray CheCked = ((AlertDialog) dialog).getListView().getCheckedItemPositions();
	        					        for(int it=0;it<itemchoice_len;++it) {
	        					        	if (CheCked.get(it)) {
	        					        		Core.functionsSavingBuffer.add(new Function(Core.functions.get(it)));
	        					            }
	        					        }
	        					        Core.exportAs(opName, m_chosen, act);
	        					        
	        				        }
	        				    });
	        				    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	        				        public void onClick(DialogInterface dialog, int id) {
	        				             dialog.cancel();
	        				        }
	        				    });
	        				    builder.create().show();
	        				
	                            
	                        }
	                    });
	            builderSingle.show();
				
			}
		});
		
		FileSaveDialog.Default_File_Name = "my_default.txt";
		FileSaveDialog.chooseFile_or_Dir("/storage/extSdCard");
	}
	
	static void onSaveFunction(final Activity act) {

    	SimpleFileDialog FileSaveDialog =  new SimpleFileDialog(act, "FileSave",
		new SimpleFileDialog.SimpleFileDialogListener() {
			
			@Override
			public void onChosenDir(String chosenDir) {
				final String m_chosen = chosenDir;
				Toast.makeText(act, "Chosen FileOpenDialog File: " + 
				m_chosen, Toast.LENGTH_LONG).show();
				
				
				 	final int itemchoice_len = Core.functions.size();
				 	CharSequence[] items = new CharSequence[itemchoice_len];
				 	for(int it=0;it<itemchoice_len;++it) {
				 		if(Core.functions.get(it).object_type==Function.ENTRY_TYPE_LUA_DRAWER) {
				 			items[it] = Core.functions.get(it).lua_name;
				 		} else if(Core.functions.get(it).prp_equation.trim().length()>0) {
				 			items[it] = Core.functions.get(it).prp_equation;
				 		} else if(Core.functions.get(it).eqaution.trim().length()>0) {
				 			items[it] = Core.functions.get(it).eqaution;
				 		} else {
				 			items[it] = "Function @"+((Object)(it+1)).toString();
				 		}
				 	}
				 
				 	final boolean[] states = new boolean[items.length];
				    for(int it=0;it<items.length;++it) {states[it]=false;}
				    AlertDialog.Builder builder = new AlertDialog.Builder(act);
				    builder.setTitle("What would you like to do?");
				    builder.setMultiChoiceItems(items, states, new DialogInterface.OnMultiChoiceClickListener(){
				        public void onClick(DialogInterface dialogInterface, int item, boolean state) {
				        }
				    });
				    builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
				        public void onClick(DialogInterface dialog, int id) {
				        	String toExport = "###begin.file;$$\n";
				        	SparseBooleanArray CheCked = ((AlertDialog) dialog).getListView().getCheckedItemPositions();
					        for(int it=0;it<itemchoice_len;++it) {
					        	if (CheCked.get(it)) {
					        		toExport += Core.functions.get(it).serialize() + "\n";
					            }
					        }
					        toExport += "###end.file;$$\n";
					        Function.exportFile(toExport, act, m_chosen);
				        }
				    });
				    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				        public void onClick(DialogInterface dialog, int id) {
				             dialog.cancel();
				        }
				    });
				    builder.create().show();
				
				
				
			}
		});
		
		FileSaveDialog.Default_File_Name = "my_default.afd";
		FileSaveDialog.setTypeModifier(new SimpleFileDialogTypeModifier() {
			@Override
			public String onDirSelected(String dir) {
				File fil = new File(dir);
				if(fil.isFile()) {
					String ext = fil.getName().substring(fil.getName().lastIndexOf('.'), fil.getName().length());
					return dir.replaceAll(ext, ".afd");
				}
				return dir;
			}
		});
		FileSaveDialog.chooseFile_or_Dir("/storage/extSdCard");
		
	}
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		String functionBody = "";
		switch (item.getItemId()) {
	        case R.id.function_panel_menu_save_functions_entry: 
	        	onSaveFunction(this);
	        	return true;
	        	
	        case R.id.function_panel_menu_change_settings:
	        	Intent intent = new Intent(FunctionPanelActivity.this, SettingsPanelActivity.class);
		    	startActivity(intent);
	        	return true;
	        	
	        case R.id.function_panel_menu_load_functions_entry: 
	        	onLoadFunction(this);
	        	return true;
	    }
		return super.onOptionsItemSelected(item);
	
	}
	
	
	
	
	@Override
	  protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.function_panel_layout);
	    
	    MainActivity.panelInstance = this;

	    //function_panel_add_new_function
	    addNewVar = (Button) findViewById(R.id.function_panel_add_new_var);
	    addNewVar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	String varexp = new String(new char[]{ (char)((Math.random()*('z'-'a')+'a')) });
            	varexp += "=";
            	varexp += ((Object)((int)(Math.random()*500))).toString();
            	Function funct = FunctionTypeSelector.select(new Function(varexp));
            	funct.eqaution = varexp;
            	Core.functions.add(funct);
            	reload();
            }
        });
	    addNewFunction = (Button) findViewById(R.id.function_panel_add_new_function);
	    addNewFunction.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	Function funct = new Function("");//("x^2+y^2=100");
            	funct.object_type = Function.ENTRY_TYPE_LUA_DRAWER;
            	//funct.color = (int)(Math.random()*Color.WHITE);
            	funct.eqaution = "fd.setColor(\"black\");\r\n" + 
            			" for i=-1000,1000 do\r\n" + 
            			" 	fd.drawPoint(i, jmath.sin(i)*500);\r\n" + 
            			" end";
            	Core.functions.add(funct);
            	reload();
            }
        });
	    
	    
	    listview = (ListView) findViewById(R.id.functionListView);
	    reload();
	    
	    listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

	      @Override
	      public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
	    	  /*Core.toEditIndex = position;
	    	  if(Core.functions.get(position).object_type == Function.ENTRY_TYPE_LUA_DRAWER) {
	    		  final Function item = (Function) parent.getItemAtPosition(position);
		    	  Intent intent = new Intent(FunctionPanelActivity.this, FunctionPanelEditLuaFunctionActivity.class);
		    	  startActivity(intent);
	    	  } else {
	    		  final Function item = (Function) parent.getItemAtPosition(position);
		    	  Intent intent = new Intent(FunctionPanelActivity.this, FunctionPanelEditFunctionActivity.class);
		    	  startActivity(intent);
	    	  }*/
	      }

	    });
	}
	
	
	public class ListAdapter extends ArrayAdapter<Function> {

		//SurfaceHolder holder = null;
		
		public ListAdapter(Context context, int textViewResourceId) {
		    super(context, textViewResourceId);
		}

		public ListAdapter(Context context, int resource, List<Function> items) {
		    super(context, resource, items);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

		    View v = convertView;

		    if (v == null) {

		        LayoutInflater vi;
		        vi = LayoutInflater.from(getContext());
		        v = vi.inflate(R.layout.function_panel_tablerow, null);

		    }

		    Function p = (Function)getItem(position);
		    
		    
		    if(p.object_type == Function.ENTRY_TYPE_VARIABLE) {
		    	 LayoutInflater vi;
			     vi = LayoutInflater.from(getContext());
			     v = vi.inflate(R.layout.function_panel_tablerow_var, null);
		    } else {
		    	 LayoutInflater vi;
			     vi = LayoutInflater.from(getContext());
			     v = vi.inflate(R.layout.function_panel_tablerow, null);
		    }
		    
		    final int itemPosition = position;

		    
		    if (p != null) {
		    	
		        WebView tt = (WebView) v.findViewById(R.id.id);
		       	tt.getSettings().setRenderPriority(RenderPriority.HIGH);
		        tt.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ONLY);
		        
		        TextView tt1 = (TextView) v.findViewById(R.id.categoryId);
		        TextView tt3 = (TextView) v.findViewById(R.id.description);
		        ImageView tcol = (ImageView) v.findViewById(R.id.function_panel_tablerow_colorview);
		        ImageButton trem = (ImageButton) v.findViewById(R.id.function_panel_tablerow_removebutton);
		        ImageButton tprops = (ImageButton) v.findViewById(R.id.function_panel_tablerow_propsbutton);
		        
		        tt.setBackgroundColor(0x00000000);
		       
		        /*tt.setOnTouchListener(new OnTouchListener() {
					@Override
					public boolean onTouch(View v, MotionEvent event) {
						if(!FunctionPanelActivity.entryCanBeClicked) return true;
						FunctionPanelActivity.entryCanBeClicked = false;
						Core.toEditIndex = itemPosition;
						
						if(Core.functions.get(itemPosition).object_type == Function.ENTRY_TYPE_LUA_DRAWER) {
							  final Function item = (Function) ListAdapter.this.getItem(itemPosition);
					    	  Intent intent = new Intent(FunctionPanelActivity.this, FunctionPanelEditLuaFunctionActivity.class);
					    	  startActivity(intent);
				    	  } else {
				    		  final Function item = (Function) ListAdapter.this.getItem(itemPosition);
				    		  Intent intent = new Intent(FunctionPanelActivity.this, FunctionPanelEditFunctionActivity.class);
				    		  startActivity(intent);
				    	}
						return true;
					}
		  	     
		        });*/
		        
		        tt.setOnTouchListener(new OnTouchListener() {
					@Override
					public boolean onTouch(View v, MotionEvent event) {
						return true;
					}
		        });
		        
		        if(p.object_type!=Function.ENTRY_TYPE_VARIABLE) {
			        tcol.setOnClickListener(new View.OnClickListener() {
			            public void onClick(View v) {
			            	/*Core.toEditIndex = itemPosition;
				  	    	final Function item = (Function) ListAdapter.this.getItem(itemPosition);
				  	    	Intent intent = new Intent(FunctionPanelActivity.this, FunctionPanelEditColorActivity.class);
				  	    	startActivity(intent);*/
			            	PopupMenu popup = new PopupMenu(FunctionPanelActivity.this, v);
			                MenuInflater inflater = popup.getMenuInflater();
			                inflater.inflate(R.menu.function_panel_color_menu, popup.getMenu());
			                popup.setOnMenuItemClickListener(new OnMenuItemClickListener() {
								@Override
								public boolean onMenuItemClick(MenuItem item) {
									switch (item.getItemId()) {
								        case R.id.function_panel_color_menu_edit_color:
								        	Core.toEditIndex = itemPosition;
								  	    	Intent intent = new Intent(FunctionPanelActivity.this, FunctionPanelEditColorActivity.class);
								  	    	startActivity(intent);
								            return true;
								        case R.id.function_panel_color_menu_edit_equation:
											if(!FunctionPanelActivity.entryCanBeClicked) return true;
											FunctionPanelActivity.entryCanBeClicked = false;
											Core.toEditIndex = itemPosition;
											if(Core.functions.get(itemPosition).object_type == Function.ENTRY_TYPE_LUA_DRAWER) {
												Intent nintent = new Intent(FunctionPanelActivity.this, FunctionPanelEditLuaFunctionActivity.class);
												startActivity(nintent);
											} else {
												Intent nintent = new Intent(FunctionPanelActivity.this, FunctionPanelEditFunctionActivity.class);
												startActivity(nintent);
											}
								        	return true;
								        case R.id.function_panel_color_menu_disable_function:
								        	final Function funct = (Function) ListAdapter.this.getItem(itemPosition);
								        	if(funct.isDisabled) {
								        		funct.isDisabled = false;
								            	//item.setTitle("Disable");
								        	} else {
								        		funct.isDisabled = true;
								            	//item.setTitle("Enable");
								        	}
								            reload();
								        	return true;
								        default:
								            return false;
									}
								}
							});
			                
			                popup.show();
			                
			                
			            }
			        });
		        }
		        
		        Bitmap bmp = Bitmap.createBitmap(30, 30, Config.ARGB_8888);
		        Canvas canvas = new Canvas(bmp);
		        Paint paint = new Paint();
		        paint.setStrokeWidth(1);
		        paint.setAntiAlias(true);
		        paint.setColor(Color.BLACK);
		        if((p.object_type==Function.ENTRY_TYPE_FUNCTION || p.object_type==Function.ENTRY_TYPE_CURVE_OBJECT) && p.object_type!=Function.ENTRY_TYPE_LUA_DRAWER) {
		        	if(p.object_type==Function.ENTRY_TYPE_CURVE_OBJECT) {
		        		
			        } else {
			        	canvas.drawCircle(15, 15, 14, paint);
			        }
		        	
			        if(p.isDisabled) {
			        	paint.setColor(Color.BLACK);
			        } else {
			        	paint.setColor(Color.WHITE);
			        }
			        
			        canvas.drawCircle(15, 15, 13, paint);
			        paint.setColor(Color.BLACK);
			        canvas.drawCircle(15, 15, 11, paint);
			        paint.setColor(p.color);
			        if(p.isDisabled) {
			        	paint.setAlpha(50);
			        }
			        canvas.drawCircle(15, 15, 10, paint);
			        
			        
		        } /*else if(p.object_type==Function.ENTRY_TYPE_CURVE_OBJECT) {
		        	paint.setColor(Color.WHITE);
		        	canvas.drawCircle(15, 15, 10, paint);
		        } */ else if(p.object_type==Function.ENTRY_TYPE_VARIABLE) {
		        	paint.setColor(Color.WHITE);
		        	paint.setTextSize(100);
		        	canvas.drawText("text", 0, 0, paint);
		        } else {
		        	paint.setColor(Color.MAGENTA);
		        	canvas.drawPaint(paint);
		        }
		        canvas.save();
		        tcol.setImageBitmap(bmp);
		        
		        
		        
		        
		        trem.setImageResource(android.R.drawable.ic_delete);
		        
		        trem.setOnClickListener(new OnClickListener(){
		        	Function funct = null;
		        	public OnClickListener init(Function arg) {
		        		funct = arg;
		        		return this;
		        	}
		        	
					@Override
					public void onClick(View arg0) {
						new AlertDialog.Builder(FunctionPanelActivity.this)
	            	    .setTitle("Deleting expression")
	            	    .setMessage("Are you sure you want to remove this expression?")
	            	    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
	            	        public void onClick(DialogInterface dialog, int which) { 
	            	        	final int len = Core.functions.size();
	    						for(int it=0;it<len;++it) {
	    							if(Core.functions.get(it).equals(funct)) {
	    								System.out.println("Found at index "+((Object)it).toString());
	    								Core.functions.removeElementAt(it);
	    								reload();
	    								return;
	    							}
	    						}
	            	        }
	            	     })
	            	    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
	            	        public void onClick(DialogInterface dialog, int which) { 
	            	           
	            	        }
	            	     })
	            	    .setIcon(android.R.drawable.ic_dialog_info)
	            	    .show();
						
					}
		        }.init(p));
		        
		        tprops.setImageResource(android.R.drawable.ic_menu_edit);
		        
		        tprops.setOnClickListener(new OnClickListener(){	
					@Override
					public void onClick(View arg0) {
						Core.toEditIndex = itemPosition;
			  	    	final Function item = (Function) ListAdapter.this.getItem(itemPosition);
			  	    	Intent intent = new Intent(FunctionPanelActivity.this, FunctionPanelChooseFunctOpActivity.class);
			  	    	startActivity(intent);
					}
		        });
		        
		        //tt.setTextColor(android.graphics.Color.BLACK);
		        if(p.object_type!=Function.ENTRY_TYPE_VARIABLE) {
		        	tt1.setTextColor(android.graphics.Color.BLACK);
		        	tt3.setTextColor(android.graphics.Color.BLACK);
		        }
		
		        tt.getSettings().setJavaScriptEnabled(true); 
		        
		        String latexCode = p.eqaution;
		        String HTMLContent = "<!DOCTYPE html>\r\n" + 
		        		"<html>\r\n" + 
		        		"<head>\r\n" + 
		        		"<link rel=\"stylesheet\" type=\"text/css\" href=\"../build/mathquill.css\">\r\n" + 
		        		"</head>\r\n" + 
		        		"<body>\r\n" + 
		        		"<script type=\"text/javascript\" src=\"support/jquery-1.7.2.js\"></script>\r\n" + 
		        		"<script type=\"text/javascript\" src=\"../build/mathquill.js\"></script>\r\n" + 
		        		"</body>\r\n" + 
		        		//"<span class=\"mathquill-embedded-latex\">\r\n" + 
		        		"<span id=\"editable-math\" style=\"position: fixed;\" class=\"mathquill-editable\">\r\n" +
		        		latexCode + 
		        		"</span>\r\n" + 
		        		"</html>\r\n" + 
		        		"";
		        
		        
		        //tt.loadUrl("file:///android_asset/page/test/tabrow_index.html");
		        tt.loadDataWithBaseURL("file:///android_asset/page/test/", HTMLContent, "text/html", "UTF-8", null);

		        
		        //tt.setText("<html><head>"+htmlContent+"</head><body>"+latexHTMLCode+"</body></html>");
		        //tt3.setText(p.eqaution);
		        
		        final boolean isGoodFunct = p.isGood();
		        
		        if(p.object_type!=Function.ENTRY_TYPE_VARIABLE) {
			        String buf = "";
			        if(isGoodFunct) {
				        if(p.checkIsInjective()) {
				        	buf = "Injective";
				        } else {
				        	buf = "Not injective";
				        }
			        } else {
			        	buf = "---";
			        }
			        tt1.setText(buf);
			        
			        buf = "";
			        if(isGoodFunct) {
			        	tt3.setTextColor(Color.BLACK);
			        	buf = "OK";
			        } else {
			        	Context context = getApplicationContext();
			        	CharSequence text = "Bad = ["+p.prp_equation+"]";
			        	int duration = Toast.LENGTH_LONG;
			        	Toast toast = Toast.makeText(context, text, duration);
			        	toast.show();
			        	
			        	tt3.setTextColor(Color.RED);
			        	buf = "Invalid expression!";
			        }
			        tt3.setText(buf);
		        }
		        
		        
		        if(p.object_type == Function.ENTRY_TYPE_LUA_DRAWER) {
		        	tt1.setText("\""+p.lua_name+"\"");
		        	tt3.setText("About "+((Object)p.eqaution.length()).toString()+" characters");
		        	tt.loadData("LUA script function", "text/html", "utf-8");
		        } else if(p.object_type == Function.ENTRY_TYPE_VARIABLE) {
		        	final FunctionVariable fvar = FunctionVariable.create(p);
		        	final double fvarMin = fvar.pbMinValue;
		        	final double fvarMax = fvar.pbMaxValue;
		        	int prfvarPMax = 100;
		        	double prfvarMul = 1.0;
		        	
		        	if(fvar.pbStepAuto||fvar.pbStepValue<=0) {
		        		prfvarMul = (fvarMax-fvarMin)/100.0;
		        	} else {
		        		prfvarPMax = (int)Math.ceil((fvarMax-fvarMin)/fvar.pbStepValue);
		        		prfvarMul = (fvarMax-fvarMin)/((double)prfvarPMax);
		        	}
		        	final double fvarPMax = prfvarPMax;
		        	final double fvarMul = prfvarMul;
		        	
		        	
		        	TextView seekBarMin = (TextView) v.findViewById(R.id.function_panel_tablerow_varvalue_min);
		        	TextView seekBarMax = (TextView) v.findViewById(R.id.function_panel_tablerow_varvalue_max);
		        	final SeekBar seekBar = (SeekBar) v.findViewById(R.id.function_panel_tablerow_varvalue);
		        	final ImageButton seekPlay = (ImageButton) v.findViewById(R.id.function_panel_tablerow_playbutton);
		        	
		        	//ic_media_play
		        	//ic_media_pause
		        	if(fvar.pbIsPlayed) {
		        		seekPlay.setImageResource(android.R.drawable.ic_media_pause);
		        	} else {
		        		seekPlay.setImageResource(android.R.drawable.ic_media_play);
		        	}
		        	
		        	final ValueAnimator anim = ValueAnimator.ofInt(0, seekBar.getMax());
		        	anim.setDuration(5000);
		        	
		        	//anim.setStartDelay(0);
		        	anim.setInterpolator(new LinearInterpolator());
		            anim.setRepeatCount(ValueAnimator.INFINITE);
		            anim.setRepeatMode(ValueAnimator.REVERSE);
		            
		        	
		        	anim.addUpdateListener(new AnimatorUpdateListener() {
	        	        @Override
	        	        public void onAnimationUpdate(ValueAnimator animation) {
	        	            int animProgress = (Integer) animation.getAnimatedValue();
	        	            seekBar.setProgress(animProgress);
	        	            //reload();
	        	        }
		        	});
		        	//anim.setInterpolator(new AccelerateInterpolator(2f));
		        	anim.addListener(new AnimatorListener() {

						@Override
						public void onAnimationCancel(Animator animation) {
							fvar.pbIsPlayed = false;
				        	seekPlay.setImageResource(android.R.drawable.ic_media_play);
						}

						@Override
						public void onAnimationEnd(Animator animation) {
							//fvar.pbIsPlayed = false;
							//seekPlay.setImageResource(android.R.drawable.ic_media_play);
							//((ValueAnimator)animation).reverse();
							
						}

						@Override
						public void onAnimationRepeat(Animator animation) {
							
						}

						@Override
						public void onAnimationStart(Animator animation) {
							
						}
		        		
		        	});
		        	
		        	if(fvar.pbIsPlayed) {
		        		//anim.start();
		        		MainActivity.runningMain.runAnimation(anim);
		        	} else {
		        		anim.end();
		        	}
		        	
		        	seekPlay.setOnClickListener(new OnClickListener(){
						@Override
						public void onClick(View arg0) {
							fvar.pbIsPlayed = !fvar.pbIsPlayed;
							ImageButton but = (ImageButton) arg0;
							if(fvar.pbIsPlayed) {
				        		but.setImageResource(android.R.drawable.ic_media_pause);
				        	} else {
				        		but.setImageResource(android.R.drawable.ic_media_play);
				        	}
							if(fvar.pbIsPlayed) {
								MainActivity.runningMain.runAnimation(anim);
								//anim.start();
				        	} else {
				        		anim.end();
				        	}
							
						}
		        	});
		        	
		        	
		        	
		        	seekBarMin.setText(FunctionDrawer.valToStr(fvarMin));
		        	seekBarMax.setText(FunctionDrawer.valToStr(fvarMax));
		        	
		        	LayoutInflater layoutInflater = (LayoutInflater)getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);  
		        	final View popupView = layoutInflater.inflate(R.layout.function_value_range_set_dialog, null);  
		        	final PopupWindow popupWindow = new PopupWindow(popupView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);  
		        	
		        	final FragmentManager frgmgr = FunctionPanelActivity.this.getFragmentManager();
		        	
		        	seekBarMin.setOnClickListener(new OnClickListener() {
		                public void onClick(View v) {
		                	SetVarRangeDialogFragment dlg = new SetVarRangeDialogFragment(itemPosition);
		                    dlg.init(FunctionPanelActivity.this);
		                	dlg.functIndex = itemPosition;
		                	dlg.show();
		                }
		            });
		        	seekBarMax.setOnClickListener(new OnClickListener() {
		                public void onClick(View v) {
		                	SetVarRangeDialogFragment dlg = new SetVarRangeDialogFragment(itemPosition);
		                    dlg.init(FunctionPanelActivity.this);
		                	dlg.functIndex = itemPosition;
		                	dlg.show();
		                }
		            });
		        	
		        	
		        	//seekBar.setMax((int)(fvarMax-fvarMin));
		        	
		        	
		        	seekBar.setIndeterminate(false);
		        	seekBar.setMinimumWidth(200);
		        	seekBar.setMax((int)fvarPMax);
		        	
		        	seekBar.setProgress((int)((fvar.value-fvarMin)/fvarMul));
		        	
		        	
		        	//seekBar.setProgress((int)(fvar.value-fvarMin));
		        	
		        	
		        	seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
						
		        		private synchronized void update() {
		        			anim.setIntValues(0, seekBar.getMax());
		        			
		        			fvar.value = (seekBar.getProgress()*fvarMul) + fvarMin;
							fvar.updateState();
							Core.functions.setElementAt(fvar, itemPosition);
							//reload();
							if(DrawingPanelActivity.runningActivity!=null) {
								boolean state = anim.isRunning();
								/*
								if(state) {
									anim.end();
		        				}
		        				*/
								System.out.println("Should be flushed.");
								try {
									wait(10);
								} catch(Exception e) {
									e.printStackTrace();
								}
								DrawingPanelActivity.runningActivity.flush();
								
								/*
								if(state){
									MainActivity.runningMain.runAnimation(anim);
								}
								*/
							}
							reloadWebView(itemPosition);
		        		}
		        		
		        		@Override
						public void onStopTrackingTouch(SeekBar seekBar) {
							//update();
						}
						@Override
						public void onStartTrackingTouch(SeekBar seekBar) {
						
							
						}
						@Override
						public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
							update();
						}
					});
		        	
		        }
		        
		    }

		    return v;

		}
	}
	
	//SetVarRangeDialogFragment
	@SuppressLint("ValidFragment")
	public class SetVarRangeDialogFragment implements OnCheckedChangeListener {
		
		public int functIndex = 0;
		Dialog dlg = null;
		
		public SetVarRangeDialogFragment(int arg0) {
			functIndex = arg0;
		}
		
		private boolean isNumber(String val) {
			try {
				double x = Double.parseDouble(val);
				return true;
			} catch(Throwable t) {
				return false;
			}
		}
		
	    public void init(final Activity activity) {
	    	
	    	AlertDialog.Builder builder = new AlertDialog.Builder(activity);
	        LayoutInflater inflater = activity.getLayoutInflater();
	        
	        builder.setView(inflater.inflate(R.layout.function_value_range_set_dialog, null))
	         .setPositiveButton(android.R.string.ok, null)
	         .setNegativeButton(android.R.string.cancel, null);
	        
	        dlg = builder.create();
	        dlg.setOnShowListener(new DialogInterface.OnShowListener() {
	            @Override
	            public void onShow(DialogInterface dialog) {
	                Button positive = ((AlertDialog) dlg).getButton(AlertDialog.BUTTON_POSITIVE);
	                positive.setOnClickListener(new View.OnClickListener() {
	                    @Override
	                    public void onClick(View view) {
	                       final FunctionVariable fvar = (FunctionVariable) Core.functions.get(functIndex);
	                	   EditText dlgf_min = (EditText) dlg.findViewById(R.id.function_value_range_set_dialog_minfield);
	                       EditText dlgf_max = (EditText) dlg.findViewById(R.id.function_value_range_set_dialog_maxfield);
	                       EditText dlgf_step = (EditText) dlg.findViewById(R.id.function_value_range_set_dialog_stepfield);
	                       
	                       boolean error = false;
	                       if(!isNumber(dlgf_step.getText().toString().trim())){
	                    	   error=true;
	                    	   dlgf_step.setError("Given value is not a number!");
	                       } else if(Double.parseDouble(dlgf_step.getText().toString().trim())<=0) {
	                    	   error=true;
	                    	   dlgf_step.setError("Step must be positive value!");
	                       }
	                       if(!isNumber(dlgf_min.getText().toString().trim())){
	                    	   error=true;
	                    	   dlgf_min.setError("Given value is not a number!");
	                       } else if(isNumber(dlgf_max.getText().toString().trim())) {
	                    	   if(Double.parseDouble(dlgf_min.getText().toString().trim())>Double.parseDouble(dlgf_max.getText().toString().trim())) {
									error=true;
									dlgf_min.setError("Minium value cannot be greater than maximum!"); 
	                    	   }

	                       }
	                       if(!isNumber(dlgf_max.getText().toString().trim())){
	                    	   error=true;
	                    	   dlgf_max.setError("Given value is not a number!");
	                       }
	                       
	                       if(!error) {                
		                       fvar.pbMinValue = Double.parseDouble(((SpannableStringBuilder)dlgf_min.getText()).toString());
		                       fvar.pbMaxValue = Double.parseDouble(((SpannableStringBuilder)dlgf_max.getText()).toString());
		                       fvar.pbStepValue = Double.parseDouble(((SpannableStringBuilder)dlgf_step.getText()).toString());
		                	   Core.functions.setElementAt(fvar, functIndex);
		                	   reload();
		                	   SetVarRangeDialogFragment.this.dlg.dismiss();
	                       } else {
	                    	   
	                       }
	                   }
	                });
	                Button negative = ((AlertDialog) dlg).getButton(AlertDialog.BUTTON_NEGATIVE);
	                negative.setOnClickListener(new View.OnClickListener() {
	                    @Override
	                    public void onClick(View view) {
	                    	SetVarRangeDialogFragment.this.dlg.dismiss();
	                    }
	                });
	            }
	        });
	        
	        
	        /*
	         .setPositiveButton("OK", new DialogInterface.OnClickListener() {
	                   @Override
	                   public void onClick(DialogInterface dialog, int id) {
	                	   final FunctionVariable fvar = (FunctionVariable) Core.functions.get(functIndex);
	                	   EditText dlgf_min = (EditText) dlg.findViewById(R.id.function_value_range_set_dialog_minfield);
	                       EditText dlgf_max = (EditText) dlg.findViewById(R.id.function_value_range_set_dialog_maxfield);
	                       EditText dlgf_step = (EditText) dlg.findViewById(R.id.function_value_range_set_dialog_stepfield);
	                       
	                       boolean error = false;
	                       if(!isNumber(dlgf_step.getText().toString().trim())){
	                    	   error=true;
	                    	   dlgf_step.setError("Given value is not a number!");
	                       } else if(Double.parseDouble(dlgf_step.getText().toString().trim())<=0) {
	                    	   error=true;
	                    	   dlgf_step.setError("Step must be positive value!");
	                       }
	                       if(!isNumber(dlgf_min.getText().toString().trim())){
	                    	   error=true;
	                    	   dlgf_min.setError("Given value is not a number!");
	                       } else if(Double.parseDouble(dlgf_min.getText().toString().trim())>Double.parseDouble(dlgf_max.getText().toString().trim())) {
	                    	   error=true;
	                    	   dlgf_min.setError("Minium value cannot be greater than maximum!");
	                       }
	                       if(!isNumber(dlgf_max.getText().toString().trim())){
	                    	   error=true;
	                    	   dlgf_max.setError("Given value is not a number!");
	                       }
	                       
	                       if(!error) {                     
		                       fvar.pbMinValue = Double.parseDouble(((SpannableStringBuilder)dlgf_min.getText()).toString());
		                       fvar.pbMaxValue = Double.parseDouble(((SpannableStringBuilder)dlgf_max.getText()).toString());
		                       fvar.pbStepValue = Double.parseDouble(((SpannableStringBuilder)dlgf_step.getText()).toString());
		                	   Core.functions.setElementAt(fvar, functIndex);
		                	   SetVarRangeDialogFragment.this.dlg.dismiss();
	                       } else {
	                    	   
	                       }
	                   }
	               })
	               .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	                   public void onClick(DialogInterface dialog, int id) {
	                	   SetVarRangeDialogFragment.this.dlg.dismiss();
	                   }
	               });      
	         */
	     	
	    }
	    
	    private EditText findInput(ViewGroup np) {
	        int count = np.getChildCount();
	        for (int i = 0; i < count; i++) {
	            final View child = np.getChildAt(i);
	            if (child instanceof ViewGroup) {
	                findInput((ViewGroup) child);
	            } else if (child instanceof EditText) {
	                return (EditText) child;
	            }
	        }
	        return null;
	    }
	    
	    public void show() {
	    	dlg.show();
	        
            EditText dlgf_min = (EditText) dlg.findViewById(R.id.function_value_range_set_dialog_minfield);
            EditText dlgf_max = (EditText) dlg.findViewById(R.id.function_value_range_set_dialog_maxfield);
            EditText dlgf_step = (EditText) dlg.findViewById(R.id.function_value_range_set_dialog_stepfield);
            RadioButton dlgf_step_auto = (RadioButton) dlg.findViewById(R.id.function_value_range_set_dialog_step_setting_auto);
            RadioButton dlgf_step_custom = (RadioButton) dlg.findViewById(R.id.function_value_range_set_dialog_step_setting_custom);
            RadioGroup dlgf_step_settings = (RadioGroup) dlg.findViewById(R.id.function_value_range_set_dialog_step_settings);	
            
            dlgf_step_auto.setOnCheckedChangeListener(this);
            dlgf_step_custom.setOnCheckedChangeListener(this);
            
            dlgf_step.setText( String.format("%f", (((FunctionVariable)Core.functions.get(functIndex)).pbStepValue)).replaceAll(",","\\.") );
            if(((FunctionVariable)Core.functions.get(functIndex)).pbStepValue<=0||((FunctionVariable)Core.functions.get(functIndex)).pbStepAuto) {
            	dlgf_step.setEnabled(false);
            	dlgf_step_auto.setChecked(true);
            	dlgf_step_custom.setChecked(false);
            } else {
            	dlgf_step.setEnabled(true);
            	dlgf_step_auto.setChecked(false);
            	dlgf_step_custom.setChecked(true);
            }
            	
            dlgf_min.setText( String.format("%f", (((FunctionVariable)Core.functions.get(functIndex)).pbMinValue)).replaceAll(",","\\.") );
            dlgf_max.setText( String.format("%f", (((FunctionVariable)Core.functions.get(functIndex)).pbMaxValue)).replaceAll(",","\\.") );

            dlgf_step.setImeActionLabel("",EditorInfo.IME_ACTION_NEXT);
           
	    }

		@Override
		public void onCheckedChanged(CompoundButton view, boolean isChecked) {		    
		    switch(view.getId()) {
		        case R.id.function_value_range_set_dialog_step_setting_custom:
		        	if(isChecked) {
		            	FunctionVariable fvar = (FunctionVariable) Core.functions.get(functIndex);
		            	fvar.pbStepAuto = false;
		            	EditText dlgf_step = (EditText) dlg.findViewById(R.id.function_value_range_set_dialog_stepfield);
		            	dlgf_step.setEnabled(true);
		            	Core.functions.setElementAt(fvar, functIndex);
		            }
		            break;
		        case R.id.function_value_range_set_dialog_step_setting_auto:
		            if(isChecked) {
		            	FunctionVariable fvar = (FunctionVariable) Core.functions.get(functIndex);
		            	fvar.pbStepAuto = true;
		            	EditText dlgf_step = (EditText) dlg.findViewById(R.id.function_value_range_set_dialog_stepfield);
		            	dlgf_step.setEnabled(false);
		            	Core.functions.setElementAt(fvar, functIndex);
		            }
		            break;
		    }
		}
	}

}
