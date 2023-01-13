package exportexcel;

import enums.UnitOutput;

import java.util.ArrayList;

class UnitMatching {

    /**
     * Модуль сопоставления единиц измерения.
     *
     * Сопоставляется индекст внутреннего смещения в блоке с соответствующей ему единицей измерения в настройках вывода.
     *
     * @param listDataIndex
     * @param column
     * @param listInternalOffsets
     * @param unitOutput
     * @return
     */

    protected UnitOutput getUnit(int listDataIndex, int column, ArrayList<int[]> listInternalOffsets, UnitOutput[] unitOutput) {

        UnitOutput unit = null;

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

                // высота (длина) во вспомогательном столбце
                if (column == listInternalOffsets.get(0)[2]) {
                    unit = unitOutput[0];
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






