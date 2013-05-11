package exceptions;

public class InstructionNotFoundException extends Exception {
	
	final static String message = "Instruction is not found";
	
	public InstructionNotFoundException() {
		
	}
	
	public String toString() {
		return message;
	}
}
