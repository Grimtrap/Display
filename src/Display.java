import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Toolkit;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Rectangle;

public class Display extends JFrame{

    public static void main(String[] args) {
	    new Display(new Bracket());
    }

    public Display(Bracket tournament) {


        super("Tournament Bracket");

        JPanel displayPanel = new DisplayPanel();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(Toolkit.getDefaultToolkit().getScreenSize().width,Toolkit.getDefaultToolkit().getScreenSize().height);
        this.requestFocusInWindow(); //make sure the frame has focus
        this.setVisible(true);
    }

    private class DisplayPanel extends JPanel {

        public void paintComponent(Graphics g) {



            super.paintComponent(g);
            setDoubleBuffered(true);


        }

    }

}
