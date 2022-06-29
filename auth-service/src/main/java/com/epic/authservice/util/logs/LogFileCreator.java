package com.epic.authservice.util.logs;


import com.epic.authservice.util.config.Configurations;
import com.epic.authservice.util.varlist.CommonVarList;

import java.io.*;
import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by bhanuka_t on 2/13/2018.
 */
public class LogFileCreator {
    //write error log for api exceptions
    public synchronized static void writeErrorTologs(Exception ex) {
        BufferedWriter bw = null;
        FileWriter fw = null;
        String msg;
        String errorLogPath;
        try {

            if(Configurations.getOS_Type().equals(CommonVarList.OS_WINDOWS)){
                errorLogPath = "C:\\CMS_API\\LOGS\\ERROR";
            }else{
                errorLogPath = "/root/CMS_API/LOGS/ERROR";
            }

            String filename = getFileName() + "_API_ERROR.txt";

            //make directory if not exist
            File file = new File(errorLogPath);
            if (!file.exists()) {
                file.mkdirs();
            }

            filename = errorLogPath + File.separator + filename;
            msg = "\r\nERROR--> SERVER TIME: " + LocalDateTime.now() + "    EXCEPTION TYPE: " + getStackTrace(ex);

            fw = new FileWriter(filename, true);
            bw = new BufferedWriter(fw);
            bw.write(msg);
            bw.newLine();
            bw.flush();
        } catch (Exception ioe) {
            Logger.getLogger(LogFileCreator.class.getName()).log(Level.SEVERE, null, ioe);
        } finally {
            try {
                if (bw != null) {
                    bw.flush();
                    bw.close();
                }
                if (fw != null) {
                    fw.close();
                }
            } catch (Exception ioe2) {
            }
        }
    }

    private static String getFileName() throws Exception {

        LocalDateTime sysDate;
        sysDate = LocalDateTime.now();

        int currentYear = sysDate.getYear();
        int currentMonth = sysDate.getMonthValue();
        int currentDay = sysDate.getDayOfMonth();

        String currentMonthStr = Integer.toString(currentMonth);
        String currentDayStr = Integer.toString(currentDay);

        if (currentMonth < 10) {
            currentMonthStr = '0' + currentMonthStr;
        }
        if (currentDay < 10) {
            currentDayStr = '0' + currentDayStr;
        }
        return "" + currentYear + currentMonthStr + currentDayStr;

    }


    private static String getStackTrace(Throwable aThrowable) {
        String re = "";
        Writer result = null;
        PrintWriter printWriter = null;
        try {
            result = new StringWriter();
            printWriter = new PrintWriter(result);

            aThrowable.printStackTrace(printWriter);
            re = result.toString();
            result.close();
            printWriter.close();

        } catch (Exception e) {

        } finally {
            try {
                if (result != null) {
                    result.close();
                }
                if (printWriter != null) {
                    printWriter.close();
                }
            } catch (IOException e) {

            }
        }
        return re;
    }
}
