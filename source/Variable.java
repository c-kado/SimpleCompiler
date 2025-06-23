package enshud.s4.compiler;

public class Variable {
	private final String name;
	private final String type;
	private final int maxSubscript;
	private final int minSubscript;
	private final String arrayType;

	public Variable(final String varName, final String varType, final int max, final int min, final String arrayType) {
		name = varName;
		type = varType;
		maxSubscript = max;
		minSubscript = min;
		this.arrayType = arrayType;
	}

	public Variable(final String varName, final String varType) {
		name = varName;
		type = varType;
		maxSubscript = 0;
		minSubscript = 0;
		arrayType = null;
	}

	public String getName() {
		return name;
	}

	public String getType() {
		return type;
	}

	public String getArrayType() {
		return arrayType;
	}

	public int getArraySize() {
		return maxSubscript - minSubscript + 1;
	}

	public int getMinSubscr() {
		return minSubscript;
	}
}
