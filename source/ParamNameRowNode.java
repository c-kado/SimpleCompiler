package enshud.s4.compiler;

import java.util.ArrayList;
import java.util.List;

public class ParamNameRowNode extends Node {
	private final List<String> paramNameRow = new ArrayList<String>();

	public ParamNameRowNode(final Node node) {
		super(node);
	}

	@Override
	public void accept(final Visitor visitor) throws EnshudSemanticErrorException {
		visitor.visit(this);
	}

	public void receiveParamName(final String paramName) {
		paramNameRow.add(paramName);
	}

	public void sendParamNameRowToParent() {
		((ParamRowNode) getParent()).receiveParamNameRow(paramNameRow);
	}

	public boolean isParamNameDec(final String paramName) {
		return paramNameRow.contains(paramName);
	}


}
