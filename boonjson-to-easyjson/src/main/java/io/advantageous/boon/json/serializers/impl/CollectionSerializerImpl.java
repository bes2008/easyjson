/*
 * Copyright 2013-2014 Richard M. Hightower
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  		http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * __________                              _____          __   .__
 * \______   \ ____   ____   ____   /\    /     \ _____  |  | _|__| ____    ____
 *  |    |  _//  _ \ /  _ \ /    \  \/   /  \ /  \\__  \ |  |/ /  |/    \  / ___\
 *  |    |   (  <_> |  <_> )   |  \ /\  /    Y    \/ __ \|    <|  |   |  \/ /_/  >
 *  |______  /\____/ \____/|___|  / \/  \____|__  (____  /__|_ \__|___|  /\___  /
 *         \/                   \/              \/     \/     \/       \//_____/
 *      ____.                     ___________   _____    ______________.___.
 *     |    |____ ___  _______    \_   _____/  /  _  \  /   _____/\__  |   |
 *     |    \__  \\  \/ /\__  \    |    __)_  /  /_\  \ \_____  \  /   |   |
 * /\__|    |/ __ \\   /  / __ \_  |        \/    |    \/        \ \____   |
 * \________(____  /\_/  (____  / /_______  /\____|__  /_______  / / ______|
 *               \/           \/          \/         \/        \/  \/
 */

package io.advantageous.boon.json.serializers.impl;

import io.advantageous.boon.json.serializers.ArraySerializer;
import io.advantageous.boon.json.serializers.CollectionSerializer;
import io.advantageous.boon.json.serializers.JsonSerializerInternal;
import io.advantageous.boon.primitive.CharBuf;

import java.lang.reflect.Array;
import java.util.Collection;

public class CollectionSerializerImpl implements CollectionSerializer, ArraySerializer {

    private static final char [] EMPTY_LIST_CHARS = {'[', ']'};


    @Override
    public final void serializeCollection ( JsonSerializerInternal serializer, Collection<?> collection, CharBuf builder ) {
        if ( collection.size () == 0 ) {
            builder.addChars ( EMPTY_LIST_CHARS );
            return;
        }

        builder.addChar( '[' );
        for ( Object o : collection ) {
            if (o == null) {
                builder.addNull();
                builder.addChar ( ',' );
                continue;
            }
            serializer.serializeObject(o, builder);
            builder.addChar ( ',' );
        }
        builder.removeLastChar ();
        builder.addChar( ']' );

    }

    @Override
    public void serializeArray ( JsonSerializerInternal serializer, Object array, CharBuf builder )  {
        if ( Array.getLength ( array ) == 0 ) {
            builder.addChars ( EMPTY_LIST_CHARS );
            return;
        }

        builder.addChar( '[' );
        final int length = Array.getLength ( array );
        for ( int index = 0; index < length; index++ ) {
            final Object o = Array.get(array, index);
            if (o == null) {
                builder.addNull();
                builder.addChar ( ',' );
                continue;
            }
            serializer.serializeObject ( o, builder );
            builder.addChar ( ',' );
        }
        builder.removeLastChar ();
        builder.addChar( ']' );

    }
}
