package parcial.servicio;

import parcial.modelo.Odontologo;
import parcial.patronDAO.IDAO;

public class OdontologoService {
    private IDAO<Odontologo> odontologoDao;

    public OdontologoService(IDAO<Odontologo> odontologoDao) {
        this.odontologoDao = odontologoDao;
    }

    public Odontologo guardar(Odontologo odontologo){
        return odontologoDao.guardar(odontologo);
    }

    public Odontologo buscarPorMatricula(String matricula){
        return odontologoDao.buscarPorMatricula(matricula);
    }

    public int listarOdontologosTotal(){
        return odontologoDao.listarOdontologosTotal();
    }
}
