public class Bracket {

    public int getNumberOfTeams() {
        return 16;
    }

    public int getNumberOfRounds() {
        return 6;
    }

    public int getNumOfMatchesInRound(int round) {
        return (int) (16/Math.pow(2,round));
    }

    public String[] getTeamsInMatch(int round, int matchNumber) {
        String[] teams = {"aaahfhhh", "fheff"};
        return teams;
    }
}
