package com.example.onetry.portfolio.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPortfolio is a Querydsl query type for Portfolio
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPortfolio extends EntityPathBase<Portfolio> {

    private static final long serialVersionUID = -1627438837L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPortfolio portfolio = new QPortfolio("portfolio");

    public final com.example.onetry.common.QBaseTimeEntity _super = new com.example.onetry.common.QBaseTimeEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createDate = _super.createDate;

    public final StringPath generateFileName = createString("generateFileName");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedDate = _super.lastModifiedDate;

    public final StringPath portfolioName = createString("portfolioName");

    public final StringPath portfolioPath = createString("portfolioPath");

    public final com.example.onetry.user.entity.QUser user;

    public QPortfolio(String variable) {
        this(Portfolio.class, forVariable(variable), INITS);
    }

    public QPortfolio(Path<? extends Portfolio> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPortfolio(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPortfolio(PathMetadata metadata, PathInits inits) {
        this(Portfolio.class, metadata, inits);
    }

    public QPortfolio(Class<? extends Portfolio> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new com.example.onetry.user.entity.QUser(forProperty("user")) : null;
    }

}

