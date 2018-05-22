package inter;

public class Break extends Statement {
    Statement stmt;
    public Break(){
        if(Statement.Enclosing == Statement.NULL) {
            error("unenclosed break");
        }
        stmt = Statement.Enclosing;
    }
    public void generate(int b, int a){
        emit("goto L"+stmt.after);
    }
}
