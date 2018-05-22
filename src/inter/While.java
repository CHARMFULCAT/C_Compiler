package inter;

import symbols.*;

public class While extends Statement {
    Expression expr;
    Statement stmt;
    public While(){
        expr = null;
        stmt = null;
    }
    public void init(Expression x, Statement s){
        expr = x;
        stmt = s;
        if(expr.type != Type.Bool) {
            expr.error("boolean required in while");
        }
    }
    public void generate(int b, int a){
        after = a;
        expr.jumping(0,a);
        int label = newlabel();
        emitlabel(label);
        stmt.generate(label, b);
        emit("goto L"+b);
    }
}
