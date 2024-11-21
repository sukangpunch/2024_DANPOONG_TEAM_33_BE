package com.onetry.spring.volunteering.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QVolunteering is a Querydsl query type for Volunteering
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QVolunteering extends EntityPathBase<Volunteering> {

    private static final long serialVersionUID = 2083752450L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QVolunteering volunteering = new QVolunteering("volunteering");

    public final StringPath generateFileName = createString("generateFileName");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath time = createString("time");

    public final com.onetry.spring.user.entity.QUser user;

    public final StringPath volunteeringFileName = createString("volunteeringFileName");

    public final StringPath volunteeringPath = createString("volunteeringPath");

    public QVolunteering(String variable) {
        this(Volunteering.class, forVariable(variable), INITS);
    }

    public QVolunteering(Path<? extends Volunteering> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QVolunteering(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QVolunteering(PathMetadata metadata, PathInits inits) {
        this(Volunteering.class, metadata, inits);
    }

    public QVolunteering(Class<? extends Volunteering> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new com.onetry.spring.user.entity.QUser(forProperty("user")) : null;
    }

}

