import enums.UnitInput;
import enums.UnitOutput;
import exportexcel.ExportChart;
import exportexcel.ExportData;
import exportexcel.ExportHeading;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import solvers.SolverAtmosphere;
import solvers.SolverUnitConverter;
import solvers.SolverVelocity;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class Engine {

    // конструктор
    public Engine (int[] settingLimitType, ArrayList <int[]> listInternalOffsets, double[] inputAltitude, double[] inputVelocity,
                   UnitOutput[] unitOutput, UnitInput[] unitInput)

    {
        this.settingLimitType = settingLimitType;
        this.listInternalOffsets = listInternalOffsets;
        this.inputAltitude = inputAltitude;
        this.unitOutput = unitOutput;
        this.unitInput = unitInput;
        this.inputVelocity = inputVelocity;
    }

    // сокровищница
    private final int[] settingLimitType;                                                                               // хранилище общих настроек
    private final ArrayList <int[]> listInternalOffsets;                                                                // ранилище внутренних смещений
    private final ArrayList <double[][]> listData = new ArrayList();                                                    // хранилище данных
    private final double[] inputAltitude;
    private final UnitOutput[] unitOutput;
    private final UnitInput[] unitInput;
    private final double[] inputVelocity;

    // вспомогательные переменные
    private int rowCount;                                                                                               // размерность массива
    private int[] rowEndIndex = new int[] {0, 0, 0, 0};                                                                 // счетчик индексов строк, на которых остановился экспорт скорости
    private XSSFWorkbook dataBook;                                                                                      // для перекидывания книги между методами

    // исходные данные после конвертации
    private double[] inputVelocityConvert;
    private double[] inputAltitudeConvert;

    // конвертер единиц измерения
    SolverUnitConverter unitConverter = new SolverUnitConverter();                                                      // конвертер единиц измерения

    // расчет скоростей
    protected void dataArray (double[] inputMaxM) {

        // все что нужно для рассчетов
        SolverVelocity solverVelocity = new SolverVelocity();                                                           // решатели
        SolverAtmosphere solverAtmosphere = new SolverAtmosphere();

        // сконвертировали исходные данные в СИ
        getInputUnitConvert();


        // Определение размерности маасива.
        // Предполагается, что рассчеты идут с шагом в 1 метр в системе Си или 1 фут в имперской системе.
        // Километры, будучи кратной единицей измерения, идут с шагом 0,001
        switch (this.unitInput[0]) {

            case Meter:

            case Foot:

                this.rowCount = (int) Math.ceil ((Math.abs(this.inputAltitude[0]) + Math.abs(this.inputAltitude[1])));
                break;

            case Kilometer:

                this.rowCount = (int) Math.ceil ((Math.abs(this.inputAltitude[0]) + Math.abs(this.inputAltitude[1])) / 0.001);
                break;
        }

        // создание массивов
        double[][] blank = new double[0][0];                                                                            // заглушка
        double[][] dataAltitude = new double[this.rowCount + 1][this.listInternalOffsets.get(0).length];                // массив для хранения высот
        double[][] dataAtmParam = new double[this.rowCount + 1][this.listInternalOffsets.get(1).length];                // массив для хранения параметров атмосферы
        double[][] dataVelocityVd = new double[this.rowCount + 1][this.listInternalOffsets.get(2).length];              // массив для скорости Vd
        double[][] dataVelocityVc = new double[this.rowCount + 1][this.listInternalOffsets.get(2).length];              // массив для скорости Vc
        double[][] dataVelocityVa = new double[this.rowCount + 1][this.listInternalOffsets.get(2).length];              // массив для скорости Va
        double[][] dataVelocityVs = new double[this.rowCount + 1][this.listInternalOffsets.get(2).length];              // массив для скорости Vs

        // скинули в хранилище
        this.listData.add(dataAltitude);
        this.listData.add(dataAtmParam);
        this.listData.add(dataVelocityVd);
        this.listData.add(dataVelocityVc);
        this.listData.add(dataVelocityVa);
        this.listData.add(dataVelocityVs);

        // записали высоты
        solverAtmosphere.getAltitude(this.listData.get(0), this.listInternalOffsets.get(0), this.inputAltitude[0], this.unitConverter, this.unitInput);
        // записали атмосферу
        solverAtmosphere.getAtmParam(this.listData.get(1), this.listData.get(0), this.listInternalOffsets.get(0), this.listInternalOffsets.get(1));        // аргументы метода: 0 - массив в который пишутся параметры; 1 - параметры, необходимые для расчетов
        // записали скорость Vd
        solverVelocity.getVelocity(this.listData.get(2), this.listData.get(1), this.listInternalOffsets, this.inputVelocityConvert[0], inputMaxM[0], this.settingLimitType[0], blank);
        // записали скорость Vc
        solverVelocity.getVelocity(this.listData.get(3), this.listData.get(1), this.listInternalOffsets, this.inputVelocityConvert[1], inputMaxM[1], this.settingLimitType[1], blank);
        // записали скорость Va
        solverVelocity.getVelocity(this.listData.get(4), this.listData.get(1), this.listInternalOffsets, this.inputVelocityConvert[2], inputMaxM[1], this.settingLimitType[2], dataVelocityVc);
        // записали скорость Vs
        solverVelocity.getVelocity(this.listData.get(5), this.listData.get(1), this.listInternalOffsets, this.inputVelocityConvert[3], -1.0f, this.settingLimitType[3], blank);


        // выводим в консоль
        for (double[]floats : this.listData.get(0)) {
            System.out.println();
            for (double result : floats) {
                System.out.printf("%.6f", result);
                System.out.print("     ");
            }
        }

    }


    // экспорт в excel
    protected void exportExcel() {

        // все что нужно для экспорта в эксель
        XSSFWorkbook dataBook = new XSSFWorkbook();                                                                     // создание книги
        XSSFSheet sheet = dataBook.createSheet("V-H");                                                         // создание листа с названием V-H

        // вспомогательные переменные
        int localVerticalOffset = 3;                                                                                    // смещение таблицы с данными относительно заголовка
        int globalVerticalOffset = 26;                                                                                  // глобальное смещение всей таблицы, включая заголовок, по вертикали

        // экспорт заголовка
        ExportHeading heading = new ExportHeading();
        heading.exportHeading(dataBook, sheet, this.listData, this.listInternalOffsets, globalVerticalOffset, this.unitOutput);

        // экспорт данных
        ExportData data = new ExportData();
        data.exportData(dataBook, sheet, this.listData, this.listInternalOffsets, globalVerticalOffset, localVerticalOffset,
                this.inputAltitude[2], this.rowCount, this.rowEndIndex, this.unitOutput, this.unitConverter, this.unitInput);

        // отрисовка графиков
        ExportChart chart = new ExportChart();
        chart.Chart(sheet, listInternalOffsets, globalVerticalOffset, localVerticalOffset, this.rowEndIndex, this.listData, this.unitOutput);

        this.dataBook = dataBook;

    }

    // запись файла на диск
    protected void writeFile (String path) {

        XSSFWorkbook file = this.dataBook;

        try {
            FileOutputStream out = new FileOutputStream(path);
            file.write(out);
            out.close();
            file.close();
            System.out.println("Your excel file has been generated!");
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

      }

    // конвертация исходных данных в СИ
    private void getInputUnitConvert() {

        // вспомогательные переменные
        double[] altitude = new double[this.inputAltitude.length];
        double[] velocity = new double[this.inputVelocity.length];

        // скорости
        for (int i = 0; i <= velocity.length - 1; i++) {

            velocity[i] = this.unitConverter.getUnitInputToSI(this.unitInput[1], this.inputVelocity[i]);
        }

        // высоты
        for (int i = 0; i <= altitude.length - 1; i++) {

            altitude[i] = this.unitConverter.getUnitInputToSI(this.unitInput[0], this.inputAltitude[i]);
        }

        this.inputAltitudeConvert = altitude;
        this.inputVelocityConvert = velocity;
    }



}
