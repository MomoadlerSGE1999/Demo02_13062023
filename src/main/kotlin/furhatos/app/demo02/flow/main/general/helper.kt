package furhatos.app.demo02.flow.main.general

import FrageWiederholen
import furhatos.app.demo02.flow.main.patient.ValidierungNummerKunde
import furhatos.flow.kotlin.*
import furhatos.skills.UserManager
import furhatos.skills.UserManager.current
import com.google.zxing.BarcodeFormat
import com.google.zxing.BinaryBitmap
import com.google.zxing.MultiFormatReader
import com.google.zxing.RGBLuminanceSource
import com.google.zxing.common.HybridBinarizer
import com.google.zxing.DecodeHintType
import com.google.zxing.Result
import furhatos.records.User
import org.zeromq.SocketType
import org.zeromq.ZContext
import java.awt.image.BufferedImage
import java.awt.Graphics2D
import java.io.File
import javax.imageio.ImageIO
import org.zeromq.ZMQ
import java.io.ByteArrayInputStream
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response


//In der helper.kt sind alle benötigten Funktionen der Interaktion definiert.

fun GetDigitsKunde (Benutzer: User, furhat: Furhat, field: String) {

//Mit furhat.askFor fragt furhat nach einem spezifischem intent. In diesem Fall eine Nummer.
    furhat.askFor<furhatos.nlu.common.Number>("Wie lautet die Patientennummer Ihres Kunden", timeout = 20000, endSil = 5000)
    {
        //Antwortet der Nutzer mit einer Nummer, so triggert onResponse<Number>.
        onResponse<furhatos.nlu.common.Number> {
            //Furhat schaut den User an, der geantwortet hat
            furhat.attend(user= current)


            //Mit it.text kann das Gesagte des Nutzers manipuliert werden. Mit toString wird aus dem Gesagten
            //ein String gemacht. Anschließend werden alle Leerzeichen mit replace ersetzt.
            val x: String = it.text.toString().replace(" ".toRegex(), "")

            //Aus der Variable X werden alle Buchstaben entfernt, lediglich Zahlen bleiben über.
            //So kann der Nutzer auch sagen: "Meine Patientennummer ist XXXXX. Auch in diesem Fall,
            //würde nur die Patientennummer verarbeitet werden.
            val resultx: String = x.filter { it.isDigit() }

            //Schließlich wird das field Patientennummer mit der vollends korrekt manipulierten Variable
            //resultx beschrieben, dem nutzer ist jetzt eine Patientennummer zugeordnet.
            Benutzer.put("Patientennummer", resultx)
            goto(ValidierungNummerKunde)
        }
        onNoResponse {
        //Antwortet der Nutzer gar nicht, so startet Furhat die Frage (Funktion) nochmals.
            reentry()
        }
        onResponse<FrageWiederholen> {
            //Fordert der Nutzer Furhat auf die Frage noch einmal zu wiederholen,
            // so wiederholt auch durch diesen trigger Furhat die Frage nochmal.
            reentry()
        }
    }
}


fun GetDigitsKundenochmal (Benutzer: User, furhat: Furhat, field: String) {
//Die GetDigitsNochmal-Funktionen unterscheiden sich nur in der Frage nach der Patientennummer.
    furhat.askFor<furhatos.nlu.common.Number>("Wie dann?", timeout = 15000, endSil = 5000) {
        onResponse<furhatos.nlu.common.Number> {
            furhat.attend(user= current)
            val x: String = it.text.toString().replace(" ".toRegex(), "")
            val resultx: String = x.filter { it.isDigit() }
            Benutzer.put("Patientennummer", resultx)
            goto(ValidierungNummerKunde)
        }
        onResponse<FrageWiederholen> {
            reentry()
        }
    }
}

/*fun qrCodeScan13(benutzer: User) {
    val imageInput: BufferedImage? = captureImageFromSocket1()

        val luminanceSource = RGBLuminanceSource(imageInput.width, imageInput.height, getPixels2(imageInput))
        val binaryBitmap = BinaryBitmap(HybridBinarizer(luminanceSource))

        val reader = MultiFormatReader()
        val result = reader.decode(binaryBitmap)

        println("QR Code Text: ${result.text}")
        val barcodeText: String = result.text
        benutzer.put("QR Code Text", barcodeText) // Speichert den erkannten QR-Code-Text im Benutzerobjekt

}

 */
fun containsQRCode(image: BufferedImage): Boolean {
    val luminanceSource = RGBLuminanceSource(image.width, image.height, getPixels2(image))
    val binaryBitmap = BinaryBitmap(HybridBinarizer(luminanceSource))

    val reader = MultiFormatReader()

    return try {
        // Versuche, einen QR-Code im Bild zu erkennen
        reader.decode(binaryBitmap)
        // Wenn kein Fehler aufgetreten ist, gebe true zurück
        true
    } catch (e: Exception) {
        // Wenn ein Fehler aufgetreten ist (kein QR-Code gefunden), gebe false zurück
        false
    }
}

fun getPixels2(image: BufferedImage): IntArray {
    val width = image.width
    val height = image.height
    val pixels = IntArray(width * height)
    image.getRGB(0, 0, width, height, pixels, 0, width)
    return pixels
}




fun captureImageFromSocket(benutzer: User) {
    val context = ZContext()
    val subscriber = context.createSocket(SocketType.SUB)

    // Verbinde den Socket mit dem ZMQ.SUB-Socket
    subscriber.connect("tcp://10.198.3.150:3000")

    // Setze den Filter auf leeren String, um alle Nachrichten zu empfangen
    subscriber.subscribe("".toByteArray())

    var imageInput: BufferedImage?

    // Empfange eine Nachricht
    val message = subscriber.recv(0)

    // Schließe den Socket und den Kontext
    subscriber.close()
    context.close()

    // Überprüfe, ob eine Nachricht empfangen wurde
    if (message != null) {
        // Verarbeite das empfangene Bild
        val image = ImageIO.read(ByteArrayInputStream(message))

        // Speichere das empfangene Bild als latestImage
        imageInput = image

        if (!containsQRCode(imageInput)) {
            // Wenn kein Bild übergeben wurde oder das Bild keinen QR-Code enthält,
            // rufe die Funktion rekursiv neu auf
            Thread.sleep(2000) // Füge eine Verzögerung von einer halben Sekunde ein

            captureImageFromSocket(benutzer)
        } else {
            val luminanceSource = RGBLuminanceSource(imageInput.width, imageInput.height, getPixels2(imageInput))
            val binaryBitmap = BinaryBitmap(HybridBinarizer(luminanceSource))

            val reader = MultiFormatReader()
            val result = reader.decode(binaryBitmap)

            println("QR Code Text: ${result.text}")
            val barcodeText: String = result.text
            benutzer.put("QR Code Text", barcodeText) // Speichert den erkannten QR-Code-Text im Benutzerobjekt
        }
    } else {
        // Keine Nachricht empfangen - handle den Fall entsprechend
        // Zum Beispiel eine Fehlermeldung ausgeben oder weitere Maßnahmen ergreifen
    }
}

fun Serverconnect() {
    val client = OkHttpClient()

    val request = Request.Builder()
        .url("https://example.com") // Ersetze "example.com" durch die URL des Servers, den du aufrufen möchtest
        .build()

    val response: Response = client.newCall(request).execute()

    if (response.isSuccessful) {
        val responseBody = response.body?.string()
        println("Antwort des Servers: $responseBody")
    } else {
        println("Fehler: ${response.code}")
    }
}


/*
fun GetDigitsTaxifahrer(Benutzer: User, furhat: Furhat, field: String) {
//Unterscheidet sich nur in der Art und Weise wie Furhat nach der Patientennummer fragt.
    furhat.askFor<furhatos.nlu.common.Number>(
        "Was ist die Patientennummer ihres Kunden beziehungsweise Angehörigen", timeout = 20000, endSil = 5000)
    {
        onResponse<furhatos.nlu.common.Number> {
            furhat.attend(user= current)
            val x: String = it.text.toString().replace(" ".toRegex(), "")
            val resultx: String = x.filter { it.isDigit() }
            Benutzer.put("Patientennummer", resultx)
            goto(ValidierungNummerKunde)
        }
        onNoResponse {
            //Antwortet der Nutzer gar nicht, so startet Furhat die Frage (Funktion) nochmals.
            reentry()
        }
        onResponse<FrageWiederholen> {
            //Fordert der Nutzer Furhat auf die Frage noch einmal zu wiederholen,
            // so wiederholt auch durch diesen trigger Furhat die Frage nochmal.
            reentry()
        }
    }
}

fun GetDigitsTaxifahrernochmal(Benutzer: User, furhat: Furhat, field: String) {
//Die GetDigitsNochmal-Funktionen unterscheiden sich nur in der Frage nach der Patientennummer
    furhat.askFor<furhatos.nlu.common.Number>("Wie dann?", timeout = 20000, endSil = 5000) {
        onResponse<furhatos.nlu.common.Number> {
            furhat.attend(user= current)
            val x: String = it.text.toString().replace(" ".toRegex(), "")
            val resultx: String = x.filter { it.isDigit() }
            Benutzer.put("Patientennummer", resultx)
            goto(ValidierungNummerKunde)
        }
    }
}



 */
