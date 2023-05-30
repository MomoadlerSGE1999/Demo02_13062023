package furhatos.app.demo02.flow.main.taxi

import FrageWiederholen
import nlu.Ja
import ReadExcel
import furhatos.app.demo02.flow.Parent
import furhatos.app.demo02.flow.main.general.Benutzer
import furhatos.app.demo02.flow.main.general.Idle
import furhatos.flow.kotlin.*
import furhatos.flow.kotlin.voice.Voice
import furhatos.gestures.Gestures
import furhatos.nlu.common.No

val ValidierungNummerKundealt : State = state(Parent) {

    onEntry {
        //Hier fragt Furhat den Gesprächspartner, ob seine verstandene Patientennummer korrekt ist.
        furhat.ask {
            +"Die Patientennummer Ihres Angehörigen beziehungsweise Kunden ist"
            //Durch "Voice.SayAsType.DIGITS" spricht Furhat die Patientennummer als 5 separate Ziffern.
            +voice!!.sayAs("${Benutzer!!.get("Patientennummer")}", Voice.SayAsType.DIGITS)
            +", stimmt das?"
            furhat.gesture(Gestures.BigSmile)
            }
        }

    onResponse<Ja> {
        //Der User, der antwortet, kann mit "it" angesprochen werden, da "it" durch den onResponse-Handler
        //bereits gesetzt ist.
        furhat.attend(it.userId)

        //Benutzer wird für die Funktion ReadExcelTaxi gesetzt
        Benutzer = users.getUser(it.userId)

            ReadExcel(Benutzer!!)
        //Hat der Benutzer keinen Termin, wird das field "Row" des Benutzers mit -1 beschrieben und die If-Bedingung
        //trifft zu. Sollte der Benutzer keinen Termin haben wird er darüber informiert. Die Interaktion endet dann.
            if (Benutzer!!.get("row") == -1) {
                furhat.say(
                    "Ihr Angehöriger beziehungsweise Kunde steht leider nicht auf dem Belegungsplan welcher mir " +
                            "vorliegt, bitte fragen Sie am Empfang nach. Ich wünsche Ihnen einen schönen Tag"
                )
                goto(Idle)
            }
            //Ist das field "row" nicht -1, so läuft die interaktion weiter mit dem State "Taxidriverdialogue01".
            else goto(Taxidriverdialogue01)
    }
    onResponse<No> {
        furhat.attend(it.userId)
        //Ist die Patientennummer falsch, so wird im State WennNummerFalsch das field patientennummer genullt und Furhat
        //fragt erneut nach der Patientennummer.
        goto(WennNummerFalschTaxifahrer)
    }
    onResponse<FrageWiederholen> {
        furhat.attend(it.userId)
        reentry()
    }
}



