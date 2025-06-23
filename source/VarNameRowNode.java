package enshud.s4.compiler;

import java.util.ArrayList;
import java.util.List;

public class VarNameRowNode extends Node {
	private final List<String> varNameRow = new ArrayList<String>();

	public VarNameRowNode(final Node node) {
		super(node);
	}

	@Override
	public void accept(final Visitor visitor) throws EnshudSemanticErrorException {
		visitor.visit(this);
	}

	public void receiveVarName(final String varName) {
		varNameRow.add(varName);
	}

	public void sendVarNamesToParent() {
		((VarDecRowNode) getParent()).receiveVarNameRow(varNameRow);
	}

	public boolean canDecVar(final String varName) {
		return !varNameRow.contains(varName);
	}
}
