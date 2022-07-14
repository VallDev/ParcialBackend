package parcial.patronDAO;

import org.apache.log4j.Logger;
import parcial.modelo.Odontologo;

import java.sql.*;

public class OdontologoDAOH2 implements IDAO<Odontologo>{

    //SENTENCIAS SQL

    private static final String SQL_INSERT="INSERT INTO ODONTOLOGOS (MATRICULA, NOMBRE, APELLIDO)"
            +" VALUES (?,?,?)";

    private static final String SQL_SELECTxMATRICULA="SELECT * FROM ODONTOLOGOS WHERE MATRICULA=?";

    private static final String SQL_SELECT_TOTAL="SELECT * FROM ODONTOLOGOS";

    //LOGGER PARA LOS REGISTROS E INFO

    private static final Logger logger= Logger.getLogger(OdontologoDAOH2.class);

    //GUARDAR ODONTOLOGOS -> PERMITE GUARDAR ODONTOLOGOS EN LA BASE DE DATOS

    @Override
    public Odontologo guardar(Odontologo odontologo) {

        Connection connection = null;
        try {
            connection = getConnection(connection);

            //SE PREPARA EL RESULTSET PARA QUE EL APUNTADOR DE SU RESULTADO PUEDA MODIFICARSE A CONVENIENCIA
            //Y QUE SE PUEDA UBICAR EN LA FILA QUE SE DESEE

            PreparedStatement preparedStatement = connection.prepareStatement(SQL_INSERT,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            preparedStatement.setString(1,odontologo.getNroMatricula());
            preparedStatement.setString(2,odontologo.getNombre());
            preparedStatement.setString(3,odontologo.getApellido());

            preparedStatement.executeUpdate();

            ResultSet rs = preparedStatement.getGeneratedKeys();
            odontologo = ejecutarResultSet(odontologo,rs,'g');

            //SE PONE EL RESULTSET APUNTANDO A LA PRIMERA FILA NUEVAMENTE

            rs.beforeFirst();
            mostrarResulset(rs);

        }catch (Exception e){
            e.printStackTrace();
            logger.error("ERROR CON CONEXION");
        }finally {
            try{
                connection.close();
                logger.info("Conexion cerrada exitosamente");
            }catch (SQLException se){
                se.printStackTrace();
                logger.error("ERROR AL CERRAR CONEXION");
            }
        }
        return odontologo;
    }

    // SE BUSCA UN ODONTOLOGO POR MATRICULA EN LA BASE DE DATOS PARA PODER MOSTRAR EL RESULTADO

    @Override
    public Odontologo buscarPorMatricula(String matricula) {

        Connection connection = null;
        Odontologo odontologo = null;
        try {
            connection = getConnection(connection);

            //SE PREPARA EL RESULTSET PARA QUE EL APUNTADOR DE SU RESULTADO PUEDA MODIFICARSE A CONVENIENCIA
            //Y QUE SE PUEDA UBICAR EN LA FILA QUE SE DESEE

            PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECTxMATRICULA,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            preparedStatement.setString(1,matricula);
            ResultSet rs = preparedStatement.executeQuery();
            odontologo = ejecutarResultSet(odontologo,rs,'b');

            //SE PONE EL RESULTSET APUNTANDO A LA PRIMERA FILA NUEVAMENTE

            rs.beforeFirst();
            mostrarResulset(rs);

        }catch (Exception e){
            e.printStackTrace();
            logger.error("ERROR CON CONEXION");
        }finally {
            try {
                connection.close();
                logger.info("Conexion cerrada exitosamente");
            }catch (SQLException se){
                se.printStackTrace();
                logger.error("ERROR AL CERRAR CONEXION");
            }
        }

        return odontologo;
    }

    //SE LISTA EL TOTAL DE ODONTOLOGOS EN LA BASE DE DATOS PARA PODER MOSTRARLOS EN PANTALLA

    public int listarOdontologosTotal(){
        Connection connection = null;
        int cantidadFilas = 0;

        try{
            connection = getConnection(connection);

            //SE PREPARA EL RESULTSET PARA QUE EL APUNTADOR DE SU RESULTADO PUEDA MODIFICARSE A CONVENIENCIA
            //Y QUE SE PUEDA UBICAR EN LA FILA QUE SE DESEE

            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = statement.executeQuery(SQL_SELECT_TOTAL);
            cantidadFilas = cantidadFilasResultset(rs);

            //SE PONE EL RESULTSET APUNTANDO A LA PRIMERA FILA NUEVAMENTE

            rs.beforeFirst();
            mostrarResulset(rs);

        }catch (Exception e){
            e.printStackTrace();
            logger.error("ERROR CON CONEXION");
        } finally {
            try {
                connection.close();
                logger.info("Conexion cerrada exitosamente");
            }catch (SQLException se){
                se.printStackTrace();
                logger.error("ERROR AL CERRAR CONEXION");
            }
        }
        return cantidadFilas;
    }

    //METODO QUE NOS SIRVE PARA CONECTARNOS A LA BASE DE DATOS

    private static Connection getConnection(Connection connection) throws Exception{
        Class.forName("org.h2.Driver").newInstance();
        connection = DriverManager.getConnection("jdbc:h2:~/parcial","sa","sa");
        logger.info("Conexion con base de datos inicada");
        return connection;
    }

    //EJECUTA EL RESULSET TOMANDO COMO BASE LA OPCION DE SI SE QUIERE GUARDAR O BUSCAR EN LA BASE DE DATOS

    private Odontologo ejecutarResultSet(Odontologo odontologo,ResultSet rs, char opcion) throws SQLException{
        if (opcion=='g'){
            while (rs.next()) {
                odontologo.setId(rs.getInt(1));
            }
            logger.info("Nuevo odontologo guardado exitosamente");
            System.out.println("GUARDADO EXITOSAMENTE");
        }

        if (opcion=='b'){
           while (rs.next()){
               odontologo = new Odontologo(rs.getInt(1),rs.getString(2),rs.getString(3),rs.getString(4));
           }
           logger.info("Odontologo encontrado exitosamente");
            System.out.println("BUSQUEDA  EXITOSA");
        }
        return odontologo;
    }

    //MUESTRA LOS RESULTADOS DEL RESULSET Y LOS MUESTRA POR PANTALLA

    private void mostrarResulset(ResultSet rs) throws SQLException{
        System.out.println("DATOS: ");
        while(rs.next()){

            System.out.println("ID: "+ rs.getInt(1)+ " - MATRICULA: "+rs.getString(2)+" - NOMBRE: "+rs.getString(3)+
                    " - APELLIDO: "+rs.getString(4));
        }
        logger.info("Se imprimieron odontologos por pantalla");
    }

    //CALCULA LA CANTIDAD DE FILAS TOTAL TOMANDO EL RESULTADO DEL RESULTSET
    //POR LO QUE CALCULARIA EL TOTAL DE ODONTOLOGOS EN LA BASE DE DATOS

    private int cantidadFilasResultset(ResultSet rs) throws  SQLException{
        int cantidadFilas = 0;
        while (rs.next()){
            cantidadFilas++;
        }
        logger.info("Se consultaron la cantidad total de odontologos");
        return cantidadFilas;
    }
}
