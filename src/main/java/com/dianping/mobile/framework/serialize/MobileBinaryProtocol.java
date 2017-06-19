/**
 * 
 */
package com.dianping.mobile.framework.serialize;

/**
 * @author kewen.yao
 *
 */
public interface MobileBinaryProtocol {
	char BIN_NULL_VALUE = 'N';
	char BIN_BOOLEAN_TRUE = 'T';
	char BIN_BOOLEAN_FALSE = 'F';
	char BIN_INTEGER_TYPE = 'I';
	char BIN_LONG_TYPE = 'L';
	char BIN_DOUBLE_TYPE = 'D';
	char BIN_UTCDATE_TYPE = 'U';
	char BIN_STRING_TYPE = 'S';
	char BIN_OBJECT_TYPE_BEGIN = 'O';
	char BIN_OBJECT_MEMBER = 'M';
	char BIN_OBJECT_TYPE_END = 'Z';
	char BIN_ARRAY_TYPE = 'A';
}
