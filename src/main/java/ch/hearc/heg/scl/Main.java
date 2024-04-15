package ch.hearc.heg.scl;

import ch.hearc.heg.scl.business.Mesure;
import ch.hearc.heg.scl.business.Ville;
import ch.hearc.heg.scl.dao.MesureDAO;
import ch.hearc.heg.scl.dao.VentDAO;
import ch.hearc.heg.scl.dao.VilleDAO;
import ch.hearc.heg.scl.database.DBDataSource;
import ch.hearc.heg.scl.io.Saisie;
import ch.hearc.heg.scl.web.OpenWeatherMapService;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {
    private static Map<Integer, Ville> villes;

    public static void main(String[] args) {
        villes = new HashMap<>();

        Scanner clavier = new Scanner(System.in);

        boolean exit = false;

        while (!exit) {
            int choice;
            System.out.println("---------------------------------");
            System.out.println("Que voulez-vous faire ?");
            System.out.println("(1) Saisir une nouvelle ville");
            System.out.println("(2) Afficher les villes de la map");
            System.out.println("(3) Tester la connexion à la base de données");
            System.out.println("(4) Sortir du programme");
            System.out.println();
            System.out.print("Mon choix : ");

            // Boucle pour gérer les erreurs de saisie
            do {
                try {
                    choice = Integer.parseInt(clavier.nextLine());
                    if (choice < 1 || choice > 4) {
                        throw new IllegalArgumentException();
                    }
                    break;
                } catch (IllegalArgumentException e) {
                    System.out.println("Erreur de saisie. Veuillez saisir un nombre entre 1 et 4.");
                    System.out.print("Mon choix : ");
                }
            } while (true);

            switch (choice) {
                case 1:
                    saisieVille();
                    break;
                case 2:
                    afficherVilles();
                    break;
                case 3:
                    testerConnexionDB();
                    break;
                case 4:
                    exit = true;
                    break;
            }
        }
    }

    private static void afficherVilles(){
        System.out.println("Affichage des villes");

        //Afficher un message d'erreur si il n'y a pas de villes dans la Map
        if(villes.isEmpty()){
            System.out.println("Il n'y a pas de villes dans la Map");
        }else{
        for (Ville ville : villes.values()) {
            System.out.println(ville);
            }
        }
    }

    private static void saisieVille() {
        System.out.println("Saisie et affichage d'une ville");

        // Saisie d'une ville
        Ville ville = Saisie.saisirLatitudeLongitude();

        Ville WebResponse = OpenWeatherMapService.getData(ville);

        // Affichage de la réponse
        System.out.println(WebResponse);

        // Vérifie si la ville existe déjà dans la base de données
        Ville villeExistante = VilleDAO.findVilleByUniqueAttributes(WebResponse.getNom(), WebResponse.getLatitude(), WebResponse.getLongitude());

        if (villeExistante == null) {
            // Si la ville n'existe pas, insère la nouvelle ville
            long idVille = VilleDAO.create(WebResponse);
            if (idVille != -1) {
                WebResponse.setNumero((int) idVille); // Mise à jour de l'ID de la ville avec l'ID généré
                System.out.println("Nouvelle ville insérée dans la base de données.");
            } else {
                System.out.println("Erreur lors de l'insertion de la ville.");
                return;
            }
        } else {
            // Si la ville existe déjà, utilise l'ID existant
            WebResponse.setNumero(villeExistante.getNumero());
            System.out.println("La ville existe déjà dans la base de données.");
        }

        try {
            ajouterMesureEtVent(WebResponse);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        // Met à jour la collection locale des villes avec les nouvelles données
        villes.putIfAbsent(WebResponse.getOpenWeatherMapId(), WebResponse);
    }

    private static void ajouterMesureEtVent(Ville ville) throws Exception {
        Mesure derniereMesure = ville.getDerniereMesure();

        boolean mesureExists = MesureDAO.mesureExists(derniereMesure, ville.getNumero());
        if (!mesureExists) {
            // Insère le vent associé à la mesure
            long idVent = VentDAO.create(derniereMesure.getVent());
            if (idVent != -1) {
                derniereMesure.getVent().setNumero((int)idVent); // Mise à jour avec l'ID généré
            } else {
                throw new Exception("Erreur lors de l'insertion du vent.");
            }

            // Insère la mesure maintenant que le vent est traité
            long idMesure = MesureDAO.create(derniereMesure, ville.getNumero());
            if (idMesure == -1) {
                throw new Exception("Erreur lors de l'insertion de la mesure.");
            }
            System.out.println("Mesure ajoutée avec succès.");
        } else {
            throw new Exception("Une mesure similaire existe déjà pour cette ville.");
        }
    }


    private static void testerConnexionDB(){
        System.out.println("Test de la connexion à la base de données");

        try (Connection conn = DBDataSource.getJDBCConnection()) {
            System.out.println("Connexion à la base de données réussie");
        } catch (SQLException e) {
            System.out.println("Erreur lors de la connexion à la base de données : " + e.getMessage());
        }
    }
}