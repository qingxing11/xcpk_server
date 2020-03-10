package com.wt.xcpk.vo.poker;

public enum FaceTypeEnum {
	FaceValueNum_Null1(0, "异常",0),
	FaceValueNum_Null2(1, "异常",0),
	FaceValueEnum_2(2, "2",1),
	FaceValueEnum_3(3, "3",5),
	FaceValueEnum_4(4, "4",9),
	FaceValueEnum_5(5, "5",13),
	FaceValueEnum_6(6, "6",17),
	FaceValueEnum_7(7, "7",21),
	FaceValueEnum_8(8, "8",25),
	FaceValueEnum_9(9, "9",29),
	FaceValueEnum_10(10, "10",33),
	FaceValueEnum_J(11, "J",37),
	FaceValueEnum_Q(12, "Q",41),
	FaceValueEnum_K(13, "K",45),
	FaceValueEnum_A(14, "A",49),
	FaceValueEnum_SmallJorker(30, "小王",0),
	FaceValueEnum_BigJorker(40, "大王",0),
	FaceValueEnum_next(0,"",0),
	;
	 
	// 数值
	public static final int FACE_TYPE_NULL = -1;
	
	public static final int FACE_TYPE_SMALL_JORKER = 30;
	public static final int FACE_TYPE_BIG_JORKER   = 40;
	
	public static final int FACE_TYPE_MIN_VALUE = 0;
	public static final int FACE_TYPE_2_VALUE = 2;
	public static final int FACE_TYPE_3_VALUE = 3;
	public static final int FACE_TYPE_4_VALUE = 4;
	public static final int FACE_TYPE_5_VALUE = 5;
	public static final int FACE_TYPE_6_VALUE = 6;
	public static final int FACE_TYPE_7_VALUE = 7;
	public static final int FACE_TYPE_8_VALUE = 8;
	public static final int FACE_TYPE_9_VALUE = 9;
	public static final int FACE_TYPE_10_VALUE = 10;
	public static final int FACE_TYPE_J_VALUE = 11;
	public static final int FACE_TYPE_Q_VALUE = 12;
	public static final int FACE_TYPE_K_VALUE = 13;
	public static final int FACE_TYPE_A_VALUE = 14;
	public static final int FACE_TYPE_MAIN_VALUE = 20;
	
	
	private int value;
	private String desc;
	private int majorValue;
	private FaceTypeEnum(int code, String desc,int majorValue)
	{
		this.value = code;
		this.desc = desc;
		this.majorValue = majorValue;
		
//		System.err.println(toString());
	}

	public FaceTypeEnum getNext()
	{
		FaceTypeEnum nextFaceType = null;
		 switch (value)
		{
			case FACE_TYPE_2_VALUE:
				nextFaceType = FaceValueEnum_3;
				break;

			case FACE_TYPE_3_VALUE:
				nextFaceType = FaceValueEnum_4;
				break;
				
			case FACE_TYPE_4_VALUE:
				nextFaceType = FaceValueEnum_5;
				break;
				
			case FACE_TYPE_5_VALUE:
				nextFaceType = FaceValueEnum_6;
				break;
				
			case FACE_TYPE_6_VALUE:
				nextFaceType = FaceValueEnum_7;
				break;
				
			case FACE_TYPE_7_VALUE:
				nextFaceType = FaceValueEnum_8;
				break;
				
			case FACE_TYPE_8_VALUE:
				nextFaceType = FaceValueEnum_9;
				break;
				
			case FACE_TYPE_9_VALUE:
				nextFaceType = FaceValueEnum_10;
				break;
				
			case FACE_TYPE_10_VALUE:
				nextFaceType = FaceValueEnum_J;
				break;
				
			case FACE_TYPE_J_VALUE:
				nextFaceType = FaceValueEnum_Q;
				break;
				
			case FACE_TYPE_Q_VALUE:
				nextFaceType = FaceValueEnum_K;
				break;
				
			case FACE_TYPE_K_VALUE:
				nextFaceType = FaceValueEnum_A;
				break;
				
			default:
				nextFaceType = FaceValueEnum_A;
				break;
		}
		return nextFaceType;
	}
	
	public FaceTypeEnum getBackLevel()
	{
		FaceTypeEnum nextFaceType = null;
		 switch (value)
		{
			case FACE_TYPE_3_VALUE:
				nextFaceType = FaceValueEnum_2;
				break;

			case FACE_TYPE_4_VALUE:
				nextFaceType = FaceValueEnum_3;
				break;
				
			case FACE_TYPE_5_VALUE:
				nextFaceType = FaceValueEnum_4;
				break;
				
			case FACE_TYPE_6_VALUE:
				nextFaceType = FaceValueEnum_5;
				break;
				
			case FACE_TYPE_7_VALUE:
				nextFaceType = FaceValueEnum_6;
				break;
				
			case FACE_TYPE_8_VALUE:
				nextFaceType = FaceValueEnum_7;
				break;
				
			case FACE_TYPE_9_VALUE:
				nextFaceType = FaceValueEnum_8;
				break;
				
			case FACE_TYPE_10_VALUE:
				nextFaceType = FaceValueEnum_9;
				break;
				
			case FACE_TYPE_J_VALUE:
				nextFaceType = FaceValueEnum_10;
				break;
				
			case FACE_TYPE_Q_VALUE:
				nextFaceType = FaceValueEnum_J;
				break;
				
			case FACE_TYPE_K_VALUE:
				nextFaceType = FaceValueEnum_Q;
				break;
				
			case FACE_TYPE_A_VALUE:
				nextFaceType = FaceValueEnum_K;
				break;
				
			default:
				nextFaceType = FaceValueEnum_2;
				break;
		}
		return nextFaceType;
	}
	
	public static FaceTypeEnum geFaceTypeEnum(int value)
	{
		FaceTypeEnum faceType = FaceValueNum_Null1;
		 switch (value)
		{
			case FACE_TYPE_2_VALUE:
				faceType = FaceValueEnum_2;
				break;

			case FACE_TYPE_3_VALUE:
				faceType = FaceValueEnum_3;
				break;
				
			case FACE_TYPE_4_VALUE:
				faceType = FaceValueEnum_4;
				break;
				
			case FACE_TYPE_5_VALUE:
				faceType = FaceValueEnum_5;
				break;
				
			case FACE_TYPE_6_VALUE:
				faceType = FaceValueEnum_6;
				break;
				
			case FACE_TYPE_7_VALUE:
				faceType = FaceValueEnum_7;
				break;
				
			case FACE_TYPE_8_VALUE:
				faceType = FaceValueEnum_8;
				break;
				
			case FACE_TYPE_9_VALUE:
				faceType = FaceValueEnum_9;
				break;
				
			case FACE_TYPE_10_VALUE:
				faceType = FaceValueEnum_10;
				break;
				
			case FACE_TYPE_J_VALUE:
				faceType = FaceValueEnum_J;
				break;
				
			case FACE_TYPE_Q_VALUE:
				faceType = FaceValueEnum_Q;
				break;
				
			case FACE_TYPE_K_VALUE:
				faceType = FaceValueEnum_K;
				break;
				
			case FACE_TYPE_A_VALUE:
				faceType = FaceValueEnum_A;
				break;
				
			default:
				break;
		}
		return faceType;
	}

	public String getDesc()
	{
		return desc;
	}

	public final int getType()
	{
		return value;
	}
	
	public final int getMajorValue()
	{
		return majorValue;
	}

	public static int getSpecficHeartByFaceValue(int nFaceValue)
	{
		if (FACE_TYPE_A_VALUE == nFaceValue)
		{
			return PokerValueEnum.POKER_A_红桃;
		}

		int nCardTypeActor = nFaceValue - 2;
		int nSpecficHeartValue = (nCardTypeActor * 4) + 1;
		return nSpecficHeartValue;
	}

	public static boolean isBelongToKing(int nFaceValue)
	{
		if (FACE_TYPE_SMALL_JORKER == nFaceValue)
		{
			return true;
		}

		if (FACE_TYPE_BIG_JORKER == nFaceValue)
		{
			return true;
		}

		return false;
	}

	public static boolean isNextCardType(int nPreCardType, int nNextCardType)
	{
		if (nPreCardType < FACE_TYPE_2_VALUE)
		{
			return false;
		}

		if (nPreCardType + 1 == nNextCardType)
		{
			return true;
		}

		return false;
	}

	public static int getNextCardType(int nCardType)
	{
		if ((nCardType >= FACE_TYPE_A_VALUE) || (nCardType < FACE_TYPE_2_VALUE))
		{
			return 0;
		}

		int nNextCardType = nCardType + 1;
		return nNextCardType;
	}

	public static int getPreCardType(int nCardType)
	{
		if ((nCardType > FACE_TYPE_A_VALUE) || (nCardType <= FACE_TYPE_2_VALUE))
		{
			return 0;
		}

		int nPreCardType = nCardType - 1;
		return nPreCardType;
	}

	public static int getMinCardType()
	{
		return FACE_TYPE_2_VALUE;
	}

	public static int getMaxCardType()
	{
		return FACE_TYPE_A_VALUE;
	}

	public static int incFaceValueEnum(int i)
	{
		if (FACE_TYPE_A_VALUE == i)
		{
			return FACE_TYPE_2_VALUE;
		}

		int nIncFaceValueNum = i + 1;
		return nIncFaceValueNum;
	}

	public static int addFaceValueEnum(int i, int nAddValue)
	{
		if (FACE_TYPE_A_VALUE == i)
		{
			int nValue = FACE_TYPE_2_VALUE + (nAddValue - 1);
			return nValue;
		}

		int nResultValue = i + nAddValue;
		return nResultValue;
	}

	public static boolean isBelongToSmallKing(int nFaceValue)
	{
		if (FACE_TYPE_SMALL_JORKER == nFaceValue)
		{
			return true;
		}

		return false;
	}

	public static boolean isBelongToBigKing(int nFaceValue)
	{
		if (FACE_TYPE_BIG_JORKER == nFaceValue)
		{
			return true;
		}
		return false;
	}
	
	public String toString()
	{
		String str = "value:"+getType()+",desc"+getDesc()+",major:"+getMajorValue();
		return str;
	}
}