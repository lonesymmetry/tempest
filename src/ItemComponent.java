import javax.swing.JComponent;
import java.awt.*;

/**
 *
 */
public class ItemComponent extends JComponent{
	private static final Dimension DEFUALT_SIZE = new Dimension(50,10);
	private static final Color DEFAULT_COLOR = Color.WHITE;
	private Dimension size;
	private Util.Point<Integer> start;

	public ItemComponent(){
		this(DEFUALT_SIZE,new Util.Point<Integer>(0,0));
	}

	public ItemComponent(Util.Point<Integer> start){
		this(DEFUALT_SIZE,start);
	}

	public ItemComponent(Dimension size){
		this(size,new Util.Point<Integer>(0,0));
	}

	public ItemComponent(int width, int height){
		this(new Dimension(width,height),new Util.Point<Integer>(0,0));
	}

	public ItemComponent(Dimension size, Util.Point<Integer> start){
		this.size = size;
		this.start = start;
	}

	@Override
	public String toString(){
		return "ItemComponent(size: " + this.size + ")";
	}

	@Override
	public void paintComponent(java.awt.Graphics g) {
		Graphics2D g2 = (Graphics2D) g;// Recover Graphics2D
		Rectangle body = new Rectangle(start.getX(),start.getY(),DEFUALT_SIZE.width,DEFUALT_SIZE.height);
		g2.setColor(DEFAULT_COLOR);
		g2.fill(body);
	}
}

