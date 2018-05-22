package inter;

import lexer.*;
import symbols.*;

public class Unary extends Op {
    public Expression expr;
    public Unary(Token tok, Expression x){
        super(tok, null);
        expr = x;
        type = Type.max(Type.Int, expr.type);
        if(type == null) {
            error("type error");
        }
    }
    public Expression generate() {
        return new Unary(op, expr.reduce());
    }

    @Override
    public String toString() {
        return op.toString()+" "+expr.toString();
    }
}
