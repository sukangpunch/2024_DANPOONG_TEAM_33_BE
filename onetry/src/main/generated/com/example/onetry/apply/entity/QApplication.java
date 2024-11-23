package com.example.onetry.apply.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QApplication is a Querydsl query type for Application
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QApplication extends EntityPathBase<Application> {

    private static final long serialVersionUID = 565793901L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QApplication application = new QApplication("application");

    public final com.example.onetry.common.QBaseTimeEntity _super = new com.example.onetry.common.QBaseTimeEntity(this);

    public final NumberPath<Integer> applicantCount = createNumber("applicantCount", Integer.class);

    public final DatePath<java.time.LocalDate> applicationDeadLine = createDate("applicationDeadLine", java.time.LocalDate.class);

    public final NumberPath<Long> applyId = createNumber("applyId", Long.class);

    public final StringPath averageGrade = createString("averageGrade");

    public final StringPath companyName = createString("companyName");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createDate = _super.createDate;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedDate = _super.lastModifiedDate;

    public final ListPath<String, StringPath> requiredCertifications = this.<String, StringPath>createList("requiredCertifications", String.class, StringPath.class, PathInits.DIRECT2);

    public final EnumPath<ApplicationState> status = createEnum("status", ApplicationState.class);

    public final StringPath title = createString("title");

    public final com.example.onetry.user.entity.QUser user;

    public final StringPath userGrade = createString("userGrade");

    public QApplication(String variable) {
        this(Application.class, forVariable(variable), INITS);
    }

    public QApplication(Path<? extends Application> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QApplication(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QApplication(PathMetadata metadata, PathInits inits) {
        this(Application.class, metadata, inits);
    }

    public QApplication(Class<? extends Application> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new com.example.onetry.user.entity.QUser(forProperty("user")) : null;
    }

}

