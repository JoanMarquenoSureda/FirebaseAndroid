package com.example.activitat2uf02;

/**
 * Columnes de la BBDD: Nom, Cognoms, Telèfon, Marca Vehicle, Model Vehicle i Matrícula
 */
public class Vehicle {

    private String nom;
    private String cognoms;
    private String telefon;
    private String marcaVehicle;
    private String modelVehicle;
    private String matricula;

    public Vehicle(String matricula, String nom, String cognoms, String telefon, String marcaVehicle, String modelVehicle) {
        this.nom = nom;
        this.cognoms = cognoms;
        this.telefon = telefon;
        this.marcaVehicle = marcaVehicle;
        this.modelVehicle = modelVehicle;
        this.matricula = matricula;
    }

    public Vehicle(String nom, String cognoms, String telefon, String marcaVehicle, String modelVehicle) {
        this.nom = nom;
        this.cognoms = cognoms;
        this.telefon = telefon;
        this.marcaVehicle = marcaVehicle;
        this.modelVehicle = modelVehicle;
    }

    public Vehicle() {
        this.nom = "";
        this.cognoms = "";
        this.telefon = "";
        this.marcaVehicle = "";
        this.modelVehicle = "";
        this.matricula = "";
    }



    public String getNom() {
        return nom;
    }

    public String getCognoms() {
        return cognoms;
    }

    public String getTelefon() {
        return telefon;
    }

    public String getMarcaVehicle() {
        return marcaVehicle;
    }

    public String getModelVehicle() {
        return modelVehicle;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setCognoms(String cognoms) {
        this.cognoms = cognoms;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

    public void setMarcaVehicle(String marcaVehicle) {
        this.marcaVehicle = marcaVehicle;
    }

    public void setModelVehicle(String modelVehicle) {
        this.modelVehicle = modelVehicle;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }


    @Override
    public String toString() {

        return "Vehicle{" +
                "nom='" + nom + '\'' +
                ", cognoms='" + cognoms + '\'' +
                ", telefon='" + telefon + '\'' +
                ", marcaVehicle='" + marcaVehicle + '\'' +
                ", modelVehicle='" + modelVehicle + '\'' +
                ", matricula='" + matricula + '\'' +
                '}';
    }


}
