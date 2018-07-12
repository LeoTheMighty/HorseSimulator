import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

public class Race {
    // This determines how much the ratings change because of
    public static final float RATING_COEFFICIENT = 0.1f;

    private Random random = new Random();

    // This is us
    private User currentUser;

    // Take this from the Simulation class
    private ArrayList<User> computerUsers;
    private HashMap<String, Horse> horseHashMap;

    // idk how many horses are usually in races so here it is
    private int numRacersPerRace = 6;
    private ArrayList<Horse> racers;
    private ArrayList<Bet> bets;

    private boolean ifInside = true;

    private BufferedReader console = new BufferedReader(new InputStreamReader(System.in));

    private League league = League.PONY;

    private boolean ifAdmin;

    public Race(User currentUser, HashMap<String, Horse> horseHashMap, ArrayList<User> computerUsers, boolean ifAdmin) {
        // TODO INCLUDE SOMETHING ABOUT LEAGUES IN THE RACE
        this.ifAdmin = ifAdmin;
        this.currentUser = currentUser;
        this.horseHashMap = horseHashMap;
        this.computerUsers = computerUsers;
        racers = new ArrayList<>();
        bets = new ArrayList<>();
        prepareNewRace();
    }

    private void prepareNewRace() {
        // This chooses random `numRacersPerRace` horses to be in the race
        int numHorses = horseHashMap.size();
        HashSet<Integer> horseNumberSet = new HashSet<>();
        for (int i = 0; i < numRacersPerRace; i++) {
            int r;
            // SAVAGE empty bodied while loop
            while (horseNumberSet.contains((r = (random.nextInt(numHorses)))));
            horseNumberSet.add(r);
        }
        int i = 0;
        // TODO Only horses in the league of the race can participate
        for (Horse horse : horseHashMap.values()) {
            if (horseNumberSet.contains(i)) {
                racers.add(horse);
            }
            i++;
        }

        // bets = new ArrayList<>();
        // Take bets from the computer players
        // Take a percentage of the money
        for (User computerUser : computerUsers) {
            // nextFloat gives float 0.0f-1.0f
            // Bet no more than 0.25
            // TODO create an algorithm that smartly bets
            // TODO Calculate the expected value
            float percentageBet = (random.nextFloat() / 4);
            Horse randomHorse = racers.get((new Random()).nextInt(racers.size()));
            Bet bet = new Bet(computerUser, randomHorse, computerUser.getMoney() * percentageBet);
            if (ifAdmin) {
                System.out.println(bet.getUser().getName() + " bets " + bet.getValue() + " on " + randomHorse.getName() + " (rating: " + randomHorse.getRating() + ")");
            }
            bets.add(bet);
        }
    }

    public void enter() {
       System.out.println("Welcome to the horse Races!");

       ifInside = true;
       while (ifInside) {
           mainDialogue();
       }

       System.out.println("Hope you had a fun time, come back soon.!");
       // TODO Consider enter to continue
    }

    public void mainDialogue() {

        System.out.println("For the next race we have the following horses competing...");
        int i = 0;
        for (Horse horse : racers) {
            i++;
            System.out.println(Integer.toString(i) + ". " + horse.getName() + "; " + Integer.toString(horse.getAge()) + " years old");
            System.out.println("    Trainer: " + horse.getTrainer().getName());
        }
        System.out.println();

        // TODO Choices of what to do
        System.out.println("Here's what you can do:");
        System.out.println("    1. Watch the race go on.");
        System.out.println("    2. Make a bet on a horse");
        System.out.println("    3. Hear the latest gossip");
        System.out.println("    4. Go back to the main lobby");

        boolean ifCorrectlyResponded = false;
        int numResponse;
        while (!ifCorrectlyResponded) {
            try {
                numResponse = Integer.parseInt(console.readLine());
            }
            catch (IOException e) {
                e.printStackTrace();
                System.err.println("ERROR IN RACE MAIN WHILE");
                continue;
            }

            ifCorrectlyResponded = true;
            switch (numResponse) {
                // TODO implement these
                case 1:
                    // Watch the race!
                    runTheRace();
                    prepareNewRace();
                    break;
                case 2:
                    // Bet on a horse
                    betDialgoue();
                    break;
                case 3:
                    // Gossip
                    // TODO Create a gossip corner where you can hear about all of the horses and trainers
                case 4:
                    // Leave
                    ifInside = false;
                default:
                    ifCorrectlyResponded = false;
                    break;
            }
        }
    }

    public void betDialgoue() {
        // TODO Use this to choose the amount of bet you want
        System.out.println("For betting today, we have a number of fine horses to choose from");
        printBetOptions();

        boolean ifTalking = true;
        int dialogueSection = 0;
        int numResponse;
        Horse selectedHorse = null;

        while (ifTalking) {
            switch (dialogueSection) {
                case 0:
                    printBetOptions();
                    break;
                case 1:
                    // Choose a value of bet
                    System.out.println("How much would you like to bet on this fine horse?");
                    float betValue = 0;
                    try {
                        betValue = Float.parseFloat(console.readLine());
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                        System.err.println("ERROR IN INPUT MONEY FOR BET");
                    }
                    if (betValue > currentUser.getMoney()) {
                        // Too expensive
                        System.out.println("Get real, you can't pay that!");
                        continue;
                    }
                    else if (betValue < 5.0f) {
                        // Too cheap
                        System.out.println("Our minimum value bet is $5.00 sir/ma'am");
                    }
                    else {
                        // Just right
                        System.out.println("Thank you! Now sit down and get ready for the show!");
                        bets.add(new Bet(currentUser, selectedHorse, betValue));
                        dialogueSection = 0;
                    }
                    continue;
                case 2:
                    // Incorrect response
                    break;
                default:
                    // Whoops lmao
                    break;
            }

            try {
                numResponse = Integer.parseInt(console.readLine());
            }
            catch (IOException e) {
                e.printStackTrace();
                System.err.println("ERROR IN RACE BET DIALOGUE");
                continue;
            }

            if (numResponse > 0 && numResponse <= racers.size()) {
                // The user chose one of the horses
                selectedHorse = racers.get(numResponse - 1);
                dialogueSection = 1;
            }
            else if (numResponse == racers.size() + 1) {
                // The user wants to leave
                ifTalking = false;
            }
            else {
                // The user incorrectly repsonded
                System.out.println("Sorry, that wasn't one of the options...");
            }

        }
    }

    public void printBetOptions() {
        System.out.println("Which horse are you interested in betting on today?");
        int i = 1;
        for (Horse racer : racers) {
            // You can win up to Bet.horseBestWInning
            System.out.println(i + ". " + racer.getName() + ":");
            System.out.println("    Rating: " + racer.getRating());
            if (ifAdmin) {
                System.out.println("    Volatility: " + racer.getVolatility());
                System.out.println("    Trainer skill: " + racer.getTrainer().getSkill());
            }
            System.out.println("    Best Multiplier: " + Bet.horseBestReturn(racer) + "x ");
            i++;
        }
        System.out.println(i + ". Leave the betting area");
    }


    public void runTheRace() {
        // Choose who wins based on their skill
        // Then update their skills based on their trainers and how well they did
        for (Horse racer : racers) {
            // Rating is solid, then at most you add entire volatility or subtract entire volatility
            //racer.setRaceValue(racer.getRating() + (racer.getVolatility() * ((new Random()).nextInt(3) - 1)));
            racer.setRandomRaceValue();
        }
        racers.sort(Horse::compareTo);

        // TODO Create silly dialogue of the race. Saying who wins in the end.
        announceTheRace();

        // Depending on how well they did and their trainer skill, we determine how much their rating improves by
        // Depending on how well they did and their horse rating, their trainer rating also goes up (but less)

        if (ifAdmin) {
            System.out.println("ADMIN RACE RESULTS: ");
        }

        for (int i = 0; i < racers.size(); i++) {
            // inverse to position on list
            // Becomes negative after beating less than half of the other racers
            Horse racer = racers.get(i);

            float horseRating = racer.getRating();
            float trainerRating = racer.getTrainer().getSkill();

            int wellness = (racers.size() - i - 1) - (racers.size() / 2);

            // You only win if you get first. Otherwise, you're a loser :)
            if (i == 0) {
                racer.win(wellness);
            }
            else {
                racer.lose(wellness);
            }

            if (ifAdmin) {
                System.out.println((i+1) + ". " + racer.getName() + " w/ race value: " + racer.getRaceValue());
                System.out.println("    (Rating: " + racer.getRating() + ", Volatility: " + racer.getVolatility() + ")");
                System.out.println("    (Wellness in race: " + wellness + ")");
                System.out.println("    (Rating increased by " + (racer.getRating() - horseRating) + ")");
                System.out.println("    (Trainer skill increased by " + (racer.getTrainer().getSkill() - trainerRating) + ")");
            }
        }

        if (ifAdmin) {
            System.out.println("ADMIN BET RESULTS: ");
        }

        // Then the bets are figured out from the positions of the race
        for (Bet bet : bets) {
            float userInitial = bet.getUser().getMoney() + bet.getValue();
            bet.cashBet(racers.indexOf(bet.getHorse()), racers.size());
            if (ifAdmin) {
                float dm = bet.getUser().getMoney() - userInitial;
                String got = dm > 0 ? " won " : " lost ";
                if (dm == 0) {
                    System.out.println(bet.getUser().getName() + " broke exactly even!");
                }
                else {
                    System.out.println(bet.getUser().getName() + got + "$" + dm + "!");
                }
            }
        }

        // Now all bets are properly processed
        bets.clear();

        // Then the race is done!
    }

    private void enterToContinue() {
        System.out.print("... ");
        try {
            console.readLine();
        }
        catch (IOException e) {
            e.printStackTrace();
            System.err.println("ERROR AT ENTER TO CONTINUE");
        }
    }

    private void shuffleArrayList(ArrayList<Horse> racerList) {
        // TODO How to shuffle an ArrayList in Java?
        // TODO THIS IS BRUTALLY INEFFICIENT
        ArrayList<Integer> randomIndexes = new ArrayList<>();
        int length = racerList.size();
        for (int i = 0; i < length; i++) {
            // Do it "size" times
            int r;
            while (randomIndexes.contains(r = random.nextInt(length)));
            randomIndexes.add(r);
        }
        ArrayList<Horse> returnArray = new ArrayList<>();
        for (Integer i : randomIndexes) {
            returnArray.add(racerList.get(i));
        }

        for (int i = 0; i < length; i++) {
            racerList.set(i, returnArray.get(i));
        }
    }

    public void announceTheRace() {
        // racers should be sorted at this point
        // TODO add some randomness to this
        // TODO include the stats about volatility, trainer skill, rating, name, league
        // TODO Also use input to make it more interactive

        // Different values of note:

        // If volatility > 0.5(rating)
        // If volatility > 0.25(rating)
        // If volatility > 0.1(rating)
        // If volatility < 0.1(rating)

        // If trainerSkill < horseRating(0.75)
        // If horseRating(0.75) < trainerSkill < horseRating(1.25)
        // If horseRating(1.25) < trainerSkill

        // How to measure good/bad days?
        // How good raceValue is?

        ArrayList<Horse> randomOrderRacers = racers;
        shuffleArrayList(randomOrderRacers);

        int numChoices = 2;
        // For the introduction
        switch (random.nextInt(numChoices)) {
            case 0:
                System.out.println("Welcome everyone to the horse race! We have a number of great horses competing today!");
                System.out.println("The race is about to begin, so everyone get ready for this exciting competition!!!");
                break;
            case 1:
                System.out.println("Nooooooowwwww people! it's time to get right into the action!");
                System.out.println("I hope you brought your raincoats and umberellas, but talent is about to rain down on everyone!");
                break;
            default:
                // Error here
                break;
        }

        enterToContinue();
        shuffleArrayList(randomOrderRacers);
        numChoices = 1;

        // The specific horse stats
        switch (random.nextInt(numChoices)) {
            case 0:
                System.out.println("Each horse looks poised and ready to sprint!");
                // TODO Add lane numbers? (i.e in lane number one we have rider...)
                for (Horse horse : randomOrderRacers) {
                   System.out.println(horse.getName() + " looks good!");
                }
                break;
            default:
                break;
        }

        enterToContinue();
        shuffleArrayList(randomOrderRacers);

        // For the beginning of the race
        switch (random.nextInt(numChoices)) {

        }

        enterToContinue();
        shuffleArrayList(randomOrderRacers);

        // For the initial places
        switch (random.nextInt(numChoices)) {

        }

        enterToContinue();
        shuffleArrayList(randomOrderRacers);

        // For the full speed
        switch (random.nextInt(numChoices)) {

        }

        enterToContinue();
        shuffleArrayList(randomOrderRacers);

        // For the turn
        switch (random.nextInt(numChoices)) {

        }

        enterToContinue();
        shuffleArrayList(randomOrderRacers);

        // For the random historical commentary
        switch (random.nextInt(numChoices)) {

        }

        enterToContinue();
        shuffleArrayList(randomOrderRacers);

        // For the twist near the end!
        switch (random.nextInt(numChoices)) {

        }

        enterToContinue();
        shuffleArrayList(randomOrderRacers);

        // For the twist at the very end
        switch (random.nextInt(numChoices)) {

        }

        enterToContinue();
        shuffleArrayList(randomOrderRacers);

        // The finishing positions
        switch (random.nextInt(numChoices)) {

        }

        enterToContinue();
    }
}
