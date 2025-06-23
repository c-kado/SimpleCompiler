package enshud.s4.compiler;

import java.util.ArrayList;
import java.util.List;

public class FormulationRowNode extends Node {
	private final List<String> formTypeRow = new ArrayList<String>();

	public FormulationRowNode(final Node node) {
		super(node);
	}

	@Override
	public void accept(final Visitor visitor) throws EnshudSemanticErrorException {
		visitor.visit(this);
	}

	public void receiveFormType(final String formType) {
		formTypeRow.add(formType);
	}

	public void checkFormsType() throws EnshudSemanticErrorException {
		if(getParent().equalNodeName("CallStatementNode")) {
			checkFormTypeInCallStat();
		} else {
			checkFormTypeInOutStat();
		}
	}

	private void checkFormTypeInCallStat() {
		((CallStatementNode) getParent()).checkParamsType(formTypeRow);
	}

	private void checkFormTypeInOutStat() throws EnshudSemanticErrorException {
		for(final String formType: formTypeRow) {
			if(!formType.equals("integer") && !formType.equals("char") && !formType.equals("charArray")) {
				final Visitor errVisitor = new ErrorVisitor();
				errVisitor.visit(this);
			}
		}
	}

	public void sendFormTypeRowToParent() {
		if(getParent().equalNodeName("OutStatementNode")) {
			((OutStatementNode) getParent()).receiveFormTypeRow(formTypeRow);
		}
	}

	public void sendFormCountToParent() throws EnshudSemanticErrorException {
		if(getParent().equalNodeName("CallStatementNode") && !((CallStatementNode) getParent()).receiveFormCount(getNumOfChildren())) {
			final Visitor errVisitor = new ErrorVisitor();
			errVisitor.visit(this);
		}
	}
}
