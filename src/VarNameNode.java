package enshud.s4.compiler;

public class VarNameNode extends Node {
	private static final int ANCNUM_OF_VARDECRORNODE = 2;
	private static final int ANCNUM_OF_SUBPRGDECNODE = 4;

	public VarNameNode(final Node node) {
		super(node);
	}

	@Override
	public void accept(final Visitor visitor) throws EnshudSemanticErrorException{
		visitor.visit(this);
	}

	public void sendVarNameToParent() throws EnshudSemanticErrorException {
		if(getParent().equalNodeName("VarNameRowNode")) {
			((VarNameRowNode) getParent()).receiveVarName(getTerminalString());
		} else {
			((VarNode) getParent()).receiveVarName(getTerminalString());
		}
	}

	public void canDecVar()throws EnshudSemanticErrorException {
		if(!((VarNameRowNode) getParent()).canDecVar(getTerminalString())
			|| !((VarDecRowNode) getAnc(ANCNUM_OF_VARDECRORNODE)).canDecVar(getTerminalString())) {
			throw new EnshudSemanticErrorException(getTerminalLine());
		}

		if(getAnc(ANCNUM_OF_SUBPRGDECNODE).equalNodeName("SubPrgDecNode")
			&& !((SubPrgDecNode) getAnc(ANCNUM_OF_SUBPRGDECNODE)).canDecVar(getTerminalString())) {
			//副プログラム宣言内の変数宣言
			throw new EnshudSemanticErrorException(getTerminalLine());
		}
	}
}
