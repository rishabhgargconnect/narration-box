import json
import base64
import requests

URL = "http://localhost:8080/images/"

selected_option = int(input("Select your option:\n1. Upload a image. \n2. Upload a folder of images."))
if(selected_option == 1):
    path_of_file = input('Enter path of file:')
    identity = input('What is this character?').lower()
    emotion =  input('How does this character feel right now?').lower()
    
    try: 
        with open(path_of_file, "rb") as image_file:
            encoded_string = base64.b64encode(image_file.read())
        json_data = {
            'identity': identity,
            'emotion': emotion,
            'file' : str(encoded_string)
        }
        print(json_data)
        headers = {'content-type':'application/json'}
        r = requests.post(URL, data = json.dumps(json_data), headers = headers)
        print(r)
    except FileNotFoundError:
        print("No such file as "+ path_of_file)
elif selected_option == 2:
    print("I haven't programmed that path yet.")
else:
    print("Option menu: I am a joke to you?")