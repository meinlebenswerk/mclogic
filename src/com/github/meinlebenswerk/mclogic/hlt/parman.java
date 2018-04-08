package com.github.meinlebenswerk.mclogic.hlt;

import com.github.meinlebenswerk.mclogic.glt.gate;
import com.github.meinlebenswerk.mclogic.glt.ice;
import com.github.meinlebenswerk.mclogic.glt.net;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class parman {
    //the place and route manager
    private translator tl;

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
        //then group the nets into layers, depending on level (here net-ids are used)
        int[] cnt = new int[highest_level+1];
        int[][] net_layers = new int[highest_level+1][];
        for (int i = 0; i <= highest_level; i++) {
            for (int j = 0; j < netlist.size(); j++) {
                if(netlist.get(j).getLevel() == i){
                    cnt[i]++;
                }
            }
            net_layers[i] = new int[cnt[i]];
            int index = 0;
            for (int j = 0; j < netlist.size(); j++) {
                if(netlist.get(j).getLevel() == i){
                    net_layers[i][index] = j;
                    index++;
                }
            }
        }
        //as well as the gates::
        int[] tmp = new int[highest_level+1];
        int[][] gate_layers = new int[highest_level+1][];
        //first determine levels; tmp contains the occurrences of the levels
        for(gate g : gatelist){
            //gate level is determined by highest input-level
            int lvl = 0;
            for(int in : g.getInput()){
                if(netlist.get(in).getLevel() > lvl){
                    lvl = netlist.get(in).getLevel();
                }
            }
            g.setLevel(lvl);
            tmp[lvl] += 1;
        }
        for(int i=0;i<tmp.length;i++){
            gate_layers[i] = new int[tmp[i]];
        }

        //then sort the gates accordingly; tmp is now the level_index_counter
        tmp = new int[highest_level+1];
        for(gate g : gatelist){
            gate_layers[g.getLevel()][tmp[g.getLevel()]] = gatelist.indexOf(g);
            tmp[g.getLevel()] ++;
        }


        //STEP 3
        //then physically place the gates as described in the net
        tl.placeGates(gatelist,gate_layers);


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


