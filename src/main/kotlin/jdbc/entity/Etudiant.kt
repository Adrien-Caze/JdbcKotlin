package jdbc.entity

import java.time.LocalDate

class Etudiant(val id : Int, var nom : String, var prenom : String, var email : String, val dateNaissance : LocalDate) {

}