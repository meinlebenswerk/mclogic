package com.github.meinlebenswerk.mclogic.pac;

import com.github.meinlebenswerk.mclogic.glt.ice;
import com.github.meinlebenswerk.mclogic.glt.net;
import com.github.meinlebenswerk.mclogic.glt.gate;

import java.util.*;

public class parser {

    public ice readTokenList(List<token> tokenList){


        //STEP 1 -> isolate wires
        // also clean up comments and rogue newlines
        boolean pc = false;
        List<String> wires = new ArrayList<>();
        ListIterator<token> token_iter = tokenList.listIterator();

        while (token_iter.hasNext()) {
            token tk = token_iter.next();
            boolean parsing = true;
            if(tk.type == token.token_type.IDENTIFIER && tk.payload.equals("wire")){
                token_iter.remove();
                while(parsing){
                    tk = token_iter.next();
                    if(tk.type == token.token_type.IDENTIFIER){
                        wires.add(tk.payload.replace(",",""));
                    }else if(tk.type == token.token_type.SEMICOLON){
                        parsing = false;
                    }else{
                        //SYNTAX ERROR!
                    }
                    token_iter.remove();
                }
            }else if(tk.type == token.token_type.DIVIDE){
                //Cleans up comments
                tk = token_iter.next();
                if(tk.type == token.token_type.DIVIDE){
                    token_iter.remove();
                    //we have a comment
                    token_iter.previous();
                    token_iter.remove();
                    token_iter.next();
                    token_iter.remove();

                    while(token_iter.next().type != token.token_type.NEWLINE){
                        token_iter.remove();
                    }
                    token_iter.remove();
                }
            }else if(tk.type == token.token_type.NEWLINE){
                //cleans up rogue newlines
                token_iter.remove();
            }
        }

        //STEP 2 -> make wire <-> internal LUT
        Map<String, Integer> codename_lut = new HashMap<String, Integer>();
        List<net> nets = new ArrayList<>();
        int net_index = 0;
        Iterator<String> wire_iter = wires.iterator();
        while (wire_iter.hasNext()){
            net tmp_net = new net(wire_iter.next(), net_index);
            nets.add(tmp_net);

            codename_lut.put(tmp_net.getCodename(), net_index);
            net_index++;
        }

        //STEP 3 -> take in composite logic and create wires for that
        //TODO: make this step incremental allowing multiple boxed logic statements
        int int_wr = 0;

        for(int i=0;i<tokenList.size();i++){
            token tk = tokenList.get(i);

            if(tk.type == token.token_type.OPEN_PARENTHESIS){
                net tmp_net = new net("internal_"+int_wr,net_index);
                token logic_op;

                String a =  tokenList.get(i+1).payload;
                logic_op =  tokenList.get(i+2);
                //System.out.println(logic_op.makeReadable());
                String b =  tokenList.get(i+3).payload;

                tokenList.remove(i);
                tokenList.remove(i);
                tokenList.remove(logic_op);
                tokenList.remove(i);
                tokenList.remove(i);

                tokenList.add(i,new token(token.token_type.IDENTIFIER,tmp_net.getCodename()));
                //make new internal wire, replace the original declaration with that

                codename_lut.put(tmp_net.getCodename(), net_index);
                nets.add(tmp_net);

                //and insert assign statement into the end
                tokenList.add(new token(token.token_type.IDENTIFIER,"assign"));
                tokenList.add(new token(token.token_type.IDENTIFIER,tmp_net.getCodename()));
                tokenList.add(new token(token.token_type.EQUALS));
                tokenList.add(new token(token.token_type.IDENTIFIER,a));
                tokenList.add(logic_op);
                tokenList.add(new token(token.token_type.IDENTIFIER,b));
                tokenList.add(new token(token.token_type.SEMICOLON));

                net_index ++;
                int_wr ++;
            }
        }

        //debugPringNL(nets);
        //debugPrint(tokenList);

        //STEP 4 -> add in the gates -> TODO currently limited to 1 out and 2 ins
        int tmp_index;
        int output = 0;
        int[] input = new int[2];
        token_iter = tokenList.listIterator();
        List<gate> gateList = new ArrayList<>();

        for(int i=0;i<tokenList.size();i++) {
            //find assign statements and parse them to gates.
            token tk = tokenList.get(i);
            if(tk.type == token.token_type.IDENTIFIER && tk.payload.equals("assign")){
                gate tmp_gt;

                output = codename_lut.get(tokenList.get(i+1).payload);

                if(tokenList.get(i+2).type != token.token_type.EQUALS){} //this would be an ERROR
                input[0] = codename_lut.get(tokenList.get(i+3).payload);
                input[1] = codename_lut.get(tokenList.get(i+5).payload);

                switch(tokenList.get(i+4).type){
                    case AND:
                        tmp_gt = new gate(input,output, gate.gate_type.AND);
                        break;
                    case OR:
                        tmp_gt = new gate(input,output, gate.gate_type.OR);
                        break;
                    case XOR:
                        tmp_gt = new gate(input,output, gate.gate_type.XOR);
                        break;
                    case NOT:
                        tmp_gt = new gate(input,output, gate.gate_type.NOT);
                        break;
                    case IMPLIES:
                        tmp_gt = new gate(input,output, gate.gate_type.IMPLIES);
                        break;
                    default:
                        tmp_gt = new gate(input,output, gate.gate_type.UNKNW);
                        break;
                }

                tokenList.remove(i); //assign
                tokenList.remove(i); // a
                tokenList.remove(i); //EQUALS
                tokenList.remove(i); // b
                tokenList.remove(i); // LOGIC
                tokenList.remove(i); // c

                gateList.add(tmp_gt);
            }
        }

        debugPringNL(nets);
        debugPrintGL(gateList);
        //debugPrint(tokenList);

        tokenList   = null;
        token_iter  = null;

        wires       = null;
        wire_iter   = null;

        System.gc(); //Dunno if necessary

        return new ice(nets,gateList,codename_lut);
    }

    private void debugPringNL(List<net> netlist){
        Iterator<net> iter = netlist.iterator();
        while (iter.hasNext()) {
            net nt = iter.next();
            System.out.println(nt.makeReadable());
        }
        iter = null;
    }


    private void debugPrintGL(List<gate> gateList){
        Iterator<gate> iter = gateList.iterator();
        while (iter.hasNext()) {
            gate gt = iter.next();
            System.out.println(gt.makeReadable());
        }
        iter = null;
    }


    private void debugPrint(List<token> tokenList){
        Iterator<token> iter = tokenList.iterator();
        while (iter.hasNext()) {
            token tok = iter.next();
            System.out.println(tok.makeReadable());
        }
        iter = null;
    }
}
