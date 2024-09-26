package com.iessoterohernandez.Nominas.Laboral;

/**
 * La clase {@code Nomina} se encarga de calcular el sueldo de un empleado
 * en función de su categoría y años de servicio.
 * Contiene una lista de sueldos base predefinidos para cada categoría.
 */
public class Nomina {

    // Sueldo base para cada categoría de empleado.
    private static final int SUELDO_BASE[] = {
            50000, 70000, 90000, 110000, 130000,
            150000, 170000, 190000, 210000, 230000
    };

    /**
     * Calcula el sueldo total de un empleado basado en su categoría
     * y sus años de servicio.
     *
     * @param empleado El empleado del cual se quiere calcular el sueldo.
     * @return El sueldo total del empleado, que incluye el sueldo base
     *         y un incremento por cada año de servicio.
     */
    public static int sueldo(Empleado empleado) {
        return SUELDO_BASE[empleado.getCategoria() - 1] + 5000 * empleado.anyos;
    }
}
