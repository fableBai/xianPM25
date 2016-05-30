

package com.example.xian_pm25;

import android.content.Context;
import android.content.Intent;


public  class PollInfo{
	

    private String CityName;
    
    private String CityPollInfo;
    
    private String SO2;
    
    private String NO2;
    
    private String PM10;
    
    private String CO;
    
    private String O31;
    
    private String O38;
    
    private String PM25;
    
    private String average;
    
    private int Num;

    public PollInfo(Context context) {
		// TODO Auto-generated constructor stub
	}


	public PollInfo() {
		// TODO Auto-generated constructor stub
	}
	public void SetValuebyNum(String txt, int Num)
	{
		switch (Num) {
        case 0:
   		 	setCityName(txt);
            break;
        case 1:
        	setSO2(txt);
            break;
        case 2:
        	setNO2(txt);
            break;
        case 3:
        	setPM10(txt);
            break;
        case 4:
        	setCO(txt);
            break;
        case 5:
        	setO31(txt);
            break;
        case 6:
        	setO38(txt);
            break;
        case 7:
        	setPM25(txt);
            break;
        case 8:
        	setState(txt);
            break;
        default:
            break;
        }
	}
	public int getNum() {
        return this.Num;
    }
    public void setNum(final int Num) {
        this.Num = Num;
    }
	public String getNumToStr() {
		String s = new String();
        return s.valueOf(Num);
    }
	public String getSO2() {
        return this.SO2;
    }
    public void setSO2(final String SO2) {
        this.SO2 = SO2;
    }
    
	public String getNO2() {
        return this.NO2;
    }
    public void setNO2(final String NO2) {
        this.NO2 = NO2;
    }
    
	public String getCO() {
        return this.CO;
    }
    public void setCO(final String CO) {
        this.CO = CO;
    }
    
	public String getPM10() {
        return this.PM10;
    }
    public void setPM10(final String PM10) {
        this.PM10 = PM10;
    }
    
	public String getPM25() {
        return this.PM25;
    }
    public void setPM25(final String PM25) {
        this.PM25 = PM25;
    }
    
	public String getCityName() {
        return this.CityName;
    }
    public void setCityName(final String CityName) {
        this.CityName = CityName;
    }
    
	public String getO31() {
        return this.O31;
    }
    public void setO31(final String O31) {
        this.O31 = O31;
    }
    
	public String getO38() {
        return this.O38;
    }
    public void setO38(final String O38) {
        this.O38 = O38;
    }
    
	public String getState() {
        return this.CityPollInfo;
    }
    public void setState(final String State) {
        this.CityPollInfo = State;
    }
    
	public String getAverage() {
        return this.average;
    }
    public void setAverage(final String Average) {
        this.average = Average;
    }
    
    
    @Override
    public String toString() {
        return getNumToStr() + "#";
    }
}
