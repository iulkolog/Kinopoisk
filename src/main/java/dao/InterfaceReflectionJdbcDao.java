package dao;

import java.util.List;

/**
 * Created by Iulkolog on 17.05.2016.
 */
 public interface InterfaceReflectionJdbcDao< T >{

    /**
    * Вставка объекта в соответствующую таблицу БД
    * @param object объект, поля которого вставляются в запись в таблице
    */
    public void insert(T object) throws DAOException;

    /**
    * Обновление соответствующей записи в таблице БД.
    * @param object объект, поля которого обновляются в записи в таблице;
    * идентификация записи происходит по ключевым полям объекта.
    */
    public void update(T object) throws DAOException;

    /**
    * Удаление объекта из таблицы БД. Идентификация записи происходит
    * по ключевым полям объекта. Остальные поля объекта в методе
    * не используются и могут быть не заполнены.
    * @param key удаляемый объект с заполненными ключевыми полями
    */
    public void deleteByKey(T key) throws DAOException;

    /**
    * Выборка из таблицы БД объекта. Идентификация записи происходит
    * по ключевым полям объекта. Остальные поля объекта в методе
    * не используются и могут быть не заполнены.
    * @param key выбираемый объект с заполненными ключевыми полями
    * @return выбранный из БД объект со всеми заполненными полями
    */
    public T selectByKey(T key) throws DAOException;

    /**
    * Выборка всех объектов из соответствующей таблицы БД;
    * @return список выбранных объектов
    */
    public List< T > selectAll() throws DAOException;
}

