package service

import java.lang.reflect.Modifier

object ReflectionService {

    fun inspeccionarClase(clase: Class<*>) {

        println("=== REFLECTION: ${clase.simpleName} ===")

        println("Propiedades declaradas:")

        val propiedades = clase.declaredFields
            .filterNot { it.isSynthetic }
            .sortedBy { it.name }

        for (propiedad in propiedades) {
            println(
                "- ${propiedad.name}: ${propiedad.type.simpleName}"
            )
        }

        println("Métodos públicos declarados:")

        val metodos = clase.declaredMethods
            .filter { metodo ->
                Modifier.isPublic(metodo.modifiers) &&
                    !metodo.isSynthetic &&
                    !metodo.name.contains("\$default")
            }
            .sortedBy { it.name }

        for (metodo in metodos) {
            val parametros = metodo.parameterTypes.joinToString {
                it.simpleName
            }

            println(
                "- ${metodo.name}($parametros): " +
                    metodo.returnType.simpleName
            )
        }

        println(
            "Total propiedades: ${propiedades.size}"
        )
        println(
            "Total métodos públicos: ${metodos.size}"
        )
    }
}
