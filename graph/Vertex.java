package graph;

import list.*;

/**
 * Vertex class denote every vertex in the graph
 * @author Shuo
 *
 */
public class Vertex{
	public Object item;
	public List adjList;
	public Vertex(){
		item = null;
		adjList = new DList();
	}
	public Vertex(Object item){
		this.item = item;
		this.adjList = new DList();
	}
	public int neighborNum(){
		return adjList.length();
	}
	public void addEdge(Edge e){
		adjList.insertFront(e);
	}
	public void removeEdge(Edge e){
		
	}
	
}
