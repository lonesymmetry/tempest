import javax.swing.JFrame;
import java.awt.Dimension;
import java.awt.Color;

/**
 *
 */
public class DesktopUI implements Input{
	private JFrame frame;
	private static final String FRAME_TITLE = "Tempest";
	private static final boolean HANDLE_TASKBAR = true;
	private static final boolean FULLSCREEN = true;
	private static final Dimension DEFAULT_FRAME_SIZE = new Dimension(100,100);
	private static final Color DEFAULT_BACKGROUND_COLOR = Color.BLACK;

	public Item createNewItem(){
		//TODO
		Util.Util.nyi(Util.Util.getFileName(),Util.Util.getLineNumber());
		return new Item();//should never reach this line
	}

	public void open(){
		this.frame.setVisible(true);
		writeToFrame();
	}

	public void close(){
		frame.setVisible(false);
		frame.dispose();
	}

	private void initializeFrame(){
		this.frame = new JFrame(FRAME_TITLE);
		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Dimension size = FULLSCREEN ? Util.Graphics.generateFullscreenDimension(HANDLE_TASKBAR) : DEFAULT_FRAME_SIZE;
		this.frame = Util.Graphics.createJFrameOfSize(this.frame,size);
		this.frame.getContentPane().setBackground(DEFAULT_BACKGROUND_COLOR);
	}

	private void writeToFrame(){//TODO
		{//for testing only
			this.frame.add(new ItemComponent(new Util.Point<Integer>(100,100)));
		}
	}

	public DesktopUI(){
		initializeFrame();
	}
}
