package model

class Producto(
    val nombre: String,
    val precio: Double,
    val ingredientes: Map<String, Int>,
    val categoria: String = "Pizza"
) {

    init {
        require(nombre.isNotBlank()) {
            "El nombre del producto es obligatorio."
        }
        require(categoria.isNotBlank()) {
            "La categoría del producto es obligatoria."
        }
        require(precio >= 0.0 && precio.isFinite()) {
            "El precio debe ser un número válido no negativo."
        }
    }

    fun detallarProducto(): String {
        val detalleIngredientes =
            ingredientes.entries.joinToString { ingrediente ->
                "${ingrediente.key}: ${ingrediente.value}"
            }

        return "Producto: $nombre | Precio: $$precio | " +
            "Ingredientes: $detalleIngredientes"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Producto) return false

        return nombre.trim().lowercase() == other.nombre.trim().lowercase() &&
            categoria.trim().lowercase() == other.categoria.trim().lowercase()
    }

    override fun hashCode(): Int {
        var resultado = nombre.trim().lowercase().hashCode()
        resultado = 31 * resultado + categoria.trim().lowercase().hashCode()
        return resultado
    }
}
