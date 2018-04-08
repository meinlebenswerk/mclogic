package com.github.meinlebenswerk.mclogic.hlt;

import java.util.Arrays;

public class structure{
    private int[][][] data;
    private int[][] iomap;
    private String name;
    private int delay;

    private int sx,sy,sz;

    private int x, y, z;

    public structure(String name,String size,int delay, String din, String io){
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
            String[] tt = tmp[i].split(":");
            int y = (int) Math.floor(i/sz);
            int z = i%sz;
            for(int ii=0;ii<sx;ii++){
                int x = ii;
                data[x][y][z] = Integer.parseInt(tt[ii]);
            }
            System.out.println(Arrays.toString(data[0][0]));
        }
    }

    public void setPosition(int x,int y, int z){
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public int getSX(){return sx;}
    public int getSY(){return sy;}
    public int getSZ(){return sz;}

    public String getName(){return name;}

    public String makeReadable(){
        return String.format("Structure %s, size:[%d][%d][%d], delay:[%d]", name, sx,sy,sz, delay);
    }
}
