package com.dbserver.votacao.util;


public final class DateConverter {
    private DateConverter() {
        throw new UnsupportedOperationException("Classe utilitária - não instanciar.");
    }

    public static java.time.LocalDateTime convertTimestampToLocalDateTime(java.sql.Timestamp timestamp) {
        if (timestamp == null) {
            return null;
        }
        return timestamp.toLocalDateTime();
    }

    public static java.sql.Timestamp convertLocalDateTimeToTimestamp(java.time.LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return null;
        }
        return java.sql.Timestamp.valueOf(localDateTime);
    }

}
