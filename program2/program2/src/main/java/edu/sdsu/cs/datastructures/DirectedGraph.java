package edu.sdsu.cs.datastructures;

import java.util.*;

public class DirectedGraph<V> implements IGraph<V> {


    private TreeMap<V, TreeSet<V>> graph;
    private static boolean isConnected=false;
    private static IGraph connectedGraph = new DirectedGraph<>();

    public DirectedGraph() {
        graph = new TreeMap<>();
    }

    @Override
    public void add(V vertexName) {
        if (!contains(vertexName)) {
            graph.put(vertexName, new TreeSet<>());
        }
    }

    @Override
    public void connect(V start, V destination) {
        if (contains(start) && contains(destination)) {
            graph.get(start).add(destination);
        } else {
            throw new NoSuchElementException(start + " and " + destination + " does not exist in graph");
        }
    }

    @Override
    public void clear() {
        graph.clear();
    }

    @Override
    public boolean contains(V label) {
        return graph.containsKey(label);
    }

    @Override
    public void disconnect(V start, V destination) {
        if (contains(start) && contains(destination)) {
            graph.get(start).remove(destination);
        } else {
            throw new NoSuchElementException(start + " and " + destination + " does not exist in graph");
        }
    }

    @Override
    public boolean isConnected(V start, V destination) {
        if (contains(start) && contains(destination)) {

            checkIfNodesAreConnected(start,destination);

        } else {
            throw new NoSuchElementException(start + " and " + destination + " does not exist in graph");
        }
        return isConnected;
    }
    private void checkIfNodesAreConnected(V start, V destination){
        List<V> target = new ArrayList<>();
        neighbors(start).iterator().forEachRemaining(target::add);
        if(target.size()!=0){
            for(int i=0;i<target.size();i++){
                if(contains(target.get(i))){
                    if (target.get(i).equals(destination)) {
                        isConnected = true;
                    } else {
                        isConnected = false;
                        checkIfNodesAreConnected(target.get(i), destination);
                    }
                }
            }
        }
        else{
            isConnected = false;
        }
    }
    @Override
    public Iterable<V> neighbors(V vertexName) {
        if(contains(vertexName))
            return graph.get(vertexName);
        else{
            throw new NoSuchElementException("Source node does not exist");
        }

    }

    @Override
    public void remove(V vertexName) {
        if(contains(vertexName)) {
            graph.remove(vertexName);

            Iterator<V> itr=graph.keySet().iterator();
            while (itr.hasNext()){
                V v=itr.next();
                graph.get(v).remove(vertexName);
            }

        }
        else
            throw new NoSuchElementException("Source node does not exist");
    }

    @Override
    public List<V> shortestPath(V start, V destination) {
        if (contains(start) && contains(destination)) {
            Dijkstra<V> dijkstra = new Dijkstra<>(this, start);
            return dijkstra.calculateShortestPath(destination);
        } else {
            throw new NoSuchElementException(start + " and " + destination + " does not exist in graph");
        }
    }

    @Override
    public int size() {
        return graph.size();
    }

    @Override
    public Iterable<V> vertices() {
        return graph.keySet();
    }

    @Override
    public IGraph<V> connectedGraph(V origin) {
        buildConnectedGraph(origin);
    return connectedGraph;
    }
    private void buildConnectedGraph(V origin) {
        for (V v : neighbors(origin)) {
            connectedGraph.add(origin);
            connectedGraph.add(v);
            connectedGraph.connect(origin, v);
            buildConnectedGraph(v);
        }
    }
    private static class Dijkstra<V> {
        private TreeMap<V, V> map1 = new TreeMap<>();
        private TreeMap<V, Integer> map2 = new TreeMap<>();

        private Dijkstra(DirectedGraph<V> graph, V origin) {

            Queue<V> queue = new PriorityQueue<>();
            queue.add(origin);
            map2.put(origin, 0);

            while (!queue.isEmpty()) {
                V v1 = queue.remove();

                List<V> target = new ArrayList<>();
                graph.neighbors(v1).iterator().forEachRemaining(target::add);

                for(int i=0;i<target.size();i++){
                    if (!map2.containsKey(target.get(i))) {
                        queue.add(target.get(i));
                        map2.put(target.get(i), 1 + map2.get(v1));
                        map1.put(target.get(i), v1);
                    }
                }

            }
        }

        private List<V> calculateShortestPath(V destination) {
            List<V> path = new ArrayList<>();
            while (destination != null && map2.containsKey(destination)) {
                path.add(destination);
                destination = map1.get(destination);
            }
            Collections.reverse(path);
            return path;
        }

    }

    public String toString() {

        StringBuilder s = new StringBuilder();
        for (V v1 : graph.keySet()) {
            s.append(v1 + ": ");
            for (V v2 : graph.get(v1)) {
                s.append(v2 + ", ");
            }
            List<V> target = new ArrayList<>();
            neighbors(v1).iterator().forEachRemaining(target::add);
            s.append("\nDegree of "+v1+": "+target.size());
            s.append('\n');
        }
        return s.toString();
    }
}
