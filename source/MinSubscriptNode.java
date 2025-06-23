package enshud.s4.compiler;

public class MinSubscriptNode extends Node {

	public MinSubscriptNode(final Node node) {
		super(node);
	}

	@Override
	public void accept(final Visitor visitor) throws EnshudSemanticErrorException {
		visitor.visit(this);
	}

	public void receiveIntNum(final int minSubscript) {
		((ArrayTypeNode) getParent()).receiveMinSubscr(minSubscript);
	}
}
