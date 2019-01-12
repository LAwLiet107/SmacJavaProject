package project;

import java.io.FileNotFoundException;
import java.util.*;

public class Smac {

	public static void main(String[] args) throws TokenException, LexicalErrorException, FileNotFoundException {
		
		System.out.println("Welcome to SMAC");
		System.out.println();
		
		Scanner console = new Scanner (System.in);
		MathematicalEvaluator mathEvaluator = new MathematicalEvaluator();
		Evaluator evaluator = new Evaluator();
		FunOp funOp = new FunOp();
		String input = "";
		
		while(true) {
			System.out.print("> ");
			input = console.nextLine();
			if(input.equalsIgnoreCase("exit"))
				break;
			Tokenizer tokenizer = new Tokenizer(input);
			evaluator.evaluateInput(input, tokenizer, mathEvaluator, funOp);
			System.out.println();
		}
		System.out.println("Thank you for using SMAC");
	}
}
