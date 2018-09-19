import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Toolkit;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Rectangle;

public class Display {

    public static void main(String[] args) {
	// write your code here
    }

    private class DisplayPanel extends JPanel {

        public void paintComponent(Graphics g) {

            super.paintComponent(g);
            setDoubleBuffered(true);


        }

    }

    public void display(Bracket tournament) {

    }
}
