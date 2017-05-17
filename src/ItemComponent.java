import javax.swing.JComponent;
import java.awt.Dimension;
import java.awt.Graphics2D;

/**
 *
 */
public class ItemComponent extends JComponent{
	private Dimension size;

	public ItemComponent(){
		this(0,0);
	}

	public ItemComponent(Dimension size){
		this.size = size;
	}

	public ItemComponent(int width, int height){
		this.size = new Dimension(width,height);
	}

	@Override
	public String toString(){
		return "ItemComponent(size: " + this.size + ")";
	}

	@Override
	public void paintComponent(java.awt.Graphics g) {
		Graphics2D g2 = (Graphics2D) g;// Recover Graphics2D
	}
}

