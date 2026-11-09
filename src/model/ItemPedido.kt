package model

data class ItemPedido(
    val producto: Producto,
    val cantidad: Int
) {
    init {
        require(cantidad in 1..100) {
            "La cantidad debe estar entre 1 y 100."
        }
    }

    fun calcularSubtotal(): Double {
        return producto.precio * cantidad
    }
}
