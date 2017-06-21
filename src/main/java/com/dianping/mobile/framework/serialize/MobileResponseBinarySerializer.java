/**
 *
 */
package com.dianping.mobile.framework.serialize;

import com.dianping.mobile.core.datatypes.ClientInfoRule;
import com.dianping.mobile.core.enums.ClientType;
import com.dianping.mobile.core.enums.Platform;
import com.dianping.mobile.core.util.ApiUtil;
import com.dianping.mobile.core.util.VersionUtil;
import com.dianping.mobile.framework.annotation.MobileDo;
import com.dianping.mobile.framework.annotation.MobileDo.MobileField;
import com.dianping.mobile.framework.annotation.MobileDo.MobilePlatform;
import com.dianping.mobile.framework.annotation.MobileDo.MobileVersion;
import com.dianping.mobile.framework.util.ClientRuleUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author kewen.yao
 */
public class MobileResponseBinarySerializer {

    private static final Logger log = Logger.getLogger(MobileResponseBinarySerializer.class);
    private static final Map<String, Map<Field, Annotation[]>> fieldAnnotationCache = new ConcurrentHashMap<String, Map<Field, Annotation[]>>();
    private DataOutputStream output;

    public MobileResponseBinarySerializer(OutputStream os) {
        this.output = new DataOutputStream(os);
    }

    public static int getHashCode(String value) {
        int hash = 0;
        for (char c : value.toCharArray()) {
            hash = 31 * hash + c;
        }
        return hash;
    }

    public static byte[] getHashByte(int length) {
        int lenBytes = (0xffff & length) ^ (length >> 16);
        return new byte[]{(byte) (lenBytes >> 8), (byte) (lenBytes)};
    }

    private static boolean needSerialzation(MobilePlatform platformAnnotation,
                                            Platform platform) {
        if (platformAnnotation == null) {
            return true;
        }
        boolean include = platformAnnotation.include();
        Platform[] platforms = platformAnnotation.values();
        if (include) {
            for (Platform tmp : platforms) {
                if (tmp == platform) {
                    return true;
                }
            }
            return false;
        } else {
            for (Platform tmp : platforms) {
                if (tmp == platform) {
                    return false;
                }
            }
            return true;
        }
    }

    private static boolean needSerialzation(MobileVersion versionAnnotation,
                                            String version) {
        boolean needSerialization = true;
        if (versionAnnotation != null && version != null) {
            String[] vTmp = versionAnnotation.values();
            String minVersion = null, maxVersion = null;
            if (vTmp.length > 0) {
                minVersion = vTmp[0];
            }
            if (vTmp.length > 1) {
                maxVersion = vTmp[1];
            }
            if (minVersion != null) {
                int compareToMin = VersionUtil.compare(version, minVersion);
                if (compareToMin < 0) {
                    needSerialization = false;
                } else if (maxVersion != null) {
                    int compareToMax = VersionUtil.compare(version, maxVersion);
                    if (compareToMax > 0) {
                        needSerialization = false;
                    }
                }
            }
            if (versionAnnotation.negated()) {
                return !needSerialization;
            }
        }
        return needSerialization;
    }

    public static void writeNull(DataOutputStream output) throws IOException {
        output.write(MobileBinaryProtocol.BIN_NULL_VALUE);
    }

    public static void writeBoolean(DataOutputStream output, boolean value)
            throws IOException {
        if (value) {
            output.write(MobileBinaryProtocol.BIN_BOOLEAN_TRUE);
        } else {
            output.write(MobileBinaryProtocol.BIN_BOOLEAN_FALSE);
        }
    }

    public static void writeInt(DataOutputStream output, int value)
            throws IOException {
        output.write(MobileBinaryProtocol.BIN_INTEGER_TYPE);
        output.writeInt(value);
    }

    public static void writeLong(DataOutputStream output, long value)
            throws IOException {
        output.write(MobileBinaryProtocol.BIN_LONG_TYPE);
        output.writeLong(value);
    }

    public static void writeDouble(DataOutputStream output, double value)
            throws IOException {
        output.write((byte) (MobileBinaryProtocol.BIN_DOUBLE_TYPE));
        output.writeDouble(value);
    }

    public static void writeUTCDate(DataOutputStream output, Date dateTime)
            throws IOException {
        int seconds = (int) (dateTime.getTime() / 1000);
        output.write((byte) (MobileBinaryProtocol.BIN_UTCDATE_TYPE));
        output.writeInt(seconds);
    }

    public static void writeString(DataOutputStream output, String value)
            throws IOException {
        output.write((byte) (MobileBinaryProtocol.BIN_STRING_TYPE));
        byte[] buffer = value.getBytes("utf-8");
        int length = buffer.length;
        output.writeShort(length);
        output.write(buffer, 0, length);
    }

    public static void writeArray(DataOutputStream output, int count)
            throws IOException {
        output.write((byte) (MobileBinaryProtocol.BIN_ARRAY_TYPE));
        output.writeShort(count);
    }

    public static void writeObjectBegin(DataOutputStream output, int objectID)
            throws IOException {
        output.write((byte) (MobileBinaryProtocol.BIN_OBJECT_TYPE_BEGIN));
        output.writeShort(objectID);
    }

    public static void writeObjectMember(DataOutputStream output, int fieldID)
            throws IOException {
        output.write((byte) (MobileBinaryProtocol.BIN_OBJECT_MEMBER));
        output.writeShort(fieldID);
    }

    public static void writeObjectEnd(DataOutputStream output)
            throws IOException {
        output.write((byte) (MobileBinaryProtocol.BIN_OBJECT_TYPE_END));
    }

    public void serialize(Object value, ClientType clientType, String v) {
        encoder(value, clientType, v);
    }

    public void close() {
        try {
            this.output.close();
        } catch (IOException e) {
        }
    }

    public void flush() {
        try {
            this.output.flush();
        } catch (IOException e) {
        }
    }

    @SuppressWarnings("unchecked")
    private void encoder(Object object, ClientType clientType, String version) {
        try {
            if (object == null) {
                this.writeNull();
            } else if (object instanceof Boolean) {
                this.writeBoolean((Boolean) object);
            } else if (object instanceof Integer) {
                this.writeInt((Integer) object);
            } else if (object instanceof Long) {
                this.writeLong((Long) object);
            } else if (object instanceof Double) {
                this.writeDouble((Double) object);
            } else if (object instanceof Date) {
                this.writeUTCDate((Date) object);
            } else if (object instanceof String) {
                this.writeString((String) object);
            } else if (object.getClass().isArray()) {
                int length = Array.getLength(object);
                this.writeArray(length);
                Object[] array = new Object[length];
                System.arraycopy(object, 0, array, 0, length);
                for (Object o : array) {
                    this.encoder(o, clientType, version);
                }
            } else if (object instanceof List) {
                List<?> list = (List<?>) object;
                this.writeArray(list.size());
                Iterator<?> iterator = list.iterator();
                while (iterator.hasNext()) {
                    Object obj = iterator.next();
                    this.encoder(obj, clientType, version);
                }
            } else {// Object
                Class<? extends Object> clazz = object.getClass();
                MobileDo classAnnotation = clazz.getAnnotation(MobileDo.class);
                if (classAnnotation != null) {
                    String classAnnotationName = classAnnotation.name();
                    int classId = 0;
                    if (!StringUtils.isEmpty(classAnnotationName)) {
                        classId = ApiUtil.getHash16(classAnnotationName);
                    } else if (classAnnotation.id() != 0) {
                        classId = classAnnotation.id();
                    } else {
                        // 默认去掉Do的class名称
                        classId = ApiUtil.getHash16(getDefaultClassHashName(clazz.getName()));
                    }
                    writeObjectBegin(this.output, classId);
                    MobileField fieldAnnotation = null;
                    MobileVersion versionAnnotation = null;
                    MobilePlatform platformAnnotation = null;
                    List<ClientInfoRule> rules = null;
                    Platform platform = clientType == null ? null : clientType.getPlatform();
                    while (!clazz.equals(Object.class)) {
                        String className = clazz.getName();
                        if (fieldAnnotationCache.containsKey(className)) {
                            Map<Field, Annotation[]> map = fieldAnnotationCache.get(className);
                            for (Field field : map.keySet()) {
                                Annotation[] annotations = map.get(field);
                                fieldAnnotation = (MobileField) annotations[0];
                                versionAnnotation = (MobileVersion) annotations[1];
                                platformAnnotation = (MobilePlatform) annotations[2];
                                rules = ClientRuleUtil.getClientInfoRules(field);
                                encodeObjectMember(object, field, clientType, version, platform, fieldAnnotation, versionAnnotation, platformAnnotation, rules);
                            }
                        } else {
                            Map<Field, Annotation[]> map = new HashMap<Field, Annotation[]>();
                            Field[] fields = clazz.getDeclaredFields();
                            for (Field field : fields) {
                                fieldAnnotation = field.getAnnotation(MobileField.class);
                                if (fieldAnnotation != null) {
                                    versionAnnotation = field.getAnnotation(MobileVersion.class);
                                    platformAnnotation = field.getAnnotation(MobilePlatform.class);
                                    Annotation[] annotations = new Annotation[3];
                                    annotations[0] = fieldAnnotation;
                                    annotations[1] = versionAnnotation;
                                    annotations[2] = platformAnnotation;
                                    map.put(field, annotations);

                                    rules = ClientRuleUtil.getClientInfoRules(field);
                                    encodeObjectMember(object, field, clientType, version, platform, fieldAnnotation, versionAnnotation, platformAnnotation, rules);
                                }
                            }
                            fieldAnnotationCache.put(className, map);
                        }
                        clazz = clazz.getSuperclass();
                    }
                    this.writeObjectEnd();
                }
            }
        } catch (IOException ex) {
        }
    }

    private String getDefaultHashName(String name) {
        String hashname = name;
        if (!StringUtils.isEmpty(hashname)) {
            hashname = Character.toUpperCase(name.charAt(0)) + name.substring(1);
        }
        return hashname;
    }

    private String getDefaultClassHashName(String name) {
        String hashname = name;
        if (name != null && name.length() > 2 && name.endsWith("Do")) {
            hashname = name.substring(1, name.length() - 2);
        }
        return hashname;
    }

    private void encodeObjectMember(Object object, Field field, ClientType clientType, String version, Platform platform,
                                    MobileField fieldAnnotation, MobileVersion versionAnnotation,
                                    MobilePlatform platformAnnotation, List<ClientInfoRule> clientInfoRules) throws IOException {
        if (!needSerialzation(versionAnnotation, version)) {
            return;
        }
        if (!needSerialzation(platformAnnotation, platform)) {
            return;
        }
        if (!ClientInfoRule.validateOr(clientInfoRules, clientType, version)) {
            return;
        }
        String fieldName = fieldAnnotation.name();
        if (!StringUtils.isEmpty(fieldName)) {
            writeObjectMember(this.output, ApiUtil.getHash16(fieldName));
        } else if (fieldAnnotation.key() != 0) {
            writeObjectMember(this.output, fieldAnnotation.key());
        } else {
            // 默认取
            writeObjectMember(this.output, ApiUtil.getHash16(getDefaultHashName(field.getName())));
        }
        fieldAnnotation = null;
        try {
            field.setAccessible(true);
            Object value = field.get(object);
            this.encoder(value, clientType, version);
        } catch (IllegalArgumentException e) {
            log.warn(e);
        } catch (IllegalAccessException e) {
            log.warn(e);
        }
    }

    private void writeNull() throws IOException {
        writeNull(this.output);
    }

    private void writeBoolean(boolean value) throws IOException {
        writeBoolean(this.output, value);
    }

    private void writeInt(int value) throws IOException {
        writeInt(this.output, value);
    }

    private void writeLong(long value) throws IOException {
        writeLong(this.output, value);
    }

    private void writeDouble(double value) throws IOException {
        writeDouble(this.output, value);
    }

    private void writeUTCDate(Date dateTime) throws IOException {
        writeUTCDate(output, dateTime);
    }

    private void writeString(String value) throws IOException {
        writeString(this.output, value);
    }

    private void writeArray(int count) throws IOException {
        writeArray(this.output, count);
    }

    private void writeObjectEnd() throws IOException {
        writeObjectEnd(this.output);
    }
}
