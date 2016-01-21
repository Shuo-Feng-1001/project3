package graph;

import list.ListNode;

public class Edge {
	private Object vertex1;
	private Object vertex2;
	private ListNode listnode1;
	private ListNode listnode2;
	private int weight;
	
	public Object vertex1(){
		return this.vertex1;
	}
	public Object vertex2(){
		return this.vertex2;
	}
	
	public Edge(Object vertex1,Object vertex2,int weight){
		this.vertex1 = vertex1;
		this.vertex2 = vertex2;
		this.weight = weight;
		listnode1 = null;
		listnode2 = null;
	}
	public Edge(VertexPair vp, int weight){
		this.vertex1 = vp.object1;
		this.vertex2 = vp.object2;
		this.weight = weight;
		listnode1 = null;
		listnode2 = null;
	}
	public void listnode(ListNode ln1, ListNode ln2){
		this.listnode1 = ln1;
		this.listnode2 = ln2;
	}
	
	public ListNode getListNode1(){
		return this.listnode1;
	}
	
	public ListNode getListNode2(){
		return this.listnode2;
	}
	
	public int getWeight(){
		return weight;
	}
	public void setWeight(int weight){
		this.weight = weight;
	}
}
