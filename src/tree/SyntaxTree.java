package tree;

public class SyntaxTree {
	
//Fields	
	private Node root;
	private Node currentNode;

	
	
//Constructors
	
	//default constructor
	public SyntaxTree(){
		this.root = null;
		this.currentNode = null;
	}
	
	
	//set the root node of this tree
	public void setRoot(Node node){
		this.root = node;
	}
	
	
	//set the current node
	public void setCurrent(Node node){
		this.currentNode = node;	
	}
	
	
	//return to the current node's parent
	public void returnToParent(){
		this.currentNode = currentNode.getParent();
	}
	
	
}
