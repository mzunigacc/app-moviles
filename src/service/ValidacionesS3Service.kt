package service

object ValidacionesS3Service {

    private val correoRegex = Regex(
        """^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$"""
    )

    private val telefonoRegex = Regex(
        """^(\d{2})(\d{3})(\d{3})(\d{4})$"""
    )

    fun validarCorreo(correo: String): Boolean {
        return correoRegex.matches(correo.trim())
    }

    fun formatearTelefono(telefono: String): String? {
        val limpio = telefono.trim()
            .removePrefix("+")
            .replace(Regex("""[\s()-]"""), "")

        val coincidencia = telefonoRegex.matchEntire(limpio)
            ?: return null

        val (pais, area, bloque1, bloque2) =
            coincidencia.destructured

        return "+$pais ($area) $bloque1-$bloque2"
    }

    fun solicitarCorreo(
        leer: () -> String? = { readlnOrNull() }
    ): String? {

        while (true) {
            println("Ingrese el correo electrónico:")
            val entrada = leer() ?: return null
            val correo = entrada.trim()

            if (validarCorreo(correo)) {
                println("Correo válido: $correo")
                return correo
            }

            println(
                "Error: correo inválido. " +
                "Use el formato nombre@dominio.com e inténtelo nuevamente."
            )
        }
    }

    fun solicitarCantidad(
        leer: () -> String? = { readlnOrNull() }
    ): Int? {

        while (true) {
            println("Ingrese una cantidad entre 1 y 100:")
            val entrada = leer() ?: return null
            val cantidad = entrada.trim().toIntOrNull()

            if (cantidad != null && cantidad in 1..100) {
                println("Cantidad válida: $cantidad")
                return cantidad
            }

            println(
                "Error: la cantidad debe ser un número entero " +
                "entre 1 y 100. Inténtelo nuevamente."
            )
        }
    }

    fun solicitarTelefono(
        leer: () -> String? = { readlnOrNull() }
    ): String? {

        while (true) {
            println("Ingrese el teléfono:")
            val entrada = leer() ?: return null
            val telefono = formatearTelefono(entrada)

            if (telefono != null) {
                println("Teléfono formateado: $telefono")
                return telefono
            }

            println(
                "Error: teléfono inválido. " +
                "Use el formato +XX (XXX) XXX-XXXX."
            )
        }
    }
}
