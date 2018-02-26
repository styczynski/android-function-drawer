package com.example.extremetic_tac_toe;

public class FunctionTypeSelector {

	public static boolean isOnlyVarName(String str) {
		return (!str.contains("*"))&&(!str.contains("-"))&&(!str.contains("+"))&&(!str.contains("^"))&&(!str.contains("/"))&&(!str.contains("\\"))&&(!str.contains("("))&&(!str.contains(")"))&&(!str.contains("{"))&&(!str.contains("}"))&&(!str.contains("["))&&(!str.contains("]"))&&(!str.contains("="))&&(!str.contains("@"))&&(!str.contains("!"))&&(!str.contains(" "))&&(!str.contains(","))&&(!str.contains("."))&&(!str.contains("?"))&&(!str.contains(";"))&&(!str.contains(":"))&&(!str.contains("\""))&&(!str.contains("'"))&&(!str.contains(">"))&&(!str.contains("<"));
	}
	
	public static Function select(Function function) {
		System.out.println("Selector runned with eq {"+function.eqaution+"} ");
		if(function.eqaution.contains("=")) {
			String[] split = function.eqaution.split("=");
			
			if(!(split.length>1)) return function;
			split[0]=split[0].trim();
			if(!isOnlyVarName(split[0])||split[0].equals("x")||split[0].equals("y")||split[1].equals("x")||split[1].equals("y")) {
				function.object_type = Function.ENTRY_TYPE_CURVE_OBJECT;
				return function;
			}
			
			FunctionVariable fvar = new FunctionVariable();
			fvar.name = split[0].trim();
			fvar.value = Double.parseDouble(split[1].trim());
			fvar.pbMaxValue = fvar.value * 1.85;
			fvar.pbMinValue = fvar.value * 0.85;
			
			fvar.updateState();
			fvar.object_type = Function.ENTRY_TYPE_VARIABLE;
			System.out.println("Selector returned variable.");
			return fvar;
		}
		
		System.out.println("Selector returned function.");
		function.object_type = Function.ENTRY_TYPE_FUNCTION;
		return function;
	}

}
