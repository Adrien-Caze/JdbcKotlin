import jdbc.BDD
import jdbc.dao.EtudiantDAO

val db = BDD()
val etudiantDAO = EtudiantDAO()
fun main() {
    db.getConnection()
    val etudiants = etudiantDAO.findAll()
    for (etudiant in etudiants) {
        println("${etudiant.id} => ${etudiant.nom}  ${etudiant.prenom}")
    }


    db.close()
}
