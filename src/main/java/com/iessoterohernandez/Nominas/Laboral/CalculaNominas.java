package com.iessoterohernandez.Nominas.Laboral;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

/**
 * La clase {@code CalculaNominas} es la clase principal que se encarga de calcular y mostrar
 * la nómina de empleados. Contiene el método principal,
 * modificaciones en sus datos y muestra la información relevante.
 */
public class CalculaNominas {

    private static GestorBD g;

    /**
     * Crea instancias de {@code Empleado}, actualiza sus datos y
     * imprime la información de la nómina correspondiente a cada uno.
     *
     * @param args Los argumentos de línea de comandos (no se utilizan en esta implementación).
     */
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int opcion;

        do {
            System.out.println("Introduce el número del dato que quieras modificar:\n" +
                    "0 - Salir\n" +
                    "1 - Mostrar información de todos los empleados\n" +
                    "2 - Mostrar salario de un empleado\n" +
                    "3 - Modificar datos de un empleado\n" +
                    "4 - Actualizar sueldo de un empleado\n" +
                    "5 - Actualizar sueldo de todos los empleados\n" +
                    "6 - Relizar copia de seguridad de la base de datos");
            opcion = sc.nextInt();
            switch (opcion) {
                case 0:
                    System.out.println("Saliendo");
                    break;
                case 1:
                    imprimeEmpleados();
                    break;
                case 2:
                    System.out.println("Indica el dni del empleado:");
                    String dni = sc.next();
                    imprimeSueldo(dni);
                    break;
                case 3:
                    updateEmpleado();
                    break;
                case 4, 5:
                    updateSueldo();
                    break;
                case 6:
                    copiaSeguridad();
                    break;
                default:
                    System.out.println("Introduce una opción válida");
                    break;

            }
        } while (opcion != 0);
    }

    /**
     * Imprime la información de los empleados y su sueldo.
     *
     * @param empleados Lista de empleados.
     */
    private static void escribe(List<Empleado> empleados) {
        for (Empleado empleado : empleados) {
            empleado.imprime();
            System.out.println("Sueldo de " + empleado.nombre + ": " + Nomina.sueldo(empleado));
        }
    }

    public static void calculaSueldo(String archivo, List<Empleado> empleados) {
        try (DataOutputStream datos = new DataOutputStream(new FileOutputStream(archivo))) {
            for (Empleado empleado : empleados) {
                datos.writeUTF(empleado.dni);
                datos.writeDouble(Nomina.sueldo(empleado));
            }
        } catch (IOException e) {
            System.err.println("Error al escribir los datos");
        }

    }

    private static List<Empleado> leer(String archivo) {
        List<Empleado> empleados = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] empleado = linea.split(", ");
                String dni = empleado[0];
                String nombre = empleado[1];
                char sexo = empleado[2].charAt(0);
                int categoria = empleado.length > 3 ? Integer.parseInt(empleado[3]) : 1;
                int anyos = empleado.length > 4 ? Integer.parseInt(empleado[4]) : 0;

                Empleado e1 = new Empleado(dni, nombre, sexo, categoria, anyos);
                empleados.add(e1);
            }
        } catch (IOException e) {
            System.err.println("Error al leer el archivo");
        }
        return empleados;
    }

    private static void imprimeEmpleados() {
        GestorBD g = new GestorBD();
        List<Empleado> empleados = g.obtieneEmpleados();
        for (Empleado e : empleados) {
            e.imprime();
        }
    }

    private static void imprimeSueldo(String dni) {
        GestorBD g = new GestorBD();
        double sueldo = g.obtieneSueldo(dni);
        System.out.println("El empleado con dni: " + dni + " cobra " + sueldo + " al año.");
    }

    private static void updateEmpleado() {
        GestorBD g = new GestorBD();
        g.updateEmpleado();
    }

    public static void updateSueldo() {
        System.out.println("El sueldo de los empleados se actualiza automaticamente al realizar cualquier cambio en los datos.");
    }

    public static void copiaSeguridad() {
        Scanner sc = new Scanner(System.in);
        GestorBD g = new GestorBD();
        System.out.println("Introduzca el nombre del archivo sin la extension:");
        String archivo = sc.next();
        archivo.concat(".txt");
        g.copiaSeguridad(archivo);
    }

}
