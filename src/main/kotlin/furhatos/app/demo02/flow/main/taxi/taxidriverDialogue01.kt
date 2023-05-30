package furhatos.app.demo02.flow.main.taxi

import Danke
import FrageWiederholen
import nlu.Ja
import Nein
import WelcherPlatzRaum
import furhatos.app.demo02.flow.Parent
import furhatos.app.demo02.flow.main.general.Benutzer
import furhatos.app.demo02.flow.main.general.Idle
import furhatos.flow.kotlin.*
import furhatos.flow.kotlin.voice.Voice
import furhatos.gestures.Gestures
import furhatos.skills.UserManager
import furhatos.skills.UserManager.current

//Vergleiche patientdialogue.kt
val Taxidriverdialogue01 : State = state(Parent){
    onEntry {
        var raumx: Any? = Benutzer!!.get("raum")
        var platzy: Any? = Benutzer!!.get("platz")
        var dialyseende: Any? = furhat.voice.sayAs(Benutzer!!.get("dialyseende").toString(), Voice.SayAsType.TIME)
        var dialysebeginn: Any? = furhat.voice.sayAs(Benutzer!!.get("dialysebeginn").toString(), Voice.SayAsType.TIME)

        furhat.say("Ich würde Sie bitten ihren Kunden " +
                "${Benutzer!!.get("name")} im $raumx am $platzy abzuholen")

        //Verzögerung um eine halbe Sekunde, um die Betonung zu optimieren.
        delay(500)

        furhat.say("Die Dialyse ihres Kunden beginnt um $dialysebeginn und endet um" +
                "$dialyseende ich wünsche Ihnen einen schönen Tag")


        furhat.ledStrip.solid(java.awt.Color.GREEN)
        furhat.gesture(Gestures.Nod())
        furhat.gesture(Gestures.BigSmile)
        furhat.listen(timeout = 10000)
    }
    onResponse<FrageWiederholen> {
        furhat.attend(user= current)
        reentry()
    }
    onResponse<WelcherPlatzRaum> {
        furhat.attend(user= current)
        reentry()
    }
    onNoResponse {
        delay(6000)
        furhat.attendNobody()
        goto(Idle)
    }
    onResponse<Danke> {
        furhat.say("Ich danke ebenfalls, es war mir eine Freude")
        delay(4000)
        goto(Idle)
    }
    onResponse<Ja> {
        goto(Idle)
    }
    onResponse<Nein> {
        goto(Idle)
    }
    onReentry {
        var raumy: Any? = Benutzer!!.get("raum")
        var platzx: Any? = Benutzer!!.get("platz")
        furhat.say(" In den $raumy, an den $platzx")
        goto(Idle)
    }
}


