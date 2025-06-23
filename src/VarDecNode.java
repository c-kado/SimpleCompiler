package enshud.s4.compiler;

public class VarDecNode extends Node {

	public VarDecNode(final Node node) {
		super(node);
	}

	@Override
	public void accept(final Visitor visitor) throws EnshudSemanticErrorException {
		visitor.visit(this);
	}
}
