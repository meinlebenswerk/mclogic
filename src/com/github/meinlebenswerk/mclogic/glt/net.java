package com.github.meinlebenswerk.mclogic.glt;

public class net {

    //A wire basically is the same as a net;
    //each net has a driver and an id (which is an INT);

    String codename;
    int id;
    int level;

    public net(String codename, int id){
        this.codename = codename;
        this.id = id;

        this.level = 0;
    }

    public String makeReadable(){
        return String.format("NET name:[%s], internal_id:[%d], level:[%d]", codename, id,level);
    }

    public String getCodename(){
        return codename;
    }

    public int getId(){
        return id;
    }

    public void incrementLevel(){level++;}
    public void decrementLevel(){level--;}
    public int getLevel(){return level;}
}
