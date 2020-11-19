package com.jojoldu.blogcode.querydsl.domain.book;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.jojoldu.blogcode.querydsl.domain.book.QBook.book;


/**
 * Created by jojoldu@gmail.com on 30/07/2020
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */

@RequiredArgsConstructor
@Repository
public class BookQueryRepository {
    private final JPAQueryFactory queryFactory;

    @Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
    public Book getBookById (Long bookId) {
        return queryFactory
                .select(book)
                .from(book)
                .where(book.id.eq(bookId))
                .fetchOne();
    }

    @Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
    public String getBookNameById (Long bookId) {
        return queryFactory
                .select(book.name)
                .from(book)
                .where(book.id.eq(bookId))
                .fetchOne();
    }

    @Transactional(readOnly = true)
    public Boolean exist(Long bookId) {
        Integer fetchOne = queryFactory
                .selectOne()
                .from(book)
                .where(book.id.eq(bookId))
                .fetchFirst();

        return fetchOne != null;
    }

    public Boolean existByBookId(Long bookId) {

        return queryFactory.select(queryFactory
                .selectOne()
                .from(book)
                .where(book.id.eq(bookId))
                .fetchAll().exists())
                .fetchOne();

    }

    public List<Book> getBooks (int bookNo, int pageNo) {
        return queryFactory
                .selectFrom(book)
                .where(book.bookNo.eq(bookNo))
                .offset(pageNo)
                .limit(10)
                .fetch();
    }

    public List<BookPageDto> getBookDtos (int bookNo, int pageNo) {
        return queryFactory
                .select(Projections.fields(BookPageDto.class,
                        book.name,
                        book.bookNo,
                        book.id
                ))
                .from(book)
                .where(book.bookNo.eq(bookNo))
                .offset(pageNo)
                .limit(10)
                .fetch();
    }

    public List<BookPageDto> getBookPage (int bookNo, int pageNo) {
        return queryFactory
                .select(Projections.fields(BookPageDto.class,
                        book.name,
//                        Expressions.as(Expressions.constant(pageNo), "pageNo"),
//                        Expressions.constant(pageNo),
//                        Expressions.constantAs(bookNo, book.bookNo)
                        Expressions.asNumber(pageNo).as("pageNo"),
                        Expressions.asNumber(bookNo).as(book.bookNo)
                        ))
                .from(book)
                .where(book.bookNo.eq(bookNo))
                .offset(pageNo)
                .limit(10)
                .fetch();
    }

    /**
     *
     * @param name
     * @return
     */
    public Long getMax(String name) {
        return queryFactory
                .select(book.id.max())
                .from(book)
                .where(book.name.like(name))
                .fetchOne();
    }

    /**
     *
     * @param name
     * @return
     */
    public Long getSortAndLimit(String name) {
        return queryFactory
                .select(book.id)
                .from(book)
                .where(book.name.like(name))
                .orderBy(book.id.desc())
                .limit(1)
                .fetchOne();
    }

}
