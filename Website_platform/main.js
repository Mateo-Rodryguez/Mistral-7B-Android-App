function processUserInput(userInput) {
    // processing user input
    addChatMessage("You: " + userInput, true);
    
    fetch('http:localhost:5000/generate',{
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Accept': 'application/json'
        },
        body: JSON.stringify({promt: userInput})
    })
    .then(response => response.json())
    .then(data =>{
        // Adding the bot's response to the chat
        addChatMessage("Bot: " + data.response, false);
    })
    .catch(error => console.error('Error:', error));
}
function addChatMessage(message, isUser) {
    chatAdapter.add({message: message, isUser: isUser})
}