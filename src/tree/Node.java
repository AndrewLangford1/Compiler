package tree;

import java.util.ArrayList;
import java.util.HashMap;

public class Node {
	
///Fields
	
	//symbol table data about this node.
	private HashMap<String, String> nodeData;
	
	//this nodes children
	private ArrayList<Node> children;
	
	//this nodes parent node
	private Node parent;
	
	
//Constructors
	
	//params:
	//parent => this node's parent node
	public Node(Node parent){
		this.nodeData = new HashMap<String, String>();
		this.children = new ArrayList<Node>();
		this.parent = parent;
	}
	
	
////Methods
	
	//returns this node's parent
	public Node getParent(){
		return this.parent;
	}
	
	
	//returns the data associated with this node (ie symbol table data)
	public HashMap<String,String> getNodeData(){
		return this.nodeData;
	}
	
	//add a child node to this node
	public void addChild(Node node){
		this.children.add(node);
	}
}
