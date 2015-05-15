package dataStructures;

import java.util.ArrayList;
/*
 * Node Class
 * Makes up nodes of either a CST or AST
 * 
 */
import java.util.HashMap;

public class Node {
	
//--Fields--//
	
	//this nodes children
	private ArrayList<Node> children;
	
	//this nodes parent node
	private Node parent;
	
	private boolean isLeafNode;
	
	
	//Some leaf nodes will carry Tokens from lexing phase
	private Token token;
	
	//face value of the node
	private String value;
	
	
	
	/**
	 *TODO
	 * This really shouldnt be here....only nodes in symbol table need this data but oh well.
	 * Need to go back and modularize this better! (if theres time
	 */
	private HashMap<String, SymbolEntry> entryData;
	private int scopeId;
	
	private SymbolEntry symbolTableData;
	
	
		
//--Constructors--//
	
	/**
	 * Default Constructor
	 */
	public Node(){
		this.children = new ArrayList<Node>();
		this.parent = null;
		this.entryData = new HashMap<String, SymbolEntry>();
		this.symbolTableData = null;
	}
	
	/**
	 * @param parent this node's parent node
	 */
	public Node(Node parent){
		this.children = new ArrayList<Node>();
		this.parent = parent;
		this.entryData = new HashMap<String, SymbolEntry>();
		this.symbolTableData = null;
	}
	
	
//--Methods--//
	


	/**
	 * @return the scopeId
	 */
	public int getScopeId() {
		return scopeId;
	}

	/**
	 * @param scopeId the scopeId to set
	 */
	public void setScopeId(int scopeId) {
		this.scopeId = scopeId;
	}

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
	
	public void setChildren(ArrayList<Node> children){
		this.children = children;
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
	
	
	/**
	 * 
	 * @return the entry data for this node
	 */
	public HashMap<String, SymbolEntry> getEntryData(){
		return this.entryData;
	}
	
	
	
	
	/**
	 * inserts an entry for this node
	 * 
	 * @param idKey the key for this entry
	 * @param value the  Symbol entry value associated with the key
	 * @return true if insertion was successful, false otherise
	 */
	public boolean addSymbolEntry(String idKey, SymbolEntry value){
		if(!hasSymbolEntry(idKey)){
			entryData.put(idKey, value);
			return true;
		}
		else{
			return false;
		}
	}
	
	
	/**
	 * 
	 * @param idKey the key for the symbolEntry
	 * @return the SymbolEntry if it exists, null otherwise.
	 */
	public SymbolEntry getSymbolEntry(String idKey){
		return entryData.get(idKey);
	}
	
	
	/**
	 * 
	 * @param key the entry key to check
	 * @return true if the entryData contains this key, false if it doesn't.
	 */
	public boolean hasSymbolEntry(String key){
		if(entryData.get(key)==null)
			return false;
		else
			return true;
	}
	
	
	/**
	 * 
	 * 
	 * @return true if the node has a parent, false otherwise
	 */
	public boolean hasParent(){
		if(this.parent == null){
			return false;
		}
		else{
			return true;
		}
		
	}

	/**
	 * @return the symbolTableData
	 */
	public SymbolEntry getSymbolTableData() {
		return symbolTableData;
	}

	/**
	 * @param symbolTableData the symbolTableData to set
	 */
	public void setSymbolTableData(SymbolEntry symbolTableData) {
		this.symbolTableData = symbolTableData;
	}

}
