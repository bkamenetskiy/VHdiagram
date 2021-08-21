package exportexcel;

import enums.Unit;

import java.util.ArrayList;

class UnitMatching {

    // сопоставление стобца данных и его единиц измерения

    protected Unit getUnit(int listDataIndex, int column, ArrayList<int[]> listInternalOffsets, Unit[] unitOutput) {

        Unit unit = null;

        switch (listDataIndex) {

            // блок с высотами
            case 0:
                // высота (длина) в метрической системе
                if (column == listInternalOffsets.get(0)[0]) {
                    unit = unitOutput[0];
                }

                // высота (длина) в имперской системе
                if (column == listInternalOffsets.get(0)[1]) {
                    unit = unitOutput[1];
                }
                break;

            // блок с атмосферой
            case 1:

                // плотность
                if (column == listInternalOffsets.get(1)[0]) {
                    unit = unitOutput[3];
                }

                // атмосферное давление
                if (column == listInternalOffsets.get(1)[1]) {
                    unit = unitOutput[4];
                }

                // температура
                if (column == listInternalOffsets.get(1)[2]) {
                    unit = unitOutput[5];
                }

                // скорость звука
                if (column == listInternalOffsets.get(1)[3]) {
                    unit = unitOutput[2];
                }
                break;

            // все остальные блоки
            default:

                // Vcas
                if (column == listInternalOffsets.get(2)[0]) {
                    unit = unitOutput[2];
                }

                // M
                if (column == listInternalOffsets.get(2)[1]) {
                    unit = unitOutput[6];
                }

                // Vtas
                if (column == listInternalOffsets.get(2)[2]) {
                    unit = unitOutput[2];
                }

                // Veas
                if (column == listInternalOffsets.get(2)[3]) {
                    unit = unitOutput[2];
                }

                // q
                if (column == listInternalOffsets.get(2)[4]) {
                    unit = unitOutput[4];
                }
                break;

        }
        return unit;
    }

}






