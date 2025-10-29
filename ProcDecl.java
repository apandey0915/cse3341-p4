import java.util.*;

class ProcDecl {
    String name;
    FormalList formals;
    DeclSeq ds;      // optional locals
    StmtSeq body;

    void parse() {
        Parser.expectedToken(Core.PROCEDURE); Parser.scanner.nextToken();

        Parser.expectedToken(Core.ID);
        name = Parser.scanner.getId(); Parser.scanner.nextToken();

        Parser.expectedToken(Core.LPAREN); Parser.scanner.nextToken();
        formals = new FormalList(); formals.parse();
        Parser.expectedToken(Core.RPAREN); Parser.scanner.nextToken();

        Parser.expectedToken(Core.IS); Parser.scanner.nextToken();

        // optional locals (int/obj or nested procs)
        if (Parser.scanner.currentToken() == Core.INTEGER
         || Parser.scanner.currentToken() == Core.OBJECT
         || Parser.scanner.currentToken() == Core.PROCEDURE) {
            ds = new DeclSeq(); ds.parse();
        }

        // Body directly (no required BEGIN): parse statements until END
        body = new StmtSeq(); body.parse();

        Parser.expectedToken(Core.END); Parser.scanner.nextToken();

        // checks + register
        if (Parser.procTable.containsKey(name)) {
            System.out.println("ERROR: Duplicate procedure name " + name);
            System.exit(0);
        }
        formals.ensureDistinct();
        Parser.procTable.put(name, this);
    }

    void print(int indent) {
        tab(indent); System.out.print("procedure " + name + "("); formals.print(); System.out.println(") is");
        if (ds != null) ds.print(indent + 1);
        body.print(indent + 1);
        tab(indent); System.out.println("end");
    }

    private void tab(int n){ for (int i=0;i<n;i++) System.out.print("\t"); }
}
