import jdbc.BDD
import jdbc.dao.EtudiantDAO
import jdbc.dao.MatiereDAO
import jdbc.dao.NoteDAO
import jdbc.entity.Etudiant
import jdbc.entity.Note
import java.time.LocalDate
import kotlin.collections.iterator
import kotlin.io.iterator
import kotlin.text.iterator

val db = BDD()
val etudiantDAO = EtudiantDAO()
val noteDAO = NoteDAO()
val matieres = MatiereDAO()



fun main() {
    db.getConnection()

    try {
        afficherMenu()
    } finally {
        db.close()
    }
}





fun afficherMenu() {
    var choix: String

    do {
        println()
        println("================================")
        println("     Application Evaluation")
        println("================================")
        println("s - Sélectionner un étudiant")
        println("a - Ajouter un étudiant")
        println("n - Gestion des notes")
        println("q - Quitter")

        choix = readln().lowercase()

        when (choix) {
            "s" -> selectionnerEtudiant()
            "a" -> ajouterEtudiant()
            "n" -> menuNotes()
            "q" -> println("Au revoir !")
            else -> println("Choix invalide.")
        }
    } while (choix != "q")
}




fun selectionnerEtudiant() {
    afficherEtudiants()
    println("Sélectionner un étudiant :")
    val id = readln().toInt()
    val etudiant = etudiantDAO.findById(id)
    if (etudiant == null) {
        println("Étudiant introuvable.")
        return
    }
    afficherEtudiant(etudiant)
    afficherMenuEtudiant(etudiant)
}


fun afficherEtudiants() {

    val etudiants = etudiantDAO.findAll()
    println()
    println("Liste des étudiants")
    println("--------------------")
    for (etudiant in etudiants) {
        println("${etudiant.id} => ${etudiant.nom} ${etudiant.prenom}")
    }
}


fun afficherEtudiant(etudiant: Etudiant) {

    println()
    println("Étudiant sélectionné")
    println("--------------------")
    println("Nom : ${etudiant.nom}")
    println("Prénom : ${etudiant.prenom}")
    println("Email : ${etudiant.email}")
    println("Date de naissance : ${etudiant.dateNaissance}")
}


fun afficherMenuEtudiant(etudiant: Etudiant) {

    println()
    println("----------------------------")
    println("1 - Supprimer")
    println("2 - Modifier")
    println("3 - Retour")
    val choix = readln()
    when (choix) {
        "1" -> supprimerEtudiant(etudiant)
        "2" -> modifierEtudiant(etudiant)
        "3" -> println("Retour au menu principal.")
        else -> println("Choix invalide.")
    }
}


fun supprimerEtudiant(etudiant: Etudiant) {

    val id = etudiant.id
    if (id == null) {
        println("Impossible de supprimer cet étudiant.")
        return
    }
    etudiantDAO.deleteById(id)
    println("Étudiant supprimé.")
}

fun modifierEtudiant(etudiant: Etudiant) {

    println("Nom :")
    etudiant.nom = readln()
    println("Prénom :")
    etudiant.prenom = readln()
    println("Email :")
    etudiant.email = readln()
    println("Date de naissance (AAAA-MM-JJ) :")
    etudiant.dateNaissance = LocalDate.parse(readln())
    etudiantDAO.save(etudiant)
    println("Étudiant modifié.")
}

fun ajouterEtudiant() {

    println()
    println("Ajout d'un étudiant")
    println("-------------------")
    println("Nom :")
    val nom = readln()
    println("Prénom :")
    val prenom = readln()
    println("Email :")
    val email = readln()
    println("Date de naissance (AAAA-MM-JJ) :")
    val dateNaissance = LocalDate.parse(readln())
    val etudiant = Etudiant(null,
        nom,
        prenom,
        email,
        dateNaissance
    )
    etudiantDAO.save(etudiant)

    println("Étudiant ajouté avec l'id ${etudiant.id}.")
}

/*===================================================================================//
                                       Notes
//===================================================================================*/



fun afficherNotes() {
    val listeNotes = noteDAO.findAll()

    println()
    println("Liste des notes")
    println("----------------------------------------")

    if (listeNotes.isEmpty()) {
        println("Aucune note enregistrée.")
        return
    }

    for (note in listeNotes) {
        println(
            "${note.id} => Étudiant : ${note.etudiant_id} | " +
                    "Note : ${note.note} | " +
                    "Type : ${note.typeEvaluation} | " +
                    "Date : ${note.dateEvaluation}"
        )
    }
}



fun afficherNote(note: Note) {
    println()
    println("Note sélectionnée")
    println("----------------------------")
    println("Identifiant : ${note.id}")
    println("Étudiant : ${note.etudiant_id}")
    println("Note : ${note.note}")
    println("Type : ${note.typeEvaluation}")
    println("Date : ${note.dateEvaluation}")
}


fun selectionnerNote() {
    afficherNotes()

    println("Identifiant de la note (0 pour annuler) :")
    val id = readln().toIntOrNull()

    if (id == null) {
        println("Identifiant invalide.")
        return
    }

    if (id == 0) return

    val note = noteDAO.findById(id)

    if (note == null) {
        println("Note introuvable.")
        return
    }

    afficherNote(note)
    afficherMenuNote(note)
}



fun menuNotes() {
    while (true) {
        println()
        println("========== GESTION DES NOTES ==========")
        println("1 - Afficher toutes les notes")
        println("2 - Sélectionner une note")
        println("3 - Ajouter une note")
        println("4 - Retour")

        when (readln()) {
            "1" -> afficherNotes()
            "2" -> selectionnerNote()
            "3" -> ajouterNote()
            "4" -> return
            else -> println("Choix invalide.")
        }
    }
}




fun supprimerNote(note: Note) {
    val id = note.id

    if (id == null) {
        println("Impossible de supprimer cette note.")
        return
    }

    if (noteDAO.deleteById(id)) {
        println("Note supprimée.")
    } else {
        println("Échec de la suppression.")
    }
}


fun modifierEtudiant(etudiant: Note) {

    println("ID Etudiant :")
    etudiant.etudiant_id = readln().toInt()
    println("Note :")
    etudiant.note = readln().toFloat()
    println("Type Evaluation :")
    etudiant.typeEvaluation = readln()
    println("Date de naissance (AAAA-MM-JJ) :")
    etudiant.dateEvaluation = LocalDate.parse(readln())
    etudiantDAO.save()
    println("Étudiant modifié.")
}


fun ajouterNote() {
    println()
    println("Ajout d'une note")
    println("----------------------------")

    println("Identifiant de l'étudiant :")
    val etudiantId = readln().toIntOrNull()

    if (etudiantId == null || etudiantDAO.findById(etudiantId) == null) {
        println("Étudiant introuvable ou identifiant invalide.")
        return
    }

    println("Identifiant de la matière :")
    val matiereId = readln().toIntOrNull()

    if (matiereId == null || matiereId <= 0) {
        println("Identifiant invalide.")
        return
    }

    println("Note sur 20 :")
    val valeur = readln().toFloatOrNull()

    if (valeur == null || valeur !in 0f..20f) {
        println("La note doit être comprise entre 0 et 20.")
        return
    }

    println("Type d'évaluation :")
    val type = readln().trim()

    if (type.isBlank()) {
        println("Le type d'évaluation est obligatoire.")
        return
    }

    println("Date d'évaluation (AAAA-MM-JJ) :")
    val date = try {
        LocalDate.parse(readln())
    } catch (e: Exception) {
        println("Date invalide.")
        return
    }

    val nouvelleNote = Note(
        id = null,
        etudiant_id = etudiantId,
        note = valeur,
        typeEvaluation = type,
        dateEvaluation = date
    )

    if (noteDAO.save(nouvelleNote) != null) {
        println("Note ajoutée avec l'identifiant ${nouvelleNote.id}.")
    } else {
        println("Échec de l'ajout de la note.")
    }
}




