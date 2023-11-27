// Function to load external HTML file
function loadHTML(url, containerId) {
    var container = document.getElementById(containerId);

    // Create a new XMLHttpRequest object
    var xhr = new XMLHttpRequest();

    // Setup the GET request to the external HTML file
    xhr.open('GET', url, true);

    // Define the onload event handler
    xhr.onload = function () {
        if (xhr.status === 200) {
            container.innerHTML = xhr.responseText;
        }
    };

    // Send the request
    xhr.send();
}

// Load header.html into #header-container
loadHTML('header.html', 'header-container');

// Load footer.html into #footer-container
loadHTML('footer.html', 'footer-container');