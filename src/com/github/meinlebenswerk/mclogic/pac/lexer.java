package com.github.meinlebenswerk.mclogic.pac;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class lexer {

    List<token> tokenList = new ArrayList<>();
    String last_chars = "";

    public List<token> lex(File inputfile){
        //this takes an inputfile and generates a complete tokenlist that can be used to build an internal rep

        Scanner scanner;
        try { scanner = new Scanner(inputfile); } catch (FileNotFoundException e) { return null; }

        scanner.useDelimiter(""); //setup single-char mode

        /*
        ->> To implement for C-esque Syntax :)
            Identifier [a-zA-Z]\w*
            Integer literal [0-9]+
        */

        int block_depth = 0;

        //Here we dont do any Logic-Shit only stupidly parse the file into a token-list
        while(scanner.hasNext()){
            char c;
            c = scanner.next().charAt(0);

            switch(c){

                //Basic Programming Syntax
                case '{':
                    addIdentiferIfAny();
                    tokenList.add(new token(token.token_type.OPEN_BLOCK));
                    break;
                case '}':
                    addIdentiferIfAny();
                    tokenList.add(new token(token.token_type.CLOSE_BLOCK));
                    break;
                case '(':
                    addIdentiferIfAny();
                    tokenList.add(new token(token.token_type.OPEN_PARENTHESIS));
                    break;
                case ')':
                    addIdentiferIfAny();
                    tokenList.add(new token(token.token_type.CLOSE_PARENTHESIS));
                    break;
                case ';':
                    addIdentiferIfAny();
                    tokenList.add(new token(token.token_type.SEMICOLON));
                    break;
                case '\n':
                    addIdentiferIfAny();
                    tokenList.add(new token(token.token_type.NEWLINE));
                    break;
                case ' ':
                    addIdentiferIfAny();
                    break;
                    //Math+Logic Syntax
                case '-':
                    addIdentiferIfAny();
                    tokenList.add(new token(token.token_type.MINUS));
                    break;
                case '+':
                    addIdentiferIfAny();
                    tokenList.add(new token(token.token_type.PLUS));
                    break;
                case '=':
                    addIdentiferIfAny();
                    tokenList.add(new token(token.token_type.EQUALS));
                    break;
                case '*':
                    addIdentiferIfAny();
                    tokenList.add(new token(token.token_type.MULTIPLY));
                    break;
                case '/':
                    addIdentiferIfAny();
                    tokenList.add(new token(token.token_type.DIVIDE));
                    break;
                case '|':
                    addIdentiferIfAny();
                    tokenList.add(new token(token.token_type.OR));
                    break;
                case '^':
                    addIdentiferIfAny();
                    tokenList.add(new token(token.token_type.XOR));
                    break;
                case '&':
                    addIdentiferIfAny();
                    tokenList.add(new token(token.token_type.AND));
                    break;
                case '~':
                    addIdentiferIfAny();
                    tokenList.add(new token(token.token_type.NOT));
                    break;
                case '#':
                    addIdentiferIfAny();
                    tokenList.add(new token(token.token_type.IMPLIES));
                    break;


                    default:
                        //handling for identfiers
                        last_chars += c;
                        break;
            }
        }

        scanner.close();
        return tokenList;
    }

    private void addIdentiferIfAny(){
        if( !last_chars.isEmpty()) {
            tokenList.add(new token(token.token_type.IDENTIFIER, last_chars));
            last_chars = "";
        }
    }
}
