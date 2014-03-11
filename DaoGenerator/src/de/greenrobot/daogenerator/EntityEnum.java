package de.greenrobot.daogenerator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by saulhoward on 3/10/14.
 */
public class EntityEnum {

    private final String enumName;
    private final List<Value> values;

    private final Entity entity;

    public EntityEnum(Entity entity, String enumName, List<Value> values) {

        if (enumName == null) throw new NullPointerException("name");
        if (values == null) throw new NullPointerException("values");
        if (entity == null) throw new NullPointerException("entity");
        this.enumName = enumName;
        this.values = Collections.unmodifiableList(new ArrayList<Value>(values));
        this.entity = entity;
    }

    public String getEnumName() { return enumName; }

    public List<Value> getValues() {
        return values;
    }

    public Entity getEntity() { return entity; }

    @Override
    public boolean equals(Object other) {
        if (other instanceof EntityEnum) {
            EntityEnum that = (EntityEnum) other;
            return enumName.equals(that.enumName) //
                    && values.equals(that.values) //
                    && entity.equals(that.entity);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return enumName.hashCode();
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(enumName);
        result.append(entity.toString());
        for (Value value : values) {
            result.append("\n  ").append(value);
        }
        return result.toString();
    }

    /**
     * An enum constant.
     */
    public static final class Value {
        private final String name;
        private final int tag;

        public Value(String name, int tag) {
            if (name == null) throw new NullPointerException("name");
            this.name = name.toUpperCase();
            this.tag = tag;
        }

        public String getName() {
            return name;
        }

        public int getTag() {
            return tag;
        }

        @Override
        public boolean equals(Object other) {
            if (other instanceof Value) {
                Value that = (Value) other;
                return name.equals(that.name) //
                        && tag == that.tag;
            }
            return false;
        }

        @Override
        public int hashCode() {
            return name.hashCode();
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder(String.format("%s = %d", name, tag));
            return sb.toString();
        }
    }
}
