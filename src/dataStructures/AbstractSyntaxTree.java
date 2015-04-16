package dataStructures;

import java.util.ArrayList;


/**
 * 
 * @author Andrew Langford
 * 
 * Class to convert CSTs to ASTs
 *
 */

public class AbstractSyntaxTree extends Tree {
	
//--Fields--//

	//the CST we'll use to generate the AST
	Tree concreteSyntaxTree;
	
	
	
	/**
	 * Default Constructor
	 * @param concreteSyntaxTree the CST to convert into an AST
	 * 
	 */
	public AbstractSyntaxTree(Tree concreteSyntaxTree){
		super();
		this.concreteSyntaxTree = concreteSyntaxTree;
		this.cstToAst(concreteSyntaxTree.getRoot());
	}
	

	
	
//--Methods--//
	/**
	 * Should only be called by the constructor.
	 * 
	 * @param currentCstNode the current node we are visiting on the cst
	 */
	private void cstToAst(Node currentCstNode){
		try {
			//grab the nodes children
			ArrayList<Node> nodeChildren =  currentCstNode.getChildren();
			
			//get the value of the Nod
			String currentCstNodeValue = currentCstNode.getValue();
			
			//
			switch(currentCstNodeValue){
			
				case("Program"):{
					//dont care whatsoever, just get the first block
					cstToAst(nodeChildren.get(0));
				}
				
				break;
				
				case("Block"):{
					addBranchNode(currentCstNodeValue);
					if(currentCstNode.hasChildren())
						cstToAst(nodeChildren.get(1));
					
					
					returnToParent();
				}
				
				break;
				
				case("StatementList") : {
					if(currentCstNode.hasChildren()){
						//convert first Statement
						cstToAst(nodeChildren.get(0));	
						//convert rest of statements
						cstToAst(nodeChildren.get(1));
					}
				}
				
				break;
				
				case("Statement") :{
					//dont need a new node but need its child
					cstToAst(nodeChildren.get(0));
					
				}
				
				break;
				
				case("PrintStatement"):{
					//add new print statement node
					addBranchNode(currentCstNodeValue);
					
					//need to get the exprs output 
					cstToAst(nodeChildren.get(2));
					
					returnToParent();
				}
				
				break;
				
				case("AssignmentStatement") :{
					addBranchNode(currentCstNodeValue);
					//heading towards the ID
					cstToAst(nodeChildren.get(0));
					//heading towards the Expr
					cstToAst(nodeChildren.get(2));
					
					returnToParent();
				}
				
				break;
				
				case("VarDecl") :{
					addBranchNode(currentCstNodeValue);
					
					//heading towards the type
					cstToAst(nodeChildren.get(0));
					
					//heading towards the ID
					cstToAst(nodeChildren.get(1));
					
					returnToParent();	
				}
				
				break;
				
				case("WhileStatement") :{
					addBranchNode(currentCstNodeValue);
					//head towards boolean expr
					cstToAst(nodeChildren.get(1));
					//head towards Block
					cstToAst(nodeChildren.get(2));
					
					returnToParent();
				}
				
				break;
				
				case("IfStatement"):{
					addBranchNode(currentCstNodeValue);
					//head towards boolean expr
					cstToAst(nodeChildren.get(1));
					//head towards block
					cstToAst(nodeChildren.get(2));
					
					returnToParent();
				}
				
				break;
				
				case("Expr"):{
					//head towards the type of expr this is.
					cstToAst(nodeChildren.get(0));
				}
				
				break;	
				
				case("IntExpr") :{
					
					//digit intop Expr case
					if(nodeChildren.size() > 1){
						
						//head towards intop
						//want to add + operator as a branch node before adding digits as leaves.
						cstToAst(nodeChildren.get(1));
						
						//head towards adding left digit as leaf
						cstToAst(nodeChildren.get(0));
						
						//head towards the right operand expr
						cstToAst(nodeChildren.get(2));
						
						returnToParent();
						
						
					}
					
					//single digit case.
					else{
						
						//head towards digit
						cstToAst(nodeChildren.get(0));
						
					}
				}
			
				
				break;
				
				case("StringExpr") :{
					String leafValue = handleStringExpr(currentCstNode);
					
					Token stringToken = new Token();
					
					stringToken.setIndicator("string");
					
					addLeafNode(leafValue, stringToken);
				}
				
				break;
				
				case("BooleanExpr") :{
					
					
					
					//expr boolop expr case
					if(nodeChildren.size()>1){
						
						//head towards boolop, make the boolop a branch node
						cstToAst(nodeChildren.get(2));
						
						//head towards the left operand in the expression
						cstToAst(nodeChildren.get(1));
						
						//head towards the right operand in the expression
						cstToAst(nodeChildren.get(3));
						
						returnToParent();
					}
					
					////just a boolval case
					else{
						cstToAst(nodeChildren.get(0));
					}
				}
				
				break;
				
				case("Id") :{
					//head towards char node for the id
					cstToAst(nodeChildren.get(0));
				}
				
				break;
				
				case("CharList"):{
					//dont need to do anything, stringexpr handles charlists
					
				}
				
				break;
				
				
				//--NONTERMINALS WE ACTUALLY CARE ABOUT--//
				case("type"):{
					//type nonterminal will be first child, grab that and add a leaf
					Node leafNode = nodeChildren.get(0);
					addLeafNode(leafNode.getToken().getValue(), leafNode.getToken());
				}
				
				break;
				
				case("char"):{
					//note, this should only be called by an Id, all other chars should be handled by the stringexpr case
					
					//char nonterminal will be first child, grab that and add a leaf.
					Node leafNode = nodeChildren.get(0);
					addLeafNode(leafNode.getToken().getValue(), leafNode.getToken());
				}
				
				break;
				
				case("digit") :{
					//digit nonterminal will be first child, grab that and add a leaf
					Node leafNode = nodeChildren.get(0);
					addLeafNode(leafNode.getToken().getValue(), leafNode.getToken());
				}
				
				break;
				
				case("boolop"):{
					Node branchNode = nodeChildren.get(0);
					addBranchNode(branchNode.getToken().getValue(), branchNode.getToken());
				}
				
				break;
				
				case("boolval"):{
					Node leafNode = nodeChildren.get(0);
					addLeafNode(leafNode.getToken().getValue(), leafNode.getToken());
				}
				
				break;
				
				case("intop"):{
					Node branchNode = nodeChildren.get(0);
					addBranchNode(branchNode.getToken().getValue(), branchNode.getToken());
				}
				
				break;
				
				
				default :{
					//raise error
					
				}
			}
		} catch (Exception e) {
			System.out.println("Error generating AST");
			System.out.println(e.getMessage());
			e.printStackTrace();
		}	
	}
	
	
	/**
	 * Hackish way of building a string suitable for an ast from giant charlists....
	 * Should only be called intially from a String expr node.
	 * 
	 * @param stringExprNode, the string expr node we want to evaluate
	 * @return charlist, a string of concatenated chars, or null if something went wrong.
	 */
	private String handleStringExpr(Node stringExprNode){
		try {
			
			return  charlistBuilder(stringExprNode, "");
			
		} catch (Exception e) {
			System.out.println("Error handling String Expression");
			e.printStackTrace();
		}
		
		return null;
	}
	

	/**
	 * Recursively builds a String
	 * 
	 * @param currentNode the node to evaluate
	 * @param charList the current charlist we are building.
	 */
	private String charlistBuilder(Node currentNode, String charList){
		
		//if the node is a leaf node from the CST, we want it in the string
		if(currentNode.isLeafNode())
			charList+= currentNode.getToken().getValue();
		
		
		//base case
		//just return the charlist since theres no children
		if(currentNode.getChildren().isEmpty())
			return charList;
		
		
		//recursive part
		//grab leaf nodes from all children
		else{
			for(Node x : currentNode.getChildren())
				charList = charlistBuilder(x, charList);
		}
		
		return charList;
	}
}
