import java.io.File
class AyudanteOrtografico {

    private val alfabeto = "abcdefghijklmnopqrstuvwxyz"
    private val dicc = Array(27) {PMLI()}

    /*
        Precondicion: true
        Postcondicion: Inicializa para cada i, 0 <= i < 27 la estructura dicc, donde cada dicc[i]
        contiene un PMLI con una letra del alfabeto espanol
    */
    fun crearAyudante(){
        for (letra in alfabeto) {
            dicc[letra - 'a'].crearPMLI(letra)
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
                dicc[letra - 'a']?.agregarPalabra(line)
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
        if (!palabraValida(palabra)){
            println("Error: la palabra ${palabra} no es una palabra valida")
            return
        }
        if (dicc[palabra[0] - 'a'].buscarPalabra(palabra)) {
            val letra = palabra[0]
            dicc[letra - 'a']?.eliminarPalabra(palabra)
            println("Palabra '$palabra' eliminada del diccionario.")
        } else {
            println("La palabra '$palabra' no existe en el diccionario.")
        }
    }

    
    fun corregirTexto(fileName: String): String {
        val file = File(fileName)
        if (!file.exists()) {
            return "El archivo no existe."
        }
        val lines = file.readLines()
        val palabras = extraerTexto(lines)
        val resultado = StringBuilder()

        for (palabra in palabras) {
            if (!esPalabraValida(palabra) || palabra.isEmpty()) continue
            val idx = palabra[0] - 'a'
            if (palabra.isNotEmpty() && palabra[0] in 'a'..'z') {
                val idx = palabra[0] - 'a'
                if (!dicc[idx].buscarPalabra(palabra)) {
                    val sugerencias = dicc[idx].devolverPalabras()
                        .map { Pair(it, damerauLevenshteinDistance(palabra, it)) }
                        .sortedBy { it.second }
                        .take(4)
                        .map { it.first }

                    resultado.append("Palabra desconocida: '$palabra'\n")
                    if (sugerencias.isEmpty()) {
                        resultado.append("Sugerencias: No se encontraron sugerencias\n\n")
                    } else {
                        resultado.append("Sugerencias: ${sugerencias.joinToString(", ")}\n\n")
                    }
                }
            }
        }

        return resultado.toString()
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
