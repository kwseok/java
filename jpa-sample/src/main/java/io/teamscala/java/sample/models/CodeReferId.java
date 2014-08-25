package io.teamscala.java.sample.models;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;
import static org.apache.commons.lang3.builder.HashCodeBuilder.reflectionHashCode;
import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;

@Embeddable
public class CodeReferId implements Serializable {
    private static final long serialVersionUID = -4939684318040654135L;

    @Column(length = 4)
    private String code;
    @Column(length = 4)
    private String groupCode;

    public CodeReferId() {
    }

    public CodeReferId(String code, String groupCode) {
        this.code = code;
        this.groupCode = groupCode;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }

    @Override
    public boolean equals(Object target) {
        return reflectionEquals(this, target);
    }

    @Override
    public int hashCode() {
        return reflectionHashCode(this);
    }

    @Override
    public String toString() {
        return reflectionToString(this);
    }
}
