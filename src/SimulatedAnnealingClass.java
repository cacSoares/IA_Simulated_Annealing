public class SimulatedAnnealingClass {

    private RouteClass firstRoute;
    private RouteClass bestRoute;
    private RouteClass currentRoute;

    public SimulatedAnnealingClass(String qualquercoisa){
        //create random initial solution
        this.currentRoute = new RouteClass(qualquercoisa);
        // First Route to compare solutions
        this.firstRoute = this.currentRoute;
        // We would like to keep track if the best solution
        // Assume that the best solution is the current solution
        this.bestRoute = this.currentRoute;

    }






    //Set initial temp

    //Cooling rate






    // Loop until system has cooled

        while (temp > something){

            // Create new neighbour route

            // Get random positions in the route (make sure that routePos1 and routePos2 are different)

            // Get the cities at selected positions in the route

            // Swap them





    }

}
