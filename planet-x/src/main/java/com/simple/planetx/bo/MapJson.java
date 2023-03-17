package com.simple.planetx.bo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.simple.planetx.common.PlanetEnum;
import com.simple.planetx.config.ClueJsonDeserializer;
import com.simple.planetx.config.SpaceJsonDeserializer;
import lombok.Data;

import java.util.List;
import java.util.Map;

public class MapJson {

    //整个地图的行星布局，key为区域编号，value为行星类型
    @JsonProperty("obj")
    @JsonDeserialize(using = SpaceJsonDeserializer.class)
    private List<Space> spaceMap;

    //研究后可得的线索，key为研究类别，value为研究结果
    @JsonProperty("conf")
    private Map<String, Research> research;

    //开始时的线索获取
    @JsonProperty("info")
    private StartClue startClue;

    public static class Research {

        @JsonProperty("title")
        private Integer[] title;

        @JsonProperty("body")
        private ResearchResult result;

        public static class ResearchResult {

            @JsonProperty("type")
            private String refDescribe;

            @JsonProperty("X")
            private Integer planetA;

            @JsonProperty("Y")
            private Integer planetB;

            @JsonProperty("N")
            private Integer number;
        }
    }

    public static class StartClue {

        @JsonProperty("A")
        @JsonDeserialize(using = ClueJsonDeserializer.class)
        private List<Space> spring;

        @JsonProperty("B")
        @JsonDeserialize(using = ClueJsonDeserializer.class)
        private List<Space> summer;

        @JsonProperty("C")
        @JsonDeserialize(using = ClueJsonDeserializer.class)
        private List<Space> autumn;

        @JsonProperty("D")
        @JsonDeserialize(using = ClueJsonDeserializer.class)
        private List<Space> winter;

        @JsonProperty("E")
        @JsonDeserialize(using = ClueJsonDeserializer.class)
        private List<Space> undefined;
    }

    @Data
    public static class Space {

        private Integer location;

        private PlanetEnum planet;
    }
}
