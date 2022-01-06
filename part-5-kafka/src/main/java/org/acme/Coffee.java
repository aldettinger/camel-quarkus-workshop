package org.acme;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class Coffee {

    Long id;
    String uid;
    String blend_name;
    String origin;
    String variety;
    String notes;
    String intensifier;

    public Coffee() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getBlend_name() {
        return blend_name;
    }

    public void setBlend_name(String blend_name) {
        this.blend_name = blend_name;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getVariety() {
        return variety;
    }

    public void setVariety(String variety) {
        this.variety = variety;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getIntensifier() {
        return intensifier;
    }

    public void setIntensifier(String intensifier) {
        this.intensifier = intensifier;
    }
}
