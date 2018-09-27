/*
Display.java
A program that displays tournament brackets
@author Eric Ke
9/24/2018
 */

//Graphics &GUI imports
import javax.swing.*;
import java.awt.*;

//Keyboard imports
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import static java.awt.Color.BLACK;
import static java.awt.Color.WHITE;
import static java.awt.Color.white;


//Util


public class Display3 extends JFrame {
  
  //class variables
  private static JFrame window;
  private JPanel displayPanel;
  private Bracket tournament;
  private static double scaleRatio;
  
  
  //Constructor - this runs first
  public Display3(Bracket tournament) {
    super("Tournament Bracket");
    
    this.tournament = tournament;
    
    
    // Set the frame to full screen
    this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    this.setSize(Toolkit.getDefaultToolkit().getScreenSize());
    scaleRatio = (double) Toolkit.getDefaultToolkit().getScreenSize().width / 1920; //scale ratio of the screen so it's compatible with other screens
    System.out.println(scaleRatio);
    //frame.setResizable(false);
    
    
    //Set up the game panel (where we put our graphics)
    displayPanel = new DisplayPanel();
    displayPanel.setBackground(new Color(10,10,10,255));
    this.add(displayPanel);
    MyKeyListener keyListener = new MyKeyListener();
    this.addKeyListener(keyListener);
    
    this.requestFocusInWindow(); //make sure the frame has focus
    
    this.setVisible(true);
    
    
  } //End of Constructor
  
  
  /**
   * --------- INNER CLASSES -------------
   **/
  
  // Inner class for the the game area - This is where all the drawing of the screen occurs
  private class DisplayPanel extends JPanel {
    
    
    public void paintComponent(Graphics g) {
      Font font1 = new Font("Arial", Font.PLAIN, (int)(16*scaleRatio));
      
      int numOfRounds = tournament.getNumberOfRounds();
      
      super.paintComponent(g);
      setDoubleBuffered(true);
      g.setColor(BLACK);
      Image match = new ImageIcon("match.png").getImage();
      
      g.setColor(white);
      //i = round number
      for (int i = 2; i <= numOfRounds; i++) { //cycles through number of rounds in tournament
        int gap = (int) Math.pow(2, (i-1));
        int biggerGap = gap*2;
        int x, y;
        int connectionPointX, connectionPointY;
        
        //j = match number
        for (int j = 1; j <= tournament.getNumberOfMatchesInRound(i); j++) { //cycles through matches in round
          String[][] teams = tournament.getTeamsInMatch(i, j); //creates array of teams in specified match
          
          x = (int)((30 + 180*(i-1))*scaleRatio); //spaces them out depending on match
          connectionPointX = (int)((30 + 180*(i))*scaleRatio);
          
          y = (int) ((90 * (j - 1) * gap + 45 * gap) * scaleRatio);
          connectionPointY = (int)((90*((j-1)/2)*biggerGap + 45*biggerGap + 35)*scaleRatio);//uses gap to determine spacing between
          
          g.drawImage(match, x, y,(int)(140*scaleRatio),(int)(70*scaleRatio),null); //Displays black box
          
          if(i < numOfRounds) {
            g.drawLine((int)(x + 140* scaleRatio), (int)(y + 35*scaleRatio), connectionPointX, connectionPointY);
          }
          g.setFont(font1);
          g.drawString(teams[0][0], (int)(x+5*scaleRatio), (int)(y+20*scaleRatio));
          g.drawString(teams[1][0], (int)(x+5*scaleRatio), (int)(y+60*scaleRatio));
          update(g);
        }
      }
      
      for(int q = 1; q < tournament.getNumberOfMatchesInRound(1) + 1; q++){
        int gap = (int) Math.pow(2, (1));
        int biggerGap = gap*2;
        int x = 0;
        int y = 0;
        int connectionPointX = 0;
        int connectionPointY = 0;
        String[][] round1 = tournament.getTeamsInMatch(1, q);
        int cnt = 0;
        for(int e = 1; e < tournament.getNumberOfMatchesInRound(2) + 1; e++){
          x = (int)((30 + 180*(0))*scaleRatio); //spaces them out depending on match
          cnt++;
          String[][] round2 = tournament.getTeamsInMatch(2, e);
          connectionPointX = (int)((30 + 180*(1))*scaleRatio);
          if(round1[0][0] == round2[0][0]){
            y = (int) ((90 * (e-1) * gap + 45 * gap) * scaleRatio);
            connectionPointY = (int)((90*((e-1)/2)*biggerGap + 45*biggerGap + 35)*scaleRatio);
            //g.drawLine((int)(x + 140* scaleRatio), (int)(y + 35*scaleRatio*e-1), connectionPointX, connectionPointY);
            g.drawLine((int)(x + 140* scaleRatio), (int)(y+40*scaleRatio), connectionPointX, (int)(y+40*scaleRatio));
            g.drawImage(match, x, y,(int)(140*scaleRatio),(int)(70*scaleRatio),null); //Displays black box
            g.setFont(font1);
          g.drawString(round1[0][0], (int)(x+5*scaleRatio), (int)(y+20*scaleRatio));
          g.drawString(round1[1][0], (int)(x+5*scaleRatio), (int)(y+60*scaleRatio));
          }
          else if(round1[0][0] == round2[1][0]){
            y = (int) ((90 * (e-1) * gap + 45 * gap) * scaleRatio);
            connectionPointY = (int)((90*((e-1)/2)*biggerGap + 45*biggerGap + 35)*scaleRatio);
            //g.drawLine((int)(x + 140* scaleRatio), (int)(y + 35*scaleRatio*e-1), connectionPointX, connectionPointY);
             g.drawLine((int)(x + 140* scaleRatio), (int)(y+40*scaleRatio), connectionPointX, (int)(y+40*scaleRatio));
            g.drawImage(match, x, y,(int)(140*scaleRatio),(int)(70*scaleRatio),null); //Displays black box
            g.setFont(font1);
          g.drawString(round1[0][0], (int)(x+5*scaleRatio), (int)(y+20*scaleRatio));
          g.drawString(round1[1][0], (int)(x+5*scaleRatio), (int)(y+60*scaleRatio));
          }
          else if(round1[1][0] == round2[1][0]){
            y = (int) ((90 * (e-1) * gap + 45 * gap) * scaleRatio);
            connectionPointY = (int)((90*((e-1)/2)*biggerGap + 45*biggerGap + 35)*scaleRatio);
            //g.drawLine((int)(x + 140* scaleRatio), (int)(y + 35*scaleRatio*e-1), connectionPointX, connectionPointY);
             g.drawLine((int)(x + 140* scaleRatio), (int)(y+40*scaleRatio), connectionPointX, (int)(y+40*scaleRatio));
            g.drawImage(match, x, y,(int)(140*scaleRatio),(int)(70*scaleRatio),null); //Displays black box
            g.setFont(font1);
          g.drawString(round1[0][0], (int)(x+5*scaleRatio), (int)(y+20*scaleRatio));
          g.drawString(round1[1][0], (int)(x+5*scaleRatio), (int)(y+60*scaleRatio));
          }
          else if(round1[1][0] == round2[1][0]){
            y = (int) ((90 * (e-1) * gap + 45 * gap) * scaleRatio);
           connectionPointY = (int)((90*((e-1)/2)*biggerGap + 45*biggerGap + 35)*scaleRatio);
            //g.drawLine((int)(x + 140* scaleRatio), (int)(y + 35*scaleRatio*e-1), connectionPointX, connectionPointY);
            g.drawLine((int)(x + 140* scaleRatio), (int)(y+40*scaleRatio), connectionPointX, (int)(y+40*scaleRatio));
            g.drawImage(match, x, y,(int)(140*scaleRatio),(int)(70*scaleRatio),null); //Displays black box
            g.setFont(font1);
          g.drawString(round1[0][0], (int)(x+5*scaleRatio), (int)(y+20*scaleRatio));
          g.drawString(round1[1][0], (int)(x+5*scaleRatio), (int)(y+60*scaleRatio));
          }
          
          
          
          
          /*if(1 < numOfRounds) {
            g.drawLine((int)(x + 140* scaleRatio), (int)(y + 35*scaleRatio), connectionPointX, connectionPointY);
          }*/
          /*g.setFont(font1);
          g.drawString(round1[0][0], (int)(x+5*scaleRatio), (int)(y+20*scaleRatio));
          g.drawString(round1[1][0], (int)(x+5*scaleRatio), (int)(y+60*scaleRatio));
          update(g);*/
          update(g);
        }
      }
    
    }
    
    public void update(Graphics g) {
      repaint();
    }
    
  }
  
  // -----------  Inner class for the keyboard listener - this detects key presses and runs the corresponding code
  private class MyKeyListener implements KeyListener {
    
    public void keyTyped(KeyEvent e) {
    }
    
    public void keyPressed(KeyEvent e) {
      //System.out.println("keyPressed="+KeyEvent.getKeyText(e.getKeyCode()));
      
      if (KeyEvent.getKeyText(e.getKeyCode()).equals("A")) {  //If 'D' is pressed
        
      } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {  //If ESC is pressed
        System.out.println("Quitting!"); //close frame & quit
        window.dispose();
        
      }
    }
    
    public void keyReleased(KeyEvent e) {
    }
    //end of keyboard listener
  }
}
