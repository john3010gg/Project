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
        var actual = tabla[idx]
        var anterior: Nodo? = null
        while (actual != null) {
            if (actual.palabra == p) {
                if (anterior == null) {
                    tabla[idx] = actual.siguiente
                } else {
                    anterior.siguiente = actual.siguiente
                }
                tamaño--
                return
            }
            anterior = actual
            actual = actual.siguiente
        }
    }

   
    fun pertenece(p: String): Boolean {
        val idx = hash(p)
        var actual = tabla[idx]
        while (actual != null) {
            if (actual.palabra == p) return true
            actual = actual.siguiente
        }
        return false
    }

    
    fun mostrar() {
        val lista = mutableListOf<String>()
        for (bucket in tabla) {
            var actual = bucket
            while (actual != null) {
                lista.add(actual.palabra)
                actual = actual.siguiente
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
            var actual = bucket
            while (actual != null) {
                agregar(actual.palabra)
                actual = actual.siguiente
            }
        }
    }
}

