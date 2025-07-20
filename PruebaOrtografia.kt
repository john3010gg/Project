fun main() {
    val scanner = java.util.Scanner(System.`in`)
    var ayudante = AyudanteOrtografico()
    var salir = false
    var creado = false

    while (!salir) {
        println(
            """
            Menú:
            1. Crear un nuevo ayudante ortográfico
            2. Cargar un diccionario
            3. Eliminar palabra
            4. Corregir texto
            5. Mostrar diccionario
            6. Salir de la aplicación
            Seleccione una opción:
            """.trimIndent()
        )

        val opcion = scanner.nextLine().trim()

        when (opcion) {
            "1" -> {
                ayudante.crearAyudante()
                creado = true
                println("Ayudante ortográfico creado correctamente.")
            }
            "2" -> {
                if (creado == false) {
                    println("Error: Debe crear primero el ayudante ortográfico.")
                } else {
                    println("Ingrese la ruta del archivo de diccionario")
                    val ruta = scanner.nextLine().trim()
                    ayudante.cargarDiccionario(ruta)
                    println("Diccionario cargado.")
                }
            }
            "3" -> {
                if (creado == false) {
                    println("Error: Debe crear primero el ayudante ortográfico (opción 1).")
                } else {
                    println("Ingrese la palabra a eliminar:")
                    val palabra = scanner.nextLine().trim().lowercase()
                    ayudante.borrarPalabra(palabra)
                }
            }
            "4" -> {
                if (creado == false) {
                    println("Error: Debe crear primero el ayudante ortográfico (opción 1).")
                } else {
                    println("Ingrese la ruta del texto a corregir")
                    val finput = scanner.nextLine().trim()
                    println("Ingrese la ruta del archivo de salida")
                    val foutput = scanner.nextLine().trim()
                    ayudante.corregirTexto(finput, foutput)
                    println("Texto corregido. Resultados guardados en '$foutput'.")
                }
            }
            "5" -> {
                if (creado == false) {
                    println("Error: Debe crear primero el ayudante ortográfico (opción 1).")
                } else {
                    println("Diccionario actual:")
                    ayudante.imprimirDiccionario()
                }
            }
            "6" -> {
                salir = true
                println("Saliendo de la aplicación...")
            }
            else -> println("Opción no válida, intente de nuevo.")
        }
    }
}