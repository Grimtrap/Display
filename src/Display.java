/*
Display.java
A class that displays tournament brackets
@author Eric Ke, Kyle To
9/24/2018
*/

//graphics imports
import javax.swing.*;
import java.awt.*;

//keyboard imports

import static java.awt.Color.WHITE;


public class Display extends JFrame {

    //class variables
    private JPanel displayPanel;
    private Bracket tournament;
    private static double scaleRatio;
    private static double heightMultiplier = 1;
    private static double widthMultiplier = 0;



    /**
     * Constructor for the display for the tournament
     * @param tournament a generated bracket
     */
    public Display(Bracket tournament) {
        super("Tournament Bracket");

        this.tournament = tournament;

        scaleRatio = (Toolkit.getDefaultToolkit().getScreenSize().width / 1920.0); //scale ratio of the screen so it's compatible with other screens



        // Set the frame to full screen
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);


        //height multiplier for more teams
        if(tournament.getNumberOfRounds() < 4) {
            heightMultiplier = 1;
        } else if (tournament instanceof SingleBracket){
            heightMultiplier = Math.pow(2, tournament.getNumberOfRounds()-4);
        } else {
            heightMultiplier = tournament.getNumberOfRounds(); //double bracket scaling
        }

        //width multiplier for more teams
        if(tournament.getNumberOfRounds() > 5) {
            widthMultiplier = scaleRatio*180*(tournament.getNumberOfRounds()-5);
        }



        this.setSize((int)(Toolkit.getDefaultToolkit().getScreenSize().getWidth()* + widthMultiplier), (int)(Toolkit.getDefaultToolkit().getScreenSize().getHeight()* heightMultiplier));
        this.setPreferredSize(new Dimension((int)(Toolkit.getDefaultToolkit().getScreenSize().getWidth()+ widthMultiplier), (int)(Toolkit.getDefaultToolkit().getScreenSize().getHeight()* heightMultiplier)));
        //frame.setResizable(false);


        //Set up the game panel (where we put our graphics)
        displayPanel = new DisplayPanel();
        displayPanel.setBackground(new Color(10, 10, 10, 255));
        displayPanel.setPreferredSize(new Dimension((int)(Toolkit.getDefaultToolkit().getScreenSize().getWidth()+ widthMultiplier), (int)(Toolkit.getDefaultToolkit().getScreenSize().getHeight()* heightMultiplier)));
        this.pack();
        //scroll bar
        JScrollPane scroll = new JScrollPane(displayPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        this.add(scroll);

        this.requestFocusInWindow(); //make sure the frame has focus

        this.setVisible(true);


    }

    /**
     * updates the screen
     *
     * @param tournament the tournament bracket
     */
    public void update(Bracket tournament) {
        this.dispose();
        new Display(tournament);

    }

    private int getMaxNumOfMatches() {

        int maxRounds = 0;

        for(int i = 0; i < tournament.getNumberOfRounds(); i++) {
            try {
                if (tournament.getNumberOfMatchesInRound(i) > maxRounds)
                    maxRounds = tournament.getNumberOfMatchesInRound(i);
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
        return maxRounds;
    }


    /**
     * --------- INNER CLASSES -------------
     **/

    // Inner class for the the game area - This is where all the drawing of the screen occurs
    private class DisplayPanel extends JPanel {


        /**
         * draws the tournament graphics on the screen
         * @param g graphics
         */
        public void paintComponent(Graphics g) {

            if(tournament instanceof SingleBracket) {
                drawSingleBracket(g);
            } else {
                drawDoubleBracket(g);
            }
            //check for collision

            //repaint
        }

        /**
         * checks whether a team from a previous match has the possibility to advance
         * is for the purpose of drawing brackets in the correct positions
         * @param team1 the list of teams to check against the other, from the current match
         * @param team2 the list of teams from a match from a previous round
         * @return whether or not a match should be drawn
         */
        private boolean checkTeams(String[] team1, String[][] team2) {

            for(int i = 0; i < team1.length; i++) {
                String current = team1[i];
                for(int j = 0; j < team2.length; j++) {
                    for(int k = 0; k < team2[j].length; k++) {
                        String comparison = team2[j][k];
                        if (current.equalsIgnoreCase(comparison)) { return true; }
                    }

                }
            }
            return false;

        }


        /**
         * draws the tournament bracket on the screen for single brackets
         * @param g paintComponent graphics
         */
        private void drawSingleBracket(Graphics g) {

            super.paintComponent(g);
            setDoubleBuffered(true);

            Image match = new ImageIcon("resources/match.png").getImage();

            double center = (Toolkit.getDefaultToolkit().getScreenSize().getHeight()/2)* heightMultiplier - 140*scaleRatio;


            g.setColor(WHITE);
            //i = round number

            drawWinner(g, ((int)((50+180*(tournament.getNumberOfRounds()+1))*scaleRatio)), (int)center, tournament.getTournamentWinner());

            for (int i = tournament.getNumberOfRounds(); i > 0; i--) {
                int currentX = (int)((50 + 180 * (i)) * scaleRatio);

                Font font1 = new Font("Arial", Font.BOLD, (int)(22*scaleRatio));
                g.setFont(font1);
                g.drawString("Round " + i, currentX, (int)(70*scaleRatio));

                //j = match number
                for (int j = 1; j <= tournament.getNumberOfMatchesInRound(i); j++) {

                    String[][] teams = tournament.getTeamsInMatch(i, j);

                    //coordinates
                    double baseY = (center/Math.pow(2, tournament.getNumberOfRounds() - i));
                    double gap = (center/Math.pow(2, tournament.getNumberOfRounds() - i -1));
                    double currentY = (baseY + (j-1)*gap + 140*scaleRatio);
                    double nextShift = baseY/2;


                    //draw final round
                    if (i == tournament.getNumberOfRounds()) {
                        g.drawImage(match, (currentX), (int) (currentY), (int) (140 * scaleRatio), (int) (70 * scaleRatio), null);
                        drawTeams(g, teams, (currentX), (int) (currentY), i-1, j);
                    }


                    //determines whether certain matches exist or not
                    for(int u = 1; u <= tournament.getNumberOfMatchesInRound(i-1); u++) {

                        int connectionPointX = (int) ((50 + 180 * (i - 1)) * scaleRatio);

                        String[][] teams1;

                        teams1 = tournament.getTeamsInMatch(i - 1, u);

                        //draws the final bracket


                        //checks whether the winner of a previous match can go to the current match, draws accordingly
                            if (teams1 != null && teams1.length > 0) {
                                if (checkTeams(teams[0], teams1)) {
                                    g.drawImage(match, connectionPointX, (int)(currentY-nextShift), (int) (140 * scaleRatio), (int) (70 * scaleRatio), null);
                                    g.drawLine(currentX, (int)(currentY+(35*scaleRatio)), (int)(connectionPointX+140*scaleRatio), (int)(currentY-nextShift+35*scaleRatio));
                                    drawTeams(g, teams1, connectionPointX, (int)(currentY-nextShift), i-1, j);

                                }

                            }
                            if (teams1 != null && teams1.length > 0) {
                                if (checkTeams(teams[1], teams1)) {
                                    g.drawImage(match, connectionPointX, (int)(currentY+nextShift), (int) (140 * scaleRatio), (int) (70 * scaleRatio), null);
                                    g.drawLine(currentX, (int)(currentY+(35*scaleRatio)), (int)(connectionPointX+140*scaleRatio), (int)(currentY+nextShift+35*scaleRatio));

                                    drawTeams(g, teams1, connectionPointX, (int)(currentY+nextShift), i-1 ,j);
                                }

                            }

                    }

                }

            }
        }

        /**
         *
         * @param g paintComponent graphics
         * @param teams the teams that will be drawn
         * @param x the x coordinate
         * @param y the y coordinate
         */
        private void drawTeams(Graphics g, String[][] teams, int x, int y, int round, int match) {

            Font font = new Font("Arial", Font.PLAIN, (int)(16*scaleRatio));
            g.setFont(font);
            g.setColor(WHITE);
            for (int i = 0; i< teams.length; i++) {
               if(teams[i].length == 1) {
                    g.drawString(teams[i][0], (int)(x+15*scaleRatio), (int)(y+(25+35*i)*scaleRatio));
                } else {
                   try { //display previous match winner in the correct position
                       g.drawString(((SingleBracket) (tournament)).getMatchWinner(round, match*2 -(1-i)), (int) (x + 15 * scaleRatio), (int) (y + (25 + 35 * i) * scaleRatio));
                   } catch (Exception e) {
                       g.drawString("TBD", (int) (x + 15 * scaleRatio), (int) (y + (25 + 35 * i) * scaleRatio));
                   }
               }
            }

        }


        private void drawDoubleBracket(Graphics g) {

            super.paintComponent(g);
            setDoubleBuffered(true);

            Image match = new ImageIcon("resources/match.png").getImage();

            //double center = (Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 2) * heightMultiplier - 140 * scaleRatio;


            g.setColor(WHITE);

            drawWinner(g, ((tournament.getNumberOfRounds() * 200) + 200), (tournament.getNumberOfTeams()*200)/2, tournament.getTournamentWinner());
            //double preWY = 0;
            //double preLY = 0;
            double[] preWY = new double[tournament.getNumberOfTeams()];
            double[] preLY = new double[tournament.getNumberOfTeams()];
            double[] curWY = new double[tournament.getNumberOfTeams()];
            double[] curLY = new double[tournament.getNumberOfTeams()];
            //int i = 4;
            //i = round number
            for (int i = tournament.getNumberOfRounds(); i > 2; i--) {
            //for (int i = tournament.getNumberOfRounds(); i > 3; i--) {

                Font font1 = new Font("Arial", Font.BOLD, (int) (22 * scaleRatio));
                g.setFont(font1);
                //g.drawString("Round " + i, currentX, (int)(70*scaleRatio));
                int winC = 0; //num win bracket matches in round - 1
                int loseC = 0; //num lose bracket in round - 1
                int currentW = 0;
                int currentL = 0;



                //coordinates
                int xR = i * 200;
                int xL = xR - 200;
                int currentX = (xL + xR) / 2;
                double y1 = 0;
                double y2 = tournament.getNumberOfTeams() * 200;
                double hw = y2 - y1;
                double y3 = y2 + ((tournament.getNumberOfTeams() / 2) * 200);
                double hl = y3 - y2;


                if (i != 3) { //if round is not 3 (3 has special case

                    for (int q = 1; q <= tournament.getNumberOfMatchesInRound(i-1); q++) { //counts num of win bracket matches
                        if (tournament.getMatchBracket(i-1, q) == 0) {
                            winC++;
                        }

                    }
                    for (int q = 1; q <= tournament.getNumberOfMatchesInRound(i-1); q++) {
                        if (tournament.getMatchBracket(i-1, q) == 1) {
                            loseC++;
                        }

                    }

                    /*for (int f = 1; f <= tournament.getNumberOfMatchesInRound(i); f++) { //counts num of win bracket matches
                        if (tournament.getMatchBracket(i-1, f) == 0) {
                            currentW++;
                        }

                    }*/
                    for (int f = 1; f <= tournament.getNumberOfMatchesInRound(i); f++) {
                        if (tournament.getMatchBracket(i, f) == 1) {
                            currentL++;
                        }

                    }

                    double yy2=0;
                    //j = match number
                    int numDraw = tournament.getNumberOfMatchesInRound(i-1); //counts how many matches that need to be drawn
                    int drawn = 0; //num matches drawn in round (prevents duplicate drawing)

                    preWY = curWY;
                    preLY = curLY;
                    for (int j = 1; j <= tournament.getNumberOfMatchesInRound(i); j++) {

                        String[][] teams = tournament.getTeamsInMatch(i, j);
                        String[][] teams1;

                        if (tournament.getMatchBracket(i, j) == 0) { //if winner bracket

                            //coordinates
                            double currentY = 0; //sets y in winner half


                            //draw final round
                            if (i == tournament.getNumberOfRounds()) {
                                currentY = ((hw / (winC + 1)) * j); //sets y in winner half
                                g.drawImage(match, (currentX), (int) (currentY), (int) (140 * scaleRatio), (int) (70 * scaleRatio), null);
                                String test = "Round " + i + " Match " + j;
                                drawTeams(g, teams, (currentX), (int) (currentY), i - 1, j);
                                g.drawString(test, currentX, (int) (currentY));
                                preWY[j-1] = currentY;
                            }

                            int yC = 1;
                            //determines whether certain matches exist or not
                            for (int u = 1; u <= tournament.getNumberOfMatchesInRound(i - 1); u++) {


                                teams1 = tournament.getTeamsInMatch(i - 1, u);

                                if (tournament.getMatchBracket(i - 1, u) == 0) { //sets y into win bracket half of screen
                                    currentY = ((hw / (winC + 1)) * u);
                                } else if (tournament.getMatchBracket(i - 1, u) == 1) { //sets y into lose bracket half of screen
                                    currentY = y2 + ((hl / (loseC + 1)) * yC);
                                    yC++;
                                }

                                //checks whether the winner of a previous match can go to the current match, draws accordingly
                                if (teams1 != null && teams1.length > 0) {
                                    if (checkTeams(teams[0], teams1)) {

                                        if (drawn < numDraw) { //prevents duplicate
                                            g.drawImage(match, currentX - 200, (int) (currentY), (int) (140 * scaleRatio), (int) (70 * scaleRatio), null);
                                            if (tournament.getMatchBracket(i-1, u) == 0) { //sets y into win bracket half of screen
                                                yy2 = preWY[u-1];
                                            } else if (tournament.getMatchBracket(i - 1, u) == 1) { //sets y into lose bracket half of screen
                                                yy2  = preLY[(u - winC) - 1];
                                            }
                                            if (tournament.getMatchBracket(i, j) == tournament.getMatchBracket(i-1, u)) {
                                                g.drawLine((currentX - 200) + (int) ((140 * scaleRatio)), (int) (currentY + (70 * scaleRatio) / 2), (currentX), (int) (yy2 + (70 * scaleRatio) / 2));
                                            }
                                            drawDoubleTeams(g, teams1, currentX - 200, (int) (currentY), i - 1, j);
                                            String test = "Round " + (i - 1) + " Match " + u;
                                            g.drawString(test, currentX - 200, (int) (currentY));
                                            drawn++;
                                            if (tournament.getMatchBracket(i - 1, u) == 0) { //sets y into win bracket half of screen
                                                for (int p = 0; p < winC; p++) {
                                                    curWY[p] = currentY;
                                                }
                                            } else if (tournament.getMatchBracket(i - 1, u) == 1) { //sets y into lose bracket half of screen
                                                for(int k = 0; k < loseC;k++) {
                                                    curLY[k] = currentY;
                                                }
                                            }
                                            //preWY[u-1] = currentY;
                                        }
                                    } else if (checkTeams(teams[1], teams1)) {
                                        if (drawn < numDraw) { //prevents duplicate

                                            g.drawImage(match, currentX - 200, (int) (currentY), (int) (140 * scaleRatio), (int) (70 * scaleRatio), null);
                                            if (tournament.getMatchBracket(i-1, u) == 0) { //sets y into win bracket half of screen
                                                yy2 = preWY[u-1];
                                            } else if (tournament.getMatchBracket(i - 1, u) == 1) { //sets y into lose bracket half of screen
                                                yy2  = preLY[(u - winC) - 1];
                                            }
                                            if (tournament.getMatchBracket(i, j) == tournament.getMatchBracket(i-1, u)) {
                                                g.drawLine((currentX - 200) + (int) ((140 * scaleRatio)), (int) (currentY + (70 * scaleRatio) / 2), (currentX), (int) (yy2 + (70 * scaleRatio) / 2));
                                            }

                                            drawDoubleTeams(g, teams1, currentX - 200, (int) (currentY), i - 1, j);
                                            String test = "Round " + (i - 1) + " Match " + u;
                                            g.drawString(test, currentX - 200, (int) (currentY));
                                            drawn++;
                                            if (tournament.getMatchBracket(i - 1, u) == 0) { //sets y into win bracket half of screen
                                                for (int p = 0; p < winC; p++) {
                                                    curWY[p] = currentY;
                                                }
                                            } else if (tournament.getMatchBracket(i - 1, u) == 1) { //sets y into lose bracket half of screen
                                                for(int k = 0; k < loseC;k++) {
                                                    curLY[k] = currentY;
                                                }
                                            }

                                        }
                                    }

                                }

                            }

                        } //end winner loop

                        else if (tournament.getMatchBracket(i, j) == 1) { //if loser bracket

                            //coordinates
                            double currentY = 0; //sets y in loser bracket half

                            int yC = 1;
                            //determines whether certain matches exist or not
                            for (int u = 1; u <= tournament.getNumberOfMatchesInRound(i - 1); u++) {


                                teams1 = tournament.getTeamsInMatch(i - 1, u);



                                if (tournament.getMatchBracket(i - 1, u) == 0) { //same as above
                                    currentY = ((hw / (winC + 1)) * u);
                                } else if (tournament.getMatchBracket(i - 1, u) == 1) { //same as above
                                    currentY = y2 + ((hl / (loseC + 1)) * yC);
                                    yC++;
                                }

                                //checks whether the winner of a previous match can go to the current match, draws accordingly
                                if (teams1 != null && teams1.length > 0) {
                                    if (checkTeams(teams[0], teams1)) {
                                        if (tournament.getMatchBracket(i-1, u) == 0) { //sets y into win bracket half of screen
                                            yy2 = preWY[u-1];
                                        } else if (tournament.getMatchBracket(i - 1, u) == 1) { //sets y into lose bracket half of screen
                                            yy2  = preLY[(u - winC) - 1];
                                        }
                                        if (tournament.getMatchBracket(i, j) == tournament.getMatchBracket(i-1, u)) {
                                            g.drawLine((currentX - 200) + (int) ((140 * scaleRatio)), (int) (currentY + (70 * scaleRatio) / 2), (currentX), (int) (yy2 + (70 * scaleRatio) / 2));
                                        }
                                        if (drawn < numDraw) { //same as above
                                            g.drawImage(match, currentX - 200, (int) (currentY), (int) (140 * scaleRatio), (int) (70 * scaleRatio), null);
                                            drawDoubleTeams(g, teams1, currentX - 200, (int) (currentY), i - 1, j);
                                            String test = "Round " + (i - 1) + " Match " + u;
                                            g.drawString(test, currentX - 200, (int) (currentY));
                                            drawn++;
                                            if (tournament.getMatchBracket(i - 1, u) == 0) { //sets y into win bracket half of screen
                                                for (int p = 0; p < winC; p++) {
                                                    curWY[p] = currentY;
                                                }
                                            } else if (tournament.getMatchBracket(i - 1, u) == 1) { //sets y into lose bracket half of screen
                                                for(int k = 0; k < loseC;k++) {
                                                    curLY[k]  = currentY;
                                                }
                                            }

                                        }

                                    } else if (checkTeams(teams[1], teams1)) {
                                        if (tournament.getMatchBracket(i-1, u) == 0) { //sets y into win bracket half of screen
                                            yy2 = preWY[u-1];
                                        } else if (tournament.getMatchBracket(i - 1, u) == 1) { //sets y into lose bracket half of screen
                                            yy2  = preLY[(u - winC) - 1];
                                        }
                                        if (tournament.getMatchBracket(i, j) == tournament.getMatchBracket(i-1, u)) {
                                            g.drawLine((currentX - 200) + (int) ((140 * scaleRatio)), (int) (currentY + (70 * scaleRatio) / 2), (currentX), (int) (yy2 + (70 * scaleRatio) / 2));
                                        }
                                        if (drawn < numDraw) { //same as above
                                            g.drawImage(match, currentX - 200, (int) (currentY), (int) (140 * scaleRatio), (int) (70 * scaleRatio), null);

                                            //g.drawLine((currentX-200) - (int) (140 * scaleRatio), (int) (currentY), (currentX), (int) (preLY));
                                            drawDoubleTeams(g, teams1, currentX - 200, (int) (currentY), i - 1, j);
                                            String test = "Round " + (i - 1) + " Match " + u;
                                            g.drawString(test, currentX - 200, (int) (currentY));
                                            drawn++;
                                            if (tournament.getMatchBracket(i - 1, u) == 0) { //sets y into win bracket half of screen
                                                for (int p = 0; p < winC; p++) {
                                                    curWY[p] = currentY;
                                                }
                                            } else if (tournament.getMatchBracket(i - 1, u) == 1) { //sets y into lose bracket half of screen
                                                for(int k = 0; k < loseC;k++) {
                                                    curLY[k]  = currentY;
                                                }
                                            }
                                        }
                                    }

                                }

                            }

                        } //end loser loop


                    } //end match loop
                }//end of not 3 loop

                else if (i == 3) { //special case of 3

                    winC = 0;
                    for (int q = 1; q <= tournament.getNumberOfMatchesInRound(i-2); q++) { //winner bracket before round 3 is round 1
                        if (tournament.getMatchBracket(i-2, q) == 0) {
                            winC++;
                        }

                    }
                    for (int q = 1; q <= tournament.getNumberOfMatchesInRound(i-1); q++) {
                        if (tournament.getMatchBracket(i-1, q) == 1) {
                            loseC++;
                        }

                    }

                    //j = match number
                    for (int j = 1; j <= tournament.getNumberOfMatchesInRound(i); j++) {

                        String[][] teams = tournament.getTeamsInMatch(i, j);

                        if (tournament.getMatchBracket(i, j) == 0) {

                            //determines whether certain matches exist or not
                            for (int u = 1; u <= tournament.getNumberOfMatchesInRound(1); u++) { //winner bracket round before 3 is 1

                                //int connectionPointX = (int) ((50 + 180 * (i - 1)) * scaleRatio);
                                double currentY = ((hw / (winC + 1)) * u);


                                String[][] teams1;

                                teams1 = tournament.getTeamsInMatch(1, u);

                                //checks whether the winner of a previous match can go to the current match, draws accordingly
                                if (teams1 != null && teams1.length > 0) {
                                    if (checkTeams(teams[0], teams1)) {
                                        g.drawImage(match, currentX - 200, (int) (currentY), (int) (140 * scaleRatio), (int) (70 * scaleRatio), null);
                                        //g.drawLine((currentX-200) - (int) (140 * scaleRatio), (int) (currentY), (currentX), (int) (preWY));
                                        drawDoubleTeams(g, teams1, currentX - 200, (int) (currentY), i - 1, j);
                                        String test = "Round " + (1) + " Match " + u;
                                        g.drawString(test, currentX - 200, (int) (currentY));
                                        //preWY = currentY;
                                    } else if (checkTeams(teams[1], teams1)) {
                                        g.drawImage(match, currentX - 200, (int) (currentY), (int) (140 * scaleRatio), (int) (70 * scaleRatio), null);
                                        //g.drawLine((currentX-200) - (int) (140 * scaleRatio), (int) (currentY), (currentX), (int) (preWY));
                                        drawDoubleTeams(g, teams1, currentX - 200, (int) (currentY), i - 1, j);
                                        String test = "Round " + (1) + " Match " + u;
                                        g.drawString(test, currentX - 200, (int) (currentY));
                                        //preWY = currentY;
                                    }

                                }

                            }

                        } //end winner loop

                        else if (tournament.getMatchBracket(i, j) == 1) {

                            //determines whether certain matches exist or not
                            for (int u = 1; u <= tournament.getNumberOfMatchesInRound(2); u++) { //loser bracket round before 3 is 2
                                double currentY = y2 + ((hl / (loseC + 1)) * u);


                                String[][] teams1;

                                teams1 = tournament.getTeamsInMatch(2, u);

                                //checks whether the winner of a previous match can go to the current match, draws accordingly
                                if (teams1 != null && teams1.length > 0) {
                                    if (checkTeams(teams[0], teams1)) {
                                        g.drawImage(match, currentX - 200, (int) (currentY), (int) (140 * scaleRatio), (int) (70 * scaleRatio), null);
                                        //g.drawLine((currentX-200) - (int) (140 * scaleRatio), (int) (currentY), (currentX), (int) (preLY));
                                        drawDoubleTeams(g, teams1, currentX - 200, (int) (currentY), i - 1, j);
                                        String test = "Round " + (2) + " Match " + u;
                                        g.drawString(test, currentX - 200, (int) (currentY));
                                        //preLY = currentY;

                                    } else if (checkTeams(teams[1], teams1)) {
                                        g.drawImage(match, currentX - 200, (int) (currentY), (int) (140 * scaleRatio), (int) (70 * scaleRatio), null);
                                        //g.drawLine((currentX-200) - (int) (140 * scaleRatio), (int) (currentY), (currentX), (int) (preLY));
                                        drawDoubleTeams(g, teams1, currentX - 200, (int) (currentY), i - 1, j);
                                        String test = "Round " + (2) + " Match " + u;
                                        g.drawString(test, currentX - 200, (int) (currentY ));
                                        //preLY = currentY;
                                    }

                                }

                            }

                        } //end loser loop


                    } //end match loop
                }

            }//end round loop
        }

        private void drawDoubleTeams(Graphics g, String[][] teams, int x, int y, int round, int match) {

            Font font = new Font("Arial", Font.PLAIN, (int)(16*scaleRatio));
            g.setFont(font);
            g.setColor(WHITE);
            for (int i = 0; i< teams.length; i++) {
                if(teams[i].length == 1) {
                    g.drawString(teams[i][0], (int)(x+15*scaleRatio), (int)(y+(25+35*i)*scaleRatio));
                    //g.drawString(teams[i][0], x, y);
                } else {
                    try { //display previous match winner in the correct position
                        g.drawString(((DoubleBracket) (tournament)).getWinnerOfMatch(round, match*2 -(1-i)), (int) (x + 15 * scaleRatio), (int) (y + (25 + 35 * i) * scaleRatio));
                    } catch (Exception e) {
                        g.drawString("TBD", (int) (x + 15 * scaleRatio), (int) (y + (25 + 35 * i) * scaleRatio));
                        //g.drawString("TBD", x, y);
                    }
                }
            }

        }



        private void drawWinner(Graphics g, int x, int y, String winner) {

            if(winner == null) {
                winner = "TBD";
            }

            Font bigFont = new Font("Arial", Font.BOLD, 28) ;
            g.setFont(bigFont);
            g.drawRect(x,(int)(y+100*scaleRatio), (int)(400*scaleRatio), (int)(150*scaleRatio));
            g.drawString("WINNER", (int)(x+50*scaleRatio),(int)(y+50*scaleRatio));
            g.drawString(winner, (int)(x+50*scaleRatio),(int)(y+200*scaleRatio));

        }

    }

    // -----------  Inner class for the keyboard listener - this detects key presses and runs the corresponding code
}
  