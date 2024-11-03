package org.example.defender;

import org.example.defender.exception.OSNotFoundException;
import org.example.defender.model.DTC;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Defender {

    private String os;

    public Defender() {
        this.os = getOperatingSystem();
    }

    private String getOperatingSystem() {
        String os = System.getProperty("os.name").toLowerCase();
        if (DTC.SYSTEM.WIN_VERSIONS.contains(os)) {
            return DTC.SYSTEM.WIN;
        }
        throw new OSNotFoundException(String.format("Operating system '%s' is not supported", os));
    }

    public Process createProcess(String processName) throws IOException {
        return Runtime.getRuntime().exec(processName);
    }

    public List<String> executeProcess(Process process) throws IOException {
        List<String> result = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            result.add(line);
        }
        return result;
    }

    public boolean isWindows() {
        return os.equals(DTC.SYSTEM.WIN);
    }

    public boolean isLinux() {
        return os.equals(DTC.SYSTEM.NIX) || os.equals(DTC.SYSTEM.NUX);
    }

}
