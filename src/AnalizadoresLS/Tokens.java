
package AnalizadoresLS;

public enum Tokens {
    id,
    num,
    Entero("int"),
    Float("float"),
    Char("char"),
    Coma(","),
    PuntoComa(";"),
    Suma("+"),
    Resta("-"),
    Producto("*"),
    Division("/"),
    AbreParentesis("("),
    CierraParentesis(")"),
    Igual("="),
    ERROR;
    
    private final String simbolo;

    private Tokens() {
        this.simbolo = null;
    }

    private Tokens(String simbolo) {
        this.simbolo = simbolo;
    }

    public String getSimbolo() {
        return simbolo;
    }
}
