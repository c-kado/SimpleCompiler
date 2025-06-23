package enshud.s4.compiler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class IfStatementNode extends Node {
	private int ifNum;

	public IfStatementNode(final Node node) {
		super(node);
	}

	@Override
	public void accept(final Visitor visitor) throws EnshudSemanticErrorException {
		visitor.visit(this);
	}

	public boolean receiveFormType(final String formType) {
		return formType.equals("boolean");
	}

	public List<String> genIfCode(final int ifNum){
		final int MAXCHILDRENNUM = 3;
		final List<String> ifCode = new ArrayList<String>();
		this.ifNum = ifNum;

		ifCode.add("\tPOP\tGR1");	//GR1 <- 式
		ifCode.add("\tCPA\tGR1, =#FFFF");	//is式true?
		if(getNumOfChildren() == MAXCHILDRENNUM) {
			ifCode.add("\tJZE\tELSE" + this.ifNum);		//elseあり
		} else {
			ifCode.add("\tJZE\tENDIF" + this.ifNum);	//elseなし
		}

		return ifCode;
	}

	public List<String> genElseCode(){
		return Arrays.asList(
				"\tJUMP\tENDIF" + ifNum,	//then複合文終了
				"ELSE" + ifNum + "\tNOP");	//else文開始
	}

	public String genEndIfCode(){
		return "ENDIF" + ifNum + "\tNOP";
	}
}
