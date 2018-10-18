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
    private JFrame thisFrame;
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
        this.thisFrame = this;
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
        thisFrame.dispose();
        thisFrame = new Display(tournament);

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
                        g.setFont(font1);
                        g.drawString(Integer.toString(j), (int)(currentX-30*scaleRatio), (int)(currentY+35*scaleRatio));
                    }


                    //determines whether certain matches exist or not
                    for(int u = 1; u <= tournament.getNumberOfMatchesInRound(i-1); u++) {

                        int connectionPointX = (int) ((50 + 180 * (i - 1)) * scaleRatio);

                        String[][] teams1;

                        teams1 = tournament.getTeamsInMatch(i - 1, u);


                        //checks whether the winner of a previous match can go to the current match, draws accordingly
                        if (teams1 != null && teams1.length > 0) {
                            if (checkTeams(teams[0], teams1)) {
                                g.drawImage(match, connectionPointX, (int)(currentY-nextShift), (int) (140 * scaleRatio), (int) (70 * scaleRatio), null);
                                g.drawLine(currentX, (int)(currentY+(35*scaleRatio)), (int)(connectionPointX+140*scaleRatio), (int)(currentY-nextShift+35*scaleRatio));
                                g.setFont(font1);
                                g.drawString(Integer.toString(u), (int)(connectionPointX-30*scaleRatio), (int)(currentY-nextShift+35*scaleRatio));
                                drawTeams(g, teams1, connectionPointX, (int)(currentY-nextShift), i-1, j);

                            }

                        }
                        if (teams1 != null && teams1.length > 0) {
                            if (checkTeams(teams[1], teams1)) {
                                g.drawImage(match, connectionPointX, (int)(currentY+nextShift), (int) (140 * scaleRatio), (int) (70 * scaleRatio), null);
                                g.drawLine(currentX, (int)(currentY+(35*scaleRatio)), (int)(connectionPointX+140*scaleRatio), (int)(currentY+nextShift+35*scaleRatio));
                                g.setFont(font1);
                                g.drawString(Integer.toString(u), (int)(connectionPointX-30*scaleRatio), (int)(currentY+nextShift+35*scaleRatio));
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

            g.setColor(WHITE);

            double winY = ((tournament.getNumberOfTeams() * 100) + ((tournament.getNumberOfTeams() / 2) * 100))-200;
            double winX = tournament.getNumberOfRounds()*100+400;
            //double winX = 100;

            drawDoubleWinner(g, (int)(winX), (int)(winY), tournament.getTournamentWinner());

            int num = tournament.getNumberOfTeams();
            int lowP = 0;
            int highP = 0;
            int check = 100;
            int checker = 0;
            for (int r = 0; r < tournament.getNumberOfTeams(); r++){
                if ((num - 2^r) < check){
                    check = num - 2^r;
                    lowP = 2^r;
                }
                else if ((num - 2^r) > check){
                    highP = 2^(r+1);
                }

            }

            if (!isPowerOfTwo(tournament.getNumberOfTeams())) {
                if (num - lowP < num - highP) {
                    checker = 1;
                } else if (num - lowP > num - highP) {
                    checker = 2;
                }
            }


             //if (tournament.getNumberOfTeams() == 5) {
             //if (!isPowerOfTwo(tournament.getNumberOfTeams())){
             if (checker == 2){
                //i = round number
                for (int i = tournament.getNumberOfRounds(); i > 1; i--) { //stops at 4

                    Font font1 = new Font("Arial", Font.BOLD, (int) (22 * scaleRatio));
                    g.setFont(font1);

                    //Initialize win and loss counter variables
                    int winC = 0; //num win bracket matches in round - 1
                    int loseC = 0; //num lose bracket in round - 1
                    int currentW = 0;
                    int currentL = 0;

                    //coordinates
                    int xR = i * 200;
                    int xL = xR - 200;
                    int currentX = (xL + xR) / 2;
                    double y1 = 0;
                    double y2 = tournament.getNumberOfTeams() * 100;
                    double hw = y2 - y1;
                    double y3 = y2 + ((tournament.getNumberOfTeams() / 2) * 100);
                    double hl = y3 - y2;

                    //Draws titles and line dividing brackets
                    g.drawLine(0, (int) (y2), (tournament.getNumberOfRounds() * 200)+200, (int) (y2));
                    g.drawString("Winner's Bracket", 50, (int) (y1 + 20));
                    g.drawString("Loser's Bracket", 50, (int) (y2 + 20));

                    //Rounds excluding Round 4
                    if (i > 4) {

                        double currentY = 0; //sets y in winner half

                        //Counts winner bracket match in previous round
                        for (int q = 1; q <= tournament.getNumberOfMatchesInRound(i - 1); q++) {
                            if (tournament.getMatchBracket(i - 1, q) == 0) {
                                winC++;
                            }

                        }
                        //Counts loser bracket match in previous round
                        for (int q = 1; q <= tournament.getNumberOfMatchesInRound(i - 1); q++) {
                            if (tournament.getMatchBracket(i - 1, q) == 1) {
                                loseC++;
                            }

                        }
                        //Counts winner bracket match in current round
                        for (int f = 1; f <= tournament.getNumberOfMatchesInRound(i); f++) { //counts num of win bracket matches
                            if (tournament.getMatchBracket(i, f) == 0) {
                                currentW++;
                            }

                        }
                        //Counts loser bracket match in previous round
                        for (int f = 1; f <= tournament.getNumberOfMatchesInRound(i); f++) {
                            if (tournament.getMatchBracket(i, f) == 1) {
                                currentL++;
                            }

                        }

                        //draws the round number
                        if (currentW > 0 && currentL > 0) {
                            g.drawString("Round " + i, currentX, (int) (y1 + 50));
                            g.drawString("Round " + i, currentX, (int) (y2 + 50));
                        } else if (currentW > 0 && currentL == 0) {
                            g.drawString("Round " + i, currentX, (int) (y1 + 50));
                        } else if (currentW == 0 && currentL > 0) {
                            g.drawString("Round " + i, currentX, (int) (y2 + 50));
                        }

                        int numDraw = tournament.getNumberOfMatchesInRound(i - 1); //counts how many matches that need to be drawn
                        int drawn = 0; //num matches drawn in round (prevents duplicate drawing)

                        //j = match number
                        for (int j = 1; j <= tournament.getNumberOfMatchesInRound(i); j++) {

                            String[][] teams = tournament.getTeamsInMatch(i, j);
                            String[][] teams1;

                            //draw final round
                            if (i == tournament.getNumberOfRounds()) {
                                currentY = ((hw / (winC + 1)) * j); //sets y in winner half
                                g.drawImage(match, (currentX), (int) (currentY), (int) (140 * scaleRatio), (int) (70 * scaleRatio), null);
                                drawDoubleTeams(g, teams, (currentX), (int) (currentY), i - 1, j);
                                g.setFont(font1);
                                g.drawString(Integer.toString(j), currentX + ((int) (140 * scaleRatio) / 2), (int) (currentY - 10));
                            }

                            int yC = 1; //multiplier for y coordinate
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
                                            drawDoubleTeams(g, teams1, currentX - 200, (int) (currentY), i - 1, j);
                                            g.setFont(font1);
                                            g.drawString(Integer.toString(u), currentX - 200 + ((int) (140 * scaleRatio) / 2), (int) (currentY - 10));
                                            drawn++;
                                        }
                                    } else if (checkTeams(teams[1], teams1)) {
                                        if (drawn < numDraw) { //prevents duplicate

                                            g.drawImage(match, currentX - 200, (int) (currentY), (int) (140 * scaleRatio), (int) (70 * scaleRatio), null);

                                            drawDoubleTeams(g, teams1, currentX - 200, (int) (currentY), i - 1, j);
                                            g.setFont(font1);
                                            g.drawString(Integer.toString(u), currentX - 200 + ((int) (140 * scaleRatio) / 2), (int) (currentY - 10));
                                            drawn++;
                                        }

                                    }//end of check teams

                                }//end of check loop

                            }//end of previous round match loop

                        }//end match loop

                    }//end of not round 4 case

                    //Special case for round 4
                    else if (i == 4) {

                        g.drawString("Round 1", currentX - 400, (int) (y1 + 50));
                        g.drawString("Round 2", currentX - 400, (int) (y2 + 50));
                        g.drawString("Round 2", currentX - 200, (int) (y1 + 50));
                        g.drawString("Round 3", currentX - 200, (int) (y2 + 50));
                        g.drawString("Round 4", currentX, (int) (y1 + 50));
                        g.drawString("Round 4", currentX, (int) (y2 + 50));

                        winC = 0;
                        for (int q = 1; q <= tournament.getNumberOfMatchesInRound(i - 2); q++) { //winner bracket before round 3 is round 1
                            if (tournament.getMatchBracket(i - 2, q) == 0) {
                                winC++;
                            }

                        }
                        loseC = 0;
                        for (int q = 1; q <= tournament.getNumberOfMatchesInRound(i - 1); q++) {
                            if (tournament.getMatchBracket(i - 1, q) == 1) {
                                loseC++;
                            }

                        }

                        //g.drawString("Round 1", currentX - 200, (int) (y1 + 50));
                        //g.drawString("Round 2", currentX - 200, (int) (y2 + 50));
                        //g.drawString("Round 3", currentX, (int) (y1 + 50));
                        //g.drawString("Round 3", currentX, (int) (y2 + 50));

                        //j = match number
                        for (int j = 1; j <= tournament.getNumberOfMatchesInRound(i); j++) {

                            String[][] teams = tournament.getTeamsInMatch(i, j);

                            if (tournament.getMatchBracket(i, j) == 0) {

                                //determines whether certain matches exist or not
                                for (int u = 1; u <= tournament.getNumberOfMatchesInRound(2); u++) { //winner bracket round before 3 is 1

                                    if (tournament.getMatchBracket(2, u) == 0) {
                                        //int connectionPointX = (int) ((50 + 180 * (i - 1)) * scaleRatio);
                                        double currentY = ((hw / (winC + 1)) * u);


                                        String[][] teams1;

                                        teams1 = tournament.getTeamsInMatch(2, u);

                                        //checks whether the winner of a previous match can go to the current match, draws accordingly
                                        if (teams1 != null && teams1.length > 0) {
                                            if (checkTeams(teams[0], teams1)) {
                                                g.drawImage(match, currentX - 200, (int) (currentY), (int) (140 * scaleRatio), (int) (70 * scaleRatio), null);
                                                drawDoubleTeams(g, teams1, currentX - 200, (int) (currentY), i - 1, j);
                                                g.setFont(font1);
                                                g.drawString(Integer.toString(u), currentX - 200 + ((int) (140 * scaleRatio) / 2), (int) (currentY - 10));
                                            } else if (checkTeams(teams[1], teams1)) {
                                                g.drawImage(match, currentX - 200, (int) (currentY), (int) (140 * scaleRatio), (int) (70 * scaleRatio), null);
                                                drawDoubleTeams(g, teams1, currentX - 200, (int) (currentY), i - 1, j);
                                                g.setFont(font1);
                                                g.drawString(Integer.toString(u), currentX - 200 + ((int) (140 * scaleRatio) / 2), (int) (currentY - 10));
                                            }

                                        }

                                    }
                                }

                            } //end winner loop

                            else if (tournament.getMatchBracket(i, j) == 1) {

                                //determines whether certain matches exist or not
                                for (int u = 1; u <= tournament.getNumberOfMatchesInRound(3); u++) { //loser bracket round before 3 is 2
                                    double currentY = y2 + ((hl / (loseC + 1)) * u);


                                    String[][] teams1;

                                    teams1 = tournament.getTeamsInMatch(3, u);

                                    //checks whether the winner of a previous match can go to the current match, draws accordingly
                                    if (teams1 != null && teams1.length > 0) {
                                        if (checkTeams(teams[0], teams1)) {
                                            g.drawImage(match, currentX - 200, (int) (currentY), (int) (140 * scaleRatio), (int) (70 * scaleRatio), null);
                                            drawDoubleTeams(g, teams1, currentX - 200, (int) (currentY), i - 1, j);
                                            g.setFont(font1);
                                            g.drawString(Integer.toString(u), currentX - 200 + ((int) (140 * scaleRatio) / 2), (int) (currentY - 10));

                                        } else if (checkTeams(teams[1], teams1)) {
                                            g.drawImage(match, currentX - 200, (int) (currentY), (int) (140 * scaleRatio), (int) (70 * scaleRatio), null);
                                            drawDoubleTeams(g, teams1, currentX - 200, (int) (currentY), i - 1, j);
                                            g.setFont(font1);
                                            g.drawString(Integer.toString(u), currentX - 200 + ((int) (140 * scaleRatio) / 2), (int) (currentY - 10));
                                        }

                                    }

                                }//end of previous round match loop

                            }//end loser loop

                        }//end match loop

                    }//end of 4 case

                    else if (i == 3) {

                        loseC = 0;
                        for (int q = 1; q <= tournament.getNumberOfMatchesInRound(i - 1); q++) {
                            if (tournament.getMatchBracket(i - 1, q) == 1) {
                                loseC++;
                            }

                        }


                        for (int j = 1; j <= tournament.getNumberOfMatchesInRound(i); j++) {
                            String[][] teams = tournament.getTeamsInMatch(i, j);
                            int yC = 1;
                            for (int u = 1; u <= tournament.getNumberOfMatchesInRound(2); u++) { //loser bracket round before 3 is 2

                                if (tournament.getMatchBracket(2, u) == 1) {
                                    double currentY = y2 + ((hl / (loseC + 1)) * yC);
                                    yC++;


                                    String[][] teams1;

                                    teams1 = tournament.getTeamsInMatch(2, u);

                                    //checks whether the winner of a previous match can go to the current match, draws accordingly
                                    if (teams1 != null && teams1.length > 0) {
                                        if (checkTeams(teams[0], teams1)) {
                                            g.drawImage(match, currentX - 200, (int) (currentY), (int) (140 * scaleRatio), (int) (70 * scaleRatio), null);
                                            drawDoubleTeams(g, teams1, currentX - 200, (int) (currentY), i - 1, j);
                                            g.setFont(font1);
                                            g.drawString(Integer.toString(u), currentX - 200 + ((int) (140 * scaleRatio) / 2), (int) (currentY - 10));

                                        } else if (checkTeams(teams[1], teams1)) {
                                            g.drawImage(match, currentX - 200, (int) (currentY), (int) (140 * scaleRatio), (int) (70 * scaleRatio), null);
                                            drawDoubleTeams(g, teams1, currentX - 200, (int) (currentY), i - 1, j);
                                            g.setFont(font1);
                                            g.drawString(Integer.toString(u), currentX - 200 + ((int) (140 * scaleRatio) / 2), (int) (currentY - 10));
                                        }
                                    }
                                }
                            }

                        }//end of previous round match loop
                    }//end of 3 case

                    else if (i == 2) {

                        winC = 0;
                        for (int q = 1; q <= tournament.getNumberOfMatchesInRound(i - 1); q++) {
                            if (tournament.getMatchBracket(i - 1, q) == 0) {
                                winC++;
                            }

                        }


                        for (int j = 1; j <= tournament.getNumberOfMatchesInRound(i); j++) {
                            String[][] teams = tournament.getTeamsInMatch(i, j);
                            for (int u = 1; u <= tournament.getNumberOfMatchesInRound(1); u++) { //loser bracket round before 3 is 2

                                if (tournament.getMatchBracket(1, u) == 0) {
                                    double currentY = y1 + ((hw / (winC + 1)) * u);


                                    String[][] teams1;

                                    teams1 = tournament.getTeamsInMatch(1, u);

                                    //checks whether the winner of a previous match can go to the current match, draws accordingly
                                    if (teams1 != null && teams1.length > 0) {
                                        if (checkTeams(teams[0], teams1)) {
                                            g.drawImage(match, currentX, (int) (currentY), (int) (140 * scaleRatio), (int) (70 * scaleRatio), null);
                                            drawDoubleTeams(g, teams1, currentX, (int) (currentY), i - 1, j);
                                            g.setFont(font1);
                                            g.drawString(Integer.toString(u), currentX + ((int) (140 * scaleRatio) / 2), (int) (currentY - 10));

                                        } else if (checkTeams(teams[1], teams1)) {
                                            g.drawImage(match, currentX, (int) (currentY), (int) (140 * scaleRatio), (int) (70 * scaleRatio), null);
                                            drawDoubleTeams(g, teams1, currentX, (int) (currentY), i - 1, j);
                                            g.setFont(font1);
                                            g.drawString(Integer.toString(u), currentX + ((int) (140 * scaleRatio) / 2), (int) (currentY - 10));
                                        }
                                    }
                                }
                            }

                        }//end of previous round match loop
                    }//end of 2 case

                }//end round loop

            }//end of even teams loop
            else {
                //i = round number
                for (int i = tournament.getNumberOfRounds(); i > 2; i--) { //stops at 3 (i>2)

                    Font font1 = new Font("Arial", Font.BOLD, (int) (22 * scaleRatio));
                    g.setFont(font1);

                    //Initialize win and loss counter variables
                    int winC = 0; //num win bracket matches in round - 1
                    int loseC = 0; //num lose bracket in round - 1
                    int currentW = 0;
                    int currentL = 0;

                    //coordinates
                    int xR = i * 200;
                    int xL = xR - 200;
                    int currentX = (xL + xR) / 2;
                    double y1 = 0;
                    double y2 = tournament.getNumberOfTeams() * 100;
                    double hw = y2 - y1;
                    double y3 = y2 + ((tournament.getNumberOfTeams() / 2) * 100);
                    double hl = y3 - y2;

                    //Draws titles and line dividing brackets
                    g.drawLine(0, (int) (y2), (tournament.getNumberOfRounds() * 200), (int) (y2));
                    g.drawString("Winner's Bracket", 50, (int) (y1 + 20));
                    g.drawString("Loser's Bracket", 50, (int) (y2 + 20));

                    //Rounds excluding Round 3
                    if (i != 3) {

                        double currentY = 0; //sets y in winner half

                        //Counts winner bracket match in previous round
                        for (int q = 1; q <= tournament.getNumberOfMatchesInRound(i - 1); q++) {
                            if (tournament.getMatchBracket(i - 1, q) == 0) {
                                winC++;
                            }

                        }
                        //Counts loser bracket match in previous round
                        for (int q = 1; q <= tournament.getNumberOfMatchesInRound(i - 1); q++) {
                            if (tournament.getMatchBracket(i - 1, q) == 1) {
                                loseC++;
                            }

                        }
                        //Counts winner bracket match in current round
                        for (int f = 1; f <= tournament.getNumberOfMatchesInRound(i); f++) { //counts num of win bracket matches
                            if (tournament.getMatchBracket(i, f) == 0) {
                                currentW++;
                            }

                        }
                        //Counts loser bracket match in previous round
                        for (int f = 1; f <= tournament.getNumberOfMatchesInRound(i); f++) {
                            if (tournament.getMatchBracket(i, f) == 1) {
                                currentL++;
                            }

                        }

                        //draws the round number
                        if (currentW > 0 && currentL > 0) {
                            g.drawString("Round " + i, currentX, (int) (y1 + 50));
                            g.drawString("Round " + i, currentX, (int) (y2 + 50));
                        } else if (currentW > 0 && currentL == 0) {
                            g.drawString("Round " + i, currentX, (int) (y1 + 50));
                        } else if (currentW == 0 && currentL > 0) {
                            g.drawString("Round " + i, currentX, (int) (y2 + 50));
                        }

                        int numDraw = tournament.getNumberOfMatchesInRound(i - 1); //counts how many matches that need to be drawn
                        int drawn = 0; //num matches drawn in round (prevents duplicate drawing)

                        //j = match number
                        for (int j = 1; j <= tournament.getNumberOfMatchesInRound(i); j++) {

                            String[][] teams = tournament.getTeamsInMatch(i, j);
                            String[][] teams1;

                            //draw final round
                            if (i == tournament.getNumberOfRounds()) {
                                currentY = ((hw / (winC + 1)) * j); //sets y in winner half
                                g.drawImage(match, (currentX), (int) (currentY), (int) (140 * scaleRatio), (int) (70 * scaleRatio), null);
                                drawDoubleTeams(g, teams, (currentX), (int) (currentY), i - 1, j);
                                g.setFont(font1);
                                g.drawString(Integer.toString(j), currentX + ((int) (140 * scaleRatio) / 2), (int) (currentY - 10));
                            }

                            int yC = 1; //multiplier for y coordinate
                            //determines whether certain matches exist or not
                            for (int u = 1; u <= tournament.getNumberOfMatchesInRound(i - 1); u++) {

                                teams1 = tournament.getTeamsInMatch(i - 1, u);

                                if (tournament.getMatchBracket(i - 1, u) == 0) { //sets y into win bracket half of screen
                                    currentY = ((hw / (winC + 1)) * u);
                                } else if (tournament.getMatchBracket(i - 1, u) == 1) { //sets y into lose bracket half of screen
                                    currentY = y2 + ((hl / (loseC + 1)) * yC);
                                    yC++;
                                }

                                //positions[i-1][j-1] = currentY;

                                //checks whether the winner of a previous match can go to the current match, draws accordingly
                                if (teams1 != null && teams1.length > 0) {
                                    if (checkTeams(teams[0], teams1)) {

                                        if (drawn < numDraw) { //prevents duplicate
                                            g.drawImage(match, currentX - 200, (int) (currentY), (int) (140 * scaleRatio), (int) (70 * scaleRatio), null);
                                            drawDoubleTeams(g, teams1, currentX - 200, (int) (currentY), i - 1, j);
                                            g.setFont(font1);
                                            g.drawString(Integer.toString(u), currentX - 200 + ((int) (140 * scaleRatio) / 2), (int) (currentY - 10));
                                            drawn++;
                                        }
                                    } else if (checkTeams(teams[1], teams1)) {
                                        if (drawn < numDraw) { //prevents duplicate
                                            g.drawImage(match, currentX - 200, (int) (currentY), (int) (140 * scaleRatio), (int) (70 * scaleRatio), null);
                                            drawDoubleTeams(g, teams1, currentX - 200, (int) (currentY), i - 1, j);
                                            g.setFont(font1);
                                            g.drawString(Integer.toString(u), currentX - 200 + ((int) (140 * scaleRatio) / 2), (int) (currentY - 10));
                                            drawn++;
                                        }

                                    }//end of check teams

                                }//end of check loop

                            }//end of previous round match loop

                        }//end match loop

                    }//end of not round 3 case

                    //Special case for round 3
                    else if (i == 3) {

                        winC = 0;
                        for (int q = 1; q <= tournament.getNumberOfMatchesInRound(i - 2); q++) { //winner bracket before round 3 is round 1
                            if (tournament.getMatchBracket(i - 2, q) == 0) {
                                winC++;
                            }

                        }
                        loseC = 0;
                        for (int q = 1; q <= tournament.getNumberOfMatchesInRound(i - 1); q++) {
                            if (tournament.getMatchBracket(i - 1, q) == 1) {
                                loseC++;
                            }

                        }

                        g.drawString("Round 1", currentX - 200, (int) (y1 + 50));
                        g.drawString("Round 2", currentX - 200, (int) (y2 + 50));
                        g.drawString("Round 3", currentX, (int) (y1 + 50));
                        g.drawString("Round 3", currentX, (int) (y2 + 50));

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
                                            drawDoubleTeams(g, teams1, currentX - 200, (int) (currentY), i - 1, j);
                                            g.setFont(font1);
                                            g.drawString(Integer.toString(u), currentX - 200 + ((int) (140 * scaleRatio) / 2), (int) (currentY - 10));
                                        } else if (checkTeams(teams[1], teams1)) {
                                            g.drawImage(match, currentX - 200, (int) (currentY), (int) (140 * scaleRatio), (int) (70 * scaleRatio), null);
                                            drawDoubleTeams(g, teams1, currentX - 200, (int) (currentY), i - 1, j);
                                            g.setFont(font1);
                                            g.drawString(Integer.toString(u), currentX - 200 + ((int) (140 * scaleRatio) / 2), (int) (currentY - 10));
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
                                            drawDoubleTeams(g, teams1, currentX - 200, (int) (currentY), i - 1, j);
                                            g.setFont(font1);
                                            g.drawString(Integer.toString(u), currentX - 200 + ((int) (140 * scaleRatio) / 2), (int) (currentY - 10));

                                        } else if (checkTeams(teams[1], teams1)) {
                                            g.drawImage(match, currentX - 200, (int) (currentY), (int) (140 * scaleRatio), (int) (70 * scaleRatio), null);
                                            drawDoubleTeams(g, teams1, currentX - 200, (int) (currentY), i - 1, j);
                                            g.setFont(font1);
                                            g.drawString(Integer.toString(u), currentX - 200 + ((int) (140 * scaleRatio) / 2), (int) (currentY - 10));
                                        }

                                    }

                                }//end of previous round match loop

                            }//end loser loop

                        }//end match loop

                    }//end of 3 case

                }//end round loop

            }//end of even teams loop



        }//end of double bracket

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

        private void drawDoubleWinner(Graphics g, int x, int y, String winner) {

            if(winner == null) {
                winner = "TBD";
            }

            Font bigFont = new Font("Arial", Font.BOLD, 28) ;
            g.setFont(bigFont);
            g.drawRect(x, y, 200, 100);
            g.drawString("WINNER", (int)(x),(int)(y-10));
            g.drawString(winner, (int)(x+20),(int)(y+50));

        }

    }
    private static boolean isPowerOfTwo(int number) {

        return number > 0 && ((number & (number - 1)) == 0);

    }

    // -----------  Inner class for the keyboard listener - this detects key presses and runs the corresponding code
}
  