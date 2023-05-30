import furhatos.nlu.Intent

//Erklärung: Vergleiche Klasse "nlu.Ja"
class Danke : Intent() {

    override fun getExamples(lang: furhatos.util.Language): List<String> {
        return listOf(
            "Vielen Dank",
            "Danke, gleichfalls",
            "Danke",
            "Gleichfalls",
            "ebenfalls",
            "den wünsche ich auch"
        )
    }
}

