package ch.hearc.heg.scl.dao;

import ch.hearc.heg.scl.business.Pays;
import ch.hearc.heg.scl.database.DBDataSource;
import oracle.jdbc.OraclePreparedStatement;
import oracle.jdbc.OracleTypes;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Classe permettant d'interagir avec la table VILLE de la base de données,
 * en fournissant des méthodes pour la création, la recherche, la mise à jour
 * et la suppression des enregistrements de villes.
 */
public class PaysDAO { // #TODO: Supprimer les méthodes inutiles

    /**
     * Crée un nouveau pays dans la base de données.
     *
     * @param p Le pays à créer.
     * @return Le numéro attribué au nouveau pays dans la base de données, ou -1 en cas d'erreur.
     */
    public static long create(final Pays p) {
        Connection c = null;
        OraclePreparedStatement pstmt = null;
        ResultSet rs = null;
        long rNumero = -1;
        try {
            c = DBDataSource.getJDBCConnection();
            String sql = "INSERT INTO PAYS (CODE, NOM) VALUES (?, ?) returning NUMERO into ?";
            pstmt = (OraclePreparedStatement) c.prepareStatement(sql);
            pstmt.setString(1, p.getCode());
            pstmt.setString(2, p.getNom());
            pstmt.registerReturnParameter(3, OracleTypes.NUMBER);
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
     * Recherche et renvoie un pays par son identifiant unique.
     *
     * @param paysId L'identifiant unique du pays à rechercher.
     * @return Le pays correspondant à l'identifiant spécifié, ou null si aucun pays n'est trouvé.
     */
    public static Pays findPaysById(final long paysId) {
        Pays p = null;
        Connection c = null;
        OraclePreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            c = DBDataSource.getJDBCConnection();
            String sql = "SELECT NUMERO, CODE, NOM FROM PAYS WHERE NUMERO = ?";
            pstmt = (OraclePreparedStatement) c.prepareStatement(sql);
            pstmt.setLong(1, paysId);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                p = new Pays();
                p.setNumero(rs.getInt("NUMERO"));
                p.setCode(rs.getString("CODE"));
                p.setNom(rs.getString("NOM"));
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return p;
    }

    /**
     * Met à jour les informations d'un pays dans la base de données.
     *
     * @param p Le pays à mettre à jour.
     */
    public static void update(final Pays p) {
        Connection c = null;
        OraclePreparedStatement pstmt = null;
        try {
            c = DBDataSource.getJDBCConnection();
            String sql = "UPDATE PAYS SET CODE = ?, NOM = ? WHERE NUMERO = ?";
            pstmt = (OraclePreparedStatement) c.prepareStatement(sql);
            pstmt.setString(1, p.getCode());
            pstmt.setString(2, p.getNom());
            pstmt.setLong(3, p.getNumero());
            pstmt.executeUpdate();
            c.commit();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    /**
     * Supprime un pays de la base de données.
     *
     * @param numero Le numéro du pays à supprimer.
     * @return true si la suppression est réussie, false sinon.
     */
    public static boolean delete(final long numero) {
        Connection c = null;
        OraclePreparedStatement pstmt = null;
        try {
            c = DBDataSource.getJDBCConnection();
            String sql = "DELETE FROM PAYS WHERE NUMERO = ?";
            pstmt = (OraclePreparedStatement) c.prepareStatement(sql);
            pstmt.setLong(1, numero);
            int affectedRows = pstmt.executeUpdate();
            c.commit();
            return affectedRows > 0;
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            return false;
        }
    }
}
