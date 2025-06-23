package enshud.s4.compiler;

import java.util.ArrayList;
import java.util.List;

public class CallStatementNode extends Node {
	private List<Variable> params = new ArrayList<Variable>();
	private int procNo;
	private int lVarSize;
	private static String stCode = "\tST\tGR1, 0, GR2";
	private static String ldCode = "\tLD\tGR1, 0, GR2";
	private static String popCode = "\tPOP\tGR1";
	private static String pushCode = "\tPUSH\t0, GR1";

	public CallStatementNode(final Node node) {
		super(node);
	}

	@Override
	public void accept(final Visitor visitor) throws EnshudSemanticErrorException {
		visitor.visit(this);
	}

	public void receiveParams(final List<Variable> params) {
		this.params = params;
	}

	public boolean checkParamsType(final List<String> paramTypes) {
		for(int i = 0; i < params.size(); i++) {
			if(!paramTypes.get(i).equals(params.get(i).getType())) {
				return false;
			}
		}

		return true;
	}

	public void receiveProcNo(final int procNo) {
		this.procNo  = procNo;
	}

	public boolean receiveFormCount(final int formCount) {
		return params.size() == formCount;
	}

	public void receiveLVarSize(final int lVarSize) {
		this.lVarSize = lVarSize;
	}

	public List<String> genVarSaveCode() {
		final List<String> varSaveCode = new ArrayList<String>();

		if(lVarSize > 0) {
			//varSaveCode.addAll(genLVarSaveCode(lVarSize));
			varSaveCode.addAll(genVarSaveCode("LVAR", lVarSize));
		}

		if(params.size() > 0) {
			varSaveCode.addAll(genVarSaveCode("PARAM", params.size()));
		}

		return varSaveCode;
	}

	private List<String> genVarSaveCode(final String varStr, final int varNum){
		final List<String> varSaveCode = new ArrayList<String>();
		varSaveCode.add("\tLAD\tGR2, " + varStr);
		for(int i = 0; i < varNum - 1; i++) {
			varSaveCode.add(ldCode);
			varSaveCode.add(pushCode);
			varSaveCode.add("\tADDA\tGR2, =1");
		}
		varSaveCode.add(ldCode);
		varSaveCode.add(pushCode);

		return varSaveCode;
	}

	public List<String> genCallPrcCode() {
		final List<String> callPrcCode = new ArrayList<String>();

		if(params.size() != 0) {
			callPrcCode.add("\tLAD\tGR2, PARAM");
			for(int i = 0; i < params.size() - 1; i++) {
				callPrcCode.add(popCode);
				callPrcCode.add(stCode);
				callPrcCode.add("\tADDA\tGR2, =1");
			}
			callPrcCode.add(popCode);
			callPrcCode.add(stCode);
		}
		callPrcCode.add("\tCALL\tPROC" + procNo);

		return callPrcCode;
	}

	public List<String> genVarLdCode() {
		final List<String> varLdCode = new ArrayList<String>();

		if(params.size() > 0) {
			varLdCode.addAll(genVarLdCode("PARAM", params.size()));
		}

		if(lVarSize > 0) {
			varLdCode.addAll(genVarLdCode("LVAR", lVarSize));
		}

		return varLdCode;
	}

	private List<String> genVarLdCode(final String varStr, final int varNum){
		final List<String> varLdCode = new ArrayList<String>();
		varLdCode.add("\tLAD\tGR2, " + varStr);
		varLdCode.add("\tADDA\tGR2, =" + (varNum - 1));
		for(int i = 0; i < varNum - 1; i++) {
			varLdCode.add(popCode);
			varLdCode.add(stCode);
			varLdCode.add("\tSUBA\tGR2, =1");
		}
		varLdCode.add(popCode);
		varLdCode.add(stCode);

		return varLdCode;
	}
}
