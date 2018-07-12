public class Bet {
    private User user;
    private Horse horse;
    private float value;
    private float bestReturn;

    public Bet(User user, Horse horse, float value) {
        // value is how much you pay to do the bet
        this.user = user;
        this.horse = horse;
        this.value = value;
        this.bestReturn = horseBestReturn(horse);

        // You also take the value from the user
        user.takeMoney(value);
    }

    // Cash, the verb lol
    public void cashBet(int place, int numRacers) {
        // The value is proportional to the place
        // If place == 0, then give bestReturn, else give proportional so that last is zero
        float cashReturn = bestReturn * ((numRacers - (place + 1)) / (numRacers - 1));
        user.giveMoney(cashReturn);
    }

    public Horse getHorse() {
        return horse;
    }

    public float getValue() {
        return value;
    }

    public User getUser() {
        return user;
    }

    // TODO Consider making this not static
    public static float horseBestReturn(Horse horse) {
        // The max return is a multiplication value
        // i.e if 1.2 returned, then for 1st place you get 1.2 * valueOfBet
        // This is based off the rating and the league maxRating (inversely proportional)
        float rating = horse.getRating();
        float maxRating = horse.getLeague().maxRating();
        float bestReturn = maxRating / rating;
        if (bestReturn < 1.2f) {
            bestReturn = 1.2f;
        }
        return bestReturn;
    }
}
