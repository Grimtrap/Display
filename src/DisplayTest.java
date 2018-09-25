import java.util.ArrayList;

public class DisplayTest {

    public static void main(String[] args) {
        ArrayList<Team> teams = new ArrayList();

        teams.add(new Team("good"));
        teams.add(new Team("bad"));
        teams.add(new Team("dsfd"));
        teams.add(new Team("ooooo"));
        teams.add(new Team("gooddddddddd"));
        teams.add(new Team("badddddd"));
        teams.add(new Team("ooooo"));
        teams.add(new Team("gooddddddddd"));
        teams.add(new Team("badddddd"));
        teams.add(new Team("dsfssssd"));
        teams.add(new Team("dsfssssd"));

        SingleGenerator generator = new SingleGenerator(teams, true);


        new Display(generator.getBracket());

    }

}
