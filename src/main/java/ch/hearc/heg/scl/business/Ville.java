package ch.hearc.heg.scl.business;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Ville {
    private int numero;
    private Integer openWeatherMapId;       // id
    private String nom;                     // name
    private Pays pays;                      // sys->country, voir aussi la classe Pays
    private Double latitude;                // coord->lat
    private Double longitude;               // coord->lon
    private Integer timezone;               // timezone, décalage horaire en secondes par rapport à UTC
    private Map<Long, Mesure> mesures;      // Liste des mesures pour cette ville, la clé est le datetime de la mesure

    public Ville() {
        this.mesures = new HashMap<>();
    }

    public Ville(int numero, Integer openWeatherMapId, String nom, Pays pays, Double latitude, Double longitude, Integer timezone) {
        this.numero = numero;
        this.openWeatherMapId = openWeatherMapId;
        this.nom = nom;
        this.pays = pays;
        this.latitude = latitude;
        this.longitude = longitude;
        this.timezone = timezone;

        this.mesures = new HashMap<>();
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public Integer getOpenWeatherMapId() {
        return openWeatherMapId;
    }

    public void setOpenWeatherMapId(Integer openWeatherMapId) {
        this.openWeatherMapId = openWeatherMapId;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Pays getPays() {
        return pays;
    }

    public void setPays(Pays pays) {
        this.pays = pays;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Integer getTimezone() {
        return timezone;
    }

    public void setTimezone(Integer timezone) {
        this.timezone = timezone;
    }

    public Map<Long, Mesure> getMesures() {
        return mesures;
    }

    public void addMesure(Mesure mesure) {
        // Ajoute la mesure à la liste des mesures, la clé est le datetime de la mesure et doit être unique
        try {
            if (this.mesures.containsKey(mesure.getDateTime())) {
                throw new Exception("La mesure existe déjà pour cette date");
            }else {
                System.out.println("La mesure a été ajoutée");
                this.mesures.put(mesure.getDateTime(), mesure);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Mesure> getMesuresList() {
        return (List<Mesure>) mesures.values();
    }

    public Mesure getMesure(long dateTime) {
        return mesures.get(dateTime);
    }

    public Mesure getDerniereMesure() {
        // La dernière mesure est celle qui a le datetime le plus grand et donc la clé la plus grande
        long max = 0;
        Mesure derniereMesure = null;
        for (Map.Entry<Long, Mesure> entry : mesures.entrySet()) {
            if (entry.getKey() > max) {
                max = entry.getKey();
                derniereMesure = entry.getValue();
            }
        }
        return derniereMesure;
    }

    public void setMesures(Map<Long, Mesure> mesures) {
        this.mesures = mesures;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Ville)) return false;
        Ville ville = (Ville) o;
        return numero == ville.numero && Objects.equals(openWeatherMapId, ville.openWeatherMapId) && Objects.equals(nom, ville.nom) && Objects.equals(pays, ville.pays) && Objects.equals(latitude, ville.latitude) && Objects.equals(longitude, ville.longitude) && Objects.equals(timezone, ville.timezone) && Objects.equals(mesures, ville.mesures);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numero, openWeatherMapId, nom, pays, latitude, longitude, timezone, mesures);
    }

    @Override
    public String toString() {
        return "Ville{" +
                "numero=" + numero +
                ", openWeatherMapId=" + openWeatherMapId +
                ", nom='" + nom + '\'' +
                ", pays=" + pays +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", timezone=" + timezone +
                ", mesures=" + mesures +
                '}';
    }
}
