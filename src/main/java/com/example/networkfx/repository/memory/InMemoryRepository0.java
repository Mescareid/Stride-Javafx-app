package com.example.networkfx.repository.memory;

import com.example.networkfx.domain.Entity;
import com.example.networkfx.domain.validators.ValidationException;
import com.example.networkfx.domain.validators.Validator;
import com.example.networkfx.repository.Repository0;
import com.example.networkfx.repository.RepositoryException;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

public class InMemoryRepository0<ID, E extends Entity<ID>> implements Repository0<ID, E> {

    private final Validator<E> validator;
    protected  Map<ID, E> entities;

    public InMemoryRepository0(Validator<E> validator) {
        this.validator = validator;
        entities = new HashMap<ID, E>();
    }

    /**
     *
     * @param Long ID
     *          ID must not be null
     * @return entity - if the ID entity  exists,
     *                otherwise  returns null  - (e.g id does not exist).
     */
    @Override
    public E findOne(ID id) {
        if (id == null)
            throw new IllegalArgumentException("id must be not null");
        return entities.get(id);
    }

    /**
     *
     * @param none
     * @return Iterable<E>
     */
    @Override
    public Iterable<E> findAll() {
        return entities.values();
    }

    /**
     *
     * @param entity
     *         entity must be not null
     * @return null- if the given entity is saved
     *         otherwise returns the entity (id already exists)
     * @throws ValidationException
     *            if the entity is not valid
     * @throws IllegalArgumentException
     *             if the given entity is null.     *
     */
    @Override
    public E save(E entity) throws ValidationException{
        if (entity == null)
            throw new IllegalArgumentException("entity must be not null");
        validator.validate(entity);
        if (entities.get(entity.getId()) != null) {
            return entity;
        } else entities.put(entity.getId(), entity);
        return null;
    }

    /**
     *  removes the entity with the specified id
     * @param id
     *      id must be not null
     * @return the removed entity or null if there is no entity with the given id
     * @throws IllegalArgumentException
     *                   if the given id is null.
     */
    @Override
    public E delete(ID id) throws FileNotFoundException {
        if(id == null)
            throw new IllegalArgumentException("Id must not be null!");

        return entities.remove(id);
    }

    /**
     *
     * @param entity
     *          entity must not be null
     * @return null - if the entity is updated,
     *                otherwise  returns the entity  - (e.g id does not exist).
     * @throws IllegalArgumentException
     *             if the given entity is null.
     * @throws ValidationException
     *             if the entity is not valid.
     */
    @Override
    public E update(E entity) {

        if (entity == null)
            throw new IllegalArgumentException("entity must be not null!");
        validator.validate(entity);

        entities.put(entity.getId(), entity);

        if (entities.get(entity.getId()) != null) {
            entities.put(entity.getId(), entity);
            return null;
        }
        return entity;

    }
    /**
     *
     * @param entity
     *          entity must not be null
     * @return 1 - if the entity is exists,
     *                otherwise  returns 0  - (e.g id does not exist).
     */
    @Override
    public boolean exists(E entity){
        Iterable<E> list = findAll();
        for(E e : list){
            if(e.equals(entity))
                return true;
        }
        return false;
    }

    /**
     *
     * @param none
     * @return Integer - number of entities
     */
    @Override
    public int size(){
        return entities.size();
    }

}
