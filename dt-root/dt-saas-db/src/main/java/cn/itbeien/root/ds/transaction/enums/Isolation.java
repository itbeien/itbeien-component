package cn.itbeien.root.ds.transaction.enums;

import java.sql.Connection;

/**
 * 事务隔离级别
 * @author itbeien
 * 项目网站：https://www.itbeien.cn
 * 公众号：贝恩聊架构
 * 全网同名，欢迎小伙伴们关注
 * 知识星球专享-多租户平台-SAAS DB组件
 * Copyright© 2025 itbeien
 */
public enum Isolation {
    /**
     * 默认事务隔离级别，具体使用的数据库事务隔离级别由底层决定。
     * @see Connection*/
    DEFAULT(-1),
    /**
     * 脏读
     * <p>脏读又称无效数据的读出，是指在数据库访问中，事务T1将某一值修改，然后事务T2读取该值，此后T1因为某种原因撤销对该值的修改，这就导致了T2所读取到的数据是无效的。
     * <p>脏读就是指当一个事务正在访问数据，并且对数据进行了修改，而这种修改还没有提交到数据库中，这时，另外一个事务也访问这个数据，然后使用了这个数据。
     * 因为这个数据是还没有提交的数据，那么另外一个事务读到的这个数据是脏数据，依据脏数据所做的操作可能是不正确的。
     * @see Connection#TRANSACTION_READ_UNCOMMITTED
     */
    READ_UNCOMMITTED(Connection.TRANSACTION_READ_UNCOMMITTED),
    /**
     * 不可重复读
     * <p>不可重复读，是指在数据库访问中，一个事务范围内两个相同的查询却返回了不同数据。
     * <p>这是由于查询时系统中其他事务修改的提交而引起的。比如事务T1读取某一数据，事务T2读取并修改了该数据，T1为了对读取值进行检验而再次读取该数据，便得到了不同的结果。
     * <p>一种更易理解的说法是：在一个事务内，多次读同一个数据。在这个事务还没有结束时，另一个事务也访问该同一数据。
     * 那么，在第一个事务的两次读数据之间。由于第二个事务的修改，那么第一个事务读到的数据可能不一样，这样就发生了在一个事务内两次读到的数据是不一样的，因此称为不可重复读，即原始读取不可重复。
     * @see Connection#TRANSACTION_READ_COMMITTED
     */
    READ_COMMITTED(Connection.TRANSACTION_READ_COMMITTED),
    /**
     * 可重复读取
     * <p>可重复读(Repeatable Read)，当使用可重复读隔离级别时，在事务执行期间会锁定该事务以任何方式引用的所有行。
     * 因此，如果在同一个事务中发出同一个SELECT语句两次或更多次，那么产生的结果数据集总是相同的。
     * 因此，使用可重复读隔离级别的事务可以多次检索同一行集，并对它们执行任意操作，直到提交或回滚操作终止该事务。
     * @see Connection#TRANSACTION_REPEATABLE_READ
     */
    REPEATABLE_READ(Connection.TRANSACTION_REPEATABLE_READ),
    /**
     * 同步事务
     * <p>提供严格的事务隔离。它要求事务序列化执行，事务只能一个接着一个地执行，但不能并发执行。
     * <p>如果仅仅通过“行级锁”是无法实现事务序列化的，必须通过其他机制保证新插入的数据不会被刚执行查询操作的事务访问到。
     * @see Connection#TRANSACTION_SERIALIZABLE
     */
    SERIALIZABLE(Connection.TRANSACTION_SERIALIZABLE);

    private int value;

    Isolation(final int value) {
        this.value = value;
    }

    protected int value() {
        return this.value;
    }

    public static Isolation valueOf(final int value) {
        switch (value) {
            case -1:
                return Isolation.DEFAULT;
            case Connection.TRANSACTION_READ_UNCOMMITTED:
                return Isolation.READ_UNCOMMITTED;
            case Connection.TRANSACTION_READ_COMMITTED:
                return Isolation.READ_COMMITTED;
            case Connection.TRANSACTION_REPEATABLE_READ:
                return Isolation.REPEATABLE_READ;
            case Connection.TRANSACTION_SERIALIZABLE:
                return Isolation.SERIALIZABLE;
        }
        throw new IllegalStateException(String.format("Connection ISOLATION error level %s.", value));
    }

    public int getValue() {
        return value;
    }
}
