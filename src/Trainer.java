import java.util.ArrayList;

public class Trainer {
    private String name;
    private int age;
    private League league;

    // This is out of league.maxRating()
    private float skill;

    public ArrayList<Horse> ownedHorses = new ArrayList<>();

    public Trainer(String name, int age, float skill, League league) {
        this.name = name;
        this.age = age;
        this.skill = skill;
        this.league = league;
    }

    public void addHorse(Horse horse) {
        ownedHorses.add(horse);
    }

    public void removeHorse(Horse horse) {
        ownedHorses.remove(horse);
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public float getSkill() { return skill; }

    public League getLeague() { return league; }

    public void setSkill(float skill) { this.skill = skill; }
}
