package solvers;

import models.ModelАtmosphere;

public class SolverAtmParam {

   private ModelАtmosphere atmParamCalc = new ModelАtmosphere();

    // заполнение блока высот. высота пишется строго в столбец с индексом 0.
    // dataArray - двумерный массив; numIterate - количество итераций/высота массива
   public float[][] getAltitude (float[][] dataArray, int numIterate) {                                                 // первая версия. подлежит удалению

        for (int row = 0; row <= numIterate; row++) {
            dataArray[row][0] = row;
        }
        return dataArray;
    }

    // заполнение блока высот. высота пишется в столбец с индексом, указанным пользователем в переменной index
   public float[][] getAltitude1 (float[][] dataArray, int index) {

        for (int row = 0; row <= dataArray.length - 1; row++) {
            dataArray[row][index] = row;
        }
        return dataArray;
   }

    // заполнение блока высот в одномерный массив
    public float[] getAltitude (float[] dataArray) {

        for (int row = 0; row <= dataArray.length - 1; row++) {
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
    public float[][] getAtmParam(float[][] dataArray, float[][] dataAltitude, int[] serviceInternalOffsets) {

        for (int row = 0; row <= dataArray.length - 1; row++) {

            // установка высоты для которой рассчитываются параметры
            atmParamCalc.setAltitude(dataAltitude[row][serviceInternalOffsets[0]]);

            // расчет плотности
            dataArray[row][serviceInternalOffsets[2]] = atmParamCalc.getDens();

            // расчет атмосферного давления
            dataArray[row][serviceInternalOffsets[3]] = atmParamCalc.getPressStatic();

            // расчет атмосферного температуры
            dataArray[row][serviceInternalOffsets[4]] = atmParamCalc.getTemp();

            // расчет корости звука
            dataArray[row][serviceInternalOffsets[5]] = atmParamCalc.getSoundVel();
        }
        return dataArray;
    }

}
