package ch.hearc.heg.scl.business;

import java.util.Objects;

public class Pays {
    private int numero;
    private String code;        // Sys->Country     (code du pays selon la norme ISO-3166)
    private String nom;         // Non disponible dans la réponse OpenWeatherMap (désigne le nom francophone du pays)

    public Pays(int numero, String code, String nom) {
        this.numero = numero;
        this.code = code;
        this.nom = nom;
    }

    public Pays() {
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    @Override
    public String toString() {
        return "Pays{" +
                "numero=" + numero +
                ", code='" + code + '\'' +
                ", nom='" + nom + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Pays)) return false;
        Pays pays = (Pays) o;
        return numero == pays.numero && Objects.equals(code, pays.code) && Objects.equals(nom, pays.nom);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numero, code, nom);
    }
}
