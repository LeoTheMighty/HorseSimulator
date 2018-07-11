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

    public Race(User currentUser, HashMap<String, Horse> horseHashMap, ArrayList<User> computerUsers) {
        // TODO INCLUDE SOMETHING ABOUT LEAGUES IN THE RACE
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
            while (horseNumberSet.contains((r = (new Random()).nextInt(numHorses))));
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
            float percentageBet = ((new Random()).nextFloat() / 4);
            Horse randomHorse = racers.get((new Random()).nextInt(racers.size()));
            bets.add(new Bet(computerUser, randomHorse, computerUser.getMoney() * percentageBet));
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

        for (int i = 0; i < racers.size(); i++) {
            // inverse to position on list
            // Becomes negative after beating less than half of the other racers
            int wellness = (racers.size() - i - 1) - (racers.size() / 2);
            // You only win if you get first. Otherwise, you're a loser :)
            if (i == 0) {
                racers.get(i).win(wellness);
            }
            else {
                racers.get(i).lose(wellness);
            }
        }

        // Then the bets are figured out from the positions of the race
        for (Bet bet : bets) {
            bet.cashBet(racers.indexOf(bet.getHorse()), racers.size());
        }

        // Now all bets are properly processed
        bets.clear();

        // Then the race is done!
    }

    public void announceTheRace() {
        // Should be sorted at this point
        // TODO add some randomness to this
        // TODO include the stats about volatility, trainer skill, rating, name, league
        // TODO Also use input to make it more interactive
    }
}
