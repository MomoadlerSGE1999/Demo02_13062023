package furhatos.app.demo02.nluAlt

import furhatos.nlu.Intent

class QRCode : Intent() {
    override fun getExamples(lang: furhatos.util.Language): List<String> {
        return listOf(
            "QR-Code",
            "",
            "Ich habe einen QR-Code",
            "QRCode",
            "Kuh Arr",
            "QR",
            "Qa"
        )
    }
}