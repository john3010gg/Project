class PMLI{
    var letra: Char? = null
    var conjuntoPalabras: ConjuntoPalabras? = null

    /*
        Precondicion: True
        PostCondicion: letra = l ^ conjuntoPalabras != ∅
        Procedimiento que inicializa el TAD PMLI con un parametro de entrada que indica la letra
    */
    fun crearPMLI(l: Char) {
        this.letra = l
        conjuntoPalabras = ConjuntoPalabras()
    }


    /*
        Precondicion: palabra[0] = letra
        Postcondicion: ConjuntoPalabras = ConjuntoPalabras U {palabra}
        Procedimiento que recibe un parametro de entrada tipo STRING que se almacena en el ConjuntoPalabras
     */
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


    /*
        Precondicion: palabra[0] = letra
        Postcondicion: ConjuntoPalabras = ConjuntoPalabras - {palabra}
        Procedimiento que recibe un parametro de entrada tipo STRING que se elimna del ConjuntoPalabras
    */
    fun eliminarPalabra(palabra: String) {
        if (!esPalabraValida(palabra)) {
            println("La palabra '$palabra' no es válida.")
            return
        }
        conjuntoPalabras!!.eliminar(palabra)
    }

    /*
        Precondicion: True
        Postcondicion: Imprimir por la salida estandar los elementos del ConjuntoPalabras
        Procedimiento que imprime por la salida estandar los elementos del conjunto
    */
    fun mostrarPalabras(){
        conjuntoPalabras!!.mostrar()
    }

    /*
        Precondicion: palabra[0] = letra
        Postcondicion: BuscarPalabra == (palabra ∈ ConjuntoPalabras)
        Funcion que recibe un parametro de entrada tipo STRING que se busca si pertenece al ConjuntoPalabras,
        y que devuelve True o False
    */
    fun buscarPalabra(palabra: String): Boolean {
        return conjuntoPalabras!!.pertenece(palabra)
    }
    
    fun devolverPalabras(): Array<String> {
        return conjuntoPalabras!!.palabrasEnConjunto()
    }
}