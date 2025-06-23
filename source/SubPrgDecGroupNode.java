package enshud.s4.compiler;

public class SubPrgDecGroupNode extends Node {

	public SubPrgDecGroupNode(final Node node) {
		super(node);
	}

	@Override
	public void accept(final Visitor visitor) throws EnshudSemanticErrorException {
		visitor.visit(this);
	}

	public String genDecProcCode(final int procNo) {
		return "PROC" + procNo + "\tNOP";
	}
}
