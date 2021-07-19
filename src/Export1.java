import  java.io.*;

import models.ModelUnits;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.util.ArrayList;

public class Export1 {

    // сокровищница
    private ArrayList <int[]> serviceList;                                                                              // хранилище общих настроек
    private ArrayList <int[]> outputList;                                                                               // хранилище настроек вывода
    private ArrayList <float [][]> dataList;                                                                            // хранилище данных
    //private int outputAltitudeInc = 10;

    // создание конвертера величин
    private ModelUnits unitConvert = new ModelUnits();
    // создание книги
    private XSSFWorkbook dataFile = new XSSFWorkbook();
    // создание листа
    private XSSFSheet sheet = this.dataFile.createSheet("Result");


    private int unitAltitude = 1; // 1 - километры; любой другой ключ - метры
    private int unitVelocity = 1; // 0 - м/с; 1 - км/ч; 2 - knot
    private int unitTemp = 0;     // 0 - K; 1 - C; 2 - F



    public void exportData (float maxAltitude, int outputAltitudeInc) throws IOException {



        // экспорт данных
        int initAlt = 0; // счетчик текущей высоты (в метрах) = начальная высота с которой начинается экспорт. потом к ней будет прибавляться outputAltitudeInc
        int rowInc = 0; // счетчик строк
        int deltaHeight = 2; // смещение таблицы с данными по вертикали
        int deltaWidth = 0; // смещение таблицы вправо

        float conversionAltitude;                                                                                       // вспомогательная переменная для конвертации высот
        float conversionVelocity;                                                                                       // вспомогательная переменная для конвертации скоростей
        int columnGlobal = 0;


        // сие сотонинство работает пока начальная высота (initAlt) меньше или равна той высоте,
        // которая хранится в последней строке [serviceList.get(0)[0]], в метрах [this.serviceList.get(1)[0]]) массива с данными this.dataList.get(0)
        while (initAlt <= (int) this.dataList.get(0)[this.serviceList.get(0)[0]] [this.serviceList.get(1)[0]]){

            XSSFRow rowData = this.sheet.createRow((short) rowInc + deltaHeight);                                // создание строки (тип short), с номером rowInc с глобальным смещением вниз на высоту шапки таблицы deltaHeight

            // блок высот
            for (int column = 0; column <= this.dataList.get(0)[0].length - 1; column++) {                              // перебор столбцов блока высот от нулевого до последнего

                // экспорт высоты в метрах
                if (column == this.serviceList.get(1)[0]) {

                    if (unitAltitude == 1) {                                                                            // конвертация высоты в километры иначе оставить как есть - м/с
                        conversionAltitude = this.dataList.get(0)[initAlt][this.serviceList.get(1)[0]];                 // текущая высота в метрах
                        conversionAltitude = unitConvert.getLengthKilometer(conversionAltitude);                        // конвертация из метров в километры
                        rowData.createCell(column + deltaWidth).setCellValue(conversionAltitude);            // создание ячейки в строке с номером rowData и номером столбца column, запись в нее элемента Altitude
                    } else {
                        conversionAltitude = this.dataList.get(0)[initAlt][this.serviceList.get(1)[0]];
                        rowData.createCell(column + deltaWidth).setCellValue(conversionAltitude);
                    }
                }

                // экспорт высоты в футах
                if (column == this.serviceList.get(1)[1]) {
                    conversionAltitude = this.dataList.get(0)[initAlt][this.serviceList.get(1)[0]];                     // текущая высота в метрах
                    conversionAltitude = conversionAltitude = unitConvert.getLengthFoot(conversionAltitude);            // конвертация из метров в футы
                    rowData.createCell(column + deltaWidth).setCellValue(conversionAltitude);
                }
                columnGlobal = column;
            }

            // блок атмосферы
            for (int column = 0; column <= this.dataList.get(1)[0].length - 1; column++) {

                rowData.createCell(column + deltaWidth + columnGlobal+1).setCellValue(this.dataList.get(1)[initAlt][column]);

            }

            // блок скорости
            for (int column = 0; column <= this.dataList.get(2)[0].length - 1; column++) {

               // rowData.createCell(column + deltaWidth + columnGlobal+1).setCellValue(this.dataList.get(2)[initAlt][column]);

            }





            initAlt = initAlt + outputAltitudeInc;                                                                      // установка значения следующей высоты для вывода
            rowInc++;                                                                                                   // установка координаты следующей строки
        }

        System.out.println(columnGlobal);



        int [][] abc = new int[][] {{0,1,2,3}, {0, 1, 2, 3}, {0, 1, 2, 3}, {0, 1, 2, 3},{0, 1, 2, 3},{0, 1, 2, 3}};
        System.out.println(abc[0].length);
        System.out.println(this.dataList.get(0)[0].length);


        writeFile("d:\\Data.xlsx", this.dataFile);
        System.out.println("Your excel file has been generated!");




    }



    // запись файла на диск
    private void writeFile (String filename, XSSFWorkbook file) throws IOException {
        FileOutputStream out = new FileOutputStream(filename);
        file.write(out);
        out.close();
        file.close();
    }


    public void setServiceList(ArrayList<int[]> serviceList) {
        this.serviceList = serviceList;
    }

    public void setOutputList(ArrayList<int[]> outputList) {
        this.outputList = outputList;
    }

    public void setDataList(ArrayList<float[][]> dataList) {
        this.dataList = dataList;
    }
}
