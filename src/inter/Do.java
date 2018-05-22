package inter;


import symbols.*;

public class Do extends Statement {
    Expression expr;
    Statement stmt;
    public Do(){
        expr = null;
        stmt = null;
    }
    public void init(Statement s, Expression x){
        expr = x;
        stmt = s;
        if(expr.type != Type.Bool) {
            expr.error("boolean required in while");
        }
    }
    public void generate(int b, int a){
        after = a;
        int label = newlabel();
        stmt.generate(b,label);
        emitlabel(label);
        expr.jumping(b,0);
    }
}