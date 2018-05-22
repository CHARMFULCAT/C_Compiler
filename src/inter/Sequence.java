package inter;

public class Sequence extends Statement {
    Statement stmt1;
    Statement stmt2;
    public Sequence(Statement s1, Statement s2){
        stmt1 = s1;
        stmt2 = s2;
    }

    public void generate(int b, int a){
        if(stmt1 == Statement.NULL){
            stmt2.generate(b,a);
        }
        else if(stmt2 == Statement.NULL){
            stmt1.generate(b,a);
        }
        else{
            int label = newlabel();
            stmt1.generate(b, label);
            emitlabel(label);
            stmt2.generate(label, a);
        }
    }
}

