package view.engine.system;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

import environment.colorwar.Constants;

public class Display {
	private int m_width;
	private int m_height;
	private DisplayComponent m_component;
	private boolean m_isInteractive;
	
	public Display(int width, int height, boolean interactive) {
		m_width = width;
		m_height = height;
		m_component = null;
		m_isInteractive = interactive;
	}
	
	public BufferedImage getContext() {
		return m_component.getContext();
	}
	
	public void initialize() {
		m_component = new DisplayComponent(m_width, m_height, m_isInteractive);
		m_component.initialize();
		
		JFrame frame = new JFrame(Constants.GAME_NAME);
		frame.setContentPane(m_component);
		frame.setResizable(true);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		// TODO make windows closeable, currently there is an error when window closes before app shuts down,
		//  so window closing has been temporarily disabled...
		frame.pack();

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setLocation((screenSize.width - frame.getWidth())/2, (screenSize.height - frame.getHeight()) / 2);
		frame.setVisible(true);
	}
}