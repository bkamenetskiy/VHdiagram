package solvers;

import models.ModelVelocity;

public class SolverVelocity {

    ModelVelocity velocityCalc = new ModelVelocity();

    public float[][] getVelocity(float[][] dataArray, int[] serviceDimension, int[] serviceCSYS, int serviceBlockOffset, float inputVelocity, float maxM, int serviceLimitType) {

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


    public float[][] getVelocity(float[][] dataVelocity, float[][] dataAtmParam, int[] serviceBlockDimension, int[] serviceInternalOffsets, float inputVelocity, float maxM, int serviceLimitType, float [][] blockСompare) {

        // V (cas), м/с
        for (int row = 0; row <= serviceBlockDimension[0]; row++) {
            dataVelocity[row][serviceInternalOffsets[6]] = inputVelocity;
        }

        // M
        for (int row = 0; row <= serviceBlockDimension[0]; row++) {

            // Ms (без верхнего ограничения по маху)
            if (serviceLimitType == 0) {
                dataVelocity[row][serviceInternalOffsets[7]] = velocityCalc.getVcasToMach(dataAtmParam[row][serviceInternalOffsets[3]], inputVelocity);
            }

            // Md, Mc (директивное ограничение максимальным махом)
            if (serviceLimitType == 1) {
                float Md = velocityCalc.getVcasToMach(dataAtmParam[row][serviceInternalOffsets[3]], inputVelocity);
                dataVelocity[row][serviceInternalOffsets[7]] = Math.min(Md, maxM);
            }

            // Ma (ограничение другим махом)
            if (serviceLimitType == 2) {
                float Ma = velocityCalc.getVcasToMach(dataAtmParam[row][serviceInternalOffsets[3]], inputVelocity);
                // сравниваются два маха - Ma и другой из любого из блоков скоростей (blockСompare) для одноименных высот. Если Ma превышает ограничение, то обращается в -1
                if (Ma <= blockСompare[row][serviceInternalOffsets[7]]) {
                    dataVelocity[row][serviceInternalOffsets[7]] = Ma;
                } else {
                    dataVelocity[row][serviceInternalOffsets[7]] = -1.0f;
                }
            }
        }

        // V (tas), м/с
        for (int row = 0; row <= serviceBlockDimension[0]; row++) {
            // если M=-1, то его производные - V(tas), V(eas), q (и дополнительно V(cas)) обращаются в -1. иначе считаются нормально
            if (dataVelocity[row][serviceInternalOffsets[7]] == -1.0f){
                dataVelocity[row][serviceInternalOffsets[8]] = -1.0f;
            } else {
                dataVelocity[row][serviceInternalOffsets[8]] = velocityCalc.getMachToVtas(dataAtmParam[row][serviceInternalOffsets[5]], dataVelocity[row][serviceInternalOffsets[7]]);
            }
        }

        // V (eas), м/с
        for (int row = 0; row <= serviceBlockDimension[0]; row++) {
            if (dataVelocity[row][serviceInternalOffsets[7]] == -1.0f){
                dataVelocity[row][serviceInternalOffsets[9]] = -1.0f;
            } else {
                dataVelocity[row][serviceInternalOffsets[9]] = velocityCalc.getVtasToVeas(dataAtmParam[row][serviceInternalOffsets[2]], dataVelocity[row][serviceInternalOffsets[8]]);
            }
        }

        // q, Па
        for (int row = 0; row <= serviceBlockDimension[0]; row++) {
            if (dataVelocity[row][serviceInternalOffsets[7]] == -1.0f){
                dataVelocity[row][serviceInternalOffsets[6]] = -1.0f;
                dataVelocity[row][serviceInternalOffsets[10]] = -1.0f;
            } else {
                dataVelocity[row][serviceInternalOffsets[10]] = velocityCalc.getDynPress(dataAtmParam[row][serviceInternalOffsets[2]], dataVelocity[row][serviceInternalOffsets[8]]);
            }
        }

        return dataVelocity;
    }



}











