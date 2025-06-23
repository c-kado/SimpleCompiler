package enshud.s4.compiler;

import java.util.ArrayList;
import java.util.List;

public class OutStatementNode extends Node {
	List<String> formTypeRow = new ArrayList<String>();

	public OutStatementNode(final Node node) {
		super(node);
	}

	@Override
	public void accept(final Visitor visitor) throws EnshudSemanticErrorException {
		visitor.visit(this);
	}

	public void receiveFormTypeRow(final List<String> formTypeRow) {
		this.formTypeRow = formTypeRow;
	}

	public List<String> genOutCode() {
		final List<String> outCode = new ArrayList<String>();

		for(final String formType: formTypeRow) {
			outCode.add("\tPOP\tGR2");
			switch(formType) {
				case "integer":
					outCode.add("\tCALL\tWRTINT");
					break;
				case "char":
					outCode.add("\tCALL\tWRTCH");
					break;
				default:
					outCode.add("\tPOP\tGR1");
					outCode.add("\tCALL\tWRTSTR");
					break;
			}
		}
		outCode.add("\tCALL\tWRTLN");
		return outCode;
	}
}
