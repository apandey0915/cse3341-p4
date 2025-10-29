import java.util.*;

class ActualList {
    List<Id> ids = new ArrayList<>();

    void parse() {
        if (Parser.scanner.currentToken() == Core.RPAREN) return;
        Id id = new Id(); id.parse(); ids.add(id);
        while (Parser.scanner.currentToken() == Core.COMMA) {
            Parser.scanner.nextToken();
            id = new Id(); id.parse(); ids.add(id);
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
