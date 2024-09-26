package com.iessoterohernandez.Nominas.Laboral;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GestorBD implements GestorBDInterface {
    private static final String URL = "jdbc:mysql://localhost:3306/empresa";  // Cambia "tu_base_de_datos"
    private static final String USUARIO = "root";   // Cambia el usuario si es necesario
    private static final String CONTRASENA = "root"; // Cambia la contraseña


    public void altaEmpleado(Empleado e) {
        String insercionSQL = "INSERT INTO empleados (dni, nombre, genero, categoria, anyos_trabajados) VALUES ('" + e.dni + "', '" + e.nombre + "', '" + e.sexo + "', " + e.getCategoria() + ", " + e.anyos + ")";
        try (Connection con = DriverManager.getConnection(URL, USUARIO, CONTRASENA);
             Statement st = con.createStatement()) {
            System.out.println("Base de datos conectada");
            st.execute(insercionSQL);
        } catch (SQLException exception) {
            exception.printStackTrace();
            System.out.println("Error al conectar a la base de datos.");
        }
    }

    @Override
    public void altaEmpleado(String archivo) {
        List<Empleado> empleados = leer(archivo);
        for (Empleado e : empleados) {
            altaEmpleado(e);
        }
    }

    private List<Empleado> leer(String archivo) {
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

    public List<Empleado> obtieneEmpleados() {
        List<Empleado> empleados = new ArrayList<>();
        try (Connection con = DriverManager.getConnection(URL, USUARIO, CONTRASENA);
             Statement st = con.createStatement(); ResultSet rs = st.executeQuery("SELECT * FROM EMPLEADOS")) {

            while (rs.next()) {
                String dni = rs.getString(1);
                String nombre = rs.getString(2);
                String sexo = rs.getString(3);
                int categoria = rs.getInt(4);
                int anyos = rs.getInt(5);

                Empleado e = new Empleado(dni, nombre, sexo.charAt(0), categoria, anyos);
                empleados.add(e);
            }

        } catch (SQLException exception) {
            exception.printStackTrace();
            System.out.println("Error al conectar a la base de datos.");
        }
        return empleados;
    }

    public double obtieneSueldo(String dni) {
        double sueldo = 0;
        try (Connection con = DriverManager.getConnection(URL, USUARIO, CONTRASENA);
             Statement st = con.createStatement(); ResultSet rs = st.executeQuery("SELECT sueldo FROM nominas WHERE dni='" + dni + "'")) {
            if (rs.next()) {
                sueldo = rs.getDouble(1);
            }

        } catch (SQLException exception) {
            exception.printStackTrace();
            System.out.println("Error al conectar a la base de datos.");
        }
        return sueldo;
    }

    public void updateEmpleado() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Introduce el DNI del empleado que deseas modificar:");
        String dni = sc.next();
        try (Connection con = DriverManager.getConnection(URL, USUARIO, CONTRASENA);
             Statement st = con.createStatement(); ResultSet rs = st.executeQuery("SELECT dni FROM empleados WHERE dni='" + dni + "'")) {
            if (!rs.next()) {
                System.out.println("No existe empleado con DNI: " + dni);
            } else {
                int opcion;

                do {
                    System.out.println("Introduce el número del dato que quieras modificar:\n" +
                            "0 - Salir\n" +
                            "1 - Nombre\n" +
                            "2 - Genero\n" +
                            "3 - Categoría\n" +
                            "4 - Años Trabajados");
                    opcion = sc.nextInt();
                    switch (opcion) {
                        case 0:
                            System.out.println("Saliendo");
                            break;
                        case 1:
                            System.out.println("Introduce el nuevo nombre:");
                            String nombre = sc.next();
                            st.execute("UPDATE empleados set nombre ='" + nombre + "' where dni='" + dni + "'");
                            break;
                        case 2:
                            System.out.println("Introduce nuevo genero:");
                            char genero = sc.next().charAt(0);
                            st.execute("UPDATE empleados set genero ='" + genero + "' where dni='" + dni + "'");
                            break;
                        case 3:
                            System.out.println("Introduce nueva categoria:");
                            int categoria = sc.nextInt();
                            st.execute("UPDATE empleados set categoria =" + categoria + " where dni='" + dni + "'");
                            break;
                        case 4:
                            System.out.println("Introduce nuevos años trabajados:");
                            int anyos_trabajados = sc.nextInt();
                            st.execute("UPDATE empleados set anyos_trabajados ='" + anyos_trabajados + "' where dni='" + dni + "'");
                            break;
                        default:
                            System.out.println("Introduce un numero valido.");
                            break;

                    }
                } while (opcion != 0);
                ResultSet rs2 = st.executeQuery("Select * from empleados where dni='" + dni + "'");
                while (rs2.next()) {
                    String dni2 = rs2.getString(1);
                    String nombre = rs2.getString(2);
                    String sexo = rs2.getString(3);
                    int categoria = rs2.getInt(4);
                    int anyos = rs2.getInt(5);

                    Empleado e = new Empleado(dni2, nombre, sexo.charAt(0), categoria, anyos);
                    e.imprime();
                }


            }

        } catch (SQLException exception) {
            exception.printStackTrace();
            System.out.println("Error al conectar a la base de datos.");
        }
    }

    public void copiaSeguridad(String archivo) {
        List<Empleado> empleados = obtieneEmpleados();
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivo))) {
            bw.write("DNI, NOMBRE, SEXO, CATEGORIA, AÑOS TRABAJADOS, SUELDO\n");
            for (Empleado e : empleados) {
                String linea = String.format("%s, %s, %c, %d, %d, %.2f",
                        e.dni,
                        e.nombre,
                        e.sexo,
                        e.getCategoria(),
                        e.anyos,
                        obtieneSueldo(e.dni));
                bw.write(linea);
                bw.newLine();
            }
            System.out.println("Copia de seguridad realizada en " + archivo);
        } catch (IOException e) {
            System.err.println("Error al escribir el archivo de copia de seguridad: " + e.getMessage());

        }
    }
}

