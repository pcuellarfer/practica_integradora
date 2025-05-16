// Función para mostrar u ocultar las contraseñas de varios campos a la vez
function script() {
    let campocontrasena = document.getElementById("contrasena");
    let campocontrasena2 = document.getElementById("contrasena1");
    let campocontrasena3 = document.getElementById("contrasena2");
    let checkbox = document.getElementById("mostrarcontrasena");

    // Cambia el tipo de input entre "text" y "password" según el estado del checkbox
    campocontrasena.type = checkbox.checked ? "text" : "password";
    campocontrasena2.type = checkbox.checked ? "text" : "password";
    campocontrasena3.type = checkbox.checked ? "text" : "password";
}

// Función para mostrar u ocultar solo el campo de confirmación de contraseña
function mostrarConfContrasena() {
    let confcontrasena = document.getElementById("confirmContrasena");
    let checkbox = document.getElementById("mostrarconfcontrasena");

    // Cambia el tipo del input confirmContrasena según el checkbox
    confcontrasena.type = checkbox.checked ? "text" : "password";
}

// Función para mostrar u ocultar las contraseñas al restablecer contraseña (dos campos)
function mostrarContrasenaRestablecer() {
    var contrasena1 = document.getElementById("contrasena1");
    var contrasena2 = document.getElementById("contrasena2");
    var checkbox = document.getElementById("mostrarcontrasena");

    // Si el checkbox está marcado, muestra las contraseñas en texto plano, sino las oculta
    if (checkbox.checked) {
        contrasena1.type = "text";
        contrasena2.type = "text";
    } else {
        contrasena1.type = "password";
        contrasena2.type = "password";
    }
}

// Función para recuperar la contraseña a partir del nombre de usuario
function recuperarContrasena() {
    let nombreUsuario = document.getElementById("nombreusuario").value;

    if (!nombreUsuario) {
        alert("Error: No se encontró el usuario."); // Valida que el usuario haya ingresado un nombre
        return;
    }

    // Envía una petición al controlador para recuperar la contraseña
    fetch(`/recuperar-contrasena?nombreUsuario=${nombreUsuario}`)
        .then(response => response.text())
        .then(data => {
            if (data.startsWith("Error:")) {
                alert(data); // Muestra error si el usuario no existe
            } else {
                alert("Tu contraseña es: " + data); // Muestra la contraseña recuperada
            }
        })
        .catch(error => {
            alert("Error al recuperar la contraseña."); // Captura y muestra errores en la petición
            console.error(error);
        });
}

// Función para deseleccionar la opción seleccionada en un select de países
function deseleccionarPais() {
    document.getElementById("pais").selectedIndex = 0; // Selecciona la primera opción (vacía)
}

// Función para deseleccionar todos los radio buttons del grupo "genero"
function deseleccionarGenero() {
    var radios = document.getElementsByName("genero");
    for (var i = 0; i < radios.length; i++) {
        radios[i].checked = false; // Marca todos como no seleccionados
    }
}

// Configuración inicial del contador para la sesión, en segundos (5 minutos)
let countdownTime = 300;

// Función que actualiza el contador y redirige al logout cuando el tiempo se acaba
function startCountdown() {
    let countdownElement = document.getElementById('countdown');

    let timer = setInterval(function() {
        let minutes = Math.floor(countdownTime / 60);
        let seconds = countdownTime % 60;

        // Formatea el tiempo en mm:ss y lo muestra en el elemento correspondiente
        countdownElement.innerHTML = minutes + ":" + (seconds < 10 ? "0" + seconds : seconds);

        if (countdownTime <= 0) {
            clearInterval(timer);
            // Tiempo agotado: redirige a la página de cierre de sesión
            window.location.href = '/login/logout';
        } else {
            countdownTime--; // Decrementa el contador cada segundo
        }
    }, 1000); // Ejecuta la función cada 1000 ms (1 segundo)
}

// Función para reiniciar el contador cuando el usuario realiza alguna acción
function resetCountdown() {
    countdownTime = 300; // Reinicia el contador a 5 minutos
}

// Escucha eventos de movimiento del ratón y clicks para reiniciar el contador
document.addEventListener('mousemove', resetCountdown);
document.addEventListener('click', resetCountdown);

// Inicia el contador cuando se carga la página
window.onload = startCountdown;

// Función que confirma el bloqueo de un empleado pidiendo un motivo
function confirmarBloqueo(form) {
    const motivo = form.querySelector('input[name="motivoBloqueo"]').value;

    if (!motivo.trim()) {
        alert("Por favor, indica el motivo del bloqueo."); // Valida que haya motivo
        return false; // Cancela el envío si no hay motivo
    }

    // Pide confirmación para proceder con el bloqueo
    return confirm("¿Estás seguro de que quieres bloquear a este empleado?");
}

// Función que confirma el desbloqueo de un empleado
function confirmarDesbloqueo() {
    // Pide confirmación para proceder con el desbloqueo
    return confirm("¿Estás seguro de que quieres desbloquear a este empleado?");
}

// Función para mostrar un mensaje de confirmación antes de borrar un producto
function borrarProducto(id) {
    if (!confirm('¿Seguro que quieres borrar este producto?')) {
        return;
    }

    // Si el usuario confirma, envía una petición DELETE al servidor
    fetch(`/api/productos/${id}`, {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json'
        }
    })
        .then(response => {
            if (response.ok) {
                alert('Producto borrado correctamente');
                // Recargar la página o eliminar el elemento de la lista dinámicamente
                location.reload();
            } else {
                response.text().then(text => alert('Error: ' + text));
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('Error al borrar el producto');
        });
}