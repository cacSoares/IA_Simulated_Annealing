import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        System.out.println("Simulated Annealing!");
        Scanner in = new Scanner(System.in);
        String input = in.nextLine();
        DistanceMatrix d = new DistanceMatrix("DistancesExample.txt");
        SimulatedAnnealingClass sa = new SimulatedAnnealingClass(input, d);
        System.out.println("First route:\t"+sa.getFirstRoute());
        System.out.println("\nBest route:\t\t"+sa.searchRoute());
        System.out.println(sa);
        // 1 - ADPTUV
        // 2 - CDGILNOQRSTU
        // 3 - BCDEFGHILMNOPQRSTU
        // 4 - ABCDEFGHIJLMNOPQRSTUV
    }
}