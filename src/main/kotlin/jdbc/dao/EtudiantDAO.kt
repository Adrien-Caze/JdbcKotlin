package jdbc.dao
import db
import jdbc.BDD
import jdbc.entity.Etudiant

class EtudiantDAO (val bdd : BDD = db) {

    fun findAll(): MutableList<Etudiant> {
        val result = mutableListOf<Etudiant>()/* Création de la variable qui contiendra le resultat de la recherche */
        val sql = "SELECT * FROM etudiant"/* Requête SQL pour afficher tout les colonnes de la Table etudiant */
        val requetePreparer = bdd.connectionBDD!!.prepareStatement(sql)
        val resultatRequete = bdd.executePreparedStatement(requetePreparer)


        if (resultatRequete != null) {
            while (resultatRequete.next()) {
                val id = resultatRequete.getInt("id")
                val nom = resultatRequete.getString("nom")
                val prenom = resultatRequete.getString("prenom")
                val email = resultatRequete.getString("email")
                val dateNaissance = resultatRequete.getDate("date_naissance")
                result.add(Etudiant(id,nom,prenom,email,dateNaissance.toLocalDate()))
            }
        }/* Si le résultat est non null, nous récuperons les infos présentes dans la table pour les stocker dans la variable result */


        requetePreparer.close()
        return result
    }

    fun findByNom(nom: String): Etudiant? {
        var result: Etudiant? = null
        val sql = "SELECT * FROM etudiant WHERE nom =?"
        val requetePreparer = bdd.connectionBDD!!.prepareStatement(sql)
        requetePreparer.setString(1, nom) // insere la valeur de l'id dans la requete preparer
        val resultatRequete = bdd.executePreparedStatement(requetePreparer)
        if (resultatRequete != null && resultatRequete.next()) {
            val id = resultatRequete.getInt("id")
            val nom = resultatRequete.getString("nom")
            val prenom = resultatRequete.getString("prenom")
            val email = resultatRequete.getString("email")
            val dateNaissance = resultatRequete.getDate("date_naissance")
            result = Etudiant(id,nom,prenom,email,dateNaissance.toLocalDate())
        }


        requetePreparer.close()
        return result
    }

}