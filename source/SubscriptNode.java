package enshud.s4.compiler;

public class SubscriptNode extends Node {

	public SubscriptNode(final Node node) {
		super(node);
	}

	@Override
	public void accept(final Visitor visitor) throws EnshudSemanticErrorException {
		visitor.visit(this);
	}

	public boolean receiveFormType(final String formType) {
		return formType.equals("integer");
	}

	public void checkArray() throws EnshudSemanticErrorException {
		if(!((VarNode) getParent()).isArray()) {
			final Visitor errVisitor = new ErrorVisitor();
			errVisitor.visit(this);
		}
	}

	public String genSubscrCode() {
		return "\tPOP\tGR2";
	}
}
