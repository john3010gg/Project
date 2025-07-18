class PMLI{
    var letra: Char? = null
    var conjuntoPalabras: ConjuntoPalabras? = null

    fun crearPMLI(letra: Char) {
        this.letra = letra
        conjuntoPalabras = ConjuntoPalabras()
    }

    fun agregarPalabra(palabra: String) {
        if (palabra[0] != letra){
            println("La palabra '$palabra' no es válida o no comienza con la letra '$letra'.")
            return
        }

        if (!esPalabraValida(palabra)) {
            println("La palabra '$palabra' no es válida.")
            return
        }
        conjuntoPalabras!!.agregar(palabra)
    }
    
    fun eliminarPalabra(palabra: String) {
        if (!esPalabraValida(palabra)) {
            println("La palabra '$palabra' no es válida.")
            return
        }
        conjuntoPalabras!!.eliminar(palabra)
    }
    fun mostrarPalabras(){
        conjuntoPalabras!!.mostrar()
    }

    fun buscarPalabra(palabra: String): Boolean {
        return conjuntoPalabras!!.pertenece(palabra)
    }
    
    fun devolverPalabras(): List<String> {
        return conjuntoPalabras!!.palabrasEnConjunto()
    }
}