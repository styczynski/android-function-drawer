package com.example.extremetic_tac_toe;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

import de.congrace.exp4j.Calculable;
import de.congrace.exp4j.CustomFunction;
import de.congrace.exp4j.CustomOperator;
import de.congrace.exp4j.ExpressionBuilder;
import de.congrace.exp4j.InvalidCustomFunctionException;
import de.congrace.exp4j.UnknownFunctionException;
import de.congrace.exp4j.UnparsableExpressionException;


public class LatexParser {
	
	private static int iterator;
	private static double EPSILON = 0.00000001;
	private static HashMap<String, Double> vars = new HashMap<String, Double>();
	private static CustomFunction funct_cfrac=null, funct_pow=null, funct_frac=null, funct_sqrt=null, funct_ln=null, funct_log=null, funct_min=null, funct_max=null;
	private static CustomOperator funct_is_eq=null;
	private static double x_var = 0.0;
	private static double y_var = 0.0;
	
	public static void setX(double x) {
		x_var = x;
	}
	
	public static void setY(double y) {
		y_var = y;
	}
	
	private static String extractSingleArgument(String arg0, String arg1) {
		if(arg0.length()==2) {
			if(arg0.substring(1).equals(arg1)) {
				return arg0.substring(0, 1);
			}
		}
		return arg0;
	}
	
	private static String findTokenArg(String data) {
		final int len = data.length();
		int level = 0;
		String buf = "";
		short singleArgType = 0;
		for(int it=iterator;it<len;++it) {
			if(data.charAt(it)=='{'/*||data.charAt(it)=='('*/||data.charAt(it)=='[') {
				if(singleArgType!=0) {
					iterator = it;
					return buf;
				}
				if(level!=0) {
					buf = buf.concat(data.substring(it, it+1));
				}
				++level;
			} else if(data.charAt(it)=='}'/*||data.charAt(it)==')'*/||data.charAt(it)==']') {
				if(singleArgType!=0) {
					iterator = it;
					return buf;
				}
				--level;
				if(level!=0) {
					buf = buf.concat(data.substring(it, it+1));
				} else {
					if(it+1<len) {
						iterator = it+1;
					} else {
						iterator = len-1;
					}
					return buf;
				}
			}  else if(level==0) {
				
				if(singleArgType==0 && (data.charAt(it)>='a'&&data.charAt(it)<='z')||(data.charAt(it)>='A'&&data.charAt(it)<='Z')) {
					singleArgType=1;
				}
				if(singleArgType==0 && (data.charAt(it)>='0'&&data.charAt(it)<='9')) {
					singleArgType=2;
				}
				if((singleArgType==1&&(data.charAt(it)>='0'&&data.charAt(it)<='9'))||(singleArgType==2&&(data.charAt(it)>='a'&&data.charAt(it)<='z')||(data.charAt(it)>='A'&&data.charAt(it)<='Z'))||(!((data.charAt(it)>='a'&&data.charAt(it)<='z')||(data.charAt(it)>='A'&&data.charAt(it)<='Z')||(data.charAt(it)>='0'&&data.charAt(it)<='9')||data.charAt(it)=='.'||data.charAt(it)==' '))) {
					iterator = it;
					return buf;
				} else {
					if(data.charAt(it)!=' ') {
						buf = buf.concat(data.substring(it, it+1));
					}
				}
			} else {
				if(level==0&&data.charAt(it)==' ') {
					
				} else {
					buf = buf.concat(data.substring(it, it+1));
				}
			}
		}
		iterator = len-1;
		return buf;
	}
	
	
	private static String findSpecialTextArgument(String data) {
		final int len = data.length();
		int level = 0;
		String buf = "";
		for(int it=iterator;it<len;++it) {
			if(data.charAt(it)=='{'||data.charAt(it)=='('||data.charAt(it)=='[') {
				++level;
				if(level>=0) {
					buf = buf.concat(data.substring(it, it+1));
				} else {
					if(it+1<len) {
						iterator = it+1;
					} else {
						iterator = len-1;
					}
					return buf;
				}
			} else if(data.charAt(it)=='}'||data.charAt(it)==')'||data.charAt(it)==']') {
				--level;
				if(level>=0) {
					buf = buf.concat(data.substring(it, it+1));
				} else {
					if(it+1<len) {
						iterator = it+1;
					} else {
						iterator = len-1;
					}
					return buf;
				}
			} else {
				buf = buf.concat(data.substring(it, it+1));
			}
		}
		iterator = len-1;
		return buf;
	}
	
	private static int findOnArray(String el, String arr[]) {
		final int len = arr.length;
		for(int it=0;it<len;++it) {
			if(el.equals(arr[it])) {
				return it;
			}
		}
		return -1;
	}
	
	private static double eval(String str) throws Throwable {
		Calculable calc;
		try {
			ExpressionBuilder expbl = new ExpressionBuilder(str)
			.withVariable("x",x_var)
			.withVariable("y",y_var)
			.withVariable("e", Math.E)
			.withVariable("pi", Math.PI)
			.withOperation(funct_is_eq)
			.withCustomFunction(funct_cfrac)
			.withCustomFunction(funct_pow)
			.withCustomFunction(funct_frac)
			.withCustomFunction(funct_sqrt)
			.withCustomFunction(funct_ln)
			.withCustomFunction(funct_log)
			.withCustomFunction(funct_min)
			.withCustomFunction(funct_max);
			
			final int len = Core.functions.size();
			for(int it=0;it<len;++it) {
				if(Core.functions.get(it).object_type==Function.ENTRY_TYPE_VARIABLE) {
					final FunctionVariable fvar = (FunctionVariable) Core.functions.get(it);
					expbl = expbl.withVariable(fvar.name, fvar.value);
				}
			}
			
			calc = expbl.build();
			
			return calc.calculate();
			
		} catch (Throwable e) {
			throw e;
		}
	}
	
	public static String preparseLatexString(String str) throws Throwable {
		String expr = str;
    	String wyn = "";
    	final int len = expr.length();
    	String tbuf = "";
    	boolean command = false;
    	int lastComPos = 0;
    	
    	String elementaryFunctionsOneArg[] = {"ln", "abs", "acos", "asin", "atan", "ceil", "cos", "cosh", "exp", "floor", "sin", "sinh", "tan", "tanh"};
    	String elementaryFunctionsTwoArg[] = {"log", "min", "max"};
    	
    	for(int it=0;it<len;++it) {
    		
    		if(command) {
    			if((expr.charAt(it)>='a'&&expr.charAt(it)<='z')||(expr.charAt(it)>='A'&&expr.charAt(it)<='Z')) {
    				tbuf = tbuf.concat(expr.substring(it, it+1));
    				//System.out.println(tbuf);
    			} else {
    				if(expr.charAt(it)=='_') {
    					tbuf = tbuf.substring(0, tbuf.length());
    					++it;
    				}
    				
    				command = false;
    				//System.out.println("TOKEN = ["+tbuf+"]");
    				
    				/*iterator = it;
    				System.out.println("ARG#0 = ["+findTokenArg(expr)+"]");
    				it = iterator;
    				iterator = it;
    				System.out.println("ARG#1 = ["+findTokenArg(expr)+"]");
    				it = iterator;*/
    				
    				if(tbuf.equals("frac")) {
    					String arg0, arg1;
    					iterator = it;
        				arg0 = findTokenArg(expr);
        				it = iterator;
        				iterator = it;
        				arg1 = findTokenArg(expr);
        				it = iterator;
        				arg0 = extractSingleArgument(arg0, arg1);
        				
        				String argx0=preparseLatexString(arg0), argx1=preparseLatexString(arg1);
        				if(argx0.equals("d")&&argx1.equals("dx")) {
        					iterator = it;
            				String temp = findSpecialTextArgument(expr);
            				it = iterator;
            				//System.out.println("SPECIAL ARG =["+temp+"]");
            				wyn += "(("+temp.replaceAll("x", "(x+"+String.format("%.10f", EPSILON).replaceAll(",","\\.")+")")+")-("+temp+")"+")/("+String.format("%.10f", EPSILON).replaceAll(",","\\.")+")";
            				
        				} else {
        					wyn += "(("+argx0+")/("+argx1+"))";
        				}
        				
    				} else if(tbuf.equals("sqrt")) {
    					
    					System.out.println("[DEBUG] "+expr.charAt(it));
    					if(expr.charAt(it)=='{') {
    						String arg0;
	    					iterator = it;
	        				arg0 = findTokenArg(expr);
	        				it = iterator;
	        				
	        				wyn += "(("+preparseLatexString(arg0)+")^(0.5))";
    					} else {
	    					String arg0, arg1;
	    					iterator = it;
	        				arg0 = findTokenArg(expr);
	        				it = iterator;
	        				iterator = it;
	        				arg1 = findTokenArg(expr);
	        				it = iterator;
	        				arg0 = extractSingleArgument(arg0, arg1);
	        				
	        				wyn += "(("+preparseLatexString(arg1)+")^(1.0/("+preparseLatexString(arg0)+")))";
    					}
    				} else if(tbuf.equals("log")) {
    					String arg0, arg1;
    					iterator = it;
        				arg0 = findTokenArg(expr);
        				it = iterator;
        				iterator = it;
        				arg1 = findTokenArg(expr);
        				it = iterator;
        				arg0 = extractSingleArgument(arg0, arg1);
        				
    					wyn += "log("+preparseLatexString(arg0)+","+preparseLatexString(arg1)+")";
    				} else if(tbuf.equals("sum")) {
    					//\sum_{i=1}^0x
    					String arg0, arg1, arg2;
    					iterator = it;
        				arg0 = findTokenArg(expr);
        				it = iterator;
        				++it;
        				iterator = it;
        				arg1 = findTokenArg(expr);
        				it = iterator;
        				//++it;
        				
        				iterator = it;
        				arg2 = findSpecialTextArgument(expr);
        				it = iterator;
        				arg1 = extractSingleArgument(arg1, arg2);
            				
        				String arg0spl[] = arg0.split("=");
        				String varname = arg0spl[0];
        				double varval = eval(arg0spl[1]);
        				
        				String cummulator="";
        				double repval = eval(arg1);
        				final int rep = (int)Math.floor(repval);
        				for(int j=0;j<rep;++j) {
        					String tmp = arg2.replaceAll(varname, String.format("%f", varval).replaceAll(",","\\."));
        					cummulator += "("+tmp+")";
        					if(j!=rep-1) {
        						cummulator += "+";
        					}
        					++varval;
        				}
        				if(!DoubleComparator.equals((repval-((double)rep)),0)) {
        					String tmp = arg2.replaceAll(varname, String.format("%f", varval).replaceAll(",","\\."));
        					cummulator += "+(("+tmp+")*("+String.format("%f", (repval-((double)rep))).replaceAll(",","\\.")+"))";
        				}
        				
        				if(rep<=0) {
        					wyn+="0";
        				} else {
        					wyn+="("+cummulator+")";
        				}
        				
        				/*System.out.println("SPECIAL ARG0 =["+arg0+"]");
        				System.out.println("SPECIAL ARG1 =["+arg1+"]");
        				System.out.println("SPECIAL ARG2 =["+arg2+"]");*/
        				
        				
    				} else if(findOnArray(tbuf, elementaryFunctionsOneArg)!=-1) {
    					String arg0;
    					iterator = it;
        				arg0 = findTokenArg(expr);
        				it = iterator;
        				
        				wyn += elementaryFunctionsOneArg[findOnArray(tbuf, elementaryFunctionsOneArg)]+"("+preparseLatexString(arg0)+")";
    				} else if(findOnArray(tbuf, elementaryFunctionsTwoArg)!=-1) {
    					String arg0, arg1;
    					iterator = it;
        				arg0 = findTokenArg(expr);
        				it = iterator;
        				iterator = it;
        				arg1 = findTokenArg(expr);
        				it = iterator;
        				arg0 = extractSingleArgument(arg0, arg1);
        				
        				wyn += elementaryFunctionsTwoArg[findOnArray(tbuf, elementaryFunctionsTwoArg)]+"("+preparseLatexString(arg0)+", "+preparseLatexString(arg1)+")";
    				} else {
    					--it;
    				}
    				//--it;
    				
    			}
    		} else if(expr.charAt(it)=='\\') {
    			command = true;
    			tbuf = "";
    			lastComPos = it;
    		} else {
    			wyn = wyn.concat(expr.substring(it,it+1));
    		}
    	}
    	return wyn;
	}
	
	public static double calculate(String expr) throws Throwable {
		return eval(expr);
	}
	
	public static double calculate(double x, String expr) throws Throwable {
		setX(x);
		return eval(expr);
	}
	
	public static double calculate(double x, double y, String expr) throws Throwable {
		setX(x);
		setY(y);
		double pom = eval(expr);
		//System.out.println("Evaluating not-preparsed str = {"+expr+"} = {"+((Object)pom).toString()+"}");
		return pom;
	}
	
	public static void init() {
		try {
			funct_cfrac = new CustomFunction("cfrac") {
			    public double applyFunction(double[] values) {
			    	double ret = 0.0;
			    	final int argnum = this.getArgumentCount(); 
			    	for (int i=argnum-1;i>=1;--i) {
			            ret += values[i];
			            ret = 1.0/ret;
			        }
			    	if(argnum>=1) {
			    		return (1.0/ret)+values[0];
			    	}
			    	return 0.0;
			    }
			};
			funct_pow = new CustomFunction("pow", 2) {
			    public double applyFunction(double[] values) {
			                return Math.pow(values[0], values[1]);
			        }
			};
			funct_frac = new CustomFunction("frac", 2) {
			    public double applyFunction(double[] values) {
			                return values[0]/values[1];
			        }
			};
			funct_sqrt = new CustomFunction("sqrt", 2) {
			    public double applyFunction(double[] values) {
			    	return Math.pow(values[1], 1.0/values[0]);
			    }
			};
			funct_ln = new CustomFunction("ln", 1) {
			    public double applyFunction(double[] values) {
			    	return Math.log(values[0]);
			    }
			};
			funct_log = new CustomFunction("log", 2) {
			    public double applyFunction(double[] values) {
			    	return Math.log(values[1])/Math.log(values[0]);
			    }
			};
			funct_min = new CustomFunction("min") {
			    public double applyFunction(double[] values) {
			    	final int len = this.getArgumentCount();
			    	if(len==0) {
			    		return 0;
			    	}
			    	double min = values[0];
			    	for(int it=0;it<len;++it) {
			    		if(values[it]<min) min=values[it];
			    	}
			    	return min;
			    }
			};
			funct_max = new CustomFunction("max") {
				 public double applyFunction(double[] values) {
				    	final int len = this.getArgumentCount();
				    	if(len==0) {
				    		return 0;
				    	}
				    	double max = values[0];
				    	for(int it=0;it<len;++it) {
				    		if(values[it]>max) max=values[it];
				    	}
				    	return max;
				 }
			};
			funct_is_eq = new CustomOperator("=", true, 0, 2) {
				@Override
				protected double applyOperation(double[] arg0) {
					//System.out.println("OPERATION [=] argument[0] = {"+((Object)arg0[0]).toString()+"}");
					//System.out.println("OPERATION [=] argument[1] = {"+((Object)arg0[1]).toString()+"}");
					
					if(DoubleComparator.equals(arg0[0], arg0[1])) {
						return 0;
					}
					return Math.signum(arg0[0]-arg0[1]);
				}
			};
					
					
		} catch (InvalidCustomFunctionException e1) {
			e1.printStackTrace();
		}
	}

}
