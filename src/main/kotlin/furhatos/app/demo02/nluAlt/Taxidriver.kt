package furhatos.app.demo02.nluAlt

import furhatos.nlu.Intent
//Erklärung: Vergleiche Klasse "nlu.Ja"

class Taxidriver : Intent() {
    //var name: String = ""
    //var vorname: String = ""
    override fun getExamples(lang: furhatos.util.Language): List<String> {
        return listOf("Taxi driver", "Taxi", "Driver", "Taxifahrer", "Fahrer", "ich bin Taxifahrer", "Ich bin Cara")
    }
    /*fun getFullName(): String =
        "$vorname $name"
*/
}