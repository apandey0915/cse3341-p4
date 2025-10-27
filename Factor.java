class Factor {
	Id id;
	String key;
	int constant;
	Expr expr;
	
	void parse() {
		if (Parser.scanner.currentToken() == Core.ID) {
			id = new Id();
			id.parse();
			if (Parser.scanner.currentToken() == Core.LSQUARE) {
				Parser.scanner.nextToken();
				key = Parser.scanner.getString();
				Parser.scanner.nextToken();
				Parser.expectedToken(Core.RSQUARE);
				Parser.scanner.nextToken();
			}
		} else if (Parser.scanner.currentToken() == Core.CONST) {
			constant = Parser.scanner.getConst();
			Parser.scanner.nextToken();
		} else if (Parser.scanner.currentToken() == Core.LPAREN) {
			Parser.scanner.nextToken();
			expr = new Expr();
			expr.parse();
			Parser.expectedToken(Core.RPAREN);
			Parser.scanner.nextToken();
		} else {
			System.out.println("ERROR: Expected ID, CONST, LPAREN, recieved " + Parser.scanner.currentToken());
			System.exit(0);
		}
	}
	
	void print() {
		if (id != null) {
			id.print();
			if (key != null) {
				System.out.print("['" + key + "']");
			}
		} else if (expr != null) {
			System.out.print("(");
			expr.print();
			System.out.print(")");
		} else {
			System.out.print(constant);
		}
	}
	
	int execute() {
		int value;
		if (id != null && key != null) { // x['key']
			value = Memory.load(id.getId(), key);
		} else if (id != null) {  // x
			value = Memory.load(id.getId());
		} else if (expr != null) {
			value = expr.execute();
		} else {  // const
			value = constant;
		}
		return value;
	}
}