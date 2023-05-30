package furhatos.app.demo02.flow.main

import furhatos.app.demo02.flow.Parent
import furhatos.flow.kotlin.*
import furhatos.gestures.Gestures
import org.apache.xmlbeans.xml.stream.Location
import java.time.Duration

val YoutubeVideo : State = state() {

    onEntry {
        furhat.say({+"Hallo, mein Name ist Furhat."
                    +delay(200)
                    furhat.gesture(Gestures.BigSmile(duration = 1.1), async = false)
                    +"ich bin zwar nicht mehr neu hier am Fachbereich, aber der ein oder andere dürfte mich noch nicht kennen. "
                    +"Ich bin ein sozialer Serviceroboter mit Emotionserkennung und menschenähnlicher Mimik."
                    +delay(500)
                    +"Vielleicht sind manche Menschen sogar weniger menschenähnlich als ich"
                    +blocking{furhat.gesture(Gestures.Thoughtful(duration = 1.5), async = false)}})
        furhat.attend(furhatos.records.Location.RIGHT)
        delay(5000)
        furhat.attend(location = furhatos.records.Location.STRAIGHT_AHEAD)

            furhat.say({+"Naja"
                +delay(150)
            +blocking{furhat.gesture(Gestures.Wink(duration = 1.0))}
            +"gerade du musst es ja wissen"})


delay(6500)
        furhat.say(" ${furhat.voice.emphasis("Okay")}")
        delay(300)
        furhat.say({
                    +"In diesem Video sollt ihr meinen ersten Anwendungsfall gezeigt bekommen."
                    +"Moritz wird euch zudem etwas über mein Potential und etwas zu meinen technischen Limitationen erzählen. "
                    delay(200)
                    + "Ich bin schon so aufgeregt."
                    delay(200)
                    +"Bühne Frei!"
                    +blocking {furhat.gesture(Gestures.Nod(duration = 2.0))}})
    }
}
