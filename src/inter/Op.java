package inter;

import symbols.*;
import lexer.*;

public class Op extends Expression {
    public Op(Token tok, Type p){
        super(tok, p);
    }
    public Expression reduce(){
        Expression x = generate();
        Temp t = new Temp(type);
        emit(t.toString() + " = " + x.toString());
        return t;
    }
}