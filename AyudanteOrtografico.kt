import java.io.File
class AyudanteOrtografico {

    private val alfabeto = "abcdefghijklmnopqrstuvwxyz"
    private val conjuntoPalabras = Array(27) {PMLI()}

    fun crearAyudante(){
        for (letra in alfabeto) {
            conjuntoPalabras[letra - 'a'].crearPMLI(letra)
        }
    }

    fun cargarDiccionario(fileName: String) {
        val file = File(fileName)
        if (!file.exists()) {
            println("El archivo no existe.")
            return
        }
        val lines = file.readLines()
        for (line in lines) {
            if (esPalabraValida(line)) {
                val letra = line[0]
                conjuntoPalabras[letra - 'a']?.agregarPalabra(line)
            } else {
                println("La palabra '$line' no es válida.")
            }
        }
    }
    fun borrarPalabra(palabra: String) {
        if (conjuntoPalabras[palabra[0] - 'a'].buscarPalabra(palabra)) {
            val letra = palabra[0]
            conjuntoPalabras[letra - 'a']?.eliminarPalabra(palabra)
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
                if (!conjuntoPalabras[idx].buscarPalabra(palabra)) {
                    val sugerencias = conjuntoPalabras[idx].devolverPalabras()
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
