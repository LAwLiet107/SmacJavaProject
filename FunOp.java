package project;

import java.io.*;
import java.util.*;

public class FunOp {

	int decimalNumber = -1;
	int isLogPattern = 0;
	String fileName;
	String letKey;
	String logName;
	PrintStream outputLog;
	Map<String, Double> letMaps = new TreeMap<String, Double>();
	Set<String> set = new TreeSet<String>();
	
	public void setprecision(Tokenizer tokenizer) throws TokenException, LexicalErrorException {
		tokenizer.readNextToken();
		if(tokenizer.hasNextToken()) {
			Token tSetprecision = tokenizer.readNextToken();
			decimalNumber = (int) tSetprecision.getNumber();
			System.out.println("precision set to " + decimalNumber);
		}
		else if(decimalNumber != -1)
			System.out.println("current precision is " + decimalNumber);
		else
			System.out.println("No decimal point is set");
	}
	
	public void letFunction(Tokenizer tokenizer, MathematicalEvaluator mathEvaluator, FunOp funOp) throws TokenException, LexicalErrorException {
		Token t2 = null;
		int i = 0;
		while ( tokenizer.hasNextToken() ) {
			//t2 = tokenizer.peekNextToken();
			t2 = tokenizer.readNextToken();
			if(i == 1)
				letKey = t2.getIdentifier();
			if(t2.isEqual())
				break;
			i++;
		}
		if(i == 1&&letMaps.isEmpty()) {
			System.out.println("no variable defined");
			if(isLogPattern == 1)
				outputLog.println("no variable defined");
		}
		else if(i == 1)
			for(String keyName: letMaps.keySet()) {
				Double keyValue = letMaps.get(keyName);
				if(!keyName.equals("last")) {
					System.out.print(keyName + " = ");
					mathEvaluator.doubleOrInt(funOp, keyValue);
					if(isLogPattern==1)
						outputLog.print(keyName + " = " + mathEvaluator.last);
				}
			}
		else {
			double mapValue = mathEvaluator.CoreFunction(tokenizer, funOp);
			letMaps.put(letKey, mapValue);
		}
	}
	
	public void resetFunction(Tokenizer tokenizer) throws TokenException, LexicalErrorException {
		Token t3 = tokenizer.readNextToken();
		if(!tokenizer.hasNextToken()&&!letMaps.isEmpty()) {
			Iterator<Map.Entry<String, Double>> it = letMaps.entrySet().iterator();   //使用迭代器对Map进行遍历删除
			while(it.hasNext()) {
				Map.Entry<String, Double> entry = it.next();
				if(!entry.getKey().equals("last"))
					System.out.println(entry.getKey() + " has been reset");
				it.remove();
			}
		}
		resetResult(tokenizer, t3);
	}
	
	public void resetResult(Tokenizer tokenizer, Token t3) throws TokenException, LexicalErrorException {
		while (tokenizer.hasNextToken()) {
			t3 = tokenizer.readNextToken();
			if(t3.isIdentifier()&&t3.getIdentifier().equals("last")) {
				System.out.println("Syntax error: last is not a variable");
				if(isLogPattern==1)
					outputLog.print("Syntax error: last is not a variable");
			}
			else if(!letMaps.isEmpty()&&letMaps.keySet().contains(t3.getIdentifier())) {
				letMaps.remove(t3.getIdentifier());
				System.out.println(t3.getIdentifier() + " has been reset");	
				if(isLogPattern==1)
					outputLog.println(t3.getIdentifier() + " has been reset");
			}
			else {
				System.out.println("error: " + t3.getIdentifier() + " is not defined");
				if(isLogPattern==1)
					outputLog.println("error: " + t3.getIdentifier() + " is not defined");
			}
		}
	}
	
	public void lastFunction(String input, Tokenizer tokenizer, MathematicalEvaluator mathEvaluator, Map<String, Double> letMaps, FunOp funOp) throws TokenException, LexicalErrorException {
		Tokenizer tokenizerLast = new Tokenizer(input);
		Token t4 = tokenizer.readNextToken();
		if(tokenizer.hasNextToken()) {
			mathEvaluator.CoreFunction(tokenizerLast, funOp);
		}
		else
			mathEvaluator.doubleOrInt(funOp, mathEvaluator.last);
	}
	
	public void saveFunction(Tokenizer tokenizer, MathematicalEvaluator mathEvaluator) throws TokenException, LexicalErrorException, FileNotFoundException {
		tokenizer.readNextToken();
		Token t5 = tokenizer.readNextToken();
		fileName = t5.getString();
		PrintStream output = new PrintStream(new File("Smac_save/" + fileName + ".txt"));
		if(!letMaps.isEmpty()&&!tokenizer.hasNextToken()) {
			saveAll(output, fileName);
			set.add(fileName);
		}
		else if(!letMaps.isEmpty()) {
			savePart(output, t5, tokenizer, fileName);
			set.add(fileName);
		}
		else {
			System.out.println("no variable to save");
			if(isLogPattern == 1)
				outputLog.println("no variable to save");
		}
	}
	
	public void saveAll(PrintStream output, String fileName) {
		for(String keyName: letMaps.keySet()) {
			Double keyValue = letMaps.get(keyName);
			outputResult(output, keyName, keyValue);
		}
		System.out.println("variables saved in " + fileName);
		if(isLogPattern == 1)
			outputLog.println("variables saved in " + fileName);
		letMaps.clear();
	}
	
	public void savePart(PrintStream output, Token t5, Tokenizer tokenizer, String fileName) throws TokenException, LexicalErrorException {
		while(tokenizer.hasNextToken()) {
			t5 = tokenizer.readNextToken();
			deleteMap(output, t5);
		}
		System.out.println("variables saved in " + fileName);
		if(isLogPattern==1)
			outputLog.println("variables saved in " + fileName);
	}
	
	public void deleteMap(PrintStream output, Token t5) throws TokenException {
		Iterator<Map.Entry<String, Double>> it = letMaps.entrySet().iterator();   //使用迭代器对Map进行遍历删除
		while(it.hasNext()) {
			Map.Entry<String, Double> entry = it.next();
			if(entry.getKey().equals(t5.getIdentifier())) {
				double keyValue = letMaps.get(entry.getKey());
				outputResult(output, entry.getKey(), keyValue);
				it.remove();
			}
		}
	}
	
	public void outputResult(PrintStream output, String keyName, Double keyValue) {
		output.print(keyName + " = ");
		outputDoubleOrInt(keyValue, output);
	}
	
	public void outputDoubleOrInt(double result, PrintStream output) {
		int result2 = (int)result;
		if(result - result2 == 0&&decimalNumber < 0)
			outputFinalResult(result2, output);
		else
			outputFinalResult(result, output);
	}
	
	public void outputFinalResult(Object result, PrintStream output) {
		if(decimalNumber >= 0) {
			output.printf("%." + decimalNumber + "f", result);
			output.println();
		}
		else
			output.println(result);
	}
	
	public void savedFunction() {
		for(String key: set) {
			System.out.println(key);
			if(isLogPattern==1)
				outputLog.println(key);
		}
	}
	
	public void loadFunction(Tokenizer tokenizer) throws TokenException, LexicalErrorException, FileNotFoundException {
		tokenizer.readNextToken();
		Token t6 = tokenizer.readNextToken();
		Scanner inputFile = new Scanner(new File("Smac_save/" + t6.getString() + ".txt"));
		while(inputFile.hasNextLine()) {
			Tokenizer tokenizer2 = new Tokenizer(inputFile.nextLine());
			Token t7 = tokenizer2.readNextToken();
			String key = t7.getIdentifier();
			tokenizer2.readNextToken();
			t7 = tokenizer2.readNextToken();
			double value = t7.getNumber();
			letMaps.put(key, value);
		}
		System.out.println(t6.getString() + " loaded");
		if(isLogPattern==1)
			outputLog.println(t6.getString() + " loaded");
	}
	
	public void logFunction(Tokenizer tokenizer, Evaluator evaluator, MathematicalEvaluator mathEvaluator, FunOp funOp) throws TokenException, LexicalErrorException, FileNotFoundException {
		tokenizer.readNextToken();
		if(tokenizer.hasNextToken()) {
			Token t7 = tokenizer.readNextToken();
			logName = t7.getString();
			outputLog = new PrintStream(new File("Smac_log/" + logName + ".txt"));
			System.out.println("logging session to " + logName);
			logProcess(evaluator, mathEvaluator, funOp);
		}
		else if(!logName.equals(null)){
			System.out.println(logName);
			if(isLogPattern == 1)
				outputLog.println(logName);
		}
		else
			System.out.println("no log");
		isLogPattern = 0;
	}
	
	public void logProcess(Evaluator evaluator, MathematicalEvaluator mathEvaluator, FunOp funOp) throws FileNotFoundException, TokenException, LexicalErrorException {
		while(true) {
			isLogPattern = 1;
			Scanner console1 = new Scanner (System.in);
			System.out.println();
			System.out.print(">> ");
			String inputLog = console1.nextLine();
			if(inputLog.equalsIgnoreCase("log end")) {
				System.out.println("session was logged to " + logName);
				break;
			}
			outputLog.println(inputLog);
			Tokenizer tokenizerLog = new Tokenizer(inputLog);
			evaluator.evaluateInput(inputLog, tokenizerLog, mathEvaluator, funOp);
		}
	}
	
	public void loggedFunction() {
		System.out.println(logName);
	}
}
