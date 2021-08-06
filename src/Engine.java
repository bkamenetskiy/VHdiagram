import ExportExcel.ExportChart;
import ExportExcel.ExportData;
import ExportExcel.ExportHeading;
import models.ModelUnits;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import solvers.SolverAtmParam;
import solvers.SolverVelocity;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class Engine {

    // конструктор
    public Engine (int[] settingLimitType, ArrayList <int[]> listInternalOffsets, float[] inputAltitude)

    {
        this.settingLimitType = settingLimitType;
        this.listInternalOffsets = listInternalOffsets;
        this.inputAltitude = inputAltitude;
    }

    // сокровищница
    private int[] settingLimitType;                                                                             // хранилище общих настроек
    private ArrayList <int[]> listInternalOffsets;                                                                      // ранилище внутренних смещений
    private ArrayList <float[][]> listData = new ArrayList <>();                                                        // хранилище данных
    private float[] inputAltitude;

    // решатели
    private SolverVelocity solverVelocity = new SolverVelocity();
    private SolverAtmParam solverAtmParam = new SolverAtmParam();
    private ModelUnits unitConverter = new ModelUnits();

    // вспомогательные переменные
    private int rowCount = 0;
    private int[] rowEndIndex = new int[] {0, 0, 0, 0};

    // расчет скоростей в отдельных массивах
    public void dataArray (float[] inputVelocity, float[] inputMaxM) throws IOException {

        this.rowCount = (int) Math.ceil ((Math.abs(inputAltitude[0]) + Math.abs(inputAltitude[1])) / 1.0f);

        // создание массивов
        float[][] blank = new float[0][0];                                                                              // заглушка
        float[][] dataAltitude = new float[rowCount + 1][2];               // создали массив для хранения высот
        float[][] dataAtmParam = new float[rowCount + 1][4];               // создали массив для хранения параметров атмосферы
        float[][] dataVelocityVd = new float[rowCount + 1][5];             // создали массив для скорости Vd
        float[][] dataVelocityVc = new float[rowCount + 1][5];             // создали массив для скорости Vc
        float[][] dataVelocityVa = new float[rowCount + 1][5];             // создали массив для скорости Va
        float[][] dataVelocityVs = new float[rowCount + 1][5];             // создали массив для скорости Vs

        // скинули в хранилище
        this.listData.add(dataAltitude);
        this.listData.add(dataAtmParam);
        this.listData.add(dataVelocityVd);
        this.listData.add(dataVelocityVc);
        this.listData.add(dataVelocityVa);
        this.listData.add(dataVelocityVs);

        // записали высоты
        solverAtmParam.getAltitude(this.listData.get(0), this.listInternalOffsets.get(0)[0], this.inputAltitude[0]);
        // записали атмосферу
        solverAtmParam.getAtmParam(this.listData.get(1), this.listData.get(0), this.listInternalOffsets.get(0), this.listInternalOffsets.get(1));        // аргументы метода: 0 - массив в который пишутся параметры; 1 - параметры, необходимые для расчетов
        // записали скорость Vd
        solverVelocity.getVelocity(this.listData.get(2), this.listData.get(1), this.listInternalOffsets, inputVelocity[0], inputMaxM[0], this.settingLimitType[0], blank);
        // записали скорость Vc
        solverVelocity.getVelocity(this.listData.get(3), this.listData.get(1), this.listInternalOffsets, inputVelocity[1], inputMaxM[1], this.settingLimitType[1], blank);
        // записали скорость Va
        solverVelocity.getVelocity(this.listData.get(4), this.listData.get(1), this.listInternalOffsets, inputVelocity[2], inputMaxM[1], this.settingLimitType[2], dataVelocityVc);
        // записали скорость Vs
        solverVelocity.getVelocity(this.listData.get(5), this.listData.get(1), this.listInternalOffsets, inputVelocity[3], -1.0f, this.settingLimitType[3], blank);

        // выводим в консоль
        for (float[]floats : this.listData.get(0)) {
            System.out.println();
            for (float result : floats) {
                System.out.printf("%.6f", result);
                System.out.print("     ");
            }
        }
    }


    // экспорт в excel
    void dataOutput () throws IOException {

        // единицы измерения
        // 0. Единицы измерения высоты:
        // 1 - километры;
        // любой другой ключ - метры
        // 1. Единицы измерения скорости:
        // 0 - м/с;
        // 1 - км/ч;
        // 2 - knot
        int [] outputUnit = new int[] {0, 1};


        // глобальное смещение всей таблицы, включая заголовок, по вертикали
        int globalVerticalOffset = 0;

        // смещение данных
        int localVerticalOffset = 3;

        // создание книги
        XSSFWorkbook dataBook = new XSSFWorkbook();
        // создание листа
        XSSFSheet sheet = dataBook.createSheet("Result");

        // экспорт заголовка
        ExportHeading heading = new ExportHeading();
        heading.exportHeading(sheet, this.listData, this.listInternalOffsets, outputUnit[0], outputUnit[1], globalVerticalOffset);

        // экспорт данных
        ExportData data = new ExportData();
        data.exportData(sheet, this.listData, this.listInternalOffsets, outputUnit[0], outputUnit[1],
                                    globalVerticalOffset, localVerticalOffset, (int) this.inputAltitude[2], unitConverter, this.rowCount, this.rowEndIndex, dataBook);

        // отрисовка графиков
        ExportChart chart = new ExportChart();
        chart.Chart(sheet, listInternalOffsets, globalVerticalOffset, localVerticalOffset, this.rowEndIndex);

        // запись файла
        writeFile(dataBook);
        System.out.println("Your excel file has been generated!");

    }

    // запись файла на диск
    private void writeFile (XSSFWorkbook file) throws IOException {
        FileOutputStream out = new FileOutputStream("d:\\Data.xlsx");
        file.write(out);
        out.close();
        file.close();
    }



}
