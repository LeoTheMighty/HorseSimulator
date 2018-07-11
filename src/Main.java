 import java.io.BufferedReader;
 import java.io.IOException;
 import java.io.InputStreamReader;
 import java.util.Random;

 public class Main {
    public static void main(String[] args) {
        // For testing various things
//         System.out.println((League.PONY).toString());
//         System.out.println(League.valueOf("PONY").toString());
//        System.out.println((new Random()).nextFloat());

         // Stop it before it goes any further
//         try {
//             (new BufferedReader(new InputStreamReader(System.in))).readLine();
//         }
//         catch (IOException e) {
//             e.printStackTrace();
//             return;
//         }
        Simulation simulation = new Simulation();
        simulation.enter();
    }
}
