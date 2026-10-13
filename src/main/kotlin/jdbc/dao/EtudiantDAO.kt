package jdbc.dao
import db
import jdbc.BDD
import jdbc.entity.Etudiant
import java.sql.PreparedStatement
import java.sql.SQLException
import java.sql.Statement

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

    fun findById(id: Int): Etudiant? {
        var result: Etudiant? = null
        val sql = "SELECT * FROM etudiant WHERE id = ?"
        val requetePreparer = bdd.connectionBDD!!.prepareStatement(sql)
        requetePreparer.setInt(1, id) // insere la valeur de l'id dans la requete preparer
        val resultatRequete = bdd.executePreparedStatement(requetePreparer)


        if (resultatRequete != null && resultatRequete.next()) {
            val nom = resultatRequete.getString("nom")
            val prenom = resultatRequete.getString("prenom")
            val email = resultatRequete.getString("email")
            val dateNaissance = resultatRequete.getDate("date_naissance")
            result = Etudiant(id,nom,prenom,email,dateNaissance.toLocalDate())
        }


        requetePreparer.close()
        return result
    }

    fun save(etudiant: Etudiant): Etudiant? {
        val requetePreparer: PreparedStatement
        if (etudiant.id == null) {
            // Insertion
            val sql = """
            INSERT INTO etudiant (nom, prenom, email, date_naissance)
            VALUES (?, ?, ?, ?)
            """.trimIndent()

            requetePreparer = bdd.connectionBDD!!.prepareStatement(sql,
                Statement.RETURN_GENERATED_KEYS
            )
            requetePreparer.setString(1, etudiant.nom)
            requetePreparer.setString(2, etudiant.prenom)
            requetePreparer.setString(3, etudiant.email)
            requetePreparer.setDate(
                4,
                java.sql.Date.valueOf(etudiant.dateNaissance)
            )
            val nbLigneMaj = requetePreparer.executeUpdate()
            if (nbLigneMaj > 0) {
                val generatedKeys = requetePreparer.generatedKeys
                if (generatedKeys.next()) {
                    etudiant.id = generatedKeys.getInt(1)
                }
                generatedKeys.close()
                requetePreparer.close()
                return etudiant
            }

        } else {
            // Mise à jour
            val sql = """
                UPDATE etudiant
                SET nom = ?, prenom = ?, email = ?, date_naissance = ?
                WHERE id = ?
            """.trimIndent()


       requetePreparer = bdd.connectionBDD!!.prepareStatement(sql)


       requetePreparer.setString(1, etudiant.nom)
       requetePreparer.setString(2, etudiant.prenom)
       requetePreparer.setString(3, etudiant.email)
       requetePreparer.setDate(
           4,
           java.sql.Date.valueOf(etudiant.dateNaissance)
       )
       requetePreparer.setInt(5, etudiant.id!!)


       val nbLigneMaj = requetePreparer.executeUpdate()


       requetePreparer.close()


       if (nbLigneMaj > 0) {
           return etudiant
       }
   }


   requetePreparer.close()
   return null
}

    fun deleteById(id: Int): Boolean {
        val sql = "DELETE FROM etudiant WHERE id = ?"
        val requetePreparer = bdd.connectionBDD!!.prepareStatement(sql)
        requetePreparer.setInt(1, id)


        return try {
            val nbLigneMaj = requetePreparer.executeUpdate()
            requetePreparer.close()
            nbLigneMaj > 0
        } catch (erreur: SQLException) {
            println("Erreur lors de la suppression de l'étudiant : ${erreur.message}")
            false
        }
    }

    fun saveAll(entraineurs: Collection<Etudiant>): MutableList<Etudiant> {
        val result = mutableListOf<Etudiant>()
        for (e in entraineurs) {
            val sauvegarde = save(e)
            if (sauvegarde != null) result.add(sauvegarde)
        }
        return result
    }


}