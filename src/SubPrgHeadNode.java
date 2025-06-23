package enshud.s4.compiler;

import java.util.ArrayList;
import java.util.List;

public class SubPrgHeadNode extends Node {
	private String procName;
	private List<Variable> paramRow = new ArrayList<Variable>();

	public SubPrgHeadNode(final Node node) {
		super(node);
	}

	@Override
	public void accept(final Visitor visitor) throws EnshudSemanticErrorException {
		visitor.visit(this);
	}

	public void receiveProcName(final String procName) {
		this.procName = procName;
	}

	public void receiveParamRow(final List<Variable> paramRow) {
		this.paramRow = paramRow;
	}

	public boolean isEqualProcName(final String paramName) {
		return paramName.equals(procName);
	}

	public void sendProcNameToParent() {
		((SubPrgDecNode) getParent()).receiveProcName(procName);
	}

	public void sendParamRowToParent() {
		((SubPrgDecNode) getParent()).receiveParamRow(paramRow);
	}
}
