import os
import cv2
import numpy as np

# Hyper-Parameter
CONST_FLOAT_PER = 0.93

# 경로 설정
CONST_IMAGE_PATH = 'test/2024-01-19/'
CONST_OUTPUT_PATH = f'final_result_{CONST_FLOAT_PER}.txt'


# 이미지의 3개 채널(R, G, B)에 대한 히스토그램을 계산
def calculate_histogram(image):
    hist_channels = [cv2.calcHist([image], [i], None, [256], [0, 256]) for i in range(3)]
    hist = np.concatenate(hist_channels)
    hist = cv2.normalize(hist, hist).flatten()
    return hist


# 두 히스토그램 사이의 유사도 계산
def calculate_similarity(hist1, hist2):
    return cv2.compareHist(hist1, hist2, cv2.HISTCMP_CORREL)


# 주어진 폴더 내 모든 JPG 이미지 파일의 파일명을 가져옴
def get_jpg_file_names(CONST_IMAGE_PATH):
    jpg_files = [f for f in os.listdir(CONST_IMAGE_PATH) if os.path.isfile(os.path.join(CONST_IMAGE_PATH, f))
                 and f.lower().endswith('.jpg')]
    return jpg_files


# 유사도 결과를 텍스트 파일에 저장
def save_similarity_results(base_image_path, compare_image_path, similarity, CONST_OUTPUT_PATH):
    if similarity >= CONST_FLOAT_PER:
        with open(CONST_OUTPUT_PATH, 'a') as file:
            file.write(f'유사도 {compare_image_path}): {similarity}\n')


def main():

    jpg_files = get_jpg_file_names(CONST_IMAGE_PATH)

    if jpg_files:
        # 첫 번째 이미지를 기준 이미지로 선택
        base_image_path = os.path.join(CONST_IMAGE_PATH, jpg_files[0])
        base_image = cv2.imread(base_image_path)
        base_hist = calculate_histogram(base_image)

        # 결과 파일 초기화
        open(CONST_OUTPUT_PATH, 'w').close()

        # 나머지 이미지와의 유사도 계산 및 저장
        for jpg_file in jpg_files[1:]:
            compare_image_path = os.path.join(CONST_IMAGE_PATH, jpg_file)
            compare_image = cv2.imread(compare_image_path)
            compare_hist = calculate_histogram(compare_image)

            similarity = calculate_similarity(base_hist, compare_hist)
            print(similarity)
            save_similarity_results(base_image_path, compare_image_path, similarity, CONST_OUTPUT_PATH)

        print(f'결과가 {CONST_OUTPUT_PATH}에 저장되었습니다.')
    else:
        print('JPG 이미지 파일이 없습니다.')

if __name__ == "__main__":
    main()