package spellAid.ui.speaker;

import java.util.Arrays;
import java.util.List;

import javafx.scene.Node;

/**
 * This class acts as a bridge for GUI containers to talk to a speaker object
 * asynchronously. This class stores a list of all registered components that
 * should be enabled or disabled asynchronously.
 * 
 * @author Luke Tudor
 *
 */
public class AsynchronousComponentEnabler {

	private List<Node> nodes;
	private boolean[] recommendedStates;

	public AsynchronousComponentEnabler(Node[] nodeList,
			boolean allShouldBeEnabled) {

		nodes = Arrays.asList(nodeList);
		recommendedStates = new boolean[nodeList.length];
		Arrays.fill(recommendedStates, allShouldBeEnabled);
	}

	public void enableAllComponents() {
		for (Node n : nodes)
			n.setDisable(false);
	}

	public void disableAllComponents() {
		for (Node n : nodes)
			n.setDisable(true);
	}

	public void applyAllRecommendedStates() {
		for (int i = 0; i < nodes.size(); i++) 
			nodes.get(i).setDisable(!recommendedStates[i]);
	}

	public void setShouldComponentBeEnabled(Node n, boolean state) {
		recommendedStates[nodes.indexOf(n)] = state;
	}

}
