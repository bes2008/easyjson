/*
 * Copyright 2019 the original author or authors.
 *
 * Licensed under the LGPL, Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at  http://www.gnu.org/licenses/lgpl-3.0.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.sf.json.util;


import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collection;

public class EqualsBuilder {
    private boolean isEquals = true;

    public EqualsBuilder() {
    }

    public static boolean reflectionEquals(Object lhs, Object rhs) {
        return reflectionEquals(lhs, rhs, false, (Class) null, (String[]) null);
    }


    public static boolean reflectionEquals(Object lhs, Object rhs, Collection excludeFields) {
        return reflectionEquals(lhs, rhs, ReflectionToStringBuilder.toNoNullStringArray(excludeFields));
    }

    public static boolean reflectionEquals(Object lhs, Object rhs, String[] excludeFields) {
        return reflectionEquals(lhs, rhs, false, (Class) null, excludeFields);
    }

    public static boolean reflectionEquals(Object lhs, Object rhs, boolean testTransients) {
        return reflectionEquals(lhs, rhs, testTransients, (Class) null, (String[]) null);
    }

    public static boolean reflectionEquals(Object lhs, Object rhs, boolean testTransients, Class reflectUpToClass) {
        return reflectionEquals(lhs, rhs, testTransients, reflectUpToClass, (String[]) null);
    }

    public static boolean reflectionEquals(Object lhs, Object rhs, boolean testTransients, Class reflectUpToClass, String[] excludeFields) {
        if (lhs == rhs) {
            return true;
        } else if (lhs != null && rhs != null) {
            Class lhsClass = lhs.getClass();
            Class rhsClass = rhs.getClass();
            Class testClass;
            if (lhsClass.isInstance(rhs)) {
                testClass = lhsClass;
                if (!rhsClass.isInstance(lhs)) {
                    testClass = rhsClass;
                }
            } else {
                if (!rhsClass.isInstance(lhs)) {
                    return false;
                }

                testClass = rhsClass;
                if (!lhsClass.isInstance(rhs)) {
                    testClass = lhsClass;
                }
            }

            EqualsBuilder equalsBuilder = new EqualsBuilder();

            try {
                reflectionAppend(lhs, rhs, testClass, equalsBuilder, testTransients, excludeFields);

                while (testClass.getSuperclass() != null && testClass != reflectUpToClass) {
                    testClass = testClass.getSuperclass();
                    reflectionAppend(lhs, rhs, testClass, equalsBuilder, testTransients, excludeFields);
                }
            } catch (IllegalArgumentException var10) {
                return false;
            }

            return equalsBuilder.isEquals();
        } else {
            return false;
        }
    }

    private static void reflectionAppend(Object lhs, Object rhs, Class clazz, EqualsBuilder builder, boolean useTransients, String[] excludeFields) {
        Field[] fields = clazz.getDeclaredFields();
        AccessibleObject.setAccessible(fields, true);

        for (int i = 0; i < fields.length && builder.isEquals; ++i) {
            Field f = fields[i];
            if (!Arrays.asList(excludeFields).contains(f.getName()) && f.getName().indexOf(36) == -1 && (useTransients || !Modifier.isTransient(f.getModifiers())) && !Modifier.isStatic(f.getModifiers())) {
                try {
                    builder.append(f.get(lhs), f.get(rhs));
                } catch (IllegalAccessException var10) {
                    throw new InternalError("Unexpected IllegalAccessException");
                }
            }
        }

    }

    public EqualsBuilder appendSuper(boolean superEquals) {
        if (!this.isEquals) {
            return this;
        } else {
            this.isEquals = superEquals;
            return this;
        }
    }

    public EqualsBuilder append(Object lhs, Object rhs) {
        if (!this.isEquals) {
            return this;
        } else if (lhs == rhs) {
            return this;
        } else if (lhs != null && rhs != null) {
            Class lhsClass = lhs.getClass();
            if (!lhsClass.isArray()) {
                this.isEquals = lhs.equals(rhs);
            } else if (lhs.getClass() != rhs.getClass()) {
                this.setEquals(false);
            } else if (lhs instanceof long[]) {
                this.append((long[]) ((long[]) lhs), (long[]) ((long[]) rhs));
            } else if (lhs instanceof int[]) {
                this.append((int[]) ((int[]) lhs), (int[]) ((int[]) rhs));
            } else if (lhs instanceof short[]) {
                this.append((short[]) ((short[]) lhs), (short[]) ((short[]) rhs));
            } else if (lhs instanceof char[]) {
                this.append((char[]) ((char[]) lhs), (char[]) ((char[]) rhs));
            } else if (lhs instanceof byte[]) {
                this.append((byte[]) ((byte[]) lhs), (byte[]) ((byte[]) rhs));
            } else if (lhs instanceof double[]) {
                this.append((double[]) ((double[]) lhs), (double[]) ((double[]) rhs));
            } else if (lhs instanceof float[]) {
                this.append((float[]) ((float[]) lhs), (float[]) ((float[]) rhs));
            } else if (lhs instanceof boolean[]) {
                this.append((boolean[]) ((boolean[]) lhs), (boolean[]) ((boolean[]) rhs));
            } else {
                this.append((Object[]) ((Object[]) lhs), (Object[]) ((Object[]) rhs));
            }

            return this;
        } else {
            this.setEquals(false);
            return this;
        }
    }

    public EqualsBuilder append(long lhs, long rhs) {
        if (!this.isEquals) {
            return this;
        } else {
            this.isEquals = lhs == rhs;
            return this;
        }
    }

    public EqualsBuilder append(int lhs, int rhs) {
        if (!this.isEquals) {
            return this;
        } else {
            this.isEquals = lhs == rhs;
            return this;
        }
    }

    public EqualsBuilder append(short lhs, short rhs) {
        if (!this.isEquals) {
            return this;
        } else {
            this.isEquals = lhs == rhs;
            return this;
        }
    }

    public EqualsBuilder append(char lhs, char rhs) {
        if (!this.isEquals) {
            return this;
        } else {
            this.isEquals = lhs == rhs;
            return this;
        }
    }

    public EqualsBuilder append(byte lhs, byte rhs) {
        if (!this.isEquals) {
            return this;
        } else {
            this.isEquals = lhs == rhs;
            return this;
        }
    }

    public EqualsBuilder append(double lhs, double rhs) {
        return !this.isEquals ? this : this.append(Double.doubleToLongBits(lhs), Double.doubleToLongBits(rhs));
    }

    public EqualsBuilder append(float lhs, float rhs) {
        return !this.isEquals ? this : this.append(Float.floatToIntBits(lhs), Float.floatToIntBits(rhs));
    }

    public EqualsBuilder append(boolean lhs, boolean rhs) {
        if (!this.isEquals) {
            return this;
        } else {
            this.isEquals = lhs == rhs;
            return this;
        }
    }

    public EqualsBuilder append(Object[] lhs, Object[] rhs) {
        if (!this.isEquals) {
            return this;
        } else if (lhs == rhs) {
            return this;
        } else if (lhs != null && rhs != null) {
            if (lhs.length != rhs.length) {
                this.setEquals(false);
                return this;
            } else {
                for (int i = 0; i < lhs.length && this.isEquals; ++i) {
                    this.append(lhs[i], rhs[i]);
                }

                return this;
            }
        } else {
            this.setEquals(false);
            return this;
        }
    }

    public EqualsBuilder append(long[] lhs, long[] rhs) {
        if (!this.isEquals) {
            return this;
        } else if (lhs == rhs) {
            return this;
        } else if (lhs != null && rhs != null) {
            if (lhs.length != rhs.length) {
                this.setEquals(false);
                return this;
            } else {
                for (int i = 0; i < lhs.length && this.isEquals; ++i) {
                    this.append(lhs[i], rhs[i]);
                }

                return this;
            }
        } else {
            this.setEquals(false);
            return this;
        }
    }

    public EqualsBuilder append(int[] lhs, int[] rhs) {
        if (!this.isEquals) {
            return this;
        } else if (lhs == rhs) {
            return this;
        } else if (lhs != null && rhs != null) {
            if (lhs.length != rhs.length) {
                this.setEquals(false);
                return this;
            } else {
                for (int i = 0; i < lhs.length && this.isEquals; ++i) {
                    this.append(lhs[i], rhs[i]);
                }

                return this;
            }
        } else {
            this.setEquals(false);
            return this;
        }
    }

    public EqualsBuilder append(short[] lhs, short[] rhs) {
        if (!this.isEquals) {
            return this;
        } else if (lhs == rhs) {
            return this;
        } else if (lhs != null && rhs != null) {
            if (lhs.length != rhs.length) {
                this.setEquals(false);
                return this;
            } else {
                for (int i = 0; i < lhs.length && this.isEquals; ++i) {
                    this.append(lhs[i], rhs[i]);
                }

                return this;
            }
        } else {
            this.setEquals(false);
            return this;
        }
    }

    public EqualsBuilder append(char[] lhs, char[] rhs) {
        if (!this.isEquals) {
            return this;
        } else if (lhs == rhs) {
            return this;
        } else if (lhs != null && rhs != null) {
            if (lhs.length != rhs.length) {
                this.setEquals(false);
                return this;
            } else {
                for (int i = 0; i < lhs.length && this.isEquals; ++i) {
                    this.append(lhs[i], rhs[i]);
                }

                return this;
            }
        } else {
            this.setEquals(false);
            return this;
        }
    }

    public EqualsBuilder append(byte[] lhs, byte[] rhs) {
        if (!this.isEquals) {
            return this;
        } else if (lhs == rhs) {
            return this;
        } else if (lhs != null && rhs != null) {
            if (lhs.length != rhs.length) {
                this.setEquals(false);
                return this;
            } else {
                for (int i = 0; i < lhs.length && this.isEquals; ++i) {
                    this.append(lhs[i], rhs[i]);
                }

                return this;
            }
        } else {
            this.setEquals(false);
            return this;
        }
    }

    public EqualsBuilder append(double[] lhs, double[] rhs) {
        if (!this.isEquals) {
            return this;
        } else if (lhs == rhs) {
            return this;
        } else if (lhs != null && rhs != null) {
            if (lhs.length != rhs.length) {
                this.setEquals(false);
                return this;
            } else {
                for (int i = 0; i < lhs.length && this.isEquals; ++i) {
                    this.append(lhs[i], rhs[i]);
                }

                return this;
            }
        } else {
            this.setEquals(false);
            return this;
        }
    }

    public EqualsBuilder append(float[] lhs, float[] rhs) {
        if (!this.isEquals) {
            return this;
        } else if (lhs == rhs) {
            return this;
        } else if (lhs != null && rhs != null) {
            if (lhs.length != rhs.length) {
                this.setEquals(false);
                return this;
            } else {
                for (int i = 0; i < lhs.length && this.isEquals; ++i) {
                    this.append(lhs[i], rhs[i]);
                }

                return this;
            }
        } else {
            this.setEquals(false);
            return this;
        }
    }

    public EqualsBuilder append(boolean[] lhs, boolean[] rhs) {
        if (!this.isEquals) {
            return this;
        } else if (lhs == rhs) {
            return this;
        } else if (lhs != null && rhs != null) {
            if (lhs.length != rhs.length) {
                this.setEquals(false);
                return this;
            } else {
                for (int i = 0; i < lhs.length && this.isEquals; ++i) {
                    this.append(lhs[i], rhs[i]);
                }

                return this;
            }
        } else {
            this.setEquals(false);
            return this;
        }
    }

    public boolean isEquals() {
        return this.isEquals;
    }

    protected void setEquals(boolean isEquals) {
        this.isEquals = isEquals;
    }

    public void reset() {
        this.isEquals = true;
    }
}
