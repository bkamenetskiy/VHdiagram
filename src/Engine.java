import solvers.SolverAtmParam;
import solvers.SolverVelocity;
import java.io.IOException;
import java.util.ArrayList;

public class Engine {

    // конструктор. он тут не нужен, но пусть будет
    public Engine (ArrayList <int[]> listSettings, ArrayList <int[]> listInternalOffsets)

    {this.listSettings = listSettings;
    this.listInternalOffsets = listInternalOffsets;}

    // сокровищница
    private ArrayList <int[]> listSettings;                                                                             // хранилище общих настроек
    private ArrayList <int[]> listInternalOffsets;                                                              // ранилище внутренних смещений
    private ArrayList <float[][]> listData = new ArrayList <>();                                                       // хранилище данных

    // решатели
    private SolverVelocity solverVelocity = new SolverVelocity();
    private SolverAtmParam solverAtmParam = new SolverAtmParam();

    // расчет скоростей в отдельных массивах. вторая концепция
    public void dataArray (float[] inputVelocity, float[] inputMaxM) throws IOException {

        // создание массивов
        float[][] blank = new float[0][0];                                                                              // заглушка
        float[][] dataAltitude = new float[this.listSettings.get(0)[0] + 1][this.listSettings.get(0)[1]];               // создали массив для хранения высот
        float[][] dataAtmParam = new float[this.listSettings.get(0)[0] + 1][this.listSettings.get(0)[2]];               // создали массив для хранения параметров атмосферы
        float[][] dataVelocityVd = new float[this.listSettings.get(0)[0] + 1][this.listSettings.get(0)[3]];             // создали массив для скорости Vd
        float[][] dataVelocityVc = new float[this.listSettings.get(0)[0] + 1][this.listSettings.get(0)[3]];             // создали массив для скорости Vc
        float[][] dataVelocityVa = new float[this.listSettings.get(0)[0] + 1][this.listSettings.get(0)[3]];             // создали массив для скорости Va
        float[][] dataVelocityVs = new float[this.listSettings.get(0)[0] + 1][this.listSettings.get(0)[3]];             // создали массив для скорости Vs

        // скинули в хранилище
        this.listData.add(dataAltitude);
        this.listData.add(dataAtmParam);
        this.listData.add(dataVelocityVd);
        this.listData.add(dataVelocityVc);
        this.listData.add(dataVelocityVa);
        this.listData.add(dataVelocityVs);

        // записали высоты
        solverAtmParam.getAltitude(this.listData.get(0), this.listInternalOffsets.get(0)[0]);
        // записали атмосферу
        solverAtmParam.getAtmParam(this.listData.get(1), this.listData.get(0), this.listInternalOffsets.get(0), this.listInternalOffsets.get(1));        // аргументы метода: 0 - массив в который пишутся параметры; 1 - параметры, необходимые для расчетов
        // записали скорость Vd
        solverVelocity.getVelocity(this.listData.get(2), this.listData.get(1), this.listInternalOffsets, inputVelocity[0], inputMaxM[0], this.listSettings.get(1)[0], blank);
        // записали скорость Vc
        solverVelocity.getVelocity(this.listData.get(3), this.listData.get(1), this.listInternalOffsets, inputVelocity[1], inputMaxM[1], this.listSettings.get(1)[1], blank);
        // записали скорость Va
        solverVelocity.getVelocity(this.listData.get(4), this.listData.get(1), this.listInternalOffsets, inputVelocity[2], inputMaxM[1], this.listSettings.get(1)[2], dataVelocityVc);
        // записали скорость Vs
        solverVelocity.getVelocity(this.listData.get(5), this.listData.get(1), this.listInternalOffsets, inputVelocity[3], -1.0f, this.listSettings.get(1)[3], blank);

        // выводим в консоль
        for (float[]floats : this.listData.get(3)) {
            System.out.println();
            for (float result : floats) {
                System.out.printf("%.6f", result);
                System.out.print("     ");
            }
        }

        dataOutput();

    }


    // экспорт второй версии
    void dataOutput () throws IOException {

        // Экспортируем
        Export1 export = new Export1();
        export.setListData(listData);
        export.setListSettings(listSettings);
        export.setListInternalOffsets(listInternalOffsets);
        export.exportData(10);









    }



}
