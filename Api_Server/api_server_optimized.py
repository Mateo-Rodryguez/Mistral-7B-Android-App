from flask import Flask, request, jsonify
from transformers import AutoTokenizer, AutoModelForCausalLM

# Initialize Flask app
app = Flask(__name__)

# Load the tokenizer and model
tokenizer = AutoTokenizer.from_pretrained("mistralai/Mistral-7B-Instruct-v0.2")
model = AutoModelForCausalLM.from_pretrained("mistralai/Mistral-7B-Instruct-v0.2")

# Set custom max_length
max_length = 256  # Reduced max_length for faster response

# Function to generate responses
def generate_response(prompt):
    # Encode the prompt
    input_ids = tokenizer.encode(prompt, return_tensors="pt", max_length=max_length, truncation=True)
    
    if input_ids is None or len(input_ids) == 0:
        return "Error: Unable to encode the prompt."
    
    # Generate text
    output = model.generate(input_ids, max_length=150, num_return_sequences=1, temperature=0.7)
    generated_text = tokenizer.decode(output[0], skip_special_tokens=True)
    return generated_text

# Define API endpoint for model inference
@app.route('/generate', methods=['POST'])
def generate():
    # Get user prompt from request data
    prompt = request.json['prompt']
    
    # Generate response
    response = generate_response(prompt)
    
    # Return generated response as JSON
    return jsonify({'response': response})

# Run the Flask app
if __name__ == '__main__':
    app.run(debug=True)