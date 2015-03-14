/*
 * Copyright (C) 2014 The Android Open Source Project
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
package com.example.android.sunshine.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import java.util.HashSet;

public class TestDb extends AndroidTestCase {

    public static final String LOG_TAG = TestDb.class.getSimpleName();

    // Since we want each test to start with a clean slate
    void deleteTheDatabase() {
        mContext.deleteDatabase(WeatherDbHelper.DATABASE_NAME);
    }

    /*
        This function gets called before each test is executed to delete the database.  This makes
        sure that we always have a clean test.
     */
    public void setUp() {
        deleteTheDatabase();
    }

    /*
        Students: Uncomment this test once you've written the code to create the Location
        table.  Note that you will have to have chosen the same column names that I did in
        my solution for this test to compile, so if you haven't yet done that, this is
        a good time to change your column names to match mine.

        Note that this only tests that the Location table has the correct columns, since we
        give you the code for the weather table.  This test does not look at the
     */
    public void testCreateDb() throws Throwable {
        // build a HashSet of all of the table names we wish to look for
        // Note that there will be another table in the DB that stores the
        // Android metadata (db version information)
        final HashSet<String> tableNameHashSet = new HashSet<String>();
        tableNameHashSet.add(WeatherContract.LocationEntry.TABLE_NAME);
        tableNameHashSet.add(WeatherContract.WeatherEntry.TABLE_NAME);

        mContext.deleteDatabase(WeatherDbHelper.DATABASE_NAME);
        SQLiteDatabase db = new WeatherDbHelper(
                this.mContext).getWritableDatabase();
        assertEquals(true, db.isOpen());

        // have we created the tables we want?
        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);

        assertTrue("Error: This means that the database has not been created correctly",
                c.moveToFirst());

        // verify that the tables have been created
        do {
            tableNameHashSet.remove(c.getString(0));
        } while( c.moveToNext() );

        // if this fails, it means that your database doesn't contain both the location entry
        // and weather entry tables
        assertTrue("Error: Your database was created without both the location entry and weather entry tables",
                tableNameHashSet.isEmpty());

        // now, do our tables contain the correct columns?
        c = db.rawQuery("PRAGMA table_info(" + WeatherContract.LocationEntry.TABLE_NAME + ")",
                null);

        assertTrue("Error: This means that we were unable to query the database for table information.",
                c.moveToFirst());

        // Build a HashSet of all of the column names we want to look for
        final HashSet<String> locationColumnHashSet = new HashSet<String>();
        locationColumnHashSet.add(WeatherContract.LocationEntry._ID);
        locationColumnHashSet.add(WeatherContract.LocationEntry.COLUMN_CITY_NAME);
        locationColumnHashSet.add(WeatherContract.LocationEntry.COLUMN_COORD_LAT);
        locationColumnHashSet.add(WeatherContract.LocationEntry.COLUMN_COORD_LONG);
        locationColumnHashSet.add(WeatherContract.LocationEntry.COLUMN_LOCATION_SETTING);

        int columnNameIndex = c.getColumnIndex("name");
        do {
            String columnName = c.getString(columnNameIndex);
            locationColumnHashSet.remove(columnName);
        } while(c.moveToNext());

        // if this fails, it means that your database doesn't contain all of the required location
        // entry columns
        assertTrue("Error: The database doesn't contain all of the required location entry columns",
                locationColumnHashSet.isEmpty());
        db.close();
    }

    /*
        Students:  Here is where you will build code to test that we can insert and query the
        location database.  We've done a lot of work for you.  You'll want to look in TestUtilities
        where you can uncomment out the "createNorthPoleLocationValues" function.  You can
        also make use of the ValidateCurrentRecord function from within TestUtilities.
    */
    public void testLocationTable() {
        insertLocation();
    }

    /*
        Students:  Here is where you will build code to test that we can insert and query the
        database.  We've done a lot of work for you.  You'll want to look in TestUtilities
        where you can use the "createWeatherValues" function.  You can
        also make use of the validateCurrentRecord function from within TestUtilities.
     */
    public void testWeatherTable() {
        // First insert the location, and then use the locationRowId to insert
        // the weather. Make sure to cover as many failure cases as you can.

        // Instead of rewriting all of the code we've already written in testLocationTable
        // we can move this code to insertLocation and then call insertLocation from both
        // tests. Why move it? We need the code to return the ID of the inserted location
        // and our testLocationTable can only return void because it's a test.

        Long locationRowId  = insertLocation();


        // First step: Get reference to writable database
        WeatherDbHelper dbHelper = new WeatherDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Create ContentValues of what you want to insert
        // (you can use the createWeatherValues TestUtilities function if you wish)

        long   TEST_DATE            = 1419033600L;
        double COLUMN_DEGREES       = 1.1;
        double COLUMN_HUMIDITY      = 1.2;
        double COLUMN_PRESSURE      = 1.3;
        double COLUMN_MAX_TEMP      = 75;
        double COLUMN_MIN_TEMP      = 65;
        String COLUMN_SHORT_DESC    ="Asteroids";
        double COLUMN_WIND_SPEED    = 5.5;
        double COLUMN_WEATHER_ID    = 321;

        ContentValues weatherValues = new ContentValues();
        weatherValues.put(WeatherContract.WeatherEntry.COLUMN_LOC_KEY, locationRowId);
        weatherValues.put(WeatherContract.WeatherEntry.COLUMN_DATE, TEST_DATE);
        weatherValues.put(WeatherContract.WeatherEntry.COLUMN_DEGREES, COLUMN_DEGREES);
        weatherValues.put(WeatherContract.WeatherEntry.COLUMN_HUMIDITY, COLUMN_HUMIDITY);
        weatherValues.put(WeatherContract.WeatherEntry.COLUMN_PRESSURE, COLUMN_PRESSURE);
        weatherValues.put(WeatherContract.WeatherEntry.COLUMN_MAX_TEMP, COLUMN_MAX_TEMP);
        weatherValues.put(WeatherContract.WeatherEntry.COLUMN_MIN_TEMP, COLUMN_MIN_TEMP);
        weatherValues.put(WeatherContract.WeatherEntry.COLUMN_SHORT_DESC, COLUMN_SHORT_DESC);
        weatherValues.put(WeatherContract.WeatherEntry.COLUMN_WIND_SPEED, COLUMN_WIND_SPEED);
        weatherValues.put(WeatherContract.WeatherEntry.COLUMN_WEATHER_ID, COLUMN_WEATHER_ID);

        // Insert ContentValues into database and get a row ID back
        long weahterRowId;
        weahterRowId = db.insert(WeatherContract.WeatherEntry.TABLE_NAME, null, weatherValues);
        assertTrue("Error: Failure to insert " +weatherValues+" Location Values", weahterRowId != -1);

        // Query the database and receive a Cursor back

        Cursor c = db.rawQuery("SELECT * FROM " + WeatherContract.WeatherEntry.TABLE_NAME+ " WHERE " + WeatherContract.WeatherEntry._ID +" = " +  weahterRowId, null);

        // Move the cursor to a valid database row
        assertTrue("Error: This means that the database has not been created correctly",c.moveToFirst());

        // Validate data in resulting Cursor with the original ContentValues
        assertEquals(WeatherContract.WeatherEntry.COLUMN_DATE       + " NON MATCHING",c.getLong(c.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_DATE))       , TEST_DATE);
        assertEquals(WeatherContract.WeatherEntry.COLUMN_DEGREES    + " NON MATCHING",c.getDouble(c.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_DEGREES))    , COLUMN_DEGREES);
        assertEquals(WeatherContract.WeatherEntry.COLUMN_HUMIDITY   + " NON MATCHING",c.getDouble(c.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_HUMIDITY))   , COLUMN_HUMIDITY);
        assertEquals(WeatherContract.WeatherEntry.COLUMN_PRESSURE   + " NON MATCHING",c.getDouble(c.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_PRESSURE))   , COLUMN_PRESSURE);
        assertEquals(WeatherContract.WeatherEntry.COLUMN_MAX_TEMP   + " NON MATCHING",c.getDouble(c.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_MAX_TEMP))   , COLUMN_MAX_TEMP);
        assertEquals(WeatherContract.WeatherEntry.COLUMN_MIN_TEMP   + " NON MATCHING",c.getDouble(c.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_MIN_TEMP))   , COLUMN_MIN_TEMP);
        assertEquals(WeatherContract.WeatherEntry.COLUMN_SHORT_DESC + " NON MATCHING",c.getString(c.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_SHORT_DESC)) , COLUMN_SHORT_DESC);
        assertEquals(WeatherContract.WeatherEntry.COLUMN_WIND_SPEED + " NON MATCHING",c.getDouble(c.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_WIND_SPEED)) , COLUMN_WIND_SPEED);

        // (you can use the validateCurrentRecord function in TestUtilities to validate the
        // query if you like)
        assertFalse( "Error: More than one record returned from location query",c.moveToNext() );

        // Finally, close the cursor and database
        c.close();
    }


    /*
        Students: This is a helper method for the testWeatherTable quiz. You can move your
        code from testLocationTable to here so that you can call this code from both
        testWeatherTable and testLocationTable.
     */
    public long insertLocation() {

        // First step: Get reference to writable database

        // insert our test records into the database
        WeatherDbHelper dbHelper = new WeatherDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Create ContentValues of what you want to insert
        // (you can use the createNorthPoleLocationValues if you wish)

        String testLocationSetting = "99705";
        String testCityName = "North Pole";
        double testLatitude = 64.7488;
        double testLongitude = -147.353;

        ContentValues testValues =new ContentValues();
        testValues.put(WeatherContract.LocationEntry.COLUMN_LOCATION_SETTING, testLocationSetting);
        testValues.put(WeatherContract.LocationEntry.COLUMN_CITY_NAME, testCityName);
        testValues.put(WeatherContract.LocationEntry.COLUMN_COORD_LAT, testLatitude);
        testValues.put(WeatherContract.LocationEntry.COLUMN_COORD_LONG,testLongitude);

        // Insert ContentValues into database and get a row ID back
        long locationRowId;
        locationRowId = db.insert(WeatherContract.LocationEntry.TABLE_NAME, null, testValues);

        // Verify we got a row back.
        assertTrue("Error: Failure to insert " +testCityName+" Location Values", locationRowId != -1);

        // Query the database and receive a Cursor back
        Cursor c = db.rawQuery("SELECT * FROM " + WeatherContract.LocationEntry.TABLE_NAME+ " WHERE " + WeatherContract.LocationEntry._ID +" = " +  locationRowId, null);

        // Move the cursor to a valid database row
        assertTrue("Error: This means that the database has not been created correctly",
                c.moveToFirst());

        // Validate data in resulting Cursor with the original ContentValues
        assertEquals(WeatherContract.LocationEntry.COLUMN_LOCATION_SETTING + " NON MATCHING",c.getString(c.getColumnIndex(WeatherContract.LocationEntry.COLUMN_LOCATION_SETTING)),testLocationSetting);
        assertEquals(WeatherContract.LocationEntry.COLUMN_CITY_NAME + " NON MATCHING",c.getString(c.getColumnIndex(WeatherContract.LocationEntry.COLUMN_CITY_NAME)),testCityName);
        assertEquals(WeatherContract.LocationEntry.COLUMN_COORD_LAT + " NON MATCHING",c.getDouble(c.getColumnIndex(WeatherContract.LocationEntry.COLUMN_COORD_LAT)),testLatitude);
        assertEquals(WeatherContract.LocationEntry.COLUMN_COORD_LONG + " NON MATCHING",c.getDouble(c.getColumnIndex(WeatherContract.LocationEntry.COLUMN_COORD_LONG)),testLongitude);

        // (you can use the validateCurrentRecord function in TestUtilities to validate the
        // query if you like)
        assertFalse( "Error: More than one record returned from location query",c.moveToNext() );

        // Finally, close the cursor and database
        c.close();

        return locationRowId;
    }
}
