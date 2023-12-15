
package paq1;
import static paq1.Tokens.*;
import java.util.Map;
import java.util.LinkedHashMap;

class Token {
    public String lexema;
    public String token;
    public int nLinea;

    public Token(String lexema, String token, int nLinea) {
        this.lexema = lexema;
        this.token = token;
        this.nLinea = nLinea;
    }

    public String getLexema() {
        return lexema;
    }

    public void setLexema(String lexema) {
        this.lexema = lexema;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getnLinea() {
        return nLinea;
    }

    public void setnLinea(int nLinea) {
        this.nLinea = nLinea;
    }
    
    @Override
    public String toString() {
        return super.toString(); 
    }
    
    
}

%%

%class Lexer
%type Tokens
%line
%column
%{
   public String lexeme;
   InfoTokens t = new InfoTokens();
   Map<String,Token> tablaSimbolos = new LinkedHashMap<>();
%}

terminadorDeLinea = \r|\n|\r\n
entradaDeCaracter = [^\r\n]
espacioEnBlanco = {terminadorDeLinea} | [ \t\f]
comentarioTradicional   = "/*" [^*] ~"*/" | "/*" "*"+ "/"
finDeLineaComentario = "//" {entradaDeCaracter}* {terminadorDeLinea}?
contenidoComentario = ( [^*] | \*+ [^/*] )*
comentarioDeDocumentacion = "/**" {contenidoComentario} "*"+ "/"
comentario = {comentarioTradicional} | {finDeLineaComentario} | {comentarioDeDocumentacion}

letra = [a-zA-ZñÑ_$á-źÁ-Ź]
digito = [0-9]
espacio = [ ]+
caracter = \'(\\.|[^\'\\])?\'
carinc = \'(\\.|[^\'\\])?
flotante = (-?[1-9][0-9]*\.[0-9]*[1-9])|(0\.0)|(-?[1-9][0-9]*\.0)|(-?[1-9][0-9]*\.[0-9]*[1-9][eE][-+][1-9][0-9]*)|(-?0\.[0-9]*[1-9][eE][-+][1-9][0-9]*)
entero = (0|-?[1-9][0-9]*)
num = {entero} | {flotante}
tipo = entero|flotante|caracter|cadena|booleano

id = {letra}({letra}|{digito})*
%%

{comentario}|{espacioEnBlanco} { /* Ignorar */ }

int {t.numeroLinea=yyline; t.lexema=yytext(); lexeme=yytext(); Token t1 = new Token(yytext(),"int",yyline); return Entero;}
float {t.numeroLinea=yyline; t.lexema=yytext(); lexeme=yytext(); Token t1 = new Token(yytext(),"float",yyline); return Float;}
char {t.numeroLinea=yyline; t.lexema=yytext(); lexeme=yytext(); Token t1 = new Token(yytext(),"char",yyline); return Char;}

{id} {t.numeroLinea=yyline; t.lexema=yytext(); lexeme=yytext(); Token t1 = new Token(yytext(),"id",yyline); tablaSimbolos.put(yytext(),t1); return id;}

{num} {t.numeroLinea=yyline; t.lexema=yytext(); lexeme=yytext(); Token t1 = new Token(yytext(),"num",yyline); return num;}

"=" {t.numeroLinea=yyline; t.lexema=yytext(); lexeme=yytext(); Token t1 = new Token(yytext(),":=",yyline); return Igual;}

"+" {t.numeroLinea=yyline; t.lexema=yytext(); lexeme=yytext(); Token t1 = new Token(yytext(),"+",yyline); return Suma;}
"-" {t.numeroLinea=yyline; t.lexema=yytext(); lexeme=yytext(); Token t1 = new Token(yytext(),"-",yyline); return Resta;}
"/" {t.numeroLinea=yyline; t.lexema=yytext(); lexeme=yytext(); Token t1 = new Token(yytext(),"/",yyline); return Division;}
"*" {t.numeroLinea=yyline; t.lexema=yytext(); lexeme=yytext(); Token t1 = new Token(yytext(),"*",yyline); return Producto;}

"(" {t.numeroLinea=yyline; t.lexema=yytext(); lexeme=yytext(); Token t1 = new Token(yytext(),"(",yyline); return AbreParentesis;}
")" {t.numeroLinea=yyline; t.lexema=yytext(); lexeme=yytext(); Token t1 = new Token(yytext(),")",yyline); return CierraParentesis;}

"," {t.numeroLinea=yyline; t.lexema=yytext(); lexeme=yytext(); Token t1 = new Token(yytext(),",",yyline); return Coma;}
";" {t.numeroLinea=yyline; t.lexema=yytext(); lexeme=yytext(); Token t1 = new Token(yytext(),";",yyline); return PuntoComa;}

 . {t.numeroLinea=yyline; t.lexema=yytext(); lexeme=yytext(); return ERROR;}
   {carinc} {t.numeroLinea=yyline; t.lexema=yytext(); lexeme=yytext(); return ERROR;} 
   {caracter} {t.numeroLinea=yyline; t.lexema=yytext(); lexeme=yytext(); return ERROR;} 
