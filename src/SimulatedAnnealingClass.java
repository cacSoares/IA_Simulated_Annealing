import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

public class SimulatedAnnealingClass {

    private final RouteClass firstRoute;
    private RouteClass bestRoute;
    private RouteClass currentRoute;
    private ArrayList<RouteClass> allRoutes;
    private double coolingRate;
    private double T;
    private DistanceMatrix dm;
    private int attempts;
    private int acceptedAttempts;

    public SimulatedAnnealingClass(String filter, DistanceMatrix dm, double coolingRate){
        // First Route to compare solutions
        this.firstRoute = new RouteClass(filter, dm);
        //create random initial solution
        this.currentRoute = this.firstRoute;
        // We would like to keep track if the best solution
        // At this point assume that the best solution is the current solution
        this.bestRoute = this.firstRoute;
        //Set initial temp
        this.T = setInitialTemp(dm);
        //Cooling rate
        this.coolingRate = coolingRate;
        // Distance Matrix
        this.dm = dm;
        // All generated solutions
        this.allRoutes = new ArrayList<>();
        this.allRoutes.add(this.firstRoute);
        // Attempts Counters
        this.attempts = 0;
        this.acceptedAttempts = 0;

    }

    private double setInitialTemp(DistanceMatrix dm) {
        int maxDistance = Integer.MIN_VALUE;
        int minDistance = Integer.MAX_VALUE;
        for (int i = 0 ; i < this.firstRoute.getRouteSize(); i ++){
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


    public RouteClass searchRoute(){

        // Loop until system has cooled

        while (T >= 1.0){
            this.attempts++;
            // Create new neighbour route
            RouteClass neighbourRoute = new RouteClass(this.currentRoute, dm);
            allRoutes.add(neighbourRoute);

            // TODO if necessary
            // Get random positions in the route (make sure that routePos1 and routePos2 are different)
            // Get the cities at selected positions in the route
            // Swap them

            // Get energy of solutions
            int currentDistance = this.currentRoute.getTotalRouteDistance();
            int neighbourDistance = neighbourRoute.getTotalRouteDistance();

            // Decide if we should accept the neighbour
            double prob = acceptProbability(currentDistance, neighbourDistance);
            double rand = randomDouble();

            if ( prob > rand ) {
                this.currentRoute = neighbourRoute;
                this.acceptedAttempts++;
                // Check if bestRoute is still the better one
                if (this.currentRoute.getTotalRouteDistance() < this.bestRoute.getTotalRouteDistance())
                    this.bestRoute = this.currentRoute;
            }
            // Cooling
            T *= 1 - coolingRate;
        }
        return bestRoute;
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
