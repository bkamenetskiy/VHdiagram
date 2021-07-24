package solvers;

import models.ModelАtmosphere;

public class SolverAtmParam {

   private ModelАtmosphere atmParamCalc = new ModelАtmosphere();

   // заполнение блока высот. высота пишется в столбец с индексом, указанным пользователем в переменной index
   public void getAltitude1 (float[][] dataArray, int index) {

        for (int row = 0; row <= dataArray.length - 1; row++) {
            dataArray[row][index] = row;
        }
   }

    // расчет параметров атмосферы в отдельном блоке
    public void getAtmParam(float[][] dataArray, float[][] dataAltitude, int[] serviceInternalOffsets) {

        for (int row = 0; row <= dataArray.length - 1; row++) {

            // установка высоты для которой рассчитываются параметры
            atmParamCalc.setAltitude(dataAltitude[row][serviceInternalOffsets[0]]);

            // расчет плотности
            dataArray[row][serviceInternalOffsets[0]] = atmParamCalc.getDens();

            // расчет атмосферного давления
            dataArray[row][serviceInternalOffsets[1]] = atmParamCalc.getPressStatic();

            // расчет атмосферного температуры
            dataArray[row][serviceInternalOffsets[2]] = atmParamCalc.getTemp();

            // расчет корости звука
            dataArray[row][serviceInternalOffsets[3]] = atmParamCalc.getSoundVel();
        }
    }

}
