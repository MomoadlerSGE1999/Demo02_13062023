package furhatos.app.demo02.flow.main.general

import Greetingname
import furhat.libraries.standard.UsersLib.usersLib
import furhatos.app.demo02.flow.Parent
import furhatos.flow.kotlin.*

val Idle: State = state (Parent) {

    //Durch "init{}" wird der Code innerhalb der Klammer nur initial ausgeführt. Durchläuft die Interaktion
    //also zum zweiten Mal den State Idle, so wird der Code innerhalb der Klammer von init nicht mehr ausgelöst
    init {
        furhat.usersLib.attendClosestUser()
        if(users.count > 0)
        {
            //Befinden sich mehr als 0 User in Furhat's Reichweite, so leuchtet der LED Ring Furhat's in Magenta.
            furhat.ledStrip.solid(java.awt.Color.MAGENTA)

            //Furhat schaut den ihm nächsten User an
            furhat.usersLib.attendClosestUser()

            //und geht in den nächsten State "(Greetingname") über
            goto(Greeting)
        }
        //Falls genau 0 User in Furhat's Reichweite sind, schaut er niemanden an.
        else(furhat.attendNobody())
    }
    //Tritt ein User in Sichtweite Furhat's triggert der Befehl onUserEnter
    //Der User wird in OnUserEnter mit "it" definiert.
    onUserEnter {
        furhat.ledStrip.solid(java.awt.Color.GREEN)

        //Furhat schaut dann den User an, der mit "it" definiert ist.
        furhat.attend(it)
            goto(Greeting)
}
}





/*
    /*init {
        furhat.ledStrip.solid(java.awt.Color.GREEN)
        when {
            users.count > 0 -> {
                furhat.attendClosestUser()
                goto(Greetingname)
            }
        }
    }

     */

    onEntry {
        furhat.ledStrip.solid(java.awt.Color.YELLOW)
        when {
            users.count > 0 -> {
                furhat.attendClosestUser()
                goto(Greetingname)
            }
        }

        onUserEnter {
            furhat.ledStrip.solid(java.awt.Color.GREEN)
            furhat.attendClosestUser()
            goto(Greetingname)
        }
        /*
            when {

                users.count > 0 -> {
                    furhat.ledStrip.solid(java.awt.Color.GREEN)
                    furhat.attendClosestUser()
                    goto(Greetingname)
                }
            }
            furhat.ledStrip.solid(java.awt.Color.BLUE)
            if (users.count > 0) {
                furhat.attend(users.other)
                goto(Greetingname)
            } else {
                goto(Init)
            }
        }
        */
        onUserLeave {
            goto(Init)
        }
    }
}



    /*init {
        furhat.ledStrip.solid(java.awt.Color.WHITE)
        furhat.attend(users.other)
        if (users.count > 0) {
                furhat.attend(users.random)
                goto(Greetingname)
            }
        else{
            delay(5000)
            reentry()
        }
    }

    onEntry {
            furhat.attendClosestUser()
            furhat.ledStrip.solid(java.awt.Color.BLUE)
            furhat.attend(users.current)
            goto(Greetingname)
        }
    onUserLeave {
        furhat.attend(users.other)
        furhat.attend(users.current)
        goto(Greetingname)
    }


    onUserEnter(instant = true) {
        furhat.ledStrip.solid(java.awt.Color.RED)
        furhat.attend(it)
        goto(Greetingname)
    }
}

/*
val Idle: State = state {


    init {
        /** Set value to avoid AutomaticHeadMovements to interfere with other head movements defined in a gesture. We assume gestures are not longer than 3000 ms **/
        AutomaticHeadMovements.autoHeadMovementDelay(3000)

        onUserEnter {
            furhat.attend(it)
            goto(Greeting)
        }
        onUserAttend {
            furhat.attend(it)
            furhat.say("Hallo du schaust mich nun an")
            goto(Greeting)
        }
        onUserLeave {
            reentry()
        }


       /* if (users.count == 1) {
            goto(Greeting)
        } else if (users.count > 1) {
            furhat.attendClosestUser()
            //Furhat schaut bei einer Gruppe bestehend aus mehr als zwei Usern den von ihm aus gesehen nächsten user an
            goto(Greeting)
        } else {
            furhat.attendNobody()

        }
*/
    }



    }



 */