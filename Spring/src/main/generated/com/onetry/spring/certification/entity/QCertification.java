package com.onetry.spring.certification.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCertification is a Querydsl query type for Certification
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCertification extends EntityPathBase<Certification> {

    private static final long serialVersionUID = -3801672L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCertification certification = new QCertification("certification");

    public final DatePath<java.time.LocalDate> acquisitionDate = createDate("acquisitionDate", java.time.LocalDate.class);

    public final StringPath certificationName = createString("certificationName");

    public final StringPath certificationPath = createString("certificationPath");

    public final StringPath generateFileName = createString("generateFileName");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath issuingOrganization = createString("issuingOrganization");

    public final com.onetry.spring.user.entity.QUser user;

    public QCertification(String variable) {
        this(Certification.class, forVariable(variable), INITS);
    }

    public QCertification(Path<? extends Certification> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCertification(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCertification(PathMetadata metadata, PathInits inits) {
        this(Certification.class, metadata, inits);
    }

    public QCertification(Class<? extends Certification> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new com.onetry.spring.user.entity.QUser(forProperty("user")) : null;
    }

}

