package ru.yandex.practicum.filmorate.annotation;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import ru.yandex.practicum.filmorate.model.Genre;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class GenresDeserializer extends JsonDeserializer<Set<Genre>> {

    @Override
    public Set<Genre> deserialize(JsonParser p, DeserializationContext ctx) throws IOException {
        Set<Genre> genres = new HashSet<>();
        JsonNode node = p.getCodec().readTree(p);

        if (node.isArray()) {
            for (JsonNode genreNode : node) {
                Genre genre = new Genre();
                if (genreNode.has("id")) {
                    genre.setId(genreNode.get("id").asLong());
                }
                if (genreNode.has("name")) {
                    genre.setName(genreNode.get("name").asText());
                }
                genres.add(genre);
            }
        }

        return genres;
    }

}