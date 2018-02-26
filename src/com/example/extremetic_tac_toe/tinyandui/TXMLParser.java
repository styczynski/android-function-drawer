package com.example.extremetic_tac_toe.tinyandui;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

import android.R.style;
import android.app.Activity;
import android.content.res.AssetManager;

import java.io.File;
import java.io.InputStream;
import java.util.Hashtable;
import java.util.Vector;
 
public class TXMLParser {
 
	public Hashtable<String, TSkin> skins = new Hashtable<String, TSkin>();
	
	public TComponent recursiveParser(Node el, TContainer container) {
		return recursiveParser(el, container, false);
	}
	
	public TComponent recursiveParser(Node el, TContainer container, boolean parseOnlyGlobal) {
		String nodeName = el.getNodeName();
		if(!(el instanceof Element)) {
			return null;
		}
		Element e = (Element)el;
		if(nodeName.equals("fence")) {
			if(parseOnlyGlobal) {return null;}
			TFence fence = new TFence(container, e.getAttribute("id"));
			fence.setVisible(true);
			NodeList children = el.getChildNodes();
			final int len = children.getLength();
			for(int it=0;it<len;++it) {
				if(children.item(it)instanceof Node) {
					TComponent comp = recursiveParser(children.item(it), container, parseOnlyGlobal);
					if(comp!=null) {
						fence.add(comp);
					}
				}
			}
			return fence;
		}
		if(nodeName.equals("container")) {
			NodeList children = el.getChildNodes();
			final int len = children.getLength();
			for(int it=0;it<len;++it) {
				if(children.item(it)instanceof Node) {
					TComponent comp = recursiveParser(children.item(it), container, parseOnlyGlobal);
					if(comp!=null) {
						container.add(comp);
					}
				}
			}
			return container;
		}
		if(nodeName.equals("style")) {
			TSkin newStyle = new TSkin();
			String styleName = e.getAttribute("id");
			System.out.println("Creating style {id="+styleName+"}");
			
			NodeList children = el.getChildNodes();
			String childrenName = "";
			String childrenVal = "";
			final int len = children.getLength();
			for(int it=0;it<len;++it) {
				if(children.item(it) instanceof Element) {
					childrenName = children.item(it).getNodeName();
					childrenVal = ((Element)children.item(it)).getAttribute("value");
					newStyle.val(childrenName, childrenVal);
					System.out.println("style.value {name="+childrenName+", value="+childrenVal+"}");
				}
			}
			
			skins.put(styleName, newStyle);
			return null;
		}
		
		String cid = e.getAttribute("id");
		float cx = Float.parseFloat(e.getAttribute("posx"));
		float cy = Float.parseFloat(e.getAttribute("posy"));
		float cw = 0.0f;
		float ch = 0.0f;
		TSkin cskin = null;
		int czindex = 0;
		boolean cvisible = !(e.getAttribute("visible")).equals("false");
		
		if(!e.getAttribute("w").equals("")) {
			cw = Float.parseFloat(e.getAttribute("w"));
		}
		if(!e.getAttribute("h").equals("")) {
			ch = Float.parseFloat(e.getAttribute("h"));
		}
		if(!e.getAttribute("zindex").equals("")) {
			czindex = Integer.parseInt(e.getAttribute("zindex"));
		}
		
		String cstylename = e.getAttribute("style");
		if(cstylename.contains("{")) {
			cskin = new TSkin();
			cskin.loadFrom(cstylename);
		} else {
			if((!cstylename.equals(""))&&skins.containsKey(cstylename)) {
				System.out.println("Setted skin {id="+cstylename+"}");
				cskin = skins.get(cstylename);
			}
		}
		
		
		if(nodeName.equals("window")) {
			if(parseOnlyGlobal) {return null;}
			TWindow wnd = new TWindow(container, e.getAttribute("id"));
			wnd.setVisible(cvisible);
			wnd.setPos(cx, cy);
			
			if(cw!=0.0f) { wnd.setW(cw); }
			if(ch!=0.0f) { wnd.setH(ch); }
			if(czindex!=0) { wnd.setZIndex(czindex); }
			
			if(cskin!=null) {
				wnd.setSkin(cskin);
			}
			
			NodeList children = el.getChildNodes();
			final int len = children.getLength();
			for(int it=0;it<len;++it) {
				if(children.item(it)instanceof Node) {
					TComponent comp = recursiveParser(children.item(it), container, parseOnlyGlobal);
					if(comp!=null) {
						wnd.add(comp);
					}
				}
			}
			return wnd;
		}
		if(nodeName.equals("image")) {
			if(parseOnlyGlobal) {return null;}
			TImage img = new TImage(cid, cx, cy);
			img.setVisible(cvisible);
			
			if(cw!=0.0f) { img.setW(cw); }
			if(ch!=0.0f) { img.setH(ch); }
			if(czindex!=0) { img.setZIndex(czindex); }
			
			if(cskin!=null) {
				img.setSkin(cskin);
			}
			
			return img;
		}
		if(nodeName.equals("background")) {
			if(parseOnlyGlobal) {return null;}
			TBackground img = new TBackground(cid, cx, cy);
			img.setVisible(cvisible);
			
			if(cw!=0.0f) { img.setW(cw); } else { img.setW(TCore.getScreenW()); }
			if(ch!=0.0f) { img.setH(ch); } else { img.setH(TCore.getScreenH()); }
			if(czindex!=0) { img.setZIndex(czindex); }
			
			if(cskin!=null) {
				img.setSkin(cskin);
			}
			
			return img;
		}
		if(nodeName.equals("button")) {
			if(parseOnlyGlobal) {return null;}
			TButton but = new TButton(cid, cx, cy);
			but.setVisible(cvisible);
			
			if(cw!=0.0f) { but.setW(cw); }
			if(ch!=0.0f) { but.setH(ch); }
			if(czindex!=0) { but.setZIndex(czindex); }
			
			if(cskin!=null) {
				but.setSkin(cskin);
			}
			
			return but;
		}
		if(nodeName.equals("tbutton")) {
			if(parseOnlyGlobal) {return null;}
			String text = e.getAttribute("text");
			
			TTextButton but;
			if(cskin!=null) {
				but = new TTextButton(cid, cx, cy, new TText(text).useSkin(cskin));
			} else {
				but = new TTextButton(cid, cx, cy, new TText(text));
			}
			but.setVisible(cvisible);
			
			if(cw!=0.0f) { but.setW(cw); } else {
				but.setW(-1);
			}
			if(ch!=0.0f) { but.setH(ch); }
			if(czindex!=0) { but.setZIndex(czindex); }
			
			if(cskin!=null) {
				but.setSkin(cskin);
			}
			
			return but;
		}
		if(nodeName.equals("textinput")) {
			if(parseOnlyGlobal) {return null;}
			String text = e.getAttribute("text");
			
			TTextInput but;
			if(cskin!=null) {
				but = new TTextInput(cid, cx, cy, new TText(text).useSkin(cskin));
			} else {
				but = new TTextInput(cid, cx, cy, new TText(text));
			}
			but.setVisible(cvisible);
			
			if(cw!=0.0f) { but.setW(cw); } else {
				but.setW(-1);
			}
			if(ch!=0.0f) { but.setH(ch); }
			if(czindex!=0) { but.setZIndex(czindex); }
			
			if(cskin!=null) {
				but.setSkin(cskin);
			}
			
			return but;
		}
		if(nodeName.equals("canvas")) {
			if(parseOnlyGlobal) {return null;}
			
			TCanvas canvas = null;
			
			float maxw = 0.0f;
			float maxh = 0.0f;
			float canvw = 0.0f;
			float canvh = 0.0f;
			if(!e.getAttribute("canvas.maxw").equals("")) {
				maxw = Float.parseFloat(e.getAttribute("canvas.maxw"));
			}
			if(!e.getAttribute("canvas.maxh").equals("")) {
				maxh = Float.parseFloat(e.getAttribute("canvas.maxh"));
			}
			if(!e.getAttribute("canvas.w").equals("")) {
				canvw = Float.parseFloat(e.getAttribute("canvas.w"));
			}
			if(!e.getAttribute("canvas.h").equals("")) {
				canvh = Float.parseFloat(e.getAttribute("canvas.h"));
			}
			if(maxw==0.0f) {
				maxw = cw;
			}
			if(maxh==0.0f) {
				maxh = ch;
			}
			if(canvw==0.0f) {
				canvw = maxw;
			}
			if(canvh==0.0f) {
				canvh = maxh;
			}
			
			canvas = new TCanvas(cid, cx, cy, cw, ch, maxw, maxh);
			canvas.setVisible(cvisible);
			canvas.setCanvasDimension(maxw, maxh);
			System.out.println("CANVAS MAKED: maxw="+((Object)maxw).toString()+", maxh="+((Object)maxh).toString()+", canvw="+((Object)canvw).toString()+", canvh="+((Object)canvh).toString());
			
			if(czindex!=0) { canvas.setZIndex(czindex); }
			
			canvas.clear();
			return canvas;
		}
		return null;
	}
	
	public void load(Activity activity, TContainer container) {
		try {
		    	
		    AssetManager assetManager = activity.getAssets();
		    InputStream ims = assetManager.open("xml/ui.xml");
		    	
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(ims);
		 
			doc.getDocumentElement().normalize();
		 
			recursiveParser(doc.getDocumentElement(), container, true);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void finalizeLoading(TContainer container) {
		container.mScene.sortChildren();
	}
	
	public void loadFence(Activity activity, TFence fence, TContainer container, String fenceName) {
		try {
		    	
		    AssetManager assetManager = activity.getAssets();
		    InputStream ims = assetManager.open("xml/ui.xml");
		    	
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(ims);
		 
			doc.getDocumentElement().normalize();
		 
			NodeList nl = doc.getDocumentElement().getElementsByTagName("fence");
			Node fenceNode = null;
			final int len = nl.getLength();
			for(int it=0;it<len;++it) {
				if(((Element)nl.item(it)).getAttribute("id").equals(fenceName)) {	
					fenceNode = nl.item(it);
				}
			}
			
			NodeList children = fenceNode.getChildNodes();
			final int fnlen = children.getLength();
			for(int it=0;it<fnlen;++it) {
				if(children.item(it) instanceof Node) {
					TComponent comp = recursiveParser(children.item(it), container);
					if(comp!=null) {
						fence.add(comp);
					}
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
 
}