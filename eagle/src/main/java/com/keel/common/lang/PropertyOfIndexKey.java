package com.keel.common.lang;

import com.keel.utils.UKeyWorker;

/**
 * (预留) + ACT_Type（1B） + RESOURCE（1B）
 * */
public class PropertyOfIndexKey {
	//资源类型标识位数
	private final static long P_RESOURCE_TYPE_BITS = 8L;
	//动作类型标识位数
	private final static long P_ACT_TYPE_BITS = 8L;
	//动作类型标识偏左移
	private final static long P_ACT_TYPE_SHIFT = PropertyOfIndexKey.P_RESOURCE_TYPE_BITS;
	//资源类型标识最大值
	private final static long MAX_RESOURCE_TYPE = -1L ^ -1L << PropertyOfIndexKey.P_RESOURCE_TYPE_BITS;
	//动作类型标识最大值
	private final static long MAX_ACT_TYPE = -1L ^ -1L << PropertyOfIndexKey.P_ACT_TYPE_BITS;
	//资源类型标识Mask
	private final static long P_RESOURCE_TYPE_MASK = -1L ^ -1L << PropertyOfIndexKey.P_RESOURCE_TYPE_BITS;
	//动作类型标识Mask
	private final static long P_ACT_TYPE_MASK = ( -1L ^ -1L << PropertyOfIndexKey.P_ACT_TYPE_BITS )<< PropertyOfIndexKey.P_ACT_TYPE_SHIFT;
	
	/*资源类型定义*/
	//书评
	public final static long RESOURCE_REVIEW_FOR_BOOK = 0x1;
	//影评
	public final static long RESOURCE_REVIEW_FOR_FILM = 0x2;
	//连载
	public final static long RESOURCE_REVIEW_FOR_SERIALIZATION = 0x3;
	//日记
	public final static long RESOURCE_REVIEW_FOR_DIARY = 0x4;
	
	/*动作类型定义*/
	//NULL
	public final static long ACT_NONE = 0x0;
	//转
	public final static long ACT_FORWARD = 0x1;
	//评
	public final static long ACT_REVIEW = 0x2;
	//赞
	public final static long ACT_ZAN = 0x3;
	

	public static long createPropertyOfIndexKey(final long resourceType, final long actType) {
		if (resourceType > PropertyOfIndexKey.MAX_RESOURCE_TYPE || resourceType < 0) {
			throw new IllegalArgumentException(String.format(
					"resource type can't be greater than %d or less than 0",
					PropertyOfIndexKey.MAX_RESOURCE_TYPE));
		}
		
		if (actType > PropertyOfIndexKey.MAX_ACT_TYPE || actType < 0) {
			throw new IllegalArgumentException(String.format(
					"act type can't be greater than %d or less than 0",
					PropertyOfIndexKey.MAX_ACT_TYPE));
		}

		return (( actType << PropertyOfIndexKey.P_ACT_TYPE_SHIFT ) | resourceType);
	}
	
	public static long getResourceTypeOfProperty(final long p) {
		return p & PropertyOfIndexKey.P_RESOURCE_TYPE_MASK;

	}

	public static long getActTypeOfProperty(final long p) {
		return ( p & PropertyOfIndexKey.P_ACT_TYPE_MASK ) >> PropertyOfIndexKey.P_ACT_TYPE_SHIFT;
	}
	
	public static void main(String [] argv) {
		long p = PropertyOfIndexKey.createPropertyOfIndexKey(PropertyOfIndexKey.RESOURCE_REVIEW_FOR_FILM, PropertyOfIndexKey.ACT_FORWARD);
		System.out.println(Long.toBinaryString(p));
		
		System.out.println(Long.toBinaryString(PropertyOfIndexKey.getActTypeOfProperty(p)));
		System.out.println(Long.toBinaryString(PropertyOfIndexKey.getResourceTypeOfProperty(p)));
	}
}
