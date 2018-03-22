package TestClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;


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



    public static void main (String[] args)
    {
        int vert = 6;
        ArrayList<Integer> maxLength = new ArrayList<>();
        LongestPath t = new LongestPath();
        t.setNumVertices(vert);
        int numVertex = t.getNumVertices();
        Graph g = new Graph(vert);

        HashMap<String, Integer> cities = new HashMap<>();

        String [] cityList = new String[]{"Seattle", "Portland", "SLC", "SF", "Seattle", "Langley", "Portland", "DC"};

        int count = 0;
        for(int i = 0; i < cityList.length; i++){

            if(!cities.containsKey(cityList[i])){
                cities.put(cityList[i], count);
                count++;
            }
        }

        g.addEdge(cities.get("Seattle"),cities.get("SF"),-4);
        g.addEdge(cities.get("Seattle"),cities.get("Portland"),-2);
        g.addEdge(cities.get("Seattle"),cities.get("SLC"),-3);
        g.addEdge(cities.get("Portland"),cities.get("SF"),-5);
        g.addEdge(cities.get("Portland"),cities.get("DC"),-6);
        g.addEdge(cities.get("SF"),cities.get("Langley"),-2);
        g.addEdge(cities.get("SLC"),cities.get("Langley"),-2);
        g.addEdge(cities.get("Langley"),cities.get("DC"),-6);

//        g.addEdge(0,2,-4);
//        g.addEdge(0,1,-2);
//        g.addEdge(0,3,-3);
//        g.addEdge(1,2,-5);
//        g.addEdge(1,5,-6);
//        g.addEdge(2,4,-2);
//        g.addEdge(3,4,-2);
//        g.addEdge(4,5,-6);


        for(int i = 0; i < vert; i++) {

            for(int j = 0; j < vert; j++){
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

    }
}

class Graph {
    private int adjMatrix[][];
    private int numVertices;

    public int[][] getAdjMatrix() {
        return adjMatrix;
    }

    public void setAdjMatrix(int[][] adjMatrix) {
        this.adjMatrix = adjMatrix;
    }

    public Graph(int numVertices) {
        this.numVertices = numVertices;
        adjMatrix = new int[numVertices][numVertices];
    }

    public void addEdge(int i, int j, int weight) {
        adjMatrix[i][j] = weight;
        adjMatrix[j][i] = weight;
    }

}