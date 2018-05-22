package inter;

import lexer.*;
import symbols.*;

public class Not extends Logical {
    public Not(Token tok, Expression x2){
        super(tok, x2, x2);
    }
    public void jumping(int t, int f){
        expr2.jumping(f,t);
    }

    @Override
    public String toString() {
        return op.toString()+" "+expr2.toString();
    }
}
