package com.example.onetry.resume.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QExperience is a Querydsl query type for Experience
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QExperience extends EntityPathBase<Experience> {

    private static final long serialVersionUID = -552449220L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QExperience experience = new QExperience("experience");

    public final StringPath companyName = createString("companyName");

    public final StringPath employmentPeriod = createString("employmentPeriod");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath lastPosition = createString("lastPosition");

    public final StringPath responsibilities = createString("responsibilities");

    public final QResume resume;

    public QExperience(String variable) {
        this(Experience.class, forVariable(variable), INITS);
    }

    public QExperience(Path<? extends Experience> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QExperience(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QExperience(PathMetadata metadata, PathInits inits) {
        this(Experience.class, metadata, inits);
    }

    public QExperience(Class<? extends Experience> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.resume = inits.isInitialized("resume") ? new QResume(forProperty("resume"), inits.get("resume")) : null;
    }

}

