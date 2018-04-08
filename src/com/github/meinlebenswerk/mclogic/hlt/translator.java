package com.github.meinlebenswerk.mclogic.hlt;

//Block ID-Format -> see idformat.txt

import com.github.meinlebenswerk.mclogic.glt.gate;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class translator {
    //this class just handles to structuredef to mc-blocks conversion
    private String fname;
    private List<structure> struts;

    private int padding_z = 1;
    private int padding_x = 5;

    private int base_y = 20;

    public translator(String fname){
        this.fname  = fname;
        this.struts = new ArrayList<>();
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
                structure strut = new structure(data[0],data[1],Integer.parseInt(data[2]),data[3],data[4]);
                System.out.println(strut.makeReadable());
                struts.add(strut);
            }
        }

        System.out.println("Loaded Gates in HLT-Table ::");
        for(structure s : struts){
            System.out.println(s.getName());
        }
    }

    public void placeGates(List<gate> gatelist, int[][] layers){
        //layers is a 2d array of gatelist-ids (basically can be regarded as pointers.) layers[layer][gate] = gateID
        //TODO: instead of saving the pointer -> save a rep of the gate that we're talking about


        //first get the length of the longest gate -> that determines scaling for that layer.
        //basically looks like this:
        // gate gate gate ^ next layer -> more gates ^length -> width [^x ->z]
        int[] layersize = new int[layers.length];
        for(int i=0;i<layers.length;i++){
            layersize[i] = 0;
            for(int ii=0;ii<layers[i].length;ii++){
                structure tmp_s = lookUpGateStructure(gatelist.get(layers[i][ii]));
                if(tmp_s.getSX() > layersize[i]){
                    layersize[i] = tmp_s.getSX();
                }
            }
        }

        //then just via translator align them.
        int cx = 0;
        int index = 0;
        structure[] structures = new structure[gatelist.size()];
        for(int i=0;i<layers.length;i++){
            structure[] tmp_struts = alignLayerSimple(cx,base_y,0, gatelist, layers[i]);
            cx += layersize[i] + padding_x;

            System.arraycopy(tmp_struts, 0, structures, index, layers[i].length);
            index += layers[i].length;
        }

        //now all the gates exists as structures in the "structures" list -> TODO: use structure_instances to save memory :)

        //This concludes the place part

        System.out.println("place_done");
    }

    private structure[] alignLayerSimple(int start_x, int start_y, int start_z, List<gate> gatelist, int[] layer_data){
        //this functions places one layer with the "simple"-place algorithm

        int cposx, cposy, cposz;
        cposx = start_x;
        cposy = start_y;
        cposz = start_z;
        structure[] layer_strut = new structure[layer_data.length];

        for(int i=0;i<layer_data.length;i++){
            layer_strut[i] = lookUpGateStructure(gatelist.get(layer_data[i]));
            layer_strut[i].setPosition(cposx,cposy,cposz);

            cposz += padding_z + layer_strut[i].getSZ();
        }

        return layer_strut;
    }

    private structure lookUpGateStructure(gate g){
        //TODO search in struts
        for(structure s : struts){
            if(s.getName().equalsIgnoreCase(g.getType().name())){
                return s;
            }
        }
        return null;
    }

}
