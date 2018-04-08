package com.github.meinlebenswerk.mclogic;

import com.github.meinlebenswerk.mclogic.glt.ice;
import com.github.meinlebenswerk.mclogic.hlt.parman;
import com.github.meinlebenswerk.mclogic.hlt.translator;

public class Main {

    public static void main( String args[] ){
        gllparser parser = new gllparser("testfile.gll");
        ice ic = parser.lexAndTranslate();

        translator tr = new translator("structures.sdef");
        tr.init();

        parman par = new parman(tr);
        par.layered_par(ic);

        //blockLUT lut = new blockLUT("blocklut.txt");
        //lut.init();
        //lut.printInfo();

    }
}
