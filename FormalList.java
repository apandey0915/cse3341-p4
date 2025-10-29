import java.util.*;

class FormalList {
    List<Id> ids = new ArrayList<>();

    void parse() {
        // empty parameter list?
        if (Parser.scanner.currentToken() == Core.RPAREN) return;

        // [object] ID { , [object] ID }*
        while (true) {
            if (Parser.scanner.currentToken() == Core.OBJECT) {
                Parser.scanner.nextToken();
            }
            Parser.expectedToken(Core.ID);
            Id id = new Id(); id.parse(); ids.add(id);

            if (Parser.scanner.currentToken() == Core.COMMA) {
                Parser.scanner.nextToken();
            } else break;
        }
    }

    void ensureDistinct() {
        HashSet<String> seen = new HashSet<>();
        for (Id id : ids) {
            String s = id.getId();
            if (!seen.add(s)) {
                System.out.println("ERROR: Duplicate formal parameter " + s);
                System.exit(0);
            }
        }
    }

    int size() { return ids.size(); }
    Id get(int i) { return ids.get(i); }

    void print() {
        for (int i=0;i<ids.size();i++) {
            ids.get(i).print();
            if (i+1<ids.size()) System.out.print(", ");
        }
    }
}
