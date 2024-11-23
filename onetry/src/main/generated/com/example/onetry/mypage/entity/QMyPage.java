package com.example.onetry.mypage.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMyPage is a Querydsl query type for MyPage
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMyPage extends EntityPathBase<MyPage> {

    private static final long serialVersionUID = -1928258885L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMyPage myPage = new QMyPage("myPage");

    public final NumberPath<Integer> appliedCompanyCount = createNumber("appliedCompanyCount", Integer.class);

    public final NumberPath<Integer> certificationCount = createNumber("certificationCount", Integer.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final DateTimePath<java.time.LocalDateTime> lastAppliedTime = createDateTime("lastAppliedTime", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> lastCertificationModified = createDateTime("lastCertificationModified", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> lastPreferenceModified = createDateTime("lastPreferenceModified", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> lastResumeModified = createDateTime("lastResumeModified", java.time.LocalDateTime.class);

    public final NumberPath<Integer> portfolioCount = createNumber("portfolioCount", Integer.class);

    public final NumberPath<Integer> resumeCompletionPercentage = createNumber("resumeCompletionPercentage", Integer.class);

    public final com.example.onetry.user.entity.QUser user;

    public final NumberPath<Integer> volunteeringTime = createNumber("volunteeringTime", Integer.class);

    public QMyPage(String variable) {
        this(MyPage.class, forVariable(variable), INITS);
    }

    public QMyPage(Path<? extends MyPage> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMyPage(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMyPage(PathMetadata metadata, PathInits inits) {
        this(MyPage.class, metadata, inits);
    }

    public QMyPage(Class<? extends MyPage> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new com.example.onetry.user.entity.QUser(forProperty("user")) : null;
    }

}

