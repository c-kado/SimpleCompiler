package enshud.s4.compiler;

public class BaseStatementNode extends Node {

	public BaseStatementNode(final Node node) {
		super(node);
	}

	@Override
	public void accept(final Visitor visitor) throws EnshudSemanticErrorException {
		visitor.visit(this);
	}

}
