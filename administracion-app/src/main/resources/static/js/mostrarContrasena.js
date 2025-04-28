//funcion para mostrar u ocultar la contraseña
function mostrarContrasena() {
    let campocontrasena = document.getElementById("contrasena");
    let campocontrasena2 = document.getElementById("contrasena1");
    let campocontrasena3 = document.getElementById("contrasena2");
    let checkbox = document.getElementById("mostrarcontrasena");
    campocontrasena.type = checkbox.checked ? "text" : "password";
    campocontrasena2.type = checkbox.checked ? "text" : "password";
    campocontrasena3.type = checkbox.checked ? "text" : "password";
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