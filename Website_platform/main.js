function postMessage(userInput){
    fetch('http://127.0.0.1:5000'{
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({ prompt: userInput})
    })
    .then(response => response.json())
    .then(data => {
        const ul = document.querySelector('.listcontainer ul')
    })
}