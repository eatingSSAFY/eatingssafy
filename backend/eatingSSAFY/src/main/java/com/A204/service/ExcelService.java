package com.A204.service;

import com.A204.code.CategoryCode;
import com.A204.domain.NocardPerson;
import org.dhatim.fastexcel.Workbook;
import org.dhatim.fastexcel.Worksheet;
import org.dhatim.fastexcel.reader.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Stream;

@Service
@PropertySource("classpath:application.yaml")
public class ExcelService {
    @Value("${resource_dir}")
    private String RESOURCES_PATH;

    private final Logger logger = LoggerFactory.getLogger(ExcelService.class);
    private final static String TMP_FILE_NAME = "/tmp_excel.xlsx";
    private final static List<String> HEADER = new ArrayList<>(); // no card peron excel header
    private final static Map<String, Integer> ROW_MAP = new HashMap<>();

    private ExcelService() {
        HEADER.add("결과");
        HEADER.add("식당");
        HEADER.add("취식일자");
        HEADER.add("배식일정");
        HEADER.add("DomainID");
        HEADER.add("성명");
        HEADER.add("사번");
        HEADER.add("사업장");
        HEADER.add("취식수");
        for (int i = 0; i < HEADER.size(); i++) {
            ROW_MAP.put(HEADER.get(i), i);
        }
    }

    private String writeExcelNocardPerson() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String fileName = "/" + LocalDate.now().format(formatter) + "-카드미소지자.xlsx";
        try {
            if (Files.exists(Paths.get(RESOURCES_PATH + fileName)))
                deleteNocardPersonExcel(fileName);
            Files.createFile(Paths.get(RESOURCES_PATH + fileName));
        } catch (IOException e) {
            logger.error(e.getMessage());
            logger.error("임시 파일 생성 실패");
        }
        return fileName;
    }

    public String writeNocardPersonExcel(List<NocardPerson> list) {
        String fileName = writeExcelNocardPerson();
        try (OutputStream out = Files.newOutputStream(Paths.get(RESOURCES_PATH + fileName));
             Workbook wb = new Workbook(out, "EatingSSAFY", "1.0")) {
            Worksheet ws = wb.newWorksheet("Sheet 1");

            for (int i = 0; i < HEADER.size(); i++) {
                if (HEADER.get(i).equals("취식일자")) ws.width(i, 30);
                else ws.width(i, 10);
                ws.value(0, i, HEADER.get(i));
            }

            ws.range(0, 0, 0, HEADER.size()).style().bold().set();
            int rowIdx = 1;
            for (NocardPerson person : list) {
                ws.value(rowIdx, ROW_MAP.get("식당"), "10층");
                ws.value(rowIdx, ROW_MAP.get("취식일자"), person.getCreatedAt().toString());
                ws.value(rowIdx, ROW_MAP.get("배식일정"), person.getCreatedAt().toLocalDateTime().toLocalDate().toString());
                ws.value(rowIdx, ROW_MAP.get("성명"), person.getPersonName());
                ws.value(rowIdx, ROW_MAP.get("사번"), person.getPersonId());
                ws.value(rowIdx, ROW_MAP.get("취식수"), 1);
                rowIdx++;
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
            logger.error("엑셀 파일 쓰기 실패");
        }
        return RESOURCES_PATH + fileName;
    }

    private void deleteNocardPersonExcel(String fileName) {
        try {
            Files.deleteIfExists(Paths.get(RESOURCES_PATH + fileName));
        } catch (IOException e) {
            logger.error(e.getMessage());
            logger.error("NocardPersonExcel 파일 삭제 실패");
        }
    }

    public Map<Date, Map<CategoryCode, Integer>> readStockExcel(String fileUrl) {
        downloadTmpFile(fileUrl);
        Map<Date, Map<CategoryCode, Integer>> stockGroupByDate = new HashMap<>();
        try (FileInputStream file = new FileInputStream(RESOURCES_PATH + TMP_FILE_NAME);
             ReadableWorkbook wb = new ReadableWorkbook(file)) {
            Sheet sheet = wb.getFirstSheet();

            // category header
            Row headerRow = findCategoryHeaderRow(sheet.openStream());
            Map<Integer, CategoryCode> header = new HashMap<>();
            for (Cell cell : headerRow) {
                if (cell == null || cell.getRawValue() == null) continue;

                CategoryCode cc = CategoryCode.getByTitle(cell.getRawValue());
                if (cc == null) continue;
                header.put(cell.getColumnIndex(), cc);
            }

            // 발주량
            Pattern pattern = Pattern.compile("[0-9]");
            for (Row row : findAmountRow(sheet.openStream(), headerRow.getRowNum())) {
                Cell dateCell = row.stream().filter(o -> o != null && o.getRawValue() != null).findFirst().orElseThrow(); // 첫 col = 배식일자 고정
                Date key = Date.valueOf(LocalDate.of(1899, Month.DECEMBER, 30)
                        .plusDays(Long.parseLong(dateCell.getRawValue())));
                row.stream()
                        .filter(o -> o != null
                                && o.getRawValue() != null
                                && o.getColumnIndex() != dateCell.getColumnIndex()
                                && pattern.matcher(o.getRawValue()).find()
                                && header.containsKey(o.getColumnIndex()))
                        .forEach(o -> {
                            Map<CategoryCode, Integer> amountMap = stockGroupByDate.getOrDefault(key, new HashMap<>());
                            amountMap.put(header.get(o.getColumnIndex()), Integer.parseInt(o.getRawValue()));
                            stockGroupByDate.put(key, amountMap);
                        });
            }
        } catch (IOException e) {
            logger.warn(e.getMessage());
        }
        removeTmpFile();
        return stockGroupByDate;
    }

    public Map<Date, Map<CategoryCode, Cell>> read20MenuExcel(String fileUrl) {
        Map<Date, Map<CategoryCode, Cell>> menuDataGroupByDate = new HashMap<>();
        downloadTmpFile(fileUrl);
        try (FileInputStream file = new FileInputStream(RESOURCES_PATH + TMP_FILE_NAME);
             ReadableWorkbook wb = new ReadableWorkbook(file)) {
            Sheet sheet = wb.getFirstSheet();

            // date header
            Map<Integer, Cell> header = new HashMap<>();
            for (Cell cell : findDateHeaderRow(sheet.openStream())) {
                header.put(cell.getColumnIndex(), cell);
            }

            // 한식/일품
            Map<Integer, Map<CategoryCode, Cell>> menuData = new HashMap<>();
            for (Row row : findABRow(sheet.openStream())) {
                CategoryCode categoryCode =
                        row.stream().anyMatch(o -> o != null && o.getRawValue() != null && o.getRawValue().contains(CategoryCode.A.getTitle())) ? CategoryCode.A : CategoryCode.B;
                for (Cell cell : row) {
                    if (cell == null || cell.getRawValue() == null || !header.containsKey(cell.getColumnIndex()))
                        continue;
                    Map<CategoryCode, Cell> menu = menuData.getOrDefault(cell.getColumnIndex(), new HashMap<>());
                    menu.put(categoryCode, cell);
                    menuData.put(cell.getColumnIndex(), menu);
                }
            }

            for (Integer key : menuData.keySet()) {
                menuDataGroupByDate.put(
                        Date.valueOf(LocalDate.of(1899, Month.DECEMBER, 30)
                                .plusDays(Long.parseLong(header.get(key).getRawValue()))),
                        menuData.get(key)
                );
            }

            // 단일 메뉴인 경우 CategoryCode 'B' 로 조정
            for (Date key : menuDataGroupByDate.keySet()) {
                if (menuDataGroupByDate.get(key).size() == 1) {
                    Cell tmp = menuDataGroupByDate.get(key).values().stream().findAny().orElseThrow();
                    Map<CategoryCode, Cell> adjustMap = new HashMap<>();
                    adjustMap.put(CategoryCode.B, tmp);
                    menuDataGroupByDate.put(key, adjustMap);
                }
            }
        } catch (IOException | NoSuchElementException e) {
            logger.error(e.getMessage());
        }
        removeTmpFile();
        return menuDataGroupByDate;
    }

    private void downloadTmpFile(String fileUrl) {
        try (BufferedInputStream in = new BufferedInputStream(new URL(fileUrl).openStream());
             FileOutputStream fileOutputStream = new FileOutputStream(RESOURCES_PATH + TMP_FILE_NAME)
        ) {
            byte[] dataBuffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                fileOutputStream.write(dataBuffer, 0, bytesRead);
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
            logger.error("엑셀 파일 다운로드 실패");
        }
    }

    private void removeTmpFile() {
        Path path = Paths.get(RESOURCES_PATH + TMP_FILE_NAME);
        try {
            Files.delete(path);
        } catch (IOException e) {
            logger.error(e.getMessage());
            logger.error("임시 파일 삭제 실패");
        }
    }

    /**
     * 10층 발주분 엑셀 파일에서 구분(도시락/샌드위치/샐러드/총합)이 적힌 Row를 검색: 가장 처음 발견한 Row 고정값
     */
    private Row findCategoryHeaderRow(Stream<Row> rows) {
        return rows
                .filter(o -> {
                    boolean res = false;
                    for (Cell cell : o) {
                        if (cell != null && cell.getRawValue() != null) {
                            res = true;
                            break;
                        }
                    }
                    return res;
                })
                .findFirst().orElseThrow(); // 없는 경우 NoSuchElementException
    }

    /**
     * 20층 메뉴 엑셀 파일에서 배식일정이 작성된 Row를 검색
     */
    private List<Cell> findDateHeaderRow(Stream<Row> rows) {
        Pattern pattern = Pattern.compile("[0-9]");
        return rows
                .filter(Objects::nonNull)
                .filter(o -> {
                    boolean res = false;
                    for (Cell cell : o) {
                        if (cell != null && cell.getRawValue() != null && cell.getType().equals(CellType.FORMULA) && pattern.matcher(cell.getRawValue()).find()) {
                            res = true;
                            break;
                        }
                    }
                    return res;
                })
                .map(o -> {
                    List<Cell> cellList = new ArrayList<>();
                    for (Cell cell : o) {
                        if (cell == null || cell.getRawValue() == null || !pattern.matcher(cell.getRawValue()).find())
                            continue;
                        cellList.add(cell);
                    }
                    return cellList;
                })
                .findFirst().orElseThrow(); // 없는 경우 NoSuchElementException
    }

    /**
     * 10층 발주분 엑셀 파일에서 배식 일자 별 발주량이 작성된 Row를 검색
     */
    private List<Row> findAmountRow(Stream<Row> rows, int exceptRowIdx) {
        Pattern pattern = Pattern.compile("[0-9]");
        return rows
                .filter(o -> o != null && o.getRowNum() != exceptRowIdx)
                .filter(o -> {
                    // 가장 처음 만나는 셀이 date 값일 것
                    for (Cell cell : o) {
                        if (cell != null && cell.getRawValue() != null && pattern.matcher(cell.getRawValue()).find())
                            return true;
                    }
                    return false;
                }).toList();
    }

    /**
     * 20층 메뉴 엑셀 파일에서 A/B 메뉴가 작성된 Row를 검색
     */
    private List<Row> findABRow(Stream<Row> rows) {
        return rows
                .filter(Objects::nonNull)
                .filter(o -> {
                    boolean res = false;
                    for (Cell cell : o) {
                        res |= cell != null && cell.getRawValue() != null && (cell.getRawValue().contains(CategoryCode.A.getTitle())
                                || cell.getRawValue().contains(CategoryCode.B.getTitle())
                        );
                    }
                    return res;
                })
                .toList();
    }
}
