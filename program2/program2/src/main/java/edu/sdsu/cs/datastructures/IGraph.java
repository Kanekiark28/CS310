package edu.sdsu.cs.datastructures;

import java.util.List;

public interface IGraph<V> {
    void add(V vertexName);
    void connect(V start, V destination);
    void clear();
    boolean contains(V label);
    void disconnect(V start, V destination);
    boolean isConnected(V start, V destination);
    Iterable<V> neighbors(V vertexName);
    void remove(V vertexName);
    List<V> shortestPath(V start, V destination);
    int size();
    Iterable<V> vertices();
    IGraph<V> connectedGraph(V origin);
}
