package com.github.meinlebenswerk.mclogic.glt;

import java.util.Arrays;
import java.util.List;

public class optimizer {
    ice top;

    public optimizer(ice ce){
        this.top = ce;
    }

    public void optimizeGates(){
        //check for duplicate Gates -> this can happen when using Complex-logic

        List<gate> gl = top.getGates();
        for(int i=0;i<gl.size();i++){
            gate g1 = gl.get(i);
            compareloop: for(int ii=i+1;ii<gl.size();ii++){
                gate g2 = gl.get(ii);
                //compare gates
                if(g1.getType()!=g2.getType()){
                    continue compareloop;
                }
                //inputs
                if(!Arrays.equals(g1.getInput(),g2.getInput())){
                    continue compareloop;
                }

                //no need to compare outputs
                //if we are here -> g1==g2 -> delete one :) and put outputs together;

                int[] n_out = new int[g1.getOutput().length+g2.getOutput().length];
                for(int j=0;j<n_out.length;j++){
                    n_out[j] = (j<g1.getOutput().length) ? g1.getOutput()[j] : g2.getOutput()[j-g1.getOutput().length] ;
                }
                int[] inputs = g1.getInput();
                //remove both gates
                gl.remove(g1);
                gl.remove(g2);

                System.out.println(String.format("removed duplicate gates: [%d,%d]",i,ii));
                gate ng = new gate(inputs,n_out,g1.getType());
                System.out.println(ng.makeReadable());

                gl.add(ng);
            }
        }
    }
}
