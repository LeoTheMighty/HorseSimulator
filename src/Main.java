 import java.io.BufferedReader;
 import java.io.IOException;
 import java.io.InputStreamReader;
 import java.util.ArrayList;
 import java.util.Random;

 public class Main {
    public static void main(String[] args) {
        // For testing various things
//         System.out.println((League.PONY).toString());
//         System.out.println(League.valueOf("PONY").toString());
//        System.out.println((new Random()).nextFloat());
//         ArrayList<String> as = new ArrayList<>();
//         as.add("eff");
//         System.out.println(as.get(0));
//         int i = 0;
//         for (String a : as) {
//             as.set(i, a + "eff");
//         }
//         System.out.println(as.get(0));

          // Stop it before it goes any further
         try {
             (new BufferedReader(new InputStreamReader(System.in))).readLine();
         }
         catch (IOException e) {
             e.printStackTrace();
             return;
         }
        Simulation simulation = new Simulation();
        simulation.enter();
    }
}
