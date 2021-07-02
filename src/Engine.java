import solvers.SolverAtmParam;
import solvers.SolverVelocity;
import java.io.IOException;
import java.util.ArrayList;

public class Engine {

    // конструктор. он тут не нужен, но пусть будет
    public Engine (ArrayList <int[]> serviceList, ArrayList <int[]> outputListList)
    {this.serviceList = serviceList;
    this.outputList = outputListList;}

    // сокровищница
    private ArrayList <int[]> serviceList;                                                                              // хранилище общих настроек
    private ArrayList <int[]> outputList;                                                                               // хранилище настроек вывода
    private ArrayList <float [][]> dataList = new ArrayList <>();                                                       // хранилище данных

    // решатели
    private SolverVelocity solverVelocity = new SolverVelocity();
    private SolverAtmParam solverAtmParam = new SolverAtmParam();

    // расчет скоростей в едином массиве. изначальная концепция
    public float [][] getDataArray (float [] inputVelocity, float [] inputMaxM, int [] serviceCSYS,
                             int [] serviceBlockOffset, int [] serviceLimitType, int [] serviceDimension) {

        // создали массив для хранения данных
        float[][] dataArray = new float[serviceDimension[0] + 1][serviceDimension[1]];

        // посчитали высоты
        dataArray = solverAtmParam.getAltitude(dataArray, serviceDimension[0]);

        // посчитали параметры атмосферы
        dataArray = solverAtmParam.getAtmParam(dataArray, serviceDimension[0], serviceDimension[1]);

        // посчитали скорость Vd
        dataArray = solverVelocity.getVelocity(dataArray, serviceDimension, serviceCSYS, serviceBlockOffset[0], inputVelocity[0], inputMaxM[0], serviceLimitType[0]);

        // посчитали скорость Vc
        dataArray = solverVelocity.getVelocity(dataArray, serviceDimension, serviceCSYS, serviceBlockOffset[1], inputVelocity[1], inputMaxM[1], serviceLimitType[1]);

        // посчитали скорость Va
        dataArray = solverVelocity.getVelocity(dataArray, serviceDimension, serviceCSYS, serviceBlockOffset[2], inputVelocity[2], serviceBlockOffset[1]+serviceCSYS[4], serviceLimitType[2]);

        // посчитали скорость Vs
        dataArray = solverVelocity.getVelocity(dataArray, serviceDimension, serviceCSYS, serviceBlockOffset[3], inputVelocity[3], -1.0f, serviceLimitType[3]);

        // выводим в консоль
        for (float[]floats : dataArray) {
            System.out.println();
            for (float result : floats) {
                System.out.printf("%.4f", result);
                System.out.print("     ");
            }
        }

        // вернули в зад
        return dataArray;

    }

    // расчет скоростей в отдельных массивах. вторая концепция
    public void dataArray (float [] inputVelocity, float [] inputMaxM) throws IOException {

        // создание массивов
        float[][] blank = new float[0] [0];                                                                                  // заглушка
        float[][] dataAltitude = new float[this.serviceList.get(0)[0] + 1] [this.serviceList.get(0)[1]];                     // создали массив для хранения высот
        float[][] dataAtmParam = new float[this.serviceList.get(0)[0] + 1] [this.serviceList.get(0)[2]];                     // создали массив для хранения параметров атмосферы
        float[][] dataVelocityVd = new float[this.serviceList.get(0)[0] + 1] [this.serviceList.get(0)[3]];                   // создали массив для скорости Vd
        float[][] dataVelocityVc = new float[this.serviceList.get(0)[0] + 1] [this.serviceList.get(0)[3]];                   // создали массив для скорости Vc
        float[][] dataVelocityVa = new float[this.serviceList.get(0)[0] + 1] [this.serviceList.get(0)[3]];                   // создали массив для скорости Va
        float[][] dataVelocityVs = new float[this.serviceList.get(0)[0] + 1] [this.serviceList.get(0)[3]];                   // создали массив для скорости Vs

        // скинули в хранилище
        this.dataList.add(dataAltitude);
        this.dataList.add(dataAtmParam);
        this.dataList.add(dataVelocityVd);
        this.dataList.add(dataVelocityVc);
        this.dataList.add(dataVelocityVa);
        this.dataList.add(dataVelocityVs);

        // записали высоты
        solverAtmParam.getAltitude1(this.dataList.get(0), this.serviceList.get(1)[0]);
        // записали атмосферу
        solverAtmParam.getAtmParam(this.dataList.get(1), this.dataList.get(0), this.serviceList.get(1));                // здесь и далее: 0 - массив в который пишутся параметры; 1 - параметры, необходимые для расчетов (параметры атмосферы)
        // записали скорость Vd
        solverVelocity.getVelocity(this.dataList.get(2), this.dataList.get(1), this.serviceList.get(1), inputVelocity[0], inputMaxM[0], this.serviceList.get(2)[0], blank);
        // записали скорость Vc
        solverVelocity.getVelocity(this.dataList.get(3), this.dataList.get(1), this.serviceList.get(1), inputVelocity[1], inputMaxM[1], this.serviceList.get(2)[1], blank);
        // записали скорость Va
        solverVelocity.getVelocity(this.dataList.get(4), this.dataList.get(1), this.serviceList.get(1), inputVelocity[2], inputMaxM[1], this.serviceList.get(2)[2], dataVelocityVc);
        // записали скорость Vs
        solverVelocity.getVelocity(this.dataList.get(5), this.dataList.get(1), this.serviceList.get(1), inputVelocity[3], -1.0f, this.serviceList.get(2)[3], blank);

        // выводим в консоль
        for (float[]floats : this.dataList.get(4)) {
            System.out.println();
            for (float result : floats) {
                System.out.printf("%.4f", result);
                System.out.print("     ");
            }
        }

        dataOutput();

    }


    // экспорт второй версии
    void dataOutput () throws IOException {

        // Экспортируем
        Export1 export = new Export1();
        export.setDataList(dataList);
        export.setOutputList(outputList);
        export.setServiceList(serviceList);
        export.exportData(12200.0f, 10);










        /*
        int lenghtOutputValue = 0;
        for (int i: outputValue) {
            lenghtOutputValue += i;
        }



        int demX = 1 + ((serviceCSYS[0] - 1) * outputBlock[1]) + (serviceCSYS[2] * lenghtOutputValue);
        int demY = (int) Math.ceil (inputMaxAltitude / outputAltitudeInc);


        List<float[]> List = new ArrayList<float[]>();

        for (int i = 0; i < 10; i++) {

            List.add(new float[] {dataArray[i][0]});

        }
        System.out.println(List.get(1)[0]);



*/


    }



}
