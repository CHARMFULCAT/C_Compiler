package inter;

import symbols.*;

public class If extends Statement{
    Expression expr;
    Statement stmt;
    public If(Expression x, Statement s){
        expr = x;
        stmt = s;
        if(expr.type != Type.Bool){
            expr.error("boolean required in if");
        }
    }
    public void generate(int b, int a){
        int label = newlabel();
        expr.jumping(0, a);
        emitlabel(label);
        stmt.generate(label, a);
    }
}
