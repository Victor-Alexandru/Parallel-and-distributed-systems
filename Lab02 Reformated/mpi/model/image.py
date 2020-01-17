from PIL import Image
import math
import timeit


class ImagePrint:
    def __init__(self, image_path):
        self.path = image_path
        self.initialize_values(self.path)
        super().__init__()

    def get_height(self):
        return self.height

    def get_width(self):
        return self.width

    def initialize_values(self, image_path):
        self.image = Image.open(image_path, 'r')
        self.width, self.height = self.image.size
        # ex: an item is of form  (89, 70, 56), (92, 73, 59)]
        self.rgb_values = list(self.image.getdata())

    def gray_scale_image(self):
        gray_scale_img = []
        ## this function can be split
        for y in range(0, self.height):  # each pixel has coordinates
            for x in range(0, self.width):
                RGB = self.image.getpixel((x, y))
                R, G, B = RGB
                sum = int(R) + int(G) + int(B)
                gray_scale_img.append([])
                gray_scale_img[-1].append((int(R) + int(G) + int(B)) // 3)
                gray_scale_img[-1].append((int(R) + int(G) + int(B)) // 3)
                gray_scale_img[-1].append((int(R) + int(G) + int(B)) // 3)

        return gray_scale_img

    def getRGB(self):
        return self.rgb_values

    def get_raw_colors_to_grayscale(self):
        rez = []
        for tuple in self.rgb_values:
            R, G, B = tuple
            avg = int(R) * 0.299 + int(G) * 0.587 + int(B) * 0.144
            avg = math.ceil(avg)
            if avg > 255:
                avg = 255
            rez.append(avg)
            rez.append(avg)
            rez.append(avg)
        return rez

    def get_raw_colors_to_white_filter(self):
        rez = []
        for tuple in self.rgb_values:
            R, G, B = tuple
            avg = int(R) + int(G) + int(B) // 3
            avg = math.ceil(avg)
            if avg > 255:
                avg = 255
            rez.append(avg)
            rez.append(avg)
            rez.append(avg)
        return rez

    def transform_save_gray(self, raw_rgb_values):

        colors = bytes(raw_rgb_values)

        img = Image.frombytes('RGB', (self.width, self.height), colors)

        name = self.image.filename[:len(self.image.filename) - 4]

        img.save(
            'C:\\university\\Parallel-and-distributed-systems\\Lab02 Reformated\\mpi\\results\\' + name + 'grayscale.jpg')

    def transform_save_white(self, raw_rgb_values):

        colors = bytes(raw_rgb_values)

        img = Image.frombytes('RGB', (self.width, self.height), colors)

        name = self.image.filename[:len(self.image.filename) - 4]

        img.save('C:\\university\\Parallel-and-distributed-systems\\Lab02 Reformated\\mpi\\results\\' + name + 'white.jpg')


# Test done for logging purposes --liniar run
# imgPr = ImagePrint('100x133.jpg')
# imgPrTwo = ImagePrint('1200x56.jpg')
# imgPrThree = ImagePrint('1920x1080.jpg')
#
# start = timeit.default_timer()
# imgPr.transform_save_gray(imgPr.get_raw_colors_to_grayscale())
# stop = timeit.default_timer()
# print('Time for grayscale: ', stop - start, ' for the height ', imgPr.get_height(), '  for the width ', imgPr.get_width())
#
# start = timeit.default_timer()
# imgPr.transform_save_white(imgPr.get_raw_colors_to_white_filter())
# stop = timeit.default_timer()
# print('Time for white filter: ', stop - start, ' for the height ', imgPr.get_height(), '  for the width ', imgPr.get_width())
#
# start = timeit.default_timer()
# imgPrTwo.transform_save_gray(imgPrTwo.get_raw_colors_to_grayscale())
# stop = timeit.default_timer()
# print('Time for grayscale: ', stop - start, ' for the height ', imgPrTwo.get_height(), '  for the width ', imgPrTwo.get_width())
#
# start = timeit.default_timer()
# imgPrTwo.transform_save_white(imgPrTwo.get_raw_colors_to_white_filter())
# stop = timeit.default_timer()
# print('Time for white filter: ', stop - start, ' for the height ', imgPrTwo.get_height(), '  for the width ', imgPrTwo.get_width())
#
# start = timeit.default_timer()
# imgPrThree.transform_save_gray(imgPrThree.get_raw_colors_to_grayscale())
# stop = timeit.default_timer()
# print('Time for grayscale: ', stop - start, ' for the height ', imgPrThree.get_height(), '  for the width ', imgPrThree.get_width())
#
#
# start = timeit.default_timer()
# imgPrThree.transform_save_white(imgPrThree.get_raw_colors_to_white_filter())
# stop = timeit.default_timer()
# print('Time for white filter: ', stop - start, ' for the height ', imgPrThree.get_height(), '  for the width ', imgPrThree.get_width())

