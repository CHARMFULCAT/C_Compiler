package symbols;

import lexer.*;

import java.awt.font.NumericShaper;

public class Type extends Word {
    public int width = 0;
    public Type(String s, int tag, int w){
        super(s, tag);
        width = w;
    }
    public static final Type
        Int = new Type("int", Tag.BASIC, 4),
        Float = new Type("float", Tag.BASIC, 8),
        Char = new Type("char", Tag.BASIC, 1),
        Bool = new Type("bool", Tag.BASIC, 1);

    public static boolean numeric(Type a){
        if( a == Type.Char ||
                a == Type.Int ||
                a == Type.Float){
            return true;
        }
        else{
            return false;
        }
    }

    public static Type max(Type a1, Type a2){
        if(!numeric(a1) || ! numeric(a2)){
            return null;
        }
        else if(a1 == Type.Float || a2 == Type.Float){
            return Type.Float;
        }
        else if(a1 == Type.Int || a2 == Type.Int){
            return Type.Int;
        }
        else{
            return Type.Char;
        }
}
}