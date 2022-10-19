import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        // For route calculation time (performance)
        long startTime;
        long endTime;

        // Starts program
        System.out.println("Simulated Annealing!");
        Scanner in = new Scanner(System.in);
        String input = in.nextLine();
        DistanceMatrix d = new DistanceMatrix("DistancesExample.txt");

        // Starts route calculation
        startTime = System.currentTimeMillis();
        SimulatedAnnealingClass sa = new SimulatedAnnealingClass(input, d);
        System.out.println("First route:\t" + sa.getFirstRoute() + "\n");
        System.out.println("\n\nBest route:\t\t" + sa.searchSolution());
        endTime = System.currentTimeMillis();
        System.out.println(sa);
        System.out.println("Time elapsed: " + ((double) endTime - (double) startTime) / 1000);

        // TESTS:
        // 1 - ADPTUV - 700
        // 2 - CDGILNOQRSTU - 1546
        // 3 - BCDEFGHILMNOPQRSTU - 1718
        // 4 - ABCDEFGHIJLMNOPQRSTUV - 1868
    }
}