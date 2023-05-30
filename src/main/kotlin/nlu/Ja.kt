package nlu

import furhatos.nlu.Intent


//Die Klassen des typs intents definieren einzelne Intensionen. Sagt ein Kunde also "nlu.Ja" oder "Genau"
//stellen diese beiden Aussagen trotzdem die gleichen Intensionen dar.
//In Intents sammelt man also Ausdrucksweisen, die man unter die gleichen intensionen zusammenfassen kann.
//Das ermöglicht, dass Furhat die intension "ja" verstehen kann, obwohl der Gesprächspartner beispielsweise
//"Das ist richtig" gesagt hat
class Ja : Intent() {

    override fun getExamples(lang: furhatos.util.Language): List<String> {
        return listOf(
            "nlu.Ja", "Genau", "Steinau", "Richtig", "Korrekt", "So ist es", "Das ist richtig",
            "Yes", "Auf jeden Fall", "Ganz Genau", "nlu.Ja, so ist es", "Genau richtig", "ja, bin ich",
            "ja, das ist wahr", "Jap","habt ihr einen", "ja", "doch", "jawohl", "ja klar", "aber sicher",
            "sicher doch", "klar", "Janna", "Pia", "Peer", "Jara", "zu holen", "zu bringen",
            "ja ich bringe jemanden", "ja ich hole jemanden", "nlu.Ja, stimmt", "Das stimmt", "Exakt",
            "zu bringen ja", "nia"
        )
    }
}


