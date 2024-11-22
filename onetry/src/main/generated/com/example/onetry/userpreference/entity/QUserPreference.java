package com.example.onetry.userpreference.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUserPreference is a Querydsl query type for UserPreference
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUserPreference extends EntityPathBase<UserPreference> {

    private static final long serialVersionUID = -1800891951L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QUserPreference userPreference = new QUserPreference("userPreference");

    public final StringPath career = createString("career");

    public final StringPath educationLevel = createString("educationLevel");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath industryCategory = createString("industryCategory");

    public final StringPath region = createString("region");

    public final StringPath subIndustry = createString("subIndustry");

    public final StringPath subRegion = createString("subRegion");

    public final ListPath<String, StringPath> targetCompanies = this.<String, StringPath>createList("targetCompanies", String.class, StringPath.class, PathInits.DIRECT2);

    public final com.example.onetry.user.entity.QUser user;

    public final ListPath<String, StringPath> workDays = this.<String, StringPath>createList("workDays", String.class, StringPath.class, PathInits.DIRECT2);

    public QUserPreference(String variable) {
        this(UserPreference.class, forVariable(variable), INITS);
    }

    public QUserPreference(Path<? extends UserPreference> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QUserPreference(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QUserPreference(PathMetadata metadata, PathInits inits) {
        this(UserPreference.class, metadata, inits);
    }

    public QUserPreference(Class<? extends UserPreference> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new com.example.onetry.user.entity.QUser(forProperty("user")) : null;
    }

}

