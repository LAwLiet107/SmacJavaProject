package project;

import java.util.*;

public class MathematicalEvaluator {
	
	double last;
	
	public double CoreFunction(Tokenizer tokenizer, FunOp funOp) throws TokenException, LexicalErrorException {
		Stack<Double> valueStack = new Stack<Double>();
		Stack<String> operatorStack = new Stack<String>();
		String operator = "+-~*/^";
		Token tLast = null;
		
		while(tokenizer.hasNextToken()) {
			Token t = tokenizer.readNextToken();

			if(t.isIdentifier()&&funOp.letMaps.containsKey(t.getIdentifier())) {   //???
				valueStack.add(funOp.letMaps.get(t.getIdentifier()));
			}
			if(t.isIdentifier()&&t.getIdentifier().equals("last")&&!tokenizer.hasNextToken())
				System.out.print(funOp.letKey + " = ");
			if(tLast!=null&&tLast.isOperator()&&tLast.getOperator().equals("/")&&t.isNumber()&&t.getNumber()==0) {
				System.out.println("Error: division by zero");
				valueStack.clear();
				operatorStack.clear();
				break;
			}
			addNumber(t, valueStack);
			addOperator(t, valueStack, operatorStack, funOp, operator); // 有问题
			addDelimiter(t, valueStack, operatorStack, funOp);
			
			tLast = t;
		}
		while((!operatorStack.isEmpty()&&valueStack.size()>=2)||(!operatorStack.isEmpty()&&operatorStack.peek().equals("~")&&valueStack.size()==1))
			compute(valueStack, operatorStack);
		if(valueStack.size()==1&&!operatorStack.isEmpty()&&!operatorStack.peek().equals("~")) {
			System.out.println("Syntax error: malformed  expression");
			return 0;
		}
		else if(valueStack.isEmpty()&&operatorStack.isEmpty())
			return 0;
		else {
			double result = valueStack.pop();
			doubleOrInt(funOp, result);
			last = result;
			funOp.letMaps.put("last", last);
			return result;
		}
	}
	
	public void compute(Stack<Double> valueStack, Stack<String> operatorStack) {
		String op = operatorStack.pop();
		if(op.equals("~")) {
			double n = valueStack.pop();
			valueStack.add(0-n);
		}
		else {
			double n1 = valueStack.pop();
			double n2 = valueStack.pop();
			switch(op) {
				case "+" : valueStack.add(n2 + n1); break;
				case "-" : valueStack.add(n2 - n1); break;
				case "*" : valueStack.add(n2 * n1); break;
				case "/" : valueStack.add(n2 / n1); break;
				default : valueStack.add(Math.pow(n2, n1));
			}
		}
	}

	public boolean pushOrNot(Stack<String> operatorStack, Token t, String operator) throws TokenException {
		return operatorStack.isEmpty()||operator.indexOf(t.getOperator())>operator.indexOf(operatorStack.peek());
	}
	
	public void addNumber(Token t, Stack<Double> valueStack) throws TokenException {	
		if(t.isNumber())
			valueStack.add(t.getNumber());
	}
	
	public void addOperator(Token t, Stack<Double> valueStack, Stack<String> operatorStack, FunOp funOp, String operator) throws TokenException {	
		if(t.isOperator()) {
			while(!pushOrNot(operatorStack, t, operator))
				compute(valueStack, operatorStack);
			operatorStack.add(t.getOperator());
		}
	}
	
	
	public void addDelimiter(Token t, Stack<Double> valueStack, Stack<String> operatorStack, FunOp funOp) throws TokenException {
		if(t.isDelimiter()) {
			if(t.getDelimiter().equals("("))
				operatorStack.add(t.getDelimiter());
			if(t.getDelimiter().equals(")")) {
				while(!operatorStack.peek().equals("("))
					compute(valueStack, operatorStack);
				operatorStack.pop();
			}
		}
	}
	
	public void doubleOrInt(FunOp funOp, double result) {
		int result2 = (int)result;
		if(result - result2 == 0&&funOp.decimalNumber < 0)
			printResult(funOp, result2);
		else
			printResult(funOp, result);
	}
	
	public void printResult(FunOp funOp, Object result) {
		if(funOp.decimalNumber >= 0) {
			System.out.printf("%." + funOp.decimalNumber + "f", result);
			System.out.println();
			if(funOp.isLogPattern == 1) {
				funOp.outputLog.printf("%." + funOp.decimalNumber + "f", result);
				funOp.outputLog.println();
			}
		}
		else {
			System.out.println(result);
			if(funOp.isLogPattern == 1)
				funOp.outputLog.println(result);
		}
	}
	
}
