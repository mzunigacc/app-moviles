package model

class Cliente(
    nombre: String,
    contacto: String,
    val correo: String
) : Usuario(nombre, contacto) {

    override fun obtenerDescripcion(): String {
        return "Cliente: $nombre | Teléfono: $contacto | Correo: $correo"
    }
}
