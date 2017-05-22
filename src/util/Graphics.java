package util;
import javax.swing.*;
import java.awt.*;

/**
 * Some useful graphical operations
 * @
 * @author Logan Traffas
 */
public class Graphics {
	public static int TASKBAR_HEIGHT = 40;//px the height of the taskbar in windows

	/**
	 * Generates the dimensions that the content pane for a JFrame should be to fill the screen
	 * @param handleTaskbar whether the height of the windows taskbar should be taken into account
	 * @return the dimensions for the fullscreen content pane
	 */
	public static Dimension generateFullscreenDimension(boolean handleTaskbar){
		Dimension size = new Dimension(500, 500);
		JFrame temp = new JFrame();
		temp.setSize(size);
		temp.getContentPane().setPreferredSize(new Dimension(size));//fill frame
		temp.pack();
		size = new Dimension((int)Toolkit.getDefaultToolkit().getScreenSize().getWidth() - (temp.getInsets().left + temp.getInsets().right),(int)Toolkit.getDefaultToolkit().getScreenSize().getHeight() - (temp.getInsets().top + temp.getInsets().bottom));
		if(handleTaskbar) size.height -= TASKBAR_HEIGHT;
		return size;
	}

	public static JFrame createJFrameOfSize(JFrame frame, Dimension size){
		frame.setSize(size);
		frame.getContentPane().setPreferredSize(new Dimension(size));
		frame.pack();
		return frame;
	}

}