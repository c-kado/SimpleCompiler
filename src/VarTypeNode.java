package enshud.s4.compiler;

public class VarTypeNode extends Node {

	public VarTypeNode(final Node node) {
		super(node);
	}

	@Override
	public void accept(final Visitor visitor) throws EnshudSemanticErrorException {
		visitor.visit(this);
	}

	public void receiveType(final String type) {
		((VarDecRowNode) getParent()).receiveVarType(type);
	}

	public void receiveType(final int minSubscr, final int maxSubscr, final String arrayType) {
		((VarDecRowNode) getParent()).receiveVarType("array", maxSubscr, minSubscr, arrayType);
	}
}
