# %%
import os
from os.path import join, getsize
import json
import base64
import requests
import os
import io
import math
from PIL import Image

# %%
URL = "https://narration-box.herokuapp.com/images/"
# switch if you want to debug script without actually sending content
disable_sending_requests = False

# %%
def image_to_byte_array(image: Image, format):
    imgByteArr = io.BytesIO()
    image.save(imgByteArr, format=format)
    imgByteArr = imgByteArr.getvalue()
    return imgByteArr

def send_http_request(data):
    headers = {'content-type': 'application/json'}
    if not disable_sending_requests:
        r = requests.post(URL, data=json.dumps(data), headers=headers)
        print(r)
        print(data['type'])

# %%


def base64_encode_file_contents(image, image_format):
    try:
        encoded_string = base64.standard_b64encode(
            image_to_byte_array(image, image_format))
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
    resize_dimensions = (dim, math.floor(
        image.size[1] / image.size[0] * (dim)))
    image = image.resize(resize_dimensions, Image.ANTIALIAS)

    encoded_string = base64_encode_file_contents(image, image_format)
    return encoded_string

# %%


def upload_character(path_of_folder, root):
    name_of_folder = os.path.basename(path_of_folder)
    upload_folder(path_of_folder, root, 'character')
    for file in os.listdir(path_of_folder):
        full_file_path = os.path.join(path_of_folder, file)
        if(is_path_an_image_file(full_file_path)):
            encoded_string = resize_and_encode(full_file_path, 256)
            json_data = {
                'path': "/" + str.replace(os.path.relpath(path_of_folder, root), '\\', '/'),
                'identity': name_of_folder,
                'emotion': os.path.splitext(file)[0],
                'file': str(encoded_string),
                'type': 'emotion'
            }
            send_http_request(json_data)
            print(json_data['path'])

            if(os.path.splitext(file)[0] == 'default'):
                encoded_string = resize_and_encode(full_file_path, 64)
                json_data = {
                    'path': "/" + str.replace(os.path.relpath(path_of_folder, root), '\\', '/'),
                    'identity': name_of_folder,
                    'emotion': 'thumbnail',
                    'file': str(encoded_string),
                    'type': 'emotion'
                }
                send_http_request(json_data)


def upload_folder(path_of_folder, root, type):
    name_of_folder = os.path.basename(path_of_folder)
    json_data = {
        'path': "/" + str.replace(os.path.relpath(path_of_folder, root), '\\', '/'),
        'identity': name_of_folder,
        'file': "",  # add thumbnail for folder if required
        'type': type
    }
    send_http_request(json_data)


# %%
directory_path = r"C:\Users\nbhat\Documents\Comic\Image folder\images"


def parse_for_characters(directory_path):
    for root, dirs, files in os.walk(directory_path, topdown=True):
        dirs[:] = [d for d in dirs if d != '.svn']
        if files is not None and 'default.png' in files:
            yield (root, 'Character')
        else:
            yield(root, 'Category')


for (path_of_resource, type) in parse_for_characters(directory_path):
    if type == 'Character':
        upload_character(path_of_resource, directory_path)
    elif type == 'Category':
        upload_folder(path_of_resource, directory_path, 'category')
