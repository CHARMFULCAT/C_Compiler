package inter;

import lexer.*;
import symbols.*;

public class Access extends Op {
    public Id array;
    public Expression index;
    public Access(Id a, Expression i, Type p){
        super(new Word("[]",Tag.INDEX),p);
        array = a;
        index = i;
    }

    public Expression generate(){
        return new Access(array, index.reduce(),type);
    }

    public void jumping(int t, int f){
        emitjumps(reduce().toString(), t, f);
    }

    @Override
    public String toString(){
        return array.toString()+" [ "+index.toString()+" ]";
    }
}
