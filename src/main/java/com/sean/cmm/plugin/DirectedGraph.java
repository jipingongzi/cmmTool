package com.sean.cmm.plugin;

import java.util.*;

public class DirectedGraph {
    private int vertices; // 顶点数量
    private Map<Integer, List<Integer>> adjacencyList; // 邻接表

    public DirectedGraph(int vertices) {
        this.vertices = vertices;
        this.adjacencyList = new HashMap<>();
        for (int i = 0; i < vertices; i++) {
            adjacencyList.put(i, new LinkedList<>());
        }
    }

    // 添加有向边
    public void addEdge(int source, int destination) {
        List<Integer> neighbors = adjacencyList.get(source);
        neighbors.add(destination);
    }

    // 打印图的邻接表
    public void printGraph() {
        for (int i = 0; i < vertices; i++) {
            List<Integer> neighbors = adjacencyList.get(i);
            System.out.println("顶点 " + i + " 的邻居: " + neighbors);
        }
    }

    public static void main(String[] args) {
        int vertices = 5;
        DirectedGraph graph = new DirectedGraph(vertices);
        graph.addEdge(0, 1);
        graph.addEdge(0, 4);
        graph.addEdge(1, 2);
        graph.addEdge(1, 3);
        graph.addEdge(1, 4);
        graph.addEdge(2, 3);
        graph.addEdge(3, 4);

        graph.printGraph();
    }
}
