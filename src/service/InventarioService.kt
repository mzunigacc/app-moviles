package service

import model.Producto

class InventarioService(
    private val inventario: MutableMap<String, Int>
) {

    fun mostrarInventario() {
        println("=== INVENTARIO ===")

        for ((ingrediente, cantidad) in inventario) {
            println("$ingrediente: $cantidad")
        }
    }

    fun calcularIngredientesNecesarios(
        productos: List<Producto>
    ): Map<String, Int> {

        val ingredientesNecesarios =
            mutableMapOf<String, Int>()

        for (producto in productos) {

            for (
                (ingrediente, cantidadPorPizza)
                in producto.ingredientes
            ) {

                val cantidadAcumulada =
                    ingredientesNecesarios[ingrediente] ?: 0

                ingredientesNecesarios[ingrediente] =
                    cantidadAcumulada + cantidadPorPizza
            }
        }

        return ingredientesNecesarios
    }

    fun verificarDisponibilidad(
        ingredientesNecesarios: Map<String, Int>
    ): Boolean {

        val faltantes = mutableListOf<String>()

        for (
            (ingrediente, cantidadNecesaria)
            in ingredientesNecesarios
        ) {

            val cantidadDisponible =
                inventario[ingrediente] ?: 0

            if (
                cantidadDisponible <
                cantidadNecesaria
            ) {

                faltantes.add(
                    "$ingrediente " +
                    "(disponible: $cantidadDisponible, " +
                    "necesario: $cantidadNecesaria)"
                )
            }
        }

        return when {

            faltantes.isEmpty() -> {

                println(
                    "Todos los ingredientes están disponibles."
                )

                true
            }

            else -> {

                println(
                    "No hay ingredientes suficientes."
                )

                for (faltante in faltantes) {
                    println("- $faltante")
                }

                println(
                    "Se debe reabastecer el inventario " +
                    "antes de aceptar el pedido."
                )

                false
            }
        }
    }

    fun descontarIngredientes(
        ingredientesNecesarios: Map<String, Int>
    ) {

        for (
            (ingrediente, cantidadNecesaria)
            in ingredientesNecesarios
        ) {

            val cantidadActual =
                inventario[ingrediente] ?: 0

            inventario[ingrediente] =
                cantidadActual - cantidadNecesaria
        }
    }
}
