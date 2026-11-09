package model

import annotation.Promocionable

@Promocionable(
    descuentoPorcentaje = 5,
    descripcion = "Promoción especial en pizzas seleccionadas"
)
class PizzaPromocional(
    nombre: String,
    precio: Double,
    ingredientes: Map<String, Int>
) : Producto(
    nombre = nombre,
    precio = precio,
    ingredientes = ingredientes,
    categoria = "Pizza"
)
