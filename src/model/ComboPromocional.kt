package model

import annotation.Promocionable

@Promocionable(
    descuentoPorcentaje = 8,
    descripcion = "Promoción especial en combos"
)
class ComboPromocional(
    nombre: String,
    precio: Double,
    ingredientes: Map<String, Int>
) : Producto(
    nombre = nombre,
    precio = precio,
    ingredientes = ingredientes,
    categoria = "Combo"
)
