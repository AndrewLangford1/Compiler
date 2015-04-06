package dataStructures;

import java.util.HashMap;
import java.util.Map.Entry;


public class SyntaxTree {
	
//--Fields--//	
	
	//root node of the tree
	private Node root;
	
	//current node
	private Node currentNode;

	
	
//--Constructors--//
	
	//default constructor
	public SyntaxTree(){
		this.root = null;
		this.currentNode = null;
	}
	
	
	/*
	 * @params root => the node that will be the root of the tree
	 */
	public SyntaxTree(Node root){
		this.root = root;
		this.currentNode = root;
		
	}
	
	
//--Methods--//
	
	
	/*
	 * Set the root node of the tree
	 * @params node => the node to be the root of the tree
	 * 
	 */
	public void setRoot(Node node){
		this.root = node;
	}
	
	
	/*
	 * returns the root node of the tree
	 * @return the root node of the tree
	 */
	public Node getRoot(){
		return this.root;
	}
	
	
	/*
	 *returns the current node we are looking at
	 *@return the current node we are looking at 
	 */
	public Node getCurrent(){
		return this.currentNode;
		
	}
	
	
	/*
	 * set the current node we want to look at 
	 *
	 */
	public void setCurrent(Node node){
		this.currentNode = node;	
	}
	
	
	/*
	 * set the current node to the current node's parent
	 */
	public void returnToParent(){
		this.currentNode = currentNode.getParent();
	}
	
	public void addBranchNode(String title){
				
		//create a new node
		Node branchNode = new Node();
		branchNode.addEntry("title", title);
		
		
		//if there's no root node, make this node the root
		if(this.root == null){
			this.root = branchNode;
			
			
			
		}
		
		//if we aren't root, add ourselves to our parent's children and update current to us.
		else{
			//set this new nodes parent
			branchNode.setParent(this.currentNode);
			
			//add this node to parent's children
			this.currentNode.addChild(branchNode);
			
		}
	
		//current node is now new branchnode
		this.currentNode = branchNode;
	}
	
	public void addLeafNode(String title, HashMap<String, String> tokenData){
		//create a new node
		Node leafNode = new Node();
		
		//set the title of this node for printing purposes
		leafNode.addEntry("title", title);
		
		//copy relevant token data into this node;
		for(Entry<String, String> entry : tokenData.entrySet()){
			leafNode.addEntry(entry.getKey(), entry.getValue());
		}
		
		if(this.root == null){
			//TODO should raise an error
			
		}
		
		//if we aren't root, add ourselves to our parent's children
		else{
			//set this new nodes parent
			leafNode.setParent(this.currentNode);
			
			//add this node to parent's children
			this.currentNode.addChild(leafNode);
			
		}
	}

	
	public void print(Node node, int level){
		String toPrint = "";
		for(int i =0; i<level; i++){
			toPrint += "  ";
		}
		System.out.println(toPrint+ node.toString());
		if(node.getChildren().isEmpty()){
			return;
		}
		else{
			level += 1;
			for(Node x : node.getChildren()){
				this.print(x, level);
			}
		}
	}
	
	
	
	public void iterate(Node node){
		if(node.getChildren().isEmpty()){
			return;
		}
		else{
			for(Node x : node.getChildren()){
				iterate(x);
			}
		}
	}
	
	
}
