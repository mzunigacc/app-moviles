package model

class Producto(
    val nombre: String,
    val precio: Double,
    val ingredientes: List<String>
) {
    fun detallarProducto(): String {
        return "Producto: $nombre | Precio: $$precio | Ingredientes: ${ingredientes.joinToString()}"
    }
}
