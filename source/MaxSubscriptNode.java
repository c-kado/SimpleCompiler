package enshud.s4.compiler;

public class MaxSubscriptNode extends Node {

	public MaxSubscriptNode(final Node node) {
		super(node);
	}

	@Override
	public void accept(final Visitor visitor) throws EnshudSemanticErrorException {
		visitor.visit(this);
	}

	public void receiveIntNum(final int maxSubscript) {
		((ArrayTypeNode) getParent()).receiveMaxSubscr(maxSubscript);
	}
}
