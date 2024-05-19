function postMessage() {
    const userInput = document.querySelector('.input-container input[type="text"]').value;
    fetch('http:localhost:5000/generate',{
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({promt: userInput})
    })
    .then(response => response.json())
    .then(data =>{
        const ul = document.querySelector('.list-container ul');
        const li = document.createElement('li');
        li.textContent = 'Bot: ${data.response}';
        ul.appendChild(li);
    })
    .catch(error => console.error('Error:', error));
}
document.querySelector('.input-container input[type="submit"]').addEventListener('click', postMessage);