package enshud.s4.compiler;

public class ProcNameNode extends Node {

	private int subPrgNo;

	public ProcNameNode(final Node node) {
		super(node);
	}

	@Override
	public void accept(final Visitor visitor) throws EnshudSemanticErrorException {
		visitor.visit(this);
	}

	public void checkDecName() throws EnshudSemanticErrorException {
		if(!getRootNode().isProcNameDec(getTerminalString())) {
			throw new EnshudSemanticErrorException(getTerminalLine());
		}
	}

	public void canDecName() throws EnshudSemanticErrorException {
		if(getRootNode().isNameDec(getTerminalString())) {
			throw new EnshudSemanticErrorException(getTerminalLine());
		}
	}

	public void sendProcNameToAnc() {
		getRootNode().receiveProcName(getTerminalString());
		((SubPrgHeadNode) getParent()).receiveProcName(getTerminalString());
	}

	public void sendParamsToParent() {
		//何個めの副プログラムかを，program.procNamesのインデックスiで判定
		//副プロ宣言群のi番目のノードのparamRowを送る

		subPrgNo = getRootNode().whichSubPrg(getTerminalString());	//何番目の副プログラムなのか．
		((CallStatementNode) getParent()).receiveParams(
				((SubPrgDecNode) getRootNode().getChild(1).getLastChild().getChild(subPrgNo)).getParams());
	}

	public void sendLVarCountToParent() {
		((CallStatementNode) getParent()).receiveLVarSize(
				((SubPrgDecNode) getRootNode().getChild(1).getLastChild().getChild(subPrgNo)).getLVarSize());
	}

	public int checkProcOrder() {
		final int procNo = getRootNode().whichSubPrg(getTerminalString());
		((CallStatementNode)getParent()).receiveProcNo(procNo);
		return procNo;
	}
}
