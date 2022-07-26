package com.dmwa.dpg3.GCP;

public class VMConnection {
    private boolean isCurrentConnection;
    private String vmConnection;

    public VMConnection(boolean isCurrentConnection, String vmConnection) {
        this.isCurrentConnection = isCurrentConnection;
        this.vmConnection = vmConnection;
    }

    public boolean isCurrentConnection() {
        return isCurrentConnection;
    }

    public void setCurrentConnection(boolean currentConnection) {
        isCurrentConnection = currentConnection;
    }

    public String getVMConnection() {
        return vmConnection;
    }

    public void setVMConnection(String otherVMConnection) {
        this.vmConnection = otherVMConnection;
    }
}
