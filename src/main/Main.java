package main;

import java.io.*;
import lexer.*;
import parser.*;

public class Main {
    public static void main(String[] args) throws  IOException{
        File tobecompiled = new File(args[0]);

        Lexer lex = new Lexer(tobecompiled);
        Parser parse = new Parser(lex);
        parse.program();
        System.out.write('\n');
    }
}
