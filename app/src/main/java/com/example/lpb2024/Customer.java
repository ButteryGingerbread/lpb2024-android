package com.example.lpb2024;

import android.os.Parcel;
import android.os.Parcelable;

public class Customer implements Parcelable {
    private String name;
    private String condition;

    // Constructors
    public Customer() {
        // Default constructor
    }

    public Customer(String name, String condition) {
        this.name = name;
        this.condition = condition;
    }

    // Getters
    public String getName() {
        return name;
    }

    public String getCondition() {
        return condition;
    }

    // Setters
    public void setName(String name) {
        this.name = name;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    // Override toString to return the customer's name
    @Override
    public String toString() {
        return name;
    }

    // Parcelable implementation
    protected Customer(Parcel in) {
        name = in.readString();
        condition = in.readString();
    }

    public static final Creator<Customer> CREATOR = new Creator<Customer>() {
        @Override
        public Customer createFromParcel(Parcel in) {
            return new Customer(in);
        }

        @Override
        public Customer[] newArray(int size) {
            return new Customer[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(condition);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}

