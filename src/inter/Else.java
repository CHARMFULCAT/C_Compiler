package inter;

import symbols.*;

public class Else extends Statement{
    Expression expr;
    Statement stmt1, stmt2;
    public Else(Expression x, Statement s1, Statement s2){
        expr = x;
        stmt1 = s1;
        stmt2 = s2;
        if(expr.type != Type.Bool){
            expr.error("boolean required in if");
        }
    }
    public void generate(int b, int a){
        int label1 = newlabel();
        int label2 = newlabel();
        expr.jumping(0, label2);
        emitlabel(label1);
        stmt1.generate(label1, a);
        emit("goto L"+a);

        emitlabel(label2);
        stmt2.generate(label2, a);
    }
}