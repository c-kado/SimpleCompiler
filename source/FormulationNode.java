package enshud.s4.compiler;

public class FormulationNode extends Node {
	private String simFormType;
	private String formType;

	public FormulationNode(final Node node) {
		super(node);
	}

	@Override
	public void accept(final Visitor visitor) throws EnshudSemanticErrorException {
		visitor.visit(this);
	}

	public boolean receiveRelationOp() {
		if(simFormType.equals("integer") || simFormType.equals("char") || simFormType.equals("boolean")) {
			formType = "boolean";
			return true;
		} else {
			return false;
		}
	}

	public boolean receiveSimFormType(final String simFormType) {
		if(this.simFormType == null) {
			//1つ目の因子の型 → 型を登録
			this.simFormType = simFormType;
			formType = simFormType;
			return true;
		} else {
			//２個目以降の登録は，既にoperationが登録されており，stdTypeかの判定も終わっている
			return this.simFormType.equals(simFormType);
		}
	}

	public void sendFormTypeToParent() throws EnshudSemanticErrorException {

		if(getParent().equalNodeName("FormulationRowNode")) {
			sendFormTypeToFormRowNode();
		} else if(getParent().equalNodeName("FactNode")){
			sendFormTypeToFactNode();
		} else if(getParent().equalNodeName("SubscriptNode")){
			sendFormTypeToSubscrNode();
		} else if(getParent().equalNodeName("IfStatementNode")){
			sendFormTypeToIfStatNode();
		} else if(getParent().equalNodeName("WhileStatementNode")){
			sendFormTypeToWhileStatNode();
		} else {
			sendFormTypeToAssignNode();
		}
	}

	private void sendFormTypeToIfStatNode() throws EnshudSemanticErrorException {
		if(!((IfStatementNode) getParent()).receiveFormType(formType)) {
			final Visitor errVisitor = new ErrorVisitor();
			errVisitor.visit(this);
		}
	}

	private void sendFormTypeToWhileStatNode() throws EnshudSemanticErrorException {
		if(!((WhileStatementNode) getParent()).receiveFormType(formType)) {
			final Visitor errVisitor = new ErrorVisitor();
			errVisitor.visit(this);
		}
	}

	private void sendFormTypeToAssignNode() throws EnshudSemanticErrorException {
		if(!((AssignNode) getParent()).receiveFormType(formType)) {
			final Visitor errVisitor = new ErrorVisitor();
			errVisitor.visit(this);
		}
	}

	private void sendFormTypeToFormRowNode() {
		((FormulationRowNode) getParent()).receiveFormType(formType);
	}

	private void sendFormTypeToFactNode() {
		((FactNode) getParent()).receiveType(formType);
	}

	private void sendFormTypeToSubscrNode() throws EnshudSemanticErrorException {
		if(!((SubscriptNode) getParent()).receiveFormType(formType)) {
			final Visitor errVisitor = new ErrorVisitor();
			errVisitor.visit(this);
		}
	}
}
