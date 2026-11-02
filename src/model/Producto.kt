package model

class Producto(
    val nombre: String,
    val precio: Double,
    val ingredientes: Map<String, Int>
) {

    fun detallarProducto(): String {

        val detalleIngredientes =
            ingredientes.entries.joinToString { ingrediente ->
                "${ingrediente.key}: ${ingrediente.value}"
            }

        return "Producto: $nombre | Precio: $$precio | Ingredientes: $detalleIngredientes"
    }
}
