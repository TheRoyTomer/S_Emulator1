package Server_UTILS;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.*;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Optional;

public class OptionalTypeAdapterFactory implements TypeAdapterFactory {

    @Override
    @SuppressWarnings("unchecked")
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
        if (!Optional.class.isAssignableFrom(type.getRawType())) {
            return null;
        }

        Type innerType = ((ParameterizedType) type.getType()).getActualTypeArguments()[0];
        TypeAdapter<Object> innerAdapter = (TypeAdapter<Object>) gson.getAdapter(TypeToken.get(innerType));

        return (TypeAdapter<T>) new TypeAdapter<Optional<?>>() {
            @Override
            public void write(JsonWriter out, Optional<?> value) throws IOException {
                if (value != null && value.isPresent()) {
                    innerAdapter.write(out, value.get());
                } else {
                    out.nullValue();
                }
            }

            @Override
            public Optional<?> read(JsonReader in) throws IOException {
                if (in.peek() == JsonToken.NULL) {
                    in.nextNull();
                    return Optional.empty();
                }
                return Optional.of(innerAdapter.read(in));
            }
        };
    }
}