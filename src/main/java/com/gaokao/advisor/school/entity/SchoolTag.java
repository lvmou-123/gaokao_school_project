package com.gaokao.advisor.school.entity;

public enum SchoolTag {

    PROJECT_985("985"),
    PROJECT_211("211"),
    DOUBLE_FIRST_CLASS("双一流"),
    REGULAR_UNDERGRADUATE("普通本科"),
    REGULAR_VOCATIONAL("普通专科");

    private final String displayName;

    SchoolTag(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
