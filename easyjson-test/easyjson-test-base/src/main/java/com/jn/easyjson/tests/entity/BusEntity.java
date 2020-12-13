package com.jn.easyjson.tests.entity;

import java.sql.Timestamp;

public class BusEntity {
    private String id;
    private String name;
    private long createTime;
    private Timestamp updateTime;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BusEntity busEntity = (BusEntity) o;

        if (createTime != busEntity.createTime) return false;
        if (id != null ? !id.equals(busEntity.id) : busEntity.id != null) return false;
        if (name != null ? !name.equals(busEntity.name) : busEntity.name != null) return false;
        return updateTime != null ? updateTime.equals(busEntity.updateTime) : busEntity.updateTime == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (int) (createTime ^ (createTime >>> 32));
        result = 31 * result + (updateTime != null ? updateTime.hashCode() : 0);
        return result;
    }
}
