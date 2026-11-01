package model

open class Usuario(
    val nombre: String,
    val contacto: String
) {
    open fun obtenerDescripcion(): String {
        return "Usuario: $nombre | Contacto: $contacto"
    }
}
