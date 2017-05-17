import javax.swing.JFrame;
import java.awt.Dimension;

/**
 *
 */
public class DesktopUI implements Input{
	private JFrame frame;
	private static final Dimension defaultSize = new Dimension(100,100);

	public DesktopUI(){
		this.frame = new JFrame();
	}
}
