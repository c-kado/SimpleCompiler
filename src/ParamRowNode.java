package enshud.s4.compiler;

import java.util.ArrayList;
import java.util.List;

public class ParamRowNode extends Node {
	private List<String> paramNameRow = new ArrayList<String>();
	private final List<Variable> paramRow = new ArrayList<Variable>();

	public ParamRowNode(final Node node) {
		super(node);
	}

	@Override
	public void accept(final Visitor visitor) throws EnshudSemanticErrorException {
		visitor.visit(this);
	}

	public boolean isParamNameDec(final String paramName) {
		for(final Variable param: paramRow) {
			if(paramName.equals(param.getName())) {
				return true;
			}
		}
		return false;
	}

	public void receiveParamNameRow(final List<String> paramNameRow) {
		this.paramNameRow = paramNameRow;
	}

	public void receiveParamType(final String type){
		for(final String paramName: paramNameRow) {
			paramRow.add(new Variable(paramName, type));
		}
	}

	public void sendParamRowToParent() {
		((ParamNode) getParent()).receiveParamRow(paramRow);
	}
}
