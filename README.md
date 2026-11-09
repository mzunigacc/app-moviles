# Desarrollo App Móviles I — Sumativa 1, Semana 3

Proyecto en Kotlin/JVM desarrollado para la actividad **Creando soluciones con Kotlin (PMY2201)**.

## Funcionalidades implementadas

- Validación de correos electrónicos mediante Regex, con reingreso ante errores.
- Normalización de teléfonos al formato `+XX (XXX) XXX-XXXX`.
- Validación de cantidades utilizando Ranges (`1..100`).
- Aplicación de promociones según rangos de fechas.
- Anotación personalizada `@Promocionable` para pizzas y combos.
- Identificación dinámica de productos promocionables y sus descuentos.
- Reflection para inspeccionar propiedades y métodos de `Producto` y `Pedido`.
- Sobrecarga del operador `+` para combinar pedidos y recalcular totales.
- Comparación estructural mediante `equals()` y `hashCode()`.
- Eliminación de clientes y productos duplicados mediante `Set`.
- Desestructuración de objetos `Cliente` y `Pedido`.
- Procesamiento de pedidos con validación y descuento seguro del inventario.
- Actualización de estados y disponibilidad de repartidores.
- Resumen integrado de funcionalidades y resultados por consola.

## Tecnologías

- Kotlin/JVM
- JDK 17
- IntelliJ IDEA

## Ejecución

Abrir el proyecto en IntelliJ IDEA y ejecutar `src/Main.kt`.

El programa realiza una demostración completa de las funcionalidades y valida los resultados mediante `check()`.

Al finalizar correctamente, muestra:

`SUMATIVA S3: VALIDACIONES OK`

## Pruebas

Se incluyen programas de prueba independientes:

- `PruebasIntegridad.kt`
- `PruebasCantidades.kt`
- `PruebasOperadores.kt`
- `PruebasReflection.kt`
- `PruebasValidacionesS3.kt`

Cada archivo puede ejecutarse desde IntelliJ IDEA.

## Entrega

Proyecto Kotlin comprimido en formato ZIP, acompañado del repositorio GitHub.
