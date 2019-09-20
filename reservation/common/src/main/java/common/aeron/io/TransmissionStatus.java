package common.aeron.io;

public enum TransmissionStatus {
    OK, NOT_CONNECTED, BACK_PRESSURE, CLOSED;

    public static TransmissionStatus fromAeronCode(long code) {
        if (code > 0) {
            return OK;
        }

        if (code == -1L) {
            return NOT_CONNECTED;
        }

        if (code == -2L || code == -3L) { // ADMIN_ACTION/BACK_PRESSURED
            return BACK_PRESSURE;
        }

        /* CLOSED || MAX_POSITION_EXCEEDED */
        return CLOSED;
    }
}
