package symbols;

import java.util.*;
import lexer.*;
import inter.*;

public class Environment {
    private Hashtable table;
    protected Environment prev;
    public Environment(Environment n){
        table = new Hashtable();
        prev = n;
    }

    public void put(Token w, Id i){
        table.put(w,i);
    }
    public Id get(Token w){
        for(Environment e = this; e != null; e = e.prev){
            Id found = (Id)(e.table.get(w));
            if(found != null) {
                return found;
            }
        }
        return null;
    }
}
