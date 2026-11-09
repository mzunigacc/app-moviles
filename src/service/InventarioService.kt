package service

import model.Producto

class InventarioService(
    private val inventario: MutableMap<String, Int>
) {

    init {
        require(inventario.values.all { it >= 0 }) {
            "El inventario inicial no puede contener cantidades negativas."
        }
    }

    fun mostrarInventario() {
        println("=== INVENTARIO ===")

        for ((ingrediente, cantidad) in inventario) {
            println("$ingrediente: $cantidad")
        }
    }

    fun calcularIngredientesNecesarios(
        productos: List<Producto>
    ): Map<String, Int> {

        val ingredientesNecesarios = mutableMapOf<String, Int>()

        for (producto in productos) {
            for ((ingrediente, cantidadPorPizza) in producto.ingredientes) {

                require(cantidadPorPizza > 0) {
                    "La cantidad requerida de $ingrediente debe ser positiva."
                }

                val acumulado = ingredientesNecesarios[ingrediente] ?: 0

                ingredientesNecesarios[ingrediente] =
                    Math.addExact(acumulado, cantidadPorPizza)
            }
        }

        return ingredientesNecesarios
    }

    fun verificarDisponibilidad(
        ingredientesNecesarios: Map<String, Int>
    ): Boolean {

        val faltantes = mutableListOf<String>()

        for ((ingrediente, cantidadNecesaria) in ingredientesNecesarios) {

            if (cantidadNecesaria <= 0) {
                println("Cantidad requerida inválida para $ingrediente.")
                return false
            }

            val disponible = inventario[ingrediente] ?: 0

            if (disponible < 0) {
                println("Inventario inconsistente para $ingrediente.")
                return false
            }

            if (disponible < cantidadNecesaria) {
                val faltante = cantidadNecesaria - disponible

                faltantes.add(
                    "$ingrediente (disponible: $disponible, " +
                    "necesario: $cantidadNecesaria, faltan: $faltante)"
                )
            }
        }

        return when {
            faltantes.isEmpty() -> {
                println("Todos los ingredientes están disponibles.")
                true
            }

            else -> {
                println("No hay ingredientes suficientes.")

                for (faltante in faltantes) {
                    println("- $faltante")
                }

                println()
                println("Pasos para reabastecer:")
                println("1. Identificar los ingredientes insuficientes.")
                println("2. Reponer al menos la cantidad faltante.")
                println("3. Actualizar el inventario.")
                println("4. Volver a verificar el pedido.")

                false
            }
        }
    }

    fun descontarIngredientes(
        ingredientesNecesarios: Map<String, Int>
    ): Boolean {

        // Validar todas las cantidades antes de modificar el inventario.
        if (!verificarDisponibilidad(ingredientesNecesarios)) {
            println("Descuento cancelado: inventario sin modificaciones.")
            return false
        }

        // Solo descontamos cuando todos los ingredientes están disponibles.
        for ((ingrediente, cantidadNecesaria) in ingredientesNecesarios) {
            inventario[ingrediente] =
                inventario.getValue(ingrediente) - cantidadNecesaria
        }

        return true
    }
}
