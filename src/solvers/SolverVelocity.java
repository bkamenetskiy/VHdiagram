package solvers;

import models.ModelVelocity;

public class SolverVelocity {

    ModelVelocity velocityCalc = new ModelVelocity();

    public float[][] getVelocity(float[][] dataArray, int[] serviceDimension, int[] serviceCSYS, int serviceBlockOffset, float inputVelocity, float maxM, int serviceLimitType) {

        // берем столбец, заполняем столбец
        //1 - ссылка на плотность; 4 - скорость звука; 2 - на давление

        // V (cas), м/с
        for (int row = 0; row <= serviceDimension[0]; row++) {
            dataArray[row][serviceBlockOffset + serviceCSYS[3]] = inputVelocity;
        }

        // M
        for (int row = 0; row <= serviceDimension[0]; row++) {

            // Ms (без верхнего ограничения по маху)
            if (serviceLimitType == 0) {
                dataArray[row][serviceBlockOffset + serviceCSYS[4]] = velocityCalc.getVcasToMach(dataArray[row][2], inputVelocity);
            }

            // Md, Mc (директивное ограничение максимальным махом)
            if (serviceLimitType == 1) {
                float Md = velocityCalc.getVcasToMach(dataArray[row][2], inputVelocity);
                dataArray[row][serviceBlockOffset + serviceCSYS[4]] = Math.min(Md, maxM);
            }

            // Ma (ограничение другим махом)
            if (serviceLimitType == 2) {
                float Ma = velocityCalc.getVcasToMach(dataArray[row][2], inputVelocity);
                if (Ma <= dataArray[row][(int) maxM]) {
                    dataArray[row][serviceBlockOffset + serviceCSYS[4]] = Ma;
                } else {
                    dataArray[row][serviceBlockOffset + serviceCSYS[4]] = -1.0f;
                }
            }
        }

        // V (tas), м/с
        for (int row = 0; row <= serviceDimension[0]; row++) {
            if (dataArray[row][serviceBlockOffset + serviceCSYS[4]] == -1.0f){
                dataArray[row][serviceBlockOffset + serviceCSYS[5]] = -1.0f;
            } else {
                dataArray[row][serviceBlockOffset + serviceCSYS[5]] = velocityCalc.getMachToVtas(dataArray[row][4], dataArray[row][serviceBlockOffset + serviceCSYS[4]]);
            }
        }

        // V (eas), м/с
        for (int row = 0; row <= serviceDimension[0]; row++) {
            if (dataArray[row][serviceBlockOffset + serviceCSYS[4]] == -1.0f){
                dataArray[row][serviceBlockOffset + serviceCSYS[6]] = -1.0f;
            } else {
                dataArray[row][serviceBlockOffset + serviceCSYS[6]] = velocityCalc.getVtasToVeas(dataArray[row][1], dataArray[row][serviceBlockOffset + serviceCSYS[5]]);
            }
        }

        // q, Па
        for (int row = 0; row <= serviceDimension[0]; row++) {
            if (dataArray[row][serviceBlockOffset + serviceCSYS[4]] == -1.0f){
                dataArray[row][serviceBlockOffset + serviceCSYS[7]] = -1.0f;
                dataArray[row][serviceBlockOffset + serviceCSYS[3]] = -1.0f;
            } else {
                dataArray[row][serviceBlockOffset + serviceCSYS[7]] = velocityCalc.getDynPress(dataArray[row][1], dataArray[row][serviceBlockOffset + serviceCSYS[5]]);
            }
        }

        return dataArray;
    }

}


/*
    public float[][] getVelocity(float[][] dataArray, int[] serviceDimension, int[] serviceCSYS, int serviceBlockOffset, float casV, float maxM, int limitType) {

        // serviceBlockOffset - индекс начала блока скорости; numIterate - высота массива; dataColumn - ширина блока скорости
        int numIterate = serviceDimension[0];
        int dataColumn = serviceCSYS [1];

        // локальная система координат в блоке
        // берем столбец
        for (int column = serviceBlockOffset; column <= serviceBlockOffset + serviceCSYS [1]; column++) {

            // заполняем сроки согласно ключа
            for (int row = 0; row <= serviceDimension[0]; row++) {

                if (column == serviceBlockOffset + serviceCSYS[3]) {
                    dataArray[row][column] = casV;                                                                      // V (cas), м/с
                }
                if (column == serviceBlockOffset + serviceCSYS[4]) {                                                    // M

                    // проверка ограничений по маху
                    if (limitType == 0) {
                        dataArray[row][column] = velocityCalc.getVcasToMach(dataArray[row][2], casV);                   // Ms (без верхнего ограничения по маху)
                    }
                    if (limitType == 1) {
                        float Md = velocityCalc.getVcasToMach(dataArray[row][2], casV);                                 // Md, Mc (директивное ограничение максимальным махом)
                        dataArray[row][column] = Math.min(Md, maxM);
                    }
                    if (limitType == 2) {
                        float Ma = velocityCalc.getVcasToMach(dataArray[row][2], casV);                                 // Ma (ограничение другим махом)
                        if (Ma <= dataArray[row][(int) maxM]) {
                            dataArray[row][column] = Ma;
                        } else {
                            dataArray[row][column] = -1.0f;
                        }
                    }
                }
                if (column == serviceBlockOffset + serviceCSYS[5]) {
                    if (dataArray [row] [serviceBlockOffset + 1] == -1.0f){
                        dataArray[row][column] = -1.0f;
                    } else {
                        dataArray[row][column] = velocityCalc.getMachToVtas(dataArray[row][4], dataArray[row][serviceBlockOffset + 1]);    // V (tas), м/с
                    }
                }
                if (column == serviceBlockOffset + serviceCSYS[6]) {
                    if (dataArray [row] [serviceBlockOffset + 1] == -1.0f){
                        dataArray[row][column] = -1.0f;
                    } else {
                        dataArray[row][column] = velocityCalc.getVtasToVeas(dataArray[row][1], dataArray[row][serviceBlockOffset + 2]);    // V (eas), м/с
                    }
                }
                if (column == serviceBlockOffset + serviceCSYS[7]) {
                    if (dataArray [row] [serviceBlockOffset + 1] == -1.0f){
                        dataArray[row][column] = -1.0f;
                    } else {
                        dataArray[row][column] = velocityCalc.getDynPress(dataArray[row][1], dataArray[row][serviceBlockOffset + 2]);      // q, Па
                    }
                }
            }
        }
        return dataArray;
    }
*/








