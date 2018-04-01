package com.github.meinlebenswerk.mclogic.hlt;

import com.github.meinlebenswerk.mclogic.glt.gate;
import com.github.meinlebenswerk.mclogic.glt.ice;
import com.github.meinlebenswerk.mclogic.glt.net;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class parman {
    //the place and route manager
    translator tl;

    public parman(translator tl){
        this.tl = tl;
    }

    public void layered_par(ice ce){


        //First: make "depthtable" that contains a signals depth-value
        //STEP 1
        //eg: first-level inputs have 0, signals that depend on one signal have 1 and signals that depend on l1 signals are l3...
        List<net>  netlist  = ce.getNetlist();
        List<gate> gatelist = ce.getGates();


        //search should work like this::
        // check any connected logic gates
        // then check if gates connected to the input of these gates have the same level
        // if yes -> then increment the current nets level
        // do that until well idk (but maximum: netlist.size (basically linear design));

        boolean searching = true;
        int highest_level = 0;
        for (int j = 0; j < netlist.size(); j++) {
            for (int i = 0; i < netlist.size(); i++) {
                net n = netlist.get(i);
                for (gate g : gatelist) {
                    //check if the gate affects the net
                    for (int out : g.getOutput()) {
                        if (out == n.getId()) {
                            //this means yes!
                            //now check if we have the same level as the inputs
                            for (int in : g.getInput()) {
                                if (netlist.get(in).getLevel() >= n.getLevel()) {
                                    n.incrementLevel();
                                    if (n.getLevel() > highest_level) {
                                        highest_level = n.getLevel();
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        debugPringNL(netlist);

        //STEP 2
        //then group the gates into layers, depending on level (here gate-ids are used)
        int[] cnt = new int[highest_level+1];
        int[][] layers = new int[highest_level+1][];
        for (int i = 0; i <= highest_level; i++) {
            for (int j = 0; j < netlist.size(); j++) {
                if(netlist.get(j).getLevel() == i){
                    cnt[i]++;
                }
            }
            layers[i] = new int[cnt[i]];
            int index = 0;
            for (int j = 0; j < netlist.size(); j++) {
                if(netlist.get(j).getLevel() == i){
                    layers[i][index] = j;
                    index++;
                }
            }
        }

        //STEP 3
        //then physically place the gates as described in the net
        tl.placeGates(gatelist,layers);


        //STEP 4 route !
    }

    private void debugPringNL(List<net> netlist){
        Iterator<net> iter = netlist.iterator();
        while (iter.hasNext()) {
            net nt = iter.next();
            System.out.println(nt.makeReadable());
        }
        iter = null;
    }

}


