package enshud.s4.compiler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MultiOpNode extends Node {

	public MultiOpNode(final Node node) {
		super(node);
	}

	@Override
	public void accept(final Visitor visitor) throws EnshudSemanticErrorException {
		visitor.visit(this);
	}

	public void sendOpToParent() throws EnshudSemanticErrorException {
		if(!((TermNode) getParent()).receiveMultiOp(getTerminalString())) {
			throw new EnshudSemanticErrorException(getTerminalLine());
		}
	}

	public List<String> genMultiOpCode(){
		final List<String> multiOpCode = new ArrayList<String>();

		multiOpCode.add("\tPOP\tGR2");
		multiOpCode.add("\tPOP\tGR1");
		switch(getTerminalString()) {
			case   "*":
				multiOpCode.addAll(Arrays.asList("\tCALL\tMULT", "\tPUSH\t0, GR2"));
				return multiOpCode;
			case   "/":
			case "div":
				multiOpCode.addAll(Arrays.asList("\tCALL\tDIV", "\tPUSH\t0, GR2"));
				return multiOpCode;
			case "mod":
				multiOpCode.addAll(Arrays.asList("\tCALL\tDIV", "\tPUSH\t0, GR1"));
				return multiOpCode;
			default:
				multiOpCode.addAll(Arrays.asList("\tAND\tGR1, GR2", "\tPUSH\t0, GR1"));
				return multiOpCode;
		}
	}
}
