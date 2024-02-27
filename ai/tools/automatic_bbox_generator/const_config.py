import random

CONST_EXT_IMAGES = ".jpg"
CONST_EXT_LABEL = ".txt"

CONST_LIST_COLORS = []

def set_colors():
    for x in range(0, 256, 85):
        for y in range(0, 256, 85):
            for z in range(0, 256, 64):
                CONST_LIST_COLORS.append((x, y, z))
            z = 255
            CONST_LIST_COLORS.append((x, y, z))

    random.shuffle(CONST_LIST_COLORS)

set_colors()