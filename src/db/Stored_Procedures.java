/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Singleton.java to edit this template
 */
package db;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;
import java.util.Vector;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;

public class Stored_Procedures {
    
    private Connection db_CourseRoom_Conexion;
    
    public Stored_Procedures() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            db_CourseRoom_Conexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/courseroom", "courseroom_server", "Q291cnNlUm9vbQ==");
        } catch (ClassNotFoundException | SQLException ex) {
            
        }
    }
    
    public String Codificacion(String decodificacion){

        byte[] bytes = decodificacion.getBytes();
        bytes = Base64.getEncoder().encode(bytes);
        return new String(bytes);
    }
    
    public Vector<Object> sp_AgregarAviso(int id_Profesor, int id_Usuario, int id_Tarea, String nombre_Archivo){
        Vector<Object> respuesta = new Vector<>();
        String codificacion;
        
        try (CallableStatement ejecutor = db_CourseRoom_Conexion.prepareCall("{CALL sp_AgregarAvisoPlagioProfesor(?,?,?,?)}")){
            
            ejecutor.setInt("_IdProfesor",id_Profesor);
            ejecutor.setInt("_IdUsuario",id_Profesor);
            ejecutor.setInt("_IdTarea",id_Profesor);
            ejecutor.setString("_NombreArchivo",nombre_Archivo);

            try (ResultSet resultado = ejecutor.executeQuery()){
                if(resultado != null){
                    
                    while(resultado.next()){
                        
                        codificacion = Codificacion(resultado.getString("Mensaje"));
                        
                        respuesta.add(resultado.getInt("Codigo"));
                        respuesta.add(codificacion);
                        
                        break;
                    }
                }
            }
        } catch(SQLException ex){
            respuesta.add(-1);
            respuesta.add(ex.getMessage());
        }
        
        return respuesta;
    }
    
    public void Cerrar_Conexion(){
        try {
            db_CourseRoom_Conexion.close();
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
    }
    
}
