/*
 * Copyright (c) 2008 - 2013 10gen, Inc. <http://10gen.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.mongodb;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;

import static org.mongodb.Fixture.getMongoClient;
import static org.mongodb.Fixture.initialiseCollection;

public class DatabaseTestCase {
    //For ease of use and readability, in this specific case we'll allow protected variables
    //CHECKSTYLE:OFF
    protected static MongoDatabase database;
    protected MongoCollection<Document> collection;
    //CHECKSTYLE:ON

    @BeforeClass
    public static void setupTestSuite() {
        if (database == null) {
            database = getMongoClient().getDatabase("DriverTest-" + System.nanoTime());
            Runtime.getRuntime().addShutdownHook(new ShutdownHook());
        }
    }

    @Before
    public void setUp() throws Exception {
        collection = initialiseCollection(database, getClass().getName());
    }

    @After
    public void tearDown() {
        if (collection != null) {
            collection.tools().drop();
        }
    }

    protected String getDatabaseName() {
        return database.getName();
    }

    protected String getCollectionName() {
        return collection.getName();
    }

    static class ShutdownHook extends Thread {
        @Override
        public void run() {
            if (database != null) {
                database.tools().drop();
                getMongoClient().close();
            }
        }
    }

}