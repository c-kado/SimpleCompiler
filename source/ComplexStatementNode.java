package enshud.s4.compiler;

public class ComplexStatementNode extends Node {

	public ComplexStatementNode(final Node node) {
		super(node);
	}

	@Override
	public void accept(final Visitor visitor) throws EnshudSemanticErrorException {
		visitor.visit(this);
	}

}
