package com.simple.planetx;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.simple.planetx.bo.MapJson;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PlanetXApplicationTests {

    @Test
    void contextLoads() {
        String s = "{\n" +
                "    \"obj\": {\n" +
                "        \"1\": 4, \"2\": 9, \"3\": 3, \"4\": 2, \"5\": 3, \"6\": 1, \"7\": 1, \"8\": 0, \"9\": 4, \"10\": 1, \"11\": 1, \"12\": 0\n" +
                "    },\n" +
                "    \"conf\": {\n" +
                "        \"A\": {\n" +
                "            \"title\": [2,3],\n" +
                "            \"body\": {\n" +
                "                \"type\": \"confClue1PlusXAdjacentY\",    \n" +
                "                \"X\": 2,    \n" +
                "                \"Y\": 3\n" +
                "            }\n" +
                "        },\n" +
                "        \"B\": {\n" +
                "            \"title\": [1,2],\n" +
                "            \"body\": {\n" +
                "                \"type\": \"confClueNoXWithin1Y\",    \n" +
                "                \"X\": 1,    \n" +
                "                \"Y\": 2\n" +
                "            }\n" +
                "        },\n" +
                "        \"C\": {\n" +
                "            \"title\": [4],\n" +
                "            \"body\": {\n" +
                "                \"type\": \"confClueAllXWithinN\",    \n" +
                "                \"X\": 4,    \n" +
                "                \"Y\": null,    \n" +
                "                \"N\": 5\n" +
                "            }\n" +
                "        },\n" +
                "        \"D\": {\n" +
                "            \"title\": [4,2],\n" +
                "            \"body\": {\n" +
                "                \"type\": \"confClueNoXOppositeY\",    \n" +
                "                \"X\": 4,    \n" +
                "                \"Y\": 2\n" +
                "            }\n" +
                "        },\n" +
                "        \"E\": {\n" +
                "            \"title\": [1,3],\n" +
                "            \"body\": {\n" +
                "                \"type\": \"confClue1PlusXOppositeY\",    \n" +
                "                \"X\": 1,    \n" +
                "                \"Y\": 3\n" +
                "            }\n" +
                "        },\n" +
                "        \"F\": {\n" +
                "            \"title\": [4,3],\n" +
                "            \"body\": {\n" +
                "                \"type\": \"confClueNoXWithin1Y\",    \n" +
                "                \"X\": 4,    \n" +
                "                \"Y\": 3\n" +
                "            }\n" +
                "        },\n" +
                "        \"X1\": {\n" +
                "            \"title\": [9,1],\n" +
                "            \"body\": {\n" +
                "                \"type\": \"confClue9NotOppositeY\",    \n" +
                "                \"X\": 9,    \n" +
                "                \"Y\": 1\n" +
                "            }\n" +
                "        }\n" +
                "    },\n" +
                "    \"info\": {\n" +
                "        \"A\": {\n" +
                "            \"1\": [\n" +
                "                {\"2\": 2},\n" +
                "                {\"3\": 1},\n" +
                "                {\"5\": 2},\n" +
                "                {\"11\": 4}\n" +
                "            ],\n" +
                "            \"2\": [\n" +
                "                {\"6\": 4},\n" +
                "                {\"9\": 1},\n" +
                "                {\"10\": 4},\n" +
                "                {\"12\": 2}\n" +
                "            ],\n" +
                "            \"3\": [\n" +
                "                {\"4\": 1},\n" +
                "                {\"7\": 2},\n" +
                "                {\"8\": 4},\n" +
                "                {\"11\": 3}\n" +
                "            ]\n" +
                "        },\n" +
                "        \"B\": {\n" +
                "            \"1\": [\n" +
                "                {\"2\": 2},\n" +
                "                {\"3\": 2},\n" +
                "                {\"5\": 1},\n" +
                "                {\"11\": 4}\n" +
                "            ],\n" +
                "            \"2\": [\n" +
                "                {\"1\": 1},\n" +
                "                {\"6\": 4},\n" +
                "                {\"8\": 4},\n" +
                "                {\"12\": 2}\n" +
                "            ],\n" +
                "            \"3\": [\n" +
                "                {\"2\": 3},\n" +
                "                {\"7\": 2},\n" +
                "                {\"9\": 1},\n" +
                "                {\"10\": 4}\n" +
                "            ]\n" +
                "        },\n" +
                "        \"C\": {\n" +
                "            \"1\": [\n" +
                "                {\"2\": 2},\n" +
                "                {\"3\": 1},\n" +
                "                {\"7\": 2},\n" +
                "                {\"11\": 4}\n" +
                "            ],\n" +
                "            \"2\": [\n" +
                "                {\"8\": 4},\n" +
                "                {\"9\": 2},\n" +
                "                {\"10\": 4},\n" +
                "                {\"12\": 1}\n" +
                "            ],\n" +
                "            \"3\": [\n" +
                "                {\"2\": 3},\n" +
                "                {\"4\": 1},\n" +
                "                {\"5\": 2},\n" +
                "                {\"6\": 4}\n" +
                "            ]\n" +
                "        },\n" +
                "        \"D\": {\n" +
                "            \"1\": [\n" +
                "                {\"3\": 1},\n" +
                "                {\"5\": 2},\n" +
                "                {\"7\": 4},\n" +
                "                {\"11\": 2}\n" +
                "            ],\n" +
                "            \"2\": [\n" +
                "                {\"8\": 4},\n" +
                "                {\"9\": 1},\n" +
                "                {\"10\": 2},\n" +
                "                {\"12\": 4}\n" +
                "            ],\n" +
                "            \"3\": [\n" +
                "                {\"1\": 2},\n" +
                "                {\"2\": 3},\n" +
                "                {\"2\": 4},\n" +
                "                {\"4\": 1}\n" +
                "            ]\n" +
                "        },\n" +
                "        \"E\": {\n" +
                "            \"1\": [\n" +
                "                {\"3\": 1},\n" +
                "                {\"5\": 2},\n" +
                "                {\"7\": 4},\n" +
                "                {\"11\": 2}\n" +
                "            ],\n" +
                "            \"2\": [\n" +
                "                {\"6\": 4},\n" +
                "                {\"9\": 2},\n" +
                "                {\"10\": 4},\n" +
                "                {\"12\": 1}\n" +
                "            ],\n" +
                "            \"3\": [\n" +
                "                {\"1\": 2},\n" +
                "                {\"2\": 3},\n" +
                "                {\"4\": 1},\n" +
                "                {\"8\": 4}\n" +
                "            ]\n" +
                "        }\n" +
                "    }\n" +
                "}";

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            MapJson mapJson = objectMapper.readValue(s, MapJson.class);
            System.out.println();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
