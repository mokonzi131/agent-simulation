package agent;

import iagent.GraphicalObserver;
import iagent.IAgent;

public class HumanAgent implements IAgent {
	
	private GraphicalObserver observer;
	
	public HumanAgent(GraphicalObserver o) {
		observer = o;
	}
	
	@Override
	public int move() {
		return observer.observe();
	}
	
	@Override
	public void reward(double r) {
		observer.displayReward(r);
	}

}
