package com.example.extremetic_tac_toe;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Vector;

import android.content.Context;
import android.net.Uri;
import android.widget.Toast;

import expr.Parser;
import expr.Variable;

public class Function {

	protected boolean isTemporary = false; 
	
	public String eqaution = "";
	public String lua_name = "";
	public String prp_equation = "";
	public int color = android.graphics.Color.RED;
	public int object_type = ENTRY_TYPE_FUNCTION;
	public boolean isDisabled = false;
	
	public static final int ENTRY_TYPE_UNKNOWN = 0;
	public static final int ENTRY_TYPE_FUNCTION = 1;
	public static final int ENTRY_TYPE_VARIABLE = 2;
	public static final int ENTRY_TYPE_CURVE_OBJECT = 3;
	public static final int ENTRY_TYPE_LUA_DRAWER = 4;
	
	public static final int FUNCTION_TYPE_UNKNOWN = 0;
	public static final int FUNCTION_TYPE_INCREASING = 100;
	public static final int FUNCTION_TYPE_DECREASING = 101;
	public static final int FUNCTION_TYPE_CONSTANT = 102;
	public static final int FUNCTION_TYPE_SOFTLY_INCREASING = 103;
	public static final int FUNCTION_TYPE_SOFTLY_DECREASING = 104;
	public static final int FUNCTION_TYPE_PARITY = 200;
	public static final int FUNCTION_TYPE_NOTPARITY = 201;
	public static final int FUNCTION_TYPE_NOT_PARITY_NOT_NOTPARITY = 202;
	public static final int FUNCTION_TYPE_PARITY_AND_NOTPARITY = 203;
	
	public boolean isTemporary() {
		return isTemporary;
	}
	
	public String decodeObjectType(int type) {
		if(type==ENTRY_TYPE_FUNCTION) {
			return "type.function";
		} else if(type==ENTRY_TYPE_VARIABLE) {
			return "type.var";
		} else if(type==ENTRY_TYPE_CURVE_OBJECT) {
			return "type.2dplot";
		} else if(type==ENTRY_TYPE_LUA_DRAWER) {
			return "type.lua_script";
		} else {
			return "type.null";
		}
	}
	
	public int codeObjectType(String type) {
		if(type.equals("type.function")) {
			return ENTRY_TYPE_FUNCTION;
		} else if(type.equals("type.var")) {
			return ENTRY_TYPE_VARIABLE;
		} else if(type.equals("type.2dplot")) {
			return ENTRY_TYPE_CURVE_OBJECT;
		} else if(type.equals("type.lua_script")) {
			return ENTRY_TYPE_LUA_DRAWER;
		} else {
			return ENTRY_TYPE_UNKNOWN;
		}
	}
	
	private FunctionProperties props = null;
	
	public boolean haveFoundProperties() {
		return props != null;
	}
	
	public void clearProperties() {
		props = null;
	}
	
	public void findProperties() {
		if(!haveFoundProperties()) {
			props = new FunctionProperties(this);
		}
	}
	
	public FunctionProperties getProperties() {
		findProperties();
		return props;
	}
	
	public Function(Function funct) {
		eqaution = funct.eqaution;
		lua_name = funct.lua_name;
		color = funct.color;
		object_type = funct.object_type;
		
	}
	
	public Function() {
		
	}
	
	public Function(String eq) {
		eqaution = eq;
	}
	
	public boolean checkIsInjective() {
		final int temp = checkMonotonicity();
		return (temp==FUNCTION_TYPE_INCREASING||temp==FUNCTION_TYPE_DECREASING);
	}
	
	public int checkParity() {
		double temp = 0, temp2 = 0;
		boolean isParity = true;
		boolean isNotParity = true;
		for(double it=0;it<50;it+=0.1) {
			temp = eval(it);
			temp2 = eval(-it);
			if(isParity) {
				if(!DoubleComparator.equals(temp,temp2)) {
					isParity = false;
				}
			}
			if(isNotParity) {
				if(!DoubleComparator.equals(temp, -temp2)) {
					isNotParity = false;
				}
			}
			if((!isNotParity)&&(!isParity)) {
				break;
			}
		}
		if((isNotParity)&&(isParity)) {
			return FUNCTION_TYPE_PARITY_AND_NOTPARITY;
		}
		if((!isNotParity)&&(isParity)) {
			return FUNCTION_TYPE_PARITY;
		}
		if((isNotParity)&&(!isParity)) {
			return FUNCTION_TYPE_NOTPARITY;
		}
		if((!isNotParity)&&(!isParity)) {
			return FUNCTION_TYPE_NOT_PARITY_NOT_NOTPARITY;
		}
		return FUNCTION_TYPE_NOT_PARITY_NOT_NOTPARITY;
	}
	
	public int checkMonotonicity() {
		boolean isIncreasing = false;
		boolean isDescrasing = false;
		boolean isConsant = false;
		
		double temp = 0;
		for(int it=-50;it<50;++it) {
			temp = evalDifferentiate(it, 1);
			//System.out.println("Diff on x = "+((Object)it).toString()+" is "+((Object)temp).toString());
			if(DoubleComparator.lessThan(temp,0)) {isDescrasing=true;}
			if(DoubleComparator.equals(temp,0)) {isConsant=true;}
			if(DoubleComparator.greaterThan(temp,0)) {isIncreasing=true;}
		}
		
		if((isConsant)&&(!isIncreasing)&&(!isDescrasing)) {
			return FUNCTION_TYPE_CONSTANT;
		}
		if((!isConsant)&&(isIncreasing)&&(!isDescrasing)) {
			return FUNCTION_TYPE_INCREASING;
		}
		if((!isConsant)&&(!isIncreasing)&&(isDescrasing)) {
			return FUNCTION_TYPE_DECREASING;
		}
		if((isConsant)&&(isIncreasing)&&(!isDescrasing)) {
			return FUNCTION_TYPE_SOFTLY_INCREASING;
		}
		if((isConsant)&&(!isIncreasing)&&(isDescrasing)) {
			return FUNCTION_TYPE_SOFTLY_DECREASING;
		}
		return FUNCTION_TYPE_UNKNOWN;
	}
	
	private double[] vectorToArray(Vector<double[]> k) {
		final int len = k.size();
		double result[] = new double[len];
		for(int it=0;it<len;++it) {
			result[it] = k.get(it)[0];
		}
		return result;
	}
	
	public double[] findFunctionFixedPoints() {
		return (new Function((new String("(")).concat(eqaution).concat(")-x"))).findFunctionRoots();
	}
	
	public double[] findFunctionRoots() {
		Vector<double[]>roots = new Vector<double[]>();
		final int limit = 10;
		int rootit = 0;
		boolean add=true;
		for(int it=-50;it<50;++it) {
			final double result = runNewtonMethod(it);
			if(DoubleComparator.equals(eval(result),0,0.00000001)) {
				add=true;
				for(int j=0;j<rootit;++j) {
					if(DoubleComparator.equals(roots.get(j)[0], result)) {
						add=false;
					}
				}
				if(add) {
					roots.add(new double[]{result});
					++rootit;
					if(rootit>=limit) {
						return vectorToArray(roots);
					}
				}
			}
		}
		return vectorToArray(roots);
	}
	
	
	private double runNewtonMethod(double x0) {
		double x1 = x0;
		double tolerance =     0.00000001;
		final double epsilon = 0.0000000001;
		final int maxProbes = 3;
		final int maxIterations = 35;
		boolean foundSolution = true;
		
		for(int j=0;j<maxProbes;++j) {
			for(int it=0;it<maxIterations;++it) {
				double y = eval(x0);
				double yprime = evalDifferentiate(x0, 1);
				if(Math.abs(yprime)<epsilon) {
					break;
				}
				x1 = x0-y/yprime;
				if(Math.abs(x1-x0)/Math.abs(x1)<tolerance) {
					foundSolution = true;
					break;
				}
				x0 = x1;
			}
			if(!foundSolution) {
				tolerance*=2;
			} else {
				break;
			}
		}
		if(foundSolution) {
			return x1;
		}
		return 0.0;
	}
	
	public boolean isGood() {
		if(object_type==Function.ENTRY_TYPE_VARIABLE) return true;
		return true;
		
		/*double x = Math.random()*100;
		for(int it=0;it<5;++it) {
			try {
				LatexParser.calculate(x, eqaution);
				return true;
			} catch (Throwable e) { }
			x = Math.random()*100;
		}
		return false;*/
	}
	
	final static double epsilon = 0.05;
	double eval(double x) {
		if(prp_equation==null||prp_equation.equals("")) {
			try {
				prp_equation = LatexParser.preparseLatexString(eqaution);
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
		try {
			return LatexParser.calculate(x, prp_equation);
		} catch(Throwable t) {
			
		}
		return 0.0;
	}
	double eval(double x, double y) {
		if(prp_equation==null||prp_equation.equals("")) {
			try {
				prp_equation = LatexParser.preparseLatexString(eqaution);
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
		try {
			return LatexParser.calculate(x, y, prp_equation);
		} catch(Throwable t) {
			
		}
		return 0.0;
	}
	
	double evalDifferentiate(double x, int level) {
		if(level<=0) {
			return 0;
		}
		if(level==1) {
			return (eval(x+epsilon)-eval(x))/epsilon;
		}
		return (evalDifferentiate(x+epsilon, level-1)-evalDifferentiate(x, level-1))/epsilon;
	}
	
	public double probePoint(double x, double y) {
		return eval(x,y);
	}
	
	public boolean compare(Function funct) {
		if((funct.eqaution.equals(this.eqaution) && funct.object_type == this.object_type)) {
			return true;
		}
		return false;
	}
	
	public String serialize() {
		String txtData = "";
		txtData = 
		"###begin.entry;$$\n" +
		"object-type===>" + decodeObjectType(this.object_type) + ";$$\n" +
		"equation===>" + this.eqaution.replaceAll("\n", "\\<\\b\\r\\>") + ";$$\n";
		
		if(this.prp_equation.trim().length()>0) {
			txtData += "__equation===>" + this.prp_equation.replaceAll("\n", "\\<\\b\\r\\>") + ";$$\n";
		}
		if(this.object_type==Function.ENTRY_TYPE_LUA_DRAWER) {
			txtData += "name===>" + this.lua_name + ";$$\n";
		}
		
		txtData +=
		"color===>" + ((Object)this.color).toString() + ";$$\n" +
		"###end.entry;$$\n"
		;
		return txtData;
	}
	
	public static Vector<Function> importFile(Context context, Uri path) {
		return importFile(context, path.getPath());
	}
	
	public static Vector<Function> importFile(Context context, String path) {
		System.out.println("TRYING TO IMPORT FILE \""+path+"\".");
		
		File myFile = new File(path);
		Vector<Function> functvc = new Vector<Function>();
		try {
			char buf[] = new char[1000];
			
			FileInputStream fIn = new FileInputStream(myFile);
			InputStreamReader myInReader = new InputStreamReader(fIn);
			final int readen = myInReader.read(buf);
			
			final String data = new String(buf, 0, readen);
			String[] lines = data.split("\\;\\$\\$");
			
			final int len = lines.length;
			Function functbuf = new Function();
			boolean forceAdd = false;
			
			for(int it=0;it<len;++it) {
				lines[it] = lines[it].replaceAll("\n", "");
				if(lines[it].length()>0) {
					if(lines[it].startsWith("###")) {
						lines[it] = lines[it].replaceAll("\\#\\#\\#", "");
						System.out.println("Spec. Token = {"+lines[it]+"}");
						if(lines[it].equals("begin.entry")) {
							functbuf = new Function();
						} else if(lines[it].equals("end.entry")) {
							if(functbuf.object_type == Function.ENTRY_TYPE_VARIABLE) {
								functvc.add( FunctionVariable.create(functbuf) );
							} else {
								functvc.add(new Function(functbuf));
							}
						}
					} else {
						String token[] = lines[it].split("\\=\\=\\=\\>");
						final int tokennum = token.length;
						System.out.println("Token: {"+token[0]+"} = {"+token[1]+"}");
						if(token[0].equals("object-type")) {
							functbuf.object_type = functbuf.codeObjectType(token[1]);
						} else if(token[0].equals("equation")) { 
							functbuf.eqaution = token[1].replaceAll("\\<\\b\\r\\>", "\n");
							try {
								functbuf.prp_equation = LatexParser.preparseLatexString(functbuf.eqaution);
							} catch (Throwable e) {
								e.printStackTrace();
							}
						} else if(token[0].equals("__equation")) { 
							functbuf.prp_equation = token[1].replaceAll("\\<\\b\\r\\>", "\n");
						} else if(token[0].equals("color")) { 
							functbuf.color = Integer.parseInt(token[1]);
						} else if(token[0].equals("name")) {
							System.out.println("NAME ADAPTED");
							functbuf.lua_name = token[1];
						}
					}
				}
			}
			
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e2) {
			e2.printStackTrace();
		}
		
		return functvc;
	}
	
	public static void exportFile(String txtData, Context context, String path) {
		try {
	        File myFile = new File(path);
	        myFile.createNewFile();
	        FileOutputStream fOut = new FileOutputStream(myFile);
	        OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
	        myOutWriter.append(txtData);
	        myOutWriter.close();
	        fOut.close();
	        
	        Core.whenSavingIsCompleted(path);
	        Toast.makeText(context,"Done writing SD 'mysdfile.txt'",Toast.LENGTH_SHORT).show();
	    } catch (Exception e) {
	        Toast.makeText(context, e.getMessage(),Toast.LENGTH_SHORT).show();
	    }
	}
	
	public String luaOnFunctionLooking(double x, double y) {
		return LuaFunctionPlugin.execOnFunctionLooking(eqaution, x, y);
	}
	
	public void drawLua() {
		LuaFunctionPlugin.exec(eqaution);
	}
}
