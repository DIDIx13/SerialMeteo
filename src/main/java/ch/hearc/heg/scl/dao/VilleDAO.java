package ch.hearc.heg.scl.dao;

import ch.hearc.heg.scl.business.Pays;
import ch.hearc.heg.scl.business.Ville;
import ch.hearc.heg.scl.database.DBDataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import oracle.jdbc.OraclePreparedStatement;
import oracle.jdbc.OracleTypes;

/**
 * Classe permettant d'interagir avec la table VILLE de la base de données,
 * en fournissant des méthodes pour la création, la recherche, la mise à jour
 * et la suppression des enregistrements de villes.
 */
public class VilleDAO { // #TODO: Supprimer les méthodes inutiles

    /**
     * Crée une nouvelle ville dans la base de données.
     *
     * @param v La ville à créer.
     * @return Le numéro attribué à la nouvelle ville dans la base de données, ou -1 en cas d'erreur.
     */
    public static long create(final Ville v) {
        Connection c = null;
        OraclePreparedStatement pstmt = null;
        ResultSet rs;
        long rNumero = -1;
        try {
            c = DBDataSource.getJDBCConnection();
            String sql = "INSERT INTO VILLE (OPENWEATHERMAPID, NOM, NUM_PAYS, LATITUDE, LONGITUDE, TIMEZONE) VALUES (?, ?, ?, ?, ?, ?) returning NUMERO into ?";
            pstmt = (OraclePreparedStatement) c.prepareStatement(sql);
            pstmt.setLong(1, v.getOpenWeatherMapId());
            pstmt.setString(2, v.getNom());
            pstmt.setLong(3, v.getPays().getNumero());
            pstmt.setDouble(4, v.getLatitude());
            pstmt.setDouble(5, v.getLongitude());
            pstmt.setLong(6, v.getTimezone());
            pstmt.registerReturnParameter(7, OracleTypes.NUMBER);
            pstmt.executeUpdate();
            rs = pstmt.getReturnResultSet();
            while (rs.next()) {
                rNumero = rs.getLong(1);
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return rNumero;
    }

    /**
     * Recherche et renvoie toutes les villes de la base de données.
     *
     * @return Une liste contenant toutes les villes présentes dans la base de données.
     */
    public static List<Ville> research() {
        List<Ville> listVilles = new ArrayList<>();
        Connection cnx = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            cnx = DBDataSource.getJDBCConnection();
            String sql = "SELECT NUMERO, OPENWEATHERMAPID, NOM, NUM_PAYS, LATITUDE, LONGITUDE, TIMEZONE FROM VILLE";
            stmt = cnx.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Ville v = new Ville();
                v.setNumero(rs.getInt("numero"));
                v.setOpenWeatherMapId(rs.getInt("openWeatherMapId"));
                v.setNom(rs.getString("nom"));

                // Récupération de l'objet Pays associé à la ville
                long numPays = rs.getLong("num_pays");
                Pays pays = PaysDAO.findPaysById(numPays);
                v.setPays(pays);

                v.setLatitude(rs.getDouble("latitude"));
                v.setLongitude(rs.getDouble("longitude"));
                v.setTimezone(rs.getInt("timezone"));
                listVilles.add(v);
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return listVilles;
    }

    /**
     * Recherche et renvoie une ville par son identifiant unique.
     *
     * @param idVille L'identifiant unique de la ville à rechercher.
     * @return La ville correspondant à l'identifiant spécifié, ou null si elle n'existe pas.
     */
    public static Ville findVilleById(final long idVille) {
        // Vu que je n'arrive pas à faire fonctionne le PAYS_TRG je vais créer findVilleByUniqueAttributes à la place de findVilleById pour le moment, sinon j'ai des duplicata pour je ne sais quel raison
        Ville v = null;
        Connection cnx = null;
        OraclePreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            cnx = DBDataSource.getJDBCConnection();
            String sql = "SELECT NUMERO, OPENWEATHERMAPID, NOM, NUM_PAYS, LATITUDE, LONGITUDE, TIMEZONE FROM VILLE WHERE NUMERO = ?";
            pstmt = (OraclePreparedStatement) cnx.prepareStatement(sql);
            pstmt.setLong(1, idVille);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                v = new Ville();
                v.setNumero(rs.getInt("numero"));
                v.setOpenWeatherMapId(rs.getInt("openWeatherMapId"));
                v.setNom(rs.getString("nom"));

                // Récupération de l'objet Pays associé à la ville
                long numPays = rs.getLong("num_pays");
                Pays pays = PaysDAO.findPaysById(numPays);
                v.setPays(pays);

                v.setLatitude(rs.getDouble("latitude"));
                v.setLongitude(rs.getDouble("longitude"));
                v.setTimezone(rs.getInt("timezone"));
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return v;
    }

    /**
     * Recherche et renvoie une ville par ses attributs uniques (nom, latitude, longitude).
     *
     * @param nom       Le nom de la ville.
     * @param latitude  La latitude de la ville.
     * @param longitude La longitude de la ville.
     * @return La ville correspondant aux attributs spécifiés, ou null si elle n'existe pas.
     */
    public static Ville findVilleByUniqueAttributes(final String nom, final double latitude, final double longitude) {
        Ville ville = null;
        Connection cnx = null;
        OraclePreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            cnx = DBDataSource.getJDBCConnection();
            String sql = "SELECT NUMERO, OPENWEATHERMAPID, NOM, NUM_PAYS, LATITUDE, LONGITUDE, TIMEZONE FROM VILLE WHERE NOM = ? AND LATITUDE = ? AND LONGITUDE = ?";
            pstmt = (OraclePreparedStatement) cnx.prepareStatement(sql);
            pstmt.setString(1, nom);
            pstmt.setDouble(2, latitude);
            pstmt.setDouble(3, longitude);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                ville = new Ville();
                ville.setNumero(rs.getInt("NUMERO"));
                ville.setOpenWeatherMapId(rs.getInt("OPENWEATHERMAPID"));
                ville.setNom(rs.getString("NOM"));

                long numPays = rs.getLong("NUM_PAYS");
                Pays pays = PaysDAO.findPaysById(numPays);
                ville.setPays(pays); // Définit l'objet Pays associé à la ville

                ville.setLatitude(rs.getDouble("LATITUDE"));
                ville.setLongitude(rs.getDouble("LONGITUDE"));
                ville.setTimezone(rs.getInt("TIMEZONE"));
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return ville;
    }

    /**
     * Met à jour les informations d'une ville dans la base de données.
     *
     * @param v La ville à mettre à jour.
     */
    public static void update(final Ville v) {
        Connection cnx = null;
        OraclePreparedStatement pstmt = null;
        try {
            cnx = DBDataSource.getJDBCConnection();
            String sql = "UPDATE VILLE SET NOM = ?, NUM_PAYS = ?, LATITUDE = ?, LONGITUDE = ?, TIMEZONE = ? WHERE NUMERO = ?";
            pstmt = (OraclePreparedStatement) cnx.prepareStatement(sql);
            pstmt.setString(1, v.getNom());
            pstmt.setLong(2, v.getPays().getNumero());
            pstmt.setDouble(3, v.getLatitude());
            pstmt.setDouble(4, v.getLongitude());
            pstmt.setLong(5, v.getTimezone());
            pstmt.setLong(6, v.getNumero());
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    /**
     * Supprime une ville de la base de données.
     *
     * @param v La ville à supprimer.
     * @return true si la suppression est réussie, false sinon.
     */
    public static boolean delete(final Ville v) {
        Connection cnx = null;
        OraclePreparedStatement pstmt = null;
        try {
            cnx = DBDataSource.getJDBCConnection();
            String sql = "DELETE FROM VILLE WHERE NUMERO = ?";
            pstmt = (OraclePreparedStatement) cnx.prepareStatement(sql);
            pstmt.setLong(1, v.getNumero());
            pstmt.executeUpdate();
            return true;
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            return false;
        }
    }
}
