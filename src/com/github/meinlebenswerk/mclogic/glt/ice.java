package com.github.meinlebenswerk.mclogic.glt;

import java.util.List;
import java.util.Map;

public class ice {

    //internal circuit element :)
    List<net> netlist;
    List<gate> gatelist;

    Map<String, Integer> codename_lut;

    public ice(List<net> netlist, List<gate> gatelist,Map<String, Integer> codename_lut){
        this.netlist  = netlist;
        this.gatelist = gatelist;

        this.codename_lut = codename_lut;
    }

    public List<gate> getGates(){
        return gatelist;
    }

    public List<net> getNetlist(){
        return netlist;
    }
    public Map<String, Integer> getCodename_lut(){
        return codename_lut;
    }
}
