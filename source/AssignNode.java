package enshud.s4.compiler;

import java.util.Arrays;
import java.util.List;

public class AssignNode extends Node {
	private String leftType;

	public AssignNode(final Node node) {
		super(node);
	}

	@Override
	public void accept(final Visitor visitor) throws EnshudSemanticErrorException {
		visitor.visit(this);
	}

	public void receiveLeftType(final String varType) {
		leftType = varType;
	}

	public boolean receiveFormType(final String formType) {
		return leftType.equals(formType);
	}

	public List<String> genAssignCode() {
		return Arrays.asList(
				"\tPOP\tGR1",
				"\tST\tGR1, 0, GR2");
	}
}
