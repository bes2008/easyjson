package com.jn.easyjson.tests.entity;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Locale;

public class BusEntity {
    private String id;
    private String name;
    private long createTime;
    private Timestamp updateTime;
    private Calendar calendar;
    private byte dd = (byte)33;
    private char a = 'z';
    private Locale.Category oneEnum = Locale.Category.DISPLAY;
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    public Calendar getCalendar() {
        return calendar;
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }

    public byte getDd() {
        return dd;
    }

    public void setDd(byte dd) {
        this.dd = dd;
    }

    public char getA() {
        return a;
    }

    public void setA(char a) {
        this.a = a;
    }

    public Locale.Category getOneEnum() {
        return oneEnum;
    }

    public void setOneEnum(Locale.Category oneEnum) {
        this.oneEnum = oneEnum;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BusEntity busEntity = (BusEntity) o;

        if (createTime != busEntity.createTime) return false;
        if (dd != busEntity.dd) return false;
        if (a != busEntity.a) return false;
        if (!id.equals(busEntity.id)) return false;
        if (!name.equals(busEntity.name)) return false;
        if (!updateTime.equals(busEntity.updateTime)) return false;
        if (!calendar.equals(busEntity.calendar)) return false;
        return oneEnum == busEntity.oneEnum;
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + (int) (createTime ^ (createTime >>> 32));
        result = 31 * result + updateTime.hashCode();
        result = 31 * result + calendar.hashCode();
        result = 31 * result + (int) dd;
        result = 31 * result + (int) a;
        result = 31 * result + oneEnum.hashCode();
        return result;
    }
}
