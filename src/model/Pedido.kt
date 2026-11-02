package model

class Pedido(
    val cliente: Cliente,
    val productos: List<Producto>,
    var estado: String,
    val notas: String? = null
) {

    fun calcularSubtotal(): Double {
        return productos.sumOf { producto ->
            producto.precio
        }
    }

    fun calcularImpuesto(tasaImpuesto: Double = 0.19): Double {
        return calcularSubtotal() * tasaImpuesto
    }

    fun calcularDescuento(): Double {
        return if (productos.size > 3) {
            calcularSubtotal() * 0.10
        } else {
            0.0
        }
    }

    fun calcularTotal(tasaImpuesto: Double = 0.19): Double {
        val subtotal = calcularSubtotal()
        val impuesto = calcularImpuesto(tasaImpuesto)
        val descuento = calcularDescuento()

        return subtotal + impuesto - descuento
    }

    fun obtenerNotas(): String {
        return notas ?: "Sin notas"
    }

    fun cambiarEstado(nuevoEstado: String) {
        estado = nuevoEstado
    }
}
