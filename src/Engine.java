import solvers.SolverAtmParam;
import solvers.SolverVelocity;
import java.io.IOException;
import java.util.ArrayList;

public class Engine extends Thread {


    private SolverVelocity velocityCalculator = new SolverVelocity();
    private SolverAtmParam atmParamCalculator = new SolverAtmParam();

    float [][] getDataArray (float [] inputVelocity, float [] inputMaxM, int [] serviceCSYS,
                             int [] serviceBlockOffset, int [] serviceLimitType, int [] serviceDimension) {

        // создали массив для хранения данных
        float[][] dataArray = new float[serviceDimension[0] + 1][serviceDimension[1]];

        // посчитали высоты
        dataArray = atmParamCalculator.getAltitude(dataArray, serviceDimension[0]);

        // посчитали параметры атмосферы
        dataArray = atmParamCalculator.getAtmParam(dataArray, serviceDimension[0], serviceDimension[1]);

        // посчитали скорость Vd
        dataArray = velocityCalculator.getVelocity(dataArray, serviceDimension, serviceCSYS, serviceBlockOffset[0], inputVelocity[0], inputMaxM[0], serviceLimitType[0]);

        // посчитали скорость Vc
        dataArray = velocityCalculator.getVelocity(dataArray, serviceDimension, serviceCSYS, serviceBlockOffset[1], inputVelocity[1], inputMaxM[1], serviceLimitType[1]);

        // посчитали скорость Va
        dataArray = velocityCalculator.getVelocity(dataArray, serviceDimension, serviceCSYS, serviceBlockOffset[2], inputVelocity[2], serviceBlockOffset[1]+serviceCSYS[4], serviceLimitType[2]);

        // посчитали скорость Vs
        dataArray = velocityCalculator.getVelocity(dataArray, serviceDimension, serviceCSYS, serviceBlockOffset[3], inputVelocity[3], -1.0f, serviceLimitType[3]);

        // вернули в зад
        return dataArray;

    }

    void getDataArray_V1 (float [] inputVelocity, float [] inputMaxM, int [] serviceBlockDimension,
                          int [] serviceInternalOffsets, int [] serviceLimitType) {

        // массив-заглушка
        float [][] blank = new float[0][0];

        // создание массивов
        float[][] dataAltitude = new float[serviceBlockDimension[0] + 1][serviceBlockDimension[1]];                     // создали массив для хранения высот
        float[][] dataAtmParam = new float[serviceBlockDimension[0] + 1][serviceBlockDimension[2]];                     // создали массив для хранения параметров атмосферы
        float[][] dataVelocityVd = new float[serviceBlockDimension[0] + 1][serviceBlockDimension[3]];                   // создали массив для скорости Vd
        float[][] dataVelocityVc = new float[serviceBlockDimension[0] + 1][serviceBlockDimension[3]];                   // создали массив для скорости Vc
        float[][] dataVelocityVa = new float[serviceBlockDimension[0] + 1][serviceBlockDimension[3]];                   // создали массив для скорости Va


        // создали ArrayList
        ArrayList <float [][]> dataList = new ArrayList <>();




        // записали высоты
        atmParamCalculator.getAltitude(dataAltitude, serviceBlockDimension[0], serviceInternalOffsets[0]);
        // записали атмосферу
        atmParamCalculator.getAtmParam(dataAtmParam, dataAltitude, serviceBlockDimension, serviceInternalOffsets);

        // записали скорость Vd
        velocityCalculator.getVelocity(dataVelocityVd, dataAtmParam, serviceBlockDimension, serviceInternalOffsets, inputVelocity[0], inputMaxM[0], serviceLimitType[0], blank);
        // записали скорость Vc
        velocityCalculator.getVelocity(dataVelocityVc, dataAtmParam, serviceBlockDimension, serviceInternalOffsets, inputVelocity[1], inputMaxM[1], serviceLimitType[1], blank);
        // записали скорость Va
        velocityCalculator.getVelocity(dataVelocityVa, dataAtmParam, serviceBlockDimension, serviceInternalOffsets, inputVelocity[2], inputMaxM[1], serviceLimitType[2], dataVelocityVc);
        // создали массив для скорости Vs
        float[][] dataVelocityVs = new float[serviceBlockDimension[0] + 1][serviceBlockDimension[3]];
        // записали скорость Vs
        velocityCalculator.getVelocity(dataVelocityVs, dataAtmParam, serviceBlockDimension, serviceInternalOffsets, inputVelocity[3], -1.0f, serviceLimitType[3], blank);



        // выводим в консоль
        for (float[] floats : dataVelocityVa) {
            System.out.println();
            for (float result : floats) {
                System.out.printf("%.4f", result);
                System.out.print("     ");
            }
        }





    }

    void dataOutput (float [][] dataArray, int [] serviceCSYS, int [] serviceBlockOffset, int [] outputBlock, int [] outputValue, int outputAltitudeInc, float inputMaxAltitude) throws IOException {


        // преобразуем полный массив в урезанный
        float [][] dataOutputArray = dataArray;

        // Экспортируем
        ExcelExport export = new ExcelExport();
        export.setDataOutputArray(dataOutputArray);
        export.writeData(inputMaxAltitude);









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
