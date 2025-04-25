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