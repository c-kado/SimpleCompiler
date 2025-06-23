package enshud.s4.compiler;

public class IntegralNumNode extends Node {
	public IntegralNumNode(final Node node) {
		super(node);
	}

	@Override
	public void accept(final Visitor visitor) throws EnshudSemanticErrorException {
		visitor.visit(this);
	}

	public void receiveSign(final String sign) {
		int integralNum = Integer.parseInt(getTerminalString());
		if(sign.equals("-")) {
			integralNum *= -1;
		}
		if(getParent().equalNodeName("MaxSubscriptNode")) {
			((MaxSubscriptNode) getParent()).receiveIntNum(integralNum);
		} else {
			((MinSubscriptNode) getParent()).receiveIntNum(integralNum);
		}
	}
}
