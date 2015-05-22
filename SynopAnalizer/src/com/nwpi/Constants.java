package com.nwpi;

public class Constants {

	public static final char UNKNOWN = '/';

	public static final String LAND_STATION_CODE = "AAXX";
	public static final String SHIP_STATION_CODE = "BBXX";
	public static final String MOBILE_LAND_STATION_CODE = "OOXX";
	
	// WS - WIND SPEED
	//
	public static final char WS_WILDTYPE_IN_MPS = '0';
	public static final char WS_ANEMOMETER_IN_MPS = '1';
	public static final char WS_WILDTYPE_IN_KNOT = '3';
	public static final char WS_ANEMOMETER_IN_KNOT = '4';
	
	public static final String CHAPTER_3_CODE = "333";
	public static final String CHAPTER_4_CODE = "444";
	public static final String CHAPTER_5_CODE = "555";

	public static final char PLUS_SIGN_TEMPERATURE = '0';
	public static final char MINUS_SIGN_TEMPERATURE = '1';
	
	// DFG - DOWNFALL GROUP
	//
	public static final char DFG_IN_CHAPTER_1_AND_3 = '0';
	public static final char DFG_IN_CHAPTER_1 = '1';
	public static final char DFG_IN_CHAPTER_3 = '2';
	public static final char DFG_NO_DF = '3';
	public static final char DFG_NO_MEASUREMENT = '4';
	
	// HV - HORIZONTAL VISIBILITY [IN KILOMETERS]
	// 00 MEANS HV = BELOW 0,1
	// DECIMAL SCOPE MEANS HV = XX / 10
	// UNIT SCOPE MEANS HV = XX - 50
	// 89 MEANS HV = OVER 70
	//
	public static final String HV_LOWER_LIMIT_DECIMAL_LEAP = "00";
	public static final String HV_UPPER_LIMIT_DECIMAL_LEAP = "50";
	public static final String HV_LOWER_LIMIT_UNIT_LEAP = "56";
	public static final String HV_UPPER_LIMIT_UNIT_LEAP = "89";
	
	// CH - CLOUD HEIGH [IN METERS]
	//
	public static final char CH_0_50 = '0';
	public static final char CH_50_100 = '1';
	public static final char CH_100_200 = '2';
	public static final char CH_200_300 = '3';
	public static final char CH_300_600 = '4';
	public static final char CH_600_1000 = '5';
	public static final char CH_1000_1500 = '6';
	public static final char CH_1500_2000 = '7';
	public static final char CH_2000_2500 = '8';
	public static final char CH_OVER_2500 = '9';
	
}
