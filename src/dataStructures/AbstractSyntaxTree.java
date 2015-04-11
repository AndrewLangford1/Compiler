package dataStructures;


/**
 * 
 * @author Andrew Langford
 * 
 * Class to convert CSTs to ASTs
 *
 */

public class AbstractSyntaxTree extends SyntaxTree {
	
//--Fields--//

	//the CST we'll use to generate the AST
	SyntaxTree concreteSyntaxTree;
	
	
	
	/**
	 * Default Constructor
	 * @param concreteSyntaxTree the CST to convert into an AST
	 * 
	 */
	public AbstractSyntaxTree(SyntaxTree concreteSyntaxTree){
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
			
			String currentCstNodeValue = currentCstNode.getValue();
			switch(currentCstNodeValue){
			
				case("Program"):{
					//dont care whatsoever, just get the first block
					cstToAst(currentCstNode.getChildren().get(0));
				}
				
				break;
				
				case("Block"):{
					addBranchNode(currentCstNodeValue);
					if(currentCstNode.hasChildren())
						cstToAst(currentCstNode.getChildren().get(1));
					
					
					returnToParent();
				}
				
				break;
				
				case("StatementList") : {
					if(currentCstNode.hasChildren()){
						//convert first Statement
						cstToAst(currentCstNode.getChildren().get(0));	
						//convert rest of statements
						cstToAst(currentCstNode.getChildren().get(1));
					}
				}
				
				break;
				
				case("Statement") :{
					//dont need a new node but need its child
					cstToAst(currentCstNode.getChildren().get(0));
					
				}
				
				break;
				
				case("PrintStatement"):{
					//add new print statement node
					addBranchNode(currentCstNodeValue);
					
					//need to get the exprs output 
					cstToAst(currentCstNode.getChildren().get(2));
					
					returnToParent();
				}
				
				break;
				
				case("AssignmentStatement") :{
					addBranchNode(currentCstNodeValue);
					//heading towards the ID
					cstToAst(currentCstNode.getChildren().get(0));
					//heading towards the Expr
					cstToAst(currentCstNode.getChildren().get(2));
					
					returnToParent();
				}
				
				break;
				
				case("VarDecl") :{
					addBranchNode(currentCstNodeValue);
					
					//heading towards the type
					cstToAst(currentCstNode.getChildren().get(0));
					
					//heading towards the ID
					cstToAst(currentCstNode.getChildren().get(1));
					
					returnToParent();	
				}
				
				break;
				
				case("WhileStatement") :{
					addBranchNode(currentCstNodeValue);
					//head towards boolean expr
					cstToAst(currentCstNode.getChildren().get(1));
					//head towards Block
					cstToAst(currentCstNode.getChildren().get(2));
					
					returnToParent();
				}
				
				break;
				
				case("IfStatement"):{
					addBranchNode(currentCstNodeValue);
					//head towards boolean expr
					cstToAst(currentCstNode.getChildren().get(1));
					//head towards block
					cstToAst(currentCstNode.getChildren().get(2));
					
					returnToParent();
				}
				
				break;
				
				case("Expr"):{
					//head towards the type of expr this is.
					cstToAst(currentCstNode.getChildren().get(0));
				}
				
				break;
				
				case("IntExpr") :{
					
					//digit intop Expr case
					if(currentCstNode.getChildren().size() > 1){
						
						//head towards intop
						//want to add + operator as a branch node before adding digits as leaves.
						cstToAst(currentCstNode.getChildren().get(1));
						
						//head towards adding left digit as leaf
						cstToAst(currentCstNode.getChildren().get(0));
						
						//head towards the right operand expr
						cstToAst(currentCstNode.getChildren().get(2));
						
						
					}
					
					//single digit case.
					else{
						
						//head towards digit
						cstToAst(currentCstNode.getChildren().get(0));
						
					}
				}
				
				break;
				
				case("StringExpr") :{
					
					
					
					
				}
				
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
}
