package model

class Repartidor(
    nombre: String,
    contacto: String,
    val distanciaKm: Double,
    var disponible: Boolean = true
) : Usuario(nombre, contacto) {

    fun entregarPedido(): String {
        disponible = false
        return "$nombre está realizando la entrega del pedido."
    }

    fun finalizarEntrega() {
        disponible = true
    }

    override fun obtenerDescripcion(): String {
        val estado = if (disponible) "Disponible" else "Ocupado"

        return "Repartidor: $nombre | Distancia: $distanciaKm km | Estado: $estado"
    }
}
