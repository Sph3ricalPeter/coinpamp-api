package tech.peterj.coinpamp.repositories;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import java.util.List;
import java.util.Objects;

public abstract class GenericDao<T> {

    protected final Class<T> type;

    @PersistenceContext
    protected EntityManager em;

    /**
     * Creates a new DAO
     */
    protected GenericDao(Class<T> type) {
        this.type = type;
    }

    /**
     * Finds a object with given ID
     *
     * @param id ID to search for
     * @return Object for given ID
     */
    public T find(Object id) {
        Objects.requireNonNull(id);
        try {
            return em.find(type, id);
        } catch (RuntimeException ex) {
            throw new PersistenceException("Failed to find an object with " + id, ex);
        }
    }

    /**
     * Returns list of all objects.
     *
     * @return List of all objects.
     */
    public List<T> findAll() {
        try {
            return em.createQuery("SELECT e FROM " + type.getSimpleName() + " e", type).getResultList();
        } catch (RuntimeException ex) {
            throw new PersistenceException("Failed to find all objects!", ex);
        }
    }

    /**
     * Persists a new object.
     *
     * @param entity Object to persist.
     * @return Persisted entity.
     */
    public void persist(T entity) {
        Objects.requireNonNull(entity);
        try {
            em.persist(entity);
        } catch (RuntimeException ex) {
            throw new PersistenceException("Failed to persist an object!", ex);
        }
    }

    /**
     * Persists a list of entities.
     *
     * @param entities Entities to persist.
     */
    public void persistAll(List<T> entities) {
        Objects.requireNonNull(entities);
        try {
            entities.forEach(this::persist);
        } catch (RuntimeException ex) {
            throw new PersistenceException("Failed to persist multiple objects!", ex);
        }
    }

    /**
     * Updates a object in data storage.
     *
     * @param entity Object to update.
     * @return Updated object.
     */
    public T update(T entity) {
        Objects.requireNonNull(entity);
        try {
            return em.merge(entity);
        } catch (RuntimeException ex) {
            throw new PersistenceException("Failed to update an object!", ex);
        }
    }

    /**
     * Removes object from data storage.
     *
     * @param entity Object to remove.
     */
    public void remove(T entity) {
        Objects.requireNonNull(entity);
        try {
            em.remove(entity);
        } catch (RuntimeException ex) {
            throw new PersistenceException("Failed to remove an object!", ex);
        }
    }

    /**
     * Tells weather object with given ID exists.
     *
     * @param id ID to look for.
     * @return TRUE if exists, FALSE otherwise.
     */
    public boolean exists(Object id) {
        return id != null && em.find(type, id) != null;
    }

}
