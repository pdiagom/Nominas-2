package com.iessoterohernandez.Nominas.Laboral;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

/**
 * La clase {@code Persona} representa a una persona con atributos como
 * DNI, nombre y sexo. Se utiliza como entidad en una base de datos
 * y se beneficia de las anotaciones de Lombok para la generación
 * automática de métodos comunes.
 */
@Entity
@Table(name = "persona")
@Data
@Builder
public class Persona {

    @Id
    @Column(name = "dni")
    public String dni;

    public String nombre;
    public char sexo;

    /**
     * Establece el DNI de la persona.
     *
     * @param dni El nuevo DNI de la persona.
     */
    public void setDni(String dni) {
        this.dni = dni;
    }

    /**
     * Imprime el nombre y el DNI de la persona en la consola.
     */
    public void imprime() {
        System.out.println(nombre + " " + dni);
    }

    /**
     * Constructor que crea una nueva instancia de {@code Persona}
     * con un DNI, nombre y sexo.
     *
     * @param dni El DNI de la persona.
     * @param nombre El nombre de la persona.
     * @param sexo El sexo de la persona ('M' para masculino, 'F' para femenino).
     */
    public Persona(String dni, String nombre, char sexo) {
        this.dni = dni;
        this.nombre = nombre;
        this.sexo = sexo;
    }

    /**
     * Constructor que crea una nueva instancia de {@code Persona}
     * con un nombre y sexo, sin especificar el DNI.
     *
     * @param nombre El nombre de la persona.
     * @param sexo El sexo de la persona ('M' para masculino, 'F' para femenino).
     */
    public Persona(String nombre, char sexo) {
        this.nombre = nombre;
        this.sexo = sexo;
    }
}
