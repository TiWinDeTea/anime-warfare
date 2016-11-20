package org.tiwindetea.net;

public enum NetworkCommand {
    SCANNING(0x00);


    public final byte value;

    NetworkCommand(int value) {
        assert value <= Byte.MAX_VALUE && value >= Byte.MIN_VALUE;
        this.value = (byte) value;
    }
}
