package enshud.s4.compiler;

import java.util.Arrays;
import java.util.List;

public class ConstantNode extends Node {

	private static final int CHARCONST_STRINGSIZE = 3;
	private static final char FIRSTCH_OF_FALSE = 'f';
	private static final char FIRSTCH_OF_TRUE = 't';
	private static final char FIRSTCH_OF_STRING = '\'';

	public ConstantNode(final Node node) {
		super(node);
	}

	@Override
	public void accept(final Visitor visitor) throws EnshudSemanticErrorException {
		visitor.visit(this);
	}

	public void sendConstTypeToParent() {
		final String constStr = getTerminalString();

		if(constStr.charAt(0) == FIRSTCH_OF_FALSE || constStr.charAt(0) == FIRSTCH_OF_TRUE) {
			((FactNode) getParent()).receiveType("boolean");
		} else if(constStr.charAt(0) != FIRSTCH_OF_STRING) {
			((FactNode) getParent()).receiveType("integer");
		} else if(constStr.length() == CHARCONST_STRINGSIZE) {
			((FactNode) getParent()).receiveType("char");
		} else {
			((FactNode) getParent()).receiveType("charArray");
		}
	}

	public List<String> genConstCode(final int strConstNum, final GenCasVisitor visitor){
		final String constStr = getTerminalString();

		if(constStr.charAt(0) == FIRSTCH_OF_FALSE) {
			return Arrays.asList("\tPUSH\t#FFFF");
		} else if(constStr.charAt(0) == FIRSTCH_OF_TRUE) {
			return Arrays.asList("\tPUSH\t#0000");
		} else if(constStr.charAt(0) != FIRSTCH_OF_STRING) {		//整数
			return Arrays.asList("\tPUSH\t" + constStr);
		} else if(constStr.length() == CHARCONST_STRINGSIZE) {			//文字
			return Arrays.asList(
					"\tLD\tGR1, =" + constStr,
					"\tPUSH\t0, GR1");
		} else {	//文字列
			visitor.addStrConst(constStr);
			return Arrays.asList(
					"\tPUSH\t" + (constStr.length() - 2),
					"\tPUSH\tSTR" + (visitor.getStrConstsNum() - 1));
		}
	}
}
