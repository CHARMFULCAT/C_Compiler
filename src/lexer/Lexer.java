package lexer;


import java.util.*;
import java.io.*;
import symbols.*;

public class Lexer {
    public static int line = 1;
    char peek = ' ';
    Hashtable words = new Hashtable();
    void reserve(Word w){
        words.put(w.lexeme, w);
    }

    FileInputStream IpStream;
    public Lexer(File x) throws FileNotFoundException {
        IpStream = new FileInputStream(x);
        reserve( new Word("if", Tag.IF));
        reserve( new Word("else", Tag.ELSE));
        reserve( new Word("while", Tag.WHILE));
        reserve( new Word("do", Tag.DO));
        reserve( new Word("break", Tag.BREAK));
        reserve( Word.True);
        reserve( Word.False);
        reserve( Type.Int);
        reserve( Type.Char);
        reserve( Type.Bool);
        reserve( Type.Float);
    }

    void readChar() throws IOException {
        peek = (char)IpStream.read();
    }

    boolean readChar(char c) throws IOException{
        readChar();
        if(peek != c) return false;
        peek = ' ';
        return true;
    }

    //扫描可用的词素
    //跳过空白，先判断是否<=之类的复合词法单元，若否再判断为单字词法单元
    public Token scan() throws IOException{
        for(;;readChar()){
            if(peek == ' ' || peek == '\t' || peek == '\r'){
                continue;
            }
            else if(peek == '\n'){
                line = line + 1;
            }
            else {
                break;
            }
        }
        switch ( peek ){
            case '&':
                if(readChar('&')) return Word.and;
                else return new Token('&');
            case '|':
                if(readChar('|')) return Word.or;
                else return new Token('|');
            case '=':
                if(readChar('=')) return Word.eq;
                else return new Token('=');
            case '!':
                if(readChar('=')) return Word.ne;
                else return new Token('!');
            case '<':
                if(readChar('=')) return Word.le;
                else return new Token('<');
            case '>':
                if(readChar('=')) return Word.ge;
                else return new Token('>');
        }
        if(Character.isDigit((peek))){
            int v = 0;
            do{
                v = 10 * v + Character.digit(peek, 10);
                readChar();
            }while(Character.isDigit(peek));
            if(peek != '.'){
                return new Num(v);
            }
            float x = v;
            float d = 10;
            for(;;){
                readChar();
                if(! Character.isDigit(peek)){
                    break;
                }
                x += Character.digit(peek, 10)/ d;
                d *= 10;
            }
            return new Real(x);
        }
        if( Character.isLetter(peek)){
            StringBuffer b = new StringBuffer();
            do{
                b.append(peek);
                readChar();
            }while(Character.isLetterOrDigit(peek));
            String s = b.toString();
            Word w = (Word)words.get(s);
            if(w != null){
                return w;
            }
            w = new Word(s, Tag.ID);
            words.put(s,w);
            return w;
        }
        //最后peek中的任意字符都作为词法单元返回
        Token tok = new Token(peek);
        peek = ' ';
        return tok;
    }
}

