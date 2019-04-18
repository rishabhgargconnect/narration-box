#%%
import json
import base64
import requests
import os

#%%
URL = "https://narration-box.herokuapp.com/images/"

def parse_for_characters(directory_path): 
    for root, _, files in os.walk(directory_path):
        if files is not None and 'default.png'  in files or 'default.psd' in files:
            yield root

def base64_encode_file_contents(path_of_file):
    try:
        with open(path_of_file, "rb") as image_file:
            encoded_string = base64.standard_b64encode(image_file.read(), )
            return encoded_string
    except FileNotFoundError:
        print("No such file as "+ path_of_file)
        return ""


def upload_folder(path_of_folder, root):
    name_of_folder = os.path.basename(path_of_folder)
    if(str.isdigit(name_of_folder)):
        print(f"Character name cannot be {path_of_folder}. Please select a better name.")
        return
    for file in os.listdir(path_of_folder):
        full_file_path = os.path.join(path_of_folder, file)
        if (file in [".svn","Thumbs.db"]):
            continue
        if os.path.isdir(full_file_path):
            continue
        
        encoded_string = base64_encode_file_contents(full_file_path)
        json_data = {
            'path': str.lower(str.replace(os.path.relpath(path_of_folder, root), '\\', '/')),
            'identity': str.lower(name_of_folder),
            'emotion' : str.lower(os.path.splitext(file)[0]),
            'file' : str(encoded_string)
        }
        print(json_data)
        headers = {'content-type':'application/json'}
        r = requests.post(URL, data = json.dumps(json_data), headers = headers)

#%%
selected_option = int(input("Select your option:\n1. Upload a image. \n2. Upload a folder of images.\n3. Upload a directory containing multiple folders"))
root_folder = input('Enter root folder:\n')

#%%
if(selected_option == 1):
    print('Feature down. Go catch the developer')
    """
    path_of_file = input('Enter path of file:')
    identity = input('What is this character?').lower()
    emotion =  input('How does this character feel right now?\n').lower()
    encoded_string = base64_encode_file_contents(path_of_file)
    json_data = {
        'path': str.replace(os.path.relpath(path_of_file, root_folder), '\\', '/'),
        'identity': name_of_folder,
        'emotion' : os.path.splitext(file)[0],
        'file' : str(encoded_string)
    }
    print(json_data)
    headers = {'content-type':'application/json'}
    r = requests.post(URL, data = json.dumps(json_data), headers = headers)
    """
elif selected_option == 2:
    path_of_folder = input('Enter path of folder:')
    name_of_folder = os.path.basename(path_of_folder)
    print("**************Begining to upload. Folder name is considered as the type of the character" +
        "and the name of the emotion is considered to be its emotion.*******************")
    upload_folder(name_of_folder, root_folder)

    print("***********Upload finished*************")

elif selected_option == 3:
    for path_of_character in parse_for_characters(root_folder):
        upload_folder(path_of_character, root_folder)
    print("**************Begining to upload. Folder name is considered as the type of the character " +
        "and the name of the emotion is considered to be its emotion.*******************")

    print("***********Upload finished*************")
else:
    print("Please enter valid options")

#%%
