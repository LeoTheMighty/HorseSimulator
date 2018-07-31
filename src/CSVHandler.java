import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class CSVHandler {
    private static String userCSVFileName = "data/users.csv";
    private static String horseCSVFileName = "data/horses.csv";
    private static String trainerCSVFileName = "data/trainers.csv";
    private static String ownershipCSVFileName = "data/ownership.csv";

    private static String userBackupCSVFileName = "data.backup/users.csv";
    private static String horseBackupCSVFileName = "data.backup/horses.csv";
    private static String trainerBackupCSVFileName = "data.backup/trainers.csv";
    private static String ownershipBackupCSVFileName = "data.backup/ownership.csv";

    // return[0] is regular users, return[1] is computer users
    public static ArrayList<HashMap<String, User>> getUsers() {
        HashMap<String, User> users = new HashMap<>();
        HashMap<String, User> computerUsers = new HashMap<>();

        String filename;
        if ((new File(userCSVFileName)).isFile()) {
            filename = userCSVFileName;
        }
        else if ((new File(userBackupCSVFileName)).isFile()) {
            filename = userBackupCSVFileName;
        }
        else {
            filename = "";
            System.err.println("Could not find User CSV Files!");
        }

        try {
            BufferedReader br = new BufferedReader(new FileReader(filename));
            String line;
            br.readLine();
            while((line = br.readLine()) != null) {
                String[] values = line.split(",");
                String name = values[0].trim();
                float money = Float.parseFloat(values[1].trim());
                boolean ifPotentialComputer = Boolean.parseBoolean(values[2].trim());
                if (ifPotentialComputer) {
                    computerUsers.put(name, new User(name, money, ifPotentialComputer));
                }
                users.put(name, new User(name, money, ifPotentialComputer));
            }

        }
        catch (IOException e) {
            e.printStackTrace();
            System.err.println("ERROR AT CSV GET USERS");
        }

        ArrayList<HashMap<String, User>> returnArray = new ArrayList<>();
        returnArray.add(users);
        returnArray.add(computerUsers);

        return returnArray;
    }

    public static HashMap<String, Horse> getHorses() {
        HashMap<String, Horse> horses = new HashMap<>();

        String filename;
        if ((new File(horseCSVFileName)).isFile()) {
            filename = horseCSVFileName;
        }
        else if ((new File(horseBackupCSVFileName)).isFile()) {
            filename = horseBackupCSVFileName;
        }
        else {
            filename = "";
            System.err.println("Could not find Horse CSV Files!");
        }

        try {
            BufferedReader br = new BufferedReader(new FileReader(filename));
            String line;
            br.readLine();
            while((line = br.readLine()) != null) {
                String[] values = line.split(",");
                String name = values[0].trim();
                int age = Integer.parseInt(values[1].trim());
                float rating = Float.parseFloat(values[2].trim());
                float volatility = Float.parseFloat(values[3].trim());
                League league = League.valueOf(values[4].trim());
                int racesWon = Integer.parseInt(values[5].trim());
                int racesLost = Integer.parseInt(values[6].trim());
                horses.put(name, new Horse(name, age, rating, volatility, league, racesWon, racesLost));
            }

        }
        catch (IOException e) {
            e.printStackTrace();
            System.err.println("ERROR AT CSV GET HORSES");
        }
        return horses;
    }

    public static HashMap<String, Trainer> getTrainers() {
        //TODO
        HashMap<String, Trainer> trainers = new HashMap<>();

        String filename;
        if ((new File(trainerCSVFileName)).isFile()) {
            filename = trainerCSVFileName;
        }
        else if ((new File(trainerBackupCSVFileName)).isFile()) {
            filename = trainerBackupCSVFileName;
        }
        else {
            filename = "";
            System.err.println("Could not find Trainer CSV Files!");
        }

        try {
            BufferedReader br = new BufferedReader(new FileReader(filename));
            String line;
            br.readLine();
            while((line = br.readLine()) != null) {
                String[] values = line.split(",");
                String name = values[0].trim();
                int age = Integer.parseInt(values[1].trim());
                float skill = Float.parseFloat(values[2].trim());
                League league = League.valueOf(values[3].trim());
                trainers.put(name, new Trainer(name, age, skill, league));
            }
        }
        catch (IOException e) {
            e.printStackTrace();
            System.err.println("ERROR AT CSV GET TRAINERS");
        }
        return trainers;
    }

    public static void updateOwnership(HashMap<String, Horse> horseHashMap, HashMap<String, Trainer> trainerHashMap) {

        String filename;
        if ((new File(ownershipCSVFileName)).isFile()) {
            filename = ownershipCSVFileName;
        }
        else if ((new File(ownershipBackupCSVFileName)).isFile()) {
            filename = ownershipBackupCSVFileName;
        }
        else {
            filename = "";
            System.err.println("Could not find Ownership CSV Files!");
        }

        try {
            BufferedReader br = new BufferedReader(new FileReader(filename));
            String line;
            br.readLine();
            while((line = br.readLine()) != null) {
                String[] values = line.split(",");
                String horseName = values[0].trim();
                String trainerName = values[1].trim();
                Horse horse = horseHashMap.get(horseName);
                Trainer trainer = trainerHashMap.get(trainerName);

                // Horse can only have one trainer
                if (horse.getTrainer() == null) {
                    horse.setTrainer(trainer);
                }
                else {
                    // Error here, shouldn't be more than one owner
                    throw new IOException("Trainer for horse set twice");
                }
                trainer.addHorse(horse);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
            System.err.println("ERROR AT CSV UPDATE OWNERSHIP");
        }
    }

    public static void writeUsers(HashMap<String, User> users) {
       try {
           // append: false means we want to rewrite the file from scratch
           BufferedWriter bw = new BufferedWriter(new FileWriter(userCSVFileName, false));
           bw.write("name, money, ifPotentialComputer\n");
           for (User user : users.values()) {
               bw.write(user.getName() + ",");
               bw.write(user.getMoney() + ",");
               bw.write(user.getIfPotentialComputer() + "\n");
           }
           bw.close();
       }
       catch (IOException e) {
           e.printStackTrace();
           System.err.println("ERROR AT CSV WRITE USERS");
       }
    }

    public static void writeHorses(HashMap<String, Horse> horses) {
        try {
            // append: false means we want to rewrite the file from scratch
            BufferedWriter bw = new BufferedWriter(new FileWriter(horseCSVFileName, false));
            bw.write("name,age,rating,volatility,league,racesWon,racesLost\n");
            for (Horse horse : horses.values()) {
                bw.write(horse.getName() + ",");
                bw.write(horse.getAge() + ",");
                bw.write(horse.getRating() + ",");
                bw.write(horse.getVolatility() + ",");
                bw.write(horse.getLeague().toString() + ",");
                bw.write(horse.getRacesWon() + ",");
                bw.write(horse.getRacesLost() + "\n");
            }
            bw.close();
        }
        catch (IOException e) {
            e.printStackTrace();
            System.err.println("ERROR AT CSV WRITE HORSES");
        }
    }

    public static void writeTrainers(HashMap<String, Trainer> trainers) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(trainerCSVFileName, false));
            bw.write("name, age, skill, league\n");
            for (Trainer trainer : trainers.values()) {
                bw.write(trainer.getName() + ",");
                bw.write(trainer.getAge() + ",");
                bw.write(trainer.getSkill() + ",");
                bw.write(trainer.getLeague().toString() + "\n");
            }
            bw.close();
        }
        catch (IOException e) {
            e.printStackTrace();
            System.err.println("ERROR AT CSV WRITE TRAINERS");
        }
    }

    public static void writeOwnership(HashMap<String, Horse> horses) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(ownershipCSVFileName, false));
            bw.write("horse, trainer\n");
            for (Horse horse : horses.values()) {
                bw.write(horse.getName() + ",");
                bw.write(horse.getTrainer().getName() + "\n");
            }
            bw.close();
        }
        catch (IOException e) {
            e.printStackTrace();
            System.err.println("ERROR AT CSV WRITE OWNERSHIP");
        }
    }
}
