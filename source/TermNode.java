package enshud.s4.compiler;

public class TermNode extends Node {
	private String termType;

	public TermNode(final Node node) {
		super(node);
	}

	@Override
	public void accept(final Visitor visitor) throws EnshudSemanticErrorException {
		visitor.visit(this);
	}

	public boolean receiveMultiOp(final String multiOp) {
		return multiOp.equals("and") && termType.equals("boolean") ||
				!multiOp.equals("and") && termType.equals("integer");
	}

	public boolean receiveFactType(final String factType) {
		if(termType == null) {
			//1つ目の因子の型 → 型を登録
			this.termType = factType;
			return true;
		} else {
			return factType.equals(this.termType);
		}
	}

	public void sendTermTypeToParent() throws EnshudSemanticErrorException {
		if(!((SimpleFormNode) getParent()).receiveTermType(termType)) {
			final Visitor errVisitor = new ErrorVisitor();
			errVisitor.visit(this);
		}
	}
}
