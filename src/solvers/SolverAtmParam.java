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
            dataArray[index][row] = row;
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

    // расчет параметров атмосферы. в общем массиве
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

    // расчет параметров атмосферы. в отдельном блоке
    public float[][] getAtmParam(float[][] dataArray, float[][] dataAltitude, int[] serviceBlockDimension, int[] serviceInternalOffsets) {

        for (int row = 0; row <= serviceBlockDimension[0]; row++) {

            // установка высоты для которой рассчитываются параметры
            atmParamCalc.setAltitude(dataAltitude[serviceInternalOffsets[0]][row]);

            // расчет плотности
            dataArray[serviceInternalOffsets[2]][row] = atmParamCalc.getDens();

            // расчет атмосферного давления
            dataArray[serviceInternalOffsets[3]][row] = atmParamCalc.getPressStatic();

            // расчет атмосферного температуры
            dataArray[serviceInternalOffsets[4]][row] = atmParamCalc.getTemp();

            // расчет корости звука
            dataArray[serviceInternalOffsets[5]][row] = atmParamCalc.getSoundVel();
        }
        return dataArray;
    }

}
