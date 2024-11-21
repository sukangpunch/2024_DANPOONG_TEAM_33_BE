package com.onetry.spring.transcript.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QTranscript is a Querydsl query type for Transcript
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTranscript extends EntityPathBase<Transcript> {

    private static final long serialVersionUID = -1387497090L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QTranscript transcript = new QTranscript("transcript");

    public final StringPath generateFileName = createString("generateFileName");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath transcriptName = createString("transcriptName");

    public final StringPath transcriptPath = createString("transcriptPath");

    public final com.onetry.spring.user.entity.QUser user;

    public QTranscript(String variable) {
        this(Transcript.class, forVariable(variable), INITS);
    }

    public QTranscript(Path<? extends Transcript> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QTranscript(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QTranscript(PathMetadata metadata, PathInits inits) {
        this(Transcript.class, metadata, inits);
    }

    public QTranscript(Class<? extends Transcript> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new com.onetry.spring.user.entity.QUser(forProperty("user")) : null;
    }

}

