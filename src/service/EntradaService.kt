package service

object EntradaService {

    fun convertirPrecio(valor: String): Double {
        return try {
            val precio = valor.toDouble()

            if (precio < 0) {
                throw IllegalArgumentException(
                    "El precio no puede ser negativo."
                )
            }

            precio

        } catch (e: NumberFormatException) {

            println(
                "Error: '$valor' no es un precio válido."
            )

            0.0

        } catch (e: IllegalArgumentException) {

            println(
                "Error: ${e.message}"
            )

            0.0
        }
    }

    fun validarCantidadInventario(cantidad: Int): Int {
        return try {

            if (cantidad < 0) {
                throw IllegalArgumentException(
                    "La cantidad de inventario no puede ser negativa."
                )
            }

            cantidad

        } catch (e: IllegalArgumentException) {

            println(
                "Error: ${e.message}"
            )

            0
        }
    }
}
