package enshud.s4.compiler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProgramNode extends Node {
	private final List<Variable> variables = new ArrayList<Variable>();
	private final List<String> procNames = new ArrayList<String>();

	public ProgramNode() {
		super();
	}

	public ProgramNode(final Node node) {
		super(node);
	}

	@Override
	public void accept(final Visitor visitor) throws EnshudSemanticErrorException {
		visitor.visit(this);
	}

	public void receiveVarRow(final List<Variable> varRow) {
		variables.addAll(varRow);
	}

	public boolean isNameDec(final String name) {
		for(final Variable variable: variables) {
			if(name.equals(variable.getName())) {
				return true;
			}
		}
		for(final String procName: procNames) {
			if(name.equals(procName)) {
				return true;
			}
		}
		return false;
	}

	public void receiveProcName(final String procName) {
		procNames.add(procName);
	}

	public boolean isProcNameDec(final String procName) {
		return procNames.contains(procName);
	}

	public int whichSubPrg(final String procName) {
		return procNames.indexOf(procName);
	}

	public Variable getVar(final String varName) {
		for(final Variable param: variables) {
			if(varName.equals(param.getName())){
				return param;
			}
		}
		return null;
	}

	public List<String> genStartCode() {
		return Arrays.asList(
				"CASL\tSTART\tBEGIN",
				"BEGIN\tLAD\tGR6, 0",
				"\tLAD\tGR7, LIBBUF");
	}

	public String genReturnCode() {
		return "\tRET";
	}

	public List<String> genEndCode(final List<String> strConsts, final int lVarSize, final int paramSize) {
		final List<String> endCode = new ArrayList<String>();

		if(variables.size() != 0) {
			endCode.add("GVAR\tDS\t" + calcGVarSize());
		}
		if(lVarSize != 0) {
			endCode.add("LVAR\tDS\t" + lVarSize);
		}
		if(paramSize != 0) {
			endCode.add("PARAM\tDS\t" + paramSize);
		}


		for(int i = 0; i < strConsts.size(); i++) {
			endCode.add("STR" + i + "\tDC\t" + strConsts.get(i));
		}
		endCode.add("LIBBUF\tDS\t256");
		endCode.add("\tEND");

		return endCode;
	}

	public int getVarOrder(final String varName, final boolean isSubscr) {
		int varOrder = 0;
		int i = 0;
		while(!variables.get(i).getName().equals(varName)) {
			if(variables.get(i).getType().equals("array")) {
				varOrder += variables.get(i).getArraySize();
			} else {
				varOrder++;
			}
			i++;
		}

		if(isSubscr) {
			varOrder -= variables.get(i).getMinSubscr();
		}

		return varOrder;
	}

	public int getProcNum() {
		return procNames.size();
	}

	private int calcGVarSize() {
		int gVarSize = 0;
		for(final Variable var: variables) {
			if(var.getType().equals("array")) {
				gVarSize += var.getArraySize();
			} else {
				gVarSize++;
			}
		}
		return gVarSize;
	}
}
