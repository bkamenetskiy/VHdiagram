import  java.io.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xddf.usermodel.*;
import org.apache.poi.xddf.usermodel.chart.*;
import org.apache.poi.xssf.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFChart;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class Export1 {

    // сокровищница
    private ArrayList <int[]> serviceList;                                                                              // хранилище общих настроек
    private ArrayList <int[]> outputList;                                                                               // хранилище настроек вывода
    private ArrayList <float [][]> dataList;                                                                            // хранилище данных
    //private int outputAltitudeInc = 10;

    // создание книги
    private XSSFWorkbook dataFile = new XSSFWorkbook();
    // создание листа
    private XSSFSheet sheet = this.dataFile.createSheet("Result");


    public void exportData (float maxAltitude, int outputAltitudeInc) throws IOException {



        // экспорт данных
        int initAlt = 0; // счетчик текущей высоты = начальная высота с которой начинается экспорт. потом к ней будет прибавляться outputAltitudeInc
        int rowInc = 0; // счетчик строк
        int deltaHeight = 0; // смещение таблицы с данными по вертикали
        int deltaWidth = 0; // смещение блока вправо

        // сие сотонинство работает пока начальная высота (initAlt) меньше или равна той высоте,
        // которая записана в последней строке [serviceList.get(0)[0]], в метрах [this.serviceList.get(1)[0]]) массива с данными this.dataList.get(0)
        while (initAlt <= (int) this.dataList.get(0)[this.serviceList.get(0)[0]] [this.serviceList.get(1)[0]]){

            XSSFRow rowData = this.sheet.createRow((short)rowInc + deltaHeight);                                                      // создание строки (тип short), с номером rowInc

            // блок высот
            for (int column = 0; column <= 1; column++) {                                          // перебор столбцов блока высот
                rowData.createCell(column + deltaWidth).setCellValue(this.dataList.get(0)[initAlt][column]);                         // создание ячейки с координатами rowInc (номер строки) и column (номер столбца), запись в нее элемента массива с координатами minAltitude и column
            }
            initAlt = initAlt + outputAltitudeInc;                                                                                     // установка значения следующей высоты для вывода
            rowInc++;                                                                                                   // установка координаты следующей строки
        }




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
