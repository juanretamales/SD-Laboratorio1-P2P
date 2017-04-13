package cl.usach.sd;

import peersim.core.GeneralNode;

public class ExampleNode extends GeneralNode {
	private int count;

	public ExampleNode(String prefix) {
		super(prefix);
		this.setCount(0);
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
}
