package com.github.meinlebenswerk.mclogic.hlt;

//Block ID-Format -> see idformat.txt

import com.github.meinlebenswerk.mclogic.glt.gate;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class translator {
    //this class just handles to structuredef to mc-blocks conversion
    String fname;

    public translator(String fname){
        this.fname = fname;
    }

    public void init(){
        //this loads the structuredefs from a .sdef file
        //name,size(x,y,z),delay,blocks,i/o labelling;

        File f = new File(fname);
        Scanner scanner;

        try { scanner = new Scanner(f); } catch (FileNotFoundException e) { return; }
        scanner.useDelimiter(";|\n");
        while(scanner.hasNext()){
            String s = scanner.next();
            if(s.length()>=2 && s.substring(0,2).equals("//")){
                //this is a comment, ignore
            }else if(s.length()!=0){
                //System.out.println(s);
                s.replaceAll(";","");
                String[] data = s.split(",");
                System.out.println(Arrays.toString(data));
                structure strut = new structure(data[0],data[1],Integer.parseInt(data[2]),data[3],data[4]);
            }
        }
    }

    public void placeGates(List<gate> gatelist, int[][] layers){
        //first get the width of the widest gate -> that determines scaling.
        //then just via translator align them.
    }

}



class structure{
    int[][][] data;
    int[][] iomap;
    String name;
    int delay;

    int sx,sy,sz;

    structure(String name,String size,int delay, String din, String io){
        this.name  = name;
        this.delay = delay;

        //parse size;
        String[] tmp = size.split("x");
        sx = Integer.parseInt(tmp[0]);
        sy = Integer.parseInt(tmp[1]);
        sz = Integer.parseInt(tmp[2]);

        data = new int[sx][sy][sz];

        //parse data
        tmp = din.replaceAll(" ","").split(".");
        for(int i=0;i<tmp.length;i++){
            String[] tt = tmp[i].replaceAll("(|)","").split(":");
            int y = (int) Math.floor(i/sz);
            int z = i%sz;
            for(int ii=0;ii<sx;ii++){
                int x = ii;
                data[x][y][z] = Integer.parseInt(tt[ii]);
            }
            System.out.println(Arrays.toString(data[0][0]));
        }
    }

    String makeReadable(){
        return String.format("Structure %s, size:[%d][%d][%d], delay:[]", name, sx,sy,sz, delay);
    }
}
