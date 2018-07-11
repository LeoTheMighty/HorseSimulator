public enum League {
    PONY,
    BEGINNER,
    JUNIOR,
    EASY,
    REGIONAL,
    MEDIUM,
    HORSE,
    COUNTY,
    SENIOR,
    HARD,
    STATE,
    STALLION,
    GRANDSTALLION,
    NATIONAL,
    MASTER,
    GRANDMASTER,
    WORLD,
    UNIVERSE;

    public float maxRating() {
        // oridinal() is the order of the enum value
        // First is 5.0f
        // Then, 10.0f, 20.0f, 40.0f
        float maxRating = 5.0f;
        // TODO I can't remember the exponent java library and I'm lazy
        for (int i = 0; i < ordinal(); i++) {
           maxRating *= 2;
        }
        return maxRating;
    }
}
