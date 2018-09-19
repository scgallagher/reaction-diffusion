import numpy as np
from PIL import Image

def draw_square(matrix, side_length=None, center=None):

    if not side_length:
        side_length = int(len(matrix) / 5)

    height = matrix.shape[0]
    width = matrix.shape[1]

    if not center:
        center = (int(width / 2), int(height / 2))
    center_x, center_y = center

    low_x_bound = int(center_x - side_length / 2)
    high_x_bound = int(center_x + side_length / 2)
    low_y_bound = int(center_y - side_length / 2)
    high_y_bound = int(center_y + side_length / 2)

    for i in range(low_y_bound, high_y_bound):
        for j in range(low_x_bound, high_x_bound):
            matrix[i, j] = (0, 1)

    return matrix

def initialize_matrix(height=1000, width=1000):

    matrix = np.full((height, width), fill_value=(1, 0), dtype=(float, 2))

    return draw_square(matrix, side_length=4)

def sum_laplace(x, y):

    sum_a = 0
    sum_b = 0

    for i in range(3):
        row = x + (i - 1)
        if row < 0: row = height - 1
        elif row >= height: row = 0
        for j in range(3):
            col = y + (j - 1)
            if col < 0: col = width - 1
            elif col >= width: col = 0
            a, b = matrix[row, col]
            sum_a += a * conv_matrix[i, j]
            sum_b += b * conv_matrix[i, j]

    return (sum_a, sum_b)

def step(matrix, rates):

    diff_rate_a, diff_rate_b, feed_rate, kill_rate = rates
    new_matrix = np.empty((height, width), dtype=(float, 2))

    for x in range(height):
        for y in range(width):

            laplace_a, laplace_b = sum_laplace(x, y)
            a, b = matrix[x, y]
            new_a = diff_rate_a * laplace_a - a * b * b + feed_rate * (1 - a)
            new_b = diff_rate_b * laplace_b - a * b * b + (kill_rate + feed_rate) * b

            new_matrix[x, y] = (new_a, new_b)

    return new_matrix

def display_matrix(matrix):

    b_color = [0, 0, 0]
    background_color = [255, 255, 255]

    img_matrix = np.empty((height, width, 3))

    for i in range(height):
        for j in range(width):
            if matrix[i, j, 1] > 0.6:
                img_matrix[i, j] = b_color
            else:
                img_matrix[i, j] = background_color

    img = Image.fromarray(np.uint8(img_matrix), 'RGB')
    img.show()

conv_matrix = np.array([[0.05, 0.2, 0.05], [0.2, -1, 0.2], [0.05, 0.2, 0.05]])
width = 240
height = 135

steps = 21
matrix = initialize_matrix(height, width)

for i in range(steps):

    if i % 10 == 0:
        print(np.amax(matrix))
        #display_matrix(matrix)

    matrix = step(matrix, (1.0, 0.5, 0.0367, 0.0649))
