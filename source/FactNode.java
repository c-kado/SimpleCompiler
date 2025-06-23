package enshud.s4.compiler;

import java.util.Arrays;
import java.util.List;

public class FactNode extends Node {
	private String factType;

	public FactNode(final Node node) {
		super(node);
	}

	@Override
	public void accept(final Visitor visitor) throws EnshudSemanticErrorException {
		visitor.visit(this);
	}

	public void receiveType(final String factType) {
		this.factType = factType;
	}

	public void sendFactTypeToParent() throws EnshudSemanticErrorException {
		if(getParent().equalNodeName("TermNode")) {
			sendFactTypeToTermNode();
		} else {
			sendFactTypeToFactNode();
		}
	}

	private void sendFactTypeToTermNode() throws EnshudSemanticErrorException {
		if(!((TermNode) getParent()).receiveFactType(factType)) {
			final Visitor errVisitor = new ErrorVisitor();
			errVisitor.visit(this);
		}
	}

	private void sendFactTypeToFactNode() throws EnshudSemanticErrorException {
		if(!factType.equals("boolean")) {
			final Visitor errVisitor = new ErrorVisitor();
			errVisitor.visit(this);
		} else {
			((FactNode) getParent()).receiveType(factType);
		}
	}

	public List<String> genFactCode() {
		if(getChild(0).equalNodeName("FactNode")) {
			return Arrays.asList(
					"\tPOP\tGR1",
					"\tXOR\tGR1, =#FFFF",
					"\tPUSH\t0, GR1");
		} else if(!getChild(0).equalNodeName("VarNode")) {
			return Arrays.asList();
		} else if(factType.equals("charArray")){
			return Arrays.asList("\tPUSH\t0, GR2");
		} else {
			return Arrays.asList(
					"\tLD\tGR1, 0, GR2",
					"\tPUSH\t0, GR1");
		}
	}
}
