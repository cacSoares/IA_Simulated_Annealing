import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        System.out.println("Simulated Annealing!");
        Scanner in = new Scanner(System.in);
        String input = in.nextLine();
        DistanceMatrix d = new DistanceMatrix("DistancesExample.txt");
        SimulatedAnnealingClass sa = new SimulatedAnnealingClass(input, d, 0.002);
        System.out.println("First route:\t"+sa.getFirstRoute());
        System.out.println("\nBest route:\t\t"+sa.searchRoute());
        System.out.println("Debug route:\t"+sa.confirmResult());
        System.out.println(sa);

        //ABCDEFGHIJLMNOPQRSTUV
        /*
        ArrayList<String> cities = d.getCities();
        for (String c : cities)
            System.out.println(c);

        ArrayList<ArrayList<Integer>> distances = d.getDistances();
        for (int i = 0; i < distances.size() - 1; i++){
            for(int j = 0; j < distances.get(i).size(); j++){
                System.out.println("Index "+i+" ("+cities.get(i)+") -> "+ distances.get(i).get(j));
            }
        }

        ArrayList<String> myCities = d.getCities(input);
        System.out.println("Filtered cities!");
        for (String c : myCities)
            System.out.println(c);
*/
        //RouteClass route = new RouteClass("ADECB", d);

        //System.out.println(route);

    }
}