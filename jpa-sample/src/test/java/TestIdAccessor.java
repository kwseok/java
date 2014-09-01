import io.teamscala.java.core.json.jsonlib.util.JSONUtils;
import io.teamscala.java.jpa.Model;
import io.teamscala.java.jpa.ModelIdAccessor;

import javax.persistence.*;

public class TestIdAccessor {

    public static void main(String[] args) {
        // Single identifier
        SingleIdTestModel singleIdTestModel = new SingleIdTestModel();
        ModelIdAccessor<Long> singleIdTestModelIdAccessor = ModelIdAccessor.get(SingleIdTestModel.class);
        singleIdTestModelIdAccessor.setId(singleIdTestModel, 99l);
        System.out.println(singleIdTestModelIdAccessor.getId(singleIdTestModel));

        // Embedded identifier
        EmbeddedIdTestModel embeddedIdTestModel = new EmbeddedIdTestModel();
        ModelIdAccessor<EmbeddedIdTestModel.Identifier> embeddedIdTestModelIdAccessor = ModelIdAccessor.get(EmbeddedIdTestModel.class);
        embeddedIdTestModelIdAccessor.setId(embeddedIdTestModel, new EmbeddedIdTestModel.Identifier("askdfj", 123));
        System.out.println(JSONUtils.toJSONString(embeddedIdTestModelIdAccessor.getId(embeddedIdTestModel), 2));

        // Multiple identifiers
        MultipleIdTestModel multipleIdTestModel = new MultipleIdTestModel();
        ModelIdAccessor<MultipleIdTestModel.Identifier> multipleIdTestModelIdAccessor = ModelIdAccessor.get(MultipleIdTestModel.class);
        multipleIdTestModelIdAccessor.setId(multipleIdTestModel, new MultipleIdTestModel.Identifier("test1", 1));
        System.out.println(JSONUtils.toJSONString(multipleIdTestModelIdAccessor.getId(multipleIdTestModel), 2));
    }

    @Entity
    public static class SingleIdTestModel extends Model<Long> {
        @Id private Long id;
        private String name;
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
    }

    @Entity
    public static class EmbeddedIdTestModel extends Model<EmbeddedIdTestModel.Identifier> {

        @Embeddable
        public static class Identifier {
            private String id1;
            private Integer id2;

            public Identifier() {}
            public Identifier(String id1, Integer id2) {
                this.id1 = id1;
                this.id2 = id2;
            }

            public String getId1() { return id1; }
            public void setId1(String id1) { this.id1 = id1; }
            public Integer getId2() { return id2; }
            public void setId2(Integer id2) { this.id2 = id2; }
        }

        @EmbeddedId private Identifier id;
        private String name;

        public Identifier getId() { return id; }
        public void setId(Identifier id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
    }

    @Entity
    @IdClass(MultipleIdTestModel.Identifier.class)
    public static class MultipleIdTestModel extends Model<MultipleIdTestModel.Identifier> {
        public static class Identifier {
            private String id1;
            private Integer id2;

            public Identifier() {}
            public Identifier(String id1, Integer id2) {
                this.id1 = id1;
                this.id2 = id2;
            }

            public String getId1() { return id1; }
            public void setId1(String id1) { this.id1 = id1; }
            public Integer getId2() { return id2; }
            public void setId2(Integer id2) { this.id2 = id2; }
        }

        @Id private String id1;
        @Id private Integer id2;
        private String name;

        public String getId1() { return id1; }
        public void setId1(String id1) { this.id1 = id1; }
        public Integer getId2() { return id2; }
        public void setId2(Integer id2) { this.id2 = id2; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
    }

}