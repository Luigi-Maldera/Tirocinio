function performLogin() {
    var username = $("#username").val();
    var password = $("#password").val();

    var loginData = {
        username: username,
        password: password
    };

    $.ajax({
        type: "POST",
        url: "/auth/login",
        contentType: "application/json",
        data: JSON.stringify(loginData),
        success: function(response) {
            // Gestisci la risposta di successo (ad esempio, salva il token)
            console.log("Login successo:", response);
            alert("Login successo!");
            
            // Salva il token nel localStorage
            localStorage.setItem("token", response.token);

            // Reindirizza alla pagina della calcolatrice
            window.location.href = "/calcolatrice.html";
        },
        error: function(error) {
            // Gestisci l'errore di login
            console.error("Errore di login:", error.responseJSON.message);
            alert("Errore di login: " + error.responseJSON.message);
        }
    });
}
