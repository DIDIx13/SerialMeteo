package ch.hearc.heg.scl.io;

import ch.hearc.heg.scl.business.Mesure;
import ch.hearc.heg.scl.business.Pays;
import ch.hearc.heg.scl.business.Vent;
import ch.hearc.heg.scl.business.Ville;

import java.util.Scanner;

public class Saisie {
    private static Scanner clavier;

    // Initialisation du clavier
    private static void initClavier() {
        if(clavier == null) {
            clavier = new Scanner(System.in);
        }
    }

    // Saisie d'une Ville et d'une Mesure
    public static Ville saisirVille() {
        Ville ville = new Ville();

        initClavier();

        // Saisie de l'openWeatherMapId
        System.out.print("OpenWeatherMapId: ");
        ville.setOpenWeatherMapId(Integer.parseInt(clavier.nextLine()));

        // Saisie du nom de la ville
        System.out.print("Nom de la ville: ");
        ville.setNom(clavier.nextLine());

        // Saisie de la latitude
        System.out.print("Latitude: ");
        ville.setLatitude(Double.parseDouble(clavier.nextLine()));

        // Saisie de la longitude
        System.out.print("Longitude: ");
        ville.setLongitude(Double.parseDouble(clavier.nextLine()));

        // Saisie du timezone
        System.out.print("Timezone (en secondes): ");
        ville.setTimezone(Integer.parseInt(clavier.nextLine()));

        // Saisie du pays
        ville.setPays(saisirPays());

        // Saisie de la mesure
        ville.addMesure(saisirMesure());

        return ville;
    }

    // Saisie d'un Pays
    public static Pays saisirPays() {
        Pays pays = new Pays();

        initClavier();

        // Saisie du code du pays
        System.out.print("Code du pays: ");
        pays.setCode(clavier.nextLine());

        // Saisie du nom du pays
        System.out.print("Nom du pays: ");
        pays.setNom(clavier.nextLine());

        return pays;
    }

    // Saisie d'une Mesure
    public static Mesure saisirMesure() {
        Mesure mesure = new Mesure();

        initClavier();

        // Saisie de la date de la mesure
        System.out.print("Datetime de la mesure (en secondes): ");
        mesure.setDateTime(Long.parseLong(clavier.nextLine()));

        // Saisie de la description de la mesure
        System.out.print("Description de la mesure: ");
        mesure.setDescription(clavier.nextLine());

        // Saisie de la température
        System.out.print("Température: ");
        mesure.setTemperature(Double.parseDouble(clavier.nextLine()));

        // Saisie de la température ressentie
        System.out.print("Température ressentie: ");
        mesure.setTemperature_ressentie(Double.parseDouble(clavier.nextLine()));

        // Saisie de la pression
        System.out.print("Pression: ");
        mesure.setPression(Double.parseDouble(clavier.nextLine()));

        // Saisie de l'humidité
        System.out.print("Humidité: ");
        mesure.setHumidite(Double.parseDouble(clavier.nextLine()));

        // Saisie du vent
        mesure.setVent(saisirVent());

        return mesure;
    }

    // Saisie d'un Vent
    public static Vent saisirVent() {
        Vent vent = new Vent();

        initClavier();

        // Saisie de la vitesse du vent
        System.out.print("Vitesse du vent: ");
        vent.setVitesse(Double.parseDouble(clavier.nextLine()));

        // Saisie de la direction du vent
        System.out.print("Direction du vent: ");
        vent.setDirection(Double.parseDouble(clavier.nextLine()));

        return vent;
    }

    // Saisie d'une latitude et d'une longitude
    public static Ville saisirLatitudeLongitude() {
        Ville ville = new Ville();

        initClavier();

        // Saisie de la latitude
        System.out.print("Latitude: ");
        ville.setLatitude(Double.parseDouble(clavier.nextLine()));

        // Saisie de la longitude
        System.out.print("Longitude: ");
        ville.setLongitude(Double.parseDouble(clavier.nextLine()));

        return ville;
    }
}
