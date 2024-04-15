package ch.hearc.heg.scl.business;

import java.util.Date;
import java.util.Objects;

public class Mesure {
    private int numero;
    private Date date;                          // dt                           ( exprimé en secondes depuis le 01.01.1970 , new Date(dt * 1000) )
    private String description;                 // weather[0]->description      ( texte )
    private Double temperature;                 // main->temp                   ( exprimé en °C )
    private Double temperature_ressentie;       // main->feels_like             ( exprimé en °C )
    private Double pression;                    // main->pressure               ( exprimé en hPa )
    private Double humidite;                    // main->humidity               ( exprimé en %, rapport entre la quantité de vapeur d'eau dans l'air et la quantité maximale avant saturation)
    private Vent vent;                          // wind                         ( voir la classe vent )

    public Mesure() {
    }

    public Mesure(int numero, Date date, String description, Double temperature, Double temperature_ressentie, Double pression, Double humidite, Vent vent) {
        this.numero = numero;
        this.date = date;
        this.description = description;
        this.temperature = temperature;
        this.temperature_ressentie = temperature_ressentie;
        this.pression = pression;
        this.humidite = humidite;
        this.vent = vent;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public Date getDate() {
        return date;
    }

    // Convertit une date en secondes
    public Long getDateTime() {
        return date.getTime() / 1000;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    // Convertit des secondes en date
    public void setDateTime(Long date) {
        this.date = new Date(date * 1000);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public Double getTemperature_ressentie() {
        return temperature_ressentie;
    }

    public void setTemperature_ressentie(Double temperature_ressentie) {
        this.temperature_ressentie = temperature_ressentie;
    }

    public Double getPression() {
        return pression;
    }

    public void setPression(Double pression) {
        this.pression = pression;
    }

    public Double getHumidite() {
        return humidite;
    }

    public void setHumidite(Double humidite) {
        this.humidite = humidite;
    }

    public Vent getVent() {
        return vent;
    }

    public void setVent(Vent vent) {
        this.vent = vent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Mesure)) return false;
        Mesure mesure = (Mesure) o;
        return numero == mesure.numero && Double.compare(temperature, mesure.temperature) == 0 && Double.compare(temperature_ressentie, mesure.temperature_ressentie) == 0 && Double.compare(pression, mesure.pression) == 0 && Double.compare(humidite, mesure.humidite) == 0 && Objects.equals(date, mesure.date) && Objects.equals(description, mesure.description) && Objects.equals(vent, mesure.vent);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numero, date, description, temperature, temperature_ressentie, pression, humidite, vent);
    }

    @Override
    public String toString() {
        return "Mesure{" +
                "numero=" + numero +
                ", date=" + date +
                ", description='" + description + '\'' +
                ", temperature=" + temperature +
                ", temperature_ressentie=" + temperature_ressentie +
                ", pression=" + pression +
                ", humidite=" + humidite +
                ", vent=" + vent +
                '}';
    }
}
