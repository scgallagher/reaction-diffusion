import numpy as np
from PIL import Image

def initialize_matrix(resX=1000, resY=1000):

    low_x_bound = resX / 2 - 10
    high_x_bound = resX /2 + 10
    low_y_bound = resY / 2 - 10
    high_y_bound = resY / 2 + 10
    matrix = np.zeros((resX, resY))

    for (x, y), concentration in np.ndenumerate(matrix):

        if x >= low_x_bound and x <= high_x_bound and y >= low_y_bound and y <= high_y_bound:
            matrix[x, y] = 1

    return matrix

def get_laplace(x, y):

    sum = 0
    for i in range(3):
        row = x + (i - 1)
        for j in range(3):
            col = y + (j - 1)
            sum += matrix[row, col] * conv_matrix[i, j]

    return sum

def step():

    for x in range(width):
        for y in range(height):

            laplace = get_laplace(x, y)

conv_matrix = np.array([[0.05, 0.2, 0.05], [0.2, -1, 0.2], [0.05, 0.2, 0.05]])
width = 1000
height = 1000

matrix = initialize_matrix(width, height)

a_color = [0, 0, 0]
b_color = [255, 255, 255]

img_matrix = np.empty((width, height, 3))

for x in range(width):
    for y in range(height):
        if matrix[x, y] == 1:
            img_matrix[x, y] = a_color
        else:
            img_matrix[x, y] = b_color

#print(img_matrix)

img = Image.fromarray(np.uint8(img_matrix), 'RGB')
img.show()
