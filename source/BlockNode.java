package enshud.s4.compiler;

public class BlockNode extends Node {

	public BlockNode(final Node node) {
		super(node);
	}

	@Override
	public void accept(final Visitor visitor) throws EnshudSemanticErrorException {
		visitor.visit(this);
	}

}
