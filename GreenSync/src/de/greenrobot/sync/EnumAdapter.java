package de.greenrobot.sync;

import de.greenrobot.dao.DaoEnum;

import java.util.Arrays;
import java.util.Comparator;

/**
 * Converts values of an enum to and from integers.
 */
final class EnumAdapter<E extends DaoEnum> {

    private static final Comparator<DaoEnum> COMPARATOR = new Comparator<DaoEnum>() {
        @Override
        public int compare(DaoEnum o1, DaoEnum o2) {
            return (int) (o1.getValue() - o2.getValue());
        }
    };

    private final Class<E> type;

    private final int[] values;
    private final E[] constants;
    private final boolean isDense;

    EnumAdapter(Class<E> type) {
        this.type = type;

        constants = type.getEnumConstants();
        Arrays.sort(constants, COMPARATOR);

        int length = constants.length;
        if (constants[0].getValue() == 1 && constants[length - 1].getValue() == length) {
            // Values completely fill the range from 1..length
            isDense = true;
            values = null;
        } else {
            isDense = false;
            values = new int[length];
            for (int i = 0; i < length; i++) {
                values[i] = (int) constants[i].getValue();
            }
        }
    }

    public int toInt(E e) {
        return (int) e.getValue();
    }

    public E fromInt(int value) {
        int index = isDense ? value - 1 : Arrays.binarySearch(values, value);
        try {
            return constants[index];
        } catch (IndexOutOfBoundsException e) {
            throw new IllegalArgumentException(
                    "Unknown enum tag " + value + " for " + type.getCanonicalName());
        }
    }
}