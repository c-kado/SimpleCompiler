package enshud.s4.compiler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class VarNode extends Node {
	private Variable variable;
	private String varScope;
	private static final int MAXCHILDRENNUM = 2;

	public VarNode(final Node node) {
		super(node);
	}

	@Override
	public void accept(final Visitor visitor) throws EnshudSemanticErrorException {
		visitor.visit(this);
	}

	public void receiveVarName(final String varName) throws EnshudSemanticErrorException {
		Node anc = getParent();
		while(!anc.equalNodeName("SubPrgDecNode") && !anc.equalNodeName("ProgramNode")) {
			anc = anc.getParent();
		}

		if(anc.getParent() != null && checkVarDecInSubprg(anc, varName)) {
			return;
		}

		anc = getRootNode();

		//グローバル変数の確認
		if(((ProgramNode) anc).isNameDec(varName)) {
			varScope = "GVAR";
			variable = ((ProgramNode) anc).getVar(varName);
		} else {
			//宣言されていない変数
			throw new EnshudSemanticErrorException(getChild(0).getTerminalLine());
		}
	}

	private boolean checkVarDecInSubprg(final Node anc, final String varName) {
		if(((SubPrgDecNode) anc).isVarDec(varName)) {
			varScope = "LVAR";
			variable = ((SubPrgDecNode) anc).getVar(varName);
			return true;
		} else if(((SubPrgDecNode) anc).isParamDec(varName)) {
			varScope = "PARAM";
			variable = ((SubPrgDecNode) anc).getParam(varName);
			return true;
		} else {
			return false;
		}
	}

	public void sendTypeToParent() throws EnshudSemanticErrorException {

		if(getParent().equalNodeName("AssignLeftNode")) {
			((AssignLeftNode) getParent()).receiveType(getVarType());
		} else if(getParent().equalNodeName("FactNode")) {
			((FactNode) getParent()).receiveType(getVarType());
		} else if(!((VarRowNode) getParent()).receiveVarType(getVarType())){
			final Visitor errVisitor = new ErrorVisitor();
			errVisitor.visit(this);
		}
	}

	public String getVarType() {
		if(!isArray()) {
			return variable.getType();
		} else if(getNumOfChildren() == MAXCHILDRENNUM) {	//添字がついてる → 添字つき変数（型：配列の型）
			return variable.getArrayType();
		} else {
			return variable.getArrayType() + "Array";
		}
	}

	public boolean isArray() {
		return variable.getType().equals("array");
	}

	public List<String> genVarCode() {
		final List<String> varCode = new ArrayList<String>();

		if(getNumOfChildren() == MAXCHILDRENNUM) {
			varCode.add(genVarWithSubscrCode());
		} else {
			varCode.addAll(genPureVarCode());
		}

		if(varScope.equals("GVAR")) {
			varCode.add("\tADDA\tGR2, =" + getGVarOrder());
		} else {
			varCode.add("\tADDA\tGR2, =" + getSubPrgVarOrder());
		}

		return varCode;
	}

	private int getGVarOrder() {
		return getRootNode().getVarOrder(variable.getName(), getNumOfChildren() != 1);
	}

	private int getSubPrgVarOrder() {
		Node anc = getParent();
		while(!anc.equalNodeName("SubPrgDecNode")) {
			anc = anc.getParent();
		}

		if(varScope.equals("LVAR")) {
			return ((SubPrgDecNode) anc).getVarOrder(variable.getName(), getNumOfChildren() != 1);
		} else {
			return ((SubPrgDecNode) anc).getParamOrder(variable.getName());
		}
	}

	private String genVarWithSubscrCode() {
		return "\tLAD\tGR2, " + varScope + ", GR2";
	}

	private List<String> genPureVarCode() {
		if(variable.getType().equals("array")) {	//配列型純変数
			return Arrays.asList("\tPUSH\t" + variable.getArraySize(), "\tLAD\tGR2, " + varScope);
		} else {
			return Arrays.asList("\tLAD\tGR2, " + varScope);
		}
	}
}
