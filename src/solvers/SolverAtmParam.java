package solvers;

import models.ModelAtm;

public class SolverAtmParam {

    ModelAtm atmParamCalc = new ModelAtm();

    public float[][] getAltitude(float[][] dataArray, int numIterate) {

        for (int row = 0; row <= numIterate; row++) {
            dataArray[row][0] = row;
        }
        return dataArray;
    }


    public float[][] getAtmParam(float[][] dataArray, int numIterate, int dataColumn) {

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

}
