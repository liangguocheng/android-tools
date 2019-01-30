package com.lange.support.http.helper;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * 列表解析类适配器的工厂类
 * 必须通过注解@JsonAdapter方式才能优先于默认的CollectionTypeAdapterFactory
 */
public final class ObjectTypeAdapterFactory implements TypeAdapterFactory {

    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
        Type type = typeToken.getType();

        Class<? super T> rawType = typeToken.getRawType();
        if (!Object.class.isAssignableFrom(rawType)) {
            return null;
        }

//        Type elementType = $Gson$Types.getCollectionElementType(type, rawType);
//        TypeAdapter<?> elementTypeAdapter = gson.getAdapter(TypeToken.get(elementType));
//
//        @SuppressWarnings({"unchecked", "rawtypes"}) // create() doesn't define a type parameter
//                TypeAdapter<T> result = new Adapter(gson, elementType, elementTypeAdapter);

//        TypeAdapter<T> adapter = (TypeAdapter<T>) gson.getAdapter(TypeToken.get(type));
        TypeAdapter<T> adapter = new Adapter(gson, type);
        return adapter;
    }

    private static final class Adapter<E> extends TypeAdapter<E> {
        private final TypeAdapter<E> elementTypeAdapter;

        public Adapter(Gson gson, Type type) {
            this.elementTypeAdapter = (TypeAdapter<E>) gson.getAdapter(TypeToken.get(type));
        }

        public void write(JsonWriter out, E obj) throws IOException {
            if (obj == null) {
                out.nullValue();
                return;
            }

            out.beginObject();
            elementTypeAdapter.write(out, obj);
            out.endObject();
        }

        @Override
        public E read(JsonReader in) throws IOException {
            JsonToken token = in.peek();
            switch (token) {

                case BEGIN_OBJECT:
                    E obj = (E) new Object();
                    in.beginObject();
                    obj = elementTypeAdapter.read(in);
                    in.endObject();
                    return obj;
                case STRING:
                    return (E) new Object();

                case NULL:
                    in.nextNull();
                    return null;

                default:
                    throw new IllegalStateException();
            }
        }
    }
}