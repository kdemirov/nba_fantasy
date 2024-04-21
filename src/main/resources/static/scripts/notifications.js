setInterval(function () {
    fetch("http://localhost:8080/users/notification", {
        method: "POST",
        headers: {
            "Content-type": "application/json"
        }
    }).then((response) => response.json()).then(data => {
        document.getElementById("notifications").textContent = data.length;
    });

}, 1000 * 60 * 1);