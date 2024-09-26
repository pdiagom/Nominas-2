package com.iessoterohernandez.Nominas.Laboral;

/**
* Clase empleado para
 */
public class Empleado extends Persona {
    private int categoria;
    public int anyos;

    /**
     * Constructor que crea un nuevo empleado con un DNI, nombre y sexo,
     * asignando una categoría predeterminada de 1 y años en 0.
     *
     * @param dni El DNI del empleado.
     * @param nombre El nombre del empleado.
     * @param sexo El sexo del empleado.
     */
    public Empleado(String dni, String nombre, char sexo) {
        super(dni, nombre, sexo);
        this.categoria = 1;
        this.anyos = 0;
    }

    /**
     * Constructor que crea un nuevo empleado con un DNI, nombre, sexo,
     * categoría y años. Lanza una excepción si la categoría
     * o los años no son válidos.
     *
     * @param dni El DNI del empleado.
     * @param nombre El nombre del empleado.
     * @param sexo El sexo del empleado.
     * @param categoria La categoría del empleado (debe estar entre 1 y 10).
     * @param anyos Los años de servicio del empleado (debe ser 0 o mayor).
     * @throws DatosNoCorrectosException Si la categoría o los años no son válidos.
     */
    public Empleado(String dni, String nombre, char sexo, int categoria, int anyos)
            throws DatosNoCorrectosException {
        super(dni, nombre, sexo);
        if (categoria < 1 || categoria > 10) {
            throw new DatosNoCorrectosException("Datos no correctos");
        } else {
            this.categoria = categoria;
        }
        if (anyos < 0) {
            throw new DatosNoCorrectosException("Datos no correctos");
        } else {
            this.anyos = anyos;
        }
    }

    /**
     * Establece la categoría del empleado.
     * Lanza una excepción si la categoría no es válida.
     *
     * @param categoria La nueva categoría del empleado (debe estar entre 1 y 10).
     * @throws DatosNoCorrectosException Si la categoría no es válida.
     */
    public void setCategoria(int categoria) throws DatosNoCorrectosException {
        if (categoria < 1 || categoria > 10) {
            throw new DatosNoCorrectosException("Datos no correctos");
        } else {
            this.categoria = categoria;
        }
    }

    /**
     * Obtiene la categoría del empleado.
     *
     * @return La categoría del empleado.
     */
    public int getCategoria() {
        return categoria;
    }

    /**
     * Incrementa en 1 el número de años de servicio del empleado.
     */
    public void incrAnyo() {
        anyos += 1;
    }

    /**
     * Imprime la información del empleado en la consola.
     */
    @Override
    public void imprime() {
        System.out.println("Nombre: " + nombre + " DNI: " + dni + " Sexo: " + sexo +
                " Categoria: " + categoria + " Años: " + anyos);
    }
}
