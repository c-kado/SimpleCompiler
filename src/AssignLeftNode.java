package enshud.s4.compiler;

public class AssignLeftNode extends Node {
	private String varType;

	public AssignLeftNode(final Node node) {
		super(node);
	}

	@Override
	public void accept(final Visitor visitor) throws EnshudSemanticErrorException {
		visitor.visit(this);
	}

	public void sendVarTypeToParent() {
		((AssignNode) getParent()).receiveLeftType(varType);
	}

	public void receiveType(final String varType) throws EnshudSemanticErrorException {
		if(varType.endsWith("Array")) {	//配列型の純変数
			final Visitor errVisitor = new ErrorVisitor();
			errVisitor.visit(this);
		} else {
			this.varType = varType;
		}
	}
}
