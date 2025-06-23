package enshud.s4.compiler;

public class DecCheckVisitor implements Visitor {
	//変数宣言，副プログラム宣言をチェック

	@Override
	public void visit(final ProgramNode programNode) throws EnshudSemanticErrorException {
		for(int i = 0; i < programNode.getNumOfChildren()-1; i++) {
			//複合文は，別のvisitorでチェック
			//宣言部(変数宣言，副プログラム宣言)のみチェックする
			programNode.getChild(i).accept(this);
		}
	}

	@Override
	public void visit(final BlockNode blockNode) throws EnshudSemanticErrorException {
		for(int i = 0; i < blockNode.getNumOfChildren(); i++) {
			blockNode.getChild(i).accept(this);
		}
	}

	@Override
	public void visit(final VarDecNode varDecNode) throws EnshudSemanticErrorException {
		if(varDecNode.getNumOfChildren() != 0) {
			varDecNode.getChild(0).accept(this);
		}
	}

	@Override
	public void visit(final VarDecRowNode varDecRowNode) throws EnshudSemanticErrorException {
		for(int i = 0; i < varDecRowNode.getNumOfChildren(); i += 2) {
			varDecRowNode.getChild(i).accept(this);	//VarNameRowNode
			varDecRowNode.getChild(i+1).accept(this);		//VarTypeNode
		}
		varDecRowNode.sendVarRowToAnc();
	}

	@Override
	public void visit(final VarNameRowNode varNameRowNode) throws EnshudSemanticErrorException {
		for(int i = 0; i < varNameRowNode.getNumOfChildren(); i++) {
			varNameRowNode.getChild(i).accept(this);
		}
		varNameRowNode.sendVarNamesToParent();
	}

	@Override
	public void visit(final VarNameNode varNameNode) throws EnshudSemanticErrorException {
		varNameNode.canDecVar();
		varNameNode.sendVarNameToParent();
	}

	@Override
	public void visit(final VarTypeNode varTypeNode) throws EnshudSemanticErrorException {
		varTypeNode.getChild(0).accept(this);
	}

	@Override
	public void visit(final ArrayTypeNode arrayTypeNode) throws EnshudSemanticErrorException {
		for(int i = 0; i < arrayTypeNode.getNumOfChildren(); i++) {
			arrayTypeNode.getChild(i).accept(this);
		}
		arrayTypeNode.checkMinMax();
		arrayTypeNode.sendArrayInfoToParent();
	}

	@Override
	public void visit(final StdTypeNode stdTypeNode) throws EnshudSemanticErrorException {
		stdTypeNode.sendTypeToParent();
	}

	@Override
	public void visit(final MaxSubscriptNode maxSubscriptNode) throws EnshudSemanticErrorException {
		maxSubscriptNode.getChild(0).accept(this);
	}

	@Override
	public void visit(final MinSubscriptNode minSubscriptNode) throws EnshudSemanticErrorException {
		minSubscriptNode.getChild(0).accept(this);
	}

	@Override
	public void visit(final IntegralNumNode integralNumNode) throws EnshudSemanticErrorException {
		if(integralNumNode.getNumOfChildren() != 0) {
			integralNumNode.getChild(0).accept(this);
		} else {
			integralNumNode.receiveSign("+");
		}
	}

	@Override
	public void visit(final SignNode signNode) throws EnshudSemanticErrorException {
		signNode.sendSignToParent();
	}

	@Override
	public void visit(final SubPrgDecGroupNode subPrgDecGroupNode) throws EnshudSemanticErrorException {
		for(int i = 0; i < subPrgDecGroupNode.getNumOfChildren(); i++) {
			subPrgDecGroupNode.getChild(i).accept(this);
		}
	}

	@Override
	public void visit(final SubPrgDecNode subPrgDecNode) throws EnshudSemanticErrorException {
		subPrgDecNode.getChild(0).accept(this);	//副プログラム頭部
		subPrgDecNode.getChild(1).accept(this);	//変数宣言

		final Visitor compCheckVisitor = new CompCheckVisitor();
		subPrgDecNode.getChild(2).accept(compCheckVisitor);	//複合文
	}

	@Override
	public void visit(final SubPrgHeadNode subPrgHeadNode) throws EnshudSemanticErrorException {
		for(int i = 0; i < subPrgHeadNode.getNumOfChildren(); i++) {
			subPrgHeadNode.getChild(i).accept(this);
		}
		subPrgHeadNode.sendProcNameToParent();
		subPrgHeadNode.sendParamRowToParent();
	}

	@Override
	public void visit(final ProcNameNode procNameNode) throws EnshudSemanticErrorException {
		procNameNode.canDecName();
		procNameNode.sendProcNameToAnc();
	}

	@Override
	public void visit(final ParamNode paramNode) throws EnshudSemanticErrorException {
		paramNode.getChild(0).accept(this);
		paramNode.sendParamRowToParent();
	}

	@Override
	public void visit(final ParamRowNode paramRowNode) throws EnshudSemanticErrorException {
		for(int i = 0; i < paramRowNode.getNumOfChildren(); i += 2) {
			paramRowNode.getChild(i).accept(this);	//ParamNameRowNode
			paramRowNode.getChild(i+1).accept(this);		//StdTypeNode
		}
		paramRowNode.sendParamRowToParent();
	}

	@Override
	public void visit(final ParamNameRowNode paramNameRowNode) throws EnshudSemanticErrorException {
		for(int i = 0; i < paramNameRowNode.getNumOfChildren(); i++) {
			paramNameRowNode.getChild(i).accept(this);
		}
		paramNameRowNode.sendParamNameRowToParent();
	}

	@Override
	public void visit(final ParamNameNode paramNameNode) throws EnshudSemanticErrorException {
		paramNameNode.checkDecName();
		paramNameNode.sendParamNameToParent();
	}

	@Override
	public void visit(final AddOpNode addOpNode) throws EnshudSemanticErrorException {}
	@Override
	public void visit(final BaseStatementNode baseStatementNode) throws EnshudSemanticErrorException {}
	@Override
	public void visit(final CallStatementNode callStatementNode) throws EnshudSemanticErrorException {}
	@Override
	public void visit(final ComplexStatementNode complexStatementNode) throws EnshudSemanticErrorException {}
	@Override
	public void visit(final ConstantNode constantNode) throws EnshudSemanticErrorException {}
	@Override
	public void visit(final FactNode factNode) throws EnshudSemanticErrorException {}
	@Override
	public void visit(final FormulationNode formulationNode) throws EnshudSemanticErrorException {}
	@Override
	public void visit(final FormulationRowNode formulationRowNode) throws EnshudSemanticErrorException {}
	@Override
	public void visit(final IfStatementNode ifStatementNode) throws EnshudSemanticErrorException {}
	@Override
	public void visit(final InStatementNode inStatementNode) throws EnshudSemanticErrorException {}
	@Override
	public void visit(final MultiOpNode multiOpNode) throws EnshudSemanticErrorException {}
	@Override
	public void visit(final OutStatementNode outStatementNode) throws EnshudSemanticErrorException {}
	@Override
	public void visit(final ProgramNameNode programNameNode) throws EnshudSemanticErrorException {}
	@Override
	public void visit(final RelationOpNode relationOpNode) throws EnshudSemanticErrorException {}
	@Override
	public void visit(final SimpleFormNode simpleFormNode) throws EnshudSemanticErrorException {}
	@Override
	public void visit(final StatementNode statementNode) throws EnshudSemanticErrorException {}
	@Override
	public void visit(final StatementRowNode statementRowNode) throws EnshudSemanticErrorException {}
	@Override
	public void visit(final SubscriptNode subscriptNode) throws EnshudSemanticErrorException {}
	@Override
	public void visit(final AssignLeftNode assignLeftNode) throws EnshudSemanticErrorException {}
	@Override
	public void visit(final AssignNode assignNode) throws EnshudSemanticErrorException {}
	@Override
	public void visit(final TermNode termNode) throws EnshudSemanticErrorException {}
	@Override
	public void visit(final VarNode varNode) throws EnshudSemanticErrorException {}
	@Override
	public void visit(final VarRowNode varRowNode) throws EnshudSemanticErrorException {}
	@Override
	public void visit(final WhileStatementNode whileStatementNode) throws EnshudSemanticErrorException {}
}
