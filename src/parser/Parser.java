package parser;

import java.io.*;
import lexer.*;
import symbols.*;
import inter.*;

public class Parser {
    private Lexer lex;
    private Token now;
    Environment top = null;
    int used = 0;

    public Parser(Lexer l) throws IOException {
        lex = l;
        next();
    }

    void next() throws IOException {
        now = lex.scan();
    }

    void error(String s) {
        throw new Error("near line " + lex.line + ": " + s);
    }

    void is(int t) throws IOException {
        if (now.tag == t) {
            next();
        } else {
            error("syntax error");
        }
    }


    public void program() throws IOException {
        Statement s = block();
        int begin = s.newlabel();
        int after = s.newlabel();
        s.emitlabel(begin);
        s.generate(begin, after);
        s.emitlabel(after);
    }

    //block用于对输入流进行语法分析
    Statement block() throws IOException {
        is('{');
        Environment savedEnv = top;
        top = new Environment(top);
        decls();
        Statement s = statements();
        is('}');
        top = savedEnv;
        return s;
    }

    void decls() throws IOException {
        while (now.tag == Tag.BASIC) {
            Type p = type();
            Token tok = now;
            is(Tag.ID);
            is(';');
            Id id = new Id((Word) tok, p, used);
            top.put(tok, id);
            used += p.width;
        }
    }

    Type type() throws IOException {
        Type p = (Type) now;
        is(Tag.BASIC);
        if (now.tag != '[') {
            return p;
        } else {
            return dimension(p);
        }
    }

    Type dimension(Type p) throws IOException {
        is('[');
        Token tok = now;
        is(Tag.NUM);
        is(']');
        if (now.tag == '[') {
            p = dimension(p);
        }
        return new Array(((Num) tok).value, p);
    }

    Statement statements() throws IOException {
        if (now.tag == '}') {
            return Statement.NULL;
        } else {
            return new Sequence(statement(), statements());
        }
    }

    Statement statement() throws IOException {
        Expression x;
        Statement s, s1, s2;
        Statement savedStatement;
        switch (now.tag) {
            case Tag.IF:
                is(Tag.IF);
                is('(');
                x = bool();
                is(')');
                s1 = statement();
                if (now.tag != Tag.ELSE) {
                    return new If(x, s1);
                }
                is(Tag.ELSE);
                s2 = statement();
                return new Else(x, s1, s2);
            case (int)';':
                next();
                return Statement.NULL;
            case Tag.WHILE:
                While whilenode = new While();
                savedStatement = Statement.Enclosing;
                Statement.Enclosing = whilenode;
                is(Tag.WHILE);
                is('(');
                x = bool();
                is(')');
                s1 = statement();
                whilenode.init(x, s1);
                Statement.Enclosing = savedStatement;
                return whilenode;
            case Tag.DO:
                Do donode = new Do();
                savedStatement = Statement.Enclosing;
                Statement.Enclosing = donode;
                is(Tag.DO);
                s1 = statement();
                is(Tag.WHILE);
                is('(');
                x = bool();
                is(')');
                is(';');
                donode.init(s1, x);
                Statement.Enclosing = savedStatement;
                return donode;
            case Tag.BREAK:
                is(Tag.BREAK);
                is(';');
                return new Break();
            case (int)'{':
                return block();
            default:
                return assign();
        }
    }

    Statement assign() throws IOException {
        Statement statement;
        Token t = now;
        is(Tag.ID);
        Id id = top.get(t);
        if (id == null) {
            error(t.toString() + " undeclared");
        }
        if (now.tag == '=') {
            next();
            statement = new Set(id, bool());
        } else {
            Access x = offset(id);
            is('=');
            statement = new SetElem(x, bool());
        }
        is(';');
        return statement;
    }

    Expression bool() throws IOException {
        Expression x = join();
        while (now.tag == Tag.OR) {
            Token tok = now;
            next();
            x = new Or(tok, x, join());
        }
        return x;
    }

    Expression join() throws IOException {
        Expression x = equality();
        while (now.tag == Tag.AND) {
            Token tok = now;
            next();
            x = new And(tok, x, equality());
        }
        return x;
    }

    Expression equality() throws IOException {
        Expression x = rel();
        while (now.tag == Tag.EQ ||
                now.tag == Tag.NE) {
            Token tok = now;
            next();
            x = new Rel(tok, x, rel());
        }
        return x;
    }

    Expression rel() throws IOException {
        Expression x = expr();
        switch (now.tag) {
            case '<':
            case Tag.LE:
            case Tag.GE:
            case '>':
                Token tok = now;
                next();
                return new Rel(tok, x, expr());
            default:
                return x;
        }
    }

    Expression expr() throws IOException {
        Expression x = term();
        while (now.tag == '+' || now.tag == '-') {
            Token tok = now;
            next();
            x = new Arith(tok, x, term());
        }
        return x;
    }

    Expression term() throws IOException {
        Expression x = unary();
        while (now.tag == '*' || now.tag == '/') {
            Token tok = now;
            next();
            x = new Arith(tok, x, unary());
        }
        return x;
    }

    Expression unary() throws IOException {
        if (now.tag == '-') {
            next();
            return new Unary(Word.minus, unary());
        } else if (now.tag == '!') {
            Token tok = now;
            next();
            return new Not(tok, unary());
        } else {
            return factor();
        }
    }

    Expression factor() throws IOException {
        Expression x = null;
        switch (now.tag) {
            case '(':
                next();
                x = bool();
                is(')');
                return x;
            case Tag.NUM:
                x = new Constant(now, Type.Int);
                next();
                return x;
            case Tag.REAL:
                x = new Constant(now, Type.Float);
                next();
                return x;
            case Tag.TRUE:
                x = Constant.True;
                next();
                return x;
            case Tag.FALSE:
                x = Constant.False;
                next();
                return x;
            default:
                error("syntax error");
                return x;
            case Tag.ID:
                String s = now.toString();
                Id id = top.get(now);
                if (id == null) {
                    error(now.toString() + " undeclared");
                }
                next();
                if (now.tag != '[') {
                    return id;
                } else {
                    return offset(id);
                }
        }
    }

    Access offset(Id a) throws IOException {
        Expression i;
        Expression w;
        Expression t1, t2;
        Expression loc;
        Type type = a.type;
        is('[');
        i = bool();
        is(']');
        type = ((Array)type).of;
        w = new Constant(type.width);
        t1 = new Arith(new Token('*'), i, w);
        loc = t1;
        while (now.tag == '[') {
            is('[');
            i = bool();
            is(']');
            type = ((Array) type).of;
            w = new Constant(type.width);
            t1 = new Arith(new Token('*'), i, w);
            t2 = new Arith(new Token('+'), loc, t1);
            loc = t2;
        }
        return new Access(a, loc, type);
    }
}
