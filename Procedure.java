import java.util.*;

class Procedure {
	Id id;
	DeclSeq ds;
	StmtSeq ss;
	
	void parse() {
		Parser.expectedToken(Core.PROCEDURE); Parser.scanner.nextToken();
		id = new Id(); 
		id.parse();
		Parser.expectedToken(Core.IS); Parser.scanner.nextToken();

		Core t = Parser.scanner.currentToken();
		if (t == Core.INTEGER || t == Core.OBJECT || t == Core.PROCEDURE) {
			ds = new DeclSeq();
			ds.parse();
		}

		Parser.expectedToken(Core.BEGIN); 
		Parser.scanner.nextToken();
		ss = new StmtSeq(); 
		ss.parse();
		Parser.expectedToken(Core.END); 
		Parser.scanner.nextToken();
	}
	
	void print(int indent) {
		for (int i = 0; i<indent; i++) {
			System.out.print("\t");
		}
		System.out.println("procedure " + id.getId() + " is");
		if (ds != null) {
			ds.print(indent + 1);
		}
		for (int i = 0; i<indent; i++) {
			System.out.print("\t");
		}
		System.out.println("begin");
		ss.print(indent + 1);
		for (int i = 0; i<indent; i++) {
			System.out.print("\t");
		}
		System.out.println("end");
	}
	
	void execute() {
		if (ds != null) {
			Memory.initializeGlobal();
			ds.execute();
		}
		Memory.initializeLocal();
		Memory.pushScope();
		ss.execute();
		Memory.popScope();
	}
}