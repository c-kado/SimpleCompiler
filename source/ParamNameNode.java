package enshud.s4.compiler;

public class ParamNameNode extends Node {

	public ParamNameNode(final Node node) {
		super(node);
	}

	@Override
	public void accept(final Visitor visitor) throws EnshudSemanticErrorException {
		visitor.visit(this);
	}

	public void checkDecName() throws EnshudSemanticErrorException {
		final int ANCNUM_OF_PARAMROWNODE = 2;
		final int ANCNUM_OF_SUBPHEADNODE = 4;

		if(((ParamNameRowNode) getParent()).isParamNameDec(getTerminalString()) ||
		   ((ParamRowNode) getAnc(ANCNUM_OF_PARAMROWNODE)).isParamNameDec(getTerminalString()) ||
		   ((SubPrgHeadNode) getAnc(ANCNUM_OF_SUBPHEADNODE)).isEqualProcName(getTerminalString())) {
			throw new EnshudSemanticErrorException(getTerminalLine());
		}
	}

	public void sendParamNameToParent() {
		((ParamNameRowNode) getParent()).receiveParamName(getTerminalString());
	}
}
