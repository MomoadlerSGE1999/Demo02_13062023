package furhatos.app.demo02.flow.main.general

import AngehoerigeUndTaxifahrer
import FrageWiederholen
import nlu.Ja
import Nein
import ReadExcel
import furhatos.app.demo02.flow.Parent
import furhatos.app.demo02.flow.main.patient.ValidierungNummerKunde
import furhatos.app.demo02.nluAlt.QRCode
import furhatos.app.demo02.nluAlt.Taxidriver
import furhatos.flow.kotlin.*
import furhatos.flow.kotlin.voice.Voice
import furhatos.gestures.Gestures
import furhatos.records.User
import furhatos.skills.UserManager.current


var Benutzer: User? = null

//Der State Greeting erbt von dem State Parent, dort ist das User-Handling definiert.
val Greeting : State = state(Parent) {
    onEntry {

        //Zu Beginn des States wird definiert, dass Furhat den aktuellen User weiterhin anschaut.
        furhat.attend(users.current)

        furhat.ask {
            //Furhat stellt eine Frage und zieht während der Frage seine Brauen hoch, async = flase sorgt dafür,
            //dass der State erst nach Beendigung der Gesture weiterläuft
            +"Haben Sie einen QR-Code oder sind Sie Taxifahrer"
            furhat.gesture(Gestures.BrowRaise, async = false)
        }
    }
    onResponse<Taxidriver> {
        furhat.attend(user= current)
        furhat.say {
            //mit ${furhat.voice.emphasis("Ihnen")} kann Furhat einzelne Abschnitte betonen.
            +"Gut, dann kann ich ${furhat.voice.emphasis("Ihnen")} weiterhelfen."
            furhat.gesture(Gestures.BigSmile, async = false)

        }
        //Nun wird die Variable Benutzer mit dem User überschrieben, der auf dei Frage geantwortet hat.
        Benutzer = users.getUser(it.userId)

        //Der Benutzer wird von Furhat angeschaut.
        furhat.attend(user= current)

        //Mit der Funktion GetDigitsPatient wird die Frage nach der Patientennummer des Gesprächspartners gestellt.
        //User kann nicht mehr null sein deswegen Benutzer!!, da der Benutzer bereits gesetzt.
        GetDigitsKunde(Benutzer!!, this.furhat, "Patientennummer")
        goto(ValidierungNummerKunde)

    }

    onResponse<QRCode>{
        Benutzer = users.current
        furhat.say("Okay, dann zeig mal her")
        qrCodeScan13(Benutzer!!)
        Benutzer!!.put("Patientennummer", Benutzer!!.get("QR Code Text"))
        furhat.say("${Benutzer!!.get("Patientennummer")}")
        furhat.say("${Benutzer!!.get("QR Code Text")}")
        ReadExcel(Benutzer!!)
        val raumy: Any? = Benutzer!!.get("raum")
        val platzx: Any? = Benutzer!!.get("platz")
        val dialysebeginn: Any? = furhat.voice.sayAs(Benutzer!!.get("dialysebeginn").toString(), Voice.SayAsType.TIME)
        val dialyseende: Any? = furhat.voice.sayAs(Benutzer!!.get("dialyseende").toString(), Voice.SayAsType.TIME)

        //Der Nutzer wird über seine Termindaten informiert und weiß somit, wann, wo und wie lange seine Dialyse
        //stattfinden wird.
        furhat.say (
            "Gut, ${Benutzer!!.get("name")}. Ich würde Sie ${furhat.voice.emphasis("bittten")} in " +
                    "den${furhat.voice.emphasis("$raumy")} an den  PLatz ${furhat.voice.emphasis("$platzx")} " +
                    "zu gehen. Ihre Dialyse fängt um $dialysebeginn an und endet um $dialyseende"
        )
        goto(Idle)
    }

    onResponse<Nein>  {
        furhat.attend(user= current)
        goto(AngehoerigeUndTaxifahrer)
    }
    onResponse<FrageWiederholen> {
        furhat.attend(user= current)
        reentry()
    }
    onResponseFailed {
        reentry()
    }
}




