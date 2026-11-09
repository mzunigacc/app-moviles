package service

import annotation.Promocionable
import model.Producto

object PromocionService {

    fun obtenerPromocion(producto: Producto): Promocionable? {
        return producto.javaClass.getAnnotation(
            Promocionable::class.java
        )
    }

    fun esPromocionable(producto: Producto): Boolean {
        return obtenerPromocion(producto) != null
    }

    fun identificarPromocionables(
        productos: List<Producto>
    ): List<Producto> {
        return productos.filter { producto ->
            esPromocionable(producto)
        }
    }

    fun mostrarPromociones(productos: List<Producto>) {
        println("=== PRODUCTOS PROMOCIONABLES ===")

        val elegibles = identificarPromocionables(productos)

        if (elegibles.isEmpty()) {
            println("No se encontraron productos promocionables.")
            return
        }

        for (producto in elegibles) {
            val promocion = obtenerPromocion(producto) ?: continue

            println("Producto: ${producto.nombre}")
            println("Categoría: ${producto.categoria}")
            println("Descuento: ${promocion.descuentoPorcentaje}%")
            println("Descripción: ${promocion.descripcion}")
            println()
        }
    }
}
