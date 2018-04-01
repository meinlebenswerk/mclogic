package com.github.meinlebenswerk.mclogic.glt;

import java.util.Arrays;

public class gate {

    public enum gate_type{
        OR, NOT, XOR, AND, IMPLIES, UNKNW
    }

    private int[] input;
    private int[] output;
    private gate_type type;

    public gate(int[] input, int output, gate_type type){
        this.output    = new int[1];
        this.output[0] = output;
        this.input     = Arrays.copyOf(input,input.length);
        this.type      = type;
    }

    public gate(int[] input, int[] output, gate_type type){
        this.input  = Arrays.copyOf(input,input.length);
        this.output = Arrays.copyOf(output,output.length);
        this.type   = type;
    }

    public int[] getInput(){
        return input;
    }

    public int[] getOutput(){
        return output;
    }

    public gate_type getType(){
        return type;
    }


    public String makeReadable(){
        return String.format("%s inputs%s output%s", type.name(), Arrays.toString(input), Arrays.toString(output));
    }
}
