package org.example.defender.model;

import java.util.List;

import static java.util.Arrays.asList;

/**
 * Defender Tool Class
 */
public class DTC {

    public  class PORT{
        public static final String DEFAULT_PORT_SCANNING ="DEFAULT_PORT_SCANNING";
        public static final String SHORT_PORT_SCANNING="SHORT_PORT_SCANNING";
    }

    public  class IPAddress{
        public static final String IP_ADDRESS_SCANNING="IP_ADDRESS_SCANNING";
    }

    public  class SYSTEM{
        public static final String OPERATING_SYSTEM="OPERATING_SYSTEM";
        public static final String WIN="win";
        public static final List<String> WIN_VERSIONS=asList("windows 11");
        public static final String NIX="nix";
        public static final String NUX="nux";
    }

}
