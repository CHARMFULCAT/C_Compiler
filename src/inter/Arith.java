package inter;

import lexer.*;
import symbols.*;

public class Arith extends Op {
    public Expression expr1, expr2;
    public Arith(Token tok, Expression x1, Expression x2){
        super(tok, null);
        expr1 = x1;
        expr2 = x2;
        type = Type.max(expr1.type, expr2.type);
        if(type == null){
            error("type error");
        }
    }

    public Expression generate(){
        return new Arith(op, expr1.reduce(), expr2.reduce());
    }

    public String toString(){
            return expr1.toString()+" "+op.toString()+" "+expr2.toString();
        }
}
