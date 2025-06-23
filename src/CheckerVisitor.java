package enshud.s4.compiler;

public class CheckerVisitor implements Visitor {

	@Override
	public void visit(final ProgramNode programNode) throws EnshudSemanticErrorException {
		//decCheckVisitorで，宣言のチェック
		final Visitor decCheckVisitor = new DecCheckVisitor();
		programNode.accept(decCheckVisitor);

		//compCheckVisitorで複合分のチェック
		final Visitor compCheckVisitor = new CompCheckVisitor();
		programNode.accept(compCheckVisitor);
	}

	@Override
	public void visit(final AddOpNode addOpNode) throws EnshudSemanticErrorException {}
	@Override
	public void visit(final ArrayTypeNode arrayTypeNode) throws EnshudSemanticErrorException {}
	@Override
	public void visit(final BaseStatementNode baseStatementNode) throws EnshudSemanticErrorException {}
	@Override
	public void visit(final BlockNode blockNode) throws EnshudSemanticErrorException {}
	@Override
	public void visit(final CallStatementNode callStatementNode) throws EnshudSemanticErrorException {}
	@Override
	public void visit(final ComplexStatementNode complexStatementNode) throws EnshudSemanticErrorException {}
	@Override
	public void visit(final ConstantNode constantNode) throws EnshudSemanticErrorException {}
	@Override
	public void visit(final FactNode fatNode) throws EnshudSemanticErrorException {}
	@Override
	public void visit(final FormulationNode formulationNode) throws EnshudSemanticErrorException {}
	@Override
	public void visit(final FormulationRowNode formulationRowNode) throws EnshudSemanticErrorException {}
	@Override
	public void visit(final IfStatementNode ifStatementNode) throws EnshudSemanticErrorException {}
	@Override
	public void visit(final InStatementNode inStatementNode) throws EnshudSemanticErrorException {}
	@Override
	public void visit(final IntegralNumNode integralNumNode) throws EnshudSemanticErrorException {}
	@Override
	public void visit(final MaxSubscriptNode maxSubscriptNode) throws EnshudSemanticErrorException {}
	@Override
	public void visit(final MinSubscriptNode minSubscriptNode) throws EnshudSemanticErrorException {}
	@Override
	public void visit(final MultiOpNode multiOpNode) throws EnshudSemanticErrorException {}
	@Override
	public void visit(final OutStatementNode outStatementNode) throws EnshudSemanticErrorException {}
	@Override
	public void visit(final ParamNameNode paramNameNode) throws EnshudSemanticErrorException {}
	@Override
	public void visit(final ParamNameRowNode paramNameRowNode) throws EnshudSemanticErrorException {}
	@Override
	public void visit(final ParamNode paramNode) throws EnshudSemanticErrorException {}
	@Override
	public void visit(final ParamRowNode paramRowNode) throws EnshudSemanticErrorException {}
	@Override
	public void visit(final ProcNameNode procNameNode) throws EnshudSemanticErrorException {}
	@Override
	public void visit(final ProgramNameNode programNameNode) throws EnshudSemanticErrorException {}
	@Override
	public void visit(final RelationOpNode relationOpNode) throws EnshudSemanticErrorException {}
	@Override
	public void visit(final SignNode signNode) throws EnshudSemanticErrorException {}
	@Override
	public void visit(final SimpleFormNode simpleFormNode) throws EnshudSemanticErrorException {}
	@Override
	public void visit(final StatementNode statementNode) throws EnshudSemanticErrorException {}
	@Override
	public void visit(final StatementRowNode statementRowNode) throws EnshudSemanticErrorException {}
	@Override
	public void visit(final StdTypeNode stdTypeNode) throws EnshudSemanticErrorException {}
	@Override
	public void visit(final SubPrgDecGroupNode subPrgDecGroupNode) throws EnshudSemanticErrorException {}
	@Override
	public void visit(final SubPrgDecNode subPrgDecNode) throws EnshudSemanticErrorException {}
	@Override
	public void visit(final SubPrgHeadNode subPrgHeadNode) throws EnshudSemanticErrorException {}
	@Override
	public void visit(final SubscriptNode subscriptNode) throws EnshudSemanticErrorException {}
	@Override
	public void visit(final AssignLeftNode assignLeftNode) throws EnshudSemanticErrorException {}
	@Override
	public void visit(final AssignNode assignNode) throws EnshudSemanticErrorException {}
	@Override
	public void visit(final TermNode termNode) throws EnshudSemanticErrorException {}
	@Override
	public void visit(final VarDecNode varDecNode) throws EnshudSemanticErrorException {}
	@Override
	public void visit(final VarDecRowNode varDecRowNode) throws EnshudSemanticErrorException {}
	@Override
	public void visit(final VarNameNode varNameNode) throws EnshudSemanticErrorException {}
	@Override
	public void visit(final VarNameRowNode varNameRowNode) throws EnshudSemanticErrorException {}
	@Override
	public void visit(final VarNode varNode) throws EnshudSemanticErrorException {}
	@Override
	public void visit(final VarRowNode varRowNode) throws EnshudSemanticErrorException {}
	@Override
	public void visit(final VarTypeNode varTypeNode) throws EnshudSemanticErrorException {}
	@Override
	public void visit(final WhileStatementNode whileStatementNode) throws EnshudSemanticErrorException {}
}
