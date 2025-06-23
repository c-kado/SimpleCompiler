package enshud.s4.compiler;

import java.util.ArrayList;
import java.util.List;

public class SubPrgDecNode extends Node {
	private String procName;
	private List<Variable> paramRow = new ArrayList<Variable>();
	private List<Variable> varRow = new ArrayList<Variable>();

	public SubPrgDecNode(final Node node) {
		super(node);
	}

	@Override
	public void accept(final Visitor visitor) throws EnshudSemanticErrorException {
		visitor.visit(this);
	}

	public void receiveProcName(final String procName) {
		this.procName = procName;
	}

	public void receiveParamRow(final List<Variable> paramRow) {
		this.paramRow = paramRow;
	}

	public void receiveVarRow(final List<Variable> varRow) {
		this.varRow = varRow;
	}

	public List<Variable> getParams(){
		return paramRow;
	}

	public boolean isVarDec(final String varName) {
		for(final Variable var: varRow) {
			if(varName.equals(var.getName())){
				return true;
			}
		}
		return false;
	}

	public boolean isParamDec(final String varName) {
		for(final Variable param: paramRow) {
			if(varName.equals(param.getName())){
				return true;
			}
		}
		return false;
	}

	public boolean canDecVar(final String varName) {
		if(varName.equals(procName)) {
			return false;
		}
		for(final Variable param: paramRow) {
			if(varName.equals(param.getName())){
				return false;
			}
		}
		return true;
	}

	public Variable getVar(final String varName) {
		for(final Variable var: varRow) {
			if(varName.equals(var.getName())){
				return var;
			}
		}
		return null;
	}

	public Variable getParam(final String varName) {
		for(final Variable param: paramRow) {
			if(varName.equals(param.getName())){
				return param;
			}
		}
		return null;
	}

	public int getVarOrder(final String varName, final boolean isSubscr) {
		int varOrder = 0;
		int i = 0;
		while(!varRow.get(i).getName().equals(varName)) {
			if(varRow.get(i).getType().equals("array")) {
				varOrder += varRow.get(i).getArraySize();
			} else {
				varOrder++;
			}
			i++;
		}

		if(isSubscr) {
			varOrder -= varRow.get(i).getMinSubscr();
		}

		return varOrder;
	}

	public int getParamOrder(final String varName) {
		int i = 0;
		while(!paramRow.get(i).getName().equals(varName)) {
			i++;
		}
		return i;
	}

	public int getLVarSize() {
		int varSize = varRow.size();
		for(final Variable var: varRow) {
			if(var.getType().equals("array")) {
				varSize += var.getArraySize() - 1;
			}
		}

		return varSize;
	}

	public String genReturnCode() {
		return "\tRET";
	}
}
