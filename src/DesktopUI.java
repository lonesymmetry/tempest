import javax.swing.JFrame;
import java.awt.Dimension;

/**
 *
 */
public class DesktopUI implements Input{
	private JFrame frame;
	private static final Dimension DEFAULT_FRAME_SIZE = new Dimension(100,100);
	private static final String FRAME_TITLE = "Tempest";
	private static final boolean HANDLE_TASKBAR = true;
	private static final boolean FULLSCREEN = false;

	public void run(){
		this.frame.setVisible(true);
	}

	public void close(){
		frame.setVisible(false);
		frame.dispose();
	}

	public DesktopUI(){
		{
			this.frame = new JFrame(FRAME_TITLE);
			this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			Dimension size = FULLSCREEN ? Util.Graphics.generateFullscreenDimension(HANDLE_TASKBAR) : DEFAULT_FRAME_SIZE;
			this.frame = Util.Graphics.setSize(this.frame,size);
		}
	}
}
