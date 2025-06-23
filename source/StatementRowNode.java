package enshud.s4.compiler;

public class StatementRowNode extends Node {

	public StatementRowNode(final Node node) {
		super(node);
	}

	@Override
	public void accept(final Visitor visitor) throws EnshudSemanticErrorException {
		visitor.visit(this);
	}

}
