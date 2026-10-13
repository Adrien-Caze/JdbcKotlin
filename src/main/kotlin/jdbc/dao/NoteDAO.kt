package jdbc.dao
import db
import jdbc.BDD
import jdbc.entity.Note
import java.sql.PreparedStatement
import java.sql.SQLException
import java.sql.Statement

class NoteDAO(val bdd : BDD = db){

    fun findAll(): MutableList<Note> {
        val result = mutableListOf<Note>()/* Création de la variable qui contiendra le resultat de la recherche */
        val sql = "SELECT * FROM note "/* Requête SQL pour afficher tout les colonnes de la Table etudiant */
        val requetePreparer = bdd.connectionBDD!!.prepareStatement(sql)
        val resultatRequete = bdd.executePreparedStatement(requetePreparer)
        if (resultatRequete != null) {
            while (resultatRequete.next()) {
                val id = resultatRequete.getInt("id")
                val etudiant = resultatRequete.getInt("etudiant_id")
                val note = resultatRequete.getFloat("note")
                val type = resultatRequete.getString("type_evaluation")
                val date = resultatRequete.getDate("date_evaluation").toLocalDate()
                result.add(Note(id,etudiant,note,type,date))
            }
        }/* Si le résultat est non null, nous récuperons les infos présentes dans la table pour les stocker dans la variable result */


        requetePreparer.close()
        return result
    }

    fun findById(id: Int): Note? {
        var result: Note? = null
        val sql = "SELECT * FROM note WHERE id = ?"
        val requetePreparer = bdd.connectionBDD!!.prepareStatement(sql)
        requetePreparer.setInt(1, id) // insere la valeur de l'id dans la requete preparer
        val resultatRequete = bdd.executePreparedStatement(requetePreparer)


        if (resultatRequete != null && resultatRequete.next()) {
            val id = resultatRequete.getInt("id")
            val etudiant = resultatRequete.getInt("etudiant_id")
            val note = resultatRequete.getFloat("note")
            val type = resultatRequete.getString("type_evaluation")
            val date = resultatRequete.getDate("date_evaluation").toLocalDate()
            result = (Note(id,etudiant,note,type,date))

        }


        requetePreparer.close()
        return result
    }

    fun save(note: Note): Note? {
        val requetePreparer: PreparedStatement
        if (note.id == null) {
            // Insertion
            val sql = """
            INSERT INTO note (etudiant_id, matiere_id, note, date_evaluation, type_evaluation)
            VALUES (?, ?, ?, ?, ?)
            """.trimIndent()

            requetePreparer = bdd.connectionBDD!!.prepareStatement(sql,
                Statement.RETURN_GENERATED_KEYS
            )
            requetePreparer.setInt(1, note.etudiant_id)
            requetePreparer.setFloat(2, note.note)
            requetePreparer.setString(3, note.typeEvaluation)
            requetePreparer.setDate(
                4,
                java.sql.Date.valueOf(note.dateEvaluation)
            )
            val nbLigneMaj = requetePreparer.executeUpdate()
            if (nbLigneMaj > 0) {
                val generatedKeys = requetePreparer.generatedKeys
                if (generatedKeys.next()) {
                    note.id = generatedKeys.getInt(1)
                }
                generatedKeys.close()
                requetePreparer.close()
                return note
            }

        } else {
            // Mise à jour
            val sql = """
                UPDATE note
                SET etudiant_id = ?, matiere_id = ?, note = ?, date_evaluation = ?, type_evaluation = ?
                WHERE id = ?
            """.trimIndent()


            requetePreparer = bdd.connectionBDD!!.prepareStatement(sql)


            requetePreparer.setInt(1, note.etudiant_id)
            requetePreparer.setFloat(2, note.note)
            requetePreparer.setString(3, note.typeEvaluation)
            requetePreparer.setDate(
                4,
                java.sql.Date.valueOf(note.dateEvaluation)
            )
            requetePreparer.setInt(5, note.id!!)


            val nbLigneMaj = requetePreparer.executeUpdate()


            requetePreparer.close()


            if (nbLigneMaj > 0) {
                return note
            }
        }


        requetePreparer.close()
        return null
    }

    fun deleteById(id: Int): Boolean {
        val sql = "DELETE FROM note WHERE id = ?"
        val requetePreparer = bdd.connectionBDD!!.prepareStatement(sql)
        requetePreparer.setInt(1, id)


        return try {
            val nbLigneMaj = requetePreparer.executeUpdate()
            requetePreparer.close()
            nbLigneMaj > 0
        } catch (erreur: SQLException) {
            println("Erreur lors de la suppression de la note : ${erreur.message}")
            false
        }
    }

    fun saveAll(entraineurs: Collection<Note>): MutableList<Note> {
        val result = mutableListOf<Note>()
        for (e in entraineurs) {
            val sauvegarde = save(e)
            if (sauvegarde != null) result.add(sauvegarde)
        }
        return result
    }
}