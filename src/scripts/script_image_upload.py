import json
import base64
import requests
import os

URL = "https://narration-box.herokuapp.com/images/"

selected_option = int(input("Select your option:\n1. Upload a image. \n2. Upload a folder of images."))

def base64_encode_file_contents(path_of_file):
    try:
        with open(path_of_file, "rb") as image_file:
            encoded_string = base64.standard_b64encode(image_file.read(), )
            return encoded_string
    except FileNotFoundError:
        print("No such file as "+ path_of_file)
        return ""

if(selected_option == 1):
    path_of_file = input('Enter path of file:')
    identity = input('What is this character?').lower()
    emotion =  input('How does this character feel right now?\n').lower()
    encoded_string = base64_encode_file_contents(path_of_file)
    json_data = {
        'identity': identity,
        'emotion': emotion,
        'file' : str(encoded_string, 'utf-8')
    }
    print(json_data)
    headers = {'content-type':'application/json'}
    r = requests.post(URL, data = json.dumps(json_data), headers = headers)
    print(r)

elif selected_option == 2:
    path_of_folder = input('Enter path of folder:')
    name_of_folder = os.path.basename(path_of_folder)
    print("**************Begining to upload. Folder name is considered as the type of the character" +
        "and the name of the emotion is considered to be its emotion.*******************")
    for file in os.listdir(path_of_folder):
        full_file_path = os.path.join(path_of_folder, file)
        encoded_string = base64_encode_file_contents(full_file_path)
        json_data = {
            'identity': name_of_folder,
            'emotion' : os.path.splitext(file)[0],
            'file' : str(encoded_string)
        }
        #print(json_data)
        headers = {'content-type':'application/json'}
        r = requests.post(URL, data = json.dumps(json_data), headers = headers)
        print(r)

    print("***********Upload finished*************")
else:
    print("Option menu: I am a joke to you?")