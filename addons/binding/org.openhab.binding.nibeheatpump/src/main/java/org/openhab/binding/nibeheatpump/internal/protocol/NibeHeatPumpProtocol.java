/**
 * Copyright (c) 2014-2017 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.nibeheatpump.internal.protocol;

import org.apache.commons.lang.ArrayUtils;
import org.openhab.binding.nibeheatpump.internal.NibeHeatPumpException;

/**
 * Class for parse data packets from Nibe heat pumps
 *
 * @author Pauli Anttila - Initial contribution
 */
public class NibeHeatPumpProtocol {

    public static final byte FRAME_START_CHAR_FROM_NIBE = (byte) 0x5C;
    public static final byte FRAME_START_CHAR_TO_NIBE = (byte) 0xC0;

    public static final byte OFFSET_START = 0;
    public static final byte OFFSET_ADR = 2;
    public static final byte OFFSET_CMD = 3;
    public static final byte OFFSET_LEN = 4;
    public static final byte OFFSET_DATA = 5;

    public static final byte CMD_RMU_DATA_MSG = (byte) 0x62;
    public static final byte CMD_MODBUS_DATA_MSG = (byte) 0x68;
    public static final byte CMD_MODBUS_READ_REQ = (byte) 0x69;
    public static final byte CMD_MODBUS_READ_RESP = (byte) 0x6A;
    public static final byte CMD_MODBUS_WRITE_REQ = (byte) 0x6B;
    public static final byte CMD_MODBUS_WRITE_RESP = (byte) 0x6C;

    public static final byte ADR_SMS40 = (byte) 0x16;
    public static final byte ADR_RMU40 = (byte) 0x19;
    public static final byte ADR_MODBUS40 = (byte) 0x20;

    public static boolean isModbus40DataReadOut(byte[] data) {

        if (data[OFFSET_START] == FRAME_START_CHAR_FROM_NIBE && data[1] == (byte) 0x00
                && data[OFFSET_ADR] == ADR_MODBUS40) {

            if (data[OFFSET_CMD] == CMD_MODBUS_DATA_MSG && data[OFFSET_LEN] >= (byte) 0x50) {
                return true;
            }
        }

        return false;
    }

    public static boolean isModbus40ReadResponse(byte[] data) {

        if (data[OFFSET_START] == FRAME_START_CHAR_FROM_NIBE && data[1] == (byte) 0x00
                && data[OFFSET_ADR] == ADR_MODBUS40) {

            if (data[OFFSET_CMD] == CMD_MODBUS_READ_RESP && data[OFFSET_LEN] >= (byte) 0x06) {
                return true;
            }
        }

        return false;
    }

    public static boolean isRmu40DataReadOut(byte[] data) {

        if (data[0] == FRAME_START_CHAR_FROM_NIBE && data[1] == (byte) 0x00 && data[OFFSET_ADR] == ADR_RMU40) {

            if (data[OFFSET_CMD] == CMD_RMU_DATA_MSG && data[OFFSET_LEN] >= (byte) 0x18) {
                return true;
            }
        }

        return false;
    }

    public static boolean isModbus40WriteResponsePdu(byte[] data) {

        if (data[OFFSET_START] == FRAME_START_CHAR_FROM_NIBE && data[1] == (byte) 0x00
                && data[OFFSET_ADR] == ADR_MODBUS40 && data[OFFSET_CMD] == CMD_MODBUS_WRITE_RESP) {
            return true;
        }

        return false;
    }

    public static boolean isModbus40WriteTokenPdu(byte[] data) {

        if (data[0] == FRAME_START_CHAR_FROM_NIBE && data[1] == (byte) 0x00 && data[OFFSET_ADR] == ADR_MODBUS40
                && data[OFFSET_CMD] == CMD_MODBUS_WRITE_REQ && data[OFFSET_LEN] == 0x00) {
            return true;
        }

        return false;
    }

    public static boolean isModbus40ReadTokenPdu(byte[] data) {

        if (data[OFFSET_START] == FRAME_START_CHAR_FROM_NIBE && data[1] == (byte) 0x00
                && data[OFFSET_ADR] == ADR_MODBUS40 && data[OFFSET_CMD] == CMD_MODBUS_READ_REQ
                && data[OFFSET_LEN] == 0x00) {
            return true;
        }

        return false;
    }

    public static boolean isModbus40WriteRequestPdu(byte[] data) {

        if (data[0] == FRAME_START_CHAR_TO_NIBE && data[1] == CMD_MODBUS_WRITE_REQ) {
            return true;
        }

        return false;
    }

    public static boolean isModbus40ReadRequestPdu(byte[] data) {

        if (data[OFFSET_START] == FRAME_START_CHAR_TO_NIBE && data[1] == CMD_MODBUS_READ_REQ) {
            return true;
        }

        return false;
    }

    public static byte calculateChecksum(byte[] data) {
        return calculateChecksum(data, 0, data.length);
    }

    public static byte calculateChecksum(byte[] data, int startIndex, int stopIndex) {
        byte checksum = 0;
        // calculate XOR checksum
        for (int i = startIndex; i < stopIndex; i++) {
            checksum ^= data[i];
        }
        return checksum;
    }

    /*
     * public static byte[] createModbus40ReadPdu(int coildAddress) {
     *
     * byte[] data = new byte[6];
     * data[0] = (byte) 0xC0;
     * data[1] = CMD_MODBUS_READ_REQ;
     * data[2] = (byte) 0x02; // data len
     * data[3] = (byte) (coildAddress & 0xFF);
     * data[4] = (byte) ((coildAddress >> 8) & 0xFF);
     *
     * // calculate XOR checksum
     * for (int i = 0; i < 5; i++) {
     * data[5] ^= data[i];
     * }
     *
     * return data;
     * }
     *
     * public static byte[] createModbus40WritePdu(PumpModel pumpModel, int coildAddress, int value) {
     *
     * VariableInformation variableInfo = VariableInformation.getVariableInfo(pumpModel, coildAddress);
     *
     * byte[] data = new byte[10];
     *
     * data[0] = (byte) 0xC0;
     * data[1] = CMD_MODBUS_WRITE_REQ;
     * data[2] = (byte) 0x06; // data len
     * data[3] = (byte) (coildAddress & 0xFF);
     * data[4] = (byte) ((coildAddress >> 8) & 0xFF);
     *
     * switch (variableInfo.dataType) {
     *
     * case U8:
     * case S8:
     * data[5] = (byte) (value & 0xFF);
     * data[6] = 0;
     * data[7] = 0;
     * data[8] = 0;
     * break;
     *
     * case U16:
     * case S16:
     * data[5] = (byte) (value & 0xFF);
     * data[6] = (byte) ((value >> 8) & 0xFF);
     * data[7] = 0;
     * data[8] = 0;
     * break;
     *
     * case U32:
     * case S32:
     * data[5] = (byte) (value & 0xFF);
     * data[6] = (byte) ((value >> 8) & 0xFF);
     * data[7] = (byte) ((value >> 16) & 0xFF);
     * data[8] = (byte) ((value >> 24) & 0xFF);
     * break;
     * }
     *
     * // calculate XOR checksum
     * for (int i = 0; i < 9; i++) {
     * data[9] ^= data[i];
     * }
     *
     * return data;
     * }
     */

    public static byte[] checkMessageChecksumAndRemoveDoubles(byte[] data) throws NibeHeatPumpException {
        int msglen = 0;
        int startIndex = 0;
        int stopIndex = 0;

        if (NibeHeatPumpProtocol.isModbus40ReadRequestPdu(data)
                || NibeHeatPumpProtocol.isModbus40WriteRequestPdu(data)) {
            msglen = 3 + data[2];
            startIndex = 0;
            stopIndex = msglen;
        } else {
            msglen = 5 + data[OFFSET_LEN];
            startIndex = 2;
            stopIndex = msglen;
        }

        final byte checksum = calculateChecksum(data, startIndex, stopIndex);
        final byte msgChecksum = data[msglen];

        // if checksum is 0x5C (start character), heat pump seems to send 0xC5 checksum

        if (checksum == msgChecksum || (checksum == FRAME_START_CHAR_FROM_NIBE && msgChecksum == (byte) 0xC5)) {

            // if data contains 0x5C (start character), data seems to contains double 0x5C characters

            // let's remove doubles
            for (int i = 1; i < msglen; i++) {
                if (data[i] == FRAME_START_CHAR_FROM_NIBE) {
                    data = ArrayUtils.remove(data, i);
                    msglen--;

                    // fix message len
                    data[OFFSET_LEN]--;
                }
            }
        } else {
            throw new NibeHeatPumpException(
                    "Checksum does not match. Checksum=" + (msgChecksum & 0xFF) + ", expected=" + (checksum & 0xFF));
        }

        return data;
    }
}
