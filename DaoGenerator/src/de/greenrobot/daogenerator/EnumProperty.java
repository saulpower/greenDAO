package de.greenrobot.daogenerator;

/**
 * Created by saulhoward on 3/11/14.
 */
public class EnumProperty extends Property {

    private EntityEnum entityEnum;

    public EnumProperty(Schema schema, Entity entity, EntityEnum entityEnum, String propertyName) {
        super(schema, entity, PropertyType.Enum, propertyName);

        this.entityEnum = entityEnum;
    }

    public EntityEnum getEntityEnum() { return entityEnum; }

    @Override
    public String getJavaType() { return entityEnum.getEnumName(); }
}
