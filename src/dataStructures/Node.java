package dataStructures;

import java.util.ArrayList;
/*
 * Node Class
 * Makes up nodes of either a CST or AST
 * 
 */

public class Node {
	
//--Fields--//
	
	//this nodes children
	private ArrayList<Node> children;
	
	//this nodes parent node
	private Node parent;
	
	private boolean isLeafNode;
	
	private Token token;
	
	private String value;

	
	
//--Constructors--//
	
	/**
	 * Default Constructor
	 */
	public Node(){
		this.children = new ArrayList<Node>();
		this.parent = null;
	}
	
	/**
	 * @param parent this node's parent node
	 */
	public Node(Node parent){
		this.children = new ArrayList<Node>();
		this.parent = parent;
	}
	
	
//--Methods--//
	
	/**
	 * returns the parent node of this node
	 * @return Node, the parent node of this node
	 * 
	 */
	public Node getParent(){
		return this.parent;
	}
	
	/**
	 * sets the parent node of this node
	 * @param parent the parent node
	 */
	public void setParent(Node parent){
		this.parent = parent;
	}
	
	/**
	 * 
	 * @return true if the node is a leaf node, false if otherwise
	 */
	public boolean isLeafNode() {
		return isLeafNode;
	}

	
	/**
	 * @param isLeafNode set true if this node is a leaf node, false if otherwise
	 */
	public void setLeafNode(boolean isLeafNode) {
		this.isLeafNode = isLeafNode;
	}
	
	/**
	 * get the title of this node
	 * @return the title of this node
	 */
	public String getValue() {
		return this.value;
	}

	
	/**
	 * 
	 * @param value the node value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}
	

	
	/**
	 * add a child node to this node.
	 * @param  node to add to this nodes children
	 * 
	 */
	public void addChild(Node node){
		this.children.add(node);
	}
	
	/**
	 * 
	 * @return the child nodes of this node
	 */
	public ArrayList<Node> getChildren() {
		return children;
	}

	/**
	 * @return the token
	 */
	public Token getToken() {
		return token;
	}

	/**
	 * @param token the token to set
	 */
	public void setToken(Token token) {
		this.token = token;
	}
	
	/**
	 * 
	 * @return true if this node contains child nodes
	 */
	public boolean hasChildren(){
		return !(this.children.isEmpty());
	}
	
	
}
