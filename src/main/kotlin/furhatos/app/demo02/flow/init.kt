package furhatos.app.demo02.flow

import furhatos.app.demo02.setting.distanceToEngage
import furhatos.app.demo02.setting.maxNumberOfUsers
import furhatos.app.demo02.flow.main.general.Idle
import furhatos.autobehavior.enableSmileBack
import furhatos.autobehavior.setDefaultMicroexpression
import furhatos.flow.kotlin.*
import furhatos.flow.kotlin.voice.PollyVoice

//Im State "Init" werden Parameter des Skills vor jedem Start initialisiert. States werden als Werte definiert.
//Die Klammer hinter "state" regelt die Vererbung von anderen States. In diesem Fall erbt der State vom State "Parent".

val Init : State = state() {
    init {

        //Der dialogLogger sorgt dafür, dass die Gespräche mit Furhat unter
        //dem Pfad: C:\Users\Benutzer\.furhat\logs protokolliert werden.
        dialogLogger.startSession()

        //Mit furhat.voice wird die NLU ausgewählt, die Furhat für die Interaktion verwenden soll.
        furhat.voice = PollyVoice.Hans()

        //Mikroexpressionen sind kleine Gesichtsausdrücke, die kontinuierlich während eines Skill ablaufen,
        //so wirkt Furhat lebendiger. von "facialMovements = true" wird abgeraten, da Furhat das
        //Gesicht sonst als zu unruhig wahrgenommen werden könnte, wenn man ohnehin schon mit furhat.gestures arbeitet.
        furhat.setDefaultMicroexpression(blinking = true, facialMovements = false, eyeMovements = true)

        //furhat.enableSmileBack = true sorgt dafür, dass Furhat zurücklächelt, wenn der Gesprächspartner lacht.
        furhat.enableSmileBack = true

        //Mit users.setSimpleEngagementPolicy werden von Furhat Robotics definierte Default-Werte gesetzt.
        //Die Defaultwerte umfassen zum Beispiel die maximale Anzahl an zugelassenen Gesprächspartnern.
        users.setSimpleEngagementPolicy(distanceToEngage, maxNumberOfUsers)

        //Mit dem goto() Befehl definiert man welcher State der Anwendung als nächstes eingeleitet wird.
        goto(Idle)
    }
}
