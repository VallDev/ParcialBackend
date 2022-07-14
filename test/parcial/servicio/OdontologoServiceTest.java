package parcial.servicio;

import org.apache.log4j.Logger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import parcial.modelo.Odontologo;
import parcial.patronDAO.OdontologoDAOH2;

import java.sql.*;

public class OdontologoServiceTest {

    //DECLARACION DEL LOGGER

    private static final Logger logger= Logger.getLogger(OdontologoServiceTest.class);

    //CARGA LA BASE DE DATOS TOMANDO COMO REFERENCIA EL CREATE.SQL

    @BeforeAll
    public static void cargarScriptBD(){
        logger.info("Iniciando carga de datos");
        Connection connection = null;
        try{
            Class.forName("org.h2.Driver").newInstance();
            connection= DriverManager.getConnection("jdbc:h2:~/parcial;INIT=RUNSCRIPT FROM 'create.sql'","sa","sa");
            logger.info("Conexion exitosa desde test cargando datos");

        }catch (Exception e){
            e.printStackTrace();
            logger.error("ERROR DE CONEXION EN TEST CARGANDO DATOS");
        }finally {
            try{
                connection.close();
                logger.info("Conexion cerrada exitosamente en test cargando datos");
            }
            catch (SQLException se){
                se.printStackTrace();
                logger.error("ERROR AL CERRAR CONEXION EN TEST CARGANDO DATOS");
            }
        }
    }

    //SE TESTEAN LOS METODOS DE GUARDAR Y BUSCAR ODONTOLOGO

    @Test
    public void pruebaGuardarYBuscarOdontologo(){
        logger.info("Iniciando prueba de guardado y busqueda de odontologo");

        String matriculaNuevoOdontologo = "4456123141";

        Odontologo odontologo = new Odontologo(matriculaNuevoOdontologo, "Sergio", "Lopez");
        OdontologoService odontologoService = new OdontologoService(new OdontologoDAOH2());
        Odontologo odontologoObtenido;


        odontologoService.guardar(odontologo);
        odontologoObtenido = odontologoService.buscarPorMatricula(matriculaNuevoOdontologo);

        Assertions.assertEquals(matriculaNuevoOdontologo,odontologoObtenido.getNroMatricula());

        logger.info("Prueba de guardado y busqueda de odontologo terminada");
    }

    //SE TESTEA LA FUNCION DE LISTAR Y MOSTRAR TODOS LOS ODONTOLOGOS DEL SISTEMA

    @Test
    public void pruebaListarTodosOdontologos() throws Exception {
        logger.info("Iniciando prueba de listado de todos los odontologos");

        OdontologoService odontologoService = new OdontologoService(new OdontologoDAOH2());
        int ultimoID = 0;

        Connection connection = null;
        try{
            Class.forName("org.h2.Driver").newInstance();
            connection = DriverManager.getConnection("jdbc:h2:~/parcial","sa","sa");

            logger.info("Conexion exitosa desde test prueba listar todos odontologos");

            //SE PREPARA EL RESULTSET PARA QUE EL APUNTADOR DE SU RESULTADO PUEDA MODIFICARSE A CONVENIENCIA
            //Y QUE SE PUEDA UBICAR EN LA FILA QUE SE DESEE

            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = statement.executeQuery("SELECT * FROM ODONTOLOGOS");

            //SE PONE EL RESULTSET APUNTANDO A LA ULTIMA FILA

            rs.last();
            ultimoID = rs.getInt(1);

        } catch (SQLException e) {
            e.printStackTrace();
            logger.error("ERROR DE CONEXION DESDE TEST PRUEBA LISTAR TODOS ODONTOLOGOS");
        }finally {
            try {
                connection.close();
                logger.info("Conexion cerrada exitosamente desde test prueba listar todos odontologos");
            }catch (SQLException se){
                se.printStackTrace();
                logger.error("ERROR AL CERRAR CONEXION DESDE TEST PRUEBA LISTAR TODOS ODONTOLOGOS");
            }
        }

        //SE VERIFICA SI LA CANTIDAD DE FILAS ES IGUAL AL VALOR DE LA ULTIMA FILA

        Assertions.assertEquals(odontologoService.listarOdontologosTotal(), ultimoID);

        logger.info("Prueba de busqueda de todos odontologos terminada");
    }

}
