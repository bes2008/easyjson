/*
 * Copyright (C) 2014 Square, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.squareup.moshi.internal;

import com.squareup.moshi.*;

import javax.annotation.Nullable;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.*;

import static com.squareup.moshi.Types.*;

public final class Util {
    public static final Set<Annotation> NO_ANNOTATIONS = Collections.emptySet();
    public static final Type[] EMPTY_TYPE_ARRAY = new Type[]{};

    private Util() {
    }

    public static boolean typesMatch(Type pattern, Type candidate) {
        // TODO: permit raw types (like Set.class) to match non-raw candidates (like Set<Long>).
        return Types.equals(pattern, candidate);
    }

    public static Set<? extends Annotation> jsonAnnotations(AnnotatedElement annotatedElement) {
        return jsonAnnotations(annotatedElement.getAnnotations());
    }

    public static Set<? extends Annotation> jsonAnnotations(Annotation[] annotations) {
        Set<Annotation> result = null;
        for (Annotation annotation : annotations) {
            if (annotation.annotationType().isAnnotationPresent(JsonQualifier.class)) {
                if (result == null) {
                    result = new LinkedHashSet<Annotation>();
                }
                result.add(annotation);
            }
        }
        return result != null ? Collections.unmodifiableSet(result) : Util.NO_ANNOTATIONS;
    }

    public static boolean isAnnotationPresent(
            Set<? extends Annotation> annotations, Class<? extends Annotation> annotationClass) {
        if (annotations.isEmpty()) {
            // Save an iterator in the common case.
            return false;
        }
        for (Annotation annotation : annotations) {
            if (annotation.annotationType() == annotationClass) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns true if {@code annotations} has any annotation whose simple name is Nullable.
     */
    public static boolean hasNullable(Annotation[] annotations) {
        for (Annotation annotation : annotations) {
            if (annotation.annotationType().getSimpleName().equals("Nullable")) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns true if {@code rawType} is built in. We don't reflect on private fields of platform
     * types because they're unspecified and likely to be different on Java vs. Android.
     */
    public static boolean isPlatformType(Class<?> rawType) {
        String name = rawType.getName();
        return name.startsWith("android.")
                || name.startsWith("androidx.")
                || name.startsWith("java.")
                || name.startsWith("javax.")
                || name.startsWith("kotlin.")
                || name.startsWith("scala.");
    }

    /**
     * Throws the cause of {@code e}, wrapping it if it is checked.
     */
    public static RuntimeException rethrowCause(InvocationTargetException e) {
        Throwable cause = e.getTargetException();
        if (cause instanceof RuntimeException) {
            throw (RuntimeException) cause;
        }
        if (cause instanceof Error) {
            throw (Error) cause;
        }
        throw new RuntimeException(cause);
    }

    /**
     * Returns a type that is functionally equal but not necessarily equal according to {@link
     * Object#equals(Object) Object.equals()}.
     */
    public static Type canonicalize(Type type) {
        if (type instanceof Class) {
            Class<?> c = (Class<?>) type;
            return c.isArray() ? new GenericArrayTypeImpl(canonicalize(c.getComponentType())) : c;

        } else if (type instanceof ParameterizedType) {
            if (type instanceof ParameterizedTypeImpl) {
                return type;
            }
            ParameterizedType p = (ParameterizedType) type;
            return new ParameterizedTypeImpl(p.getOwnerType(),
                    p.getRawType(), p.getActualTypeArguments());

        } else if (type instanceof GenericArrayType) {
            if (type instanceof GenericArrayTypeImpl) {
                return type;
            }
            GenericArrayType g = (GenericArrayType) type;
            return new GenericArrayTypeImpl(g.getGenericComponentType());

        } else if (type instanceof WildcardType) {
            if (type instanceof WildcardTypeImpl) {
                return type;
            }
            WildcardType w = (WildcardType) type;
            return new WildcardTypeImpl(w.getUpperBounds(), w.getLowerBounds());

        } else {
            return type; // This type is unsupported!
        }
    }

    public static Type resolve(Type context, Class<?> contextRawType, Type toResolve) {
        // This implementation is made a little more complicated in an attempt to avoid object-creation.
        while (true) {
            if (toResolve instanceof TypeVariable) {
                TypeVariable<?> typeVariable = (TypeVariable<?>) toResolve;
                toResolve = resolveTypeVariable(context, contextRawType, typeVariable);
                if (toResolve == typeVariable) {
                    return toResolve;
                }

            } else if (toResolve instanceof Class && ((Class<?>) toResolve).isArray()) {
                Class<?> original = (Class<?>) toResolve;
                Type componentType = original.getComponentType();
                Type newComponentType = resolve(context, contextRawType, componentType);
                return componentType == newComponentType
                        ? original
                        : arrayOf(newComponentType);

            } else if (toResolve instanceof GenericArrayType) {
                GenericArrayType original = (GenericArrayType) toResolve;
                Type componentType = original.getGenericComponentType();
                Type newComponentType = resolve(context, contextRawType, componentType);
                return componentType == newComponentType
                        ? original
                        : arrayOf(newComponentType);

            } else if (toResolve instanceof ParameterizedType) {
                ParameterizedType original = (ParameterizedType) toResolve;
                Type ownerType = original.getOwnerType();
                Type newOwnerType = resolve(context, contextRawType, ownerType);
                boolean changed = newOwnerType != ownerType;

                Type[] args = original.getActualTypeArguments();
                for (int t = 0, length = args.length; t < length; t++) {
                    Type resolvedTypeArgument = resolve(context, contextRawType, args[t]);
                    if (resolvedTypeArgument != args[t]) {
                        if (!changed) {
                            args = args.clone();
                            changed = true;
                        }
                        args[t] = resolvedTypeArgument;
                    }
                }

                return changed
                        ? new ParameterizedTypeImpl(newOwnerType, original.getRawType(), args)
                        : original;

            } else if (toResolve instanceof WildcardType) {
                WildcardType original = (WildcardType) toResolve;
                Type[] originalLowerBound = original.getLowerBounds();
                Type[] originalUpperBound = original.getUpperBounds();

                if (originalLowerBound.length == 1) {
                    Type lowerBound = resolve(context, contextRawType, originalLowerBound[0]);
                    if (lowerBound != originalLowerBound[0]) {
                        return supertypeOf(lowerBound);
                    }
                } else if (originalUpperBound.length == 1) {
                    Type upperBound = resolve(context, contextRawType, originalUpperBound[0]);
                    if (upperBound != originalUpperBound[0]) {
                        return subtypeOf(upperBound);
                    }
                }
                return original;

            } else {
                return toResolve;
            }
        }
    }

    static Type resolveTypeVariable(Type context, Class<?> contextRawType, TypeVariable<?> unknown) {
        Class<?> declaredByRaw = declaringClassOf(unknown);

        // We can't reduce this further.
        if (declaredByRaw == null) {
            return unknown;
        }

        Type declaredBy = getGenericSupertype(context, contextRawType, declaredByRaw);
        if (declaredBy instanceof ParameterizedType) {
            int index = indexOf(declaredByRaw.getTypeParameters(), unknown);
            return ((ParameterizedType) declaredBy).getActualTypeArguments()[index];
        }

        return unknown;
    }

    /**
     * Returns the generic supertype for {@code supertype}. For example, given a class {@code
     * IntegerSet}, the result for when supertype is {@code Set.class} is {@code Set<Integer>} and the
     * result when the supertype is {@code Collection.class} is {@code Collection<Integer>}.
     */
    public static Type getGenericSupertype(Type context, Class<?> rawType, Class<?> toResolve) {
        if (toResolve == rawType) {
            return context;
        }

        // we skip searching through interfaces if unknown is an interface
        if (toResolve.isInterface()) {
            Class<?>[] interfaces = rawType.getInterfaces();
            for (int i = 0, length = interfaces.length; i < length; i++) {
                if (interfaces[i] == toResolve) {
                    return rawType.getGenericInterfaces()[i];
                } else if (toResolve.isAssignableFrom(interfaces[i])) {
                    return getGenericSupertype(rawType.getGenericInterfaces()[i], interfaces[i], toResolve);
                }
            }
        }

        // check our supertypes
        if (!rawType.isInterface()) {
            while (rawType != Object.class) {
                Class<?> rawSupertype = rawType.getSuperclass();
                if (rawSupertype == toResolve) {
                    return rawType.getGenericSuperclass();
                } else if (toResolve.isAssignableFrom(rawSupertype)) {
                    return getGenericSupertype(rawType.getGenericSuperclass(), rawSupertype, toResolve);
                }
                rawType = rawSupertype;
            }
        }

        // we can't resolve this further
        return toResolve;
    }

    static int hashCodeOrZero(@Nullable Object o) {
        return o != null ? o.hashCode() : 0;
    }

    static String typeToString(Type type) {
        return type instanceof Class ? ((Class<?>) type).getName() : type.toString();
    }

    static int indexOf(Object[] array, Object toFind) {
        for (int i = 0; i < array.length; i++) {
            if (toFind.equals(array[i])) {
                return i;
            }
        }
        throw new NoSuchElementException();
    }

    /**
     * Returns the declaring class of {@code typeVariable}, or {@code null} if it was not declared by
     * a class.
     */
    static @Nullable
    Class<?> declaringClassOf(TypeVariable<?> typeVariable) {
        GenericDeclaration genericDeclaration = typeVariable.getGenericDeclaration();
        return genericDeclaration instanceof Class ? (Class<?>) genericDeclaration : null;
    }

    static void checkNotPrimitive(Type type) {
        if ((type instanceof Class<?>) && ((Class<?>) type).isPrimitive()) {
            throw new IllegalArgumentException("Unexpected primitive " + type + ". Use the boxed type.");
        }
    }

    public static final class ParameterizedTypeImpl implements ParameterizedType {
        private final @Nullable
        Type ownerType;
        private final Type rawType;
        public final Type[] typeArguments;

        public ParameterizedTypeImpl(@Nullable Type ownerType, Type rawType, Type... typeArguments) {
            // Require an owner type if the raw type needs it.
            if (rawType instanceof Class<?>) {
                Class<?> enclosingClass = ((Class<?>) rawType).getEnclosingClass();
                if (ownerType != null) {
                    if (enclosingClass == null || Types.getRawType(ownerType) != enclosingClass) {
                        throw new IllegalArgumentException(
                                "unexpected owner type for " + rawType + ": " + ownerType);
                    }
                } else if (enclosingClass != null) {
                    throw new IllegalArgumentException(
                            "unexpected owner type for " + rawType + ": null");
                }
            }

            this.ownerType = ownerType == null ? null : canonicalize(ownerType);
            this.rawType = canonicalize(rawType);
            this.typeArguments = typeArguments.clone();
            for (int t = 0; t < this.typeArguments.length; t++) {
                if (this.typeArguments[t] == null) {
                    throw new NullPointerException();
                }
                checkNotPrimitive(this.typeArguments[t]);
                this.typeArguments[t] = canonicalize(this.typeArguments[t]);
            }
        }

        @Override
        public Type[] getActualTypeArguments() {
            return typeArguments.clone();
        }

        @Override
        public Type getRawType() {
            return rawType;
        }

        @Override
        public @Nullable
        Type getOwnerType() {
            return ownerType;
        }

        @Override
        public boolean equals(Object other) {
            return other instanceof ParameterizedType
                    && Types.equals(this, (ParameterizedType) other);
        }

        @Override
        public int hashCode() {
            return Arrays.hashCode(typeArguments)
                    ^ rawType.hashCode()
                    ^ hashCodeOrZero(ownerType);
        }

        @Override
        public String toString() {
            StringBuilder result = new StringBuilder(30 * (typeArguments.length + 1));
            result.append(typeToString(rawType));

            if (typeArguments.length == 0) {
                return result.toString();
            }

            result.append("<").append(typeToString(typeArguments[0]));
            for (int i = 1; i < typeArguments.length; i++) {
                result.append(", ").append(typeToString(typeArguments[i]));
            }
            return result.append(">").toString();
        }
    }

    public static final class GenericArrayTypeImpl implements GenericArrayType {
        private final Type componentType;

        public GenericArrayTypeImpl(Type componentType) {
            this.componentType = canonicalize(componentType);
        }

        @Override
        public Type getGenericComponentType() {
            return componentType;
        }

        @Override
        public boolean equals(Object o) {
            return o instanceof GenericArrayType
                    && Types.equals(this, (GenericArrayType) o);
        }

        @Override
        public int hashCode() {
            return componentType.hashCode();
        }

        @Override
        public String toString() {
            return typeToString(componentType) + "[]";
        }
    }

    /**
     * The WildcardType interface supports multiple upper bounds and multiple lower bounds. We only
     * support what the Java 6 language needs - at most one bound. If a lower bound is set, the upper
     * bound must be Object.class.
     */
    public static final class WildcardTypeImpl implements WildcardType {
        private final Type upperBound;
        private final @Nullable
        Type lowerBound;

        public WildcardTypeImpl(Type[] upperBounds, Type[] lowerBounds) {
            if (lowerBounds.length > 1) {
                throw new IllegalArgumentException();
            }
            if (upperBounds.length != 1) {
                throw new IllegalArgumentException();
            }

            if (lowerBounds.length == 1) {
                if (lowerBounds[0] == null) {
                    throw new NullPointerException();
                }
                checkNotPrimitive(lowerBounds[0]);
                if (upperBounds[0] != Object.class) {
                    throw new IllegalArgumentException();
                }
                this.lowerBound = canonicalize(lowerBounds[0]);
                this.upperBound = Object.class;

            } else {
                if (upperBounds[0] == null) {
                    throw new NullPointerException();
                }
                checkNotPrimitive(upperBounds[0]);
                this.lowerBound = null;
                this.upperBound = canonicalize(upperBounds[0]);
            }
        }

        @Override
        public Type[] getUpperBounds() {
            return new Type[]{upperBound};
        }

        @Override
        public Type[] getLowerBounds() {
            return lowerBound != null ? new Type[]{lowerBound} : EMPTY_TYPE_ARRAY;
        }

        @Override
        public boolean equals(Object other) {
            return other instanceof WildcardType
                    && Types.equals(this, (WildcardType) other);
        }

        @Override
        public int hashCode() {
            // This equals Arrays.hashCode(getLowerBounds()) ^ Arrays.hashCode(getUpperBounds()).
            return (lowerBound != null ? 31 + lowerBound.hashCode() : 1)
                    ^ (31 + upperBound.hashCode());
        }

        @Override
        public String toString() {
            if (lowerBound != null) {
                return "? super " + typeToString(lowerBound);
            } else if (upperBound == Object.class) {
                return "?";
            } else {
                return "? extends " + typeToString(upperBound);
            }
        }
    }

    public static String typeAnnotatedWithAnnotations(Type type,
                                                      Set<? extends Annotation> annotations) {
        return type + (annotations.isEmpty() ? " (with no annotations)" : " annotated " + annotations);
    }

    /**
     * Loads the generated JsonAdapter for classes annotated {@link JsonClass}. This works because it
     * uses the same naming conventions as {@code JsonClassCodeGenProcessor}.
     */
    public static @Nullable
    JsonAdapter<?> generatedAdapter(Moshi moshi, Type type,
                                    Class<?> rawType) {
        JsonClass jsonClass = rawType.getAnnotation(JsonClass.class);
        if (jsonClass == null || !jsonClass.generateAdapter()) {
            return null;
        }
        String adapterClassName = rawType.getName().replace("$", "_") + "JsonAdapter";
        try {
            @SuppressWarnings("unchecked") // We generate types to match.
                    Class<? extends JsonAdapter<?>> adapterClass = (Class<? extends JsonAdapter<?>>)
                    Class.forName(adapterClassName, true, rawType.getClassLoader());
            if (type instanceof ParameterizedType) {
                Constructor<? extends JsonAdapter<?>> constructor
                        = adapterClass.getDeclaredConstructor(Moshi.class, Type[].class);
                constructor.setAccessible(true);
                return constructor.newInstance(moshi, ((ParameterizedType) type).getActualTypeArguments())
                        .nullSafe();
            } else {
                Constructor<? extends JsonAdapter<?>> constructor
                        = adapterClass.getDeclaredConstructor(Moshi.class);
                constructor.setAccessible(true);
                return constructor.newInstance(moshi).nullSafe();
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(
                    "Failed to find the generated JsonAdapter class for " + rawType, e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(
                    "Failed to find the generated JsonAdapter constructor for " + rawType, e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(
                    "Failed to access the generated JsonAdapter for " + rawType, e);
        } catch (InstantiationException e) {
            throw new RuntimeException(
                    "Failed to instantiate the generated JsonAdapter for " + rawType, e);
        } catch (InvocationTargetException e) {
            throw rethrowCause(e);
        }
    }
}
