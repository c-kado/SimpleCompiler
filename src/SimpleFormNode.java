package enshud.s4.compiler;

public class SimpleFormNode extends Node {
	private String termType;

	public SimpleFormNode(final Node node) {
		super(node);
	}

	@Override
	public void accept(final Visitor visitor) throws EnshudSemanticErrorException {
		visitor.visit(this);
	}

	public boolean receiveAddOp(final String addOp) {
		return addOp.equals("or") && termType.equals("boolean") ||
				!addOp.equals("or") && termType.equals("integer");
	}

	public boolean receiveTermType(final String termType) {
		if(getNumOfChildren() % 2 == 0 && !termType.equals("integer")) {
			//符号あり → 続く項はinteger
			return false;
		} else if(this.termType == null) {
			//1つ目の項の型 → 型を登録
			this.termType = termType;
			return true;
		} else {
			return this.termType.equals(termType);
		}
	}

	public void sendSimFormTypeToParent() throws EnshudSemanticErrorException {
		if(!((FormulationNode) getParent()).receiveSimFormType(termType)) {
			final Visitor errVisitor = new ErrorVisitor();
			errVisitor.visit(this);
		}
	}
}
