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

    fun cambiarEstado(nuevoEstado: String) {
        estado = nuevoEstado
    }
}
