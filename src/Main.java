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
        System.out.println("Temperature: " + Math.abs(sa.getT()));
        System.out.println("First route:\t" + sa.getFirstRoute() + "\n");

        // Displays solutions
        System.out.println("\n\nBest route:\t\t" + sa.searchSolution());
        endTime = System.currentTimeMillis();
        System.out.println("Worst Route: " + sa.getWorstRoute());
        System.out.println("Last route: " + sa.getLastRoute() + "\n");

        // Displays solution parameters
        System.out.println("Best route at iteration number:\t" + sa.getBestRouteIteration());
        System.out.println(sa);
        System.out.println("Time elapsed:\t\t\t\t\t" + (endTime - startTime) + " ms");

        // TESTS:
        // 1 - ADPTUV - 700
        // 2 - CDGILNOQRSTU - 1546
        // 3 - BCDEFGHILMNOPQRSTU - 1718
        // 4 - ABCDEFGHIJLMNOPQRSTUV - 1868
    }
}