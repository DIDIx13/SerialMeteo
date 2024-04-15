package ch.hearc.heg.scl.dao;

import ch.hearc.heg.scl.business.Mesure;
import ch.hearc.heg.scl.database.DBDataSource;
import oracle.jdbc.OraclePreparedStatement;
import oracle.jdbc.OracleTypes;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 * Classe permettant d'interagir avec la table MESURE de la base de données,
 * en fournissant des méthodes pour la création, la recherche, la mise à jour
 * et la suppression des enregistrements de mesures météorologiques.
 */
public class MesureDAO { // #TODO: Supprimer les méthodes inutiles

    /**
     * Crée une nouvelle mesure météorologique dans la base de données pour une ville donnée.
     *
     * @param mesure La mesure météorologique à créer.
     * @param idVille L'identifiant de la ville associée à la mesure.
     * @return L'identifiant attribué à la nouvelle mesure dans la base de données, ou -1 en cas d'erreur.
     */
    public static long create(final Mesure mesure, final int idVille) {
        long idMesure = -1;
        String sql = "INSERT INTO MESURE (DT, DESCRIPTION, TEMPERATURE, TEMPERATURE_RESSENTIE, PRESSION, HUMIDITE, NUM_VILLE) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?) returning NUMERO into ?";
        try (Connection c = DBDataSource.getJDBCConnection();
             OraclePreparedStatement pstmt = (OraclePreparedStatement) c.prepareStatement(sql)) {
            pstmt.setTimestamp(1, new Timestamp(mesure.getDate().getTime()));
            pstmt.setString(2, mesure.getDescription());
            pstmt.setDouble(3, mesure.getTemperature());
            pstmt.setDouble(4, mesure.getTemperature_ressentie());
            pstmt.setDouble(5, mesure.getPression());
            pstmt.setDouble(6, mesure.getHumidite());
            pstmt.setInt(7, idVille);
            pstmt.registerReturnParameter(8, OracleTypes.NUMBER);
            pstmt.executeUpdate();
            try (ResultSet rs = pstmt.getReturnResultSet()) {
                if (rs.next()) {
                    idMesure = rs.getLong(1);
                }
            }
        } catch (SQLException ex) {
            System.out.println("Erreur lors de la création de la mesure : " + ex.getMessage());
        }
        return idMesure;
    }

    /**
     * Recherche et renvoie une mesure météorologique par son identifiant unique.
     *
     * @param idMesure L'identifiant unique de la mesure à rechercher.
     * @return La mesure météorologique correspondant à l'identifiant spécifié, ou null si aucune mesure n'est trouvée.
     */
    public static Mesure findMesureById(final long idMesure) {
        Mesure mesure = null;
        String sql = "SELECT * FROM MESURE WHERE NUMERO = ?";
        try (Connection c = DBDataSource.getJDBCConnection();
             OraclePreparedStatement pstmt = (OraclePreparedStatement) c.prepareStatement(sql)) {
            pstmt.setLong(1, idMesure);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    mesure = new Mesure();
                    mesure.setNumero(rs.getInt("NUMERO"));
                    mesure.setDate(rs.getTimestamp("DT"));
                    mesure.setDescription(rs.getString("DESCRIPTION"));
                    mesure.setTemperature(rs.getDouble("TEMPERATURE"));
                    mesure.setTemperature_ressentie(rs.getDouble("TEMPERATURE_RESSENTIE"));
                    mesure.setPression(rs.getDouble("PRESSION"));
                    mesure.setHumidite(rs.getDouble("HUMIDITE"));
                    // Vent est traité séparément dans VentDAO
                }
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return mesure;
    }

    /**
     * Met à jour les informations d'une mesure météorologique dans la base de données.
     *
     * @param mesure La mesure météorologique à mettre à jour.
     */
    public static void update(final Mesure mesure) {
        String sql = "UPDATE MESURE SET DT = ?, DESCRIPTION = ?, TEMPERATURE = ?, TEMPERATURE_RESSENTIE = ?, PRESSION = ?, HUMIDITE = ? WHERE NUMERO = ?";
        try (Connection c = DBDataSource.getJDBCConnection();
             OraclePreparedStatement pstmt = (OraclePreparedStatement) c.prepareStatement(sql)) {
            pstmt.setTimestamp(1, new Timestamp(mesure.getDate().getTime()));
            pstmt.setString(2, mesure.getDescription());
            pstmt.setDouble(3, mesure.getTemperature());
            pstmt.setDouble(4, mesure.getTemperature_ressentie());
            pstmt.setDouble(5, mesure.getPression());
            pstmt.setDouble(6, mesure.getHumidite());
            pstmt.setLong(7, mesure.getNumero());

            pstmt.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("Erreur lors de la mise à jour de la mesure : " + ex.getMessage());
        }
    }

    /**
     * Supprime une mesure météorologique de la base de données.
     *
     * @param idMesure L'identifiant de la mesure à supprimer.
     * @return true si la suppression est réussie, false sinon.
     */
    public static boolean delete(final long idMesure) {
        String sql = "DELETE FROM MESURE WHERE NUMERO = ?";
        try (Connection c = DBDataSource.getJDBCConnection();
             OraclePreparedStatement pstmt = (OraclePreparedStatement) c.prepareStatement(sql)) {
            pstmt.setLong(1, idMesure);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            return false;
        }
    }

    /**
     * Vérifie si une mesure météorologique existe déjà pour une ville donnée à une date donnée.
     *
     * @param mesure La mesure météorologique à vérifier.
     * @param idVille L'identifiant de la ville associée à la mesure.
     * @return true si la mesure existe déjà pour la ville à la date spécifiée, false sinon.
     */
    public static boolean mesureExists(final Mesure mesure, final int idVille) {
        boolean exists = false;
        String sql = "SELECT COUNT(*) FROM MESURE WHERE DT = ? AND NUM_VILLE = ?"; // Pas sûr que ça soit la meilleure façon de vérifier
        try (Connection c = DBDataSource.getJDBCConnection();
             OraclePreparedStatement pstmt = (OraclePreparedStatement) c.prepareStatement(sql)) {
            pstmt.setTimestamp(1, new Timestamp(mesure.getDate().getTime()));
            pstmt.setInt(2, idVille);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    exists = true;
                }
            }
        } catch (SQLException ex) {
            System.out.println(
                    "Erreur lors de la vérification de l'existence de la mesure : "
                    + ex.getMessage());
        }
        return exists;
    }
}
