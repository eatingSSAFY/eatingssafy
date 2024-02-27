package com.A204.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Service
@PropertySource("classpath:application.yaml")
public class ImageService {
    @Value("${resource_dir}")
    private String RESOURCES_PATH;

    private final Logger logger = LoggerFactory.getLogger(ImageService.class);
    static private final String ORIGIN_FILE_NAME = "/origin.jpeg";
    static private final Integer CropImageHeight = 195;   //메뉴별 세로 길이
    static private final ArrayList<CropInfo> cropInfoList = new ArrayList<>();  //날짜별
    static private final ArrayList<String> cropFilePathList = new ArrayList<>();  //날짜별 crop된 이미지 파일 경로
    static private final ArrayList<Integer> verticalSection = new ArrayList<>(); // 오리진 이미지를 세로로 나누기 위한 구분. (메뉴별)

    private void init() {
        if (cropInfoList.isEmpty()) {
            // others: 헤더 포함하지 않는 only 메뉴 이미지 크롭 정보
            cropInfoList.add(new CropInfo(0, 300));
            IntStream.range(0, 4).forEach(idx -> cropInfoList.add(new CropInfo(0, cropInfoList.get(idx).y + CropImageHeight)));

            verticalSection.add(0);
            verticalSection.add(150);
            IntStream.range(1, 4).forEach(idx -> verticalSection.add(verticalSection.get(idx) + 300));

            // crop한 파일의 경로를 지정
            IntStream.range(0, cropInfoList.size())
                    .forEach(idx -> cropFilePathList.add(RESOURCES_PATH + "/crop_" + idx + ".jpg"));
        }
    }

    /**
     * 이미지를 세로로 분할했을 x가 어느 파트에 속하는가?
     * MenuTitle code 와 동일한 idx로 동작
     */
    public int getVerticalSection(float x) {
        int i;
        for (i = 1; i < verticalSection.size(); i++) {
            if (verticalSection.get(i - 1) < x && x <= verticalSection.get(i)) break;
        }
        return i - 1;
    }

    /**
     * 호스팅 서버에 등록된 이미지 파일을 저장, cropPoints 에 지정된 픽셀만큼 크롭하여 base64 데이터로 변환
     */
    public List<String> process(String fileUrl) {
        init();
        download(fileUrl);
        crop();
        List<String> res = cropFilePathList
                .stream()
                .map(this::convertBase64)
                .toList();
        removeImages();
        return res;
    }

    private void download(String fileUrl) {
        try (BufferedInputStream in = new BufferedInputStream(new URL(fileUrl).openStream());
             FileOutputStream fileOutputStream = new FileOutputStream(RESOURCES_PATH + ORIGIN_FILE_NAME)
        ) {
            byte[] dataBuffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                fileOutputStream.write(dataBuffer, 0, bytesRead);
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
            logger.error("이미지 파일 다운로드 실패");
        }
    }

    private void removeImages() {
        cropFilePathList.forEach(o -> {
            Path path = Paths.get(o);
            try {
                Files.delete(path);
            } catch (IOException e) {
                logger.error(e.getMessage());
                logger.error("크롭 이미지 삭제 실패");
            }
        });

        Path path = Paths.get(RESOURCES_PATH + ORIGIN_FILE_NAME);
        try {
            Files.delete(path);
        } catch (IOException e) {
            logger.error(e.getMessage());
            logger.error("오리진 이미지 삭제 실패");
        }
    }

    /**
     * 지정된 픽셀만큼 크롭/리소스 파일로 별도 저장
     */
    private void crop() {
        BufferedImage image = null;
        try {
            image = ImageIO.read(new File(RESOURCES_PATH + ORIGIN_FILE_NAME));
        } catch (IOException e) {
            logger.error(e.getMessage());
            logger.error("오리진 이미지 파일 불러오기 실패");
        }
        List<Integer> failedIdx = crop(
                IntStream.range(0, cropInfoList.size()).boxed(),
                image
        );
        // 크롭 실패한 이미지가 있는 경우 최대 한 번까지 더 시도
        if (!failedIdx.isEmpty()) {
            logger.warn("크롭에 실패한 이미지 " + failedIdx.size() + "개 존재, 재시도");
            List<Integer> retryResult = crop(failedIdx.stream(), image);
            logger.warn("재시도 결과 " + (failedIdx.size() - retryResult.size()) + "개 성공");
        }
    }

    /**
     * 파라미터로 받은 이미지를 지정된 픽셀만큼 크롭/리소스 파일로 별도 저장
     */
    private List<Integer> crop(Stream<Integer> idList, BufferedImage image) {
        ArrayList<Integer> failedIdx = new ArrayList<>();
        idList
                .forEach(idx -> {
                    CropInfo cropInfo = cropInfoList.get(idx);
                    BufferedImage cropImage = image.getSubimage(cropInfo.x, cropInfo.y, image.getWidth(), CropImageHeight);
                    try {
                        ImageIO.write(cropImage, "JPG", new File(cropFilePathList.get(idx)));
                    } catch (IOException e) {
                        failedIdx.add(idx);
                        logger.warn(e.getMessage());
                        logger.warn(idx + "번 째 이미지 크롭 실패");
                    }
                });
        return failedIdx;
    }

    /**
     * 이미지 파일을 Base64로 변환
     */
    private String convertBase64(String filePath) {
        String base64Image = "";
        try (FileInputStream input = new FileInputStream(filePath)) {
            byte[] bytes = input.readAllBytes();
            base64Image = Base64.getEncoder().encodeToString(bytes);
        } catch (IOException e) {
            logger.error(filePath + " base64 이미지 변환 실패");
        }
        return base64Image;
    }

    /**
     * 이미지 크롭 픽셀 정보 클래스
     */
    static class CropInfo {
        int x, y;

        public CropInfo(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
}
