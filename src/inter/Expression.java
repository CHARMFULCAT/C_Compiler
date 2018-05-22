package inter;

import lexer.*;
import symbols.*;

public class Expression extends Node {
    public Token op;
    public Type type;
    Expression(Token tok, Type p){
        op = tok;
        type = p;
    }

    public Expression generate(){
        return this;
    }

    public Expression reduce(){
        return this;
    }

    public void jumping(int t, int f){
        emitjumps(toString(), t, f);
    }
    public void emitjumps(String test, int t, int f){
        if(t != 0 && f != 0){
            emit("if "+test+" goto L"+t);
            emit("goto L" + f);
        }
        else if(t != 0){
            emit("if "+test+" goto L"+t);
        }
        else if(f != 0){
            emit("iffalse "+test+" goto L"+f);
        }
        else;
    }
    public String toString(){
        return op.toString();
    }
}

