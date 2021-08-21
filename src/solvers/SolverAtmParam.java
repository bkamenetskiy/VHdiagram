package solvers;

import models.ModelАtmosphere;

public class SolverAtmParam {

   private ModelАtmosphere atmParamCalc = new ModelАtmosphere();

   // заполнение блока высот. высота пишется в столбец с индексом, указанным пользователем в переменной index
   public void getAltitude(float[][] dataArray, int index, float initAltitude) {

        for (int row = 0; row <= dataArray.length - 1; row++) {
            dataArray[row][index] = row + initAltitude;
        }
   }

    // расчет параметров атмосферы в отдельном блоке.
    // dataArray - массив в который пишутся параметры атмосферы; dataAltitude - массив из которого берутся высоты
    public void getAtmParam(float[][] dataArray, float[][] dataAltitude, int[] internalOffsetsAltitude, int[] internalOffsetsAtmParam) {

        for (int row = 0; row <= dataArray.length - 1; row++) {

            // установка высоты для которой рассчитываются параметры
            this.atmParamCalc.setAltitude(dataAltitude[row][internalOffsetsAltitude[0]]);

            // расчет плотности
            dataArray[row][internalOffsetsAtmParam[0]] = this.atmParamCalc.getDens();

            // расчет атмосферного давления
            dataArray[row][internalOffsetsAtmParam[1]] = this.atmParamCalc.getPressStatic();

            // расчет атмосферного температуры
            dataArray[row][internalOffsetsAtmParam[2]] = this.atmParamCalc.getTemp();

            // расчет корости звука
            dataArray[row][internalOffsetsAtmParam[3]] = this.atmParamCalc.getSoundVel();
        }
    }

}
