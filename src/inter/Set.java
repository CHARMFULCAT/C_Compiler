package inter;

import lexer.*;
import symbols.*;

public class Set extends Statement {
    public Id id;
    public Expression expr;
    public Set(Id i, Expression x){
        id = i;
        expr = x;
        if(check(id.type, expr.type) == null){
            error("type error");
        }
    }
    public Type check(Type p1, Type p2){
        if(Type.numeric(p1) && Type.numeric(p2)){
            return p2;
        }
        else if(p1 == Type.Bool && p2 == Type.Bool){
            return p2;
        }
        else{
            return null;
        }
    }
    public void generate(int b, int a){
        emit(id.toString()+" = "+expr.generate().toString());
    }

}
