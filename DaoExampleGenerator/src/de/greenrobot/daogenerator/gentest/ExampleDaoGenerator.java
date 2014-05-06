/*
 * Copyright (C) 2011 Markus Junginger, greenrobot (http://greenrobot.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.greenrobot.daogenerator.gentest;

import de.greenrobot.daogenerator.*;

import java.util.ArrayList;

/**
 * Generates entities and DAOs for the example project DaoExample.
 * 
 * Run it as a Java application (not Android).
 * 
 * @author Markus
 */
public class ExampleDaoGenerator {

    public static void main(String[] args) throws Exception {
        Schema schema = new Schema(3, "de.greenrobot.daoexample.database");
        schema.enableGreenSync();

        addNote(schema);
        addCustomerOrder(schema);

        new DaoGenerator().generateAll(schema, "/Users/saulhoward/Developer/greenDAO/DaoExample/src");
    }

    private static void addNote(Schema schema) {
        Entity note = schema.addEntity("Notes");
        note.addIdProperty().markTransient();
        note.addStringProperty("name").notNull();
        note.addStringProperty("comment");
        note.setImplementParcelable(true);

        addNoteType(schema, note);
    }

    private static void addNoteType(Schema schema, Entity entity) {
        ArrayList<EntityEnum.Value> values = new ArrayList<EntityEnum.Value>();
        values.add(new EntityEnum.Value("Ultimate", 1));
        values.add(new EntityEnum.Value("Frisbee", 2));
        values.add(new EntityEnum.Value("Rocks", 3));
        EnumEntity enumEntity = schema.addEnumEntity("NoteType", values);

        entity.addEnumProperty(enumEntity.getEntityEnum(), "type");
    }

    private static EntityEnum addOrderType(Entity entity) {
        ArrayList<EntityEnum.Value> values = new ArrayList<EntityEnum.Value>();
        values.add(new EntityEnum.Value("Work", 1));
        values.add(new EntityEnum.Value("Personal", 2));
        values.add(new EntityEnum.Value("Church", 3));

        return entity.addEnum("OrderType", values);
    }

    private static void addCustomerOrder(Schema schema) {
        Entity customer = schema.addEntity("Customers");
        customer.addIdProperty().markTransient();
        customer.addStringProperty("name").notNull();

        EntityEnum entityEnum = addOrderType(customer);

        Entity order = schema.addEntity("Order");
        order.setTableName("ORDERS"); // "ORDER" is a reserved keyword
        order.addEnumProperty(entityEnum, "type");
        order.addIdProperty().markTransient();
        Property orderDate = order.addDateProperty("date").getProperty();
        Property customerId = order.addLongProperty("customerId").notNull().markTransient().getProperty();
        order.addToOne(customer, customerId);

        ToMany customerToOrders = customer.addToMany(order, customerId);
        customerToOrders.setName("orders");
        customerToOrders.orderAsc(orderDate);
    }

}
