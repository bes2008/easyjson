package com.jn.easyjson.tests.entity.struct;

public class Lic {
    private String serialNumber;
    private byte[] licDataBytes;

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public byte[] getLicDataBytes() {
        return licDataBytes;
    }

    public void setLicDataBytes(byte[] licDataBytes) {
        this.licDataBytes = licDataBytes;
    }
}
