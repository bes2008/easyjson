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
package com.squareup.moshi.adapters;

import com.squareup.moshi.*;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * A JsonAdapter factory for objects that include type information in the JSON. When decoding JSON
 * Moshi uses this type information to determine which class to decode to. When encoding Moshi uses
 * the object’s class to determine what type information to include.
 * <p>
 * <p>Suppose we have an interface, its implementations, and a class that uses them:
 * <p>
 * <pre> {@code
 *
 *   interface HandOfCards {
 *   }
 *
 *   class BlackjackHand extends HandOfCards {
 *     Card hidden_card;
 *     List<Card> visible_cards;
 *   }
 *
 *   class HoldemHand extends HandOfCards {
 *     Set<Card> hidden_cards;
 *   }
 *
 *   class Player {
 *     String name;
 *     HandOfCards hand;
 *   }
 * }</pre>
 * <p>
 * <p>We want to decode the following JSON into the player model above:
 * <p>
 * <pre> {@code
 *
 *   {
 *     "name": "Jesse",
 *     "hand": {
 *       "hand_type": "blackjack",
 *       "hidden_card": "9D",
 *       "visible_cards": ["8H", "4C"]
 *     }
 *   }
 * }</pre>
 * <p>
 * <p>Left unconfigured, Moshi would incorrectly attempt to decode the hand object to the abstract
 * {@code HandOfCards} interface. We configure it to use the appropriate subtype instead:
 * <p>
 * <pre> {@code
 *
 *   Moshi moshi = new Moshi.Builder()
 *       .add(PolymorphicJsonAdapterFactory.of(HandOfCards.class, "hand_type")
 *           .withSubtype(BlackjackHand.class, "blackjack")
 *           .withSubtype(HoldemHand.class, "holdem"))
 *       .build();
 * }</pre>
 * <p>
 * <p>This class imposes strict requirements on its use:
 * <p>
 * <ul>
 * <li>Base types may be classes or interfaces. You may not use {@code Object.class} as a base
 * type.
 * <li>Subtypes must encode as JSON objects.
 * <li>Type information must be in the encoded object. Each message must have a type label like
 * {@code hand_type} whose value is a string like {@code blackjack} that identifies which type
 * to use.
 * <li>Each type identifier must be unique.
 * </ul>
 * <p>
 * <p>For best performance type information should be the first field in the object. Otherwise Moshi
 * must reprocess the JSON stream once it knows the object's type.
 * <p>
 * <p>If an unknown subtype is encountered when decoding, this will throw a {@link
 * JsonDataException}. If an unknown type is encountered when encoding, this will throw an {@link
 * IllegalArgumentException}.
 */
public final class PolymorphicJsonAdapterFactory<T> implements JsonAdapter.Factory {
    final Class<T> baseType;
    final String labelKey;
    final List<String> labels;
    final List<Type> subtypes;

    PolymorphicJsonAdapterFactory(
            Class<T> baseType, String labelKey, List<String> labels, List<Type> subtypes) {
        this.baseType = baseType;
        this.labelKey = labelKey;
        this.labels = labels;
        this.subtypes = subtypes;
    }

    /**
     * @param baseType The base type for which this factory will create adapters. Cannot be Object.
     * @param labelKey The key in the JSON object whose value determines the type to which to map the
     *                 JSON object.
     */
    public static <T> PolymorphicJsonAdapterFactory<T> of(Class<T> baseType, String labelKey) {
        if (baseType == null) {
            throw new NullPointerException("baseType == null");
        }
        if (labelKey == null) {
            throw new NullPointerException("labelKey == null");
        }
        if (baseType == Object.class) {
            throw new IllegalArgumentException(
                    "The base type must not be Object. Consider using a marker interface.");
        }
        return new PolymorphicJsonAdapterFactory<T>(
                baseType, labelKey, Collections.<String>emptyList(), Collections.<Type>emptyList());
    }

    /**
     * Returns a new factory that decodes instances of {@code subtype}. When an unknown type is found
     * during encoding an {@linkplain IllegalArgumentException} will be thrown. When an unknown label
     * is found during decoding a {@linkplain JsonDataException} will be thrown.
     */
    public PolymorphicJsonAdapterFactory<T> withSubtype(Class<? extends T> subtype, String label) {
        if (subtype == null) {
            throw new NullPointerException("subtype == null");
        }
        if (label == null) {
            throw new NullPointerException("label == null");
        }
        if (labels.contains(label) || subtypes.contains(subtype)) {
            throw new IllegalArgumentException("Subtypes and labels must be unique.");
        }
        List<String> newLabels = new ArrayList<String>(labels);
        newLabels.add(label);
        List<Type> newSubtypes = new ArrayList<Type>(subtypes);
        newSubtypes.add(subtype);
        return new PolymorphicJsonAdapterFactory<T>(baseType, labelKey, newLabels, newSubtypes);
    }

    @Override
    public JsonAdapter<?> create(Type type, Set<? extends Annotation> annotations, Moshi moshi) {
        if (Types.getRawType(type) != baseType || !annotations.isEmpty()) {
            return null;
        }

        List<JsonAdapter<Object>> jsonAdapters = new ArrayList<JsonAdapter<Object>>(subtypes.size());
        for (int i = 0, size = subtypes.size(); i < size; i++) {
            jsonAdapters.add(moshi.adapter(subtypes.get(i)));
        }

        JsonAdapter<Object> objectJsonAdapter = moshi.adapter(Object.class);
        return new PolymorphicJsonAdapter(
                labelKey, labels, subtypes, jsonAdapters, objectJsonAdapter).nullSafe();
    }

    static final class PolymorphicJsonAdapter extends JsonAdapter<Object> {
        final String labelKey;
        final List<String> labels;
        final List<Type> subtypes;
        final List<JsonAdapter<Object>> jsonAdapters;
        final JsonAdapter<Object> objectJsonAdapter;

        /**
         * Single-element options containing the label's key only.
         */
        final JsonReader.Options labelKeyOptions;
        /**
         * Corresponds to subtypes.
         */
        final JsonReader.Options labelOptions;

        PolymorphicJsonAdapter(String labelKey, List<String> labels,
                               List<Type> subtypes, List<JsonAdapter<Object>> jsonAdapters,
                               JsonAdapter<Object> objectJsonAdapter) {
            this.labelKey = labelKey;
            this.labels = labels;
            this.subtypes = subtypes;
            this.jsonAdapters = jsonAdapters;
            this.objectJsonAdapter = objectJsonAdapter;

            this.labelKeyOptions = JsonReader.Options.of(labelKey);
            this.labelOptions = JsonReader.Options.of(labels.toArray(new String[0]));
        }

        @Override
        public Object fromJson(JsonReader reader) throws IOException {
            int labelIndex = labelIndex(reader.peekJson());
            return jsonAdapters.get(labelIndex).fromJson(reader);
        }

        private int labelIndex(JsonReader reader) throws IOException {
            reader.beginObject();
            while (reader.hasNext()) {
                if (reader.selectName(labelKeyOptions) == -1) {
                    reader.skipName();
                    reader.skipValue();
                    continue;
                }

                int labelIndex = reader.selectString(labelOptions);
                if (labelIndex == -1) {
                    throw new JsonDataException("Expected one of "
                            + labels
                            + " for key '"
                            + labelKey
                            + "' but found '"
                            + reader.nextString()
                            + "'. Register a subtype for this label.");
                }
                reader.close();
                return labelIndex;
            }

            throw new JsonDataException("Missing label for " + labelKey);
        }

        @Override
        public void toJson(JsonWriter writer, Object value) throws IOException {
            Class<?> type = value.getClass();
            int labelIndex = subtypes.indexOf(type);
            if (labelIndex == -1) {
                throw new IllegalArgumentException("Expected one of "
                        + subtypes
                        + " but found "
                        + value
                        + ", a "
                        + value.getClass()
                        + ". Register this subtype.");
            }
            JsonAdapter<Object> adapter = jsonAdapters.get(labelIndex);
            writer.beginObject();
            writer.name(labelKey).value(labels.get(labelIndex));
            int flattenToken = writer.beginFlatten();
            adapter.toJson(writer, value);
            writer.endFlatten(flattenToken);
            writer.endObject();
        }

        @Override
        public String toString() {
            return "PolymorphicJsonAdapter(" + labelKey + ")";
        }
    }
}
