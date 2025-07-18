class ConjuntoPalabras {

    data class Nodo(val palabra: String, var siguiente: Nodo?)

    var capacidad = 16
    var tamaño = 0
    var tabla: Array<Nodo?> = arrayOfNulls(capacidad)

  
    fun agregar(p: String) {
        if (pertenece(p)) return

        if ((tamaño + 1).toDouble() / capacidad >= 0.7) {
            rehash()
        }
        val idx = hash(p)
        tabla[idx] = Nodo(p, tabla[idx])
        tamaño++
    }

   
    fun eliminar(p: String) {
        val idx = hash(p)
        var current = tabla[idx]
        var anterior: Nodo? = null
        while (current != null) {
            if (current.palabra == p) {
                if (anterior == null) {
                    tabla[idx] = current.siguiente
                } else {
                    anterior.siguiente = current.siguiente
                }
                tamaño--
                return
            }
            anterior = current
            current = current.siguiente
        }
    }

   
    fun pertenece(p: String): Boolean {
        val idx = hash(p)
        var current = tabla[idx]
        while (current != null) {
            if (current.palabra == p) return true
            current = current.siguiente
        }
        return false
    }

    
    fun mostrar(){
        val lista = mutableListOf<String>()
        for (bucket in tabla) {
            var current = bucket
            while (current != null) {
                lista.add(current.palabra)
                current = current.siguiente
            }
        }
        lista.sort()
        for (p in lista) println(p)
    }

    fun hash(p: String): Int {
        val h = p.hashCode() % capacidad
        return if (h < 0) h + capacidad else h
    }

    fun rehash() {
        val oldTabla = tabla
        capacidad *= 2
        tabla = arrayOfNulls(capacidad)
        tamaño = 0
        for (bucket in oldTabla) {
            var current = bucket
            while (current != null) {
                agregar(current.palabra)
                current = current.siguiente
            }
        }
    }

    fun palabrasEnConjunto(): List<String> {
        val lista = mutableListOf<String>()
        for (bucket in tabla) {
            var current = bucket
            while (current != null) {
                lista.add(current.palabra)
                current = current.siguiente
            }
        }
        return lista
    }
}

fun esPalabraValida(string: String): Boolean{
    for (ch in string){
        if (!(ch in 'a'..'z' || ch == 'ñ')){
            return false
        }
    }
    return true
}

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
                d[i][j] + cost,                  // substitution
                d[i + 1][j] + 1,                 // insertion
                d[i][j + 1] + 1,                 // deletion
                d[i1][j1] + (i - i1 - 1) + 1 + (j - j1 - 1) // transposition
            )
        }
        da[a[i - 1]] = i
    }

    return d[a.length + 1][b.length + 1]
}