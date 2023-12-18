var display = $("#display");
var operando1 = "";
var operatore = "";
var operando2 = "";

function aggiungiNumero(numero) {
    display.val(display.val() + numero);
}

function aggiungiDecimal(decimal) {
    if (display.val().indexOf(decimal) === -1) {
        display.val(display.val() + decimal);
    }
}

function eseguiOperazione(op) {
    operando1 = display.val();
    operatore = op;
    display.val("");
}

function calcolaRisultato() {
    operando2 = display.val();
    var calcolatriceData = {
        parametro1: parseFloat(operando1),
        parametro2: parseFloat(operando2)
    };

    $.ajax({
        type: "POST",
        url: "/api/" + operatore,
        contentType: "application/json",
        data: JSON.stringify(calcolatriceData),
        headers: {
            "Authorization": "Bearer " + localStorage.getItem("token")
        },
        success: function (response) {
            display.val(response);
        },
        error: function (error) {
            if (error.status === 400 && error.responseJSON && error.responseJSON.message) {
                // Divisione per zero, mostra un popup di errore
                alert("Errore di calcolatrice: " + error.responseJSON.message);
            } else {
                console.error("Errore di calcolatrice:", error.responseJSON.message);
                alert("Errore di calcolatrice: " + error.responseJSON.message);
            }
        }
    });
}

function cancella() {
    display.val("");
    operando1 = "";
    operatore = "";
    operando2 = "";
}
