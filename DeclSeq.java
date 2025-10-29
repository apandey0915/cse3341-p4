class DeclSeq {
    Decl decl;          // integer/object
    ProcDecl pdecl;     // procedure
    DeclSeq ds;

    // A decl starts with one of these tokens
    private boolean startsDecl() {
        Core t = Parser.scanner.currentToken();
        return t == Core.INTEGER || t == Core.OBJECT || t == Core.PROCEDURE;
    }

    void parse() {
        if (!startsDecl()) return;

        if (Parser.scanner.currentToken() == Core.INTEGER || Parser.scanner.currentToken() == Core.OBJECT) {
            decl = new Decl();
            decl.parse();
        } else { // PROCEDURE
            pdecl = new ProcDecl();
            pdecl.parse();
        }

        if (startsDecl()) {
            ds = new DeclSeq();
            ds.parse();
        }
    }

    void print(int indent) {
        if (decl != null) decl.print(indent);
        if (pdecl != null) pdecl.print(indent);
        if (ds != null) ds.print(indent);
    }

    void execute() {
        if (decl != null) decl.execute();
        if (ds != null) ds.execute();
        // pdecl is compile-time only (registered in procTable)
    }
}
