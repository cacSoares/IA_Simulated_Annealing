import java.util.ArrayList;
import java.util.Random;

public class SimulatedAnnealingClass {

    // Constants
    private static final double ACCEPTANCE = 0.95;
    private static final double ALPHA = 0.995;
    private static final int N_INT_OPTIMIZER_BASE = 39;
    private static final int N_LETTERS = 20;
    private static final int N_INT_SCALER = 15;

    // Variables
    private double T;
    private final RouteClass firstRoute;
    private RouteClass lastRoute;
    private RouteClass bestRoute;
    private RouteClass worstRoute;
    private RouteClass currentRoute;
    private ArrayList<RouteClass> allRoutes;
    private DistanceMatrix dm;
    private int attempts;
    private int acceptedAttempts;
    private int bestRouteIteration;
    private int nIntOptimizer;
    private double n_iterations = 10.0;

    public SimulatedAnnealingClass(String filter, DistanceMatrix dm) {
        // Creates first route to compare solutions and sets best, worst, current and last solutions
        // At this point we assume all the solutions above are the same as the first one
        this.firstRoute = new RouteClass(filter, dm);
        this.currentRoute = this.firstRoute;
        this.lastRoute = this.firstRoute;
        this.bestRoute = this.firstRoute;
        this.worstRoute = this.firstRoute;
        this.bestRouteIteration = 0;

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

        // Inits nIntOptimizer. nIntScaler increases when the number of letters decreases,
        // Decreasing the calculation time for lower filters
        // N_INT_SCALER * (N_LETTERS/filter.length()); if filter.length() = 21 -> nIntScaler = 0 [(int)20/21 -> 0]
        int nIntScaler = N_INT_SCALER * (N_LETTERS / filter.length());
        nIntOptimizer = N_INT_OPTIMIZER_BASE + nIntScaler;
    }

    private double setInitialTemp(DistanceMatrix dm) {
        int maxDistance = Integer.MIN_VALUE;
        int minDistance = Integer.MAX_VALUE;
        for (int i = 0; i < this.firstRoute.getRouteSize() - 1; i++) {
            for (int j = i + 1; j < this.firstRoute.getRouteSize(); j++) {
                int d = dm.distance(this.firstRoute.getCity(i), this.firstRoute.getCity(j));
                if (maxDistance < d) maxDistance = d;
                if (minDistance > d) minDistance = d;
            }
        }
        int d = dm.distance(this.firstRoute.getCity(this.firstRoute.getRouteSize() - 1), this.firstRoute.getCity(0));
        if (maxDistance < d) maxDistance = d;
        if (minDistance > d) minDistance = d;

        return (-(maxDistance - minDistance)) / Math.log(0.9);
    }

    public RouteClass getFirstRoute() {
        return this.firstRoute;
    }

    public RouteClass searchSolution() {
        double tMax = T;
        if (this.firstRoute.getRouteSize() <= 3) {
            return this.firstRoute;
        }

        // Loop until system has cooled
        while (T >= 0.1) {
            // Shows solution progress
            System.out.printf("\rCompleted: %.2f%%", Math.min(100 * (tMax - T) / (tMax - 33), 100));

            for (int n = 0; n < (int) n_iterations; n++) {
                this.attempts++;

                // Create new neighbour route
                RouteClass neighbourRoute = new RouteClass(this.currentRoute, dm);
                allRoutes.add(neighbourRoute);

                // Get energy of solutions
                int currentDistance = this.currentRoute.getTotalRouteDistance();
                int neighbourDistance = neighbourRoute.getTotalRouteDistance();

                // Calculates worst route while searching for the best solution
                if (currentDistance > this.worstRoute.getTotalRouteDistance()) {
                    this.worstRoute = currentRoute;
                }

                // Decide if we should accept the neighbour
                double prob = acceptProbability(currentDistance, neighbourDistance);
                double randProb = randomDouble();

                if (prob >= randProb) {
                    this.currentRoute = neighbourRoute;
                    this.acceptedAttempts++;
                    // Check if bestRoute is still the better one
                    if (currentDistance < this.bestRoute.getTotalRouteDistance()) {
                        this.bestRoute = this.currentRoute;
                        this.bestRouteIteration = this.attempts;
                    }
                }
            }

            // Cooling in geometric form
            cooling();

            // Stops the algorithm when we have good values
            if (!isAcceptableToContinue()) break;
        }

        // Sets the last solution
        lastRoute = currentRoute;

        return bestRoute;
    }

    private boolean isAcceptableToContinue() {
        // Sets the last solution
        lastRoute = currentRoute;

        return ACCEPTANCE > (1.0 - ((double) this.acceptedAttempts / (double) this.attempts));
    }

    private void cooling() {
        // Cools the temperature
        T *= ALPHA;

        // Changes the number of iterations with the temperature value
        double ratio = 1 - ((double) this.acceptedAttempts / (double) this.attempts);
        n_iterations = n_iterations * (1.0 + (ratio) / nIntOptimizer);
    }

    private double defineIterationNumber(int n_iterations) {
        return 0.0;

    }

    // Just a debug function to confirm result of searchRoute
    public RouteClass confirmResult() {
        RouteClass best = allRoutes.get(0);
        for (int i = 1; i < allRoutes.size(); i++) {
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
        return r.nextInt(10000) / 10000.0;
    }

    public int getBestRouteIteration() {
        return bestRouteIteration;
    }

    public RouteClass getWorstRoute() {
        return worstRoute;
    }

    public RouteClass getLastRoute() {
        return lastRoute;
    }

    public double getT() {
        return T;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Total attempts: " + "\t\t\t\t" + this.attempts + "\n");
        sb.append("Total acceptedAttempts: " + "\t\t" + this.acceptedAttempts);
        return sb.toString();
    }


}
