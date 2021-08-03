package ExportExcel;
import models.ModelUnits;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import java.util.ArrayList;

public class ExportData {


    public int exportData(XSSFSheet sheet, ArrayList<float[][]> listData, ArrayList <int[]> listInternalOffsets,
                           ArrayList <int[]> listSettings, int unitAltitude, int unitVelocity, int globalVerticalOffset,
                           int localVerticalOffset, int rowInc, int outputAltitudeInc, ModelUnits unitConverter) {

        int abc = 0;
        // экспорт данных
        int initAlt = 0; // счетчик текущей высоты (в метрах) = начальная высота с которой начинается экспорт. потом к ней будет прибавляться outputAltitudeInc


        float conversionAltitude;                                                                                       // вспомогательная переменная для конвертации высот
        float conversionVelocity;                                                                                       // вспомогательная переменная для конвертации скоростей

        // экспорт работает пока начальная высота (initAlt) меньше или равна той высоте,
        // которая хранится в последней строке [serviceList.get(0)[0]], в метрах [this.serviceList.get(1)[0]]) массива с данными this.listData.get(0)
        while (initAlt <= (int) listData.get(0)[listSettings.get(0)[0]] [listInternalOffsets.get(0)[0]]) {

            XSSFRow rowData = sheet.createRow(rowInc + localVerticalOffset + globalVerticalOffset);                                       // создание строки, с номером rowInc с глобальным смещением вниз на высоту шапки таблицы localVerticalOffset

            // блок высот
            for (int column = 0; column <= listData.get(0)[0].length - 1; column++) {                                   // перебор столбцов блока высот от нулевого до последнего

                conversionAltitude = listData.get(0)[initAlt] [listInternalOffsets.get(0)[0]];                          // установка текущей высоты в метрах

                // экспорт высоты в метрах
                if (column == listInternalOffsets.get(0)[0]) {

                    switch (unitAltitude) {
                        case 1:
                            conversionAltitude = unitConverter.getLengthKilometer(conversionAltitude);                    // конвертация из метров в километры
                            rowData.createCell(column).setCellValue(conversionAltitude);                                // создание ячейки в строке с номером rowData и номером столбца column, запись в нее элемента Altitude
                            break;
                        default:
                            rowData.createCell(column).setCellValue(conversionAltitude);                                // по умолчанию заполняется метрами
                            break;
                    }
                }

                // экспорт высоты в футах
                if (column == listInternalOffsets.get(0)[1]) {

                    conversionAltitude = unitConverter.getLengthFoot(conversionAltitude);                                 // конвертация из метров в футы
                    rowData.createCell(column).setCellValue(conversionAltitude);

                }
            }

            // блок атмосферы. экспортируется как есть, без конвертации
            for (int column = 0; column <= listData.get(1)[0].length - 1; column++) {

                rowData.createCell(column + listData.get(0)[0].length).setCellValue(listData.get(1)[initAlt][column]);

            }

            // блок скорости. перебор массивов скоростей. 2 - место, с которого начинаются блоки скорости в dataList
            for (int i = 2; i < listData.size(); i++) {

                // вспомогательная переменная. считает смещение текущего блока скорости i относительно уже экспортированных
                int offset = listData.get(0)[0].length + listData.get(1)[0].length + listData.get(i)[0].length * (i - 2);

                // экспорт данных из блока i
                for (int column = 0; column <= listData.get(i)[0].length - 1; column++) {

                    // вспомогательная переменная. хранит посчитанное текущеее значение взятое из блока скорости
                    conversionVelocity = listData.get(i)[initAlt][column];

                    // конвертация Vcas и отсечение отрицательных значений
                    if ((column == listInternalOffsets.get(2)[0]) & (conversionVelocity >= 0.0f)) {

                        switch (unitVelocity) {
                            case 0:
                                rowData.createCell(offset + column).setCellValue(conversionVelocity);
                                break;
                            case 1:
                                rowData.createCell(offset + column).setCellValue(unitConverter.getVelocityKm(conversionVelocity));
                                break;
                            case 2:
                                rowData.createCell(offset + column).setCellValue(unitConverter.getVelocityKt(conversionVelocity));
                                break;
                        }
                    }

                    // проверка маха на отрицательные значения
                    if ((column == listInternalOffsets.get(2)[1]) & (conversionVelocity >= 0.0f)) {

                        rowData.createCell(offset + column).setCellValue(conversionVelocity);

                        // Ma граничен сверху Mc, при этом, если Ma превышает предельное значение, он обращается в -1.
                        // Для корректного построения графика необходимо определить последнюю строку, в котрой Ma больше 0
                        if (i == 4) {
                            abc = rowInc;
                        }

                    }

                    // конвертация Vtas и отсечение отрицательных значений
                    if ((column == listInternalOffsets.get(2)[2]) & (conversionVelocity >= 0.0f)) {

                        switch (unitVelocity) {
                            case 0:
                                rowData.createCell(offset + column).setCellValue(conversionVelocity);
                                break;
                            case 1:
                                rowData.createCell(offset + column).setCellValue(unitConverter.getVelocityKm(conversionVelocity));
                                break;
                            case 2:
                                rowData.createCell(offset + column).setCellValue(unitConverter.getVelocityKt(conversionVelocity));
                                break;
                        }
                    }

                    // конвертация Veas и отсечение отрицательных значений
                    if ((column == listInternalOffsets.get(2)[3]) & (conversionVelocity >= 0.0f)) {

                        switch (unitVelocity) {
                            case 0:
                                rowData.createCell(offset + column).setCellValue(conversionVelocity);
                                break;
                            case 1:
                                rowData.createCell(offset + column).setCellValue(unitConverter.getVelocityKm(conversionVelocity));
                                break;
                            case 2:
                                rowData.createCell(offset + column).setCellValue(unitConverter.getVelocityKt(conversionVelocity));
                                break;
                        }
                    }

                    // проверка скоростного напора на отрицательные значения
                    if ((column == listInternalOffsets.get(2)[4]) & (conversionVelocity >= 0.0f)) {

                        rowData.createCell(offset + column).setCellValue(conversionVelocity);

                    }
                }
            }

            initAlt = initAlt + outputAltitudeInc;                                                                      // установка значения следующей высоты для вывода
            rowInc++;                                                                                                   // установка индекса следующей строки
        }

        return rowInc;

    }



}
