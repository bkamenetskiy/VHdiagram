package solvers;

import models.ModelAtm;

public class SolverAtmParam {

    ModelAtm atmParamCalc = new ModelAtm();

    // заполнение блока высот. высота пишется строго в столбец с индексом 0.
    // dataArray - двумерный массив; numIterate - количество итераций/высота массива
    public float[][] getAltitude (float[][] dataArray, int numIterate) {                                                // первая версия. подлежит удалению

        for (int row = 0; row <= numIterate; row++) {
            dataArray[row][0] = row;
        }
        return dataArray;
    }

    // заполнение блока высот. высота пишется в столбец с индексом, указанным пользователем в переменной index
    public float[][] getAltitude (float[][] dataArray, int numIterate, int index) {

        for (int row = 0; row <= numIterate; row++) {
            dataArray[row][index] = row;
        }
        return dataArray;
    }

    // заполнение блока высот в одномерный массив
    public float[] getAltitude (float[] dataArray, int numIterate) {

        for (int row = 0; row <= numIterate; row++) {
            dataArray[row] = row;
        }
        return dataArray;
    }

    // заполнение блока параметров атмосферы
    public float[][] getAtmParam(float[][] dataArray, int numIterate, int dataColumn) {                                 // первая версия. подлежит удалению

        for (int row = 0; row <= numIterate; row++) {

            atmParamCalc.setAltitude(dataArray[row][0]);

            for (int column = 1; column <= dataColumn; column++) {

                if (column == 1) {
                    dataArray[row][column] = atmParamCalc.getDens();
                }
                if (column == 2) {
                    dataArray[row][column] = atmParamCalc.getPressStatic();
                }
                if (column == 3) {
                    dataArray[row][column] = atmParamCalc.getTemp();
                }
                if (column == 4) {
                    dataArray[row][column] = atmParamCalc.getSoundVel();
                }
            }
        }
        return dataArray;
    }

    // расчет параметров атмосферы
    public float[][] getAtmParam(float[][] dataAtmParam, float[][] dataAltitude, int [] serviceBlockDimension, int [] serviceInternalOffsets) {

        for (int row = 0; row <= serviceBlockDimension[0]; row++) {

            // установка высоты для которой рассчитываются параметры
            atmParamCalc.setAltitude(dataAltitude[row][serviceInternalOffsets[0]]);

            // расчет плотности
            dataAtmParam[row][serviceInternalOffsets[2]] = atmParamCalc.getDens();

            // расчет атмосферного давления
            dataAtmParam[row][serviceInternalOffsets[3]] = atmParamCalc.getPressStatic();

            // расчет атмосферного температуры
            dataAtmParam[row][serviceInternalOffsets[4]] = atmParamCalc.getTemp();

            // расчет корости звука
            dataAtmParam[row][serviceInternalOffsets[5]] = atmParamCalc.getSoundVel();
        }
        return dataAtmParam;
    }

}
