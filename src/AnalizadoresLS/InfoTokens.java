
package AnalizadoresLS;

public class InfoTokens {
    public static int numeroLinea;
    public static String tok;
    public static String lexema;
    
    public static void Guardar(int linea, String token){
        numeroLinea=linea;
        tok=token;
    }
}
