package edu.sdsu.cs;

import edu.sdsu.cs.datastructures.DirectedGraph;
import edu.sdsu.cs.datastructures.IGraph;

import java.io.*;

public class App {

    public static String INPUT_FILE = "layout.csv";

    private static IGraph<String> graph = new DirectedGraph<>();

    public static void main(String[] args) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(INPUT_FILE));
            String line = reader.readLine();
            while (line != null) {
                String nodes[] = line.split(",");
                if (nodes.length == 1) {
                    graph.add(nodes[0]);
                } else {
                    graph.connect(nodes[0], nodes[1]);
                }
                line = reader.readLine();
            }
            reader.close();

            System.out.println(graph);

            reader = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Enter source location: ");
            String source = reader.readLine();
            System.out.println("Enter destination location: ");
            String destination = reader.readLine();

            System.out.println("Shortest path from " + source + " to " + destination + " is \n" + graph.shortestPath(source, destination));

        } catch (FileNotFoundException fnfe) {
            System.out.println("Unable to open " + INPUT_FILE + ". Verify the file exists, is\n" +
                    "accessible, and meets the syntax requirements");
        } catch (IOException ioe) {
            System.out.println("Eror while reading file's content");
        }


    }


}
