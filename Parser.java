import java.util.*;

class Parser {
	//scanner is stored here as a static field so it is avaiable to the parse method
	public static CoreScanner scanner;
	
	static Map<String, ProcDecl> procTable = new HashMap<>();
	
	//helper method for handling error messages, used by the parse methods
	static void expectedToken(Core expected) {
		if (scanner.currentToken() != expected) {
			System.out.println("ERROR: Expected " + expected + ", recieved " + scanner.currentToken());
			System.exit(0);
		}
	}

	static void expected(Core expected) { 
		expectedToken(expected); 
	}

	static boolean startsVarDecl() {
		Core t = scanner.currentToken();
		return t == Core.INTEGER || t == Core.OBJECT;
	}

	static boolean startsAnyDecl() {
		Core t = scanner.currentToken();
		return t == Core.INTEGER || t == Core.OBJECT || t == Core.PROCEDURE;
	}
	
	static boolean startsDecl() {
		return startsVarDecl(); 
	}

	
}