import java.util.ArrayList;

public class DisplayTest {

    public static void main(String[] args) {
        ArrayList<Team> teams = new ArrayList();

        teams.add(new Team("1"));
        teams.add(new Team("2"));
        teams.add(new Team("3"));
        teams.add(new Team("4"));
        teams.add(new Team("5"));
        teams.add(new Team("6"));
        teams.add(new Team("7"));
        teams.add(new Team("8"));

        SingleGenerator generator = new SingleGenerator(teams, true);


        new Display2(generator.getBracket());

    }

}
