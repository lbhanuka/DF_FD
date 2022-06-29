package com.epic.authservice.util.config;

import com.epic.authservice.util.varlist.CommonVarList;

/**
 * Created by bhanuka_t on 2/14/2018.
 */
public class Configurations {

    public static String getOS_Type() throws Exception {

        String osType ;
        String osName ;
        osName = System.getProperty("os.name", "").toLowerCase();

        // For WINDOWS
        if (osName.contains("windows")) {
            osType = CommonVarList.OS_WINDOWS;
        } else {
            // For LINUX
            if (osName.contains("linux")) {
                osType = CommonVarList.OS_LINUX;
            } else {
                throw new Exception("Cannot identify the Operating System.");
            }
        }

        return osType;
    }
}
