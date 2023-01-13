package solvers;

import models.ModelVelocity;
import java.util.ArrayList;

public class SolverVelocity {

    public void getVelocity(double[][] dataVelocity, double[][] dataAtmParam, ArrayList<int[]> listInternalOffsets,
                            double inputVelocity, double maxM, int serviceLimitType, double [][] blockCompare) {

        ModelVelocity velocityCalc = new ModelVelocity();

        // V (cas), м/с
        for (int row = 0; row <= dataVelocity.length - 1; row++) {
            dataVelocity[row][listInternalOffsets.get(2)[0]] = inputVelocity;
        }

        // M
        for (int row = 0; row <= dataVelocity.length - 1; row++) {

            // Ms (без верхнего ограничения по маху)
            if (serviceLimitType == 0) {
                dataVelocity[row][listInternalOffsets.get(2)[1]] = velocityCalc.getVcasToMach(dataAtmParam[row][listInternalOffsets.get(1)[1]], inputVelocity);
            }

            // Md, Mc (директивное ограничение максимальным махом)
            if (serviceLimitType == 1) {
                double Md = velocityCalc.getVcasToMach(dataAtmParam[row][listInternalOffsets.get(1)[1]], inputVelocity);
                dataVelocity[row][listInternalOffsets.get(2)[1]] = Math.min(Md, maxM);
            }

            // Ma (ограничение другим махом)
            if (serviceLimitType == 2) {
                double Ma = velocityCalc.getVcasToMach(dataAtmParam[row][listInternalOffsets.get(1)[1]], inputVelocity);
                // сравниваются два маха - Ma и другой из любого из блоков скоростей (blockCompare) для одноименных высот. Если Ma превышает ограничение, то обращается в -1
                if (Ma <= blockCompare[row][listInternalOffsets.get(2)[1]]) {
                    dataVelocity[row][listInternalOffsets.get(2)[1]] = Ma;
                } else {
                    dataVelocity[row][listInternalOffsets.get(2)[1]] = -1.0;
                }
            }
        }

        // V (tas), м/с
        for (int row = 0; row <= dataVelocity.length - 1; row++) {
            // если M=-1, то его производные - V(tas), V(eas), q (и дополнительно V(cas)) обращаются в -1. иначе считаются нормально
            if (dataVelocity[row][listInternalOffsets.get(2)[1]] <= 0.0){
                dataVelocity[row][listInternalOffsets.get(2)[2]] = -1.0;
            } else {
                dataVelocity[row][listInternalOffsets.get(2)[2]] = velocityCalc.getMachToVtas(dataAtmParam[row][listInternalOffsets.get(1)[3]], dataVelocity[row][listInternalOffsets.get(2)[1]]);
            }
        }

        // V (eas), м/с
        for (int row = 0; row <= dataVelocity.length - 1; row++) {
            if (dataVelocity[row][listInternalOffsets.get(2)[1]] <= 0.0){
                dataVelocity[row][listInternalOffsets.get(2)[3]] = -1.0;
            } else {
                dataVelocity[row][listInternalOffsets.get(2)[3]] = velocityCalc.getVtasToVeas(dataAtmParam[row][listInternalOffsets.get(1)[0]], dataVelocity[row][listInternalOffsets.get(2)[2]]);
            }
        }

        // q, Па
        for (int row = 0; row <= dataVelocity.length - 1; row++) {
            if (dataVelocity[row][listInternalOffsets.get(2)[1]] <= 0.0){
                dataVelocity[row][listInternalOffsets.get(2)[0]] = -1.0;
                dataVelocity[row][listInternalOffsets.get(2)[4]] = -1.0;
            } else {
                dataVelocity[row][listInternalOffsets.get(2)[4]] = velocityCalc.getDynPress(dataAtmParam[row][listInternalOffsets.get(1)[0]], dataVelocity[row][listInternalOffsets.get(2)[2]]);
            }
        }

    }



}











