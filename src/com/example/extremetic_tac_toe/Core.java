package com.example.extremetic_tac_toe;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Vector;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Bitmap.Config;
import android.net.Uri;
import android.widget.ArrayAdapter;

public class Core {

	public static Vector<Function> functions = new Vector<Function>();
	public static Vector<Function> functionsSavingBuffer = new Vector<Function>();
	public static int toEditIndex = -1;
	
	protected static boolean openFileAlwaysAfterSaving = false;
	protected static boolean clearAlwaysAtStartup = false;
	
	protected static void whenSavingIsCompleted(final String path) {
		if(openFileAlwaysAfterSaving) {
			File file = new File(path);
			Intent i = new Intent();
			i.setAction(android.content.Intent.ACTION_VIEW);
			//i.setDataAndType(Uri.fromFile(file), "video/mpeg");
			MainActivity.runningMain.startActivity(i);
		}
	}
	
	public static void exportAs(final String opName, final String path, final Activity act) {
		if(opName.equals("PNG")||opName.equals("JPEG")) {
		
			AlertDialog.Builder builderSingle = new AlertDialog.Builder(act);
            builderSingle.setIcon(R.drawable.ic_launcher);
            builderSingle.setTitle("Please select output mode.");
            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(act, android.R.layout.select_dialog_singlechoice);
            arrayAdapter.add("Defualt view");
            arrayAdapter.add("Currently set view");
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
							final String opMode = arrayAdapter.getItem(which);
							
							FunctionDrawer fd = new FunctionDrawer();
							
							if(opMode.equals("Currently set view")) {
								fd = DrawingPanelActivity.runningActivity.fd;
							}
							
							fd.functions = functionsSavingBuffer;
							Paint paint = new Paint();
							Bitmap bmp = Bitmap.createBitmap(500, 500, Config.ARGB_8888);
							Canvas canvas = new Canvas(bmp);
							paint.setColor(Color.WHITE);
							canvas.drawPaint(paint);
							paint.setColor(Color.BLACK);
							fd.paintComponent(canvas, paint, 500, 500);
							
							
							FileOutputStream out = null;
							try {
							       out = new FileOutputStream(path);
							       if(opName.equals("PNG")) {
							    	   bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
							       } else if(opName.equals("JPEG")) {
							    	   bmp.compress(Bitmap.CompressFormat.JPEG, 90, out);
							       }
							       whenSavingIsCompleted(path);
							} catch (Exception e) {
							    e.printStackTrace();
							} finally {
							       try{
							           out.close();
							       } catch(Throwable ignore) {}
							}
							
						}
            				
            		}
            );
            builderSingle.create().show();
			
		} else if(opName.equals("Plain text")) {
			FileOutputStream out = null;
			try {
			    out = new FileOutputStream(path);
			    final int len = functionsSavingBuffer.size();
			    String str = "";
			    for(int it=0;it<len;++it) {
			    	if(functionsSavingBuffer.get(it).object_type==Function.ENTRY_TYPE_VARIABLE) {
			    		FunctionVariable fvar = new FunctionVariable();
			    		fvar = (FunctionVariable) FunctionTypeSelector.select(functionsSavingBuffer.get(it));
			    		fvar.updateState();
			    		str+="Variable: "+fvar.eqaution+"\r\n";
			    	} else {
			    		str+="Expression: "+functionsSavingBuffer.get(it).eqaution+"\r\n";
			    	}
			    }
			    byte[] contentInBytes = str.getBytes();
				out.write(contentInBytes);
				out.flush();
				whenSavingIsCompleted(path);
			       
			} catch (Exception e) {
			    e.printStackTrace();
			} finally {
			       try{
			           out.close();
			       } catch(Throwable ignore) {}
			}
		}
	}
	
	protected static void cleanupTemporaryFunctions() {
		int len = functions.size();
		for(int it=0;it<len;++it) {
			if(functions.get(it).isTemporary) {
				functions.remove(it);
				--it;
				--len;
			}
		}
	}
	
	public static boolean integrateFunction(Function funct) {
		final int len = functions.size();
		for(int it=0;it<len;++it) {
			if(functions.get(it).compare(funct)) {
				System.out.println("Function "+funct.eqaution+" is identical to "+functions.get(it).eqaution);
				return false;
			}
		}
		functions.add(funct);
		return true;
	}
	
}
