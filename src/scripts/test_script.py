#%%
import os
from os.path import join, getsize
import json
import base64
import requests
import os
import io
from PIL import Image

#%%
URL = "https://narration-box.herokuapp.com/images/"

#%%
def image_to_byte_array(image:Image, format):
  imgByteArr = io.BytesIO()
  image.save(imgByteArr, format=format)
  imgByteArr = imgByteArr.getvalue()
  return imgByteArr

#%%
def base64_encode_file_contents(image, image_format):
    try:
        encoded_string = base64.standard_b64encode(image_to_byte_array(image, image_format))
        return encoded_string
    except:
        print("Encode error for file: " + image.file)
        return ""

def is_path_an_image_file(full_file_path):
    if(os.path.isdir(full_file_path)):
        return False
    try:
        image = Image.open(full_file_path)
        image.close()
    except IOError:
        return False
        
    return True

def resize_and_encode(full_file_path, dim):
    image = Image.open(full_file_path)
    image_format = image.format
    image = image.resize(dim,Image.ANTIALIAS)

    encoded_string = base64_encode_file_contents(image, image_format)
    return encoded_string

#%%
def upload_folder(path_of_folder, root):
    name_of_folder = os.path.basename(path_of_folder)
    for file in os.listdir(path_of_folder):
        full_file_path = os.path.join(path_of_folder, file)
        if(is_path_an_image_file(full_file_path)):
            encoded_string = resize_and_encode(full_file_path, (256, 256))
            json_data = {
                'path': str.replace(os.path.relpath(path_of_folder, root), '\\', '/'),
                'identity': name_of_folder,
                'emotion' : os.path.splitext(file)[0],
                'file' : str(encoded_string)
            }
            headers = {'content-type':'application/json'}
            r = requests.post(URL, data = json.dumps(json_data), headers = headers)
            print(r)

            if(os.path.splitext(file)[0] == 'default'):
                encoded_string = resize_and_encode(full_file_path, (48, 48))
                json_data = {
                    'path': str.replace(os.path.relpath(path_of_folder, root), '\\', '/'),
                    'identity': name_of_folder,
                    'emotion' : 'thumbnail',
                    'file' : str(encoded_string)
                }
                headers = {'content-type':'application/json'}
                r = requests.post(URL, data = json.dumps(json_data), headers = headers)
                print(r)


#%%
directory_path ="C:\\Users\\nbhat\\Documents"
def parse_for_characters(directory_path): 
    for root, _, files in os.walk(directory_path):
        if files is not None and 'default.png' in files:
            yield root

for path_of_character in parse_for_characters(directory_path):
    upload_folder(path_of_character, directory_path)


#%%
