package de.greenrobot.daogenerator;

import java.util.List;

/**
 * Created by saulhoward on 3/14/14.
 */
public class EnumEntity extends Entity {

    private EntityEnum entityEnum;

    EnumEntity(Schema schema, String enumName, List<EntityEnum.Value> values) {
        this(schema, enumName);

        entityEnum = new EntityEnum(this, enumName, values);
    }

    EnumEntity(Schema schema, String enumName) {
        super(schema, enumName);

        setHasKeepSections(false);
        setActive(false);
        anEnum = true;
    }

    public void setValues(List<EntityEnum.Value> values) {
        entityEnum = new EntityEnum(this, getClassName(), values);
    }

    public EntityEnum getEntityEnum() {
        return entityEnum;
    }

    @Override
    public EntityEnum addEnum(String enumName, List<EntityEnum.Value> values) {
        throw new UnsupportedOperationException("Cannot add an enum to an enum!");
    }

    @Override
    void init3ndPass() {
    }

    @Override
    void init2ndPass() {
        init2nPassNamesWithDefaults();
    }

    @Override
    public Property.PropertyBuilder addProperty(PropertyType propertyType, String propertyName) {
        throw new UnsupportedOperationException("Adding properties is not supported for enums!");
    }

    @Override
    public Property.PropertyBuilder addIdProperty() {
        throw new UnsupportedOperationException("Adding properties is not supported for enums!");
    }

    @Override
    public ToMany addToMany(Entity target, Property targetProperty) {
        throw new UnsupportedOperationException("Adding relationships is not supported for enums!");
    }

    @Override
    public ToMany addToMany(Property[] sourceProperties, Entity target, Property[] targetProperties) {
        throw new UnsupportedOperationException("Adding relationships is not supported for enums!");
    }

    @Override
    public ToOne addToOne(Entity target, Property fkProperty) {
        throw new UnsupportedOperationException("Adding relationships is not supported for enums!");
    }

    @Override
    public ToOne addToOne(Entity target, Property fkProperty, String name) {
        throw new UnsupportedOperationException("Adding relationships is not supported for enums!");
    }

    @Override
    public ToOne addToOneWithoutProperty(String name, Entity target, String fkColumnName, boolean notNull, boolean unique) {
        throw new UnsupportedOperationException("Adding relationships is not supported for enums!");
    }

    @Override
    protected void addIncomingToMany(ToMany toMany) {
        throw new UnsupportedOperationException("Adding relationships is not supported for enums!");
    }
}
