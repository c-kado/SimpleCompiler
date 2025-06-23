package enshud.s4.compiler;

public class StatementNode extends Node {

	public StatementNode(final Node node) {
		super(node);
	}

	@Override
	public void accept(final Visitor visitor) throws EnshudSemanticErrorException {
		visitor.visit(this);
	}

}
