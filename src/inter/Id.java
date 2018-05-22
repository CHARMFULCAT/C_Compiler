package inter;

import lexer.*;
import symbols.*;

public class Id extends Expression {
    public int offset;
    public Id(Word id, Type p, int b){
        super(id, p);
        offset = b;
    }
}
