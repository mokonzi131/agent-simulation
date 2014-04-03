package engine;

import java.awt.Graphics2D;
import java.util.logging.Logger;

import colorsgame.Constants;
import engine.rendering.Display;

public class Engine implements Runnable {
	private static final Logger LOGGER = Logger.getLogger(Thread.currentThread().getStackTrace()[0].getClassName());
	private static final int FPS = 30;
	private static final float NANOS_PER_SECOND = 1000000000f;
	private static final float FRAME_FREQUENCY_NANOS = NANOS_PER_SECOND / FPS;
	
	public enum RENDERING_TYPE {
		SIMULATED,
		DEVELOPER,
		NORMAL
	}
	
	private Game m_game;
	private RENDERING_TYPE m_renderingType;
	private Display m_masterDisplay;
	private Display m_humanDisplay;
	private boolean m_running;
	
	public Engine(Game game) {
		m_game = game;
		m_renderingType = Constants.renderingType;
		m_masterDisplay = null;
		m_humanDisplay = null;
		m_running = false;
	}
	
	@Override
	public void run() {
		// setup the engine and rendering stack
		initialize();
		
		// setup the game
		m_game.initialize();
		
		// run the game
		start();
		long lastTime = System.nanoTime();
		while (m_running) {
			// throttle update-loop timing to FPS
			long elapsedTime = System.nanoTime() - lastTime;
			while (elapsedTime < FRAME_FREQUENCY_NANOS)
				elapsedTime = System.nanoTime() - lastTime;
			lastTime += elapsedTime;
			double  deltaTime = elapsedTime / NANOS_PER_SECOND;
			
			// update game
			m_game.update(deltaTime);
			
			// render the game (//TODO make sure displays and contexts are valid)
			Graphics2D[] contexts = new Graphics2D[2];
			contexts[0] = m_masterDisplay.getContext();
			contexts[1] = m_humanDisplay.getContext();
			m_game.render(contexts);
		}
		
		// cleanup the game
		m_game.teardown();
		
		// cleanup engine and rendering stack
		teardown();
	}
	
	private void initialize() {
		m_running = true;
		
		switch(this.m_renderingType) {
		case DEVELOPER:
			m_masterDisplay = new Display(
					Constants.DEVELOPER_VIEW_WIDTH, Constants.DEVELOPER_VIEW_HEIGHT, false);
			m_masterDisplay.initialize();
			
			// TODO also create game views for each AI agent -- one per agent?
		case NORMAL:
			m_humanDisplay = new Display(
					Constants.AGENT_VIEW_WIDTH, Constants.AGENT_VIEW_HEIGHT, Constants.isHumanPlayable);
			m_humanDisplay.initialize();
		case SIMULATED:
			// TODO implement text-based resources to help observe agent-training...
			break;
		}
	}
	
	private void start() {
		if (!m_running)
			m_running = true;
	}
	
	private void stop() {
		m_running = false;
	}
	
	private void teardown() {
		if (m_masterDisplay != null)
			m_masterDisplay.teardown();
	}
}