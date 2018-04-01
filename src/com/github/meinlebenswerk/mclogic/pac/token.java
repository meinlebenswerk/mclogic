package com.github.meinlebenswerk.mclogic.pac;

public class token {

    public enum token_type{
        SEMICOLON, OPEN_PARENTHESIS, CLOSE_PARENTHESIS, OPEN_BLOCK, CLOSE_BLOCK, IDENTIFIER, NEWLINE,
        MINUS, PLUS, MULTIPLY, DIVIDE, EQUALS,
        AND, OR, XOR, NOT, IMPLIES
    }

    String  payload;
    token_type type;

    public token(token_type type){
        this.type    = type;
        this.payload = "";
    }

    public token(token_type type, String payload){
        this.type    = type;
        this.payload = payload;
    }

    public String makeReadable(){
        return String.format("%s %s", type.name(), payload);
    }
}
