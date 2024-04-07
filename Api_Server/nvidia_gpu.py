import torch
from transformers import AutoModelForCausalLM, AutoTokenizer
 
# Load model and tokenizer
model = AutoModelForCausalLM.from_pretrained("mistralai/Mistral-7B-v0.1")
tokenizer = AutoTokenizer.from_pretrained("mistralai/Mistral-7B-v0.1")
 
# Define device
device = "cuda" if torch.cuda.is_available() else "cpu"
 
# Move model to device
model.to(device)
 
# Set maximum length for generated text
max_length = 100  # Adjust as needed
 
# Interactive conversation loop
while True:
    # User input prompt
    prompt = input("You: ")
 
    # Tokenize prompt and move to device
    model_inputs = tokenizer([prompt], return_tensors="pt").to(device)
 
    # Generate text with maximum length constraint
    generated_ids = model.generate(
        **model_inputs,
        max_length=max_length,
        do_sample=True
    )
 
    # Decode generated text
    generated_text = tokenizer.decode(generated_ids[0], skip_special_tokens=True)
 
    # Print AI response
    print("AI:", generated_text)