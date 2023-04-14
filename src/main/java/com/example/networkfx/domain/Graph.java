package com.example.networkfx.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Graph {
    private final int V;
    //adjacent List for the nodes
    ArrayList<ArrayList<Integer>> adjListArray;
    int[] dp = new int[100];

    public Graph(int V) {
        this.V = V;
        adjListArray = new ArrayList<>();

        // Create a new list for each vertex
        // such that adjacent nodes can be stored

        for (int i = 0; i < V + 5; i++) {
            adjListArray.add(i, new ArrayList<>());
        }
    }


    public void addEdge(int src, int dest) {
        adjListArray.get(src).add(dest);
        adjListArray.get(dest).add(src);
    }

    public void DFS(int v, boolean[] visited) {
        // Mark the current node as visited and print it
        visited[v] = true;
        int i = 0;
        // Recur for all the vertices
        // adjacent to this vertex
        for (int x : adjListArray.get(v)) {
            ++i;
            if (!visited[x])
                DFS(x, visited);

            //dp[x] = Math.max(dp[x], 1 + dp[adjListArray.get(x).get(i)]);
        }
    }

    public int connectedComponents() {
        int number = 0;
        boolean[] visited = new boolean[V+5];
        for (int v = 1; v <= V; ++v) {
            if (!visited[v]) {
                // print all reachable vertices
                // from v
                DFS(v, visited);
                ++number;
            }
        }
        return number;
    }

    public int longestPath(){
        int ans = 0;
        for (int i = 1; i <= V; i++) {
            ans = Math.max(ans, dp[i]);
        }
        return ans;
    }
}