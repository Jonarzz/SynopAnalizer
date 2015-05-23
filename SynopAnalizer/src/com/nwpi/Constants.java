package com.nwpi;

public class Constants {

	public static final char UNKNOWN_CHAR = '/';
	public static final String UNKNOWN_STRING = '//';

	public static final String CHAPTER_3_CODE = "333";
	public static final String CHAPTER_4_CODE = "444";
	public static final String CHAPTER_5_CODE = "555";

	public static final char PLUS_SIGN_TEMPERATURE = '0';
	public static final char MINUS_SIGN_TEMPERATURE = '1';
	
	public static final String LAND_STATION_CODE = "AAXX";
	public static final String SHIP_STATION_CODE = "BBXX";
	public static final String MOBILE_LAND_STATION_CODE = "OOXX";
	
	
	////// BEGON YEAR-HOUR-WIND GROUP [YYHHW]
	//
	// WS - WIND SPEED
	//
	public static final char WS_WILDTYPE_IN_MPS = '0';
	public static final char WS_ANEMOMETER_IN_MPS = '1';
	public static final char WS_WILDTYPE_IN_KNOT = '3';
	public static final char WS_ANEMOMETER_IN_KNOT = '4';
	//
	////// END YEAR-HOUR-WIND GROUP [YYHHW]
	
	
	//////  BEGIN DOWNFALL-STATION-CLOUD-VISABILITY [xyzXX]
	//
	// DFG - DOWNFALL GROUP
	// VALUES FROM '0' TO '4'
	//
	public static final char DFG_IN_CHAPTER_1_AND_3 = '0';
	public static final char DFG_IN_CHAPTER_1 = '1';
	public static final char DFG_IN_CHAPTER_3 = '2';
	public static final char DFG_NO_DF = '3';
	public static final char DFG_NO_MEASUREMENT = '4';
	//
	// ST - STATION TYPE
	// A - AUTOMATIC
	// N - NON-AUTOMATIC
	// VALUES FROM '1' TO '6'
	//
	public static final char ST_N_GROUP_7_ON = '1';
	public static final char ST_N_GROUP_7_OFF_NO_PHENOMENON = '2';
	public static final char ST_N_GROUP_7_OFF_NO_DATA = '3';
	public static final char ST_A_GROUP_7_ON = '4';
	public static final char ST_A_GROUP_7_OFF_NO_PHENOMENON = '5';
	public static final char ST_A_GROUP_7_OFF_NO_DATA = '6';
	//
	// CH - CLOUD HEIGH [IN METERS]
	// VALUES FROM '0' TO '9'
	// '/' ACCEPTABLE
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
	//
	// HV - HORIZONTAL VISIBILITY [IN KILOMETERS]
	// VALUES FROM "00" TO "50" AND FROM "56" TO "99"
	// 00 MEANS HV = BELOW 0,1
	// DECIMAL SCOPE MEANS HV = XX / 10
	// UNIT SCOPE MEANS HV = XX - 50
	// 89 MEANS HV = OVER 70
	// 90-99 ROUGHLY NUMBERING						// TODO
	/*
	90 - < 0,05 km
	91 – 0,05 – 0,2 km
	92 – 0,2 – 0,5 km
	93 – 0,5 – 1,0 km
	94 – 1,0 – 2,0 km
	95 – 2,0 – 4,0 km
	96 – 4,0 – 10,0 km
	97 – 10,0 – 20,0 km
	98 – 20,0 – 50,0 km
	99 - > 50 km
	*/
	//
	public static final String HV_LOWER_LIMIT_DECIMAL_LEAP = "00";
	public static final String HV_UPPER_LIMIT_DECIMAL_LEAP = "50";
	public static final String HV_LOWER_LIMIT_UNIT_LEAP = "56";
	public static final String HV_UPPER_LIMIT_UNIT_LEAP = "89";
	//
	////// END DOWNFALL-STATION-CLOUD-VISABILITY [xyzXX]
	
	
	// BEGIN CLOUDINESS-WIND-DIRECTION-SPEED [xyyzz]
	//
	// CLOUDINESS
	// CLOUDINESS = x/8 OR:
	// VALUES FROM '0' TO '9'
	// '/' ACCEPTABLE
	//
	public static final char CLOUDINESS_SKY_UNSEEN = '9';
	//
	// WD - WIND DIRECTION
	// WD = FROM { 5 + (yy - 1) * 9 } TO { 5 + yy * 9 } OR:
	// VALUES FROM "00" TO "36" AND "99"
	// "//" ACCEPTABLE
	//
	public static final String WD_NO_WIND = "00";
	public static final String WD_VARIABLE = "99";
	//
	// WIND SPEED
	// WP = zz IN WD UNIT
	//
	// END CLOUDINESS-WIND-DIRECTION-SPEED [xyyzz]
}
