package model

class Repartidor(
    nombre: String,
    contacto: String,
    val distanciaKm: Double
) : Usuario(nombre, contacto) {

    fun entregarPedido(): String {
        return "$nombre está realizando la entrega del pedido."
    }

    override fun obtenerDescripcion(): String {
        return "Repartidor: $nombre | Distancia: $distanciaKm km"
    }
}
