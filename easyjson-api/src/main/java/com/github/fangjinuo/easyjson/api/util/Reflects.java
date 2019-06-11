package com.github.fangjinuo.easyjson.api.util;

import java.lang.annotation.Annotation;

public class Reflects {
    public static Annotation getDeclaredAnnotation(Class clazz, Class<? extends Annotation> annotationClazz){
        Annotation[] annotations = clazz.getDeclaredAnnotations();
        for(Annotation anno : annotations){
            if(annotationClazz.isInstance(anno)){
                return anno;
            }
        }
        return null;
    }
}
