#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.helpers;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

import java.io.IOException;

public class StringEnumSerializer extends JsonSerializer<Enum<?>>{

    @Override
    public void serialize(Enum<?> t, JsonGenerator jg, SerializerProvider sp) throws IOException,
            JsonGenerationException {
        jg.writeString(t.name());
    }


}
