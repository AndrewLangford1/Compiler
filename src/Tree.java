


/**
 * Class to represent a Syntax Tree
 * 
 */



public class Tree{
	
//--Fields--//	
	
	//root node of the tree
	private Node root;
	
	//current node
	private Node currentNode;

	
	
//--Constructors--//`
	
	/**
	 * default constructor
	 */
	public Tree(){
		this.root = null;
		this.currentNode = null;
	}
	
	
	/**
	 * Constructor that takes a node
	 * @param root  the node that will be the root of the tree
	 */
	public Tree(Node root){
		this.root = root;
		this.currentNode = root;
		
	}
	
	
//--Methods--//
	
	
	/**
	 * Set the root node of the tree
	 * @param node the node to be the root of the tree
	 * 
	 */
	public void setRoot(Node node){
		this.root = node;
	}
	
	
	/**
	 * @return the root node of the tree
	 */
	public Node getRoot(){
		return this.root;
	}
	
	
	/**
	 *@return the current node we are looking at
	 */
	public Node getCurrent(){
		return this.currentNode;
	}
	
	
	/**
	 * set the current node we want to look at 
	 *
	 */
	public void setCurrent(Node node){
		this.currentNode = node;	
	}
	
	
	/**
	 * set the current node to the current node's parent
	 */
	public void returnToParent(){
		if(!(currentNode.getParent() == null)){
			this.currentNode = currentNode.getParent();
		}
	}
	
	
	/**
	 * Add a branch node to this tree as a child of the current node.  
	 * note: all branch nodes are non terminals in this grammar for the CST
	 *boolop and intop are branch nodes in the AST
	 * @param0 value the value of this node 
	 */
	public void addBranchNode(String value){
				
		//create a new node
		Node branchNode = new Node();
		branchNode.setValue(value);
		branchNode.setLeafNode(false);
		
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
		
	
	/**
	 * Add a branch node to this tree as a child of the current node.  
	 * note: all branch nodes are non terminals in this grammar.
	 * @param value the value of this node , token
	 */
	public void addBranchNode(String value, Token token){
				
		//create a new node
		Node branchNode = new Node();
		branchNode.setValue(value);
		branchNode.setLeafNode(false);
		branchNode.setToken(token);
		
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
	

	/**
	 * @
	 * Add a leaf node to the tree as a child of the current node.
	 * @param value the value of this node
	 * @param token the token
	 * note all leaf nodes are terminals in the grammar.
	 */
	public void addLeafNode(String value, Token token){
		//create a new node
		Node leafNode = new Node();
		
		//set the title of this node for printing purposes
		leafNode.setValue(value);
		leafNode.setToken(token);
		leafNode.setLeafNode(true);
		
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
	
	
	
	
	/**
	 * @
	 * Add a leaf node to the tree as a child of the current node.
	 * @param value the value of this node
	 * @param token the token
	 * note all leaf nodes are terminals in the grammar.
	 */
	public void addLeafNode(String value){
		//create a new node
		Node leafNode = new Node();
		
		//set the title of this node for printing purposes
		leafNode.setValue(value);
		leafNode.setLeafNode(true);
		
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
		
	
	/**
	 * Simply calls the printLevel method
	 * 
	 */
	public void print(){
		try {
			printLevel(this.getRoot(), 0);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Error printing");
		}
	}
		
	/**
	 * prints the tree out in a nested tree fashion.
	 * @param node the node to start at
	 * @param level the level of the tree
	 */
	private void printLevel(Node node, int level){
		try {
			String toPrint = "";
			for(int i =0; i<level; i++){
				toPrint += "  ";
			}
			System.out.println(toPrint+ node.getValue());
			if(node.getChildren().isEmpty()){
				return;
			}
			else{
				level += 1;
				for(Node x : node.getChildren()){
					this.printLevel(x, level);
				}
			}
		} catch (Exception e) {
			System.out.println("Error printing tree at level " + level + " at node " + node.getValue());
			e.printStackTrace();
		}
	}	
}
