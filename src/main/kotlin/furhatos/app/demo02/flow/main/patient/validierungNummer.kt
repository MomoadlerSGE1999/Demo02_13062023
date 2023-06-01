
package furhatos.app.demo02.flow.main.patient

import FrageWiederholen
import nlu.Ja
import Nein
import ReadExcel
import furhatos.app.demo02.flow.Parent
import furhatos.app.demo02.flow.main.general.Benutzer
import furhatos.app.demo02.flow.main.general.Idle
import furhatos.flow.kotlin.*
import furhatos.flow.kotlin.voice.Voice
import furhatos.gestures.Gestures
import furhatos.skills.UserManager.current

val ValidierungNummerKunde : State = state(Parent) {

    onEntry {
        furhat.attend(users.current)

        //Hier fragt Furhat den gesprächspartner, ob seine verstandene Patientennummer korrekt ist.
        furhat.ask {
            +"Die Patientennummer ist"
            //Durch "Voice.SayAsType.DIGITS" spricht Furhat dabei die Patientennummer als 5 separate Ziffern.
            +voice!!.sayAs("${Benutzer!!.get("Patientennummer")}", Voice.SayAsType.DIGITS)
            +", stimmt das?"
            furhat.gesture(Gestures.BigSmile)
        }

    }

        onResponse<Ja> {

            //Die Funktion ReadExcel wird aufgerufen und auf den Benutzer angewendet.
            ReadExcel(Benutzer!!, this.furhat)

            //Hat der Benutzer keinen Termin, wird das field "Kein Termin" des Benutzers mit -1 beschrieben und die If-Bedingung
            //trifft zu. Sollte der Benutzer keinen Termin haben wird er darüber informiert, die Interaktion endet dann.
            if (Benutzer!!.get("raum") == null) {
                furhat.say(
                    "Sie stehen leider nicht auf dem Belegungsplan welcher mir vorliegt, bitte fragen Sie am" +
                            " Empfang nach. Ich wünsche Ihnen einen schönen Tag"
                )
                goto(Idle)
            }
            //Ist das field "row" nicht -1, so läuft die interaktion weiter mit dem State "Patientdialogue".
            else goto(Informationsdialogue)
        }

        onResponse<Nein> {
            furhat.attend(user= current)
            //Ist die Patientennummer falsch, so wird im State WennNummerFalsch das field patientennummer genullt und
            //Furhat fragt erneut nach der Patientennummer.
            goto(WennNummerFalschPatient)
        }
        onResponse<FrageWiederholen> {
        furhat.attend(user= current)
        reentry()
        }
    }




