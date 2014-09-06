package io.teamscala.java.jpa.json.jsonlib.processors;

import io.teamscala.java.core.json.jsonlib.processors.JsonPreprocessor;
import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;
import org.hibernate.collection.spi.PersistentCollection;

public class PersistentCollectionJsonPreprocessor implements JsonPreprocessor<PersistentCollection> {

    @Override
    public Object process(PersistentCollection target, JsonConfig jsonConfig) {
        if (!target.wasInitialized()) return new JSONArray();
        return JSONArray.fromObject(target, jsonConfig);
    }
}
