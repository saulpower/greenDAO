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
        Schema schema = new Schema(3, "de.greenrobot.daoexample");

        addNote(schema);
        addCustomerOrder(schema);

        new DaoGenerator().generateAll(schema, "/Users/saulhoward/Developer/greenDAO/DaoExample/src-gen");
    }

    private static void addNote(Schema schema) {
        Entity note = schema.addEntity("Note");
        note.addIdProperty();
        note.addStringProperty("text").notNull();
        note.addStringProperty("comment");
        note.addDateProperty("date");
    }

    private static EntityEnum addOrderType(Entity entity) {
        ArrayList<EntityEnum.Value> values = new ArrayList<EntityEnum.Value>();
        values.add(new EntityEnum.Value("Work", 1));
        values.add(new EntityEnum.Value("Personal", 2));
        values.add(new EntityEnum.Value("Church", 3));

        return entity.addEnum("OrderType", values);
    }

    private static void addCustomerOrder(Schema schema) {
        Entity customer = schema.addEntity("Customer");
        customer.addIdProperty();
        customer.addStringProperty("name").notNull();

        EntityEnum entityEnum = addOrderType(customer);

        Entity order = schema.addEntity("Order");
        order.setTableName("ORDERS"); // "ORDER" is a reserved keyword
        order.addEnumProperty(entityEnum, "type");
        order.addIdProperty();
        Property orderDate = order.addDateProperty("date").getProperty();
        Property customerId = order.addLongProperty("customerId").notNull().getProperty();
        order.addToOne(customer, customerId);

        ToMany customerToOrders = customer.addToMany(order, customerId);
        customerToOrders.setName("orders");
        customerToOrders.orderAsc(orderDate);
    }

}
