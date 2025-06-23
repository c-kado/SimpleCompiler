package enshud.s4.compiler;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

import enshud.casl.CaslSimulator;

public class Compiler {

	private static final int SAND_NUM = 0;
	private static final int SARRAY_NUM = 1;
	private static final int SBEGIN_NUM = 2;
	private static final int SBOOLEAN_NUM = 3;
	private static final int SCHAR_NUM = 4;
	private static final int SDIVD_NUM = 5;
	private static final int SDO_NUM = 6;
	private static final int SELSE_NUM = 7;
	private static final int SEND_NUM = 8;
	private static final int SFALSE_NUM = 9;
	private static final int SIF_NUM = 10;
	private static final int SINTEGER_NUM = 11;
	private static final int SMOD_NUM = 12;
	private static final int SNOT_NUM = 13;
	private static final int SOF_NUM = 14;
	private static final int SOR_NUM = 15;
	private static final int SPROCEDURE_NUM = 16;
	private static final int SPROGRAM_NUM = 17;
	private static final int SREADLN_NUM = 18;
	private static final int STHEN_NUM = 19;
	private static final int STRUE_NUM = 20;
	private static final int SVAR_NUM = 21;
	private static final int SWHILE_NUM = 22;
	private static final int SWRITELN_NUM = 23;
	private static final int SEQUAL_NUM = 24;
	private static final int SNOTEQUAL_NUM = 25;
	private static final int SLESS_NUM = 26;
	private static final int SLESSEQUAL_NUM = 27;
	private static final int SGREATEQUAL_NUM = 28;
	private static final int SGREAT_NUM = 29;
	private static final int SPLUS_NUM = 30;
	private static final int SMINUS_NUM = 31;
	private static final int SSTAR_NUM = 32;
	private static final int SLPAREN_NUM = 33;
	private static final int SRPAREN_NUM = 34;
	private static final int SLBRACKET_NUM = 35;
	private static final int SRBRACKET_NUM = 36;
	private static final int SSEMICOLON_NUM = 37;
	private static final int SCOLON_NUM = 38;
	private static final int SRANGE_NUM = 39;
	private static final int SASSIGN_NUM = 40;
	private static final int SCOMMA_NUM = 41;
	private static final int SDOT_NUM = 42;
	private static final int SIDENTIFIER_NUM = 43;
	private static final int SCONSTANT_NUM = 44;
	private static final int SSTRING_NUM = 45;
	private static final int TS_LINE_WORD = 4;
	private static final int TOKENSTR_ROWNUM = 0;
	private static final int TOKENID_ROWNUM = 2;
	private static final int TOKENLINE_ROWNUM = 3;

	private static final List<String> TERMINALS = new ArrayList<String>(Arrays.asList
			("and", "", "", "boolean", "char", "/", "", "", "",
			 "false", "", "integer", "mod", "", "", "or", "", "",
			 "", "", "true", "", "", "", "=", "<>", "<", "<=",
			 ">=", ">", "+", "-", "*", "", "", "", "", "", "", "", "", "", ""));


	ListIterator<Integer> idIter;
	List<Integer> tokenIDs = new ArrayList<Integer>();
	List<Integer> tokenLine = new ArrayList<Integer>();
	List<String> constants = new ArrayList<String>();

	/**
	 * サンプルmainメソッド．
	 * 単体テストの対象ではないので自由に改変しても良い．
	 */
	public static void main(final String[] args) {
		//new Lexer().run("test/testcase/test.pas", "test/test.ts");

		// Compilerを実行してcasを生成する
		new Compiler().run("data/ts/normal01.ts", "test/out01.cas");

		// 上記casを，CASLアセンブラ & COMETシミュレータで実行する
		CaslSimulator.run("test/out01.cas", "test/out.ans");
	}

	/**
	 * TODO
	 *
	 * 開発対象となるCompiler実行メソッド．
	 * 以下の仕様を満たすこと．
	 *
	 * 仕様:
	 * 第一引数で指定されたtsファイルを読み込み，CASL IIプログラムにコンパイルする．
	 * コンパイル結果のCASL IIプログラムは第二引数で指定されたcasファイルに書き出すこと．
	 * 構文的もしくは意味的なエラーを発見した場合は標準エラーにエラーメッセージを出力すること．
	 * （エラーメッセージの内容はChecker.run()の出力に準じるものとする．）
	 * 入力ファイルが見つからない場合は標準エラーに"File not found"と出力して終了すること．
	 *
	 * @param inputFileName 入力tsファイル名
	 * @param outputFileName 出力casファイル名
	 */
	public void run(final String inputFileName, final String outputFileName) {

		// TODO

		try {
			checkTSFile(inputFileName);
			final Node prgAST = genAST();
			checkTraversal(prgAST);
			genCasTraversal(prgAST, outputFileName);
		} catch (IOException e) {
			System.err.println("File not found");
		} catch(EnshudSyntaxErrorException e) {
			// 構文エラーの例外をつかむ．
			System.err.println("Syntax error: line " + e.getLineNumber());
		} catch(EnshudSemanticErrorException e) {
			//セマンティックエラー
			System.err.println("Semantic error: line " + e.getLineNumber());
		} catch(EnshudTsFileErrorException e) {
			System.err.println("File error: line " + (e.getLineNumber()+1));
		}
	}

	private void checkTSFile(final String inputFileName)
			throws IOException, EnshudTsFileErrorException {
		final List<String> inputFile = Files.readAllLines(Paths.get(inputFileName));

		String[] line;
		for(int i = 0; i < inputFile.size(); i++) {
			if(inputFile.get(i).split("\t").length != TS_LINE_WORD) {
				//pasファイル中の文字列，トークン名，トークンID，行数の並びでない
				throw new EnshudTsFileErrorException(i);
			}
			line = inputFile.get(i).split("\t");
			if(!line[TOKENID_ROWNUM].matches("\\d*")) {
				throw new EnshudTsFileErrorException(i);
			}
			if(!line[TOKENLINE_ROWNUM].matches("\\d*")) {
				throw new EnshudTsFileErrorException(i);
			}
			tokenIDs.add(Integer.parseInt(line[TOKENID_ROWNUM]));
			tokenLine.add(Integer.parseInt(line[TOKENLINE_ROWNUM]));
			if(tokenIDs.get(i) == SIDENTIFIER_NUM || tokenIDs.get(i) == SCONSTANT_NUM
				|| tokenIDs.get(i) == SSTRING_NUM) {
				constants.add(line[TOKENSTR_ROWNUM]);
			}

			idIter = tokenIDs.listIterator();
		}
	}

	private Node genAST() throws EnshudSyntaxErrorException {
		final Node prgAst = new ProgramNode();
		try {
			program(prgAst);
		} catch (NoSuchElementException e) {
			throw new EnshudSyntaxErrorException(tokenLine.get(idIter.previousIndex()));
		}
		if(idIter.hasNext()) {
			throw new EnshudSyntaxErrorException(tokenLine.get(idIter.nextIndex()));
		}

		return prgAst;
	}

	private void checkTraversal(final Node prgAST) throws EnshudSemanticErrorException {
		final CheckerVisitor checkerVisitor = new CheckerVisitor();
		prgAST.accept(checkerVisitor);
	}

	private void genCasTraversal(final Node prgAST, final String outputFileName) throws EnshudSemanticErrorException, IOException {
		final GenCasVisitor genCasVisitor = new GenCasVisitor();
		prgAST.accept(genCasVisitor);

		Files.write(Paths.get(outputFileName), genCasVisitor.getCasCodeList(),
					StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
	}

	private void checkNextID(final int tokenID) throws EnshudSyntaxErrorException, NoSuchElementException {
		if(idIter.next() != tokenID) {
			throw new EnshudSyntaxErrorException(tokenLine.get(idIter.previousIndex()));
		}
	}

	private void regConst(final Node node) {
		node.addLeaf(constants.get(0), tokenLine.get(idIter.previousIndex()));
		constants.remove(0);
	}

	private void regTerminal(final Node node, final String terminal) {
		node.addLeaf(terminal, tokenLine.get(idIter.previousIndex()));
	}

	private void program(final Node node) throws EnshudSyntaxErrorException, NoSuchElementException {
		checkNextID(SPROGRAM_NUM);
		programName(new ProgramNameNode(node));
		checkNextID(SSEMICOLON_NUM);
		block(new BlockNode(node));
		node.checkLastNodeNull();
		complexStatement(new ComplexStatementNode(node));
		checkNextID(SDOT_NUM);
	}

	private void programName(final Node node) throws EnshudSyntaxErrorException, NoSuchElementException {
		checkNextID(SIDENTIFIER_NUM);
		regConst(node);
	}

	private void block(final Node node) throws EnshudSyntaxErrorException, NoSuchElementException {
		varDeclaration(new VarDecNode(node));
		node.checkLastNodeNull();
		subPrgDeclarationGroup(new SubPrgDecGroupNode(node));
		node.checkLastNodeNull();
	}

	private void varDeclaration(final Node node) throws EnshudSyntaxErrorException, NoSuchElementException {
		if(idIter.next() != SVAR_NUM) {
			idIter.previous();
			return;
		}
		varDeclarationRow(new VarDecRowNode(node));
	}

	private void varDeclarationRow(final Node node) throws EnshudSyntaxErrorException, NoSuchElementException {
		varNameRow(new VarNameRowNode(node));
		checkNextID(SCOLON_NUM);
		varType(new VarTypeNode(node));
		checkNextID(SSEMICOLON_NUM);
		while(idIter.next() == SIDENTIFIER_NUM) {
			idIter.previous();
			varNameRow(new VarNameRowNode(node));
			checkNextID(SCOLON_NUM);
			varType(new VarTypeNode(node));
			checkNextID(SSEMICOLON_NUM);
		}
		idIter.previous();
	}

	private void varNameRow(final Node node) throws EnshudSyntaxErrorException, NoSuchElementException {
		do {
			varName(new VarNameNode(node));
		} while(idIter.next() == SCOMMA_NUM);
		idIter.previous();
	}

	private void varName(final Node node) throws EnshudSyntaxErrorException, NoSuchElementException {
		checkNextID(SIDENTIFIER_NUM);
		regConst(node);
	}

	private void varType(final Node node) throws EnshudSyntaxErrorException, NoSuchElementException {
		if(idIter.next() == SARRAY_NUM) {
			idIter.previous();
			arrayType(new ArrayTypeNode(node));
		} else {
			idIter.previous();
			stdType(new StdTypeNode(node));
		}
	}

	private void stdType(final Node node) throws EnshudSyntaxErrorException, NoSuchElementException {
		final int nextToken = idIter.next();
		if(nextToken == SBOOLEAN_NUM || nextToken == SCHAR_NUM || nextToken == SINTEGER_NUM) {
			regTerminal(node, TERMINALS.get(nextToken));
		} else {
			throw new EnshudSyntaxErrorException(tokenLine.get(idIter.previousIndex()));
		}
	}

	private void arrayType(final Node node) throws EnshudSyntaxErrorException, NoSuchElementException {
		checkNextID(SARRAY_NUM);
		checkNextID(SLBRACKET_NUM);
		minSubscript(new MinSubscriptNode(node));
		checkNextID(SRANGE_NUM);
		maxSubscript(new MaxSubscriptNode(node));
		checkNextID(SRBRACKET_NUM);
		checkNextID(SOF_NUM);
		stdType(new StdTypeNode(node));
	}

	private void minSubscript(final Node node) throws EnshudSyntaxErrorException, NoSuchElementException {
		integralNum(new IntegralNumNode(node));
	}

	private void maxSubscript(final Node node) throws EnshudSyntaxErrorException, NoSuchElementException {
		integralNum(new IntegralNumNode(node));
	}

	private void integralNum(final Node node) throws EnshudSyntaxErrorException, NoSuchElementException {
		sign(new SignNode(node));
		node.checkLastNodeNull();
		checkNextID(SCONSTANT_NUM);
		regConst(node);
	}

	private void sign(final Node node) throws EnshudSyntaxErrorException, NoSuchElementException {
		final int nextToken = idIter.next();
		if(nextToken == SPLUS_NUM || nextToken == SMINUS_NUM) {
			regTerminal(node, TERMINALS.get(nextToken));
		} else {
			idIter.previous();
		}
	}

	private void subPrgDeclarationGroup(final Node node) throws EnshudSyntaxErrorException, NoSuchElementException {
		while(idIter.next() == SPROCEDURE_NUM) {
			idIter.previous();
			subPrgDeclaration(new SubPrgDecNode(node));
			checkNextID(SSEMICOLON_NUM);
		}
		idIter.previous();
	}

	private void subPrgDeclaration(final Node node) throws EnshudSyntaxErrorException, NoSuchElementException {
		subPrgHead(new SubPrgHeadNode(node));
		varDeclaration(new VarDecNode(node));
		complexStatement(new ComplexStatementNode(node));
	}

	private void subPrgHead(final Node node) throws EnshudSyntaxErrorException, NoSuchElementException {
		checkNextID(SPROCEDURE_NUM);
		procedureName(new ProcNameNode(node));
		param(new ParamNode(node));
		node.checkLastNodeNull();
		checkNextID(SSEMICOLON_NUM);
	}

	private void procedureName(final Node node) throws EnshudSyntaxErrorException, NoSuchElementException {
		checkNextID(SIDENTIFIER_NUM);
		regConst(node);
	}

	private void param(final Node node) throws EnshudSyntaxErrorException, NoSuchElementException {
		if(idIter.next() != SLPAREN_NUM) {
			idIter.previous();
			return;
		}
		paramRow(new ParamRowNode(node));
		checkNextID(SRPAREN_NUM);
	}

	private void paramRow(final Node node) throws EnshudSyntaxErrorException, NoSuchElementException {
		do {
			paramNameRow(new ParamNameRowNode(node));
			checkNextID(SCOLON_NUM);
			stdType(new StdTypeNode(node));
		} while(idIter.next() == SSEMICOLON_NUM);
		idIter.previous();
	}

	private void paramNameRow(final Node node) throws EnshudSyntaxErrorException, NoSuchElementException {
		do {
			paramName(new ParamNameNode(node));
		} while(idIter.next() == SCOMMA_NUM);
		idIter.previous();
	}

	private void paramName(final Node node) throws EnshudSyntaxErrorException, NoSuchElementException {
		checkNextID(SIDENTIFIER_NUM);
		regConst(node);
	}

	private void complexStatement(final Node node) throws EnshudSyntaxErrorException, NoSuchElementException {
		checkNextID(SBEGIN_NUM);
		statementRow(new StatementRowNode(node));
		checkNextID(SEND_NUM);
	}

	private void statementRow(final Node node) throws EnshudSyntaxErrorException, NoSuchElementException {
		statement(new StatementNode(node));
		checkNextID(SSEMICOLON_NUM);
		int nextToken = idIter.next();
		while(nextToken == SIDENTIFIER_NUM || nextToken == SREADLN_NUM || nextToken == SWRITELN_NUM ||
				nextToken == SBEGIN_NUM || nextToken == SIF_NUM || nextToken == SWHILE_NUM) {
			idIter.previous();
			statement(new StatementNode(node));
			checkNextID(SSEMICOLON_NUM);
			nextToken = idIter.next();
		}
		idIter.previous();
	}

	private void statement(final Node node) throws EnshudSyntaxErrorException, NoSuchElementException {
		switch(idIter.next()) {
			case    SIF_NUM: ifStatement(new IfStatementNode(node));
							 break;
			case SWHILE_NUM: whileStatement(new WhileStatementNode(node));
							 break;
			default: idIter.previous();
					 baseStatement(new BaseStatementNode(node));
					 break;
		}
	}

	private void ifStatement(final Node node) throws EnshudSyntaxErrorException, NoSuchElementException {
		formulation(new FormulationNode(node));
		checkNextID(STHEN_NUM);
		complexStatement(new ComplexStatementNode(node));
		if(idIter.next() != SELSE_NUM) {
			idIter.previous();
			return;
		}
		complexStatement(new ComplexStatementNode(node));
	}

	private void whileStatement(final Node node) throws EnshudSyntaxErrorException, NoSuchElementException {
		formulation(new FormulationNode(node));
		checkNextID(SDO_NUM);
		complexStatement(new ComplexStatementNode(node));
	}

	private void baseStatement(final Node node) throws EnshudSyntaxErrorException, NoSuchElementException {
		switch(idIter.next()) {
			case SIDENTIFIER_NUM: idIter.previous();
								  assignOrCall(node);
								  break;
			case      SBEGIN_NUM: idIter.previous();
								  complexStatement(new ComplexStatementNode(node));
								  break;
			case     SREADLN_NUM: inStatement(new InStatementNode(node));
								  break;
			case    SWRITELN_NUM: outStatement(new OutStatementNode(node));
								  break;
			default: throw new EnshudSyntaxErrorException(tokenLine.get(idIter.nextIndex()-1));
		}
	}

	private void assignOrCall(final Node node) throws EnshudSyntaxErrorException, NoSuchElementException {
		checkNextID(SIDENTIFIER_NUM);
		switch(idIter.next()) {
			case   SASSIGN_NUM:
			case SLBRACKET_NUM: idIter.previous();
								assign(new AssignNode(node));
								break;

			default           : idIter.previous();
								callStatement(new CallStatementNode(node));
								break;
		}
	}

	private void assign(final Node node) throws EnshudSyntaxErrorException, NoSuchElementException {
		idIter.previous();
		assignLeft(new AssignLeftNode(node));
		checkNextID(SASSIGN_NUM);
		formulation(new FormulationNode(node));
	}

	private void assignLeft(final Node node) throws EnshudSyntaxErrorException, NoSuchElementException {
		var(new VarNode(node));
	}

	private void var(final Node node) throws EnshudSyntaxErrorException, NoSuchElementException {
		//変数 = 変数名, ["[", 添字, "]"]
		varName(new VarNameNode(node));
		if(idIter.next() != SLBRACKET_NUM) {
			idIter.previous();
			return;
		}
		subscript(new SubscriptNode(node));
		checkNextID(SRBRACKET_NUM);
	}

	private void subscript(final Node node) throws EnshudSyntaxErrorException, NoSuchElementException {
		formulation(new FormulationNode(node));
	}

	private void callStatement(final Node node) throws EnshudSyntaxErrorException, NoSuchElementException {
		idIter.previous();
		procedureName(new ProcNameNode(node));
		if(idIter.next() != SLPAREN_NUM) {
			idIter.previous();
			return;
		}
		formulationRow(new FormulationRowNode(node));
		checkNextID(SRPAREN_NUM);
	}

	private void formulationRow(final Node node) throws EnshudSyntaxErrorException, NoSuchElementException {
		do {
			formulation(new FormulationNode(node));
		} while(idIter.next() == SCOMMA_NUM);
		idIter.previous();
	}

	private void formulation(final Node node) throws EnshudSyntaxErrorException, NoSuchElementException {
		simpleForm(new SimpleFormNode(node));
		final int nextToken = idIter.next();
		if(nextToken == SEQUAL_NUM || nextToken == SNOTEQUAL_NUM || nextToken == SLESS_NUM ||
			nextToken == SLESSEQUAL_NUM || nextToken == SGREATEQUAL_NUM || nextToken == SGREAT_NUM) {
			idIter.previous();
			relationOp(new RelationOpNode(node));
			simpleForm(new SimpleFormNode(node));
		} else {
			idIter.previous();
		}
	}

	private void simpleForm(final Node node) throws EnshudSyntaxErrorException, NoSuchElementException {
		sign(new SignNode(node));
		node.checkLastNodeNull();
		term(new TermNode(node));
		int nextToken = idIter.next();
		while(nextToken == SPLUS_NUM || nextToken == SMINUS_NUM || nextToken == SOR_NUM) {
			idIter.previous();
			addOp(new AddOpNode(node));
			term(new TermNode(node));
			nextToken = idIter.next();
		}
		idIter.previous();
	}

	private void term(final Node node) throws EnshudSyntaxErrorException, NoSuchElementException {
		fact(new FactNode(node));
		int nextToken = idIter.next();
		while(nextToken == SSTAR_NUM || nextToken == SDIVD_NUM || nextToken == SMOD_NUM ||
				nextToken == SAND_NUM) {
			idIter.previous();
			multiOp(new MultiOpNode(node));
			fact(new FactNode(node));
			nextToken = idIter.next();
		}
		idIter.previous();
	}

	private void fact(final Node node) throws EnshudSyntaxErrorException, NoSuchElementException {
		switch(idIter.next()) {
			case SIDENTIFIER_NUM: idIter.previous();
								  var(new VarNode(node));
								  break;

			case     SLPAREN_NUM: formulation(new FormulationNode(node));
								  checkNextID(SRPAREN_NUM);
								  break;

			case        SNOT_NUM: fact(new FactNode(node));
								  break;

			default             : idIter.previous();
								  constant(new ConstantNode(node));
								  break;
		}
	}

	private void relationOp(final Node node) throws EnshudSyntaxErrorException, NoSuchElementException {
		final int nextToken = idIter.next();
		if(nextToken == SEQUAL_NUM || nextToken == SNOTEQUAL_NUM || nextToken == SLESS_NUM ||
			nextToken == SLESSEQUAL_NUM || nextToken == SGREATEQUAL_NUM || nextToken == SGREAT_NUM) {
			regTerminal(node, TERMINALS.get(nextToken));
		} else {
			throw new EnshudSyntaxErrorException(tokenLine.get(idIter.previousIndex()));
		}
	}

	private void addOp(final Node node) throws EnshudSyntaxErrorException, NoSuchElementException {
		final int nextToken = idIter.next();
		if(nextToken == SPLUS_NUM || nextToken == SMINUS_NUM || nextToken == SOR_NUM) {
			regTerminal(node, TERMINALS.get(nextToken));
		} else {
			throw new EnshudSyntaxErrorException(tokenLine.get(idIter.previousIndex()));
		}
	}

	private void multiOp(final Node node) throws EnshudSyntaxErrorException, NoSuchElementException {
		final int nextToken = idIter.next();
		if(nextToken == SSTAR_NUM || nextToken == SDIVD_NUM ||
			nextToken == SMOD_NUM || nextToken == SAND_NUM) {
			regTerminal(node, TERMINALS.get(nextToken));
		} else {
			throw new EnshudSyntaxErrorException(tokenLine.get(idIter.previousIndex()));
		}
	}

	private void inStatement(final Node node) throws EnshudSyntaxErrorException, NoSuchElementException {
		if(idIter.next() != SLPAREN_NUM) {
			idIter.previous();
			return;
		}
		varRow(new VarRowNode(node));
		checkNextID(SRPAREN_NUM);
	}

	private void outStatement(final Node node) throws EnshudSyntaxErrorException, NoSuchElementException {
		if(idIter.next() != SLPAREN_NUM) {
			idIter.previous();
			return;
		}
		formulationRow(new FormulationRowNode(node));
		checkNextID(SRPAREN_NUM);
	}

	private void varRow(final Node node) throws EnshudSyntaxErrorException, NoSuchElementException {
		do {
			var(new VarNode(node));
		} while(idIter.next() == SCOMMA_NUM);
		idIter.previous();
	}

	private void constant(final Node node) throws EnshudSyntaxErrorException, NoSuchElementException {
		final int nextToken = idIter.next();
		if(nextToken == SCONSTANT_NUM || nextToken == SSTRING_NUM) {
			regConst(node);
		} else if(nextToken == SFALSE_NUM || nextToken == STRUE_NUM) {
			regTerminal(node, TERMINALS.get(nextToken));
		} else {
			throw new EnshudSyntaxErrorException(tokenLine.get(idIter.previousIndex()));
		}
	}
}

class EnshudSyntaxErrorException extends Exception {
	int lineNumber;

	EnshudSyntaxErrorException(final int lineNumber) {
		this.lineNumber = lineNumber;
	}

	int getLineNumber(){
		return lineNumber;
	}
}

class EnshudSemanticErrorException extends Exception {
	int lineNumber;

	EnshudSemanticErrorException(final int lineNumber) {
		this.lineNumber = lineNumber;
	}

	int getLineNumber(){
		return lineNumber;
	}
}

class EnshudTsFileErrorException extends Exception {
	int lineNumber;

	EnshudTsFileErrorException(final int lineNumber) {
		this.lineNumber = lineNumber;
	}

	int getLineNumber() {
		return lineNumber;
	}
}
