import java.io.File
class AyudanteOrtografico {

    private val alfabeto = "abcdefghijklmnopqrstuvwxyzñ"
    private val dicc = Array(27) {PMLI()}

    /*
        Precondicion: true
        Postcondicion: Inicializa para cada i, 0 <= i < 27 la estructura dicc, donde cada dicc[i]
        contiene un PMLI con una letra del alfabeto espanol
    */
    fun crearAyudante(){
        for (i in alfabeto.indices) {
            dicc[i].crearPMLI(alfabeto[i])
        }
    }

    /*
        Precondicion: el archivo de ruta fileName debe existir, ademas de tener maximo una palabra por linea y
        cada palabra por linea ser una palabraValida
        Postcondicion: se anaden a dicc, las palabras pertenecientes al archivo fileName
        Procedimiento que recibe un parametro fileName tipo String, que indica la ruta de un archivo .txt que
        contiene las palabras a anadir al dicc
    */
    fun cargarDiccionario(fileName: String) {
        val file = File(fileName)
        if (!file.exists()) {
            println("El archivo no existe.")
            return
        }
        val lines = file.readLines()
        for (line in lines) {
            if (line.trim().contains(" ")){
                println("Error: la linea ${line} contiene mas de una palabra")
                continue
            }
            if (esPalabraValida(line)) {
                val letra = line[0]
                dicc[letraToIndice(letra)]?.agregarPalabra(line)
            } else {
                println("La palabra '$line' no es válida.")
            }
        }
    }

    /*
        Precondicion: el parametro palabra debe ser una palabra valida
        Postcondicion: se elimina del dicc el parametro palabra
        Procedimiento que recibe un parametro Palabra de tipo String, que elimina la misma del dicc
    */
    fun borrarPalabra(palabra: String) {
        if (!esPalabraValida(palabra)){
            println("Error: la palabra ${palabra} no es una palabra valida")
            return
        }
        val idx = letraToIndice(palabra[0])
        if (dicc[idx].buscarPalabra(palabra)) {
            dicc[idx].eliminarPalabra(palabra)
            println("Palabra '$palabra' eliminada del diccionario.")
        } else {
            println("La palabra '$palabra' no existe en el diccionario.")
        }
    }

    /*
        Precondicion: finput es un archivo de texto valido
        Postcondicion: Imprime en el archivo foutput cada una de las palabras validas contenidas
        en el archivo finput que no se encuentren en dicc, seguidas de las cuatro palabras
        con menor distancia.
    */
    fun corregirTexto(finput: String, foutput: String) {
        val file = File(finput)
        if (!file.exists()) {
            println("El archivo no existe.")
            return
        }
        val lines = file.readLines()
        val palabras = extraerTexto(lines)
        val resultado = StringBuilder()

        for (palabra in palabras) {
            if (!esPalabraValida(palabra) || palabra.isEmpty()) continue
            val idx = letraToIndice(palabra[0])
            if (!dicc[idx].buscarPalabra(palabra)) {
                val sugerencias = mutableListOf<Pair<String, Int>>()
                for (pml in dicc) {
                    pml.devolverPalabras().forEach { p ->
                        sugerencias.add(Pair(p, damerauLevenshteinDistance(palabra, p)))
                    }
                }
                val mejores = sugerencias.sortedBy { it.second }.take(4).map { it.first }
                resultado.append("Palabra desconocida: '$palabra'\n")
                if (mejores.isEmpty()) {
                    resultado.append("Sugerencias: No se encontraron sugerencias\n\n")
                } else {
                    resultado.append("Sugerencias: ${mejores.joinToString(", ")}\n\n")
                }
            }
        }
        File(foutput).writeText(resultado.toString())
    }

    /*
        Precondicion: True
        Postcondicion: Imprime dicc por la salida estandar mostrando las palabras en orden lexicografico
    */
    fun imprimirDiccionario() {
        for (pml in dicc) {
            println("Palabras con letra inicial '${pml.letra}':")
            pml.mostrarPalabras()
        }
    }

    private fun letraToIndice(letra: Char): Int {
        return if (letra == 'ñ') 26 else letra - 'a'
    }

    private fun extraerTexto(text: List<String>): List<String> {
        val palabras = mutableListOf<String>()
        for (line in text) {
            val words = line.split(Regex("\\s+"))
            for (word in words) {
                if (word.isNotEmpty()) {
                    val cleanWord = word.trim().trimEnd('.', ',', ';', ':', '!', '?').lowercase()
                    if (cleanWord.isNotEmpty()) {
                        palabras.add(cleanWord)
                    }
                }
            }
        }
        return palabras
    }

    private fun verificarSignos(word: String): Boolean {
        if (word.isEmpty()) return false
        if (word.last() == ',' || word.last() == '.' || word.last() == '!' || word.last() == '?') {
            return true
        } else if (word.last() == ';' || word.last() == ':') {
            return true
        } else {
            return false
        }
    }
}

/*
    Funcion que recibe un parametro string de tipo String, y que retorna un valor Booleano.
    Si para todo caracter perteneciente a la String, estos pertenecen al alfabeto espanol, entonces se retorna
    true, si al menos hay uno que no pertenece al alfabeto en espanol, retorna false
*/
fun esPalabraValida(string: String): Boolean {
    for (ch in string) {
        if (!(ch in 'a'..'z' || ch == 'ñ')) {
            return false
        }
    }
    return true
}

/*
    implementacion del algoritmo de Damerau–Levenshtein para hallar la distancia enter dos palabras
*/
fun damerauLevenshteinDistance(a: String, b: String): Int {
    val maxdist = a.length + b.length
    val d = Array(a.length + 2) { IntArray(b.length + 2) }
    val da = mutableMapOf<Char, Int>()

    for (c in (a + b)) {
        da[c] = 0
    }

    d[0][0] = maxdist
    for (i in 0..a.length) {
        d[i + 1][0] = maxdist
        d[i + 1][1] = i
    }
    for (j in 0..b.length) {
        d[0][j + 1] = maxdist
        d[1][j + 1] = j
    }

    for (i in 1..a.length) {
        var db = 0
        for (j in 1..b.length) {
            val i1 = da.getOrDefault(b[j - 1], 0)
            val j1 = db
            val cost = if (a[i - 1] == b[j - 1]) {
                db = j
                0
            } else {
                1
            }
            d[i + 1][j + 1] = minOf(
                d[i][j] + cost,
                d[i + 1][j] + 1,
                d[i][j + 1] + 1,
                d[i1][j1] + (i - i1 - 1) + 1 + (j - j1 - 1)
            )
        }
        da[a[i - 1]] = i
    }

    return d[a.length + 1][b.length + 1]
}