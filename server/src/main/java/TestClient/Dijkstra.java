package TestClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

import Models.Gameplay.Graph;
import Models.Gameplay.Player;
import Models.Gameplay.Route;


/**
 * Created by kiphacking on 3/21/18.
 */

class LongestPath
{
    private int numVertices;
    public int getNumVertices() {
        return numVertices;
    }

    public void setNumVertices(int v) {
        numVertices = v;
    }


    int minDistance(int dist[], Boolean sptSet[], LongestPath t)
    {
        // Initialize min value
        int min = Integer.MAX_VALUE, min_index=-1;

        for (int v = 0; v < t.getNumVertices(); v++)
            if (sptSet[v] == false && dist[v] <= min)
            {
                min = dist[v];
                min_index = v;
            }

        return min_index;
    }

    // A utility function to print the constructed distance array
    void printSolution(int dist[], int n, LongestPath t)
    {
        int numVertex = t.getNumVertices();
        System.out.println("Vertex Distance from Source");
        for (int i = 0; i < numVertex; i++)
            System.out.println(i+" tt "+ dist[i]);
    }

    // Funtion that implements Dijkstra's single source shortest path
    // algorithm for a graph represented using adjacency matrix
    // representation
    public int dijkstra(int graph[][], int src, LongestPath t)
    {   int numVertex = t.getNumVertices();

        int dist[] = new int[numVertex]; // The output array. dist[i] will hold
        // the shortest distance from src to i

        // sptSet[i] will true if vertex i is included in shortest
        // path tree or shortest distance from src to i is finalized
        Boolean sptSet[] = new Boolean[numVertex];

        // Initialize all distances as INFINITE and stpSet[] as false
        for (int i = 0; i < numVertex; i++)
        {
            dist[i] = Integer.MAX_VALUE;
            sptSet[i] = false;
        }

        // Distance of source vertex from itself is always 0
        dist[src] = 0;

        // Find shortest path for all vertices
        for (int count = 0; count < numVertex-1; count++)
        {
            // Pick the minimum distance vertex from the set of vertices
            // not yet processed. u is always equal to src in first
            // iteration.
            int u = minDistance(dist, sptSet, t);

            // Mark the picked vertex as processed
            sptSet[u] = true;

            // Update dist value of the adjacent vertices of the
            // picked vertex.
            for (int v = 0; v < numVertex; v++)

                // Update dist[v] only if is not in sptSet, there is an
                // edge from u to v, and total weight of path from src to
                // v through u is smaller than current value of dist[v]
                if (!sptSet[v] && graph[u][v]!=0 &&
                        dist[u] != Integer.MAX_VALUE &&
                        dist[u]+graph[u][v] < dist[v])
                    dist[v] = dist[u] + graph[u][v];
        }

        // Convert costs to positive values
        for(int i = 0; i < dist.length; i++){
            dist[i] = dist[i] * -1;

        }

        // Print cost path
        printSolution(dist, numVertex, t);

        // return longest path
        return calcMaxLength(dist);

    }

    private int calcMaxLength(int dist[]){
        int[] myArray = dist;
        Arrays.sort(myArray);
        int max = myArray[myArray.length-1];
        return max;
    }

    public static int calcLongestRoute(Player player){
        int longestRouteLength = 0;
        ArrayList<Integer> maxLength = new ArrayList<>();
        LongestPath t = new LongestPath();

        // Map of cities without duplicates
        HashMap<String, Integer> cities = player.getListClaimedRouteCities();
        t.setNumVertices(cities.size());

        // Number of vertices
        int numVertex = t.getNumVertices();
        Graph g = new Graph(numVertex);


        // Create AdjacencyMatrix from claimed routes
        ArrayList<Route> claimedRoutes = player.getClaimedRouteList();
        for(int i = 0; i < claimedRoutes.size(); i++){
            g.addEdge(cities.get(claimedRoutes.get(i).getStartCity()),cities.get(claimedRoutes.get(i).getEndCity()),(claimedRoutes.get(i).getLength() * -1));

        }

        for(int i = 0; i < numVertex; i++) {

            for(int j = 0; j < numVertex; j++){
                System.out.print(g.getAdjMatrix()[i][j]);
                System.out.print(" ");

            }
            System.out.println();
        }

        int graph[][] = g.getAdjMatrix();

        for(int i = 0; i < numVertex; i++){

            int max = t.dijkstra(graph, i, t);
            maxLength.add(max);
            System.out.println("Longest route: "+max);
            System.out.println();
        }

        Collections.sort(maxLength);
        System.out.println("Overall Longest route: "+ maxLength.get(maxLength.size()-1));


        return maxLength.get(maxLength.size()-1);
    }


    public static void main (String[] args)
    {

        Player player = new Player();
        Route route1 = new Route("Seattle", "SF",4);
        Route route2 = new Route("Seattle", "Portland",2);
        Route route3 = new Route("Seattle", "SLC",3);
        Route route4 = new Route("Portland", "SF",5);
        Route route5 = new Route("Portland", "DC",6);
        Route route6 = new Route("SF", "Langley",2);
        Route route7 = new Route("SLC", "Langley",2);
        Route route8 = new Route("Langley", "DC",6);

        ArrayList<Route> routes = new ArrayList<>(Arrays.asList(route1,route2,route3,route4,route5,route6,route7,route8));
        player.setClaimedRouteList(routes);

        calcLongestRoute(player);


    }
}

//class Graph {
//    private int adjMatrix[][];
//    private int numVertices;
//
//    public int[][] getAdjMatrix() {
//        return adjMatrix;
//    }
//
//    public void setAdjMatrix(int[][] adjMatrix) {
//        this.adjMatrix = adjMatrix;
//    }
//
//    public Graph(int numVertices) {
//        this.numVertices = numVertices;
//        adjMatrix = new int[numVertices][numVertices];
//    }
//
//    public void addEdge(int i, int j, int weight) {
//        adjMatrix[i][j] = weight;
//        adjMatrix[j][i] = weight;
//    }
//
//}