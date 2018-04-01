package com.github.meinlebenswerk.mclogic.hlt;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class blockLUT{
    block[] LUT;

    String fname; //normally blocklut.txt
    public blockLUT(String fname){
        this.fname = fname;
    }

    public void init(){
        //open File, open Scanner...
        File tmp = new File(fname);
        if(!tmp.exists()){ return; }
        //System.out.println("File exists, starting parse...");
        Scanner sc = null;
        try {sc = new Scanner(tmp); } catch (FileNotFoundException e) { return; }
        sc.useDelimiter(";");

        ArrayList<String> ds = new ArrayList<String>();
        String[] data;

        while(sc.hasNext()) {
            String s = sc.next();
            if (!s.isEmpty()) {
                s = s.replaceAll(";","");
                s = s.replaceAll("\n","");
                //System.out.print(s);

                if(!s.substring(0,2).equals("//")){
                    //not a comment!
                    ds.add(s);
                }
            }
        }

        //find highest index! -->
        int len = 0;
        String[] tt;
        for(int i=0;i<ds.size();i++){
            tt = ds.get(i).split(",");
            if(Integer.parseInt(tt[0]) > len){len = Integer.parseInt(tt[0]);}
        }
        len++;

        LUT = new block[len];
        for(int i=0;i<ds.size();i++){
            tt = ds.get(i).split(",");
            int index = Integer.parseInt(tt[0]);
            LUT[index] = new block(Integer.parseInt(tt[1]), Integer.parseInt(tt[2]),tt[3]);
        }
        sc.close();
    }

    public void printInfo(){
        System.out.println("LUT-Size: "+LUT.length);
        for(int i=0;i<LUT.length;i++) {
            System.out.println("Block: ");
            System.out.println(String.format("INTERNAL_ID[%d], MC_ID[%d], MC_META[%d], INTERNAL_NAME[%s], ",i,LUT[i].getID(),LUT[i].getMeta(),LUT[i].getName()));
        }
    }
}