import furhatos.nlu.Intent

//Erklärung: Vergleiche Klasse "nlu.Ja"

class Nein : Intent() {

    override fun getExamples(lang: furhatos.util.Language): List<String> {
        return listOf(
            "Nein", "Nei", "Nain", "Fein", "rein", "sein", "ne", "nein auf gar keinen fall", "Nicht korrekt",
            "Falsch", "Das stimmt nicht so ganz", "Nein, ist falsch", "nope", "Falsch, das stimmt nicht", "Nö",
            "Maja", "Mara", "na", "naja", "na ja", "möchte ich nicht sagen"
        )
    }
}

