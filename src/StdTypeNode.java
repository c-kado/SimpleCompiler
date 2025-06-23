package enshud.s4.compiler;

public class StdTypeNode extends Node {

	public StdTypeNode(final Node node) {
		super(node);
	}

	@Override
	public void accept(final Visitor visitor) throws EnshudSemanticErrorException {
		visitor.visit(this);
	}

	public void sendTypeToParent() {
		if(getParent().equalNodeName("VarTypeNode")) {
			((VarTypeNode) getParent()).receiveType(getTerminalString());
		} else if(getParent().equalNodeName("ArrayTypeNode")) {
			((ArrayTypeNode) getParent()).receiveType(getTerminalString());
		} else {
			((ParamRowNode) getParent()).receiveParamType(getTerminalString());
		}
	}
}
