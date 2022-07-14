package parcial.patronDAO;

import parcial.modelo.Odontologo;

public interface IDAO<T> {
    T guardar(T t);
    T buscarPorMatricula(String matricula);
    int listarOdontologosTotal();
}
