
package AnalizadoresLS;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

public class HerramientaArchivo {
    JFileChooser selecFile = new JFileChooser();
    File file;
    String[] botones = new String[]{"Guardar y continuar", "Continuar"};
    FileNameExtensionFilter filtro = new FileNameExtensionFilter("ETH", "eth");
    
    
    public String getTextFile(File file){
        String text = "";
        try {
            
            BufferedReader entrada = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF8"));
            while(true){
            int b = entrada.read();
                if(b!=-1)
                    text += (char)b;
                else
                    break;
            }
        } catch (FileNotFoundException ex) {
            System.out.println("El archivo no pudo ser encontrado o no existe"+ex.getMessage());
            return null;
        } catch (IOException ex) {
            System.out.println("Error de lectura del archivo"+ex.getMessage());
            return null;
        }
        return text;   
    }

    
    public boolean saveFile(File archivo, String text){
        try {
            FileOutputStream output = new FileOutputStream(archivo);
            byte[] bytesText = text.getBytes();
            output.write(bytesText);
        } catch (FileNotFoundException ex) {
            System.out.println("Error, el archivo no es encontro"+ex.getMessage());
            return false;
        } catch (IOException ex) {
            System.out.println("Error de escritura deel archivo"+ex.getMessage());
            return false;
        } 
        return true;
    }
    
    
    public boolean guardarEditNuevo(File fileG, JFileChooser selecFileG, Principal compF){
        selecFileG.setFileFilter(filtro);
        int x;
        if(compF.getTitle().equals("ETHIDE*"))
            x = 0;
        else
            x = JOptionPane.showOptionDialog(compF, "El archivo actual ha sido editado, ¿desea guardar los cambios?",
                                            "Cambios de edición", -1, 3, null, botones, botones[0]);
        if(x==0){
            if(selecFileG.getSelectedFile() != null){
                boolean save = saveFile(fileG, compF.codigoFuente.getText());
                if(save)
                    compF.setTitle(fileG.getName());
                    
            }
            else if(compF.getTitle().equals("ETHIDE*")){
                int y = JOptionPane.showOptionDialog(compF, "¿Desea guardar el archivo actual?",
                                                    "Edición de archivo nuevo", -1, 3, null, botones, botones[0]);
                if(y==0){
                    if(selecFileG.showDialog(compF, "Guardar") == JFileChooser.APPROVE_OPTION){
                        fileG = selecFileG.getSelectedFile();
                        String fileGname = fileG.getName();
                
                        if(fileGname.endsWith(".eth")){
                            if(!fileGname.split("[.]")[0].replace(" ","").equals("")){
                                System.out.println(!fileG.exists());
                                if(!fileG.exists())
                                    guardarArch(fileG, compF);  
                                else{
                                    int z = JOptionPane.showConfirmDialog(compF, "Ya existe un archivo con este nombre, ¿desea "
                                                                         +"sobreescribirlo?", "Sobreescribir archivo existente", 2);
                                    if(z == 0)
                                        guardarArch(fileG, compF);                                        
                                }
                            }else{
                                JOptionPane.showMessageDialog(compF, "Escriba un nombre válido para el archivo",
                                                              "Nombre inválido", 2);
                                return false;
                            }
                        }else{
                            JOptionPane.showMessageDialog(compF, "El archivo debe de tener la extensión '.eth'",
                                                          "Extensión inválida", 2); 
                            return false;
                        }
                    }
                    
                }else{
                     return true;
                }
            }else{
            int z = JOptionPane.showConfirmDialog(compF, "Ya hay un archivo con este nombre, ¿desea "
                                                                         +"sobreescribirlo?", "Sobreescribir archivo", 2);
                if(z == 0)
                guardarArch(fileG, compF); 
            }
               
        }
        return true;
    }
    
    
    public boolean guardarEditAbrir(File fileG, JFileChooser selecFileG, Principal compF){
        selecFileG.setFileFilter(filtro);
        int x;
        if(compF.getTitle().equals("ETHIDE*"))
            x = 0;
        else
            x = JOptionPane.showOptionDialog(compF, "El archivo actual ha sido editado, ¿desea guardar los cambios?",
                                            "Cambios de edición", -1, 3, null, botones, botones[0]);
        if(x==0){
            if(selecFileG.getSelectedFile() != null){
                boolean save = saveFile(fileG, compF.codigoFuente.getText());
                if(save)
                    compF.setTitle(fileG.getName());
                    
            }
            else if(compF.getTitle().equals("ETHIDE*")){
                int y = JOptionPane.showOptionDialog(compF, "¿Desea guardar el archivo actual?",
                                                    "Edición de archivo nuevo", -1, 3, null, botones, botones[0]);
               
                if(y==0){
                    if(selecFileG.showDialog(compF, "Guardar") == JFileChooser.APPROVE_OPTION){
                        fileG = selecFileG.getSelectedFile();
                        String fileGname = fileG.getName();
                
                        if(fileGname.endsWith(".eth")){
                            if(!fileGname.split("[.]")[0].replace(" ","").equals("")){
                                if(!fileG.exists())
                                    guardarArch(fileG, compF);  
                                else{
                                    int z = JOptionPane.showConfirmDialog(compF, "Ya existe un archivo con este nombre, ¿desea "
                                                                         +"sobreescribirlo?", "Sobreescribir archivo", 2);
                                    if(z == 0)
                                        guardarArch(fileG, compF);  
                                    else{
                                    
                                    }
                                }
                            }
                            else{
                                JOptionPane.showMessageDialog(compF, "Escriba un nombre válido para el archivo",
                                                              "Nombre inválido", 2);
                                return false;
                            }
                        }
                        else{
                            JOptionPane.showMessageDialog(compF, "El archivo debe de tener la extensión '.eth'",
                                                          "Extensión inválida", 2); 
                            return false;
                        }
                    }
                    
                }
                else{
                     compF.codigoFuente.setText("");
                     compF.setTitle("ETHIDE");
                }
            }
            else{
                int z = JOptionPane.showConfirmDialog(compF, "Ya hay un archivo con este nombre, ¿desea "
                                                                         +"sobreescribirlo?", "Sobreescribir archivo", 2);
                if(z == 0)
                guardarArch(fileG, compF); 
            }
               
        }
        else{
            compF.codigoFuente.setText("");
            compF.setTitle("ETHIDE");
        }
        return true;
    }
    
    
    public void guardarArch(File file, Principal compF){
        boolean save = saveFile(file, compF.codigoFuente.getText());   
        if(save)
            compF.setTitle(file.getName());
        else
            JOptionPane.showMessageDialog(compF, "No se pudo guardar el archivo",
                                         "Error desconocido", 2); 
    }
        
    public void Nuevo(Principal compF){ 
        file = selecFile.getSelectedFile();
        
        if(compF.getTitle().contains("*")){
            if(guardarEditNuevo(file, selecFile, compF)){
                compF.setTitle("ETHIDE");
                compF.codigoFuente.setText("");
                selecFile = new JFileChooser();
                file = null;
            }
        }
        else{
            compF.setTitle("ETHIDE");
            compF.codigoFuente.setText("");
            selecFile = new JFileChooser();
            file = null;
        }
    }
    
    
    public boolean Abrir(Principal compF){
               
        if(compF.getTitle().contains("*")){
            if(guardarEditAbrir(file, selecFile, compF)){
                selecFile = new JFileChooser();
                file = null;
            }
        }
        JFileChooser tSelecFile = new JFileChooser();
        File tFile;
        tSelecFile.setFileFilter(filtro);
        
        if(tSelecFile.showDialog(compF, "Abrir") == JFileChooser.APPROVE_OPTION){
            tFile = tSelecFile.getSelectedFile();
            String filename = tFile.getName();
             

            if(filename.endsWith(".eth")){
                if(!filename.split("[.]")[0].replace(" ","").equals("")){
                    if(!tFile.exists())
                        JOptionPane.showMessageDialog(compF, "El archivo que sea desea abrir no existe en el directorio especificado",
                                                     "Archivo no encontrado", 2);      
                    else{
                        String t = getTextFile(tFile);
                           
                        if(t!=null){
                            compF.codigoFuente.setText(t);
                            compF.setTitle(tFile.getName());
                            selecFile = tSelecFile;
                            file = tFile;
                        }
                        else{
                            JOptionPane.showMessageDialog(compF, "Error al leer el archivo",
                                                         "Error desconocido", 2);
                            return false;
                        }
                    }
                }
                else{
                    JOptionPane.showMessageDialog(compF, "Escriba un nombre válido para el archivo",
                                                  "Nombre inválido", 2); 
                    return false;
                }
            }
            else{
                JOptionPane.showMessageDialog(compF, "El archivo debe de tener la extensión '.eth'",
                                             "Extensión inválida", 2);  
                return false;
            }
        }
        else
            return false;
        return true;
    }
    
    public boolean Guardar(Principal compF){
        if(file != null)
            guardarArch(file, compF);
        else{
            JFileChooser tSelecFile = new JFileChooser();
            File tFile;
            tSelecFile.setFileFilter(filtro);
            //FileNameExtensionFilter ext = new FileNameExtensionFilter("Archivos eth", "eth");
            //tSelecFile.setFileFilter(ext);
            
            if(tSelecFile.showDialog(compF, "Guardar") == JFileChooser.APPROVE_OPTION){
                tFile = tSelecFile.getSelectedFile();
                String filename = tFile.getName();
                
                if(true){
                    if(!filename.split("[.]")[0].replace(" ","").equals("")){
                        if(!tFile.exists()){
                            guardarArch(tFile, compF);  
                            file = tFile;
                            System.out.println(tFile);
                            selecFile = tSelecFile;
                        }
                        else{
                            int x = JOptionPane.showConfirmDialog(compF, "Ya existe un archivo con este nombre, ¿desea "
                                                                +"sobreescribirlo?", "Sobreescribir archivo", 2);
                            if(x == 0){
                                guardarArch(tFile, compF); 
                                file = tFile;
                                selecFile = tSelecFile;
                            }
                            else{
                                selecFile = new JFileChooser();
                                file = null;
                            }
                            
                        }
                    }
                    else{
                        JOptionPane.showMessageDialog(compF, "Escriba un nombre válido para el archivo",
                                                  "Nombre inválido", 2);
                        selecFile = new JFileChooser();
                        file = null;
                        return false;
                    }
                }
                else{
                    JOptionPane.showMessageDialog(compF, "El archivo debe de tener la extensión '.eth'",
                                                  "Extensión inválida", 2); 
                    selecFile = new JFileChooser();
                    file = null;
                    return false;
                }
            }
            else 
                return false;
        }
        return true;
    }
    
    
    public void guardarC(Principal compF){
        JFileChooser tSelecFile = new JFileChooser();
        tSelecFile.setFileFilter(filtro);
        
        if(tSelecFile.showDialog(compF, "Guardar como") == JFileChooser.APPROVE_OPTION){
            File tFile;
            tFile = tSelecFile.getSelectedFile();
            String filename = tFile.getName();
                
            if(filename.endsWith(".eth")){
                if(!filename.split("[.]")[0].replace(" ","").equals("")){
                    guardarArch(tFile, compF);  
                    file = tFile;
                    selecFile = tSelecFile;
                }
                else
                    JOptionPane.showMessageDialog(compF, "Escriba un nombre válido para el archivo",
                                                 "Nombre inválido", 2); 
            }
            else
                JOptionPane.showMessageDialog(compF, "El archivo debe de tener la extensión '.eth'",
                                             "Extensión inválida", 2);  
        }
    }

}
