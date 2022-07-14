package com.epic.common.util;

import com.epic.common.models.NICType;

import java.time.LocalDate;
import java.time.Period;

public class NicValidations {

    public static NICType checkNICType(String nic) throws Exception {
        if (nic.length() == 12) {
            return NICType.NEW;
        } else if (nic.length() == 10) {
            return NICType.OLD;
        } else {
            return null;
        }
    }

    public static String convertOldNicTONewNic(String nic) throws Exception {
        String newNic =
                "19".concat(nic.substring(0, 2)).concat(nic.substring(2, 5)).concat("0".concat(nic.substring(5, 8)))
                        .concat(nic.substring(nic.length() - 2, nic.length() - 1));
        return newNic;
    }

    public static boolean comparePeriod(LocalDate dateNow, LocalDate dob, int period){

        Period p = Period.between(dob, dateNow);
        if(p.getYears()>=period){
            return true;
        }

        return false;
    }

    public static class NicDetails {
        String id ,sex;

        int year = 0,month = 0, date = 0;
        int months[] = {31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        public NicDetails(String nic) {
            id = nic;
            setYear();
            setDays();
            setMonth();
            setSex();
        }

        private void setYear() {

                this.year = Integer.parseInt(id.substring(0, 4));

        }

        private void setDays() {
            int d = Integer.parseInt(id.substring(4, 7));

            if (d > 500) {
                this.date = (d - 500);
            } else {
                this.date =  d;
            }
        }

        private void setMonth() {

            int month = 0,day = 0;

            int days = this.date;

            for (int i = 0; i < months.length; i++) {
                if (days < months[i]) {
                    month = i + 1;
                    day = days;
                    break;
                } else {
                    days = days - months[i];
                }
            }

            this.month = month;
            this.date = day;

        }

        public void setSex() {

            String M = "Male", F = "Female";

            int  d = Integer.parseInt(id.substring(4, 7));

            if (d > 500) {
                this.sex =  F;
            } else {
                this.sex = M;
            }

        }

        public LocalDate getDateOfBirth(){

           return LocalDate.of(year,month,date);

        }

    }
}
