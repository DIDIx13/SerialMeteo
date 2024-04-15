package ch.hearc.heg.scl.business;

import java.util.Objects;

public class Vent {
    private int numero;
    private Double vitesse;         // Wind->Speed  (exprimé en m/s)
    private Double direction;       // Wind->Deg    (exprimé en degré par rapport au nord, désigne la direction de l'origine du vent)

    public Vent(int numero, Double vitesse, Double direction) {
        this.numero = numero;
        this.vitesse = vitesse;
        this.direction = direction;
    }

    public Vent() {
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public Double getVitesse() {
        return vitesse;
    }

    public void setVitesse(Double vitesse) {
        this.vitesse = vitesse;
    }

    public Double getDirection() {
        return direction;
    }

    public void setDirection(Double direction) {
        this.direction = direction;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vent)) return false;
        Vent vent = (Vent) o;
        return numero == vent.numero && Objects.equals(vitesse, vent.vitesse) && Objects.equals(direction, vent.direction);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numero, vitesse, direction);
    }

    @Override
    public String toString() {
        return "Vent{" +
                "numero=" + numero +
                ", vitesse=" + vitesse +
                ", direction=" + direction +
                '}';
    }
}
