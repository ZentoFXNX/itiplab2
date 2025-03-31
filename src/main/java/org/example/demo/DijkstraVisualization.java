package org.example.demo;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class DijkstraVisualization extends Application {
    private static final Graph graph = new Graph();
    private static Integer startNode = null;
    private static Integer endNode = null;

    public static void main(String[] args) {
        loadGraphFromFile();
        launch(args);
    }

    private static void loadGraphFromFile() {
        try (BufferedReader br = new BufferedReader(new FileReader("src/main/resources/org/example/demo/graph.txt"))) {
            int n = Integer.parseInt(br.readLine());
            for (int i = 0; i < n; i++) {
                String[] parts = br.readLine().split(" ");
                for (int j = 0; j < parts.length; j++) {
                    int weight = Integer.parseInt(parts[j]);
                    if (weight > 0) {
                        graph.addEdge(i, j, weight);
                    }
                }
            }
            for (int i = 0; i < n; i++) {
                String[] parts = br.readLine().split(" ");
                String shape = parts[0];
                double x = Double.parseDouble(parts[1]);
                double y = Double.parseDouble(parts[2]);
                graph.setPosition(i, x, y, shape);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start(Stage primaryStage) {
        Canvas canvas = new Canvas(600, 600);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        drawGraph(gc, new ArrayList<>());

        canvas.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            for (var entry : graph.getPositions().entrySet()) {
                double x = entry.getValue()[0];
                double y = entry.getValue()[1];
                if (Math.hypot(event.getX() - x, event.getY() - y) < 15) {
                    if (startNode == null) {
                        startNode = entry.getKey();
                    } else {
                        endNode = entry.getKey();
                        List<Integer> path = graph.dijkstra(startNode, endNode);
                        drawGraph(gc, path);
                        startNode = null;
                        endNode = null;
                    }
                    break;
                }
            }
        });

        primaryStage.setScene(new Scene(new StackPane(canvas), 600, 600));
        primaryStage.setTitle("Dijkstra Visualization");
        primaryStage.show();
    }

    private void drawGraph(GraphicsContext gc, List<Integer> path) {
        gc.clearRect(0, 0, 600, 600);
        gc.setFont(new Font(14));

        for (var entry : graph.getAdjList().entrySet()) {
            int src = entry.getKey();
            double[] srcPos = graph.getPositions().get(src);
            for (Edge edge : entry.getValue()) {
                double[] destPos = graph.getPositions().get(edge.dest);
                gc.setStroke(path.contains(src) && path.contains(edge.dest) ? Color.RED : Color.GRAY);
                gc.strokeLine(srcPos[0], srcPos[1], destPos[0], destPos[1]);
            }
        }

        for (var entry : graph.getPositions().entrySet()) {
            int node = entry.getKey();
            double x = entry.getValue()[0];
            double y = entry.getValue()[1];
            String shape = graph.getShape(node);

            gc.setFill(Color.LIGHTBLUE);
            gc.setStroke(Color.BLACK);
            if ("circle".equals(shape)) {
                gc.fillOval(x - 10, y - 10, 20, 20);
            } else if ("square".equals(shape)) {
                gc.fillRect(x - 10, y - 10, 20, 20);
            } else if ("triangle".equals(shape)) {
                gc.fillPolygon(new double[]{x, x - 10, x + 10}, new double[]{y - 10, y + 10, y + 10}, 3);
            }

            gc.strokeText(String.valueOf(node), x - 5, y - 15);
        }
    }
}