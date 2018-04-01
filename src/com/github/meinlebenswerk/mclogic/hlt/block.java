package com.github.meinlebenswerk.mclogic.hlt;

public class block{
    int id;
    int meta;
    String name;

    public block(int id, int meta, String name){
        this.id   = id;
        this.meta = meta;
        this.name = name;
    }

    public int getID(){
        return this.id;
    }

    public int getMeta(){
        return this.meta;
    }

    public String getName(){
        return this.name;
    }


}