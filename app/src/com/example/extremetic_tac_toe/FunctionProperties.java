package com.example.extremetic_tac_toe;

public class FunctionProperties {

	public int monotonicity = Function.FUNCTION_TYPE_UNKNOWN;
	public boolean isInjection = false;
	public int parity = Function.FUNCTION_TYPE_UNKNOWN;
	public double[] roots;
	public double[] fixedPoints;
	
	public FunctionProperties() {
		
	}
	
	public FunctionProperties(Function funct) {
		monotonicity = funct.checkMonotonicity();
		isInjection = funct.checkIsInjective();
		parity = funct.checkParity();
		roots = funct.findFunctionRoots();
		fixedPoints = funct.findFunctionFixedPoints();
	}

}
