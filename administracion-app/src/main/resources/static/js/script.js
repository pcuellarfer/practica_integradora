//funcion para mostrar u ocultar la contraseña
function script() {
    let campocontrasena = document.getElementById("contrasena");
    let campocontrasena2 = document.getElementById("contrasena2"); // Confirmación de la contraseña
    let checkbox = document.getElementById("mostrarcontrasena");

    // Cambiar el tipo de campo de contraseñas según el estado del checkbox
    campocontrasena.type = checkbox.checked ? "text" : "password";
    campocontrasena2.type = checkbox.checked ? "text" : "password";
}


function mostrarContrasenaRestablecer() {
    var contrasena1 = document.getElementById("contrasena1");
    var contrasena2 = document.getElementById("contrasena2");
    var checkbox = document.getElementById("mostrarcontrasena");

    // Si el checkbox está marcado, se muestran las contraseñas
    if (checkbox.checked) {
        contrasena1.type = "text";
        contrasena2.type = "text";
    } else {
        contrasena1.type = "password";
        contrasena2.type = "password";
    }
}


//Funcion para recuperar la contraseña
function recuperarContrasena() {
    let nombreUsuario = document.getElementById("nombreusuario").value;

    if (!nombreUsuario) {
        alert("Error: No se encontró el usuario.");
        return;
    }

    //solicitud a controlador con el nombreUsuario
    fetch(`/recuperar-contrasena?nombreUsuario=${nombreUsuario}`)
        .then(response => response.text())
        .then(data => {
            if (data.startsWith("Error:")) {
                alert(data); //Mostrar  error si el usuario no se encuentra
            } else {
                alert("Tu contraseña es: " + data); //Mostrar la contraseña
            }
        })
        .catch(error => {
            alert("Error al recuperar la contraseña.");
            console.error(error);
        });
}

function deseleccionarPais() {
    document.getElementById("pais").selectedIndex = 0; // Selecciona la opción vacía (primer valor en la lista)
}

function deseleccionarGenero() {
    var radios = document.getElementsByName("genero");
    for (var i = 0; i < radios.length; i++) {
        radios[i].checked = false;
    }
}

// Configuración de la cuenta atrás (en segundos)
let countdownTime = 300; // 5 minutos (300 segundos)

// Función que actualiza la cuenta atrás y redirige al logout
function startCountdown() {
    let countdownElement = document.getElementById('countdown');
    let timer = setInterval(function() {
        let minutes = Math.floor(countdownTime / 60);
        let seconds = countdownTime % 60;

        // Formato de tiempo "mm:ss"
        countdownElement.innerHTML = minutes + ":" + (seconds < 10 ? "0" + seconds : seconds);

        if (countdownTime <= 0) {
            clearInterval(timer);
            // Redirigir al servidor para cerrar la sesión
            window.location.href = '/login/logout';
        } else {
            countdownTime--;
        }
    }, 1000); // Actualiza cada segundo
}

// Reinicia la cuenta atrás si el usuario realiza alguna acción
function resetCountdown() {
    countdownTime = 300; // Reinicia el contador a 5 minutos
}

// Escuchar eventos para reiniciar el contador cuando el usuario hace clic, mueve el ratón o pulsa una tecla
document.addEventListener('mousemove', resetCountdown);
document.addEventListener('click', resetCountdown);

// Iniciar la cuenta atrás cuando se carga la página
window.onload = startCountdown;
