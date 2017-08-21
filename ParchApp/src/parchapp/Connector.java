/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parchapp;

import java.sql.*;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;


/**
 *
 * @author Usuario
 */
public class Connector {
    Connection conn = null;
    Statement stm = null;
    //Cambiar la parte '//127.0.0.1:3306/' por la ruta donde esté creada su instancia de mysql
    static final String url = "jdbc:mysql://127.0.0.1:3306/tiretec";
    static final String user = "root";
    static final String pswd = "675744";

    public Connector() {
    }
    
    public Connection getConnection() {
        try {
            conn = DriverManager.getConnection(url, user, pswd);
            //System.out.println("Se generó la conexion. ");
        } catch (Exception ex) {
            System.out.println("error occured " + ex.toString());
            System.out.println("No se generó la conexion. ");
        }
        return conn;
    }
    public void insertarCliente(Cliente cliente){
        String cadena = "{CALL insertarCliente(?,?,?,?,?,?,?,?)}";
        try{
            CallableStatement cs = this.getConnection().prepareCall(cadena);
            cs.setString(1, cliente.getCedula());
            cs.setString(2, cliente.getRuc());
            cs.setString(3, cliente.getPasaporte());
            cs.setString(4, cliente.getNombres());
            cs.setString(5, cliente.getApellidos());
            cs.setString(6, cliente.getDireccion());
            cs.setString(7, cliente.getEmail());
            cs.setString(8, cliente.getTipoCliente());
            cs.executeQuery();
            JOptionPane.showMessageDialog(null,"Cliente ingresado correctamente.","Mensaje del sistema",JOptionPane.INFORMATION_MESSAGE);
            //System.out.println("Secuencia de 'insertarProducto' ejecutada correctamente.");
        }catch(SQLException ex){
            System.out.println(ex.getMessage());
        }
    }
    
    public void insertarProducto(Producto producto){
        String cadena = "{CALL insertarProducto(?,?,?,?)}";
        try{
            CallableStatement cs = this.getConnection().prepareCall(cadena);
            cs.setString(1, producto.getNombreProducto());
            cs.setFloat(2, producto.getPrecioPublico());
            cs.setFloat(3, producto.getPrecioMayorista());
            cs.setString(4, producto.getDescripcion());
            cs.executeQuery();
            JOptionPane.showMessageDialog(null,"Producto ingresado correctamente.","Mensaje del sistema",JOptionPane.INFORMATION_MESSAGE);
            
            //System.out.println("Secuencia de 'insertarProducto' ejecutada correctamente.");
        }catch(SQLException ex){
            System.out.println(ex.getMessage());
        }
    }
        
    public boolean iniciarSesion(Usuario usuario){
        String cadena = "{? = CALL acceder(?,?)}";
        try{
            CallableStatement cs = this.getConnection().prepareCall(cadena);
            cs.registerOutParameter(1, java.sql.Types.VARCHAR);
            cs.setString(2, usuario.getUsuario());
            cs.setString(3, usuario.getContraseña());
            cs.execute();
            String output = cs.getString(1);
            if(!output.equals("")) return true;
        }catch(SQLException ex){
            System.out.println(ex.getMessage());
        }
        return false;
    }
    
    public ArrayList<String> cargarProveedores(){
        ArrayList<String> proveedores = new ArrayList();
        String cadena = "{CALL ListarProveedores()}";
        try{
            CallableStatement cs = this.getConnection().prepareCall(cadena);
            ResultSet rs = cs.executeQuery();
            while(rs.next()){
                proveedores.add(rs.getString(1));
            }
        }catch(SQLException ex){
            System.out.println(ex.getMessage());
        }
        return proveedores;
    }
    
    public void cargarProducto(JTable j1,DefaultTableModel dfm){
        ArrayList<Object[]> datos = new ArrayList<Object[]>();
        String cadena = "{CALL visualizarProductos()}";
        try{
            System.out.println("Aqui sigue1");
            CallableStatement cs = this.getConnection().prepareCall(cadena);
            ResultSet rs = cs.executeQuery();            
            System.out.println("Aqui sigue2");
            ResultSetMetaData rsm = rs.getMetaData();
            while(rs.next()){
                Object[] filas = new Object[rsm.getColumnCount()];
                for(int i = 0;i<rsm.getColumnCount();i++){
                    filas[i]= rs.getObject(i+1);
                }
                datos.add(filas);
            }
            for(int i=0;i<datos.size();i++){
                 dfm.addRow(datos.get(i));
            }
            System.out.println("Aqui sigue9");
        }catch(SQLException ex){
            System.out.println("falla");
            System.out.println(ex.getMessage());
        }
    }
    
    public ArrayList<String> cargarPaises(){
        ArrayList<String> paises = new ArrayList();
        String cadena = "{CALL CargarPaises()}";
        try{
            CallableStatement cs = this.getConnection().prepareCall(cadena);
            ResultSet rs = cs.executeQuery();
            while(rs.next()){
                paises.add(rs.getString(1));
            }
        }catch(SQLException ex){
            System.out.println(ex.getMessage());
        }
        return paises;
    }
    
    public ArrayList<String> cargarCiudades(String pais){
        ArrayList<String> ciudades = new ArrayList();
        String cadena = "{CALL CargarCiudadesPorPais(?)}";
        try{
            CallableStatement cs = this.getConnection().prepareCall(cadena);
            cs.setString(1, pais);
            ResultSet rs = cs.executeQuery();
            while(rs.next()){
                ciudades.add(rs.getString(1));
            }
        }catch(SQLException ex){
            System.out.println(ex.getMessage());
        }
        return ciudades;
    }
    
    public int obtenerIdPais(String nomPais){
        String cadena = "{? = CALL obtenerIdPais(?)}";
        int salida = 0;
        try{
            CallableStatement cs = this.getConnection().prepareCall(cadena);
            cs.registerOutParameter(1, java.sql.Types.INTEGER);
            cs.setString(2, nomPais);
            cs.execute();
            salida = cs.getInt(1);
        }catch(SQLException ex){
            System.out.println(ex.getMessage());
        }
        System.out.println("IdPais: " + salida);
        return salida;
    }
    
    public int obtenerIdCiudad(String nomPais,String nomCiudad){
        System.out.println("nomPais: "+nomPais + " nomCiudad: "+nomCiudad);
        String cadena = "{? = CALL obtenerIdCiudad(?,?)}";
        int salida = 0;
        try{
            CallableStatement cs = this.getConnection().prepareCall(cadena);
            cs.registerOutParameter(1, java.sql.Types.INTEGER);
            cs.setString(2, nomCiudad);
            cs.setString(3, nomPais);
            cs.execute();
            salida = cs.getInt(1);
        }catch(SQLException ex){
            System.out.println(ex.getMessage());
        }
        System.out.println("IdCiudad: " + salida);
        return salida;
    }
    
    public void insertarProveedor(Proveedor proveedor){
        String cadena = "{CALL insertarProveedor(?,?,?,?,?,?)}";
        try{
            CallableStatement cs = this.getConnection().prepareCall(cadena);
            cs.setString(1, proveedor.getNombreProveedor());
            cs.setString(2, proveedor.getTelefono());
            cs.setString(3, proveedor.getEmail());
            cs.setString(4, proveedor.getDireccion());
            cs.setInt(5, proveedor.getPais());
            cs.setInt(6, proveedor.getCiudad());
            cs.executeQuery();
            JOptionPane.showMessageDialog(null,"Proveedor ingresado correctamente.","Mensaje del sistema",JOptionPane.INFORMATION_MESSAGE);
            //System.out.println("Secuencia de 'insertarProducto' ejecutada correctamente.");
        }catch(SQLException ex){
            System.out.println(ex.getMessage());
        }
    }
    
    public void insertarProd_Prov(int idProd, int idProv){
        String cadena = "{CALL insertarProd_Prov(?,?)}";
        try{
            CallableStatement cs = this.getConnection().prepareCall(cadena);
            cs.setInt(1, idProd);
            cs.setInt(2, idProv);
            cs.executeQuery();
            JOptionPane.showMessageDialog(null,"Proveedor ingresado correctamente.","Mensaje del sistema",JOptionPane.INFORMATION_MESSAGE);
            //System.out.println("Secuencia de 'insertarProducto' ejecutada correctamente.");
        }catch(SQLException ex){
            System.out.println(ex.getMessage());
        }
    }
        
    public ArrayList<Integer> obtenerIdProv_Prod(int idProd){
        ArrayList<Integer> idProv = new ArrayList();
        System.out.println("idProd: "+ idProd );
        String cadena = "{ CALL obtenerIdProv_Prod(?)}";
        try{
            CallableStatement cs = this.getConnection().prepareCall(cadena);
            cs.setInt(1, idProd);
            ResultSet rs = cs.executeQuery();
            while(rs.next()){
                idProv.add(rs.getInt(1));
            }
        }catch(SQLException ex){
            System.out.println(ex.getMessage());
        }
        return idProv;
    }
    
    public int obtenerIdProducto(String nomProd){
        System.out.println("nomProd: "+nomProd );
        String cadena = "{? = CALL obtenerIdProducto(?)}";
        int salida = 0;
        try{
            CallableStatement cs = this.getConnection().prepareCall(cadena);
            cs.registerOutParameter(1, java.sql.Types.INTEGER);
            cs.setString(2, nomProd);
            cs.execute();
            salida = cs.getInt(1);
        }catch(SQLException ex){
            System.out.println(ex.getMessage());
        }
        return salida;
    }
    
    public int obtenerIdProveedor(String nomProv){
        String cadena = "{? = CALL obtenerIdProveedor(?)}";
        int salida = 0;
        try{
            CallableStatement cs = this.getConnection().prepareCall(cadena);
            cs.registerOutParameter(1, java.sql.Types.INTEGER);
            cs.setString(2, nomProv);
            cs.execute();
            salida = cs.getInt(1);
        }catch(SQLException ex){
            System.out.println(ex.getMessage());
        }
        return salida;
    }
    
    public void eliminarProd_Prov(int idProveedor, int idProducto){
        String cadena = "{CALL eliminarProd_Prov(?,?)}";
        try{
            CallableStatement cs = this.getConnection().prepareCall(cadena);
            cs.setInt(1, idProveedor);
            cs.setInt(2, idProducto);
            cs.executeQuery();
            JOptionPane.showMessageDialog(null,"Proveedor eliminado correctamente.","Mensaje del sistema",JOptionPane.INFORMATION_MESSAGE);
            //System.out.println("Secuencia de 'insertarProducto' ejecutada correctamente.");
        }catch(SQLException ex){
            System.out.println(ex.getMessage());
        }
    }
    
    public void eliminarProducto(String nombProd){
        String cadena = "{CALL eliminarProducto(?)}";
        try{
            CallableStatement cs = this.getConnection().prepareCall(cadena);
            cs.setString(1, nombProd);
            cs.executeQuery();
            JOptionPane.showMessageDialog(null,"Producto eliminado correctamente.","Mensaje del sistema",JOptionPane.INFORMATION_MESSAGE);
            //System.out.println("Secuencia de 'insertarProducto' ejecutada correctamente.");
        }catch(SQLException ex){
            System.out.println(ex.getMessage());
        }
    }
}
