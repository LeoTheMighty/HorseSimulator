import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

public class Simulation {
    // All name -> whatever
    private User currentUser;
    private ArrayList<User> computerUsers;

    private final int numComputerUsers = 10;

    private HashMap<String, User> allUsers;
    private HashMap<String, User> allComputerUsers;
    private HashMap<String, Horse> allHorses;
    private HashMap<String, Trainer> allTrainers;

    final private float startingMoney = 1000;
    private boolean ifInside = false;
    private BufferedReader console = new BufferedReader(new InputStreamReader(System.in));

    private boolean ifAdmin = false;

    private int numTimesVisitedMain = 0;

    // TODO Expand on this list
    public static String[] potentialUserNames = {
            "John", "Josh", "James", "Jacob", "Jargon",
            "Abe", "Lincoln", "Bartleby", "Buster", "Boogey-man",
            "Bailey", "Babe", "Baby", "Kathy", "Karen", "Kristina",
            "Leo", "Lonny", "Luis", "Lenny", "Manny", "Martin", "Marge"
    };

    public Simulation() {
        ArrayList<HashMap<String, User>> allUsersArray = CSVHandler.getUsers();
        // This includes all the elements that are in allComputerUsers
        allUsers = allUsersArray.get(0);
        allComputerUsers = allUsersArray.get(1);
        allHorses = CSVHandler.getHorses();
        allTrainers = CSVHandler.getTrainers();
        computerUsers = new ArrayList<>();
        CSVHandler.updateOwnership(allHorses, allTrainers);
    }

    public void enter() {
        // Starts the simulation
        System.out.println("Howdy Pard'ner, welcome to the horse place!");
        System.out.println("Before we can let you in though, what's your name?");
        String response;
        try {
           response = console.readLine();
        }
        catch(IOException e) {
            // response = "<" + e + ">";
            e.printStackTrace();
            System.err.println("ERROR AT READLINE");
            return;
        }

        if (allUsers.containsKey(response)) {
            currentUser = allUsers.get(response);
            System.out.println("Welcome back " + currentUser.getName() + "!");
            System.out.println("If we recall correctly (we always do) you currently have $" + Float.toString(currentUser.getMoney()));
        }
        else {
            System.out.println("Oh a first timer!");
            System.out.println("Welcome " + response + "! You start with $" + Float.toString(startingMoney));
            System.out.println("You can do many different things while you're here, just go to help for questions!");
        }
        System.out.println();

        // This is just for extra debugging messages about everything
        ifAdmin = response.equals("admin");

        // Choose the random computer players
        // This chooses random `numComputerUsers` users to be in the computer users set
        int numPotentialComputerUsers = allComputerUsers.size();
        // Make sure that you add computer players if there aren't enough
        if (numPotentialComputerUsers < numComputerUsers) {
            for (int i = 0; i < numComputerUsers - numPotentialComputerUsers; i++) {
                // Make a random computer user with the starting amount
                String name;
                // Another savage while loop fix
                while (allUsers.containsKey(name = potentialUserNames[(new Random()).nextInt(potentialUserNames.length)]));
                User user = new User(name, startingMoney, true);
                allUsers.put(name, user);
                allComputerUsers.put(name, user);
            }
        }

        numPotentialComputerUsers = allComputerUsers.size();

        HashSet<Integer> userNumberSet = new HashSet<>();
        for (int i = 0; i < numComputerUsers; i++) {
            int r;
            // SAVAGE empty bodied while loop
            while (userNumberSet.contains((r = (new Random()).nextInt(numPotentialComputerUsers))));
            userNumberSet.add(r);
        }
        int i = 0;
        for (User user : allComputerUsers.values()) {
            if (userNumberSet.contains(i)) {
                computerUsers.add(user);
            }
            i++;
        }
        // Randomly choose and make sure that it isn't violating any of the rules
        // Has to be a potential computer user
        // Also can't be in the current User or in the computer users already

        ifInside = true;
        while (ifInside) {
            mainDialogue();
        }

        System.out.println();
        System.out.println("See ya pard'ner, keep on keeping on!");

        // Save all the data that we just created/updated
        CSVHandler.writeUsers(allUsers);
        CSVHandler.writeHorses(allHorses);
        CSVHandler.writeTrainers(allTrainers);
        CSVHandler.writeOwnership(allHorses);
    }

    // Come back here when restarting your interaction
    public void mainDialogue() {
        // You'll have a number of different options
        // Only race for now
        // TODO include more things to do

        System.out.println("What would you like to do right now?");
        System.out.println("Options:");
        System.out.println("    1. Race");
        System.out.println("    2. Help");
        System.out.println("    3. Leave");

        boolean ifCorrectlyResponded = false;
        int numResponse;
        while(!ifCorrectlyResponded) {
            try {
                numResponse = Integer.parseInt(console.readLine());
            }
            catch(Exception e) {
                // e.printStackTrace();
                // System.err.println("ERROR AT MAIN DIAG WHILE");
                System.out.println("Sorry, I couldn't understand that response, can you say it again?");
                System.out.println("Options:");
                System.out.println("    1. Race");
                System.out.println("    2. Help");
                System.out.println("    3. Leave");
               continue;
            }
            ifCorrectlyResponded = true;
            switch (numResponse) {
                // TODO implement these
                case 1:
                    // Race
                    Race race = new Race(currentUser, allHorses, computerUsers);
                    race.enter();
                    break;
                case 2:
                    // Help
                    break;
                case 3:
                    // Leave
                    ifInside = false;
                    break;
                default:
                    System.out.println("Sorry that wasn't one of the numbered responses. Can I hear it again?");
                    ifCorrectlyResponded = false;
            }
        }

        numTimesVisitedMain++;
    }
}
