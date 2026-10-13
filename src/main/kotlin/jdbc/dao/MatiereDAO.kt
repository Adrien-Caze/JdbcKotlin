package jdbc.dao

import db
import jdbc.BDD
import jdbc.entity.Etudiant
import jdbc.entity.Matiere

class MatiereDAO (val bdd : BDD = db) {

    fun findAll(): MutableList<Matiere> {
        val result = mutableListOf<Matiere>()/* Création de la variable qui contiendra le resultat de la recherche */
        val sql = "SELECT * FROM matiere "/* Requête SQL pour afficher tout les colonnes de la Table etudiant */
        val requetePreparer = bdd.connectionBDD!!.prepareStatement(sql)
        val resultatRequete = bdd.executePreparedStatement(requetePreparer)
        if (resultatRequete != null) {
            while (resultatRequete.next()) {
                val id = resultatRequete.getInt("id")
                val nom = resultatRequete.getString("nom")
                val coefficient = resultatRequete.getFloat("coefficient")
                result.add(Matiere(id,nom,coefficient))
            }
        }/* Si le résultat est non null, nous récuperons les infos présentes dans la table pour les stocker dans la variable result */


        requetePreparer.close()
        return result
    }

    fun findById(id: Int): Matiere? {
        var result: Matiere? = null
        val sql = "SELECT * FROM matiere WHERE id = ?"
        val requetePreparer = bdd.connectionBDD!!.prepareStatement(sql)
        requetePreparer.setInt(1, id) // insere la valeur de l'id dans la requete preparer
        val resultatRequete = bdd.executePreparedStatement(requetePreparer)

        if (resultatRequete != null && resultatRequete.next()) {
            val id = resultatRequete.getInt("id")
            val nom = resultatRequete.getString("nom")
            val coefficient = resultatRequete.getFloat("coefficient")
            result = Matiere(id,nom,coefficient)
        }


        requetePreparer.close()
        return result
    }
}