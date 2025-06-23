package enshud.s4.compiler;

import java.util.ArrayList;
import java.util.List;

public class VarRowNode extends Node {
	private final List<String> varTypeRow = new ArrayList<String>();

	public VarRowNode(final Node node) {
		super(node);
	}

	@Override
	public void accept(final Visitor visitor) throws EnshudSemanticErrorException {
		visitor.visit(this);
	}

	public boolean receiveVarType(final String varType) throws EnshudSemanticErrorException {
		if(!varType.equals("integer") && !varType.equals("char") && !varType.equals("charArray")) {
			return false;
		} else {
			varTypeRow.add(varType);
			return true;
		}
	}

	public void sendVarTypeRowToParent() {
		((InStatementNode) getParent()).receiveVarTypeRow(varTypeRow);
	}

	public String genPushAdrCode() {
		return "\tPUSH\t0, GR2";
	}
}
