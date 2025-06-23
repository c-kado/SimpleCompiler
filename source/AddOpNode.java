package enshud.s4.compiler;

import java.util.ArrayList;
import java.util.List;

public class AddOpNode extends Node {

	public AddOpNode(final Node node) {
		super(node);
	}

	@Override
	public void accept(final Visitor visitor) throws EnshudSemanticErrorException {
		visitor.visit(this);
	}

	public void sendOpToParent() throws EnshudSemanticErrorException {
		if(!((SimpleFormNode) getParent()).receiveAddOp(getTerminalString())) {
			throw new EnshudSemanticErrorException(getTerminalLine());
		}
	}

	public List<String> genAddOpCode(){
		final List<String> addOpCode = new ArrayList<String>();

		addOpCode.add("\tPOP\tGR2");
		addOpCode.add("\tPOP\tGR1");
		switch(getTerminalString()) {
			case  "+": addOpCode.add("\tADDA\tGR1, GR2");
					   break;
			case  "-": addOpCode.add("\tSUBA\tGR1, GR2");
					   break;
			default  : addOpCode.add("\tOR\tGR1, GR2");
					   break;
		}
		addOpCode.add("\tPUSH\t0, GR1");

		return addOpCode;
	}
}
