
package AnalizadoresLS;

import java.awt.Image;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.undo.UndoManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Stack;

public class Principal extends javax.swing.JFrame {
    NumeroLinea numerolinea2;
    HerramientaArchivo archivo;
    UndoManager manager;
    Lexer lexer;
    public Stack<String> pila = new Stack();
    ArrayList<String> Columnas = new ArrayList<>(Arrays.asList("id", "num", "int", "float", "char", ",", ";", "+", "-", "*", "/", "=", "(", ")", "$", "P", "Tipo", "V", "A", "Exp", "E", "Term", "T", "F"));
    ArrayList<Integer> Reducciones = new ArrayList<>(Arrays.asList(2, 6, 2,2,2,2,6,4,8,6,6,4,6,6,0,4,6,6,0,2,2,6));
    ArrayList<String> NTProduccion = new ArrayList<>(Arrays.asList("P´", "P", "P", "Tipo", "Tipo", "Tipo", "V", "V", "A", "Exp", "Exp", "Exp", "E", "E", "E", "Term", "T", "T", "T", "F", "F", "F"));
    Map<String, Integer> tablaSimbolos = new LinkedHashMap<>();
    ArrayList<Integer> tipoDato = new ArrayList<>(Arrays.asList(0, 1, 2)); // 0 --> Entero | 1 --> Flotante | 2 --> Char
    public String componente; 
    Stack<String> pilaOperadores = new Stack();
    Stack<String> pilaSemantica = new Stack();
    String expPosfija = "", intermedio, vAsig;
    public String[][] tablaTipos = 
    {
        {"0", "1", "-1"},
        {"1", "1", "-1"},
        {"-1","-1", "2"}
    };
    public boolean[][] tablaAsigTipo = 
    {
        {true, false, false},
        {true, true, false},
        {false,false, true}
    };
    public String[][] Tabla = 
    {
        {"I7",	"",	"I4",	"I5",	"I6",	"",	"",	"",	"",	"",	"",	"",	"",	"",	"",	"I1",	"I2",	"",	"I3",	"",	"",	"",	"",	""},
        {"",	"",	"",	"",	"",	"",	"",	"",	"",	"",	"",	"",	"",	"",	"P0",	"",	"",	"",	"",	"",	"",	"",	"",	""},
        {"I8",	"",	"",	"",	"",	"",	"",	"",	"",	"",	"",	"",	"",	"",	"",	"",	"",	"",	"",	"",	"",	"",	"",	""},
        {"",	"",	"",	"",	"",	"",	"",	"",	"",	"",	"",	"",	"",	"",	"P2",	"",	"",	"",	"",	"",	"",	"",	"",	""},
        {"P3",	"",	"",	"",	"",	"",	"",	"",	"",	"",	"",	"",	"",	"",	"",	"",	"",	"",	"",	"",	"",	"",	"",	""},
        {"P4",	"",	"",	"",	"",	"",	"",	"",	"",	"",	"",	"",	"",	"",	"",	"",	"",	"",	"",	"",	"",	"",	"",	""},
        {"P5",	"",	"",	"",	"",	"",	"",	"",	"",	"",	"",	"",	"",	"",	"",	"",	"",	"",	"",	"",	"",	"",	"",	""},
        {"",	"",	"",	"",	"",	"",	"",	"",	"",	"",	"",	"I9",	"",	"",	"",	"",	"",	"",	"",	"",	"",	"",	"",	""},
        {"",	"",	"",	"",	"",	"I11",	"I12",	"",	"",	"",	"",	"",	"",	"",	"",	"",	"",	"I10",	"",	"",	"",	"",	"",	""},
        {"I18",	"I19",	"",	"",	"",	"",	"",	"I14",	"I15",	"",	"",	"",	"I20",	"",	"",	"",	"",	"",	"",	"I13",	"",	"I16",	"",	"I17"},
        {"",	"",	"",	"",	"",	"",	"",	"",	"",	"",	"",	"",	"",	"",	"P1",	"",	"",	"",	"",	"",	"",	"",	"",	""},
        {"I21",	"",	"",	"",	"",	"",	"",	"",	"",	"",	"",	"",	"",	"",	"",	"",	"",	"",	"",	"",	"",	"",	"",	""},
        {"I7",	"",	"I4",	"I5",	"I6",	"",	"",	"",	"",	"",	"",	"",	"",	"",	"",	"I22",	"I2",	"",	"I3",	"",	"",	"",	"",	""},
        {"",	"",	"",	"",	"",	"",	"I23",	"",	"",	"",	"",	"",	"",	"",	"",	"",	"",	"",	"",	"",	"",	"",	"",	""},
        {"I18",	"I19",	"",	"",	"",	"",	"",	"",	"",	"",	"",	"",	"I20",	"",	"",	"",	"",	"",	"",	"",	"",	"I24",	"",	"I17"},
        {"I18",	"I19",	"",	"",	"",	"",	"",	"",	"",	"",	"",	"",	"I20",	"",	"",	"",	"",	"",	"",	"",	"",	"I25",	"",	"I17"},
        {"",	"",	"",	"",	"",	"",	"P14",	"I27",	"I28",	"",	"",	"",	"",	"P14",	"",	"",	"",	"",	"",	"",	"I26",	"",	"",	""},
        {"",	"",	"",	"",	"",	"",	"P18",	"P18",	"P18",	"I30",	"I31",	"",	"",	"P18",	"",	"",	"",	"",	"",	"",	"",	"",	"I29",	""},
        {"",	"",	"",	"",	"",	"",	"P19",	"P19",	"P19",	"P19",	"P19",	"",	"",	"P19",	"",	"",	"",	"",	"",	"",	"",	"",	"",	""},
        {"",	"",	"",	"",	"",	"",	"P20",	"P20",	"P20",	"P20",	"P20",	"",	"",	"P20",	"",	"",	"",	"",	"",	"",	"",	"",	"",	""},
        {"I18",	"I19",	"",	"",	"",	"",	"",	"I14",	"I15",	"",	"",	"",	"I20",	"",	"",	"",	"",	"",	"",	"I32",	"",	"I16",	"",	"I17"},
        {"",	"",	"",	"",	"",	"I11",	"I12",	"",	"",	"",	"",	"",	"",	"",	"",	"",	"",	"I33",	"",	"",	"",	"",	"",	""},
        {"",	"",	"",	"",	"",	"",	"",	"",	"",	"",	"",	"",	"",	"",	"P7",	"",	"",	"",	"",	"",	"",	"",	"",	""},
        {"",	"",	"",	"",	"",	"",	"",	"",	"",	"",	"",	"",	"",	"",	"P8",	"",	"",	"",	"",	"",	"",	"",	"",	""},
        {"",	"",	"",	"",	"",	"",	"P14",	"I27",	"I28",	"",	"",	"",	"",	"P14",	"",	"",	"",	"",	"",	"",	"I34",	"",	"",	""},
        {"",	"",	"",	"",	"",	"",	"P14",	"I27",	"I28",	"",	"",	"",	"",	"P14",	"",	"",	"",	"",	"",	"",	"I35",	"",	"",	""},
        {"",	"",	"",	"",	"",	"",	"P11",	"",	"",	"",	"",	"",	"",	"P11",	"",	"",	"",	"",	"",	"",	"",	"",	"",	""},
        {"I18",	"I19",	"",	"",	"",	"",	"",	"",	"",	"",	"",	"",	"I20",	"",	"",	"",	"",	"",	"",	"",	"",	"I36",	"",	"I17"},
        {"I18",	"I19",	"",	"",	"",	"",	"",	"",	"",	"",	"",	"",	"I20",	"",	"",	"",	"",	"",	"",	"",	"",	"I37",	"",	"I17"},
        {"",	"",	"",	"",	"",	"",	"P15",	"P15",	"P15",	"",	"",	"",	"",	"P15",	"",	"",	"",	"",	"",	"",	"",	"",	"",	""},
        {"I18",	"I19",	"",	"",	"",	"",	"",	"",	"",	"",	"",	"",	"I20",	"",	"",	"",	"",	"",	"",	"",	"",	"",	"",	"I38"},
        {"I18",	"I19",	"",	"",	"",	"",	"",	"",	"",	"",	"",	"",	"I20",	"",	"",	"",	"",	"",	"",	"",	"",	"",	"",	"I39"},
        {"",	"",	"",	"",	"",	"",	"",	"",	"",	"",	"",	"",	"",	"I40",	"",	"",	"",	"",	"",	"",	"",	"",	"",	""},
        {"",	"",	"",	"",	"",	"",	"",	"",	"",	"",	"",	"",	"",	"",	"P6",	"",	"",	"",	"",	"",	"",	"",	"",	""},
        {"",	"",	"",	"",	"",	"",	"P9",	"",	"",	"",	"",	"",	"",	"P9",	"",	"",	"",	"",	"",	"",	"",	"",	"",	""},
        {"",	"",	"",	"",	"",	"",	"P10",	"",	"",	"",	"",	"",	"",	"P10",	"",	"",	"",	"",	"",	"",	"",	"",	"",	""},
        {"",	"",	"",	"",	"",	"",	"P14",	"I27",	"I28",	"",	"",	"",	"",	"P14",	"",	"",	"",	"",	"",	"",	"I41",	"",	"",	""},
        {"",	"",	"",	"",	"",	"",	"P14",	"I27",	"I28",	"",	"",	"",	"",	"P14",	"",	"",	"",	"",	"",	"",	"I42",	"",	"",	""},
        {"",	"",	"",	"",	"",	"",	"P18",	"P18",	"P18",	"I30",	"I31",	"",	"",	"P18",	"",	"",	"",	"",	"",	"",	"",	"",	"I43",	""},
        {"",	"",	"",	"",	"",	"",	"P18",	"P18",	"P18",	"I30",	"I31",	"",	"",	"P18",	"",	"",	"",	"",	"",	"",	"",	"",	"I44",	""},
        {"",	"",	"",	"",	"",	"",	"P21",	"P21",	"P21",	"P21",	"P21",	"",	"",	"P21",	"",	"",	"",	"",	"",	"",	"",	"",	"",	""},
        {"",	"",	"",	"",	"",	"",	"P12",	"",	"",	"",	"",	"",	"",	"P12",	"",	"",	"",	"",	"",	"",	"",	"",	"",	""},
        {"",	"",	"",	"",	"",	"",	"P13",	"",	"",	"",	"",	"",	"",	"P13",	"",	"",	"",	"",	"",	"",	"",	"",	"",	""},
        {"",	"",	"",	"",	"",	"",	"P16",	"P16",	"P16",	"",	"",	"",	"",	"P16",	"",	"",	"",	"",	"",	"",	"",	"",	"",	""},
        {"",	"",	"",	"",	"",	"",	"P17",	"P17",	"P17",	"",	"",	"",	"",	"P17",	"",	"",	"",	"",	"",	"",	"",	"",	"",	""}
    };
    String res, err;
    boolean ban;
    int tipo = -1;
    int tipoAsig = -1;

    public Principal() {
        this.manager = new UndoManager();
        initComponents();
        inicializar();
        codigoFuente.getDocument().addUndoableEditListener(manager);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private void AnalisisLexico() {
        InfoTokens infoToken;
        try {
            File codigo = new File("archivo.eth");
            FileOutputStream output = new FileOutputStream(codigo);
            byte[] bytes = codigoFuente.getText().getBytes();
            output.write(bytes);
            BufferedReader entrada = new BufferedReader(new InputStreamReader(new FileInputStream(codigo), "UTF-8"));
            lexer = new Lexer(entrada);
            infoToken = new InfoTokens();
            String resu = "";
            while (true) {
                Tokens token = lexer.yylex();
                if (token == null) {
                    AnalisisSintactico("$", "", (infoToken.numeroLinea + 1) + "");
                    resu += "";
                    lexico.setText(resu);
                    return;
                }
                switch (token) {
                    case ERROR:
                        err += "Error lexico en la linea " + (infoToken.numeroLinea + 1) + " simbolo: " + lexer.lexeme + " incorrecto" + "\n";
                        errores.setText(err);
                        res+="La cadena no se acepta...";
                        sintactico.setText(res);
                        return;
                    default:
                        if (token.getSimbolo() == null) {
                            resu += token + "\n";
                            ban=AnalisisSintactico(token + "", infoToken.lexema, (infoToken.numeroLinea + 1) + "");
                            if(ban==false)
                                return;
                            lexico.setText(resu);
                        } else {
                            resu += token.getSimbolo() + "\n";
                            ban=AnalisisSintactico(token.getSimbolo(), infoToken.lexema, (infoToken.numeroLinea + 1) + "");
                            if(ban==false)
                                return;
                            lexico.setText(resu);
                        }
                        break;
                }
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private boolean AnalisisSintactico(String comp, String lexema, String nlinea) 
    {
        String cimaPila, accion;
        ban = false;
        boolean ban2 = true;
        while(ban == false)
        {
            int ren, col = 0;
            //System.out.println("\nComponente: "+comp);
            cimaPila = pila.peek();
            //System.out.println("Cima de la pila: "+cimaPila);
            ren = Integer.parseInt(cimaPila.substring(1));
            for(String columna : Columnas)
            {
                if(comp.equals(columna))
                    break;
                else
                    col++;
            }
            //System.out.println("Renglon: "+ren + " Columna: " + col);
            accion = Tabla[ren][col];
            if(accion.equals(""))
            {
                Error(ren, lexema, nlinea);
                return false;
            }
            if(accion.equals("P0"))
            {
                res+="La cadena se acepta...";
                sintactico.setText(res);
                return false;
            }
            switch(accion.substring(0, 1))
            {
                case "I":
                    ban2 = Desplazar(comp, accion, lexema, nlinea);
                    if(ban2 == false)
                        return false;
                    ban = true;
                    break;
                case "P":
                    ban=Reduccion(accion);
                    break;
            }
        }
        return true;
    }
    
    private boolean Desplazar(String comp, String accion, String lexema, String nlinea)
    {
        String estado = accion.substring(1);
        pila.push(comp);
        pila.push(accion);
        //System.out.println(estado);
        switch(estado)
        {
            case "4":
                //Entero
                tipo = 0;
                //System.out.println("Entrando a entero con estado " + estado + " y tipo " + tipo);
                break;
            case "5":
                //Flotante
                tipo = 1;
                //System.out.println("Entrando a flotante con estado " + estado + " y tipo " + tipo);
                break;
            case "6":
                //Char
                tipo = 2;
                //System.out.println("Entrando a caracter con estado " + estado + " y tipo " + tipo);
                break;
            case "7":
                // Asignación
                if(tablaSimbolos.get(lexema) != null)
                {
                    tipoAsig = tablaSimbolos.get(lexema);
                    vAsig = lexema;
                }
                else
                {
                    err += "Error semantico en linea " + nlinea + " el identificador " + lexema + " no existe" + "\n";
                    errores.setText(err);
                    res+="La cadena no se acepta...";
                    sintactico.setText(res);
                    return false;
                }
                break;
            case "8":
                //Registrar ID a la tabla de simbolos
                //System.out.println("Entrando al case 8 con tipo " + tipo + " y lexema " + lexema);
                if(tablaSimbolos.get(lexema) == null && tipo != -1)
                {
                    //System.out.println("Se guarda " + lexema + " con el tipo: "+ tipo);
                    tablaSimbolos.put(lexema, tipo);
                    CITipo(tipo, lexema);
                }
                else
                {
                     err += "Error semantico en linea " + nlinea + " el identificador " + lexema + " ya existe" + "\n";
                     errores.setText(err);
                     res+="La cadena no se acepta...";
                     sintactico.setText(res);
                     return false;
                }
                break; 
            case "14":
                // + Inserta en pila de Operadores
                ban = Operadores(comp, lexema, nlinea);
                if(ban == false)
                    return false;
                break;
            case "15":
                // - Inserta en pila de Operadores
                ban = Operadores(comp, lexema, nlinea);
                if(ban == false)
                    return false;
                break;
            case "27":
                // + Inserta en pila de Operadores
                ban = Operadores(comp, lexema, nlinea);
                if(ban == false)
                    return false;
                break;
            case "28":
                // - Inserta en pila de Operadores
                ban = Operadores(comp, lexema, nlinea);
                if(ban == false)
                    return false;
                break;
            case "18":
                // Inserta el tipo de dato en la pila de semantica y lo pasa a la expresión
                if(tablaSimbolos.get(lexema) != null)
                {
                    pilaSemantica.push(tablaSimbolos.get(lexema)+"");
                    expPosfija += lexema + " ";
                }
                else
                {
                    err += "Error semantico en linea " + nlinea + " el identificador " + lexema + " no existe" + "\n";
                    errores.setText(err);
                    res+="La cadena no se acepta...";
                    sintactico.setText(res);
                    return false;
                }
                break;
            case "19":
                // Inserta el tipo de dato del num a la pila semantica y lo pasa a la expresion
                if(lexema.matches("(0|-?[1-9][0-9]*)"))
                {
                    pilaSemantica.push("0");
                    expPosfija += lexema + " ";
                }
                else if(lexema.matches("(-?[1-9][0-9]*\\.[0-9]*[1-9])|(0\\.0)|(-?[1-9][0-9]*\\.0)|(-?[1-9][0-9]*\\.[0-9]*[1-9][eE][-+][1-9][0-9]*)|(-?0\\.[0-9]*[1-9][eE][-+][1-9][0-9]*)"))
                {
                    pilaSemantica.push("1");
                    expPosfija += lexema + " ";
                }
                break;
            case "30":
                // * Inserta en pila de Operadores
                ban = Operadores(comp, lexema, nlinea);
                if(ban == false)
                    return false;
                break;
            case "31":
                // / Inserta en pila de Operadores
                ban = Operadores(comp, lexema, nlinea);
                if(ban == false)
                    return false;
                break;
            case "20":
                // Inserta ( en la pila de Operadores
                pilaOperadores.push("(");
                break;
            case "40":
                // ) Saca todo de la pila de operadores hasta encontrar (
                while(!pilaOperadores.isEmpty())
                {
                    if(!pilaOperadores.peek().equals("("))
                    {
                         // Mandar llamar función semantica para hacer 2 pop en la pila semantica y determinar el tipo de dato resultante
                        if(pilaOperadores.peek().equals("+")||pilaOperadores.peek().equals("-")||pilaOperadores.peek().equals("*")||pilaOperadores.peek().equals("/"))
                        {
                            expPosfija += pilaOperadores.peek() + " ";
                            int i=0;
                            for(String ps : pilaSemantica)
                                i++;
                            if(i>=2)
                            {
                                String exp = SemanticoExp(comp, lexema, nlinea);
                                if(exp.equals("-1"))
                                {
                                    err += "Error semantico en linea " + nlinea + " error de tipo \n";
                                    errores.setText(err);
                                    res+="La cadena no se acepta...";
                                    sintactico.setText(res);
                                    return false;
                                }
                                else
                                {
                                    pilaSemantica.push(exp);
                                }
                            }
                            else
                            {
                                err += "Error semantico en linea " + nlinea + " faltan operandos \n";
                                errores.setText(err);
                                res+="La cadena no se acepta...";
                                sintactico.setText(res);
                            }
                            pilaOperadores.pop();
                        }
                    }
                    else
                    {
                        pilaOperadores.pop();
                        break;
                    }
                    if(pilaOperadores.isEmpty())
                    {
                        err += "Error semantico en linea " + nlinea + " el parentesis que abre " + "(" + " no se encuentra" + "\n";
                        errores.setText(err);
                        res+="La cadena no se acepta...";
                        sintactico.setText(res);
                        return false;
                    }
                }
                break;
            case "21":
                //Registrar ID's despues de una coma
                //System.out.println("Entrando al case 21 con tipo " + tipo + " y lexema " + lexema);
                if(tablaSimbolos.get(lexema) == null && tipo != -1)
                {
                    //System.out.println("Se guarda " + lexema + " con el tipo: "+ tipo);
                    tablaSimbolos.put(lexema, tipo);
                    CITipo(tipo, lexema);
                }
                else
                {
                     err += "Error semantico en linea " + nlinea + " el identificador " + lexema + " ya existe" + "\n";
                     errores.setText(err);
                     return false;
                }
                break; 
            case "12":
                //Desactivar declaración y Sacaba todo de la pila de operadores
                tipo = -1;
                while(!pilaOperadores.isEmpty())
                {
                   // Mandar llamar función semantica para hacer 2 pop en la pila semantica y determinar el tipo de dato resultante
                   if(pilaOperadores.peek().equals("+")||pilaOperadores.peek().equals("-")||pilaOperadores.peek().equals("*")||pilaOperadores.peek().equals("/"))
                   {
                       expPosfija += pilaOperadores.peek() + " ";
                   //
                        int i=0;
                        for(String ps : pilaSemantica)
                            i++;
                        if(i>=2)
                        {
                            String exp = SemanticoExp(comp, lexema, nlinea);
                            if(exp.equals("-1"))
                            {
                                err += "Error semantico en linea " + nlinea + " error de tipo \n";
                                errores.setText(err);
                                res+="La cadena no se acepta...";
                                sintactico.setText(res);
                                return false;
                            }
                            else
                            {
                                pilaSemantica.push(exp);
                            }
                        }
                        else
                        {
                            err += "Error semantico en linea " + nlinea + " faltan operandos \n";
                            errores.setText(err);
                            res+="La cadena no se acepta...";
                            sintactico.setText(res);
                        }
                        //
                        pilaOperadores.pop();
                }
                }
                break;
            case "23":
                //Desactivar declaración y Sacaba todo de la pila de operadores
                tipo = -1;
                while(!pilaOperadores.isEmpty())
                {
                   // Mandar llamar función semantica para hacer 2 pop en la pila semantica y determinar el tipo de dato resultante
                   if(pilaOperadores.peek().equals("+")||pilaOperadores.peek().equals("-")||pilaOperadores.peek().equals("*")||pilaOperadores.peek().equals("/"))
                   {
                        expPosfija += pilaOperadores.peek() + " ";
                        //
                        int i=0;
                        for(String ps : pilaSemantica)
                            i++;
                        if(i>=2)
                        {
                            String exp = SemanticoExp(comp, lexema, nlinea);
                            if(exp.equals("-1"))
                            {
                                err += "Error semantico en linea " + nlinea + " error de tipo \n";
                                errores.setText(err);
                                res+="La cadena no se acepta...";
                                sintactico.setText(res);
                                return false;
                            }
                            else
                            {
                                pilaSemantica.push(exp);
                            }
                        }
                        else
                        {
                            err += "Error semantico en linea " + nlinea + " faltan operandos \n";
                            errores.setText(err);
                            res+="La cadena no se acepta...";
                            sintactico.setText(res);
                        }
                        //
                        pilaOperadores.pop();
                   }
                }
                if(!tablaAsigTipo[tipoAsig][Integer.parseInt(pilaSemantica.peek())])
                {
                    err += "Error semantico en linea " + nlinea + " error de tipo en la asignación \n";
                    errores.setText(err);
                    res+="La cadena no se acepta...";
                    sintactico.setText(res);
                    return false;
                }
                CodInt(expPosfija, vAsig);
        }
        System.out.println("Desplazamiento.- " + "Terminal: " + comp + " Estado: " + pila.peek());
        return true;
    }
    
    private String SemanticoExp(String comp, String lexema, String nlinea)
    {
        String n2 = pilaSemantica.pop();
        String n1 = pilaSemantica.pop();
        return tablaTipos[Integer.parseInt(n2)][Integer.parseInt(n1)]; 
    }
    
    private boolean Operadores(String comp, String lexema, String nlinea)
    {
        if(comp.equals("+") | comp.equals("-"))
        {
            if(!pilaOperadores.isEmpty())
            {
                while(true)
                    if(pilaOperadores.peek().equals("+")|pilaOperadores.peek().equals("-")|pilaOperadores.peek().equals("*")|pilaOperadores.peek().equals("/"))
                    {
                        // Mandar llamar función semantica para hacer 2 pop en la pila semantica y determinar el tipo de dato resultante
                        expPosfija += pilaOperadores.peek() + " ";
                    //
                            int i=0;
                            for(String ps : pilaSemantica)
                                i++;
                            if(i>=2)
                            {
                                String exp = SemanticoExp(comp, lexema, nlinea);
                                if(exp.equals("-1"))
                                {
                                    err += "Error semantico en linea " + nlinea + " error de tipo \n";
                                    errores.setText(err);
                                    res+="La cadena no se acepta...";
                                    sintactico.setText(res);
                                    return false;
                                }
                                else
                                {
                                    pilaSemantica.push(exp);
                                }
                            }
                            else
                            {
                                err += "Error semantico en linea " + nlinea + " faltan operandos \n";
                                errores.setText(err);
                                res+="La cadena no se acepta...";
                                sintactico.setText(res);
                            }
                            //
                        pilaOperadores.pop();
                        if(pilaOperadores.isEmpty())
                            break;
                    }
                    else
                    {
                        pilaOperadores.push(comp);
                        break;
                    }
                if(pilaOperadores.isEmpty())
                    pilaOperadores.push(comp);
            }
            else
            {
                pilaOperadores.push(comp);
            }
        }
        if(comp.equals("*") | comp.equals("/"))
            {
                if(!pilaOperadores.isEmpty())
                {
                    while(true)
                        if(pilaOperadores.peek().equals("*")|pilaOperadores.peek().equals("/"))
                        {
                            // Mandar llamar función semantica para hacer 2 pop en la pila semantica y determinar el tipo de dato resultante
                            expPosfija += pilaOperadores.peek() + " ";
                            //
                            int i=0;
                            for(String ps : pilaSemantica)
                                i++;
                            if(i>=2)
                            {
                                String exp = SemanticoExp(comp, lexema, nlinea);
                                if(exp.equals("-1"))
                                {
                                    err += "Error semantico en linea " + nlinea + " error de tipo \n";
                                    errores.setText(err);
                                    res+="La cadena no se acepta...";
                                    sintactico.setText(res);
                                    return false;
                                }
                                else
                                {
                                    pilaSemantica.push(exp);
                                }
                            }
                            else
                            {
                                err += "Error semantico en linea " + nlinea + " faltan operandos \n";
                                errores.setText(err);
                                res+="La cadena no se acepta...";
                                sintactico.setText(res);
                            }
                            //
                            pilaOperadores.pop();
                            if(pilaOperadores.isEmpty())
                                break;
                        }
                        else
                        {
                            pilaOperadores.push(comp);
                            break;
                        }
                    if(pilaOperadores.isEmpty())
                    pilaOperadores.push(comp);
                }
                else
                {
                    pilaOperadores.push(comp);
                }
            }
        return true;
    }
    
    private boolean Reduccion(String accion)
    {
        int redu, ren, col = 0;
        String estadoant, nt, estadoact;
        redu = Integer.parseInt(accion.substring(1));
        if(redu != 0)
        {
            for(int i=0; i<Reducciones.get(redu); i++)
                pila.pop();
        }
        nt = NTProduccion.get(redu);
        estadoant = pila.peek();
        pila.push(nt);
        ren = Integer.parseInt(estadoant.substring(1));
        for(String columna : Columnas)
        {
            if(nt.equals(columna))
                break;
            else
                col++;
        }
        estadoact = Tabla[ren][col];
        pila.push(estadoact);
        System.out.println("Reduccion.- " + "NT: " + nt + " Cima de la Pila: " + pila.peek() + " Produccion: " + redu +" Estado Ant: " + estadoant);
        return false;
    }
    
    private void Error(int estado, String lexema, String nlinea)
    {
        if(!lexema.equals(""))
            err+="Error sintactico en linea "+nlinea+", no se esperaba "+lexema+" se esperaba: ";
        else
            err+="Error sintactico en linea "+nlinea+", se esperaba: ";
        for(int i=0; i<14; i++)
            if(!Tabla[estado][i].equals(""))
                err+=Columnas.get(i)+" ";
        res+="La cadena no se acepta...";
        sintactico.setText(res);
        errores.setText(err);
    }
    
    private void CITipo(int tipo, String lexema)
    {
        switch(tipo)
        {
            case 0:
                intermedio += "int " + lexema + ";\n";
                break;
            case 1:
                intermedio += "float " + lexema + ";\n";
                break;
            case 2:
                intermedio += "char " + lexema + ";\n";
        }
    }
    
    private void CodInt(String exp, String asig)
    {
        String pos[] = exp.split(" ");
        int con = 1;
        for(int i = 0; i<pos.length; i++)
        {
            if(!pos[i].equals("+")&&!pos[i].equals("-")&&!pos[i].equals("*")&&!pos[i].equals("/"))
            {
                intermedio += "V" + con + "=" + pos[i] + "\n";
                con++;
            }
            else
            {
                con-=2;
                intermedio += "V" + con + "=" + "V" + con + pos[i] + "V" + (con+1) + "\n";
                con++;
            }
        }
        intermedio += asig + "=" + "V" + (con-1);
    }

    private void InicializarPilas() {
        pila.clear();
        pila.push("$");
        pila.push("I0");
        pilaOperadores.clear();
        pilaSemantica.clear();
        expPosfija = "";
        intermedio = "";
    }

    private void inicializar() {
        archivo = new HerramientaArchivo();
        setTitle("ETHIDE");
        numerolinea2 = new NumeroLinea(codigoFuente);
        jScrollPane2.setRowHeaderView(numerolinea2);
    }

    private void Cerrar() {
        String opciones[] = {"Cerrar", "Cancelar"};
        int eleccion = JOptionPane.showOptionDialog(this, "¿Estas seguro de que quieres cerrar el programa? Todo cambio sin guardar se perdera", "Cierre de programa", 0, 0, null, opciones, EXIT_ON_CLOSE);
        if (eleccion == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane2 = new javax.swing.JScrollPane();
        codigoFuente = new javax.swing.JTextArea();
        jScrollPane3 = new javax.swing.JScrollPane();
        lexico = new javax.swing.JTextArea();
        jToolBar1 = new javax.swing.JToolBar();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        sintactico = new javax.swing.JTextArea();
        jScrollPane1 = new javax.swing.JScrollPane();
        errores = new javax.swing.JTextArea();
        jScrollPane5 = new javax.swing.JScrollPane();
        interm = new javax.swing.JTextArea();
        jLabel1 = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenuItem5 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem6 = new javax.swing.JMenuItem();
        jMenuItem7 = new javax.swing.JMenuItem();
        jMenuItem8 = new javax.swing.JMenuItem();
        jMenuItem9 = new javax.swing.JMenuItem();
        jMenuItem10 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setIconImage(getIconImage());
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        codigoFuente.setColumns(20);
        codigoFuente.setRows(5);
        codigoFuente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                codigoFuenteKeyReleased(evt);
            }
        });
        jScrollPane2.setViewportView(codigoFuente);

        getContentPane().add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 53, 515, 280));

        lexico.setColumns(20);
        lexico.setRows(5);
        jScrollPane3.setViewportView(lexico);

        getContentPane().add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 60, 506, 179));

        jToolBar1.setBackground(new java.awt.Color(255, 255, 255));
        jToolBar1.setRollover(true);

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8-por-nueva-copia-24.png"))); // NOI18N
        jButton1.setFocusable(false);
        jButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton1);

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8-abrir-carpeta-24.png"))); // NOI18N
        jButton2.setFocusable(false);
        jButton2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton2.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton2);

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8-guardar-24.png"))); // NOI18N
        jButton3.setFocusable(false);
        jButton3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton3.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton3);

        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8-deshacer-24.png"))); // NOI18N
        jButton4.setFocusable(false);
        jButton4.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton4.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton4);

        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8-rehacer-24.png"))); // NOI18N
        jButton5.setFocusable(false);
        jButton5.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton5.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton5);

        jButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8-play-24.png"))); // NOI18N
        jButton6.setFocusable(false);
        jButton6.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton6.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton6);

        getContentPane().add(jToolBar1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1100, 35));

        sintactico.setColumns(20);
        sintactico.setRows(5);
        jScrollPane4.setViewportView(sintactico);

        getContentPane().add(jScrollPane4, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 350, 480, 90));

        errores.setColumns(20);
        errores.setRows(5);
        jScrollPane1.setViewportView(errores);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 450, 480, -1));

        interm.setColumns(20);
        interm.setRows(5);
        jScrollPane5.setViewportView(interm);

        getContentPane().add(jScrollPane5, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 270, 510, 260));
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 540, 1090, 20));

        jMenu1.setText("Archivo");

        jMenuItem1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8-por-nueva-copia-24.png"))); // NOI18N
        jMenuItem1.setText("Nuevo");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuItem2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8-abrir-carpeta-24.png"))); // NOI18N
        jMenuItem2.setText("Abrir");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem2);

        jMenuItem3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8-guardar-24.png"))); // NOI18N
        jMenuItem3.setText("Guardar");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem3);

        jMenuItem4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8-guardar-como-24.png"))); // NOI18N
        jMenuItem4.setText("Guardar como...");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem4);

        jMenuItem5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8-cerrar-ventana-24.png"))); // NOI18N
        jMenuItem5.setText("Cerrar");
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem5ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem5);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Editar");

        jMenuItem6.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Z, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        jMenuItem6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8-deshacer-24.png"))); // NOI18N
        jMenuItem6.setText("Deshacer");
        jMenuItem6.setActionCommand("");
        jMenuItem6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem6ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem6);

        jMenuItem7.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Y, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        jMenuItem7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8-rehacer-24.png"))); // NOI18N
        jMenuItem7.setText("Rehacer");
        jMenuItem7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem7ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem7);

        jMenuItem8.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_X, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        jMenuItem8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8-tijeras-24.png"))); // NOI18N
        jMenuItem8.setText("Cortar");
        jMenuItem8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem8ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem8);

        jMenuItem9.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        jMenuItem9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8-copiar-24.png"))); // NOI18N
        jMenuItem9.setText("Copiar");
        jMenuItem9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem9ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem9);

        jMenuItem10.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_V, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        jMenuItem10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8-pegar-24.png"))); // NOI18N
        jMenuItem10.setText("Pegar");
        jMenuItem10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem10ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem10);

        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    @Override
    public Image getIconImage() {
        Image retValue = Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("icons/IconoIDE.png"));
        return retValue;
    }

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        lexico.setText("");
        archivo.Nuevo(this);
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem5ActionPerformed

    private void jMenuItem6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem6ActionPerformed
        if (manager.canUndo()) {
            manager.undo();
        }
    }//GEN-LAST:event_jMenuItem6ActionPerformed

    private void jMenuItem9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem9ActionPerformed
        codigoFuente.copy();
    }//GEN-LAST:event_jMenuItem9ActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        Cerrar();
    }//GEN-LAST:event_formWindowClosing

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        archivo.Guardar(this);
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        lexico.setText("");
        archivo.Abrir(this);
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        lexico.setText("");
        archivo.guardarC(this);
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    private void codigoFuenteKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_codigoFuenteKeyReleased
        int key = evt.getKeyCode();
        if ((key >= 65 && key <= 90) || (key >= 48 && key <= 57) || (key >= 97 && key <= 122) || (key != 27 && (key >= 37
                && key <= 40) && !(key >= 16 && key <= 18) && key != 524 && key != 20)) {
            if (!getTitle().contains("*")) {
                setTitle(getTitle() + "*");
            }
        }
    }//GEN-LAST:event_codigoFuenteKeyReleased

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        lexico.setText("");
        archivo.Nuevo(this);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        lexico.setText("");
        archivo.Abrir(this);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        archivo.Guardar(this);
    }//GEN-LAST:event_jButton3ActionPerformed

    private void Limpiar() {
        errores.setText("");
        lexico.setText("");
        sintactico.setText("");
        interm.setText("");
    }

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        InicializarPilas();
        Limpiar();
        tablaSimbolos.clear();
        res = "";
        err = "";
        AnalisisLexico();
        interm.setText(intermedio);
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        if (manager.canUndo()) {
            manager.undo();
        }
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        if (manager.canRedo())
            manager.redo();
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jMenuItem7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem7ActionPerformed
        if (manager.canRedo())
            manager.redo();
    }//GEN-LAST:event_jMenuItem7ActionPerformed

    private void jMenuItem8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem8ActionPerformed
        codigoFuente.cut();
    }//GEN-LAST:event_jMenuItem8ActionPerformed

    private void jMenuItem10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem10ActionPerformed
        codigoFuente.paste();
    }//GEN-LAST:event_jMenuItem10ActionPerformed

    public static void main(String args[]) {
        
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Principal().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JTextArea codigoFuente;
    private javax.swing.JTextArea errores;
    private javax.swing.JTextArea interm;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem10;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JMenuItem jMenuItem7;
    private javax.swing.JMenuItem jMenuItem8;
    private javax.swing.JMenuItem jMenuItem9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JTextArea lexico;
    private javax.swing.JTextArea sintactico;
    // End of variables declaration//GEN-END:variables
}
