package solvers;

import enums.UnitInput;
import enums.UnitOutput;
import models.ModelAtmosphere;

/**
 * Модуль рассчета параметров атмосферы.
 * Параметры считаются в единицах СИ.
 * Последнее изменение - 10/2021
 */

public class SolverAtmosphere {

   private ModelAtmosphere atmParamCalc = new ModelAtmosphere();

   // заполнение блока высот. высота пишется в столбец с индексом, указанным пользователем в переменной internalOffsetsAltitude
   public void getAltitude(double[][] dataArray, int[] internalOffsetsAltitude, double initAltitude, SolverUnitConverter unitConverter, UnitInput[] unitInput) {

       double altitude = 0.0;
       double altitudeInc;

       // шаг приращения по высоте для разных единиц измерения
       if (unitInput[0] == UnitInput.Kilometer) {
           altitudeInc = 0.001;
       } else {
           altitudeInc = 1.0;
       }

       for (int row = 0; row <= dataArray.length - 1; row++) {

            // во "вспомогательный" столбец [2] пишутся высоты без конвертации
            dataArray[row][internalOffsetsAltitude[2]] = altitude + initAltitude;
            // в столбец [0] пишутся соответствующие им метры
            dataArray[row][internalOffsetsAltitude[0]] = unitConverter.getUnitInputToSI(unitInput[0], dataArray[row][internalOffsetsAltitude[2]]);
            // в столбец [1] пишутся соответствующие им футы, переведенные из столбца [0]
            dataArray[row][internalOffsetsAltitude[1]] = unitConverter.getSIToUnitOutput(UnitOutput.Foot, dataArray[row][internalOffsetsAltitude[0]]);
            altitude += altitudeInc;
       }
   }

    // расчет параметров атмосферы в отдельном блоке.
    // dataArray - массив в который пишутся параметры атмосферы; dataAltitude - массив из которого берутся высоты
    public void getAtmParam(double[][] dataArray, double[][] dataAltitude, int[] internalOffsetsAltitude, int[] internalOffsetsAtmParam) {

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
