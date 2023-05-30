package furhatos.app.demo02

import furhatos.app.demo02.flow.Init
import furhatos.flow.kotlin.Flow
import furhatos.skills.Skill

class Demo02Skill : Skill() {
    override fun start() {
        //Hier gibt man den State an in dem die interaktion starten soll,
        // typischerweise ist das der State Init.
        Flow().run(Init)
    }
}
//In Zeile 15 wird der Skill gestartet.
fun main(args: Array<String>) {
    Skill.main(args)
}









