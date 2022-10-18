import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class RouteClass {

    private int totalRouteDistance;
    private ArrayList<String> route;

    public RouteClass(String filter, DistanceMatrix dm){
        this.route = dm.getCities(filter);
        Collections.shuffle(this.route);
        setTotalRouteDistance(dm);
    }
    public RouteClass(RouteClass route, DistanceMatrix dm) {
        this.route = dm.getCities(dm.getInitials(route.getAllCities()));
        Collections.shuffle(this.route);
        setTotalRouteDistance(dm);
    }


    private void setTotalRouteDistance(DistanceMatrix dm){
        int distance = 0;
        int routeSize = this.route.size();
        for (int i = 0; i < routeSize; i++){
            if (i < routeSize-1)
                distance += dm.distance(route.get(i), route.get(i+1));
            else
                distance += dm.distance(route.get(i), route.get(0));
        }
        this.totalRouteDistance = distance;
    }

    public int getTotalRouteDistance(){
        return totalRouteDistance;
    }

    public int getRouteSize(){
        return route.size();
    }

    public String getCity(int index) {
        return route.get(index);
    }

    public ArrayList<String> getAllCities(){
        return this.route;
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("[ ");
        int l = route.size();
        int i = 0;
        for (String c : route){
            sb.append(c);
            if (i < l )
                sb.append(" -> ");
            i++;
        }
        sb.append(route.get(0));
        sb.append(" ] With total route distance: ");
        sb.append(this.getTotalRouteDistance());
        return sb.toString();
    }

}
