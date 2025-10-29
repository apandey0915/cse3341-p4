import java.util.*;

class Call implements Stmt {
    String name;
    ActualList actuals;

    public void parse() {
        Parser.scanner.nextToken();

        Parser.expectedToken(Core.ID);
        name = Parser.scanner.getId(); 
        Parser.scanner.nextToken();
        Parser.expectedToken(Core.LPAREN); 
        Parser.scanner.nextToken();
        actuals = new ActualList(); 
        actuals.parse();
        Parser.expectedToken(Core.RPAREN); 
        Parser.scanner.nextToken();
        Parser.expectedToken(Core.SEMICOLON); 
        Parser.scanner.nextToken();
    }

    public void print(int indent) {
        for (int i = 0; i<indent; i++) System.out.print("\t");
        System.out.print("begin " + name + "("); actuals.print(); System.out.println(");");
    }

    public void execute() {
        ProcDecl callee = Parser.procTable.get(name);
        if (callee == null) {
            System.out.println("ERROR: Calling undeclared procedure " + name);
            System.exit(0);
        }
        if (callee.formals.size() != actuals.size()) {
            System.out.println("ERROR: Arity mismatch for procedure " + name);
            System.exit(0);
        }

        ArrayList<Memory.Variable> actualRefs = new ArrayList<>();
        for (int i = 0; i<actuals.size(); i++) {
            String a = actuals.get(i).getId();
            Memory.Variable v = Memory.refOf(a);
            if (v == null) {
                System.out.println("ERROR: Actual parameter " + a + " not found");
                System.exit(0);
            }
            if (v.type != Core.OBJECT) {
                System.out.println("ERROR: Actual parameter " + a + " is not an object");
                System.exit(0);
            }
            actualRefs.add(v);
        }

        Memory.pushFrame();
        Memory.pushScope();

        for (int i = 0; i < callee.formals.size(); i++) {
            String f = callee.formals.get(i).getId();
            Memory.declareObject(f);
            Memory.aliasToRef(f, actualRefs.get(i));
        }

        if (callee.ds != null) {
            callee.ds.execute();
        }
        callee.body.execute();

        Memory.popScope();
        Memory.popFrame();
    }
}
