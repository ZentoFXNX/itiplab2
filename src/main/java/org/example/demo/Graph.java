package org.example.demo;

import java.util.*;

class Graph {
    private final Map<Integer, List<Edge>> adjList = new HashMap<>();
    private final Map<Integer, double[]> positions = new HashMap<>();
    private final Map<Integer, String> shapes = new HashMap<>();

    public void addEdge(int src, int dest, int weight) {
        adjList.computeIfAbsent(src, _ -> new ArrayList<>()).add(new Edge(dest, weight));
        adjList.computeIfAbsent(dest, _ -> new ArrayList<>()).add(new Edge(src, weight));
    }

    public void setPosition(int node, double x, double y, String shape) {
        positions.put(node, new double[]{x, y});
        shapes.put(node, shape);
    }

    public Map<Integer, List<Edge>> getAdjList() {
        return adjList;
    }

    public Map<Integer, double[]> getPositions() {
        return positions;
    }

    public String getShape(int node) {
        return shapes.getOrDefault(node, "circle");
    }

    public List<Integer> dijkstra(int start, int end) {
        Map<Integer, Integer> dist = new HashMap<>();
        Map<Integer, Integer> prev = new HashMap<>();
        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[1]));

        for (int node : adjList.keySet()) {
            dist.put(node, Integer.MAX_VALUE);
            prev.put(node, null);
        }
        dist.put(start, 0);
        pq.add(new int[]{start, 0});

        while (!pq.isEmpty()) {
            int[] current = pq.poll();
            int u = current[0];

            if (u == end) break;

            for (Edge edge : adjList.getOrDefault(u, Collections.emptyList())) {
                int v = edge.dest;
                int newDist = dist.get(u) == Integer.MAX_VALUE ? edge.weight : dist.get(u) + edge.weight;

                if (newDist < dist.get(v)) {
                    dist.put(v, newDist);
                    prev.put(v, u);
                    pq.add(new int[]{v, newDist});
                }
            }
        }

        List<Integer> path = new ArrayList<>();
        for (Integer at = end; at != null; at = prev.get(at)) {
            path.add(at);
        }
        Collections.reverse(path);

        return (path.size() == 1 && !path.contains(start)) ? Collections.emptyList() : path;
    }
}