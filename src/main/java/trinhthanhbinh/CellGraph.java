package trinhthanhbinh;

import java.util.Iterator;
import java.util.LinkedList;

public class CellGraph {
	private int noOfVertices; 
	private LinkedList<Integer> nodeList[]; // Adjacency List Represntation

	// Constructor
	CellGraph(int v) {
		noOfVertices = v;
		nodeList = new LinkedList[v];
		for (int i = 0; i < v; ++i)
			nodeList[i] = new LinkedList();
	}

	/**
	 * add an edge into the graph
	 * @param v
	 * @param w
	 */
	void addEdge(int v, int w) {
		nodeList[v].add(w);
		nodeList[w].add(v);
	}
	
	public boolean isExistedEdge(int a, int b) {
		boolean isExisted = false;
		Iterator<Integer> it = nodeList[a].iterator();
		Integer other;
		while (it.hasNext()) {
			other = it.next();
			if(other.intValue() == b) {
				isExisted = true;
				break;
			}
		}
		return isExisted;
	}
	

	/**
	 * A recursive function that uses visited[] and parent to detect cycle in subgraph reachable from vertex v.
	 * @param v
	 * @param visited
	 * @param parent
	 * @return
	 */
	Boolean isCyclicUtil(int v, Boolean visited[], int parent) {
		// Mark the current node as visited
		visited[v] = true;
		Integer i;

		// Recur for all the vertices adjacent to this vertex
		Iterator<Integer> it = nodeList[v].iterator();
		while (it.hasNext()) {
			i = it.next();

			// If an adjacent is not visited, then recur for that adjacent
			if (!visited[i]) {
				if (isCyclicUtil(i, visited, v))
					return true;
			}

			// If an adjacent is visited and not parent of current vertex, then there is a cycle.
			else if (i != parent)
				return true;
		}
		return false;
	}

	// Returns true if the graph contains a cycle, else false.
	Boolean isCyclic() {
		// Mark all the vertices as not visited and not part of recursion stack
		Boolean visited[] = new Boolean[noOfVertices];
		for (int i = 0; i < noOfVertices; i++)
			visited[i] = false;

		// Call the recursive helper function to detect cycle in different DFS trees
		for (int u = 0; u < noOfVertices; u++)
			if (!visited[u]) // Don't recur for u if already visited
				if (isCyclicUtil(u, visited, -1))
					return true;

		return false;
	}

	public LinkedList<Integer>[] getNodeList() {
		return nodeList;
	}

}
