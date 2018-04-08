package com.github.meinlebenswerk.mclogic;

import com.github.meinlebenswerk.mclogic.glt.ice;
import com.github.meinlebenswerk.mclogic.glt.optimizer;
import com.github.meinlebenswerk.mclogic.pac.lexer;
import com.github.meinlebenswerk.mclogic.pac.parser;
import com.github.meinlebenswerk.mclogic.pac.token;

import java.io.File;
import java.util.*;

public class gllparser {

    String fname;

    public gllparser(String fname){
        this.fname = fname;
    }

    public ice lexAndTranslate(){
        File file = new File(fname);
        if(!file.exists()){ return null; }
        System.out.println("File exists, starting parse...");

        lexer lex = new lexer();
        List<token> tokenList = lex.lex(file);

        parser parser = new parser();
        ice ce =  parser.readTokenList(tokenList);

        optimizer opt = new optimizer(ce);
        //opt.optimizeGates();

        return ce;
    }


}
