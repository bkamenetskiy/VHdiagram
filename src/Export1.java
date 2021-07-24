import  java.io.*;

import models.ModelUnits;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.util.ArrayList;

public class Export1 {

    // сокровищница
    private ArrayList <int[]> listSettings;                                                                              // хранилище общих настроек
    private ArrayList <float [][]> listData;                                                                            // хранилище данных
    private ArrayList <int[]> listInternalOffsets;



    // создание конвертера единиц измерения
    private ModelUnits unitConvert = new ModelUnits();
    // создание книги
    private XSSFWorkbook dataFile = new XSSFWorkbook();
    // создание листа
    private XSSFSheet sheet = this.dataFile.createSheet("Result");


    private int unitAltitude = 1; // 1 - километры; любой другой ключ - метры
    private int unitVelocity = 1; // 0 - м/с; 1 - км/ч; 2 - knot


    public void exportData(int outputAltitudeInc) throws IOException {

        // экспорт данных
        int initAlt = 0; // счетчик текущей высоты (в метрах) = начальная высота с которой начинается экспорт. потом к ней будет прибавляться outputAltitudeInc
        int rowInc = 0; // счетчик строк
        int deltaHeight = 2; // смещение таблицы с данными по вертикали = высота шапки таблицы
        int deltaWidth = 0; // смещение таблицы вправо

        float conversionAltitude;                                                                                       // вспомогательная переменная для конвертации высот
        float conversionVelocity;                                                                                       // вспомогательная переменная для конвертации скоростей

        // сие сотонинство работает пока начальная высота (initAlt) меньше или равна той высоте,
        // которая хранится в последней строке [serviceList.get(0)[0]], в метрах [this.serviceList.get(1)[0]]) массива с данными this.dataList.get(0)
        while (initAlt <= (int) this.listData.get(0)[this.listSettings.get(0)[0]] [this.listInternalOffsets.get(0)[0]]) {

            XSSFRow rowData = this.sheet.createRow((short) rowInc + deltaHeight);                               // создание строки (тип short), с номером rowInc с глобальным смещением вниз на высоту шапки таблицы deltaHeight

            // блок высот
            for (int column = 0; column <= this.listData.get(0)[0].length - 1; column++) {                              // перебор столбцов блока высот от нулевого до последнего

                conversionAltitude = this.listData.get(0)[initAlt] [this.listInternalOffsets.get(0)[0]];                // установка текущей высоты в метрах

                // экспорт высоты в метрах
                if (column == this.listInternalOffsets.get(0)[0]) {

                    switch (this.unitAltitude) {
                        case 1:
                            conversionAltitude = unitConvert.getLengthKilometer(conversionAltitude);                    // конвертация из метров в километры
                            rowData.createCell(column + deltaWidth).setCellValue(conversionAltitude);        // создание ячейки в строке с номером rowData и номером столбца column, запись в нее элемента Altitude
                            break;
                        default:
                            rowData.createCell(column + deltaWidth).setCellValue(conversionAltitude);        // по умолчанию заполняется метрами
                            break;
                    }
                }

                // экспорт высоты в футах
                if (column == this.listInternalOffsets.get(0)[1]) {

                    conversionAltitude = unitConvert.getLengthFoot(conversionAltitude);                                 // конвертация из метров в футы
                    rowData.createCell(column + deltaWidth).setCellValue(conversionAltitude);

                }
            }

            // блок атмосферы. экспортируется как есть, без конвертации
            for (int column = 0; column <= this.listData.get(1)[0].length - 1; column++) {

                rowData.createCell(column + deltaWidth + this.listData.get(0)[0].length).setCellValue(this.listData.get(1)[initAlt][column]);

            }

            // блок скорости. перебор массивов скоростей. 2 - место, с которого начинаются блоки скорости в dataList
            for (int i = 2; i < this.listData.size(); i++) {

                // вспомогательная переменная. считает смещение текущего блока скорости i относительно уже экспортированных
                int offset = deltaWidth + this.listData.get(0)[0].length + this.listData.get(1)[0].length + this.listData.get(i)[0].length * (i - 2);

                // экспорт данных из блока i
                for (int column = 0; column <= this.listData.get(i)[0].length - 1; column++) {

                    // вспомогательная переменная. хранит посчитанное текущеее значение взятое из блока скорости
                    conversionVelocity = this.listData.get(i)[initAlt] [column];

                    // конвертация Vcas и отсечение отрицательных значений
                    if ((column == this.listInternalOffsets.get(2)[0]) & (conversionVelocity >= 0.0f)) {

                        switch (this.unitVelocity) {
                            case 0:
                                rowData.createCell(offset + this.listInternalOffsets.get(2)[0]).setCellValue(conversionVelocity);
                                break;
                            case 1:
                                rowData.createCell(offset + this.listInternalOffsets.get(2)[0]).setCellValue(unitConvert.getVelocityKm(conversionVelocity));
                                break;
                            case 2:
                                rowData.createCell(offset + this.listInternalOffsets.get(2)[0]).setCellValue(unitConvert.getVelocityKt(conversionVelocity));
                                break;
                        }
                    }

                    // проверка маха на отрицательные значения
                    if ((column == this.listInternalOffsets.get(2)[1]) & (conversionVelocity >= 0.0f)) {

                        rowData.createCell(offset + this.listInternalOffsets.get(2)[1]).setCellValue(conversionVelocity);

                    }

                    // конвертация Vtas и отсечение отрицательных значений
                    if ((column == this.listInternalOffsets.get(2)[2]) & (conversionVelocity >= 0.0f)) {

                        switch (this.unitVelocity) {
                            case 0:
                                rowData.createCell(offset + this.listInternalOffsets.get(2)[2]).setCellValue(conversionVelocity);
                                break;
                            case 1:
                                rowData.createCell(offset + this.listInternalOffsets.get(2)[2]).setCellValue(unitConvert.getVelocityKm(conversionVelocity));
                                break;
                            case 2:
                                rowData.createCell(offset + this.listInternalOffsets.get(2)[2]).setCellValue(unitConvert.getVelocityKt(conversionVelocity));
                                break;
                        }
                    }

                    // конвертация Veas и отсечение отрицательных значений
                    if ((column == this.listInternalOffsets.get(2)[3]) & (conversionVelocity >= 0.0f)) {

                        switch (this.unitVelocity) {
                            case 0:
                                rowData.createCell(offset + this.listInternalOffsets.get(2)[3]).setCellValue(conversionVelocity);
                                break;
                            case 1:
                                rowData.createCell(offset + this.listInternalOffsets.get(2)[3]).setCellValue(unitConvert.getVelocityKm(conversionVelocity));
                                break;
                            case 2:
                                rowData.createCell(offset + this.listInternalOffsets.get(2)[3]).setCellValue(unitConvert.getVelocityKt(conversionVelocity));
                                break;
                        }
                    }

                    // проверка скоростного напора на отрицательные значения
                    if ((column == this.listInternalOffsets.get(2)[4]) & (conversionVelocity >= 0.0f)) {

                        rowData.createCell(offset + this.listInternalOffsets.get(2)[4]).setCellValue(conversionVelocity);

                    }
                }
            }

            initAlt = initAlt + outputAltitudeInc;                                                                      // установка значения следующей высоты для вывода
            rowInc++;                                                                                                   // установка координаты следующей строки
        }


        exportHeading();

        writeFile("d:\\Data.xlsx", this.dataFile);
        System.out.println("Your excel file has been generated!");


        float abc =  this.listData.get(0)[10][0];
        abc = unitConvert.getLengthKilometer(abc);
        System.out.println(abc);


    }



    public void exportHeading (){

        String[] headingTypeAtmParam = new String[] {"Плотность", "Давление", "Скорость звука", "Температура"};
        String[] headingTypeVelocity = new String[] {"Vcas", "M", "Vtas", "Veas", "q"};

        String[] headingUnitVelocity = new String[] {"м/с", "км/ч", "knot"};
        String[] headingUnitLength = new String[] {"м", "км", "фут"};
        String[] headingUnitPressure = new String[] {"Па", "кг/см^2"};
        String[] headingUnitTemp = new String[] {"C", "K", "F"};
        String[] headingUnitDensity = new String[] {"кг/м^3"};

        int rowInc = 0; // счетчик строк
        int deltaWidth = 0; // смещение таблицы вправо

        for (int row = 0; row <= 1; row++) {                                                                            // задаем индекс строки шапки таблицы

            XSSFRow rowHeading = this.sheet.createRow((short) row);                                                  // создание строки (тип short), с номером rowInc с глобальным смещением вниз на высоту шапки таблицы deltaHeight

            // шапка блока высот
            for (int column = 0; column <= this.listData.get(0)[0].length - 1; column++) {                              // перебор столбцов блока высот от нулевого до последнего

                // название столбцов
                if ((row == 0) & (column == this.listInternalOffsets.get(0)[0])) {

                    rowHeading.createCell(column + deltaWidth).setCellValue("Высота");                       // создание ячейки в строке с номером rowHeading и номером столбца column, запись в нее элемента Altitude

                }

                // единицы измерения для метрической системы
                if ((row == 1) & (column == this.listInternalOffsets.get(0)[0])) {

                    switch (this.unitAltitude) {
                        case 1:
                            rowHeading.createCell(column + deltaWidth).setCellValue(headingUnitLength[1]);
                            break;
                        default:
                            rowHeading.createCell(column + deltaWidth).setCellValue(headingUnitLength[0]);
                            break;
                    }
                }

                // единицы измерения для имперской системы
                if ((row == 1) & (column == this.listInternalOffsets.get(0)[1])) {

                    rowHeading.createCell(column + deltaWidth).setCellValue(headingUnitLength[2]);

                }
            }

            // шапка блока параметров атмосферы
            for (int column = 0; column <= this.listData.get(1)[0].length - 1; column++){

                // название столбцов
                if (row == 0) {

                    rowHeading.createCell(column + deltaWidth + this.listData.get(0)[0].length).setCellValue(headingTypeAtmParam[column]);

                }




            }







        }









    }



    // запись файла на диск
    private void writeFile (String filename, XSSFWorkbook file) throws IOException {
        FileOutputStream out = new FileOutputStream(filename);
        file.write(out);
        out.close();
        file.close();
    }


    public void setListSettings(ArrayList<int[]> listSettings) {
        this.listSettings = listSettings;
    }

    public void setListData(ArrayList<float[][]> listData) {
        this.listData = listData;
    }

    public void setListInternalOffsets(ArrayList<int[]> listInternalOffsets) {
        this.listInternalOffsets = listInternalOffsets;
    }
}
