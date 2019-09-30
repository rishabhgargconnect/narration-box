from PIL import Image
import os

#path = r"C:\Users\nbhat\Documents\Comic\Images\Animals\FarmAnimals\Pig\anger.png"
#image = Image.open(path).transpose(Image.FLIP_LEFT_RIGHT).save(path)

for path in open(r"C:\Users\nbhat\Documents\Comic\Images\flip.txt").read().splitlines():
    if os.path.isfile(path):
        image = Image.open(path).transpose(Image.FLIP_LEFT_RIGHT).save(path)
        print(path)
    elif os.path.isdir(path):
        for file_name in os.listdir(path):
            file_path = os.path.join(path, file_name)
            print(file_path)
            image = Image.open(file_path).transpose(Image.FLIP_LEFT_RIGHT).save(file_path)