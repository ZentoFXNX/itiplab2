package org.example.demo;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

class GraphTest {

    @Test
    void testAddEdge() {
        Graph graph = new Graph();
        graph.addEdge(1, 2, 10);
        graph.addEdge(2, 3, 5);

        List<Edge> edgesFrom1 = graph.getAdjList().get(1);
        assertNotNull(edgesFrom1);
        assertEquals(1, edgesFrom1.size());
        assertEquals(2, edgesFrom1.getFirst().dest);
        assertEquals(10, edgesFrom1.getFirst().weight);

        List<Edge> edgesFrom2 = graph.getAdjList().get(2);
        assertNotNull(edgesFrom2);
        assertEquals(2, edgesFrom2.size());
        assertEquals(1, edgesFrom2.get(0).dest);
        assertEquals(10, edgesFrom2.get(0).weight);
        assertEquals(3, edgesFrom2.get(1).dest);
        assertEquals(5, edgesFrom2.get(1).weight);
    }

    @Test
    void testDijkstraNoPath() {
        Graph graph = new Graph();
        graph.addEdge(1, 2, 10);
        graph.addEdge(2, 3, 5);

        List<Integer> path = graph.dijkstra(1, 4);
        assertTrue(path.isEmpty(), "Path should be empty because there is no path between 1 and 4");
    }

    @Test
    void testDijkstra() {
        Graph graph = new Graph();
        graph.addEdge(1, 2, 10);
        graph.addEdge(2, 3, 5);
        graph.addEdge(1, 3, 15);

        List<Integer> path = graph.dijkstra(1, 3);
        System.out.println("Calculated path: " + path);

        assertTrue(path.equals(List.of(1, 2, 3)) || path.equals(List.of(1, 3)),
                "Path should be either [1, 2, 3] or [1, 3], but got: " + path);
    }

    @Test
    void testDijkstraMultiplePaths() {
        Graph graph = new Graph();
        graph.addEdge(1, 2, 10);
        graph.addEdge(2, 3, 5);
        graph.addEdge(1, 3, 20);

        List<Integer> path = graph.dijkstra(1, 3);
        System.out.println("Calculated path: " + path);

        assertEquals(List.of(1, 2, 3), path, "The shortest path should be [1, 2, 3]");
    }
}