package enshud.s4.compiler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RelationOpNode extends Node {

	private int branchNum;

	public RelationOpNode(final Node node) {
		super(node);
	}

	@Override
	public void accept(final Visitor visitor) throws EnshudSemanticErrorException {
		visitor.visit(this);
	}

	public void sendOpToParent() throws EnshudSemanticErrorException {
		if(!((FormulationNode) getParent()).receiveRelationOp()) {
			throw new EnshudSemanticErrorException(getTerminalLine());
		}
	}

	public List<String> genRltOpCode(final int branchNum){
		final List<String> rltOpCode = new ArrayList<String>();
		this.branchNum = branchNum;

		rltOpCode.add("\tPOP\tGR2");
		rltOpCode.add("\tPOP\tGR1");
		rltOpCode.add("\tCPA\tGR1, GR2");
		rltOpCode.addAll(genBranchCode());

		return rltOpCode;
	}

	public List<String> genBranchCode(){
		switch(getTerminalString()){
			case  "=": return genJFBranchCode("JNZ");
			case "<=": return genJFBranchCode("JPL");
			case ">=": return genJFBranchCode("JMI");
			case "<>": return genJTBranchCode("JNZ");
			case  "<": return genJTBranchCode("JMI");
			case  ">": return genJTBranchCode("JPL");
			default: return null;
		}
	}

	private List<String> genJFBranchCode(final String jwhich) {
		return Arrays.asList(
				"\t" + jwhich + "\tFALSE" + branchNum,
				"\tLD\tGR1, =#0000",
				"\tJUMP\tBOTH" + branchNum,
				"FALSE" + branchNum + "\tLD\tGR1, =#FFFF",
				"BOTH" + branchNum + "\tPUSH\t0, GR1");
	}

	private List<String> genJTBranchCode(final String jwhich) {
		return Arrays.asList(
				"\t" + jwhich + "\tTRUE" + branchNum,
				"\tLD\tGR1, =#FFFF",
				"\tJUMP\tBOTH" + branchNum,
				"TRUE" + branchNum + "\tLD\tGR1, =#0000",
				"BOTH" + branchNum + "\tPUSH\t0, GR1");
	}
}
