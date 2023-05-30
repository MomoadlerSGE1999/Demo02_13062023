import furhatos.app.demo02.flow.main.readExcelNew.Platz
import furhatos.records.User
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.ss.usermodel.WorkbookFactory
import java.io.FileInputStream
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

//Vergleiche ReadExcel.kt
//Technisch gesehen genau dasselbe, wie die ReadExcel.kt. Durch die Separierung kann aber für die Taxifahrer/Angehörigen
//auf eine andere Excel zugegriffen werden. Da die Taxifahrer/Angehörigen die Patienten oftmals schon dann abholen,
//wenn die nächste Schicht bereits begonnen hat. So kann der Belegungsplan der vorherigen Schicht, aber weiter
//für die Taxifahrer/angehörige verwendet werden. Die Pateinten können zeitgleich den bereits aktuellen und für sie
//auch relevanten BGelegungsplan nutzen

fun ReadExcelTaxi(Benutzer: User) {



    val filename: String = "C:\\data\\Belegungsplan_Finales_Format.xls"
    //val filename: String = "N:\\Vicki_Taxi.xls"

    val wb = WorkbookFactory.create(FileInputStream(filename))

    val sheet: Sheet = wb.getSheetAt(0)

    var platzList = mutableListOf<Platz>()

    var raum: String? = null

    for (rowIndex in 0 until sheet.lastRowNum + 1) {

        val rowContent: Row? = sheet.getRow(rowIndex)

        if (rowContent != null) {
            val cellPlatz: Cell? = rowContent.getCell(2)
            val cellRaum: Cell? = rowContent.getCell(1)

            if (cellRaum != null && cellRaum.toString().trim().contains("Raum")) {
                raum = cellRaum.toString()
            }

            if (cellPlatz != null) {
                val cellPlatzString: String = cellPlatz.toString()

                if (cellPlatzString.startsWith("furhatos.app.demo02.flow.main.readExcelNew.Platz")) {
                    if (raum == null) {
                        for (rowIndex2 in rowIndex until sheet.lastRowNum + 1) {

                            val cellRaum: Cell? = rowContent.getCell(1)
                            if (cellRaum != null) {
                                raum = cellRaum.toString()
                                platzList.add(Platz(raum, cellPlatzString, rowIndex))
                                raum = null
                                break
                                }
                        }
                    } else {
                        platzList.add(Platz(raum, cellPlatzString, rowIndex))
                    }
                }
            }
        }
    }

    var searchNum: String = Benutzer.get("Patientennummer").toString()
    var spalte: Int? = null
    var daten: List<String> = LocalDate.now().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL)).split(",")

    if(daten[0] == "Montag") {
        spalte = 3
    }
    if (daten[0] == "Dienstag") {
        spalte = 4
    }
    if (daten[0] == "Mittwoch") {
        spalte = 5
    }
    if (daten[0] == "Donnerstag") {
        spalte = 6
    }
    if (daten[0] == "Freitag") {
        spalte = 7
    }
    if (daten[0] == "Samstag") {
        spalte = 4
    }

    if (daten[0] == "Sonntag") {
        spalte = 9
    }

    var col = spalte

    var foundrowIndex = -1

    var patientNum: String? = null
    var patientname: String? = ""
    var dialysezeitraum: List<String>? = null
    var dialysebeginn: String? = null
    var dialyseende: String? = null

    for (rowIndex in 0 until sheet.lastRowNum + 1) {
        val rowContent: Row? = sheet.getRow(rowIndex)

        if (rowContent != null) {

            val cellDay: Cell? = rowContent.getCell(col!!)

            if(cellDay!=null && cellDay.toString().contains(searchNum)) {

                val temp = cellDay.toString().split(',')

                patientNum = Integer.parseInt(temp[0].trim()).toString()
                patientname = temp[2].trim() + " " + temp[1].trim()
                dialysezeitraum = temp[3].trim().split('-')
                dialysebeginn = dialysezeitraum[0].trim()
                dialyseende =dialysezeitraum[1].trim()


                if(searchNum == patientNum){
                    foundrowIndex = rowIndex
                    Benutzer!!.put("name", patientname)
                    Benutzer!!.put("dialysebeginn", dialysebeginn)
                    Benutzer!!.put("dialyseende", dialyseende)
                }
            }
        }
    }

    Benutzer!!.put("row", foundrowIndex)
    println("${Benutzer!!.get("name")} - ${Benutzer!!.get("Patientennummer")}")
    for (platz in platzList) {
        if (foundrowIndex == -1) {
            println("Patient: $searchNum nicht gefunden")
            break
        }

        if (platz.zeile == foundrowIndex) {
            println("Patient: $searchNum")
            var platzfurhat: String = platz.platz
            var raumfurhat: String = platz.raum.toString().substring(startIndex = 10, endIndex = 18)

            Benutzer!!.put("raum", raumfurhat)
            Benutzer!!.put("platz", platzfurhat)
        }
    }
}
