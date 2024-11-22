package com.example.onetry.apply.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QApply is a Querydsl query type for Apply
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QApply extends EntityPathBase<Apply> {

    private static final long serialVersionUID = -602035125L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QApply apply = new QApply("apply");

    public final com.example.onetry.common.QBaseTimeEntity _super = new com.example.onetry.common.QBaseTimeEntity(this);

    public final ListPath<String, StringPath> additionalCertification = this.<String, StringPath>createList("additionalCertification", String.class, StringPath.class, PathInits.DIRECT2);

    public final NumberPath<Integer> applicantCount = createNumber("applicantCount", Integer.class);

    public final DatePath<java.time.LocalDate> applicationDeadLine = createDate("applicationDeadLine", java.time.LocalDate.class);

    public final StringPath averageGrade = createString("averageGrade");

    public final NumberPath<Long> companyId = createNumber("companyId", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createDate = _super.createDate;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedDate = _super.lastModifiedDate;

    public final StringPath portfolio = createString("portfolio");

    public final EnumPath<ApplicationState> status = createEnum("status", ApplicationState.class);

    public final com.example.onetry.user.entity.QUser user;

    public final StringPath userGrade = createString("userGrade");

    public final StringPath volunteeringTime = createString("volunteeringTime");

    public QApply(String variable) {
        this(Apply.class, forVariable(variable), INITS);
    }

    public QApply(Path<? extends Apply> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QApply(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QApply(PathMetadata metadata, PathInits inits) {
        this(Apply.class, metadata, inits);
    }

    public QApply(Class<? extends Apply> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new com.example.onetry.user.entity.QUser(forProperty("user")) : null;
    }

}

