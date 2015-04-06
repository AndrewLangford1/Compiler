package dataStructures;

import java.util.ArrayList;
import java.util.HashMap;

/*
 * Node Class
 * Makes up nodes of either a CST or AST
 * 
 */

public class Node {
	
//--Fields--//
	
	//symbol table data about this node.
	private HashMap<String, String> nodeData;
	
	//this nodes children
	private ArrayList<Node> children;
	
	//this nodes parent node
	private Node parent;
	
	
//--Constructors--//
	
	
	public Node(){
		this.nodeData = new HashMap<String, String>();
		this.children = new ArrayList<Node>();
		this.parent = null;
	}
	
	/*
	 * @params parent => this node's parent node
	 */
	public Node(Node parent){
		this.nodeData = new HashMap<String, String>();
		this.children = new ArrayList<Node>();
		this.parent = parent;
	}
	
	
//--Methods--//
	
	/*
	 * returns the parent node of this node
	 * @return Node, the parent node of this node
	 * 
	 */
	public Node getParent(){
		return this.parent;
	}
	
	public void setParent(Node parent){
		this.parent = parent;
	}
	
	
	/*
	 * returns the data associated with this node (ie symbol table data)
	 * @return HashMap of node data
	 */
	public HashMap<String,String> getNodeData(){
		return this.nodeData;
	}
	
	public void addEntry(String key, String value){
		this.nodeData.put(key, value);
	}
	
	/*
	 * add a child node to this node.
	 * @params Node node, the node to add to this nodes children
	 * 
	 */
	public void addChild(Node node){
		this.children.add(node);
	}

	public ArrayList<Node> getChildren() {
		return children;
	}
	
	public String toString(){
		return this.nodeData.get("title");
	}
	
}
