package project;

import java.io.FileNotFoundException;
import java.util.*;

public class Evaluator {
	
	public void evaluateInput(String input, Tokenizer tokenizer, MathematicalEvaluator mathEvaluator, FunOp funOp) throws TokenException, LexicalErrorException, FileNotFoundException {
		Token firstWord = tokenizer.peekNextToken();
		
		if(firstWord.isNumber()||firstWord.isDelimiter())
			mathEvaluator.CoreFunction(tokenizer, funOp);
		else if(firstWord.isIdentifier()&&!firstWord.getIdentifier().equals("last")&&funOp.letMaps.containsKey(firstWord.getIdentifier()))
			mathEvaluator.CoreFunction(tokenizer, funOp);
		else if(firstWord.isOperator()&&firstWord.getOperator().equals("~"))
			mathEvaluator.CoreFunction(tokenizer, funOp);
		else
			switch(firstWord.getIdentifier()) {
				case "setprecision" : funOp.setprecision(tokenizer); break;
				case "let" : funOp.letFunction(tokenizer, mathEvaluator, funOp); break;
				case "reset": funOp.resetFunction(tokenizer); break;
				case "last" : funOp.lastFunction(input, tokenizer, mathEvaluator, funOp.letMaps, funOp); break;
				case "save" : funOp.saveFunction(tokenizer, mathEvaluator); break;
				case "saved" : funOp.savedFunction(); break;
				case "load" : funOp.loadFunction(tokenizer); break;
				case "log" : funOp.logFunction(tokenizer, this, mathEvaluator, funOp); break;
				case "logged" : funOp.loggedFunction(); break;
				default: System.out.println(firstWord.getIdentifier() + " is not a variable");
						 if(funOp.isLogPattern==1)
							 funOp.outputLog.println("error: " + firstWord.getIdentifier() + " is not a variable");
			}
	}
}