package ExportExcel;

import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import java.util.ArrayList;

public class ExportHeading {

    public void exportHeading (XSSFSheet sheet, ArrayList <float[][]> listData, ArrayList <int[]> listInternalOffsets,
                               int unitAltitude, int unitVelocity, int globalVerticalOffset) {

        String[] headingTypeAtmParam = new String[] {"Плотность", "Атм. давление", "Температура", "Скорость звука"};
        String[] headingTypeVelocity = new String[] {"Vcas", "M", "Vtas", "Veas", "q"};

        String[] headingUnitVelocity = new String[] {"м/с", "км/ч", "knot"};
        String[] headingUnitLength = new String[] {"м", "км", "фут"};
        String[] headingUnitPressure = new String[] {"Па", "кг/см^2"};
        String[] headingUnitTemp = new String[] {"K", "C", "F"};

        // вспомогательные переменные
        int rowName = 0 + globalVerticalOffset;
        int rowType = 1 + globalVerticalOffset;
        int rowUnit = 2 + globalVerticalOffset;

        XSSFRow rowHeading;                                                                                             // создание строк для заголовка
        sheet.createRow(rowName);
        sheet.createRow(rowType);
        sheet.createRow(rowUnit);

        // заголовок блока высот
        rowHeading = sheet.getRow(rowName);
        rowHeading.createCell(0).setCellValue("Высота");
        sheet.addMergedRegion(new CellRangeAddress(rowName, rowType, 0, listData.get(0)[0].length - 1));

        // блок высот
        for (int column = 0; column <= listData.get(0)[0].length - 1; column++) {

            // высота в метрах
            if (column == listInternalOffsets.get(0)[0]) {

                // единицы измерения
                switch (unitAltitude) {
                    case 1:
                        rowHeading = sheet.getRow(rowUnit);
                        rowHeading.createCell(column).setCellValue(headingUnitLength[1]);
                        break;
                    default:
                        rowHeading = sheet.getRow(rowUnit);
                        rowHeading.createCell(column).setCellValue(headingUnitLength[0]);
                        break;
                }
            }

            // высота в футах
            if (column == listInternalOffsets.get(0)[1]) {

                // единицы измерения
                rowHeading = sheet.getRow(rowUnit);
                rowHeading.createCell(column).setCellValue(headingUnitLength[2]);

            }
        }

        // заголовок блока атмосферы
        rowHeading = sheet.getRow(rowName);
        rowHeading.createCell(listData.get(0)[0].length).setCellValue("Параметры атмосферы");
        sheet.addMergedRegion(new CellRangeAddress(rowName, rowName, listData.get(0)[0].length, listData.get(0)[0].length + listData.get(1)[0].length - 1));


        // блок параметров атмосферы
        for (int column = 0; column <= listData.get(1)[0].length - 1; column++) {

            // заголовок плотности
            if (column == listInternalOffsets.get(1)[0]) {

                // название
                rowHeading = sheet.getRow(rowType);
                rowHeading.createCell(column + listData.get(0)[0].length).setCellValue(headingTypeAtmParam[0]);

                // единицы измерения
                rowHeading = sheet.getRow(rowUnit);
                rowHeading.createCell(column + listData.get(0)[0].length).setCellValue("кг/м^3");

            }

            // заголовок атмосферного давления
            if (column == listInternalOffsets.get(1)[1]) {

                // название
                rowHeading = sheet.getRow(rowType);
                rowHeading.createCell(column + listData.get(0)[0].length).setCellValue(headingTypeAtmParam[1]);

                // единицы измерения
                rowHeading = sheet.getRow(rowUnit);
                rowHeading.createCell(column + listData.get(0)[0].length).setCellValue(headingUnitPressure[0]);

            }

            // заголовок температуры
            if (column == listInternalOffsets.get(1)[2]) {

                // название
                rowHeading = sheet.getRow(rowType);
                rowHeading.createCell(column + listData.get(0)[0].length).setCellValue(headingTypeAtmParam[2]);

                // единицы измерения
                rowHeading = sheet.getRow(rowUnit);
                rowHeading.createCell(column + listData.get(0)[0].length).setCellValue(headingUnitTemp[0]);

            }

            // заголовок скорости звука
            if (column == listInternalOffsets.get(1)[3]) {

                // название
                rowHeading = sheet.getRow(rowType);
                rowHeading.createCell(column + listData.get(0)[0].length).setCellValue(headingTypeAtmParam[3]);

                // единицы измерения
                rowHeading = sheet.getRow(rowUnit);
                rowHeading.createCell(column + listData.get(0)[0].length).setCellValue(headingUnitVelocity[0]);

            }
        }


        // блок скоростей
        for (int i = 2; i < listData.size(); i++) {

            // вспомогательная переменная. считает смещение текущего блока скорости i относительно уже экспортированных
            int offset = listData.get(0)[0].length + listData.get(1)[0].length + listData.get(i)[0].length * (i - 2);

            // заголовок блока скоростей
            rowHeading = sheet.getRow(rowName);

            switch (i) {
                case 2:
                    rowHeading.createCell(offset).setCellValue("Vd");
                    break;
                case 3:
                    rowHeading.createCell(offset).setCellValue("Vc");
                    break;
                case 4:
                    rowHeading.createCell(offset).setCellValue("Va");
                    break;
                case 5:
                    rowHeading.createCell(offset).setCellValue("Vs");
                    break;
                default:
                    rowHeading.createCell(offset).setCellValue("Неизвестный тип");
                    break;
            }
            sheet.addMergedRegion(new CellRangeAddress(rowName, rowName, offset, offset + listData.get(i)[0].length - 1));

            for (int column = 0; column <= listData.get(i)[0].length - 1; column++) {

                // Vcas
                if (column == listInternalOffsets.get(2)[0]) {

                    // название
                    rowHeading = sheet.getRow(rowType);
                    rowHeading.createCell(offset + column).setCellValue(headingTypeVelocity[0]);

                    // единицы измерения
                    switch (unitVelocity) {
                        case 0: // м/с
                            rowHeading = sheet.getRow(rowUnit);
                            rowHeading.createCell(offset + column).setCellValue(headingUnitVelocity[0]);
                            break;
                        case 1: // км/ч
                            rowHeading = sheet.getRow(rowUnit);
                            rowHeading.createCell(offset + column).setCellValue(headingUnitVelocity[1]);
                            break;
                        case 2: // узлы
                            rowHeading = sheet.getRow(rowUnit);
                            rowHeading.createCell(offset + column).setCellValue(headingUnitVelocity[2]);
                            break;
                        default:
                            rowHeading.createCell(offset).setCellValue("Неизвестный тип");
                            break;
                    }
                }

                // M
                if (column == listInternalOffsets.get(2)[1]) {

                    // название
                    rowHeading = sheet.getRow(rowType);
                    rowHeading.createCell(offset + column).setCellValue(headingTypeVelocity[1]);

                    // единицы измерения
                    rowHeading = sheet.getRow(rowUnit);
                    rowHeading.createCell(offset + column).setCellValue("-");
                }

                // Vtas
                if (column == listInternalOffsets.get(2)[2]) {

                    // название
                    rowHeading = sheet.getRow(rowType);
                    rowHeading.createCell(offset + column).setCellValue(headingTypeVelocity[2]);

                    // единицы измерения
                    switch (unitVelocity) {
                        case 0: // м/с
                            rowHeading = sheet.getRow(rowUnit);
                            rowHeading.createCell(offset + column).setCellValue(headingUnitVelocity[0]);
                            break;
                        case 1: // км/ч
                            rowHeading = sheet.getRow(rowUnit);
                            rowHeading.createCell(offset + column).setCellValue(headingUnitVelocity[1]);
                            break;
                        case 2: // узлы
                            rowHeading = sheet.getRow(rowUnit);
                            rowHeading.createCell(offset + column).setCellValue(headingUnitVelocity[2]);
                            break;
                        default:
                            rowHeading.createCell(offset).setCellValue("Неизвестный тип");
                            break;
                    }
                }

                // Veas
                if (column == listInternalOffsets.get(2)[3]) {

                    // название
                    rowHeading = sheet.getRow(rowType);
                    rowHeading.createCell(offset + column).setCellValue(headingTypeVelocity[3]);

                    // единицы измерения
                    switch (unitVelocity) {
                        case 0: // м/с
                            rowHeading = sheet.getRow(rowUnit);
                            rowHeading.createCell(offset + column).setCellValue(headingUnitVelocity[0]);
                            break;
                        case 1: // км/ч
                            rowHeading = sheet.getRow(rowUnit);
                            rowHeading.createCell(offset + column).setCellValue(headingUnitVelocity[1]);
                            break;
                        case 2: // узлы
                            rowHeading = sheet.getRow(rowUnit);
                            rowHeading.createCell(offset + column).setCellValue(headingUnitVelocity[2]);
                            break;
                        default:
                            rowHeading.createCell(offset).setCellValue("Неизвестный тип");
                            break;
                    }
                }

                // q
                if (column == listInternalOffsets.get(2)[4]) {

                    // название
                    rowHeading = sheet.getRow(rowType);
                    rowHeading.createCell(offset + column).setCellValue(headingTypeVelocity[4]);

                    // единицы измерения
                    rowHeading = sheet.getRow(rowUnit);
                    rowHeading.createCell(offset + column).setCellValue(headingUnitPressure[0]);
                }
            }


        }

    }


}
