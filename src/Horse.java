import java.util.Random;

public class Horse implements Comparable<Horse> {
    private Trainer trainer = null;

    private String name;
    private int age;
    private float rating;
    private float volatility;
    private League league;
    private int racesWon;
    private int racesLost;

    private float raceValue = 0.0f;

    public Horse(String name, int age, float rating, float volatility, League league, int racesWon, int racesLost) {
        this.name = name;
        this.age = age;
        this.rating = rating;
        this.volatility = volatility;
        this.league = league;
        this.racesWon = racesWon;
        this.racesLost = racesLost;
    }

    @Override
    public int compareTo(Horse horse) {
        return Float.compare(this.raceValue, horse.raceValue);
    }

    public void setTrainer(Trainer trainer) {
        this.trainer = trainer;
    }

    public String getName() {
        return name;
    }

    public Trainer getTrainer() {
        return trainer;
    }

    public int getAge() {
        return age;
    }

    public float getRating() {
        return rating;
    }

    public float getVolatility() { return volatility; }

    public League getLeague() { return league; }

    public int getRacesWon() {
        return racesWon;
    }

    public int getRacesLost() {
        return racesLost;
    }

    public float getRaceValue() {
        return raceValue;
    }

    public void setRaceValue(float raceValue) {
        this.raceValue = raceValue;
    }

    public void setRandomRaceValue() {
        // raceValue = rating + (volatility * ((new Random()).nextInt(3) - 1));
        raceValue = rating + (volatility * ((3 * new Random().nextFloat()) - 1));
    }

    // You have to be first to win the Race I guess
    // Winning gets a 2 * boost for ratings
    public void win(int wellness) {
        racesWon++;
        updateRatings(2 * wellness);
    }

    public void lose(int wellness) {
        racesLost++;
        updateRatings(wellness);
    }

    public void updateRatings(int wellness) {
        // TODO MAYBE NOT HERE, BUT INCLUDE THE CHANGE OF VOLATILITY
        // Depending on how well they did and their trainer skill, we determine how much their rating improves by
        // Depending on how well they did and their horse rating, their trainer rating also goes up (but less)
        float priorSkillCoefficient = (float)Math.sqrt((double)(trainer.getSkill() * rating));
        rating += wellness * Race.RATING_COEFFICIENT * priorSkillCoefficient;
        rating = Math.min(rating, league.maxRating());
        float trainerSkill = trainer.getSkill();
        trainerSkill += wellness * Race.RATING_COEFFICIENT * 0.25 * priorSkillCoefficient;
        trainer.setSkill(Math.min(trainerSkill, trainer.getLeague().maxRating()));
    }
}
