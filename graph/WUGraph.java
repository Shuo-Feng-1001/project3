/* WUGraph.java */

package graph;

import dict.Entry;
import dict.HashTableChained;
import list.DList;
import list.InvalidNodeException;
import list.List;
import list.ListNode;

/**
 * The WUGraph class represents a weighted, undirected graph.  Self-edges are
 * permitted.
 */

public class WUGraph {
	
	private int vertexNum;
	private int edgeNum;
	/**
	 * actually vertexList is the list of list, each vertex is the listnode of the list
	 * meanwhile, each node also signifying a list, which records its vertex's neighbors
	 * neighbors, each edge connected vertex
	 * Attention, each list's length do not mean the degree, should minus 1 
	 */
	private List vertexList;
	private HashTableChained vertexTable;
	private HashTableChained edgeTable;

  /**
   * WUGraph() constructs a graph having no vertices or edges.
   *
   * Running time:  O(1).
   */
  public WUGraph(){
	  vertexNum = 0;
	  edgeNum = 0;
	  vertexList = new DList();
	  vertexTable = new HashTableChained();
	  edgeTable = new HashTableChained();
  }

  /**
   * vertexCount() returns the number of vertices in the graph.
   *
   * Running time:  O(1).
   */
  public int vertexCount(){
	  return vertexNum;
  }

  /**
   * edgeCount() returns the total number of edges in the graph.
   *
   * Running time:  O(1).
   */
  public int edgeCount(){
	  return edgeNum;
  }

  /**
   * getVertices() returns an array containing all the objects that serve
   * as vertices of the graph.  The array's length is exactly equal to the
   * number of vertices.  If the graph has no vertices, the array has length
   * zero.
   *
   * (NOTE:  Do not return any internal data structure you use to represent
   * vertices!  Return only the same objects that were provided by the
   * calling application in calls to addVertex().)
   *
   * Running time:  O(|V|).
   */
  public Object[] getVertices(){
	  Object[] vert = new Object[vertexNum];
	  int count = 0;
	  ListNode curr = this.vertexList.front();
	  try{
		  while(curr.isValidNode()){
			  Object obj = ((List)curr.item()).front().item();
			  vert[count++] = obj;
			  curr = curr.next();
		  }
	  }catch(InvalidNodeException e){
		  e.printStackTrace();
	  }
	  return vert;
  }

  /**
   * addVertex() adds a vertex (with no incident edges) to the graph.
   * The vertex's "name" is the object provided as the parameter "vertex".
   * If this object is already a vertex of the graph, the graph is unchanged.
   *
   * Running time:  O(1).
   */
  public void addVertex(Object vertex){
	  List lst  = new DList();
	  Entry entry = this.vertexTable.find(vertex);
	  if(entry == null){
		  lst.insertBack(vertex);
	  // we use two data structures to store the vertices
		  this.vertexList.insertBack(lst);
		  this.vertexTable.insert(vertex, this.vertexList.back());
		  this.vertexNum++;
	  }
  }

  /**
   * removeVertex() removes a vertex from the graph.  All edges incident on the
   * deleted vertex are removed as well.  If the parameter "vertex" does not
   * represent a vertex of the graph, the graph is unchanged.
   *
   * Running time:  O(d), where d is the degree of "vertex".
   */
  public void removeVertex(Object vertex){
	  Entry entry = this.vertexTable.find(vertex);
	  try{
		  if(entry == null){
			  return;
		  }else{
			   ListNode node = (ListNode)entry.value();
			   List lst = (List) node.item();
			   lst.front().remove();
			   while(!lst.isEmpty()){
				   Edge curr = (Edge)lst.front().item();
				   // consider self edge condition
				   if(curr.getListNode1() == curr.getListNode2()){
					   curr.getListNode1().remove();
				   }else{
					   curr.getListNode1().remove();
					   curr.getListNode2().remove();
				   }
				   this.edgeNum--;
			   }
			   node.remove();
			   this.vertexTable.remove(vertex);
			   this.vertexNum--;
		  }
	  }catch(InvalidNodeException e){
		  e.printStackTrace();
	  }
  }

  /**
   * isVertex() returns true if the parameter "vertex" represents a vertex of
   * the graph.
   *
   * Running time:  O(1).
   */
  public boolean isVertex(Object vertex){
	  Entry entry = this.vertexTable.find(vertex);
	  if(entry != null){
		  return true;
	  }
	  return false;
  }

  /**
   * degree() returns the degree of a vertex.  Self-edges add only one to the
   * degree of a vertex.  If the parameter "vertex" doesn't represent a vertex
   * of the graph, zero is returned.
   *
   * Running time:  O(1).
   */
  public int degree(Object vertex){
	  if(isVertex(vertex)){
		  Entry entry = this.vertexTable.find(vertex);
		  try{
			  List lst = (List)((ListNode)entry.value()).item();
			  return (lst.length()-1);
		  }catch(InvalidNodeException e){
			  e.printStackTrace();
		  }  
	  }
	  return 0;
  }

  /**
   * getNeighbors() returns a new Neighbors object referencing two arrays.  The
   * Neighbors.neighborList array contains each object that is connected to the
   * input object by an edge.  The Neighbors.weightList array contains the
   * weights of the corresponding edges.  The length of both arrays is equal to
   * the number of edges incident on the input vertex.  If the vertex has
   * degree zero, or if the parameter "vertex" does not represent a vertex of
   * the graph, null is returned (instead of a Neighbors object).
   *
   * The returned Neighbors object, and the two arrays, are both newly created.
   * No previously existing Neighbors object or array is changed.
   *
   * (NOTE:  In the neighborList array, do not return any internal data
   * structure you use to represent vertices!  Return only the same objects
   * that were provided by the calling application in calls to addVertex().)
   *
   * Running time:  O(d), where d is the degree of "vertex".
   */
  public Neighbors getNeighbors(Object vertex){
	  if(!this.isVertex(vertex)  || degree(vertex) ==0){
		  return null;
	  }
	  Neighbors neigh = new Neighbors();
	  neigh.neighborList = new Object[degree(vertex)];
	  neigh.weightList = new int[degree(vertex)];
	  // degree !=0
	  Entry entry = this.vertexTable.find(vertex); 
	  try{
		  List lst = (List)((ListNode)entry.value()).item();
		  int count = 0;
		  ListNode neighNode = (ListNode)lst.front().next();
		  while(neighNode.isValidNode()){
			  Edge edge = (Edge)neighNode.item();
			  neigh.weightList[count] = edge.getWeight();
			  if(edge.vertex1() != vertex){
				  neigh.neighborList[count] = edge.vertex1();
			  }else{
				  neigh.neighborList[count] = edge.vertex2();
			  }
			  count++;
			  neighNode = neighNode.next();
		  }
	  }catch(InvalidNodeException e){
		  e.printStackTrace();
	  }
	  return neigh;
  }

  /**
   * addEdge() adds an edge (u, v) to the graph.  If either of the parameters
   * u and v does not represent a vertex of the graph, the graph is unchanged.
   * The edge is assigned a weight of "weight".  If the graph already contains
   * edge (u, v), the weight is updated to reflect the new value.  Self-edges
   * (where u.equals(v)) are allowed.
   *
   * Running time:  O(1).
   */
  public void addEdge(Object u, Object v, int weight){
	  Entry e1 = this.vertexTable.find(u);
	  Entry e2 = this.vertexTable.find(v);  
	  try{
		  if(e1!=null && e2!=null){
			  VertexPair vp = new VertexPair(u,v);
			  if(isEdge(u,v)){
				  Edge old = (Edge)this.edgeTable.find(vp).value();
				  old.setWeight(weight);
			  }else{
				  Edge edge = new Edge(u,v,weight);
				  // if the edge is self-edge
				  List u1 = (List)((ListNode)e1.value()).item();
				  List v1 = (List)((ListNode)e2.value()).item();
				  if(e1 == e2){
					  u1.insertBack(edge);
					  edge.listnode(u1.back(), u1.back());
				  }else{
					  v1.insertBack(edge);
					  u1.insertBack(edge);
					  edge.listnode(v1.back(), u1.back());
				  }
				  this.edgeTable.insert(vp, edge);
				  this.edgeNum++;
			  }
		  }
	  }catch(InvalidNodeException e){
		  e.printStackTrace();
	  }
  }

  /**
   * removeEdge() removes an edge (u, v) from the graph.  If either of the
   * parameters u and v does not represent a vertex of the graph, the graph
   * is unchanged.  If (u, v) is not an edge of the graph, the graph is
   * unchanged.
   *
   * Running time:  O(1).
   */
  public void removeEdge(Object u, Object v){
	  if(isEdge(u,v)){
		  VertexPair vp = new VertexPair(u,v);
		  Entry entry = this.edgeTable.find(vp);
		  Edge edgeNode = (Edge)entry.value();
		  ListNode node1 = edgeNode.getListNode1();
		  ListNode node2 = edgeNode.getListNode2();
		  try{
			  // need to consider self-edge condition, if that
			  // happen, we cannot remove the same edge twice
			  if(node1 == node2){
				  node1.remove();
			  }else{
				  node1.remove();
				  node2.remove();  
			  }
		  }catch(InvalidNodeException e){
			  e.printStackTrace();
		  }
		  this.edgeTable.remove(vp);
		  this.edgeNum--;
	  }
  }

  /**
   * isEdge() returns true if (u, v) is an edge of the graph.  Returns false
   * if (u, v) is not an edge (including the case where either of the
   * parameters u and v does not represent a vertex of the graph).
   *
   * Running time:  O(1).
   */
  public boolean isEdge(Object u, Object v){
	  Entry e1 = this.vertexTable.find(u);
	  Entry e2 = this.vertexTable.find(v);     
	  if(e1!=null && e2!=null){
		  VertexPair vp = new VertexPair(u,v);
		  Entry entry = this.edgeTable.find(vp);  
		  if(entry != null){
			  return true;
		  }
	  }
	  return false;
  }

  /**
   * weight() returns the weight of (u, v).  Returns zero if (u, v) is not
   * an edge (including the case where either of the parameters u and v does
   * not represent a vertex of the graph).
   *
   * (NOTE:  A well-behaved application should try to avoid calling this
   * method for an edge that is not in the graph, and should certainly not
   * treat the result as if it actually represents an edge with weight zero.
   * However, some sort of default response is necessary for missing edges,
   * so we return zero.  An exception would be more appropriate, but also more
   * annoying.)
   *
   * Running time:  O(1).
   */
  public int weight(Object u, Object v){
	  if(isEdge(u,v)){
		  VertexPair vp = new VertexPair(u,v);
		  Entry entry = this.edgeTable.find(vp);  
		  return ((Edge)entry.value()).getWeight();
	  }
	  return 0;
  }

}
