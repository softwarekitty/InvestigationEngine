package tree;

import javax.swing.JTree;
import javax.swing.tree.TreeModel;

import org.jdom2.Element;

import undecided.Util;

@SuppressWarnings("serial")
public class CustomTree extends JTree {
	
	public CustomTree(TreeModel model){
		super(model);
	}

	@Override
	public String convertValueToText(Object value, boolean selected,
			boolean expanded, boolean leaf, int row, boolean hasFocus) {
		return Util.getTreeLabel((Element)value);
	}

}
