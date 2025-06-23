package enshud.s4.compiler;

public class CompCheckVisitor implements Visitor {

	@Override
	public void visit(final ProgramNode programNode) throws EnshudSemanticErrorException {
		programNode.getLastChild().accept(this);
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
		for(int i = 0; i < ifStatementNode.getNumOfChildren(); i++) {
			ifStatementNode.getChild(i).accept(this);
		}
	}

	@Override
	public void visit(final WhileStatementNode whileStatementNode) throws EnshudSemanticErrorException {
		for(int i = 0; i < whileStatementNode.getNumOfChildren(); i++) {
			whileStatementNode.getChild(i).accept(this);
		}
	}

	@Override
	public void visit(final BaseStatementNode baseStatementNode) throws EnshudSemanticErrorException {
		baseStatementNode.getChild(0).accept(this);
	}

	@Override
	public void visit(final AssignNode assignNode) throws EnshudSemanticErrorException {
		for(int i = 0; i < assignNode.getNumOfChildren(); i++) {
			assignNode.getChild(i).accept(this);
		}
	}

	@Override
	public void visit(final AssignLeftNode assignLeftNode) throws EnshudSemanticErrorException {
		assignLeftNode.getChild(0).accept(this);
		assignLeftNode.sendVarTypeToParent();
	}

	@Override
	public void visit(final VarNode varNode) throws EnshudSemanticErrorException {
		for(int i = 0; i < varNode.getNumOfChildren(); i++) {
			varNode.getChild(i).accept(this);
		}
		varNode.sendTypeToParent();
	}

	@Override
	public void visit(final VarNameNode varNameNode) throws EnshudSemanticErrorException {
		varNameNode.sendVarNameToParent();
	}

	@Override
	public void visit(final SubscriptNode subscriptNode) throws EnshudSemanticErrorException {
		subscriptNode.checkArray();
		subscriptNode.getChild(0).accept(this);
	}

	@Override
	public void visit(final CallStatementNode callStatementNode) throws EnshudSemanticErrorException {
		for(int i = 0; i < callStatementNode.getNumOfChildren(); i++) {
			callStatementNode.getChild(i).accept(this);
		}
	}

	@Override
	public void visit(final ProcNameNode procNameNode) throws EnshudSemanticErrorException {
		procNameNode.checkDecName();
		procNameNode.sendParamsToParent();
		procNameNode.sendLVarCountToParent();
	}

	@Override
	public void visit(final FormulationRowNode formulationRowNode) throws EnshudSemanticErrorException {
		for(int i = 0; i < formulationRowNode.getNumOfChildren(); i++) {
			formulationRowNode.getChild(i).accept(this);
		}
		formulationRowNode.checkFormsType();
		formulationRowNode.sendFormTypeRowToParent();
		formulationRowNode.sendFormCountToParent();
	}

	@Override
	public void visit(final FormulationNode formulationNode) throws EnshudSemanticErrorException {
		for(int i = 0; i < formulationNode.getNumOfChildren(); i++) {
			formulationNode.getChild(i).accept(this);
		}
		formulationNode.sendFormTypeToParent();
	}

	@Override
	public void visit(final SimpleFormNode simpleFormNode) throws EnshudSemanticErrorException {
		for(int i = 0; i < simpleFormNode.getNumOfChildren(); i++) {
			simpleFormNode.getChild(i).accept(this);
		}
		simpleFormNode.sendSimFormTypeToParent();
	}

	@Override
	public void visit(final TermNode termNode) throws EnshudSemanticErrorException {
		for(int i = 0; i < termNode.getNumOfChildren(); i++) {
			termNode.getChild(i).accept(this);
		}
		termNode.sendTermTypeToParent();
	}

	@Override
	public void visit(final FactNode factNode) throws EnshudSemanticErrorException {
		factNode.getChild(0).accept(this);
		factNode.sendFactTypeToParent();
	}

	@Override
	public void visit(final RelationOpNode relationOpNode) throws EnshudSemanticErrorException {
		relationOpNode.sendOpToParent();
	}

	@Override
	public void visit(final AddOpNode addOpNode) throws EnshudSemanticErrorException {
		addOpNode.sendOpToParent();
	}

	@Override
	public void visit(final MultiOpNode multiOpNode) throws EnshudSemanticErrorException {
		multiOpNode.sendOpToParent();
	}

	@Override
	public void visit(final InStatementNode inStatementNode) throws EnshudSemanticErrorException {
		if(inStatementNode.getNumOfChildren() != 0) {
			inStatementNode.getChild(0).accept(this);
		}
	}

	@Override
	public void visit(final OutStatementNode outStatementNode) throws EnshudSemanticErrorException {
		if(outStatementNode.getNumOfChildren() != 0) {
			outStatementNode.getChild(0).accept(this);
		}
	}

	@Override
	public void visit(final VarRowNode varRowNode) throws EnshudSemanticErrorException {
		for(int i = 0; i < varRowNode.getNumOfChildren(); i++) {
			varRowNode.getChild(i).accept(this);
		}
		varRowNode.sendVarTypeRowToParent();
	}

	@Override
	public void visit(final ConstantNode constantNode) throws EnshudSemanticErrorException {
		constantNode.sendConstTypeToParent();
	}

	@Override
	public void visit(final SignNode signNode) throws EnshudSemanticErrorException {}
	@Override
	public void visit(final ArrayTypeNode arrayTypeNode) throws EnshudSemanticErrorException {}
	@Override
	public void visit(final BlockNode blockNode) throws EnshudSemanticErrorException {}
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
	public void visit(final SubPrgDecGroupNode subPrgDecGroupNode) throws EnshudSemanticErrorException {}
	@Override
	public void visit(final SubPrgDecNode subPrgDecNode) throws EnshudSemanticErrorException {}
	@Override
	public void visit(final SubPrgHeadNode subPrgHeadNode) throws EnshudSemanticErrorException {}
	@Override
	public void visit(final VarDecNode varDecNode) throws EnshudSemanticErrorException {}
	@Override
	public void visit(final VarDecRowNode varDecRowNode) throws EnshudSemanticErrorException {}
	@Override
	public void visit(final VarNameRowNode varNameRowNode) throws EnshudSemanticErrorException {}
	@Override
	public void visit(final VarTypeNode varTypeNode) throws EnshudSemanticErrorException {}
}
