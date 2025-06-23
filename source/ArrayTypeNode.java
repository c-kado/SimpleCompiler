package enshud.s4.compiler;

public class ArrayTypeNode extends Node {
	private int minSubscr;
	private int maxSubscr;
	private String type;

	public ArrayTypeNode(final Node node) {
		super(node);
	}

	@Override
	public void accept(final Visitor visitor) throws EnshudSemanticErrorException {
		visitor.visit(this);
	}

	public void receiveMinSubscr(final int minSubscr) {
		this.minSubscr = minSubscr;
	}

	public void receiveMaxSubscr(final int maxSubscr) {
		this.maxSubscr = maxSubscr;
	}

	public void receiveType(final String type) {
		this.type = type;
	}

	public void sendArrayInfoToParent() {
		((VarTypeNode) getParent()).receiveType(minSubscr, maxSubscr, type);
	}

	public void checkMinMax() throws EnshudSemanticErrorException {
		if(maxSubscr < minSubscr) {
			final Visitor errVisitor = new ErrorVisitor();
			errVisitor.visit(this);
		}
	}
}
