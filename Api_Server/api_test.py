from flask import Flask, request, jsonify

app = Flask(__name__)

@app.route('/')
def index():
    return 'working'

@app.route('/generate', methods=['POST'])
def generate():
    # Get user prompt from request data
    prompt = request.json['prompt']
    
    # Generate a mock response
    response = "This is a mock response."
    
    # Return generated response as JSON
    return jsonify({'response': response})

if __name__ == '__main__':
    app.run()