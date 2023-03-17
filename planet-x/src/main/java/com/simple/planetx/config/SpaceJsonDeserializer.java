package com.simple.planetx.config;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.simple.planetx.bo.MapJson;
import com.simple.planetx.common.PlanetEnum;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;
import java.util.*;

@JsonComponent
public class SpaceJsonDeserializer extends JsonDeserializer<List<MapJson.Space>> {

    @Override
    public List<MapJson.Space> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
        List<MapJson.Space> result = new ArrayList<>();
        TreeNode treeNode = p.getCodec().readTree(p);
        LinkedHashMap<String, Integer> map = p.getCodec().treeToValue(treeNode, LinkedHashMap.class);
        map.forEach((k,v) -> {
            MapJson.Space space = new MapJson.Space();
            space.setLocation(Integer.valueOf(k));
            space.setPlanet(PlanetEnum.getByCode(v));
            result.add(space);
        });
        return result;
    }
}
