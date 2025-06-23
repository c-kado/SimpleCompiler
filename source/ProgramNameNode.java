package enshud.s4.compiler;

public class ProgramNameNode extends Node {

	public ProgramNameNode(final Node node) {
		super(node);
	}

	@Override
	public void accept(final Visitor visitor) throws EnshudSemanticErrorException {
		visitor.visit(this);
	}
}
