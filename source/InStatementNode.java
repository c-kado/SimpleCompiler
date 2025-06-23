package enshud.s4.compiler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InStatementNode extends Node {
	List<String> varTypeRow = new ArrayList<String>();

	public InStatementNode(final Node node) {
		super(node);
	}

	@Override
	public void accept(final Visitor visitor) throws EnshudSemanticErrorException {
		visitor.visit(this);
	}

	public void receiveVarTypeRow(final List<String> varTypeRow) {
		this.varTypeRow = varTypeRow;
	}

	public List<String> genInCode() {
		if(varTypeRow.size() == 0) {
			return Arrays.asList("\tCALL\tRDLN");
		}

		final List<String> inCode = new ArrayList<String>();
		for(final String varType: varTypeRow) {
			inCode.add("\tPOP\tGR2");
			switch(varType) {
				case "integer":
					inCode.add("\tCALL\tRDINT");
					break;
				case "char":
					inCode.add("\tCALL\tRDCH");
					break;
				default:
					inCode.add("\tPOP\tGR1");
					inCode.add("\tCALL\tRDSTR");
					break;
			}
		}
		return inCode;
	}
}
