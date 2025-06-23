package enshud.s4.compiler;

import java.util.Arrays;
import java.util.List;

public class WhileStatementNode extends Node {
	private int whileNum;

	public WhileStatementNode(final Node node) {
		super(node);
	}

	@Override
	public void accept(final Visitor visitor) throws EnshudSemanticErrorException {
		visitor.visit(this);
	}

	public boolean receiveFormType(final String formType) {
		return formType.equals("boolean");
	}

	public String genLoopLabel(final int whileNum){
		this.whileNum = whileNum;
		return "LOOP" + this.whileNum + "\tNOP";
	}

	public List<String> genBranchCode(){
		return Arrays.asList(
				"\tPOP\tGR1",	//GR1 <- 式
				"\tCPA\tGR1, =#FFFF",	//is式true?
				"\tJZE\tENDLP" + whileNum);
	}

	public List<String> genEndLoopCode(){
		return Arrays.asList(
				"\tJUMP\tLOOP" + whileNum,
				"ENDLP" + whileNum + "\tNOP");
	}
}
