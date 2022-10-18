import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

public class SimulatedAnnealingClass {

    // Constants
    private static final double ACCEPTANCE = 0.8;
    private static final double ALPHA = 0.998;
    private static final int N_INT_OPTIMIZER_BASE = 40;
    private static final int N_LETTERS = 20;
    private static final int N_INT_SCALER = 15;

    // Variables
    private double T;
    private final RouteClass firstRoute;
    private RouteClass bestRoute;
    private RouteClass currentRoute;
    private ArrayList<RouteClass> allRoutes;
    private DistanceMatrix dm;
    private int attempts;
    private int acceptedAttempts;
    private int nIntOtimizer;
    private double n_iterations = 10.0;

    public SimulatedAnnealingClass(String filter, DistanceMatrix dm){
        // First Route to compare solutions
        this.firstRoute = new RouteClass(filter, dm);
        //create random initial solution
        this.currentRoute = this.firstRoute;
        // We would like to keep track if the best solution
        // At this point assume that the best solution is the current solution
        this.bestRoute = this.firstRoute;
        //Set initial temp
        this.T = setInitialTemp(dm);
        // Distance Matrix
        this.dm = dm;
        // All generated solutions
        this.allRoutes = new ArrayList<>();
        this.allRoutes.add(this.firstRoute);
        // Attempts Counters
        this.attempts = 0;
        this.acceptedAttempts = 0;

        // Inits nIntOtimizer. nIntScaler increases when the number of letters decreases,
        //      decreasing the calculation time for lower filters
        int nIntScaler = N_INT_SCALER * (N_LETTERS/filter.length());
        nIntOtimizer = N_INT_OPTIMIZER_BASE + nIntScaler;
    }

    private double setInitialTemp(DistanceMatrix dm) {
        int maxDistance = Integer.MIN_VALUE;
        int minDistance = Integer.MAX_VALUE;
        for (int i = 0 ; i < this.firstRoute.getRouteSize()-1; i ++){
                for (int j = i + 1; j < this.firstRoute.getRouteSize(); j ++){
                    int d = dm.distance(this.firstRoute.getCity(i), this.firstRoute.getCity(j));
                    if (maxDistance < d) maxDistance = d;
                    if (minDistance > d) minDistance = d;
                }
        }
        int d = dm.distance(this.firstRoute.getCity(this.firstRoute.getRouteSize()-1), this.firstRoute.getCity(0));
        if (maxDistance < d) maxDistance = d;
        if (minDistance > d) minDistance = d;

        return (-(maxDistance - minDistance)) / Math.log(0.9);
    }

    public RouteClass getFirstRoute(){
        return this.firstRoute;
    }

    public RouteClass searchRoute(){

        // Loop until system has cooled
        while (T >= 1.0){

            for (int n = 0 ; n < (int)n_iterations ; n++) {
                this.attempts++;

                // Create new neighbour route
                RouteClass neighbourRoute = new RouteClass(this.currentRoute, dm);
                allRoutes.add(neighbourRoute);

                // TODO if necessary
                // The actual solution runs with the implementation of Collections.shuffle
                // We can implement a minor changes in initial route by swapping only two cities a time
                // Get random positions in the route (make sure that routePos1 and routePos2 are different)
                // Get the cities at selected positions in the route
                // Swap them
                // If so: TODO: implement a function to get random integer in a range of [ 0 - neighbourRoute.size() - 1 ]

                // Get energy of solutions
                int currentDistance = this.currentRoute.getTotalRouteDistance();
                int neighbourDistance = neighbourRoute.getTotalRouteDistance();

                // Decide if we should accept the neighbour
                double prob = acceptProbability(currentDistance, neighbourDistance);
                double randProb = randomDouble();

                if (prob >= randProb) {
                    this.currentRoute = neighbourRoute;
                    this.acceptedAttempts++;
                    // Check if bestRoute is still the better one
                    if (this.currentRoute.getTotalRouteDistance() < this.bestRoute.getTotalRouteDistance())
                        this.bestRoute = this.currentRoute;
                }
            }

            // Cooling in geometric form
            cooling();

            // Stops the algorithm when we have good values
            if (!isAcceptableToContinue()) break;
        }
        return bestRoute;
    }

    private boolean isAcceptableToContinue() {
        return ACCEPTANCE > (1.0 - ((double)this.acceptedAttempts / (double)this.attempts));
    }

    private void cooling() {
        // Cools the temperature
        T *= ALPHA;

        // Changes the number of iterations with the temperature value
        double ratio = 1- ((double)this.acceptedAttempts / (double)this.attempts);
        n_iterations = n_iterations * (1.0 + (ratio)/nIntOtimizer);
        System.out.printf("\rCompleted: %.2f%%", Math.min(100*ratio/0.8, 100));
    }

    private double defineIterationNumber(int n_iterations) {
        return 0.0;

    }

    // Just a debug function to confirm result of searchRoute
    public RouteClass confirmResult(){
        RouteClass best = allRoutes.get(0);
        for (int i = 1; i < allRoutes.size(); i++){
            if (best.getTotalRouteDistance() > allRoutes.get(i).getTotalRouteDistance())
                best = allRoutes.get(i);
        }
        return best;
    }

    private double acceptProbability(int currentDistance, int neighbourDistance) {
        if (currentDistance > neighbourDistance)
            return 1.0;
        return Math.exp((currentDistance - neighbourDistance) / T);
    }

    // Generate a random double [0.0000 - 1.0000]
    private double randomDouble() {
        Random r = new Random();
        return r.nextInt(10000)/10000.0;
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("Total attempts: "+"\t\t\t"+this.attempts+"\n");
        sb.append("Total acceptedAttempts: "+"\t"+this.acceptedAttempts+"\n");



        return sb.toString();
    }


}
