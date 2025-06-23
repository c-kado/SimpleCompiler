package enshud.s4.compiler;

public class Leaf {
	private String terminal;
	private int line;

	public void setTerminal(final String terminal) {
		this.terminal = terminal;
	}

	public void setLine(final int line) {
		this.line = line;
	}

	public String getTerminal() {
		return terminal;
	}

	public int getLine() {
		return line;
	}
}
