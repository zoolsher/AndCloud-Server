package com.safecode.andcloud.vo;

/**
 * 实体类，AndroidPermission
 */
public class Permission {

    public int id;
    public String name;
    public String group;
    public String level;
    public String flags;
    public String label;
    public String description;

    public Permission() {

    }

    public Permission(int id, String name, String group, String level,
                      String flags, String label, String description) {
        this.id = id;
        this.name = name;
        this.group = group;
        this.level = level;
        this.flags = flags;
        this.label = label;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getFlags() {
        return flags;
    }

    public void setFlags(String flags) {
        this.flags = flags;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Permission{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", group='" + group + '\'' +
                ", level='" + level + '\'' +
                ", flags='" + flags + '\'' +
                ", label='" + label + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
