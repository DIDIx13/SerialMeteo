package ch.hearc.heg.scl.dao;

import ch.hearc.heg.scl.business.Vent;
import ch.hearc.heg.scl.database.DBDataSource;
import oracle.jdbc.OraclePreparedStatement;
import oracle.jdbc.OracleTypes;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Classe permettant d'interagir avec la table VENT de la base de données,
 * en fournissant des méthodes pour la création, la recherche, la mise à jour
 * et la suppression des enregistrements de vents.
 */
public class VentDAO { // #TODO: Supprimer les méthodes inutiles

    /**
     * Crée un nouveau vent dans la base de données.
     *
     * @param vent Le vent à créer.
     * @return L'identifiant attribué au nouveau vent dans la base de données, ou -1 en cas d'erreur.
     */
    public static long create(final Vent vent) {
        long idVent = -1;
        String sql = "INSERT INTO VENT (VITESSE, DIRECTION) VALUES (?, ?) returning NUMERO into ?";
        try (Connection c = DBDataSource.getJDBCConnection();
             OraclePreparedStatement pstmt = (OraclePreparedStatement) c.prepareStatement(sql)) {
            pstmt.setDouble(1, vent.getVitesse());
            pstmt.setDouble(2, vent.getDirection());
            pstmt.registerReturnParameter(3, OracleTypes.NUMBER);
            pstmt.executeUpdate();
            try (ResultSet rs = pstmt.getReturnResultSet()) {
                if (rs.next()) {
                    idVent = rs.getLong(1);
                }
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return idVent;
    }

    /**
     * Recherche et renvoie un vent par son identifiant unique.
     *
     * @param idVent L'identifiant unique du vent à rechercher.
     * @return Le vent correspondant à l'identifiant spécifié, ou null si aucun vent n'est trouvé.
     */
    public static Vent findVentById(final long idVent) {
        Vent vent = null;
        String sql = "SELECT * FROM VENT WHERE NUMERO = ?";
        try (Connection c = DBDataSource.getJDBCConnection();
             OraclePreparedStatement pstmt = (OraclePreparedStatement) c.prepareStatement(sql)) {
            pstmt.setLong(1, idVent);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    vent = new Vent();
                    vent.setNumero(rs.getInt("NUMERO"));
                    vent.setVitesse(rs.getDouble("VITESSE"));
                    vent.setDirection(rs.getDouble("DIRECTION"));
                }
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return vent;
    }

    /**
     * Met à jour les informations d'un vent dans la base de données.
     *
     * @param vent Le vent à mettre à jour.
     */
    public static void update(final Vent vent) {
        String sql = "UPDATE VENT SET VITESSE = ?, DIRECTION = ? WHERE NUMERO = ?";
        try (Connection c = DBDataSource.getJDBCConnection();
             OraclePreparedStatement pstmt = (OraclePreparedStatement) c.prepareStatement(sql)) {
            pstmt.setDouble(1, vent.getVitesse());
            pstmt.setDouble(2, vent.getDirection());
            pstmt.setLong(3, vent.getNumero());
            pstmt.executeUpdate();
            c.commit();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    /**
     * Supprime un vent de la base de données.
     *
     * @param idVent L'identifiant du vent à supprimer.
     * @return true si la suppression est réussie, false sinon.
     */
    public static boolean delete(final long idVent) {
        String sql = "DELETE FROM VENT WHERE NUMERO = ?";
        try (Connection c = DBDataSource.getJDBCConnection();
             OraclePreparedStatement pstmt = (OraclePreparedStatement) c.prepareStatement(sql)) {
            pstmt.setLong(1, idVent);
            int affectedRows = pstmt.executeUpdate();
            c.commit();
            return affectedRows > 0;
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            return false;
        }
    }
}
