package enshud.s4.compiler;

import java.util.ArrayList;
import java.util.List;

public class VarDecRowNode extends Node {
	private static final int ANCNUM_OF_BLCORSUBPDECNODE = 2;
	private static final int ANCNUM_OF_PRGORSUBPDECGRPNODE = 3;
	private List<String> varNameRow = new ArrayList<String>();
	private final List<Variable> varRow = new ArrayList<Variable>();

	public VarDecRowNode(final Node node) {
		super(node);
	}

	@Override
	public void accept(final Visitor visitor) throws EnshudSemanticErrorException {
		visitor.visit(this);
	}

	public void receiveVarNameRow(final List<String> varNameRow) {
		this.varNameRow = varNameRow;
	}

	public void receiveVarType(final String type, final int maxSubscript, final int minSubscript, final String arrayType){
		for(final String varName: varNameRow) {
			varRow.add(new Variable(varName, type, maxSubscript, minSubscript, arrayType));
		}
	}

	public void receiveVarType(final String type){
		for(final String varName: varNameRow) {
			varRow.add(new Variable(varName, type));
		}
	}

	public void sendVarRowToAnc() {
		if(getAnc(ANCNUM_OF_BLCORSUBPDECNODE).equalNodeName("SubPrgDecNode")) {
			((SubPrgDecNode) getAnc(ANCNUM_OF_BLCORSUBPDECNODE)).receiveVarRow(varRow);
		} else {
			((ProgramNode) getAnc(ANCNUM_OF_PRGORSUBPDECGRPNODE)).receiveVarRow(varRow);
		}
	}

	public boolean canDecVar(final String varName) {
		for(final Variable var:varRow) {
			if(varName.equals(var.getName())) {
				return false;
			}
		}
		return true;
	}
}
