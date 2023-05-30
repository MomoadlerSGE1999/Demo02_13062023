package furhatos.app.demo02.flow

import furhat.libraries.standard.UsersLib.usersLib
import furhatos.app.demo02.flow.main.general.Idle
import furhatos.flow.kotlin.*

val Parent: State = state {

    //Verlässt ein User die Sichtweite Furhat's, so triggert onUserLeave.
    onUserLeave(instant = true) {
        //Furhat verabschiedet den User.
        furhat.say("Hat mich gefreut Sie kennenzulernen")
        //Eine Sekunde Verzögerung mit delay(1000)
        delay(1000)
        //Ist der users.count > 0, so schaut furhat dem ihn nächsten user an.
        if(users.count > 0){
            furhat.usersLib.attendClosestUser()
        }
        when {
            //Wenn der users.count = 0 ist, dann goto(Idle).
            users.count == 0 -> goto(Idle)
            //Triggert Zeile 13 nicht, so wird ein anderer User zum users.current (aktuellen Nutzer)
            //und kann somit mit "it" angesprochen werden.
            it == users.current -> furhat.attend(users.other)
        }

    }

    onUserEnter(instant = true) {
        //Wird ein neuer User von Furhat erkannt, schaut Furhat diesen kurz an.
        furhat.glance(it)
    }
}

