package enshud.s4.compiler;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class GenCasVisitor implements Visitor {

	private final List<String> casCode = new ArrayList<String>();
	private final List<String> compCasCode = new ArrayList<String>();
	private int ifNum = 0;
	private int whileNum = 0;
	private int tfBranchNum = 0;
	private int maxLVarSize = 0;
	private int maxParamSize = 0;
	private final List<String> strConsts = new ArrayList<String>();
	private final List<Boolean> callProcs = new ArrayList<Boolean>();

	public List<String> getCasCodeList(){
		return casCode;
	}

	private void setCallProcsList(final int procNum) {
		for(int i = 0; i < procNum; i++) {
			callProcs.add(false);
		}
	}

	public void addStrConst(final String strConst) {
		strConsts.add(strConst);
	}

	public int getStrConstsNum() {
		return strConsts.size();
	}

	@Override
	public void visit(final ProgramNode programNode) throws EnshudSemanticErrorException {

		final int CHILDORDER_OF_BLCORCOMP = 1;

		/*
		 * 1．最初の記述，レジスタの初期化を行う．
		 * 2．複合文の処理を行う．
		 * 3．ブロックの処理を行う．
		 * （手続きは使用するもののみ？）
		 * （変数はグローバル変数をVAR，ローカルは使用される手続き中最大サイズで領域確保）
		 * 4．文字列があれば，DC）
		 * 5．サブルーチン用のバフ確保）
		 * 6．END記述
		 * 7．lib.casを加える
		 */

		casCode.addAll(programNode.genStartCode());

		setCallProcsList(programNode.getProcNum());
		programNode.getLastChild().accept(this);

		casCode.addAll(compCasCode);
		compCasCode.clear();
		casCode.add(programNode.genReturnCode());

		if(programNode.getChild(CHILDORDER_OF_BLCORCOMP).getClass().getName().endsWith(".BlockNode")) {
			programNode.getChild(CHILDORDER_OF_BLCORCOMP).accept(this);
		}

		casCode.addAll(programNode.genEndCode(strConsts, maxLVarSize, maxParamSize));

		try {
			casCode.addAll(Files.readAllLines(Paths.get("data/cas/lib.cas")));
		} catch (IOException e) {
			System.err.println(e);
		}
	}

	@Override
	public void visit(final BlockNode blockNode) throws EnshudSemanticErrorException {
		if(blockNode.getNumOfChildren() != 0) {
			blockNode.getLastChild().accept(this);
		}
	}

	@Override
	public void visit(final SubPrgDecGroupNode subPrgDecGroupNode) throws EnshudSemanticErrorException {
		for(int i = subPrgDecGroupNode.getNumOfChildren() - 1; i >= 0; i--) {
			if(callProcs.get(i)) {
				casCode.add(subPrgDecGroupNode.genDecProcCode(i));
				subPrgDecGroupNode.getChild(i).accept(this);
			}
		}
	}

	@Override
	public void visit(final SubPrgDecNode subPrgDecNode) throws EnshudSemanticErrorException {
		subPrgDecNode.getLastChild().accept(this);
		casCode.addAll(compCasCode);
		compCasCode.clear();
		casCode.add(subPrgDecNode.genReturnCode());
		if(maxLVarSize < subPrgDecNode.getLVarSize()) {
			maxLVarSize = subPrgDecNode.getLVarSize();
		}
		if(maxParamSize < subPrgDecNode.getParams().size()) {
			maxParamSize = subPrgDecNode.getParams().size();
		}
	}

	@Override
	public void visit(final SignNode signNode) throws EnshudSemanticErrorException {
		compCasCode.addAll(signNode.genSignCode());
	}

	@Override
	public void visit(final ProcNameNode procNameNode) throws EnshudSemanticErrorException {
		callProcs.set(procNameNode.checkProcOrder(), true);
	}

	@Override
	public void visit(final ComplexStatementNode complexStatementNode) throws EnshudSemanticErrorException {
		complexStatementNode.getChild(0).accept(this);
	}

	@Override
	public void visit(final StatementRowNode statementRowNode) throws EnshudSemanticErrorException {
		for(int i = 0; i < statementRowNode.getNumOfChildren(); i++) {
			statementRowNode.getChild(i).accept(this);
		}
	}

	@Override
	public void visit(final StatementNode statementNode) throws EnshudSemanticErrorException {
		statementNode.getChild(0).accept(this);
	}

	@Override
	public void visit(final IfStatementNode ifStatementNode) throws EnshudSemanticErrorException {

		final int CHILDCOUNT_OF_IFELSE = 3;
		final int CHILDORDER_OF_THEN = 1;

		compCasCode.add(";\tif");
		ifStatementNode.getChild(0).accept(this);
		compCasCode.addAll(ifStatementNode.genIfCode(ifNum++));
		ifStatementNode.getChild(CHILDORDER_OF_THEN).accept(this);
		if(ifStatementNode.getNumOfChildren() == CHILDCOUNT_OF_IFELSE) {	//elseあり
			compCasCode.addAll(ifStatementNode.genElseCode());
			ifStatementNode.getLastChild().accept(this);
		}
		compCasCode.add(ifStatementNode.genEndIfCode());
	}

	@Override
	public void visit(final WhileStatementNode whileStatementNode) throws EnshudSemanticErrorException {
		compCasCode.add(";\twhile");
		compCasCode.add(whileStatementNode.genLoopLabel(whileNum++));
		whileStatementNode.getChild(0).accept(this);
		compCasCode.addAll(whileStatementNode.genBranchCode());
		whileStatementNode.getChild(1).accept(this);
		compCasCode.addAll(whileStatementNode.genEndLoopCode());
	}

	@Override
	public void visit(final BaseStatementNode baseStatementNode) throws EnshudSemanticErrorException {
		baseStatementNode.getChild(0).accept(this);
	}

	@Override
	public void visit(final AssignNode assignNode) throws EnshudSemanticErrorException {
		compCasCode.add(";\tassign");
		assignNode.getChild(1).accept(this);
		assignNode.getChild(0).accept(this);
		compCasCode.addAll(assignNode.genAssignCode());
	}

	@Override
	public void visit(final AssignLeftNode assignLeftNode) throws EnshudSemanticErrorException {
		assignLeftNode.getChild(0).accept(this);
	}

	@Override
	public void visit(final VarNode varNode) throws EnshudSemanticErrorException {
		varNode.getLastChild().accept(this);
		compCasCode.addAll(varNode.genVarCode());
	}

	@Override
	public void visit(final SubscriptNode subscriptNode) throws EnshudSemanticErrorException {
		subscriptNode.getChild(0).accept(this);
		compCasCode.add(subscriptNode.genSubscrCode());
	}

	@Override
	public void visit(final CallStatementNode callStatementNode) throws EnshudSemanticErrorException {

		final int MAXCHILDRENNUM = 2;

		compCasCode.add(";\tprocCall");
		callStatementNode.getChild(0).accept(this);
		compCasCode.addAll(callStatementNode.genVarSaveCode());
		if(callStatementNode.getNumOfChildren() == MAXCHILDRENNUM) {
			callStatementNode.getChild(MAXCHILDRENNUM - 1).accept(this);
		}
		compCasCode.addAll(callStatementNode.genCallPrcCode());
		compCasCode.addAll(callStatementNode.genVarLdCode());
	}

	@Override
	public void visit(final FormulationRowNode formulationRowNode) throws EnshudSemanticErrorException {
		for(int i = formulationRowNode.getNumOfChildren()-1; i >= 0; i--) {
			formulationRowNode.getChild(i).accept(this);
		}
	}

	@Override
	public void visit(final FormulationNode formulationNode) throws EnshudSemanticErrorException {
		final int CHILDORDER_OF_FORMNODE = 1;
		final int MINCHILDRENNUM = 1;

		formulationNode.getChild(0).accept(this);
		if(formulationNode.getNumOfChildren() != MINCHILDRENNUM) {
			formulationNode.getLastChild().accept(this);
			formulationNode.getChild(CHILDORDER_OF_FORMNODE).accept(this);
		}
	}

	@Override
	public void visit(final SimpleFormNode simpleFormNode) throws EnshudSemanticErrorException {
		int i = 1;
		if(simpleFormNode.getNumOfChildren() % 2 == 0) {
			//符号あり
			i++;
			simpleFormNode.getChild(1).accept(this);
		}
		simpleFormNode.getChild(0).accept(this);

		for(; i < simpleFormNode.getNumOfChildren(); i = i+2) {
			simpleFormNode.getChild(i+1).accept(this);
			simpleFormNode.getChild(i).accept(this);
		}
	}

	@Override
	public void visit(final TermNode termNode) throws EnshudSemanticErrorException {
		termNode.getChild(0).accept(this);

		for(int i = 1; i < termNode.getNumOfChildren(); i = i+2) {
			termNode.getChild(i+1).accept(this);
			termNode.getChild(i).accept(this);
		}
	}

	@Override
	public void visit(final FactNode factNode) throws EnshudSemanticErrorException {
		factNode.getChild(0).accept(this);
		compCasCode.addAll(factNode.genFactCode());
	}

	@Override
	public void visit(final RelationOpNode relationOpNode) throws EnshudSemanticErrorException {
		compCasCode.addAll(relationOpNode.genRltOpCode(tfBranchNum++));
	}

	@Override
	public void visit(final AddOpNode addOpNode) throws EnshudSemanticErrorException {
		compCasCode.addAll(addOpNode.genAddOpCode());
	}

	@Override
	public void visit(final MultiOpNode multiOpNode) throws EnshudSemanticErrorException {
		compCasCode.addAll(multiOpNode.genMultiOpCode());
	}

	@Override
	public void visit(final InStatementNode inStatementNode) throws EnshudSemanticErrorException {
		compCasCode.add(";\tinput");
		if(inStatementNode.getNumOfChildren() != 0) {
			inStatementNode.getChild(0).accept(this);
		}
		compCasCode.addAll(inStatementNode.genInCode());
	}

	@Override
	public void visit(final OutStatementNode outStatementNode) throws EnshudSemanticErrorException {
		compCasCode.add(";\toutput");
		if(outStatementNode.getNumOfChildren() != 0) {
			outStatementNode.getChild(0).accept(this);
		}
		compCasCode.addAll(outStatementNode.genOutCode());
	}

	@Override
	public void visit(final VarRowNode varRowNode) throws EnshudSemanticErrorException {
		for(int i = varRowNode.getNumOfChildren()-1; i >= 0; i--) {
			varRowNode.getChild(i).accept(this);
			compCasCode.add(varRowNode.genPushAdrCode());
		}
	}

	@Override
	public void visit(final ConstantNode constantNode) throws EnshudSemanticErrorException {
		compCasCode.addAll(constantNode.genConstCode(strConsts.size(), this));
	}

	@Override
	public void visit(final ArrayTypeNode arrayTypeNode) throws EnshudSemanticErrorException {}
	@Override
	public void visit(final IntegralNumNode integralNumNode) throws EnshudSemanticErrorException {}
	@Override
	public void visit(final MaxSubscriptNode maxSubscriptNode) throws EnshudSemanticErrorException {}
	@Override
	public void visit(final MinSubscriptNode minSubscriptNode) throws EnshudSemanticErrorException {}
	@Override
	public void visit(final ParamNameNode paramNameNode) throws EnshudSemanticErrorException {}
	@Override
	public void visit(final ParamNameRowNode paramNameRowNode) throws EnshudSemanticErrorException {}
	@Override
	public void visit(final ParamNode paramNode) throws EnshudSemanticErrorException {}
	@Override
	public void visit(final ParamRowNode paramRowNode) throws EnshudSemanticErrorException {}
	@Override
	public void visit(final ProgramNameNode programNameNode) throws EnshudSemanticErrorException {}
	@Override
	public void visit(final StdTypeNode stdTypeNode) throws EnshudSemanticErrorException {}
	@Override
	public void visit(final SubPrgHeadNode subPrgHeadNode) throws EnshudSemanticErrorException {}
	@Override
	public void visit(final VarDecNode varDecNode) throws EnshudSemanticErrorException {}
	@Override
	public void visit(final VarDecRowNode varDecRowNode) throws EnshudSemanticErrorException {}
	@Override
	public void visit(final VarNameNode varNameNode) throws EnshudSemanticErrorException {}
	@Override
	public void visit(final VarNameRowNode varNameRowNode) throws EnshudSemanticErrorException {}
	@Override
	public void visit(final VarTypeNode varTypeNode) throws EnshudSemanticErrorException {}
}
