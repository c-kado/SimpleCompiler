package enshud.s4.compiler;

import java.util.ArrayList;
import java.util.List;

public abstract class Node {
	private final List<Node> children = new ArrayList<Node>();
	private Node parent;
	private final Leaf leaf = new Leaf();

	public Node() {}

	public Node(final Node node) {
		node.children.add(this);
		parent = node;
	}

	public void addLeaf(final String terminal, final int line) {
		leaf.setTerminal(terminal);
		leaf.setLine(line);
	}

	public Node getLastChild() {
		return children.get(children.size()-1);
	}

	public Node getChild(final int i) {
		return children.get(i);
	}

	public int getNumOfChildren() {
		return children.size();
	}

	public Node getParent() {
		return parent;
	}

	public ProgramNode getRootNode() {
		Node root = parent;
		while(root.parent != null) {
			root = root.parent;
		}
		return (ProgramNode) root;
	}

	public void checkLastNodeNull() {
		if(children.get(children.size()-1).children.size() == 0 &&
				children.get(children.size()-1).getTerminalString() == null) {
				children.remove(children.size()-1);
			}
	}

	public Node getAnc(final int ancNum) {
		Node anc = parent;
		for(int i = 1; i < ancNum; i++) {
			if(anc == null) {
				return null;
			} else {
				anc = anc.parent;
			}
		}
		return anc;
	}

	public String getTerminalString() {
		return leaf.getTerminal();
	}

	public int getTerminalLine() {
		return leaf.getLine();
	}

	public boolean equalNodeName(final String nodeName) {
		return getClass().getName().endsWith("." + nodeName);
	}

	public abstract void accept(Visitor visitor) throws EnshudSemanticErrorException;
}
