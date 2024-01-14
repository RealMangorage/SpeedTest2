package org.mangorage.devtest.core;

public enum BitType {
    Kb(1024L, "Kb"),
    Mb(1024 * 1024L, "Mb"),
    Gb(1024 * 1024 * 1024L, "Gb"),
    Tb(1024 * 1024 * 1024 * 1024L, "Tb");

    private final long size;
    private final String prefix;
    BitType(long size, String prefix) {
        this.size = size;
        this.prefix = prefix;
    }

    public double get(long bits, Type type) {
        return type == Type.BIT ? (double) bits / size : (double) bits / (8 * size);
    }

    public double get(long bits) {
        return get(bits, Type.BIT);
    }

    public String getPrefix(Type type) {
        return type == Type.BIT ? prefix : prefix.toUpperCase();
    }

    public String getPrefix() {
        return getPrefix(Type.BIT);
    }

    public static Result getLargestSize(long bits, Type type) {
        double lastSize = 0;
        BitType lastType = null;

        for (BitType value : BitType.values()) {
            lastSize = value.get(bits, type);
            lastType = value;
            if (lastSize < 1000) {
                break;
            }
        }

        return new Result(lastType, bits, lastSize, type);
    }

    public static Result getLargestSize(long bits) {
        return getLargestSize(bits, Type.BIT);
    }


    public enum Type {
        BIT,
        BYTE
    }
    public static class Result {
        private final BitType type;
        private final long bits;
        private final double size;
        private final Type returnType;
        public Result(BitType type, long bits, double size, Type returnType) {
            this.type = type;
            this.bits = bits;
            this.size = size;
            this.returnType = returnType;
        }
        public boolean isBytes() {
            return returnType == Type.BYTE;
        }

        public String getString() {
            return String.format("%s%s", size, type.getPrefix(returnType));
        }

        public String getRoundedString() {
            return String.format("%s%s", Math.ceil(size), type.getPrefix(returnType));
        }
    }
}
