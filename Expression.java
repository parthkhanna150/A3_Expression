//Student ID: 260681792
//Student Name: Nicholas Lee

import java.io.*;
import java.util.*;

public class Expression {
	static String delimiters="+-*%()";
	// Returns the value of the arithmetic Expression described by expr
	// Throws an exception if the Expression is malformed

	static Integer evaluate(String expr) throws Exception {
		
		Stack<Integer> numbers = new Stack<Integer> (); //creates a stack used just for numbers
		Stack<String> operators = new Stack<String> (); //creates a stack used just for operators. Includes +,-,*,%,(,)
		Stack<String> parentheses = new Stack<String> (); //creates a stack used just for parentheses
		
		int stackCumulative = 0; //variable used for holding arithmetic operations outside of parentheses equations
		int holdingVar = 0; //variable that is used as a temporary numbers holder
		int tempParentheses = 0; //variable used for holding arithmetic operations inside of parentheses equations
		int numbersCounter = 0; //counter that keeps track of how many numbers are introduced in the expression
		int numbersCounterHighest = 0; //keeps track of the max number of numbers introduced in expression
		int tempCounter = 0; //counter that keeps track of how many numbers exist in parentheses only
		
	    StringTokenizer st1 = new StringTokenizer( expr , delimiters , true ); //StringTokenizer breaks down the inputed string into separate tokens
	    
	  //while there are still tokens to be added to the stack, the program continues to loop
	    while ( st1.hasMoreTokens() ) {
	    	
	    	String temp = st1.nextToken(); //temp continually holds the next token
	    	
	    	if( temp.equals( "(" ) || temp.equals( ")" ) || temp.equals( "+" ) || temp.equals( "-" ) || temp.equals( "%" ) || temp.equals( "*" ) ){ //if it is an operator, then the token is put into the operator stack
		   		operators.push( temp ); //temp is pushed into operator stack
	    	}
		   	else{ 
		   		Integer integerConvert = new Integer( temp ); //otherwise temp is converted into an Integer type and put in numbers stack
		   		numbers.push( integerConvert ); //Integer type temp is pushed into numbers stack
		   		numbersCounter += 1; //numbersCounter increments as a number has been introduced into the expression
		   		if( numbersCounter > numbersCounterHighest ) numbersCounterHighest = numbersCounter; //updates numbersCounterHighest if numbersCounter is bigger than numbersCounterHighest
		   	}
	    	//now numbers and operators are sorted in respective stacks
	    	
	    	//this if statement is entered only if the expression encounters a parentheses in it. (The !operators.isEmpty() is merely to prevent an EmptyStackException)
	    	//Either this if statement will be entered if we hit a "(" operator or we are already in a parentheses statement ( !parentheses.isEmpty() )
	    	//This if statement will continue to be entered as long as we are in a parentheses
	    	if( (!operators.isEmpty() && operators.peek().equals( "(" )) || (!parentheses.isEmpty()) ){
	    		
	    		//if statement is entered if current token is a "(" operator, (temp and operators.peek() is the same)
	    		if( temp.equals("(") && operators.peek().equals( "(" ) ){
	    			parentheses.push( "(" ); //"(" is pushed into parentheses stack to keep track of whether we are in a parentheses statement or not.
	    			tempCounter = 0; //tempCounter is reset. It only keeps track of numbers inside the current parentheses statement (and if there are more than one set of parentheses, it is reset again)
	    		}
	    		
	    		//if statement is entered if current token is a number and not an operator
	    		if( !temp.equals( operators.peek() ) ){
	    			tempCounter+=1; //tempCounter keeps track of number of numbers in this specific set of parentheses, incremented because current token is a number
	    			numbersCounter = numbersCounter  - 1; //numbersCounter used to keep track of number of numbers outside of parentheses expressions. numbersCounter is automatically incremented by 1 when a number is put onto the stack. Because a number is introduced inside the parentheses, the number of numbers outside the parentheses has not changed
	    		}
	   			
	    		//if statement entered if current token is ")". This means that parentheses statement has ended
	    		if( operators.peek().equals( ")" ) ){
	    			//If the top most element in stack is "(", then we know that we have just completed a parentheses statement. (!parentheses.isEmpty() is so that we don't encounter an EmptyStackException)
	    			if( !parentheses.isEmpty() && parentheses.peek().equals( "(" ) ){
	    				parentheses.pop(); //"(" is popped from the parentheses stack to show we have successfully matched the parentheses statement
	    				operators.pop(); // ")" is popped from operators stack
	    				operators.pop(); //"(" is popped from operators stack (Any arithmetic operators will already have been popped)
	    				if( !parentheses.isEmpty() && (operators.peek().equals( "+" ) || operators.peek().equals( "-" ) || operators.peek().equals( "*" ) || operators.peek().equals( "%" ) ) ){ //tempCounter counts number of numbers in parentheses. When pair of parentheses have been matched (and parentheses are therefore removed), there is a single number that must be introduced to the equation outside of the parentheses. Thus tempCounter (which was 1) is incremented 
	    					tempCounter = tempCounter+1;
	    				}
	    				if(parentheses.isEmpty()){ //If there are no parentheses, that means we have matched all existing pairs of parentheses in the equation. Therefore, a single number remains which must be introduced to the expression outside of the parentheses. numbersCounter keeps track of number of numbers outside of parentheses, and now that this new number is being introduced, numbersCounter must be incremented
	    					numbersCounter += 1;
	    				}
	    			}
	    			else{ //If there is no "(" in parentheses stack, then ")" will be pushed on parentheses stack (which will throw an exception)
	    				parentheses.push( ")" );
	    			}
	    		} 
	    		
	    		//if tempCounter is 2 (there are 2 numbers in the parentheses) then the if statement is entered and an arithmetic operation occurs
	   			if( tempCounter == 2 ){
	   				tempParentheses = numbers.pop(); //tempParentheses takes the value of the top number in stack
	   				holdingVar = numbers.pop(); //holdingVar takes the value of the second most top number in stack
	   				if( operators.peek().equals( "+" ) ){ operators.pop(); holdingVar = holdingVar + tempParentheses; } //if operator is "+", then these variables are added
					else if( operators.peek().equals( "-" ) ){ operators.pop(); holdingVar = holdingVar - tempParentheses; } //if operator is "-", then these variables are subtracted (always holdingVar - tempParentheses, because we are operating from left to right)
					else if( operators.peek().equals( "*" ) ){ operators.pop(); holdingVar = holdingVar * tempParentheses; } //if operator is "*", then these variables are multiplied
					else if( operators.peek().equals( "%" ) ){ operators.pop(); holdingVar = holdingVar / tempParentheses; } //if operator is "%", then these variable are divided (always holdingVar % tempParentheses, because we are operating from left to right)
	   				numbers.push(holdingVar); //the result of the arithmetic operation is pushed.
	    			tempCounter = 1; //tempCounter is set to 1, because only one number remains after the arithmetic operation
	    		}
	    	}
	    	
	    	//if statement is entered if no parentheses are involved. Involves arithmetic operations
	    	if( numbersCounter == 2 && parentheses.isEmpty() ){
	    		holdingVar = numbers.pop(); //holdingVar takes value of top most number in stack
	    		stackCumulative = numbers.pop(); //stackCumulative takes value of second top most number in stack
	    		if( operators.peek().equals( "+" ) ){ operators.pop(); stackCumulative = stackCumulative + holdingVar; } //if operator is "+", then these variables are added
	   			else if( operators.peek().equals( "-" ) ){ operators.pop(); stackCumulative = stackCumulative - holdingVar; } //if operator is "-", then these variables are subtracted (always holdingVar - tempParentheses, because we are operating from left to right)
	   			else if( operators.peek().equals( "*" ) ){ operators.pop(); stackCumulative = stackCumulative * holdingVar; } //if operator is "*", then these variables are multiplied
	   			else if( operators.peek().equals( "%" ) ){ operators.pop(); stackCumulative = stackCumulative / holdingVar; } //if operator is "%", then these variable are divided (always holdingVar % tempParentheses, because we are operating from left to right)
	    		numbers.push( stackCumulative ); //result of arithmetic operation is pushed
    			numbersCounter = 1; //numbersCounter is set to 1, because only one number remains after the arithmetic operation
	    	}
	    }
	    
	    //while loops is finished. If either the operators stack or the parentheses stack is not empty, then an exception is thrown because there are operators leftover (or parentheses are mismatched).
	    if( !operators.isEmpty() || !parentheses.isEmpty() ){
	    	throw new Exception();
	    }
	
	    System.out.print("Output");
	    return numbers.peek(); //there should only be one number left in the numbers stack, so that is returned.
	}
		
	public static void main(String args[]) throws Exception {
		String line;
		BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
                                      	                        
		do {
			line=stdin.readLine();
			if (line.length()>0) {
				try {
					Integer x=evaluate(line);
					System.out.println(" = " + x);
				}
				catch (Exception e) {
					System.out.println("Malformed Expression: "+e);
				}
			}
		} while (line.length()>0);
	}
}
