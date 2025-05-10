package ru.yandex.practicum.filmorate.annotation;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.io.IOException;

public class MpaDeserializer extends JsonDeserializer<Mpa> {

    @Override
    public Mpa deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);

        Mpa mpa = new Mpa();

        if (node.isObject() && node.has("id")) {
            mpa.setId(node.get("id").asLong());

            if (node.has("name")) {
                mpa.setName(node.get("name").asText());
            } else {
                setNameById(mpa);
            }
        } else if (node.isTextual()) {
            String mpaName = node.asText();
            mpa.setName(mpaName);
            setIdByName(mpa);
        } else if (node.isNumber()) {
            mpa.setId(node.asLong());
            setNameById(mpa);
        }

        return mpa;
    }

    private void setNameById(Mpa mpa) {
        if (mpa.getId() != null) {
            switch (mpa.getId().intValue()) {
                case 1:
                    mpa.setName("G");
                    break;
                case 2:
                    mpa.setName("PG");
                    break;
                case 3:
                    mpa.setName("PG-13");
                    break;
                case 4:
                    mpa.setName("R");
                    break;
                case 5:
                    mpa.setName("NC-17");
                    break;
                default:
                    break;
            }
        }
    }

    private void setIdByName(Mpa mpa) {
        if (mpa.getName() != null) {
            switch (mpa.getName()) {
                case "G":
                    mpa.setId(1L);
                    break;
                case "PG":
                    mpa.setId(2L);
                    break;
                case "PG_13":
                    mpa.setId(3L);
                    break;
                case "R":
                    mpa.setId(4L);
                    break;
                case "NC_17":
                    mpa.setId(5L);
                    break;
                default:
                    break;
            }
        }
    }
}