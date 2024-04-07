from transformers import AutoTokenizer, AutoModelForCausalLM

# Load the tokenizer and model
tokenizer = AutoTokenizer.from_pretrained("mistralai/Mistral-7B-Instruct-v0.2")
model = AutoModelForCausalLM.from_pretrained("mistralai/Mistral-7B-Instruct-v0.2")

# Set custom max_length
max_length = 512  # Choose an appropriate value based on your requirements

# Function to generate responses
def generate_response(prompt):
    # Encode the prompt
    input_ids = tokenizer.encode(prompt, return_tensors="pt", max_length=max_length, truncation=True)
    
    if input_ids is None or len(input_ids) == 0:
        print("Error: Unable to encode the prompt.")
        return None
    
    # Create attention mask
    attention_mask = input_ids.ne(tokenizer.pad_token_id) if tokenizer.pad_token_id is not None else None
    
    # Generate text
    output = model.generate(input_ids, attention_mask=attention_mask, pad_token_id=tokenizer.eos_token_id, max_length=150, num_return_sequences=1, temperature=0.7)
    generated_text = tokenizer.decode(output[0], skip_special_tokens=True)
    return generated_text

# Interactive prompt loop
while True:
    prompt = input("You: ")
    if prompt.lower() == "exit":
        print("Exiting...")
        break
    response = generate_response(prompt)
    if response is not None:
        print("AI:", response)
