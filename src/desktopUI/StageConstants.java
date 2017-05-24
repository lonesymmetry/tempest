package desktopUI;

import util.Graphics;
import java.awt.Dimension;
import javafx.geometry.Insets;

/**
 * Stores constants used across the desktop application
 *
 * @author Logan Traffas
 */
public abstract class StageConstants{
	public static final int EXPERIMENTAL_HORIZONTAL_INSETS = 0;//from testing with Windows 10
	public static final int EXPERIMENTAL_VERTICAL_INSETS = 31;//from testing with Windows 10
	public static final Dimension DEFAULT_SIZE = new Dimension(
			1366 - EXPERIMENTAL_HORIZONTAL_INSETS,
			768 - EXPERIMENTAL_VERTICAL_INSETS - Graphics.TASKBAR_HEIGHT
	);
	public static final int PADDING = 5;//px ?
	public static final Insets PADDING_INSETS = new Insets(PADDING, PADDING, PADDING, PADDING);
}
