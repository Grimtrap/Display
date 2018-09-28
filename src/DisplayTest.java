import java.util.ArrayList;

public class DisplayTest {

    public static void main(String[] args) {
        ArrayList<Team> teams = new ArrayList();

        teams.add(new Team("a"));
        teams.add(new Team("b"));
        teams.add(new Team("c"));
        teams.add(new Team("d"));
        teams.add(new Team("e"));
        teams.add(new Team("f"));
        teams.add(new Team("g"));
        teams.add(new Team("h"));
        teams.add(new Team("i"));
        teams.add(new Team("j"));
        teams.add(new Team("k"));
        teams.add(new Team("l"));
        teams.add(new Team("m"));
       // teams.add(new Team("n"));




        SingleGenerator generator = new SingleGenerator(teams, true);


        new Display2(generator.getBracket());

    }

}
