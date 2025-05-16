// Función para añadir una nueva línea de nómina en el contenedor
function anadirLineaNomina() {
    // Obtiene el contenedor donde se añaden las líneas
    const container = document.getElementById("lineasNominaContainer");
    // Cuenta cuántas líneas hay actualmente para usar el índice correcto
    const numLineas = container.querySelectorAll('.linea-nomina').length;

    // Crea un nuevo div para la línea con clases para layout horizontal y estilo Bootstrap
    const nueva = document.createElement("div");
    nueva.className = "linea-nomina d-flex align-items-center gap-3 mb-3 border p-3 rounded";

    // HTML interno de la nueva línea, con inputs para concepto, porcentaje, cantidad y un botón para eliminar
    // El name de los inputs usa el índice para que el backend pueda reconocerlos como arrays
    nueva.innerHTML = `
      <div class="flex-fill">
        <label class="form-label mb-1">Concepto:</label>
        <input type="text" class="form-control form-control-sm" name="lineasNomina[${numLineas}].concepto" />
      </div>
      <div style="width: 120px;">
        <label class="form-label mb-1">Porcentaje:</label>
        <input type="text" class="form-control form-control-sm" name="lineasNomina[${numLineas}].porcentaje" />
      </div>
      <div style="width: 120px;">
        <label class="form-label mb-1">Cantidad:</label>
        <input type="text" class="form-control form-control-sm" name="lineasNomina[${numLineas}].cantidad" />
      </div>
      <button type="button" class="btn btn-danger btn-sm mt-4" onclick="eliminarLinea(this)">–</button>
    `;

    // Añade la nueva línea al contenedor en el DOM
    container.appendChild(nueva);
}

// Función que actualiza los campos de cantidad en base a los porcentajes dados, usando el sueldo base
function actualizarCantidadesDesdePorcentajes() {
    // Selecciona todos los inputs de cantidades y porcentajes usando selectores específicos
    const cantidades = document.querySelectorAll('input[name^="lineasNomina"][name$=".cantidad"]');
    const porcentajes = document.querySelectorAll('input[name^="lineasNomina"][name$=".porcentaje"]');

    // El primer campo cantidad es siempre el sueldo base, se usa para calcular las cantidades
    const sueldoBaseInput = cantidades[0];
    const sueldoBase = parseFloat(sueldoBaseInput.value);

    // Si el sueldo base no es un número válido, no se actualizan las cantidades
    if (isNaN(sueldoBase)) return;

    // Recorre todas las líneas excepto la primera (sueldo base)
    for (let i = 1; i < porcentajes.length; i++) {
        const porcentajeVal = parseFloat(porcentajes[i].value);

        // Solo actualiza si el porcentaje es un número válido
        if (!isNaN(porcentajeVal)) {
            // Validación: porcentaje no puede ser mayor que 100%
            if (porcentajeVal > 100) {
                alert(`El porcentaje en la línea ${i + 1} no puede superar el 100%.`);
                // Limpia los valores erróneos
                porcentajes[i].value = "";
                cantidades[i].value = "";
                continue;
            }

            // Calcula la cantidad basada en el porcentaje y el sueldo base, con dos decimales
            const nuevaCantidad = (sueldoBase * porcentajeVal / 100).toFixed(2);
            // Actualiza el campo cantidad con el resultado calculado
            cantidades[i].value = nuevaCantidad;
        }
    }
}

// Función para eliminar una línea de nómina al pulsar el botón correspondiente
function eliminarLinea(boton) {
    // Encuentra el div padre más cercano con clase "linea-nomina"
    const fila = boton.closest(".linea-nomina");
    // Elimina esa línea del DOM
    fila.remove();
    // Actualiza los índices de los inputs para que sigan siendo correctos
    actualizarIndices();
    // Actualiza el resumen para reflejar el cambio
    actualizarResumen();
}

// Función para actualizar los nombres de los inputs según su posición tras eliminar una línea
function actualizarIndices() {
    // Selecciona todas las líneas actuales
    const filas = document.querySelectorAll(".linea-nomina");
    // Recorre cada línea y le asigna el índice correcto en los atributos name
    filas.forEach((fila, i) => {
        fila.querySelector('input[name$=".concepto"]').name = `lineasNomina[${i}].concepto`;
        fila.querySelector('input[name$=".porcentaje"]').name = `lineasNomina[${i}].porcentaje`;
        fila.querySelector('input[name$=".cantidad"]').name = `lineasNomina[${i}].cantidad`;
    });
}

// Evento global que detecta cualquier cambio en inputs y actualiza cantidades y resumen automáticamente
document.addEventListener("input", () => {
    actualizarCantidadesDesdePorcentajes();
    actualizarResumen();
});

// Función para actualizar el resumen final con totales de devengos, deducciones y el salario neto
function actualizarResumen() {
    let devengos = 0;   // suma de valores positivos o cero
    let deducciones = 0; // suma de valores negativos en valor absoluto

    // Recorre todos los inputs de cantidad para calcular totales
    document.querySelectorAll('input[name^="lineasNomina"][name$=".cantidad"]').forEach(input => {
        const val = parseFloat(input.value);
        if (!isNaN(val)) {
            if (val >= 0) devengos += val;
            else deducciones += Math.abs(val);
        }
    });

    // Calcula el salario neto restando deducciones a devengos
    const neto = devengos - deducciones;

    // Actualiza el contenido de los elementos HTML que muestran estos totales con dos decimales
    document.getElementById("totalDevengos").innerText = devengos.toFixed(2);
    document.getElementById("totalDeducciones").innerText = deducciones.toFixed(2);
    document.getElementById("salarioNeto").innerText = neto.toFixed(2);
}

// Función para enviar los datos de la nómina al servidor vía fetch
function enviarNomina() {
    // Obtiene datos del formulario
    const empleadoId = document.getElementById("empleadoId").value;
    const fechaInicio = document.querySelector('input[name="fechaInicio"]').value;
    const fechaFin = document.querySelector('input[name="fechaFin"]').value;

    const lineas = [];
    // Selecciona todos los inputs de concepto, porcentaje y cantidad para enviarlos
    const conceptos = document.querySelectorAll('input[name^="lineasNomina"][name$=".concepto"]');
    const porcentajes = document.querySelectorAll('input[name^="lineasNomina"][name$=".porcentaje"]');
    const cantidades = document.querySelectorAll('input[name^="lineasNomina"][name$=".cantidad"]');

    // Validaciones básicas para fechas
    if (!fechaInicio || !fechaFin) {
        alert("Introduce las fechas de inicio y fin.");
        btnGuardar.disabled = false;
        return;
    }

    const formatoFechaRegex = /^\d{4}-\d{2}-\d{2}$/;
    if (!formatoFechaRegex.test(fechaInicio) || !formatoFechaRegex.test(fechaFin)) {
        alert("Las fechas deben tener formato AAAA-MM-DD");
        btnGuardar.disabled = false;
        return;
    }

    // Recorre todas las líneas para validar y preparar los datos
    for (let i = 0; i < conceptos.length; i++) {
        const concepto = conceptos[i].value;
        const porcentaje = porcentajes[i].value ? Number(porcentajes[i].value) : null;
        const cantidad = cantidades[i].value ? Number(cantidades[i].value) : null;

        // Valida que el concepto no esté vacío
        if (!concepto || concepto.trim() === "") {
            alert(`La línea ${i + 1} necesita un concepto.`);
            btnGuardar.disabled = false;
            return;
        }

        // Valida que la cantidad sea un número válido
        if (cantidad === null || isNaN(cantidad)) {
            alert(`La línea ${i + 1} tiene una cantidad inválida.`);
            btnGuardar.disabled = false;
            return;
        }

        // Añade el objeto línea al array
        lineas.push({concepto, porcentaje, cantidad});
    }

    // Valida que haya al menos una línea
    if (lineas.length === 0) {
        alert("Debes añadir al menos una línea.");
        btnGuardar.disabled = false;
        return;
    }

    // Construye el objeto con todos los datos para enviar
    const datos = {
        empleadoId,
        fechaInicio,
        fechaFin,
        lineasNomina: lineas
    };

    // Envía la petición POST al servidor con fetch
    fetch('/api/nominas', {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(datos)
    }).then(response => {
        btnGuardar.disabled = false;
        if (response.ok) {
            alert("Nómina guardada correctamente");
            window.location.href = "/dashboard";
        } else {
            // En caso de error, muestra el mensaje recibido
            response.json().then(err => alert("Error: " + err.error));
        }
    }).catch(error => {
        btnGuardar.disabled = false;
        alert("Error de conexión: " + error);
    });
}