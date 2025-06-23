package enshud.s4.compiler;

import java.util.ArrayList;
import java.util.List;

public class ParamNode extends Node {
	private final List<Variable> paramRow = new ArrayList<Variable>();

	public ParamNode(final Node node) {
		super(node);
	}

	@Override
	public void accept(final Visitor visitor) throws EnshudSemanticErrorException {
		visitor.visit(this);
	}

	public void receiveParamRow(final List<Variable> paramRow) {
		this.paramRow.addAll(paramRow);
	}

	public void sendParamRowToParent() {
		((SubPrgHeadNode) getParent()).receiveParamRow(paramRow);
	}
}
