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
        teams.add(new Team("n"));
        teams.add(new Team("o"));
        teams.add(new Team("p"));
        /*teams.add(new Team("q"));
        teams.add(new Team("r"));
        teams.add(new Team("s"));
        teams.add(new Team("t"));
        teams.add(new Team("u"));
        teams.add(new Team("v"));
        teams.add(new Team("w"));
        teams.add(new Team("x"));
        teams.add(new Team("y"));
        teams.add(new Team("z"));
        teams.add(new Team("aa"));
        teams.add(new Team("ab"));
        teams.add(new Team("ac"));
        teams.add(new Team("ad"));
        teams.add(new Team("ae"));
        teams.add(new Team("af"));*/




        SingleGenerator generator = new SingleGenerator(teams, true);


        new Display(generator.getBracket());

    }

}
