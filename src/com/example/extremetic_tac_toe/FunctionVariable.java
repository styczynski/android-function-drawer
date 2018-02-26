package com.example.extremetic_tac_toe;

public class FunctionVariable extends Function {
	
	String name = "";
	double value = 0.0;
	double pbMinValue = 0.0;
	double pbMaxValue = 0.0;
	double pbStepValue = -1;
	boolean pbStepAuto = true;
	boolean pbIsPlayed = false;
	
	public FunctionVariable() {
		super();
		super.object_type = Function.ENTRY_TYPE_VARIABLE;
	}
	
	protected void updateState() {
		super.eqaution = name + " = " + String.format("%.10f", value).replaceAll(",","\\.");
	}
	
	public double getValue() {
		return value;
	}
	
	public void setValue(double arg0) {
		value = arg0;
		updateState();
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String arg0) {
		name = arg0;
		updateState();
	}
	
	public static FunctionVariable create(Function funct) {
		Function ret = FunctionTypeSelector.select(funct);
		return (FunctionVariable)ret;
	}

}
