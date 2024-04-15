package ch.hearc.heg.scl.web;

import ch.hearc.heg.scl.business.Pays;
import ch.hearc.heg.scl.business.Ville;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsoner;

import java.math.BigDecimal;

public class CountryService {
    private static final String BASE_URL = "https://db.ig.he-arc.ch/ens/cfr/pays/code?CODE=";

    private static String getJSON(String code) {
        String service = BASE_URL + code;
        return Generic.getWebResponse(service);
    }

    public static Pays getData(String code) {
        String webResponse = getJSON(code);
        Pays pays = new Pays();

        try {
            JsonObject deserialize = (JsonObject) Jsoner.deserialize(webResponse);

            BigDecimal numero = (BigDecimal) deserialize.get("numero");
            pays.setNumero(numero.intValue());

            pays.setCode(code);

            String nom = (String) deserialize.get("nom");
            pays.setNom(nom);
        }catch(Exception ex){
            ex.printStackTrace();
            System.out.println("Erreur de désérialisation : " + ex.getMessage());
        }finally {
            return pays;
        }
    }
}
