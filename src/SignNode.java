package enshud.s4.compiler;

import java.util.Arrays;
import java.util.List;

public class SignNode extends Node {

	public SignNode(final Node node) {
		super(node);
	}

	@Override
	public void accept(final Visitor visitor) throws EnshudSemanticErrorException {
		visitor.visit(this);
	}

	public void sendSignToParent() {
		if(getParent().equalNodeName("IntegralNumNode")) {
			((IntegralNumNode) getParent()).receiveSign(getTerminalString());
		}
	}

	public List<String> genSignCode(){
		if(getTerminalString().equals("-")) {
			return Arrays.asList(
					"\tPOP\tGR1",
					"\tXOR\tGR1, =#FFFF",
					"\tADDA\tGR1, =1",
					"\tPUSH\t0, GR1");
		} else {
			return Arrays.asList();
		}
	}
}
